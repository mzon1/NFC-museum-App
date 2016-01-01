package com.hallym.streaming;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Space;
import android.widget.TextView;

import com.hallym.time.MyToast;

public class EX4SoundLayout extends EX4Layout implements
		OnSeekBarChangeListener,
		OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener, OnErrorListener,
		OnClickListener {
	
	private Context context;
	
	private MediaPlayer mMediaPlayer;
	private int mediaSeek;
	private Uri uri;

	private TextView tvSubtitle, tvCaption;
	private LinearLayout mSubtitleLayout, mMediaControllerLayout, mMediaController, mName;
//	private ProgressThread PT;
	private ProgressTask PT;
	private boolean isPrepare;
	private SeekBar seekProgress;
	private TextView seekCurrentsec, seekMaxsec;
	private Button btPlay, btStop;
	boolean flag = true;

	private int MEDIA_MAX_LENGTH;

	public EX4SoundLayout(Context context) {
		super(context);
		
		this.context = context;
		
		setupLayout(context);
		
		playMedia();
	}

	public EX4SoundLayout(Context context, String uri) {
		super(context);
		setUri(uri);
		
		this.context = context;
		
		setupLayout(context);
		
		playMedia();
	}

	private void playMedia() {
		// doCleanUp();
		try {
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setDataSource(context, uri);
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mMediaPlayer.prepare();
			mMediaPlayer.setOnBufferingUpdateListener(this);
			mMediaPlayer.setOnCompletionListener(this);
			mMediaPlayer.setOnPreparedListener(this);
			mMediaPlayer.setScreenOnWhilePlaying(true);
			mMediaPlayer.setOnErrorListener(this);
			
//			setProgressTh();
//			mMediaPlayer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onDestroy() {
		int min = mediaSeek / 60000;
		int sec = (mediaSeek % 60000)/1000;
		String seekstr = String.format("%02d : %02d", min, sec);
		
		MyToast.showToast_Time(context, "Sound Close. " + seekstr);
		
		if( PT != null ) {
//			isThreadAlive = false;
			while( !PT.isCancelled() )		PT.cancel(true);
		}
		PT = null;
		
		flag = false;
		
		if (mMediaPlayer != null)
			mMediaPlayer.release();
	}

	private void setProgressTh() {
		if (mMediaPlayer == null)
			return;

		boolean isPlay = mMediaPlayer.isPlaying();

		if (isPlay) {
			MyToast.showToast_Time(context, "Sound Pause");
			mMediaPlayer.pause();

			if (PT != null) {
//				isThreadAlive = false;
				if( PT.isCancelled() ) {
					PT.cancel(true);
				}
				PT = null;
			}
		} else {
			MyToast.showToast_Time(context, "Sound Start");
			
			mMediaPlayer.start();

			if (PT == null) {
				PT = new ProgressTask(seekProgress);
//				isThreadAlive = true;
				PT.execute();
			}
		}
		
		setPlayButton();
	}
	
	private void setPlayButton() {
		if( mMediaPlayer!=null ) {
			boolean isPlay = mMediaPlayer.isPlaying();
	
			if (isPlay) {btPlay.setText("||");}
			else {btPlay.setText("|>");}
		}
		else {
			btPlay.setText("Loding");
		}
	}
	
	@SuppressWarnings("deprecation")
	private void setupLayout(Context context) {
		Space space;
		
		setOnClickListener(this);
		
		// subtitle Layout
		mSubtitleLayout = new LinearLayout(context);
		mSubtitleLayout.setLayoutParams( new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT) );
		mSubtitleLayout.setBackgroundColor(0xffffffff);
		mSubtitleLayout.setOrientation(LinearLayout.VERTICAL);
		mSubtitleLayout.setScrollBarStyle(SCROLLBARS_INSIDE_OVERLAY);
		
			tvSubtitle = new TextView(context);
			tvSubtitle.setLayoutParams( new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT) );
			tvSubtitle.setText("");
			
		mSubtitleLayout.addView(tvSubtitle);
		this.addView(mSubtitleLayout);
		
		// all Layout
		mMediaControllerLayout = new LinearLayout(context);
		mMediaControllerLayout.setLayoutParams( new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT) );
		mMediaControllerLayout.setOrientation(LinearLayout.VERTICAL);
		mMediaControllerLayout.setVisibility(LinearLayout.VISIBLE);
		
			mName = new LinearLayout(context);
			mName.setLayoutParams( new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					64) );
			mName.setBackgroundColor(0x88000000);
		mMediaControllerLayout.addView(mName);
			
			space = new Space(context);
			space.setLayoutParams( new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					0, 1.0f) );
		mMediaControllerLayout.addView(space);
		
			// caption Layout
			LinearLayout captionLayout = new LinearLayout(context);
			captionLayout.setLayoutParams( new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT) );
				
				space = new Space(context);
				space.setLayoutParams( new LinearLayout.LayoutParams(
						0,
						ViewGroup.LayoutParams.FILL_PARENT, 1.0f) );
			captionLayout.addView(space);

				tvCaption = new TextView(context);
				tvCaption.setLayoutParams( new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT) );
				tvCaption.setBackgroundColor(0xff000000);
//				LinearLayout.LayoutParams plControl = (LinearLayout.LayoutParams) tvCaption.getLayoutParams();
//				plControl.leftMargin = 4;
//				plControl.rightMargin = 4;
//				plControl.topMargin = 4;
//				plControl.bottomMargin = 4;
//				tvCaption.setLayoutParams(plControl);
				tvCaption.setText("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
				tvCaption.setTextColor(0xffffffff);
				
				space = new Space(context);
				space.setLayoutParams( new LinearLayout.LayoutParams(
						0,
						ViewGroup.LayoutParams.FILL_PARENT, 1.0f) );
			captionLayout.addView(space);
		mMediaControllerLayout.addView(captionLayout);
			
			
			mMediaController = new LinearLayout(context);
			mMediaController.setLayoutParams( new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					144) );
			mMediaController.setOrientation(LinearLayout.VERTICAL);
			mMediaController.setBackgroundColor(0x88000000);
			mMediaController.setOnClickListener(this);
			
				LinearLayout seekTextLayout = new LinearLayout(context);
				seekTextLayout.setLayoutParams( new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.FILL_PARENT,
						32) );
					seekCurrentsec = new TextView(context);
					seekCurrentsec.setLayoutParams( new LinearLayout.LayoutParams(
							ViewGroup.LayoutParams.WRAP_CONTENT,
							ViewGroup.LayoutParams.WRAP_CONTENT) );
					seekCurrentsec.setTextColor(0xffffffff);
				seekTextLayout.addView(seekCurrentsec);
					
					space = new Space(context);
					space.setLayoutParams( new LinearLayout.LayoutParams(
							0,
							ViewGroup.LayoutParams.FILL_PARENT, 1.0f) );
				seekTextLayout.addView(space);
					
					seekMaxsec = new TextView(context);
					seekMaxsec.setLayoutParams( new LinearLayout.LayoutParams(
							ViewGroup.LayoutParams.WRAP_CONTENT,
							ViewGroup.LayoutParams.WRAP_CONTENT) );
					seekMaxsec.setTextColor(0xffffffff);
				seekTextLayout.addView(seekMaxsec);
			mMediaController.addView(seekTextLayout);

				seekProgress = new SeekBar(context);
				seekProgress.setLayoutParams( new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.FILL_PARENT,
						32) );
				seekProgress.setMax(10000);
				seekProgress.setOnSeekBarChangeListener(this);
				seekProgress.setEnabled(false);
//				isThreadAlive = false;
				isPrepare = false;
			mMediaController.addView(seekProgress);
			
				LinearLayout buttonLayout = new LinearLayout(context);
				buttonLayout.setLayoutParams( new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.FILL_PARENT,
						96) );
					space = new Space(context);
					space.setLayoutParams( new LinearLayout.LayoutParams(
							0,
							ViewGroup.LayoutParams.FILL_PARENT, 1.0f) );
				buttonLayout.addView(space);
				
					LinearLayout buttonGroupLayout = new LinearLayout(context);
					buttonGroupLayout.setLayoutParams( new LinearLayout.LayoutParams(
							ViewGroup.LayoutParams.WRAP_CONTENT,
							ViewGroup.LayoutParams.FILL_PARENT) );
						btPlay = new Button(context);
						btPlay.setLayoutParams( new LinearLayout.LayoutParams(
								88,
								88) );
						btPlay.setText("Lodding");
						btPlay.setEnabled(false);
						btPlay.setOnClickListener(this);
					buttonGroupLayout.addView(btPlay);
					
						btStop = new Button(context);
						btStop.setLayoutParams( new LinearLayout.LayoutParams(
								88,
								88) );
						btStop.setText("Lodding");
						btStop.setEnabled(false);
						btStop.setOnClickListener(this);
					buttonGroupLayout.addView(btStop);
				buttonLayout.addView(buttonGroupLayout);
				
					space = new Space(context);
					space.setLayoutParams( new LinearLayout.LayoutParams(
							0,
							ViewGroup.LayoutParams.FILL_PARENT, 1.0f) );
				buttonLayout.addView(space);
			mMediaController.addView(buttonLayout);
			
		mMediaControllerLayout.addView(mMediaController);
		this.addView(mMediaControllerLayout);
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		MyToast.showToast(context, "Sound Load");
//		MEDIA_MAX_LENGTH = mp.getDuration();
//		Log.i("", "MAX : " + MEDIA_MAX_LENGTH);
		
		btPlay.setEnabled(true);
		isPrepare = true;
//		setPlayButton();

		seekProgress.setEnabled(true);
		seekCurrentsec.setText("00 : 00");
		mediaSeek = 0;
		setProgressTh();
//		mMediaPlayer.start();

//		int min = MEDIA_MAX_LENGTH / 60000;
//		int sec = (MEDIA_MAX_LENGTH % 60000)/1000;
//		String seekstr = String.format("%02d : %02d", min, sec);
//		seekMaxsec.setText( "" );
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// 불리는 타이밍을 모르겠음
//		MyToast.showToast(context, "Movie Complete");
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int arg1) {
		// 불리는 타이밍을 모르겠음
		// TODO Auto-generated method stub
	}

	@Override
	public void onClick(View v) {
		if (v == btPlay) {
			setProgressTh();
		}

		if (v == mSubtitleLayout) {
			if (mMediaControllerLayout.getVisibility() == LinearLayout.VISIBLE) {
				mMediaControllerLayout.setVisibility(LinearLayout.INVISIBLE);
			} else {
				mMediaControllerLayout.setVisibility(LinearLayout.VISIBLE);
			}
		}
	}
//	public void setProgressBar(int progressRate) {
//		seekProgress.setProgress(progressRate);
//	}
	
	private class ProgressTask extends AsyncTask<Void, Void, Void> {
		private SeekBar sp;
		
		public ProgressTask(SeekBar SP) {
			sp = SP;
		}
		
		protected Void doInBackground(Void... params) {
//			while( isThreadAlive ) {
			while( !isCancelled() ) {
				if (mMediaPlayer == null)	return null;
				
				publishProgress();
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			if (mMediaPlayer == null)	return;
			if(flag)
				mediaSeek = mMediaPlayer.getCurrentPosition();
			
			int min = mediaSeek / 60000;
			int sec = (mediaSeek % 60000) / 1000;
			String seekstr = String.format("%02d : %02d", min, sec);
			seekCurrentsec.setText(seekstr);

			if (mMediaPlayer == null)	return;
			if(flag)
				MEDIA_MAX_LENGTH = mMediaPlayer.getDuration();
			
			float mediaRate = mediaSeek*1.0f / MEDIA_MAX_LENGTH*1.0f;
			int progressRate = (int) (mediaRate * sp.getMax());

			if (sp == null)	return;
			sp.setProgress( progressRate );
			
			setPlayButton();
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {

		if (fromUser) {
			float rate = seekBar.getProgress() * 1.0f / seekBar.getMax();
			int msec = (int) (mMediaPlayer.getDuration() * rate);

			int min = msec / 60000;
			int sec = (msec % 60000) / 1000;
			String seekstr = String.format("%02d : %02d", min, sec);
			seekCurrentsec.setText(seekstr);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		float rate = seekBar.getProgress() * 1.0f / seekBar.getMax();
		int msec = (int) (mMediaPlayer.getDuration() * rate);
		mMediaPlayer.seekTo(msec);

		int min = msec / 60000;
		int sec = (msec % 60000) / 1000;
		String seekstr = String.format("%02d : %02d", min, sec);

		MyToast.showToast_Time(context, "Seek. " + seekstr);
	}

	@Override
	public void setUri(String uri) {
		this.uri = Uri.parse(uri + "sound.mp3");
		Log.i("", "setUri : " + uri);
		
	}
	
	public int setEvent(int eventNum) {
		switch(eventNum) {
		case EVENT_CHANGEIN:
			if( mMediaPlayer != null ) {
				
				
				if( isPrepare ){
					Log.i("", "Sound Start. " + mediaSeek);
					
					setProgressTh();
	//				mMediaPlayer.start();
					mMediaPlayer.seekTo(mediaSeek);
				}
				return EVENT_RETURN_END;
			}
			else {
				return EVENT_RETURN_NULL;
			}
		case EVENT_CHANGEOUT:
			if( mMediaPlayer != null ) {
				if( isPrepare ){
					mediaSeek = mMediaPlayer.getCurrentPosition();

					Log.i("", "Sound Pause. " + mediaSeek);
					mMediaPlayer.pause();
				}
				return EVENT_RETURN_END;
			}
			else {
				return EVENT_RETURN_NULL;
			}
		default:
			return EVENT_RETURN_ERROR;
		}
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		Log.i("", "Error : " + what + ", " + extra);
		
		return false;
	}
}
