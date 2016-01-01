package com.hallym.streaming;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.MediaController;
import android.widget.VideoView;

public class EX1VideoActivity extends Activity {

	private VideoView mVideoView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		mVideoView = new VideoView(this);

		Uri uri = Uri.parse(this.getIntent().getExtras().getString("Path"));
		mVideoView.setVideoURI(uri);
		mVideoView.setMediaController(new MediaController(this));
		mVideoView.setOnPreparedListener(new OnPreparedListener() {
			public void onPrepared(MediaPlayer mp) {
				mp.start();
			}
		});

		setContentView(mVideoView);
	}

	protected void onDestroy() {
		if (mVideoView.isPlaying())
			mVideoView.stopPlayback();
		super.onDestroy();
	}
}
