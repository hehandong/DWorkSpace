package com.hhd.multisocket514.bean;

public class SoundDean {
	
	private String type;
	private String IP;
	private String state;
	private boolean isPlayingLeft;
	private boolean isPlayingRight;
	private boolean isSendedLeft;
	private boolean isSendedRight;
	private String leftMusicName;
	private String rightMusicName;
	
	private String musicName;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIP() {
		return IP;
	}
	public void setIP(String iP) {
		IP = iP;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public boolean getIsPlayingLeft() {
		return isPlayingLeft;
	}
	public void setIsPlayingLeft(boolean isPlayingLeft) {
		this.isPlayingLeft = isPlayingLeft;
	}
	public boolean getIsPlayingRight() {
		return isPlayingRight;
	}
	public void setIsPlayingRight(boolean isPlayingRight) {
		this.isPlayingRight = isPlayingRight;
	}
	public boolean getIsSendedLeft() {
		return isSendedLeft;
	}
	public void setIsSendedLeft(boolean isSendedLeft) {
		this.isSendedLeft = isSendedLeft;
	}
	public boolean getIsSendedRight() {
		return isSendedRight;
	}
	public void setIsSendedRight(boolean isSendedRight) {
		this.isSendedRight = isSendedRight;
	}
	public String getLeftMusicName() {
		return leftMusicName;
	}
	public void setLeftMusicName(String leftMusicName) {
		this.leftMusicName = leftMusicName;
	}
	public String getRightMusicName() {
		return rightMusicName;
	}
	public void setRightMusicName(String rightMusicName) {
		this.rightMusicName = rightMusicName;
	}

}
