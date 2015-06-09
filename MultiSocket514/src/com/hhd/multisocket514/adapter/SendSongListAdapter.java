package com.hhd.multisocket514.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.hhd.multisocket.R;
import com.hhd.multisocket514.bean.Music;
import com.hhd.multisocket514.utils.MediaUtil;
import com.hhd.multisocket514.utils.MusicConstantValue;

public class SendSongListAdapter extends BaseAdapter {

	private Context context;

	// private Map<Integer, Boolean> isCheckMap;

	public SendSongListAdapter(Context context) {
		super();
		this.context = context;
	}

	@Override
	public int getCount() {

		return MediaUtil.getInstacen().getSongList().size();
	}

	@Override
	public Object getItem(int position) {

		return MediaUtil.getInstacen().getSongList().get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		Music music = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.sendlistitem, null);
			holder.tx1 = (TextView) convertView.findViewById(R.id.ListItemName);
			holder.tx2 = (TextView) convertView
					.findViewById(R.id.ListItemContent);
			holder.cb1 = (CheckBox) convertView.findViewById(R.id.cbmusic);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tx1.setTag(position);
		List<Music> songList = MediaUtil.getInstacen().getSongList();
		if (songList != null && songList.size() > 0) {

			music = MediaUtil.getInstacen().getSongList().get(position);
			holder.tx1.setText((position + 1) + "." + music.getTitle());
			holder.tx1.setTextColor(music.getTitleColor());
			holder.tx2.setText(music.getArtist());
			holder.cb1.setChecked(music.getIschecked());
		
		}


		return convertView;
	}

	public class ViewHolder {

		public TextView tx1;
		public TextView tx2;
		public CheckBox cb1;
	}
}
