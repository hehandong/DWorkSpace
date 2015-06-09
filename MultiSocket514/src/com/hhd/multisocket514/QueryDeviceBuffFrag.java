package com.hhd.multisocket514;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.hhd.multisocket.R;
import com.hhd.multisocket514.base.BaseFragment;
import com.hhd.multisocket514.bean.DeviceBuff;
import com.hhd.multisocket514.business.QueryDeviceBuf2;
import com.hhd.multisocket514.utils.LogUtil;
import com.hhd.multisocket514.utils.TransformUtil;

public class QueryDeviceBuffFrag extends BaseFragment {

	protected static final int QUERYSUCCESSFUL = 1;

	private TextView queryBuffSendText, queryBuffReceiveText;

	private DeviceBuff deviceBuffBean;

	private ListView buffListview;

	private QueryDeviceBuf2 queryDeviceBuf;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			case QUERYSUCCESSFUL:

				byte[] sendDatas = queryDeviceBuf.getSendDatas();

				queryBuffSendText.setText("发送获取设备的命令：" + "\n"
						+ TransformUtil.byteToHexString(sendDatas));

				queryBuffReceiveText.setText("查询的IP地址："
						+ deviceBuffBean.getDeviceIP() + "\n" + "设备的左声道缓存："
						+ deviceBuffBean.getLeftChannelBuff() + "字节" + "\n"
						+ "设备的右声道缓存：" + deviceBuffBean.getRightChannelBuff()
						+ "字节");

			default:
				break;
			}

		};
	};

	private Button sendQueryButton;

	private EditText editIP;

	private String iP;

	@Override
	protected View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.frag_querybuff, null);
		sendQueryButton = (Button) view.findViewById(R.id.btnSendQuery);
		editIP = (EditText) view.findViewById(R.id.etIP);

		queryBuffSendText = (TextView) view.findViewById(R.id.tvQueryBuffSend);
		queryBuffReceiveText = (TextView) view
				.findViewById(R.id.tvQueryBuffReceive);

		buffListview = (ListView) view.findViewById(R.id.buffListview);
		return view;
	}

	@Override
	protected void initData(Bundle savedInstanceState) {

		sendQueryButton.setOnClickListener(this);
		editIP.setText(getArguments().getString("param"));

	}
	
	 public static Fragment newInstance(String arg){  
		 QueryDeviceBuffFrag fragment = new QueryDeviceBuffFrag();  
         Bundle bundle = new Bundle();  
         bundle.putString("param", arg);  
         fragment.setArguments(bundle);  
         return fragment;  
  } 
	


	@Override
	public void onClick(View v) {
		iP = editIP.getText().toString().trim();
		new Thread(new Runnable() {

			@Override
			public void run() {

		

				queryDeviceBuf = new QueryDeviceBuf2(ct);
				queryDeviceBuf.initQueryDeviceBuf();
				deviceBuffBean = queryDeviceBuf.getDeviceBuff(iP);

				Message message = new Message();
				message.what = QUERYSUCCESSFUL;
				handler.sendEmptyMessage(message.what);

			}
		}).start();

	}

	@Override
	protected void processClick(View v) {

	}

}
