package com.hhd.multisocket514.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hhd.multisocket.R;
import com.hhd.multisocket514.bean.SoundDean;

public class SendMusicToSoundAdapter extends BaseAdapter {

	private Context context;

	private ArrayList<SoundDean> list;

	private int currentPosition = -1;
	private String cureentType;

	public int getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(int position) {
		this.currentPosition = position;
	}

	public String getCureentType() {
		return cureentType;
	}

	public void setCureentType(String cureentType) {
		this.cureentType = cureentType;
	}

	public SendMusicToSoundAdapter(Context context, ArrayList<SoundDean> list) {
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
			holder.tvtype = (TextView) convertView.findViewById(R.id.tvtype);
			holder.tvip = (TextView) convertView.findViewById(R.id.tvip);
			holder.tvstate = (TextView) convertView.findViewById(R.id.tvstate);
			holder.cbleft = (CheckBox) convertView.findViewById(R.id.cbleft);
			holder.cbright = (CheckBox) convertView.findViewById(R.id.cbright);
			holder.btleft = (Button) convertView.findViewById(R.id.btleft);
			holder.btright = (Button) convertView.findViewById(R.id.btright);
			holder.tvleftname = (TextView) convertView
					.findViewById(R.id.tvleftname);
			holder.tvrightname = (TextView) convertView
					.findViewById(R.id.tvrightname);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		SoundDean soundDean = list.get(position);
		holder.tvtype.setText(soundDean.getType() + ":");
		holder.tvip.setText(soundDean.getIP() + ":");
		holder.tvstate.setText(soundDean.getState());
		holder.cbleft.setChecked(soundDean.getIsPlayingLeft());
		holder.cbright.setChecked(soundDean.getIsPlayingRight());

		holder.tvleftname.setText(null);

		holder.tvrightname.setText(null);

		Log.d("MutilSocket", "currentPosition=" + currentPosition
				+ " cureentType=" + cureentType);

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
