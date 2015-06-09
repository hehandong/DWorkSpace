package com.hhd.multisocket514.adapter;

import java.util.ArrayList;
import java.util.List;

import com.hhd.multisocket.R;
import com.hhd.multisocket514.bean.DeviceBean;
import com.hhd.multisocket514.bean.DeviceBuff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DeviceBuffAdapter extends BaseAdapter {

	private Context context;
	private List list;

	public DeviceBuffAdapter(Context context, ArrayList<DeviceBuff> list) {
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
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.buffitem, null);
			viewHolder = new ViewHolder();
			viewHolder.tvdeviceIP = (TextView) convertView.findViewById(R.id.ip);
			viewHolder.tvleftTrackBuff = (TextView) convertView.findViewById(R.id.leftTrackBuff);
			viewHolder.tvrightTrackBuff = (TextView) convertView
					.findViewById(R.id.rightTrackBuff);

			convertView.setTag(viewHolder);

		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
//		DeviceBean deviceBean = (DeviceBean) list.get(position);
		DeviceBuff deviceBuff = (DeviceBuff) list.get(position);
		viewHolder.tvdeviceIP.setText(deviceBuff.getDeviceIP());
		viewHolder.tvleftTrackBuff.setText(deviceBuff.getLeftChannelBuff()+"");
		viewHolder.tvrightTrackBuff.setText(deviceBuff.getRightChannelBuff()+"");
		
		return convertView;
	}

	public class ViewHolder {
		TextView tvdeviceIP;
		TextView tvleftTrackBuff;
		TextView tvrightTrackBuff;
	}

}
