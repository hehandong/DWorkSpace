package com.hhd.multisocket514.base;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.R.integer;
import android.content.Context;
import android.os.Bundle;

import com.hhd.multisocket514.utils.CommonUtil;
import com.hhd.multisocket514.utils.PreferencesUtils;
import com.hhd.multisocket514.utils.TransformUtil;

public abstract class BaseSocket {

	protected boolean Flag;

	protected DatagramSocket socket;

	protected Context context;

	private byte[] receiveDatas;

	// protected byte[] receiveDatas;

	public BaseSocket(Context context) {

		this.context = context;

		// 发送命令的标志
		Flag = true;

		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 发送socket
	 * 
	 * @param sendComBuff
	 *            发送的缓存
	 * @param num
	 *            接收缓存的字节数
	 * @param IP
	 *            接收的IP地址
	 * @param port
	 *            接收的端口
	 * @param orderFlag
	 *            发送的主要命令
	 * @return 返回的信息
	 */

	public byte[] sendAndReceiveSocket(byte[] sendComBuff, String IP, int port,
			int num, String orderFlag) {

		try {
			for (int i = 0; i < 10; i++) {

				socket.setBroadcast(true);

				DatagramPacket sendPacket = new DatagramPacket(sendComBuff,
						sendComBuff.length, InetAddress.getByName(IP), port);

				socket.setSoTimeout(500);

				socket.send(sendPacket);
				// receiveSocket(num, orderFlag);

				// 接收的缓存
				byte[] rebuff = new byte[num];

				DatagramPacket receivePacket = new DatagramPacket(rebuff,
						rebuff.length);
				// 接收信息
				socket.receive(receivePacket);

				// receiveDatas = receivePacket.getData();
				receiveDatas = rebuff;

				if (TransformUtil.ArrayTo16String(receiveDatas).contains(
						orderFlag)) {

					Flag = false;

				}

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

		return receiveDatas;

	}

	/**
	 * 发送的信息
	 * 
	 * @return
	 */
	public byte[] getSendDatas() {

		byte[] sendComBuff = initCommand();

		return sendComBuff;

	}

	/**
	 * 关闭socket
	 * 
	 * @param socket
	 */
	public void closeSocket() {
		socket.close();
	}

	// 初始化SharedPreferences
	protected abstract byte[] initCommand();

	// 初始化命令
	protected abstract void initSharedPreferences();

}
