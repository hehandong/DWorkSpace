package com.hhd.multisocket514.business;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.view.ViewDebug.FlagToString;

import com.hhd.multisocket514.utils.CommonUtil;
import com.hhd.multisocket514.utils.LogUtil;
import com.hhd.multisocket514.utils.PreferencesUtils;
import com.hhd.multisocket514.utils.TransformUtil;

public class SearchServer {

	// 缓存的大小
	private byte[] comBuff = new byte[55];
	// 接收的数据
	private byte[] receiveDatas;

	private String serverIP = null;

	// 接收信息的头部
	private byte[] rececom_head;
	// 接收信息的命令
	private byte[] rececom_order;
	// 接收信息的尾部
	private byte[] rececom_end;
	// 接收信息的IP地址位；43,44,45,46
	private byte[] rececom_ip;

	private boolean Flag;

	private DatagramSocket socket;
	// IP地址字段
	private byte ip01;
	private byte ip02;
	private byte ip03;
	private byte ip04;

	private Context context;

	private PreferencesUtils sPUtils;

	public SearchServer(Context context) {

		this.context = context;

		initSendCommand();
		Flag = true;

		sPUtils = new PreferencesUtils(context, "config");

		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 初始化命令
	 */
	public void initSendCommand() {

		System.arraycopy(CommonUtil.com_head, 0, comBuff, 0, 6);
		System.arraycopy(CommonUtil.searchcom11, 0, comBuff, 11, 1);
		System.arraycopy(CommonUtil.com_end, 0, comBuff, 53, 2);
	}

	/**
	 * 发送socket
	 * 
	 * @param Flag
	 * 
	 */
	public void sendSocket() {

		try {
			for (int i = 0; i < 10; i++) {

				// 设置广播

				socket.setBroadcast(true);

				// 要发送的UDP包
				DatagramPacket sendPacket = new DatagramPacket(comBuff,
						comBuff.length,
						InetAddress.getByName(CommonUtil.BROADCASTIP),
						CommonUtil.SERVERPORT);

				socket.setSoTimeout(5000);
				// 发送socket
				socket.send(sendPacket);

				// 接收
				receiveSocket();
				if (Flag == false)
					break;

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
	 * 接收socket
	 * 
	 * @param socket
	 */
	public void receiveSocket() {

		try {
			// 缓存的大小
			byte[] rebuff = new byte[500];
			// 接收的UDP包
			DatagramPacket receivePacket = new DatagramPacket(rebuff,
					rebuff.length);

			// 接收信息
			socket.receive(receivePacket);

			// 接收的数据
			receiveDatas = receivePacket.getData();

			// Log.i(TAG, Arrays.toString(receiveDatas));

			if (TransformUtil.ArrayTo16String(receiveDatas).contains("AA")) {

				ip01 = receiveDatas[43];
				ip02 = receiveDatas[44];
				ip03 = receiveDatas[45];
				ip04 = receiveDatas[46];

				// System.out.println((ip01 & 0xff) + "." + (ip02 & 0xff) + "."
				// + (ip03 & 0xff) + "." + (ip04 & 0xff));

				serverIP = (ip01 & 0xff) + "." + (ip02 & 0xff) + "."
						+ (ip03 & 0xff) + "." + (ip04 & 0xff);

				Flag = false;

				sPUtils.putString(CommonUtil.SERVERIP, serverIP);

				System.out
						.println(sPUtils.getString(CommonUtil.SERVERIP, null));

			}

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 得到发送的信息
	 * 
	 * @return
	 */
	public byte[] getSendDatas() {
		return comBuff;

	}

	/**
	 * 获取接收的信息
	 * 
	 * @return
	 */
	public byte[] getReceiveDatas() {

		return receiveDatas;

	}

	/**
	 * 获取IP地址
	 * 
	 * @return
	 */
	public String getServerIP() {
		return serverIP;
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
