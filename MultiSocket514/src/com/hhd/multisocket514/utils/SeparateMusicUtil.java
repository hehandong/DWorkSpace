package com.hhd.multisocket514.utils;

import java.util.ArrayList;

public class SeparateMusicUtil {

	// 16bit的音乐分开左右声道
	public static ArrayList<byte[]> separate16bitMusic(byte[] musicData) {

		ArrayList<byte[]> musicList = new ArrayList<byte[]>();

		byte[] leftmusic = new byte[musicData.length / 2];
		byte[] rightmusic = new byte[musicData.length / 2];
		int leftindex = 0;
		int rightindex = 0;

		for (int i = 0; i < musicData.length; i += 4) {
			System.arraycopy(musicData, i, leftmusic, leftindex, 2);
			leftindex += 2;

		}
		musicList.add(leftmusic);

		for (int i = 2; i < musicData.length; i += 4) {
			System.arraycopy(musicData, i, rightmusic, rightindex, 2);
			rightindex += 2;

		}
		musicList.add(rightmusic);

		return musicList;

	}

}
