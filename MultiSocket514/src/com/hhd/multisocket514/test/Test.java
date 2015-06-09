package com.hhd.multisocket514.test;

import java.util.ArrayList;

import com.hhd.multisocket514.QueryDeviceBuffFrag;
import com.hhd.multisocket514.bean.DeviceBean;
import com.hhd.multisocket514.bean.DeviceBuff;
import com.hhd.multisocket514.business.DecodePlayer;
import com.hhd.multisocket514.business.ObtainDevice2;
import com.hhd.multisocket514.business.QueryDeviceBuf2;
import com.hhd.multisocket514.business.QueryPlayStatus;
import com.hhd.multisocket514.business.SearchServer;
import com.hhd.multisocket514.utils.LogUtil;
import com.hhd.multisocket514.utils.SeparateMusicUtil;
import com.hhd.multisocket514.utils.TransformUtil;

import android.net.Uri;
import android.test.AndroidTestCase;

public class Test extends AndroidTestCase {

	public void testSearchServer() throws Exception {

		SearchServer searchServer = new SearchServer(getContext());

		// 发送socket
		searchServer.sendSocket();
		byte[] receiveDatas = searchServer.getReceiveDatas();
		for (int i = 0; i < receiveDatas.length; i++) {
			LogUtil.i("第" + i + "行：" + String.format("%02X", receiveDatas[i]));

		}
		String serverIP = searchServer.getServerIP();

		LogUtil.i("服务器的地址：" + serverIP);

	}

	public void testObtainDevice() throws Exception {

		ObtainDevice2 obtainDevice = new ObtainDevice2(getContext());
		// 初始化
		obtainDevice.initObtainDevice2();

		byte[] sendDatas = obtainDevice.getSendDatas();
		for (int i = 0; i < sendDatas.length; i++) {
			LogUtil.i("发送的数据第" + i + "行：" + String.format("%02X", sendDatas[i]));
		}

		// 发送socket和接收返回的信息
		byte[] receiveDatas = obtainDevice.sendAndReceiveSocket();

		for (int i = 0; i < receiveDatas.length; i++) {
			LogUtil.i("接收的数据第" + i + "行："
					+ String.format("%02X", receiveDatas[i]));
		}

		ArrayList<DeviceBean> deviceDataList = obtainDevice.getPlayDeviceData();

	}

	public void testQueryPlayStatus() throws Exception {

		QueryPlayStatus queryPlayStatus = new QueryPlayStatus(getContext());
		queryPlayStatus.sendSocket();
		byte[] comBuffs = queryPlayStatus.getSendDatas();

		byte[] receiveDatas = queryPlayStatus.getReceiveDatas();
		for (int i = 0; i < receiveDatas.length; i++) {
			LogUtil.i("接收的数据第" + i + "行："
					+ String.format("%02X", receiveDatas[i]));
		}

	}

	/**
	 * 测试查设备的缓存
	 * @throws Exception
	 */
	public void testQueryDeviceBuff() throws Exception {

		QueryDeviceBuf2 queryDeviceBuf2 = new QueryDeviceBuf2(getContext());
		queryDeviceBuf2.initQueryDeviceBuf();
		byte[] sendDatas = queryDeviceBuf2.getSendDatas();

		for (int i = 0; i < sendDatas.length; i++) {
			LogUtil.i("发送的查buff数据第" + i + "行："
					+ String.format("%02X", sendDatas[i]));
		}
		byte[] receiveDatas = queryDeviceBuf2.getReceiveDatas("192.168.1.220");

		for (int i = 0; i < receiveDatas.length; i++) {
			LogUtil.i("接收到的查buff数据第" + i + "行："
					+ String.format("%02X", receiveDatas[i]));
		}
		DeviceBuff deviceBuff = queryDeviceBuf2.getDeviceBuff("192.168.1.220");
		LogUtil.i("设备的IP地址：------" + deviceBuff.getDeviceIP());
		LogUtil.i("设备左声道缓存：------" + deviceBuff.getLeftChannelBuff());
		LogUtil.i("设备右声道缓存：------" + deviceBuff.getRightChannelBuff());

	}

	/**
	 * 测试音乐分离左右声道
	 */
	public void testseparateMusic() {
		byte[] num = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
		ArrayList<byte[]> separate16bitMusic = SeparateMusicUtil
				.separate16bitMusic(num);
		byte[] leftbs = separate16bitMusic.get(0);

		StringBuffer leftsb = new StringBuffer();
		for (int j = 0; j < leftbs.length; j++) {
			leftsb.append(leftbs[j]);

		}
		LogUtil.i("左声道："+leftsb.toString());
		
		byte[] rightbs = separate16bitMusic.get(1);

		StringBuffer rightsb = new StringBuffer();
		for (int j = 0; j < rightbs.length; j++) {
			rightsb.append(rightbs[j]);

		}
		LogUtil.i("右声道："+rightsb.toString());
		

	}
	
	public void testIntToByte(){
		byte[] intToBytes2 = TransformUtil.intToBytes(156);
		for (int i = 0; i < intToBytes2.length; i++) {
			LogUtil.i(intToBytes2[i]+"");
			
		}
	}
	


}
