package com.hhd.multisocket514;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.hhd.multisocket.R;
import com.hhd.multisocket514.adapter.SendMusicToSoundAdapter;
import com.hhd.multisocket514.adapter.SendMusicToSoundAdapter.ViewHolder;
import com.hhd.multisocket514.bean.DeviceBean;
import com.hhd.multisocket514.bean.DeviceBuff;
import com.hhd.multisocket514.bean.SoundDean;
import com.hhd.multisocket514.business.ObtainDevice2;
import com.hhd.multisocket514.business.QueryDeviceBuf2;
import com.hhd.multisocket514.utils.LogUtil;

public class SendMusicToSoundActivity extends Activity implements
		OnItemClickListener {

	protected static final int OBTAINRECEIVE = 0;
	private final static int RECEIVELEFTNAME = 1;
	private final static int RECEIVERIGHTNAME = 2;
	private ListView lvsoundData;

	private ObtainDevice2 obtainDevice;
	private byte[] reDeveiceDatas;

	private ArrayList<DeviceBean> deviceBeanList;

	private QueryDeviceBuf2 queryDeviceBuf;
	private DeviceBuff deviceBuff;

	private SendMusicFrag8 sendMusicFrag8;

	private ArrayList<SoundDean> soundDeanList;
	private SendMusicToSoundAdapter adapter;

	private String musicName;

	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case OBTAINRECEIVE:
				if (adapter == null) {
					adapter = new SendMusicToSoundAdapter(
							getApplicationContext(), soundDeanList);
				} else {
					adapter.notifyDataSetChanged();
				}

				lvsoundData.setAdapter(adapter);
				break;
			case RECEIVELEFTNAME:
				adapter.notifyDataSetChanged();

				break;
			case RECEIVERIGHTNAME:
				adapter.notifyDataSetChanged();
				break;

			default:
				break;
			}

		};
	};
	private String reIP;
	private String reButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frag_sendmusictosound);

		lvsoundData = (ListView) findViewById(R.id.lvsoundData);

		getListViewData();

		lvsoundData.setOnItemClickListener(this);

	}

	public void getListViewData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				obtainDevice = new ObtainDevice2(getApplicationContext());
				obtainDevice.initObtainDevice2();
				reDeveiceDatas = obtainDevice.sendAndReceiveSocket();
				deviceBeanList = obtainDevice.getPlayDeviceData();
				soundDeanList = new ArrayList<SoundDean>();

				queryDeviceBuf = new QueryDeviceBuf2(getApplicationContext());
				queryDeviceBuf.initQueryDeviceBuf();

				for (int i = 0; i < deviceBeanList.size(); i++) {
					SoundDean soundDean = new SoundDean();
					soundDean.setType(deviceBeanList.get(i).getType());
					soundDean.setIP(deviceBeanList.get(i).getIP());
					soundDean.setState(deviceBeanList.get(i).getState());

					deviceBuff = queryDeviceBuf.getDeviceBuff(deviceBeanList
							.get(i).getIP());
					LogUtil.i("设备的IP地址：" + deviceBeanList.get(i).getIP());
					if (deviceBuff.getLeftChannelBuff() != 0) {

						soundDean.setIsPlayingLeft(true);
					} else {
						soundDean.setIsPlayingLeft(false);
					}

					if (deviceBuff.getRightChannelBuff() != 0) {
						soundDean.setIsPlayingRight(true);

					} else {
						soundDean.setIsPlayingRight(false);
					}
					soundDeanList.add(soundDean);
				}

				// 更新UI
				Message msg = new Message();
				msg.what = OBTAINRECEIVE;

				handler.sendEmptyMessage(msg.what);
			}
		}).start();
	}

	

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		
//		if (requestCode == 0 && resultCode == 10) {
//			Bundle bundle = intent.getExtras();
//			musicName = bundle.getString("MusicName");
//			reIP = bundle.getString("reIP");
//			reButton = bundle.getString("reButton");
//			int postition = bundle.getInt("postition");
//			LogUtil.i("musicName---" + musicName);
//			LogUtil.i("reIP--------" + reIP);
//			LogUtil.i("reButton----" + reButton);
//			LogUtil.i("postition----" + postition);
//			adapter.setCureentType(reButton);
//			adapter.setCurrentPosition(postition);
//			adapter.notifyDataSetChanged();// getView
//
//		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		
		
		

	}

}
