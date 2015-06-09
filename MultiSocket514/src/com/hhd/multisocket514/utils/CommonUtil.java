package com.hhd.multisocket514.utils;

public class CommonUtil {

	public static String SERVERIP = "serverIP";

	public static String DEVICE = "device";
	public static String DEVICETYPE = "devicetype";
	public static String DEVICETYIP = "devicetyip";
	public static String DEVICESTATE = "devicestate";
	public static String DEVICENUM = "devicenum";
	
	// 服务器的广播地址
	public static final String BROADCASTIP = "255.255.255.255";

	// 服务器的端口
	public static final int SERVERPORT = 9200;

	

	//发送信息的头部
	public final static byte[] com_head = new byte[] { 'X', 'X', 'X', 'C', 'M',
			'D' };

	// 发送信息的结尾
	public final static byte[] com_end = { (byte) 0xFF, (byte) 0xFF };

	//广播查找设备的命令
	public final static byte[] searchcom11 = { (byte) 0xAA };

	// 获取设备的命令
	public final static byte[] obtaincom11 = { (byte) 0x40 };

	// 获取播放信息的命令
	public final static byte[] querycom11 = { (byte) 0x41 };
	
	public final static byte[] QDeviceBufcom11 = { (byte) 0x33 };

}
