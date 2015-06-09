package com.hhd.multisocket514.business;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.R.integer;

public class SendSongSocket {

	public SendSongSocket(String IP, int Port, byte[] data) {
		try {
			Socket socket = new Socket(IP, Port);
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
					socket.getOutputStream());
			bufferedOutputStream.write(data);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
