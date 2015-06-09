package com.hhd.multisocket514.business;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.xml.sax.Parser;

import android.R.integer;
import android.content.Context;

import com.hhd.multisocket514.bean.DeviceBean;
import com.hhd.multisocket514.utils.CommonUtil;
import com.hhd.multisocket514.utils.PreferencesUtils;
import com.hhd.multisocket514.utils.TransformUtil;

public class ObtainDevice {

	//服务器的地址
	private String SERVERIP;
	
	private byte[] obtainDeceicecom = new byte[47];
	//接收的数据
	private byte[] receiveDatas;

	private String IPAddress = null;
 

	private boolean Flag;

	private DatagramSocket socket;

	private Context context;
	//保存到工具
	private PreferencesUtils sPUtils;
	private ArrayList<byte[]> arrayListDevice;

	private ArrayList<DeviceBean> deviceBeans2;

	public ObtainDevice(Context context) {

		this.context = context;
		
		Flag = true;

		try {
			socket = new DatagramSocket();

		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		//获取保存工具
		sPUtils = new PreferencesUtils(context, "config");
		SERVERIP = sPUtils.getString(CommonUtil.SERVERIP, null);
		
		
		
		//初始化命令
		initSendCommand();
		//发送socket的标志

	}

	/**
	 * 初始化命令
	 */
	public void initSendCommand() {
		//       -------
		System.arraycopy(CommonUtil.com_head, 0, obtainDeceicecom, 0, 6);
		System.arraycopy(CommonUtil.obtaincom11, 0, obtainDeceicecom, 11, 1);
		System.arraycopy(CommonUtil.com_end, 0, obtainDeceicecom, 45, 2);
	}

	/**
	 * 获取播放设备的信息
	 * 
	 * @return
	 */
	public ArrayList<DeviceBean> getPlayDeviceData() {
		ArrayList<DeviceBean> result = getDeviceData("50");

		return result;

	}

	/**
	 * 获取录音设备的信息
	 * 
	 * @return
	 */
	public ArrayList<DeviceBean> getRecordDeviceData() {
		ArrayList<DeviceBean> result = getDeviceData("52");
		return result;

	}

	/**
	 * 获取服务的信息
	 * 
	 * @return
	 */
	public ArrayList<DeviceBean> getServerDeviceData() {
		ArrayList<DeviceBean> result = getDeviceData("53");
		return result;

	}

	/**
	 * 
	 * 获取设备的信息
	 * @param type  “50”：播放设备，“52”：录像设备，“52”：服务器
	 *            
	 * @return
	 */
	public ArrayList<DeviceBean> getDeviceData(String typeString) {

		// 建立一个devicebean的列表
		ArrayList<DeviceBean> deviceBeans = new ArrayList<DeviceBean>();
		int n = 0;
		// 遍历接收到的数据
		for (int i = 0; i < receiveDatas.length; i++) {
			// 
			if ((String.format("%02X", receiveDatas[i])).equals(typeString)) {

				DeviceBean deviceBean = new DeviceBean();
				
				// String.format("%02X", receiveDatas[i]) 将数据格式化成16进制

				String type = String.format("%02X", receiveDatas[i]);
				// 缓存的类型
				String typetem = null;

				if (type.equals("50")) {
					typetem = "播放设备";
				} else if (type.equals("52")) {
					typetem = "录音设备";
				} else if (type.equals("53")) {
					typetem = "服务器";
				}
				// 保存类型进bean
				deviceBean.setType(typetem);
				//保存到sp
				sPUtils.putString(CommonUtil.DEVICETYIP+i, typetem);
				
				// (receiveDatas[i + 1] & 0xff)  将数据格式化成正整数
				String iP = (receiveDatas[i + 1] & 0xff) + "."
						+ (receiveDatas[i + 2] & 0xff) + "."
						+ (receiveDatas[i + 3] & 0xff) + "."
						+ (receiveDatas[i + 4] & 0xff);
				deviceBean.setIP(iP);
				//保存到sp
				sPUtils.putString(CommonUtil.DEVICETYIP+i, iP);

				String state = String.format("%02X", receiveDatas[i + 5]);
				String statetem = null;
				if (state.equals("01")) {
					statetem = "在线";
				} else if (state.equals("00")) {
					statetem = "离线";
				}

				deviceBean.setState(statetem);
				//保存进sp
				sPUtils.putString(CommonUtil.DEVICESTATE+i, statetem);
				
				n++;
				
				// 添加到deviceBean列表
				deviceBeans.add(deviceBean);

			}

		}
		sPUtils.putInt(CommonUtil.DEVICENUM, n);

		return deviceBeans;

	}



	/**
	 * 
	 * 发送socket
	 * @param Flag
	 *            
	 */
	public void sendSocket() {

		try {
			while (Flag) {

				// 

				socket.setBroadcast(true);

				// 创建UDP包
				DatagramPacket sendPacket = new DatagramPacket(
						obtainDeceicecom, obtainDeceicecom.length,
						InetAddress.getByName(SERVERIP), CommonUtil.SERVERPORT);

				socket.setSoTimeout(500);

				//发送数据
				socket.send(sendPacket);

				receiveSocket();

			}

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * 接收数据
	 * @param socket
	 */
	public void receiveSocket() {

		try {
			// 接收缓存的大小
			byte[] rebuff = new byte[300];
			// UDP包
			DatagramPacket receivePacket = new DatagramPacket(rebuff,
					rebuff.length);

			// 接收信息
			socket.receive(receivePacket);

			// 获取接收到的数据
			receiveDatas = receivePacket.getData();

			// Log.i(TAG, Arrays.toString(receiveDatas));

			if (TransformUtil.ArrayTo16String(receiveDatas).contains("40")) {

				Flag = false;

			}

			deviceBeans2 = getPlayDeviceData();
			for (int i = 0; i < deviceBeans2.size(); i++) {
				System.out.println("类型：" + deviceBeans2.get(i).getType()
						+ "\n" + "IP地址：" + deviceBeans2.get(i).getIP() + "\n"
						+ "状态：" + deviceBeans2.get(i).getState() + "\n"

				);

			}

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * 发送的信息
	 * @return
	 */
	public byte[] getSendDatas() {
		return obtainDeceicecom;

	}

	/**
	 * 接收的信息
	 * 
	 * @return
	 */
	public byte[] getReceiveDatas() {

		return receiveDatas;

	}

	/**
	 *
	 * 获取设备的数量
	 * @return
	 */
	public int getDeviceNumber() {

		return TransformUtil.byteTo10Positivebyte(receiveDatas[33]);

	}

	/**
	 *
	 * 获得IP地址
	 * @return
	 */
	public String getIPAdress() {
		return IPAddress;
	}

	public ArrayList<DeviceBean> getDeviceBeans() {
		return deviceBeans2;
	}

	/**
	 * 关闭socket
	 * 
	 * @param socket
	 */
	public void closeSocket() {
		socket.close();
	}

}
