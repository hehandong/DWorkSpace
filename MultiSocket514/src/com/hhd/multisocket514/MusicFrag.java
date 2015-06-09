package com.hhd.multisocket514;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hhd.multisocket514.utils.HandlerManager;

import com.hhd.multisocket.R;
import com.hhd.multisocket514.adapter.MySongListAdapter;
import com.hhd.multisocket514.base.BaseFragment;
import com.hhd.multisocket514.bean.Music;
import com.hhd.multisocket514.receiver.ScanSdFilesReceiver;
import com.hhd.multisocket514.service.MediaService;
import com.hhd.multisocket514.utils.MediaUtil;
import com.hhd.multisocket514.utils.MusicConstantValue;
import com.hhd.multisocket514.utils.PromptManager;

public class MusicFrag extends BaseFragment {
	protected static final String TAG = "MusicFrag";
	/************ 资源加载 ****************/
	private ListView songListView;
	private MySongListAdapter songAdapter;

	private ScanSdFilesReceiver scanReceiver;
	private ImageView reflashSongListImageView;
	
	private View view;

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
			case MusicConstantValue.PLAY_END:
				// 播放完成
				// 播放模式：单曲循环、顺序播放、循环播放、随机播放
				// 单曲循环:记录当前播放位置
				// 顺序播放:当前播放位置上＋1
				// 循环播放:判断如果，增加的结果大于songList的大小，修改播放位置为零
				// 随机播放:Random.nextInt() songList.size();

				MediaUtil.CURRENTPOS++;

				if (MediaUtil.CURRENTPOS < MediaUtil.getInstacen()
						.getSongList().size()) {
					Music music = MediaUtil.getInstacen().getSongList()
							.get(MediaUtil.CURRENTPOS);
					startPlayService(music, MusicConstantValue.OPTION_PLAY);
					changeNotice(Color.GREEN);

				}

				break;
			}

		}
	};
	/***********************************/

	/************* 音乐控制 ****************/
	private ImageView playPause;// 播放暂停
	private ImageView playNext;// 播放下一首
	private ImageView playPrev;// 播放上一首
	private ImageView playMode;// 修改播放模式
	

	/**********************************/

	@Override
	protected View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.frag_music, null);
	
		return view;
	}

	@Override
	protected void initData(Bundle savedInstanceState) {
		HandlerManager.putHandler(handler);
		init();
		setLitener();

	}
	
	private void setLitener() {
		playPause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				switch (MediaUtil.PLAYSTATE) {
				case MusicConstantValue.OPTION_PLAY:
				case MusicConstantValue.OPTION_CONTINUE:
					startPlayService(null, MusicConstantValue.OPTION_PAUSE);
					playPause.setImageResource(R.drawable.appwidget_pause);
					break;
				case MusicConstantValue.OPTION_PAUSE:
					if (MediaUtil.CURRENTPOS >= 0
							&& MediaUtil.CURRENTPOS < MediaUtil.getInstacen()
									.getSongList().size()) {
						startPlayService(MediaUtil.getInstacen().getSongList()
								.get(MediaUtil.CURRENTPOS),
								MusicConstantValue.OPTION_CONTINUE);
						playPause
								.setImageResource(R.drawable.img_playback_bt_play);

					}
					break;
				}
			}
		});

		playNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// int temp=MediaUtil.CURRENTPOS;
				if (MediaUtil.getInstacen().getSongList().size() > MediaUtil.CURRENTPOS + 1) {
					changeNotice(Color.BLACK);
					MediaUtil.CURRENTPOS++;
					startPlayService(
							MediaUtil.getInstacen().getSongList()
									.get(MediaUtil.CURRENTPOS),
							MusicConstantValue.OPTION_PLAY);
					playPause.setImageResource(R.drawable.img_playback_bt_play);
					MediaUtil.PLAYSTATE = MusicConstantValue.OPTION_PLAY;
					changeNotice(Color.GREEN);
				}

			}
		});
		playPrev.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (MediaUtil.CURRENTPOS > 0) {
					changeNotice(Color.BLACK);
					MediaUtil.CURRENTPOS--;
					startPlayService(
							MediaUtil.getInstacen().getSongList()
									.get(MediaUtil.CURRENTPOS),
							MusicConstantValue.OPTION_PLAY);
					playPause.setImageResource(R.drawable.img_playback_bt_play);
					MediaUtil.PLAYSTATE = MusicConstantValue.OPTION_PLAY;

					changeNotice(Color.GREEN);
				}

			}
		});

		songListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				changeNotice(Color.BLACK);

				MediaUtil.CURRENTPOS = position;
				Music music = MediaUtil.getInstacen().getSongList()
						.get(MediaUtil.CURRENTPOS);
				startPlayService(music, MusicConstantValue.OPTION_PLAY);
				playPause.setImageResource(R.drawable.img_playback_bt_play);
				// songAdapter.notifyDataSetChanged();
				changeNotice(Color.GREEN);

			}
		});

	}

	private void startPlayService(Music music, int option) {
		Intent intent = new Intent(ct, MediaService.class);
		if (music != null) {
			intent.putExtra("file", music.getPath());
		}
		intent.putExtra("option", option);
		ct.startService(intent);
	}

	private void init() {
		loadSongList();

		mediaController();

		reflashSongList();

	}

	/**
	 * 刷新播放列表
	 */
	private void reflashSongList() {
		
		// 刷新列表
		reflashSongListImageView = (ImageView) view.findViewById(R.id.title_right);
		reflashSongListImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				reflash();
			}
		});
	}

	private void mediaController() {
		playPause = (ImageView)  view.findViewById(R.id.imgPlay);
		playPrev = (ImageView) view.findViewById(R.id.imgPrev);
		playNext = (ImageView) view.findViewById(R.id.imgNext);

		if (MediaUtil.PLAYSTATE == MusicConstantValue.OPTION_PAUSE) {
			playPause.setImageResource(R.drawable.appwidget_pause);
		}

	}

	private void loadSongList() {
		// MediaUtil.getInstacen().initMusics(getApplicationContext());//在手机的多媒体数据库中查询声音
		songAdapter = new MySongListAdapter(ct);
		songListView = (ListView) view.findViewById(R.id.play_list);
		songListView.setAdapter(songAdapter);

		// new InitDataTask().execute();//线程池，如操作的线程过多，等待情况
		new InitDataTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);// 不用等待
	}

	public void reflash() {
		// Intent intent = new Intent();
		// intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
		// intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
		// sendBroadcast(intent);

		IntentFilter intentFilter = new IntentFilter(
				Intent.ACTION_MEDIA_SCANNER_STARTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
		intentFilter.addDataScheme("file");
		scanReceiver = new ScanSdFilesReceiver();
		ct.registerReceiver(scanReceiver, intentFilter);
		ct.sendBroadcast(new Intent(
				Intent.ACTION_MEDIA_MOUNTED,
				Uri.parse("file://" + Environment.getExternalStorageDirectory())));
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
		}

		@Override
		protected Void doInBackground(Void... params) {
			// 加载多媒体信息
			MediaUtil.getInstacen().initMusics(ct);
			// SystemClock.sleep(100);
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
