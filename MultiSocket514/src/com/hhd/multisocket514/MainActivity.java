package com.hhd.multisocket514;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.http.util.EncodingUtils;

import com.hhd.multisocket.R;
import com.hhd.multisocket514.adapter.DeviceDataAdapter;
import com.hhd.multisocket514.bean.DeviceBean;
import com.hhd.multisocket514.business.ObtainDevice;
import com.hhd.multisocket514.business.SearchServer;
import com.hhd.multisocket514.utils.TransformUtil;

import android.R.array;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml.Encoding;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final int SEND = 0;
	private static final int RECEIVE = 1;
	private static final int  OBTAINRECEIVE = 2;

	private static final String TAG = "MainActivity";

	private TextView sendText,receiveText,obtainSendText,obtainReceiveText;
	

	private SearchServer searchServer;
	
	private ObtainDevice obtainDevice;
	
	private ListView devicelistview;


	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			
			case RECEIVE:

				byte[] comBuff = searchServer.getSendDatas();
				
				sendText.setText( "发送的获取服务的命令："+"\n"+TransformUtil.byteToHexString(comBuff));
				
				receiveText.setText("服务器的地址："+searchServer.getServerIP());
				
				break;
			case OBTAINRECEIVE:

				byte[] sendDatas = obtainDevice.getSendDatas();
				
				obtainSendText.setText( "发送的信息："+"\n"+TransformUtil.byteToHexString(sendDatas));
				byte[] receiveDatas = obtainDevice.getReceiveDatas();
				
				obtainReceiveText.setText("返回的数据："+ "\n"+TransformUtil.byteToHexString(receiveDatas)+ "\n"+ "设备的数量："+obtainDevice.getDeviceNumber());
				
				
				ArrayList<DeviceBean> deviceBeans = obtainDevice.getDeviceBeans();
				DeviceDataAdapter deviceDataAdapter = new DeviceDataAdapter(getApplicationContext(), deviceBeans);
				devicelistview.setAdapter(deviceDataAdapter);
				
				break;

			default:
				break;
			}

		};
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button sendButton = (Button) findViewById(R.id.btnSend);
		Button sendobtainButton = (Button) findViewById(R.id.btnSendobtain);
		
		sendText = (TextView) findViewById(R.id.tvSend);
		receiveText = (TextView) findViewById(R.id.tvReceive);
		
		obtainSendText = (TextView) findViewById(R.id.tvObtainSend);
		obtainReceiveText = (TextView) findViewById(R.id.tvObtainReceive);
		
		devicelistview = (ListView) findViewById(R.id.devicelistview);
		
		

		// String format = String.format("%02X", 99);//���Խ�byte����ת��16����
		// Log.i(TAG, format);

		// Log.i(TAG, Arrays.toString(comBuff));

		sendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {

					@Override
					public void run() {

						searchServer = new SearchServer(getApplicationContext());

						
						searchServer.sendSocket();


						
						Message msg = new Message();
						msg.what = RECEIVE; 

						handler.sendEmptyMessage(msg.what);

					}
				}).start();

			}
		});
		
		sendobtainButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {
					
				

					@Override
					public void run() {
						obtainDevice = new ObtainDevice(getApplicationContext());
						obtainDevice.sendSocket();
						
					
						Message msg = new Message();
						msg.what = OBTAINRECEIVE; 

						handler.sendEmptyMessage(msg.what);
						
					}
				}).start();
				
			}
		});
		
		

	}
	
	/* 
	 * Activity 关闭socket
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		searchServer.closeSocket();
	}
	
   public void testgit(){
	   searchServer.closeSocket();//第三次提交
   }

}
