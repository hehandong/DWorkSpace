package com.hhd.multisocket514.adapter;

import java.util.ArrayList;
import java.util.List;

import com.hhd.multisocket.R;
import com.hhd.multisocket514.bean.PlayStatus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PlayStatusAdapter extends BaseAdapter {

	private Context context;
	private List list;

	public PlayStatusAdapter(Context context, ArrayList<PlayStatus> list) {
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
					R.layout.playstatusitem, null);
			viewHolder = new ViewHolder();
			viewHolder.tvIP = (TextView) convertView.findViewById(R.id.tvIP);
			viewHolder.tvLeftSourceIP = (TextView) convertView.findViewById(R.id.tvLeftSourceIP);
			viewHolder.tvLeftTrack = (TextView) convertView.findViewById(R.id.tvLeftTrack);
			viewHolder.tvRightSourceIP = (TextView) convertView.findViewById(R.id.tvRightSourceIP);
			viewHolder.tvRightTrack = (TextView) convertView.findViewById(R.id.tvRightTrack);
			

			convertView.setTag(viewHolder);

		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		PlayStatus playStatus = (PlayStatus) list.get(position);
		viewHolder.tvIP.setText(playStatus.getIP());
		viewHolder.tvLeftSourceIP.setText(playStatus.getLeftSourceIP());
		viewHolder.tvLeftTrack.setText(playStatus.getLeftTrack());
		viewHolder.tvRightSourceIP.setText(playStatus.getRightSourceIP());
		viewHolder.tvRightTrack.setText(playStatus.getRightTrack());
		
		return convertView;
	}

	public class ViewHolder {
		TextView tvIP;
		TextView tvLeftSourceIP;
		TextView tvLeftTrack;
		TextView tvRightSourceIP;
		TextView tvRightTrack;
	}

}
