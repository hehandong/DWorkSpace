package com.hhd.multisocket514;

import java.net.URL;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.hhd.multisocket.R;
import com.hhd.multisocket514.adapter.SendSongListAdapter;
import com.hhd.multisocket514.adapter.SendSongListAdapter.ViewHolder;
import com.hhd.multisocket514.base.BaseFragment;
import com.hhd.multisocket514.bean.Music;
import com.hhd.multisocket514.bean.SoundDean;
import com.hhd.multisocket514.business.DecodePlayer;
import com.hhd.multisocket514.interfac.CallBackIP;
import com.hhd.multisocket514.interfac.CallBackMusic;
import com.hhd.multisocket514.receiver.ScanSdFilesReceiver;
import com.hhd.multisocket514.utils.HandlerManager;
import com.hhd.multisocket514.utils.LogUtil;
import com.hhd.multisocket514.utils.MediaUtil;
import com.hhd.multisocket514.utils.MusicConstantValue;
import com.hhd.multisocket514.utils.PromptManager;

public class SendMusicFrag8 extends BaseFragment {
	protected static final String TAG = "MusicFrag";
	private ListView songListView;
	private SendSongListAdapter songAdapter;
	private ScanSdFilesReceiver scanReceiver;

	private CallBackMusic callBackMusic;

	private View view;

	private String musicName = null;

	private CallBackIP callBackIP;

	private String IP;
	private String button;
	private SendMusicToSoundFrag8 sendMusicToSoundFrag8;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MusicConstantValue.STARTED:
				// 开始刷新播放列表界面
				PromptManager.showProgressDialog(ct);
				break;
			case MusicConstantValue.FINISHED:
				// 结束刷新播放列表界面
				MediaUtil.getInstacen().initMusics(ct);
				PromptManager.closeProgressDialog();
				songAdapter.notifyDataSetChanged();
				ct.unregisterReceiver(scanReceiver);
				break;

			}

		}
	};



	@Override
	protected View initView(LayoutInflater inflater) {

		HandlerManager.putHandler(handler);
		view = inflater.inflate(R.layout.frag_sendmusic, null);
		loadSongList();

		return view;
	}

	@Override
	protected void initData(Bundle savedInstanceState) {
		setLitener();

	}




	private void setLitener() {

		songListView.setOnItemClickListener(new OnItemClickListener() {

			

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				MediaUtil.CURRENTPOS = position;

				List<Music> songList = MediaUtil.getInstacen().getSongList();
				if (songList != null && songList.size() > 0) {

					Music music = MediaUtil.getInstacen().getSongList()
							.get(MediaUtil.CURRENTPOS);
					if (music.getIschecked() == true) {
						music.setIschecked(false);
						music.setTitleColor(Color.BLACK);

					} else {
						music.setIschecked(true);
						music.setTitleColor(Color.RED);

						final String path = music.getPath();
						LogUtil.i("音乐的地址是：" + path);
						LogUtil.i("音乐的URL地址是：" + Uri.parse(path));

						musicName = music.getTitle();

						//

						FragmentManager fm = getActivity().getFragmentManager();
						FragmentTransaction transaction = fm.beginTransaction();

					    sendMusicToSoundFrag8 = new SendMusicToSoundFrag8();
						LogUtil.i("IP地址是：" + IP + "\n" + "按钮：" + button);
						transaction.replace(R.id.container,
								sendMusicToSoundFrag8, "sendMusicToSoundFrag8");
						transaction.commit();

					}

				}

				songAdapter.notifyDataSetChanged();

			}
		});

	}

	private void loadSongList() {

		MediaUtil.getInstacen().initMusics(ct);// 在手机的多媒体数据库中查询声音
		songAdapter = new SendSongListAdapter(ct);
		songListView = (ListView) view.findViewById(R.id.play_list);
		songListView.setAdapter(songAdapter);

	}

	private void changeNotice(int color) {
		TextView tx = (TextView) songListView
				.findViewWithTag(MediaUtil.CURRENTPOS);
		if (tx != null) {
			tx.setTextColor(color);
		}
	}

	/**
	 * 音乐资源过多
	 */
	class InitDataTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			PromptManager.showProgressDialog(ct);
			SystemClock.sleep(500);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// 加载多媒体信息
			MediaUtil.getInstacen().initMusics(ct);
			SystemClock.sleep(1000);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			PromptManager.closeProgressDialog();
			songAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void processClick(View v) {

	}

}
