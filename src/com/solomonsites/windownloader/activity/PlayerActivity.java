package com.solomonsites.windownloader.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class PlayerActivity extends Activity {

	private VideoView mVideoView;
	private MediaController mc;
	boolean pousing = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_layout);
		Bundle mBundle = getIntent().getBundleExtra("url");
		String url = mBundle.getString("url");
		//String url = "http://youtube.com/get_video?video_id=ZmX7zLCrcAI&t=vjVQa1PpcFPmrj_j5y370BhPYfq3qHoWsFICYcBqEl4%3D&asv=&fmt=18";
		mVideoView = (VideoView) findViewById(R.id.play_video);
		mc = new MediaController(getBaseContext());
		mc.setAnchorView(mVideoView);
		mVideoView.setMediaController(mc);
		mVideoView.setVideoURI(Uri.parse(url));
		mVideoView.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
				mp.start();
				mc.show();
				
			}
		});
		mVideoView.requestFocus();
		mVideoView.showContextMenu();	    	
	}
	
	
}
