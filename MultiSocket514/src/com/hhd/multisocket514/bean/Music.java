package com.hhd.multisocket514.bean;

import android.graphics.Color;

public class Music {

	// 标题
	private String title;
	// 演唱者
	private String artist;
	// ID
	private String id;
	// 在SDcard上的播放路径
	private String path;
	// 时长
	private String duration;
	
	private boolean ischecked = false;
	
	private int titleColor = Color.BLACK;
	

	public int getTitleColor() {
		return titleColor;
	}

	public void setTitleColor(int titleColor) {
		this.titleColor = titleColor;
	}

	public boolean getIschecked() {
		return ischecked;
	}

	public void setIschecked(boolean ischecked) {
		this.ischecked = ischecked;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
