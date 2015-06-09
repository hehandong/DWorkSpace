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

import com.hhd.multisocket514.base.BaseSocket;
import com.hhd.multisocket514.bean.DeviceBean;
import com.hhd.multisocket514.utils.CommonUtil;
import com.hhd.multisocket514.utils.LogUtil;
import com.hhd.multisocket514.utils.PreferencesUtils;
import com.hhd.multisocket514.utils.TransformUtil;

public class ObtainDevice2 extends BaseSocket {

	// 服务器的地址
	private String SERVERIP;

	private byte[] obtainDeceicecom;

	// 保存到工具
	private PreferencesUtils sPUtils;

	private ArrayList<byte[]> arrayListDevice;

	private ArrayList<DeviceBean> deviceBeans2;

	public ObtainDevice2(Context context) {
		super(context);
	}

	public void initObtainDevice2() {
		initCommand();
		initSharedPreferences();
	}

	@Override
	protected byte[] initCommand() {
		obtainDeceicecom = new byte[47];
		System.arraycopy(CommonUtil.com_head, 0, obtainDeceicecom, 0, 6);
		System.arraycopy(CommonUtil.obtaincom11, 0, obtainDeceicecom, 11, 1);
		System.arraycopy(CommonUtil.com_end, 0, obtainDeceicecom, 45, 2);
		return obtainDeceicecom;

	}

	@Override
	protected void initSharedPreferences() {

		// 获取保存工具
		sPUtils = new PreferencesUtils(context, "config");
		SERVERIP = sPUtils.getString(CommonUtil.SERVERIP, null);
		if (SERVERIP == null) {
			SERVERIP = "192.168.1.99";
		}

	}

	public byte[] sendAndReceiveSocket() {

		byte[] reDeveiceDatas = sendAndReceiveSocket(obtainDeceicecom,
				SERVERIP, CommonUtil.SERVERPORT, 300, "40");

		return reDeveiceDatas;
	}

	public ArrayList<DeviceBean> getPlayDeviceData() {

		byte[] reDeveiceDatas = sendAndReceiveSocket();

		if (reDeveiceDatas != null) {

			deviceBeans2 = getPlayDeviceData(reDeveiceDatas);
			for (int i = 0; i < deviceBeans2.size(); i++) {

				System.out.println("类型：" + deviceBeans2.get(i).getType() + "\n"
						+ "IP地址：" + deviceBeans2.get(i).getIP() + "\n" + "状态："
						+ deviceBeans2.get(i).getState() + "\n"

				);

			}

		} else {
			deviceBeans2 = null;
		}

		return deviceBeans2;

	}

	/**
	 * 获取播放设备的信息
	 * 
	 * @return
	 */
	public ArrayList<DeviceBean> getPlayDeviceData(byte[] reDeveiceDatas) {
		ArrayList<DeviceBean> result = null;
		if (reDeveiceDatas != null) {

			result = getDeviceData(reDeveiceDatas, "50");
		}

		return result;

	}

	/**
	 * 获取录音设备的信息
	 * 
	 * @return
	 */
	public ArrayList<DeviceBean> getRecordDeviceData(byte[] reDeveiceDatas) {

		ArrayList<DeviceBean> result = null;
		if (reDeveiceDatas != null) {

			result = getDeviceData(reDeveiceDatas, "52");
		}
		return result;

	}

	/**
	 * 获取服务的信息
	 * 
	 * @return
	 */
	public ArrayList<DeviceBean> getServerDeviceData(byte[] reDeveiceDatas) {

		ArrayList<DeviceBean> result = null;
		if (reDeveiceDatas != null) {

			result = getDeviceData(reDeveiceDatas, "53");
		}
		return result;

	}

	/**
	 * 
	 * 获取设备的信息
	 * 
	 * @param type
	 *            “50”：播放设备，“52”：录像设备，“52”：服务器
	 * 
	 * @return
	 */
	public ArrayList<DeviceBean> getDeviceData(byte[] reDeveiceDatas,
			String typeString) {

		// 建立一个devicebean的列表
		ArrayList<DeviceBean> deviceBeans = new ArrayList<DeviceBean>();
		if (deviceBeans != null) {

			int n = 0;
			// 遍历接收到的数据
			for (int i = 0; i < reDeveiceDatas.length; i++) {
				//
				if ((String.format("%02X", reDeveiceDatas[i]))
						.equals(typeString)) {

					DeviceBean deviceBean = new DeviceBean();

					// String.format("%02X", receiveDatas[i]) 将数据格式化成16进制

					String type = String.format("%02X", reDeveiceDatas[i]);
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
					// 保存到sp
					sPUtils.putString(CommonUtil.DEVICETYPE + n, typetem);
					LogUtil.i(CommonUtil.DEVICETYPE + n + "----" + typetem);

					// (receiveDatas[i + 1] & 0xff) 将数据格式化成正整数
					String iP = (reDeveiceDatas[i + 1] & 0xff) + "."
							+ (reDeveiceDatas[i + 2] & 0xff) + "."
							+ (reDeveiceDatas[i + 3] & 0xff) + "."
							+ (reDeveiceDatas[i + 4] & 0xff);
					deviceBean.setIP(iP);
					// 保存到sp
					sPUtils.putString(CommonUtil.DEVICETYIP + n, iP);
					LogUtil.i(CommonUtil.DEVICETYIP + n + "----" + iP);

					String state = String.format("%02X", reDeveiceDatas[i + 5]);
					String statetem = null;
					if (state.equals("01")) {
						statetem = "在线";
					} else if (state.equals("00")) {
						statetem = "离线";
					}

					deviceBean.setState(statetem);
					// 保存进sp
					sPUtils.putString(CommonUtil.DEVICESTATE + n, statetem);
					LogUtil.i(CommonUtil.DEVICESTATE + n + "----" + statetem);

					n++;

					// 添加到deviceBean列表
					deviceBeans.add(deviceBean);

				}

			}

			sPUtils.putInt(CommonUtil.DEVICENUM, n);
			LogUtil.i("设备的总数量:----" + n + "");
		}

		return deviceBeans;

	}

	/**
	 * 
	 * 获取设备的数量
	 * 
	 * @return
	 */
	public int getDeviceNumber() {

		byte[] reDeveiceDatas = sendAndReceiveSocket();

		return TransformUtil.byteTo10Positivebyte(reDeveiceDatas[33]);

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
