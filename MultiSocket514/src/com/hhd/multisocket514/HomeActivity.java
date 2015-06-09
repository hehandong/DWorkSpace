package com.hhd.multisocket514;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.hhd.multisocket.R;

public class HomeActivity extends Activity implements OnClickListener {

	private RadioButton rbSearchServer;
	private RadioButton rbSearchDevice;
	private RadioButton rbMusicService;
	private RadioButton rbRecordServer;
	private RadioButton rbSetting;
	private SearchServerFrag searchServerFra;
	private ObtainDeviceFrag2 searchDeviceFra;
	private SendMusicToSoundFrag sendMusicToSoundFrag;
	private SendMusicToSoundFrag8 sendMusicToSoundFrag8;

	private QueryPlayStatusFrag queryPlayStatusFrag;
	private MusicFrag musicFrag;
	private QueryDeviceBuffFrag queryDeviceBuffFrag;
	private SendMusicFrag sendMusicFrag;
	private SendMusicFrag8 sendMusicFrag8;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		FrameLayout containerFra = (FrameLayout) findViewById(R.id.container);
		rbSearchServer = (RadioButton) findViewById(R.id.rb_searchserver);
		rbSearchDevice = (RadioButton) findViewById(R.id.rb_searchdevice);
		rbMusicService = (RadioButton) findViewById(R.id.rb_musicservice);
		rbRecordServer = (RadioButton) findViewById(R.id.rb_recordserver);
		rbSetting = (RadioButton) findViewById(R.id.rb_setting);
		rbSearchServer.setOnClickListener(this);
		rbSearchDevice.setOnClickListener(this);
		rbMusicService.setOnClickListener(this);
		rbRecordServer.setOnClickListener(this);
		rbSetting.setOnClickListener(this);

		// setDefaultFragment();
	}

	private void setDefaultFragment() {

		FragmentManager fm = getFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		searchServerFra = new SearchServerFrag();
		transaction.replace(R.id.container, searchServerFra);
		transaction.commit();

	}

	@Override
	public void onClick(View v) {

		FragmentManager fm = getFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();

		switch (v.getId()) {
		case R.id.rb_searchserver:
			if (searchServerFra == null) {
				searchServerFra = new SearchServerFrag();
			}
			transaction.replace(R.id.container, searchServerFra);

			break;
		case R.id.rb_searchdevice:
			if (searchDeviceFra == null) {
				searchDeviceFra = new ObtainDeviceFrag2();
			}
			transaction.replace(R.id.container, searchDeviceFra);

			break;

		case R.id.rb_setting:
			if (queryPlayStatusFrag == null) {
				queryPlayStatusFrag = new QueryPlayStatusFrag();
			}
			transaction.replace(R.id.container, queryPlayStatusFrag);

			break;

	/*	case R.id.rb_musicservice:
			if (musicFrag == null) {
				musicFrag = new MusicFrag();
			}
			transaction.replace(R.id.container, musicFrag);

			break;*/
			
		case R.id.rb_recordserver:
//			if (sendMusicToSoundFrag == null) {
//				  sendMusicToSoundFrag = new SendMusicToSoundFrag();
//			}
//			transaction.replace(R.id.container, sendMusicToSoundFrag,"sendMusicToSoundFrag");
//			transaction.addToBackStack(null);
			
//			if (sendMusicToSoundFrag8 == null) {
//				  sendMusicToSoundFrag8 = new SendMusicToSoundFrag8();
//			}
//			transaction.replace(R.id.container, sendMusicToSoundFrag8,"sendMusicToSoundFrag8");
//			transaction.addToBackStack(null);
			
			Intent SendMusicIntent = new Intent(HomeActivity.this, SendMusicToSoundActivity.class);
			startActivity(SendMusicIntent);
			
			

			break;
		case R.id.rb_musicservice:
			
//			if (sendMusicFrag == null) {
//				sendMusicFrag = new SendMusicFrag();
//			}
//			transaction.replace(R.id.container, sendMusicFrag,"MusicFrag");
//			transaction.addToBackStack(null);
			
			
//			if (sendMusicFrag == null) {
//				sendMusicFrag8 = new SendMusicFrag8();
//			}
//			transaction.replace(R.id.container, sendMusicFrag8,"MusicFrag8");
//			transaction.addToBackStack(null);
			
			
			Intent musicintent = new Intent(HomeActivity.this, MusicActivity.class);
			startActivity(musicintent);
			

			break;

		}
		transaction.commit();

	}

}
