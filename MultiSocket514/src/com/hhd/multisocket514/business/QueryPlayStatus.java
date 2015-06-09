package com.hhd.multisocket514.business;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

import android.R.integer;
import android.content.Context;

import com.hhd.multisocket514.bean.DeviceBean;
import com.hhd.multisocket514.bean.PlayStatus;
import com.hhd.multisocket514.utils.CommonUtil;
import com.hhd.multisocket514.utils.PreferencesUtils;
import com.hhd.multisocket514.utils.TransformUtil;

public class QueryPlayStatus {

	// 服务器的地址
	private String SERVERIP;

	// 缓存大小
	private byte[] querycom = new byte[59];
	// 接收的数据
	private byte[] receiveQueryDatas;

	// 命令的头部
	private byte[] quecom_head;
	// 命令
	private byte[] quecom_order;
	// 命令的结束
	private byte[] quecom_end;

	private boolean Flag;

	private DatagramSocket socket;

	private Context context;

	private PreferencesUtils sPUtils;
	private byte[] receiveDatas;

	public QueryPlayStatus(Context context) {

		this.context = context;

		initSendCommand();
		Flag = true;

		sPUtils = new PreferencesUtils(context, "config");
		SERVERIP = sPUtils.getString(CommonUtil.SERVERIP, null);

		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}

	}

	public ArrayList<PlayStatus> getPlayStatus() {
		receiveDatas = getReceiveDatas();
		ArrayList<PlayStatus> playStatusList = new ArrayList<PlayStatus>();

		for (int i = 34; i < 34 + (receiveDatas[33] * 14); i += 14) {
			PlayStatus playStatus = new PlayStatus();
			// 设备的IP地址
			String iP = (receiveDatas[i] & 0xff) + "."
					+ (receiveDatas[i + 1] & 0xff) + "."
					+ (receiveDatas[i + 2] & 0xff) + "."
					+ (receiveDatas[i + 3] & 0xff);
			playStatus.setIP(iP);
			// 左声道来源的IP地址
			String leftSourceIP = (receiveDatas[i + 4] & 0xff) + "."
					+ (receiveDatas[i + 5] & 0xff) + "."
					+ (receiveDatas[i + 6] & 0xff) + "."
					+ (receiveDatas[i + 7] & 0xff);
			playStatus.setLeftSourceIP(leftSourceIP);

			// 左声道的来源声道
			String leftTrack = String.format("%02X", receiveDatas[i + 8]);
			if (leftTrack.equals("4C") || leftTrack.equals("6C")) {
				playStatus.setLeftTrack("左声道");
			} else if (leftTrack.equals("52") || leftTrack.equals("72")) {
				playStatus.setLeftTrack("右声道");
			}

			// 右声道来源的IP地址
			String rightSourceIP = (receiveDatas[i + 9] & 0xff) + "."
					+ (receiveDatas[i + 10] & 0xff) + "."
					+ (receiveDatas[i + 11] & 0xff) + "."
					+ (receiveDatas[i + 12] & 0xff);

			// 右声道来源的IP地址
			String rightTrack = String.format("%02X", receiveDatas[i + 13]);
			if (leftTrack.equals("4C") || leftTrack.equals("6C")) {
				playStatus.setRightTrack("左声道");
			} else if (leftTrack.equals("52") || leftTrack.equals("72")) {
				playStatus.setRightTrack("左声道");
			}

			playStatusList.add(playStatus);

		}
		return playStatusList;

	}

	/**
	 * 初始化命令
	 */
	public void initSendCommand() {

		System.arraycopy(CommonUtil.com_head, 0, querycom, 0, 6);
		System.arraycopy(CommonUtil.querycom11, 0, querycom, 11, 1);
		System.arraycopy(CommonUtil.com_end, 0, querycom, 57, 2);
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

				// 发送socket

				socket.setBroadcast(true);

				// 要发送的UDP包
				DatagramPacket sendPacket = new DatagramPacket(querycom,
						querycom.length, InetAddress.getByName(SERVERIP),
						CommonUtil.SERVERPORT);

				socket.setSoTimeout(500);
				// 发送socket
				socket.send(sendPacket);

				receiveSocket();
				//如果接受到有指定字符的信息就跳出循环
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
	 * 接收信息
	 * 
	 * @param socket
	 */
	public void receiveSocket() {

		try {
			// 缓存的大小
			byte[] rebuff = new byte[350];
			// UDP包
			DatagramPacket receivePacket = new DatagramPacket(rebuff,
					rebuff.length);

			// 接收socket
			socket.receive(receivePacket);

			receiveDatas = receivePacket.getData();

			if (TransformUtil.ArrayTo16String(receiveDatas).contains("41")) {

				Flag = false;

			}

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 发送的数据
	 * 
	 * @return
	 */
	public byte[] getSendDatas() {
		return querycom;

	}

	/**
	 * 接收的数据
	 * 
	 * @return
	 */
	public byte[] getReceiveDatas() {

		return receiveDatas;

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
