package com.hhd.multisocket514;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.hhd.multisocket.R;
import com.hhd.multisocket514.adapter.DeviceDataAdapter;
import com.hhd.multisocket514.base.BaseFragment;
import com.hhd.multisocket514.bean.DeviceBean;
import com.hhd.multisocket514.business.SearchServer;
import com.hhd.multisocket514.utils.LogUtil;
import com.hhd.multisocket514.utils.TransformUtil;

public class SearchServerFrag extends BaseFragment {

	protected static final int RECEIVE = 0;
	private TextView sendText, receiveText;
	private Button sendButton;
	private SearchServer searchServer;
	private View view;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			case RECEIVE:

				byte[] comBuff = searchServer.getSendDatas();

				sendText.setText("发送获取服务的命令：" + "\n"
						+ TransformUtil.byteToHexString(comBuff));

				receiveText.setText("服务器的地址：" + searchServer.getServerIP());
				
				 

				break;

			default:
				break;
			}

		};
	};

	@Override
	protected View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.frag_searchserver, null);
		sendButton = (Button) view.findViewById(R.id.btnSend2);
		sendText = (TextView) view.findViewById(R.id.tvSend2);
		receiveText = (TextView) view.findViewById(R.id.tvReceive2);
		return view;
	}

	@Override
	protected void initData(Bundle savedInstanceState) {

		sendButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
	
		new Thread(new Runnable() {

			@Override
			public void run() {

				searchServer = new SearchServer(ct);

				// 发送socket
				searchServer.sendSocket();

				// 更新UI
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
