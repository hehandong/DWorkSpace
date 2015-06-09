package com.hhd.multisocket514;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.CellIdentityCdma;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hhd.multisocket.R;
import com.hhd.multisocket514.adapter.DeviceDataAdapter;
import com.hhd.multisocket514.base.BaseFragment;
import com.hhd.multisocket514.bean.DeviceBean;
import com.hhd.multisocket514.business.ObtainDevice2;
import com.hhd.multisocket514.business.SearchServer;
import com.hhd.multisocket514.utils.LogUtil;
import com.hhd.multisocket514.utils.TransformUtil;

public class ObtainDeviceFrag2 extends BaseFragment implements
		OnItemClickListener {

	protected static final int OBTAINRECEIVE = 0;

	private TextView obtainSendText, obtainReceiveText;

	// private SearchServer searchServer;

	private ObtainDevice2 obtainDevice;

	private ArrayList<DeviceBean> deviceBeanList;
	private byte[] reDeveiceDatas;

	private ListView devicelistview;

	private Button querybuffButton;

	private Button sendobtainButton;

	private QueryDeviceBuffFrag queryDeviceBuffFrag;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			case OBTAINRECEIVE:

				byte[] sendDatas = obtainDevice.getSendDatas();

				obtainSendText.setText("发送获取设备的命令：" + "\n"
						+ TransformUtil.byteToHexString(sendDatas));

				// obtainReceiveText.setText("返回的数据：" + "\n"
				// + TransformUtil.byteToHexString(reDeveiceDatas) + "\n"
				// + "设备的数据：" + obtainDevice.getDeviceNumber());
                if (deviceBeanList!=null) {
					
					DeviceDataAdapter deviceDataAdapter = new DeviceDataAdapter(
							ct, deviceBeanList);
					devicelistview.setAdapter(deviceDataAdapter);
				}

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

		devicelistview.setOnItemClickListener(this);

	}

	@Override
	public void onClick(View v) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				obtainDevice = new ObtainDevice2(ct);
				obtainDevice.initObtainDevice2();
				reDeveiceDatas = obtainDevice.sendAndReceiveSocket();
				deviceBeanList = obtainDevice.getPlayDeviceData();

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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		String ip = deviceBeanList.get(position).getIP();

		LogUtil.i("设备的IP地址是：" + ip);

		// FragmentManager fm = getActivity().getFragmentManager();
		// FragmentTransaction transaction = fm.beginTransaction();
		// if (queryDeviceBuffFrag == null) {
		// queryDeviceBuffFrag = new QueryDeviceBuffFrag();
		//
		// }
		// transaction.replace(R.id.container, queryDeviceBuffFrag);
		// transaction.commit();

		getActivity().getFragmentManager().beginTransaction()
				.replace(R.id.container, QueryDeviceBuffFrag.newInstance(ip))
				.commit();

	}

}
