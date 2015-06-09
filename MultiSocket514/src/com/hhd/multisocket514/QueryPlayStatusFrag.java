package com.hhd.multisocket514;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hhd.multisocket.R;
import com.hhd.multisocket514.adapter.DeviceDataAdapter;
import com.hhd.multisocket514.adapter.PlayStatusAdapter;
import com.hhd.multisocket514.base.BaseFragment;
import com.hhd.multisocket514.bean.DeviceBean;
import com.hhd.multisocket514.bean.PlayStatus;
import com.hhd.multisocket514.business.QueryPlayStatus;
import com.hhd.multisocket514.business.SearchServer;
import com.hhd.multisocket514.utils.TransformUtil;

public class QueryPlayStatusFrag extends BaseFragment {

	protected static final int RECEIVE = 0;
	private TextView sendQueryText, receiveQueryText;
	private Button sendQueryButton;
	private View view;
	private QueryPlayStatus queryPlayStatus;

	private ListView lvPlayStatus;

	private byte[] comBuffs;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			case RECEIVE:

				sendQueryText.setText("发送的信息：" + "\n"
						+ TransformUtil.byteToHexString(comBuffs));
				
				System.out.println(TransformUtil.byteToHexString(comBuffs));

				for (int i = 0; i < comBuffs.length; i++) {
					System.out.println("第"+i+ "行："+comBuffs[i] + "\n");
					
				}
				for (int i = 0; i < comBuffs.length; i++) {
					System.out.println("第"+i+ "行："+String.format("%02X", comBuffs[i]) + "\n");
					
				}
				
//				receiveQueryText.setText("返回的信息："
//						+ "\n"
//						+ TransformUtil.byteToHexString(queryPlayStatus
//								.getReceiveDatas()));
				
				byte[] receiveDatas = queryPlayStatus.getReceiveDatas();
				
				for (int i = 0; i < receiveDatas.length; i++) {
					System.out.println("第"+i+ "行："+String.format("%02X", receiveDatas[i]) + "\n");
					
				}
				

				ArrayList<PlayStatus> playStatus = queryPlayStatus
						.getPlayStatus();
				
				for (PlayStatus playStatus2 : playStatus) {

					System.out.println("IP:" + playStatus2.getIP() + "\n"
							+ "左声道的来源IP：" + playStatus2.getLeftSourceIP()
							+ "\n" + "左声道的来源声道：" + playStatus2.getLeftTrack()
							+ "\n" + " 右声道的来源IP"
							+ playStatus2.getRightSourceIP() + "\n"
							+ "右声道的来源声道：" + playStatus2.getRightTrack());
				}
				
				PlayStatusAdapter playStatusAdapter = new PlayStatusAdapter(ct,
						playStatus);
				lvPlayStatus.setAdapter(playStatusAdapter);

				break;

			default:
				break;
			}

		};
	};

	@Override
	protected View initView(LayoutInflater inflater) {
		
		view = inflater.inflate(R.layout.frag_queryplaystatus, null);
		sendQueryButton = (Button) view.findViewById(R.id.btnSendQuery);
		sendQueryText = (TextView) view.findViewById(R.id.tvSendQuery);
		receiveQueryText = (TextView) view.findViewById(R.id.tvReceiveQuery);
		lvPlayStatus = (ListView) view.findViewById(R.id.lvPlayStatus);
		return view;
	}

	@Override
	protected void initData(Bundle savedInstanceState) {

		sendQueryButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				queryPlayStatus = new QueryPlayStatus(ct);
				queryPlayStatus.sendSocket();

				comBuffs = queryPlayStatus.getSendDatas();

				//更新UI
				Message msg = new Message();
				msg.what = RECEIVE;

				handler.sendEmptyMessage(msg.what);

			}
		}).start();
	}

	@Override
	protected void processClick(View v) {

	}
	
	

}
