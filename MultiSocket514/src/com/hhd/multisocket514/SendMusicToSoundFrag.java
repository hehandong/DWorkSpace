package com.hhd.multisocket514;

import java.util.ArrayList;

import android.R.integer;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.hhd.multisocket.R;
import com.hhd.multisocket514.base.BaseFragment;
import com.hhd.multisocket514.bean.DeviceBean;
import com.hhd.multisocket514.bean.DeviceBuff;
import com.hhd.multisocket514.bean.SoundDean;
import com.hhd.multisocket514.business.ObtainDevice2;
import com.hhd.multisocket514.business.QueryDeviceBuf2;
import com.hhd.multisocket514.interfac.CallBackMusic;
import com.hhd.multisocket514.utils.LogUtil;
import com.hhd.multisocket514.utils.TransformUtil;

public class SendMusicToSoundFrag extends BaseFragment implements
		OnItemClickListener {

	protected static final int OBTAINRECEIVE = 0;
	private final static int RECEIVELEFTNAME = 1;
	private final static int RECEIVERIGHTNAME = 2;
	private Button btnSendMusic;
	private View view;
	private TextView tvSendData;
	private ListView lvsoundData;

	private ObtainDevice2 obtainDevice;
	private byte[] reDeveiceDatas;

	private ArrayList<DeviceBean> deviceBeanList;

	private SendMusicToSoundAdapter sendMusicToSongAdapter;

	private QueryDeviceBuf2 queryDeviceBuf;
	private DeviceBuff deviceBuff;

	private SendMusicFrag sendMusicFrag;

	private ArrayList<SoundDean> soundDeanList;
	private SendMusicToSoundAdapter adapter;

	private String musicName;

	private String IP;
	private String button;

	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case OBTAINRECEIVE:
				if (adapter == null) {
					adapter = new SendMusicToSoundAdapter(ct, soundDeanList);
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



	@Override
	protected View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.frag_sendmusictosound, null);
//		btnSendMusic = (Button) view.findViewById(R.id.btnSendMusic);
//		tvSendData = (TextView) view.findViewById(R.id.tvSendData);
		lvsoundData = (ListView) view.findViewById(R.id.lvsoundData);
		return view;
	}

	@Override
	protected void initData(Bundle savedInstanceState) {
		btnSendMusic.setOnClickListener(this);
		lvsoundData.setOnItemClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				obtainDevice = new ObtainDevice2(ct);
				obtainDevice.initObtainDevice2();
				reDeveiceDatas = obtainDevice.sendAndReceiveSocket();
				deviceBeanList = obtainDevice.getPlayDeviceData();
				soundDeanList = new ArrayList<SoundDean>();

				queryDeviceBuf = new QueryDeviceBuf2(ct);
				queryDeviceBuf.initQueryDeviceBuf();
				// deviceBuffBean = queryDeviceBuf.getDeviceBuff(iP);

				for (int i = 0; i < deviceBeanList.size(); i++) {
					SoundDean soundDean = new SoundDean();
					soundDean.setType(deviceBeanList.get(i).getType());
					soundDean.setIP(deviceBeanList.get(i).getIP());
					soundDean.setState(deviceBeanList.get(i).getState());
					soundDeanList.add(soundDean);

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
				}

				// 更新UI
				Message msg = new Message();
				msg.what = OBTAINRECEIVE;

				handler.sendEmptyMessage(msg.what);
			}
		}).start();

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	@Override
	protected void processClick(View v) {

	}

	public class SendMusicToSoundAdapter extends BaseAdapter {

		private Context context;

		private ArrayList<SoundDean> list;

		public SendMusicToSoundAdapter(Context context,
				ArrayList<SoundDean> list) {
			super();
			this.context = context;
			this.list = list;

		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.sendmusicitem, null);
				holder.tvtype = (TextView) convertView
						.findViewById(R.id.tvtype);
				holder.tvip = (TextView) convertView.findViewById(R.id.tvip);
				holder.tvstate = (TextView) convertView
						.findViewById(R.id.tvstate);
				holder.cbleft = (CheckBox) convertView
						.findViewById(R.id.cbleft);
				holder.cbright = (CheckBox) convertView
						.findViewById(R.id.cbright);
				holder.btleft = (Button) convertView.findViewById(R.id.btleft);
				holder.btright = (Button) convertView
						.findViewById(R.id.btright);
				holder.tvleftname = (TextView) convertView
						.findViewById(R.id.tvleftname);
				holder.tvrightname = (TextView) convertView
						.findViewById(R.id.tvrightname);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final SoundDean soundDean = list.get(position);
			holder.tvtype.setText(soundDean.getType() + ":");
			holder.tvip.setText(soundDean.getIP() + ":");
			holder.tvstate.setText(soundDean.getState());
			holder.cbleft.setChecked(soundDean.getIsPlayingLeft());
			holder.cbright.setChecked(soundDean.getIsPlayingRight());
			// holder.tvleftname.setText(soundDean.getLeftMusicName());
//			if (soundDean.getIP() == IP ) {

				holder.tvleftname.setText(musicName);
//			}
//			if (soundDean.getIP() == IP && button == "btright") {
//
//				holder.tvrightname.setText(musicName);
//			}
//			holder.tvrightname.setText(soundDean.getRightMusicName());

			holder.btleft.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					FragmentTransaction transaction = getActivity()
							.getFragmentManager().beginTransaction();

					if (sendMusicFrag == null) {
						sendMusicFrag = new SendMusicFrag();

					}
					transaction.replace(R.id.container, sendMusicFrag,
							"MusicFrag").commit();

					// sendMusicFrag.setCallBack(new CallBackMusic() {
					//
					// @Override
					// public void callBackMusicName(String music) {
					// Message msg = new Message();
					// msg.what = RECEIVELEFTNAME;
					// msg.obj = music;
					// handler.sendMessage(msg);
					// }
					// });
					//
					// sendMusicFrag.callBackMusic();

				}
			});

			holder.btright.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					FragmentTransaction transaction = getActivity()
							.getFragmentManager().beginTransaction();

					if (sendMusicFrag == null) {
						sendMusicFrag = new SendMusicFrag();
					}
					transaction.replace(R.id.container, sendMusicFrag,
							"MusicFrag").commit();

//					sendMusicFrag.setCallBack(new CallBackMusic() {
//
//						@Override
//						public void callBackMusicName(String music) {
//							Message msg = new Message();
//							msg.what = RECEIVERIGHTNAME;
//							msg.obj = music;
//							handler.sendMessage(msg);
//						}
//					});
//
//					sendMusicFrag.callBackMusic();

				}
			});
			return convertView;
		}

		public class ViewHolder {
			TextView tvtype;
			TextView tvip;
			TextView tvstate;
			CheckBox cbleft;
			CheckBox cbright;
			Button btleft;
			Button btright;
			TextView tvleftname;
			TextView tvrightname;

		}

	}
}
