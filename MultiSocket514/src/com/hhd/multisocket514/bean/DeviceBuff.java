package com.hhd.multisocket514.bean;

public class DeviceBuff {
	
	private String deviceIP;
	private int leftChannelBuff;
	private int rightChannelBuff;
	
	private byte[] receiveData;
	
	public byte[] getReceiveData() {
		return receiveData;
	}
	public void setReceiveData(byte[] receiveData) {
		this.receiveData = receiveData;
	}
	public String getDeviceIP() {
		return deviceIP;
	}
	public void setDeviceIP(String deviceIP) {
		this.deviceIP = deviceIP;
	}
	public int getLeftChannelBuff() {
		return leftChannelBuff;
	}
	public void setLeftChannelBuff(int leftChannelBuff) {
		this.leftChannelBuff = leftChannelBuff;
	}
	public int getRightChannelBuff() {
		return rightChannelBuff;
	}
	public void setRightChannelBuff(int rightChannelBuff) {
		this.rightChannelBuff = rightChannelBuff;
	}
	
	

}
