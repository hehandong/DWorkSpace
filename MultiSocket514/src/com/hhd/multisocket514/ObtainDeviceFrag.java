package com.hhd.multisocket514;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hhd.multisocket.R;
import com.hhd.multisocket514.adapter.DeviceDataAdapter;
import com.hhd.multisocket514.base.BaseFragment;
import com.hhd.multisocket514.bean.DeviceBean;
import com.hhd.multisocket514.business.ObtainDevice;
import com.hhd.multisocket514.business.SearchServer;
import com.hhd.multisocket514.utils.TransformUtil;

public class ObtainDeviceFrag extends BaseFragment {
	
protected static final int OBTAINRECEIVE = 0;


private TextView obtainSendText,obtainReceiveText;
	

//	private SearchServer searchServer;
	
	private ObtainDevice obtainDevice;
	
	private ListView devicelistview;
	


	private Button sendobtainButton;
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			
			
			case OBTAINRECEIVE:

				byte[] sendDatas = obtainDevice.getSendDatas();
				
				obtainSendText.setText( "发送获取设备的命令："+"\n"+TransformUtil.byteToHexString(sendDatas));
				byte[] receiveDatas = obtainDevice.getReceiveDatas();
				
				obtainReceiveText.setText("返回的数据："+ "\n"+TransformUtil.byteToHexString(receiveDatas)+ "\n"+ "设备的数据："+obtainDevice.getDeviceNumber());
				
				
				ArrayList<DeviceBean> deviceBeans = obtainDevice.getDeviceBeans();
				DeviceDataAdapter deviceDataAdapter = new DeviceDataAdapter(ct, deviceBeans);
				devicelistview.setAdapter(deviceDataAdapter);
				


			default:
				break;
			}

		};
	};

	@Override
	protected View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.frag_searchdevice, null);
		
		sendobtainButton = (Button) view.findViewById(R.id.btnSendQuery1);
		obtainSendText = (TextView) view.findViewById(R.id.tvObtainSend2);
		obtainReceiveText = (TextView) view.findViewById(R.id.tvObtainReceive2);
		
		devicelistview = (ListView) view.findViewById(R.id.devicelistview2);
		return view;
	}
	

	@Override
	protected void initData(Bundle savedInstanceState) {
		sendobtainButton.setOnClickListener(this);

	}
    
	@Override
	public void onClick(View v) {
		new Thread(new Runnable() {
			
			

			@Override
			public void run() {
				obtainDevice = new ObtainDevice(ct);
				obtainDevice.sendSocket();
				
				// 更新UI
				Message msg = new Message();
				msg.what = OBTAINRECEIVE; 

				handler.sendEmptyMessage(msg.what);
				
			}
		}).start();
	}
	@Override
	protected void processClick(View v) {

	}

}
