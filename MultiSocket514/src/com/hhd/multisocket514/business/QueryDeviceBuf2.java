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

import android.R.integer;
import android.R.string;
import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.view.ViewDebug.FlagToString;

import com.hhd.multisocket514.base.BaseSocket;
import com.hhd.multisocket514.bean.DeviceBuff;
import com.hhd.multisocket514.utils.CommonUtil;
import com.hhd.multisocket514.utils.LogUtil;
import com.hhd.multisocket514.utils.PreferencesUtils;
import com.hhd.multisocket514.utils.TransformUtil;

public class QueryDeviceBuf2 extends BaseSocket {

	// 缓存的大小
	private byte[] QDeviceBufcom;

	private boolean Flag;

	private DatagramSocket socket;

	private PreferencesUtils sPUtils;

	private int num;

	private ArrayList<DeviceBuff> deviceBuffList;

	/*** 接收到返回的数据 */
	private byte[] reDeviceBufDatas;

	public QueryDeviceBuf2(Context context) {
		super(context);
	}

	public void initQueryDeviceBuf() {
		initCommand();
		initSharedPreferences();
	}

	/**
	 * 初始化命令
	 */
	@Override
	protected byte[] initCommand() {
		QDeviceBufcom = new byte[39];
		System.arraycopy(CommonUtil.com_head, 0, QDeviceBufcom, 0, 6);
		System.arraycopy(CommonUtil.QDeviceBufcom11, 0, QDeviceBufcom, 11, 1);
		System.arraycopy(CommonUtil.com_end, 0, QDeviceBufcom, 37, 2);

		for (int i = 0; i < QDeviceBufcom.length; i++) {
			LogUtil.i("发送的查buff数据第" + i + "行："
					+ String.format("%02X", QDeviceBufcom[i]));
		}

		return QDeviceBufcom;
	}

	@Override
	protected void initSharedPreferences() {

		sPUtils = new PreferencesUtils(context, "config");
		num = sPUtils.getInt(CommonUtil.DEVICENUM, 0);
		LogUtil.i(num + "");
		LogUtil.i("初始化sp成功");

	}

	/**
	 * 获取接收的数据
	 * 
	 * @param DeviceIP
	 * @return
	 */
	public byte[] getReceiveDatas(String DeviceIP) {

		if (DeviceIP != null) {

			reDeviceBufDatas = sendAndReceiveSocket(QDeviceBufcom, DeviceIP,
					CommonUtil.SERVERPORT, 100, "33");
		} else {
			reDeviceBufDatas = null;
		}

		return reDeviceBufDatas;

	}

	/**
	 * 获取设备的缓存
	 * 
	 * @param DeviceIP
	 * @return
	 */
	public DeviceBuff getDeviceBuff(String DeviceIP) {

		DeviceBuff deviceBuff = new DeviceBuff();
		// 放进devicebuff bean
		deviceBuff.setDeviceIP(DeviceIP);
		// 发送socket
		byte[] receiveDatasi = getReceiveDatas(DeviceIP);

		if (receiveDatasi != null) {

			deviceBuff.setReceiveData(receiveDatasi);

			// -------------左声道缓存
			byte[] leftTrackBuff = new byte[4];

			System.arraycopy(receiveDatasi, 42, leftTrackBuff, 3, 1);
			System.arraycopy(receiveDatasi, 43, leftTrackBuff, 2, 1);
			System.arraycopy(receiveDatasi, 44, leftTrackBuff, 1, 1);
			System.arraycopy(receiveDatasi, 45, leftTrackBuff, 0, 1);
			// System.arraycopy(receiveDatasi, 42, leftTrackBuff, 0, 4);

			String leftTrackBufftemp = TransformUtil
					.ArrayTo16String(leftTrackBuff);

			int leftTrackBuffInt = Integer.parseInt(leftTrackBufftemp, 16);
			deviceBuff.setLeftChannelBuff(leftTrackBuffInt);
			LogUtil.i("左声道缓存---uint----" + leftTrackBuffInt);

			// LogUtil.i("左声道缓存---16进制String----" + leftTrackBufftemp);

			// -------------右声道缓存
			byte[] rightTrackBuff = new byte[4];

			System.arraycopy(receiveDatasi, 46, rightTrackBuff, 3, 1);
			System.arraycopy(receiveDatasi, 47, rightTrackBuff, 2, 1);
			System.arraycopy(receiveDatasi, 48, rightTrackBuff, 1, 1);
			System.arraycopy(receiveDatasi, 49, rightTrackBuff, 0, 1);

			// System.arraycopy(receiveDatasi, 46, rightTrackBuff, 0, 4);

			String rightTrackBufftemp = TransformUtil
					.ArrayTo16String(rightTrackBuff);

			int rightTrackBuffInt = Integer.parseInt(rightTrackBufftemp, 16);
			deviceBuff.setRightChannelBuff(rightTrackBuffInt);

			LogUtil.i("右声道缓存---uint----" + rightTrackBuffInt);

			// LogUtil.i("右声道缓存---16进制String----" + rightTrackBufftemp);

			LogUtil.i("获取BUFF成功");

		}
		return deviceBuff;

	}
}
