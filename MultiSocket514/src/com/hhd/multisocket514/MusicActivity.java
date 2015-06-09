package com.hhd.multisocket514;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hhd.multisocket.R;
import com.hhd.multisocket514.adapter.SendSongListAdapter;
import com.hhd.multisocket514.bean.Music;
import com.hhd.multisocket514.interfac.CallBackIP;
import com.hhd.multisocket514.interfac.CallBackMusic;
import com.hhd.multisocket514.receiver.ScanSdFilesReceiver;
import com.hhd.multisocket514.utils.HandlerManager;
import com.hhd.multisocket514.utils.LogUtil;
import com.hhd.multisocket514.utils.MediaUtil;
import com.hhd.multisocket514.utils.MusicConstantValue;
import com.hhd.multisocket514.utils.PromptManager;

public class MusicActivity extends Activity implements OnClickListener {
	protected static final String TAG = "MusicFrag";
	private ListView songListView;
	private SendSongListAdapter songAdapter;
	private ScanSdFilesReceiver scanReceiver;

	private CallBackMusic callBackMusic;


	private String musicName = null;

	private CallBackIP callBackIP;

	
	
	private ImageView btnClose;
	

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MusicConstantValue.STARTED:
				// 开始刷新播放列表界面
				PromptManager.showProgressDialog(MusicActivity.this);
				break;
			case MusicConstantValue.FINISHED:
				// 结束刷新播放列表界面
				MediaUtil.getInstacen().initMusics(MusicActivity.this);
				PromptManager.closeProgressDialog();
				songAdapter.notifyDataSetChanged();
				unregisterReceiver(scanReceiver);
				break;

			}

		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frag_sendmusic);
		btnClose = (ImageView) findViewById(R.id.title_close);
		btnClose.setOnClickListener(this);
		
		HandlerManager.putHandler(handler);
		loadSongList();
		setLitener();
		
//		Intent intent = getIntent();
//		String IP = intent.getStringExtra("IP");
//		String Button = intent.getStringExtra("Button");
//		LogUtil.i("接受到的IP-------"+IP);
//		LogUtil.i("接收到的Button---"+Button);
	}
	
	private void loadSongList() {
		// new InitDataTask().execute();//线程池，如操作的线程过多，等待情况
		// new
		// InitDataTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);//
		// 不用等待

		MediaUtil.getInstacen().initMusics(getApplicationContext());// 在手机的多媒体数据库中查询声音
		songAdapter = new SendSongListAdapter(getApplicationContext());
		songListView = (ListView) findViewById(R.id.play_list);
		songListView.setAdapter(songAdapter);
		
//		songAdapter = new SendSongListAdapter(getApplicationContext());
//		songListView = (ListView) findViewById(R.id.play_list);
//		songListView.setAdapter(songAdapter);
//
//		// new InitDataTask().execute();//线程池，如操作的线程过多，等待情况
//		new InitDataTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);// 不用等待
	} 
	
	private void setLitener() {
		songListView.setItemsCanFocus(false);
		songListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		songListView.setOnItemClickListener(new OnItemClickListener() {

			private SendMusicToSoundFrag sendMusicToSoundFrag;

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
						
						Intent intent = getIntent();
						
						intent.putExtra("reIP", intent.getStringExtra("IP"));
						intent.putExtra("reButton", intent.getStringExtra("Button"));
						intent.putExtra("MusicName", musicName);
						
//						intent.putExtra("postition", intent.getIntArrayExtra("postition"));
						intent.putExtra("postition", intent.getIntExtra("postition", 1));
						
				
						MusicActivity.this.setResult(10, intent);
						

						

						
						
					}

				}

				songAdapter.notifyDataSetChanged();

			}
		});

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
			PromptManager.showProgressDialog(MusicActivity.this);
//			SystemClock.sleep(500);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// 加载多媒体信息
			MediaUtil.getInstacen().initMusics(MusicActivity.this);
//			SystemClock.sleep(1000);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			PromptManager.closeProgressDialog();
			songAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {
          MusicActivity.this.finish();	
	}


}
