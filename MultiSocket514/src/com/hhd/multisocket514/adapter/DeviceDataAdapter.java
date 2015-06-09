package com.hhd.multisocket514.adapter;

import java.util.ArrayList;
import java.util.List;

import com.hhd.multisocket.R;
import com.hhd.multisocket514.bean.DeviceBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DeviceDataAdapter extends BaseAdapter {

	private Context context;
	private List list;

	public DeviceDataAdapter(Context context, ArrayList<DeviceBean> list) {

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
					R.layout.deviceitem, null);
			viewHolder = new ViewHolder();
			viewHolder.tvType = (TextView) convertView.findViewById(R.id.type);
			viewHolder.tvIP = (TextView) convertView.findViewById(R.id.ip);
			viewHolder.tvState = (TextView) convertView
					.findViewById(R.id.state);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		DeviceBean deviceBean = (DeviceBean) list.get(position);
		viewHolder.tvType.setText(deviceBean.getType());
		viewHolder.tvIP.setText(deviceBean.getIP());
		viewHolder.tvState.setText(deviceBean.getState());

		return convertView;
	}

	public class ViewHolder {
		TextView tvType;
		TextView tvIP;
		TextView tvState;
	}

}
