package com.hallym.streaming;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
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

public class EX4VideoLayout extends EX4Layout implements
		SurfaceHolder.Callback, OnSeekBarChangeListener,
		OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener, OnErrorListener, 
		OnVideoSizeChangedListener, OnClickListener {
	
	private Context context;
	
	public static MediaPlayer mMediaPlayer = null;
	private int mediaSeek;
	private Uri uri;
	private SurfaceHolder mHolder;
	private SurfaceView suvi;

	private LinearLayout mMediaControllerLayout, mMediaController, mName;
	private ProgressTask PT;
	private boolean isPrepare;
	private SeekBar seekProgress;
	private TextView seekCurrentsec, seekMaxsec;
	private Button btPlay, btStop;
	
	boolean flag = true;

	private int MEDIA_MAX_LENGTH;

	public EX4VideoLayout(Context context) {
		super(context);
		
		this.context = context;

		/*
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		//setOnClickListener(this);
		/*/
		setupLayout(context);
		//*/
	}
	
	public EX4VideoLayout(Context context, String uri) {
		super(context);
		setUri(uri);
		
		this.context = context;

		/*
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		//setOnClickListener(this);
		/*/
		setupLayout(context);
		//*/
	}

	private void playVideo(SurfaceHolder holder) {
		// doCleanUp();
		try {
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setDataSource(context, uri);
			mMediaPlayer.setDisplay(holder);
			mMediaPlayer.prepare();
			mMediaPlayer.setOnBufferingUpdateListener(this);
			mMediaPlayer.setOnCompletionListener(this);
			mMediaPlayer.setOnPreparedListener(this);
			mMediaPlayer.setScreenOnWhilePlaying(true);
			mMediaPlayer.setOnVideoSizeChangedListener(this);
			mMediaPlayer.setOnErrorListener(this);
			// mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setProgressTh() {
		if (mMediaPlayer == null)
			return;

		boolean isPlay = mMediaPlayer.isPlaying();

		if (isPlay) {
			MyToast.showToast_Time(context, "Movie Pause");
			mMediaPlayer.pause();

			if (PT != null) {
//				isThreadAlive = false;
				if( PT.isCancelled() ) {
					PT.cancel(true);
				}
				PT = null;
			}
		} else {
			MyToast.showToast_Time(context, "Movie Start");
			
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
		boolean isPlay = mMediaPlayer.isPlaying();

		if (isPlay) {btPlay.setText("||");}
		else {btPlay.setText("|>");}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.i("surfaceChanged", "surfaceChanged");
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		playVideo(mHolder);
		Log.i("surfaceCreate", "surfaceCreate");
	}
	
	@SuppressWarnings("deprecation")
	private void setupLayout(Context context) {
		Space space;
		
		setOnClickListener(this);
		
		suvi = new SurfaceView(context);
		suvi.setLayoutParams( new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT)
		);
		suvi.setOnClickListener(this);

		mHolder = suvi.getHolder();
		mHolder.addCallback(this);
//		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//		mHolder.setType(SurfaceHolder.SURFACE_TYPE_HARDWARE);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
//		mHolder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
		
		this.addView(suvi);
		
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
	public void surfaceDestroyed(SurfaceHolder holder) {
		onDestroy();
	}

	@Override
	public void onDestroy() {
		int min = mediaSeek / 60000;
		int sec = (mediaSeek % 60000)/1000;
		String seekstr = String.format("%02d : %02d", min, sec);
		
		MyToast.showToast_Time(context, "Movie Close. " + seekstr);

		if( PT != null ) {
//			isThreadAlive = false;
			while( !PT.isCancelled() )		PT.cancel(true);
		}
		PT = null;
		
		flag = false;
		
		if (mMediaPlayer != null)
			mMediaPlayer.release();
	}

	@Override
	public void onVideoSizeChanged(MediaPlayer arg0, int arg1, int arg2) {

	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		MyToast.showToast(context, "Movie Load");
//		MEDIA_MAX_LENGTH = mp.getDuration();
//		Log.i("", "MAX : " + MEDIA_MAX_LENGTH);
		
		btPlay.setEnabled(true);
//		setPlayButton();

		seekProgress.setEnabled(true);
		seekCurrentsec.setText("00 : 00");
		mediaSeek = 0;
		setProgressTh();
//		mMediaPlayer.start();

//		int min = MEDIA_MAX_LENGTH / 60000;
//		int sec = (MEDIA_MAX_LENGTH % 60000)/1000;
//		String seekstr = String.format("%02d : %02d", min, sec);
		seekMaxsec.setText( "" );
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// 불리는 타이밍을 모르겠음
		MyToast.showToast(context, "Movie Complete");
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

		if (v == suvi) {
			if (mMediaControllerLayout.getVisibility() == LinearLayout.VISIBLE) {
				mMediaControllerLayout.setVisibility(LinearLayout.INVISIBLE);
			} else {
				mMediaControllerLayout.setVisibility(LinearLayout.VISIBLE);
			}
		}
	}
	public void setProgressBar(int progressRate) {
		seekProgress.setProgress(progressRate);
	}
	
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
			else	mediaSeek = 0;
			
			int min = mediaSeek / 60000;
			int sec = (mediaSeek % 60000) / 1000;
			String seekstr = String.format("%02d : %02d", min, sec);
			seekCurrentsec.setText(seekstr);

			if (mMediaPlayer == null)	return;
			if(flag)
				MEDIA_MAX_LENGTH = mMediaPlayer.getDuration();
			else	MEDIA_MAX_LENGTH = 0;
			
			float mediaRate = mediaSeek*1.0f / MEDIA_MAX_LENGTH*1.0f;
			int progressRate = (int) (mediaRate * sp.getMax());

			if (sp == null)	return;
			sp.setProgress(progressRate);
//			setProgressBar(progressRate);
			
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
		this.uri = Uri.parse(uri + "video.m3u8");
	}
	
	public int setEvent(int eventNum) {
		switch(eventNum) {
		case EVENT_CHANGEIN:
			if( mMediaPlayer != null ) {
				
				if( isPrepare ){
					Log.i("", "Movie Start. " + mediaSeek);
					
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

					Log.i("", "Movie Pause. " + mediaSeek);
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
		String strw = "";
		
		switch( what ) {
		case MediaPlayer.MEDIA_ERROR_UNKNOWN:		strw = "MEDIA_ERROR_UNKNOWN";	break;
		case MediaPlayer.MEDIA_ERROR_SERVER_DIED:	strw = "MEDIA_ERROR_SERVER_DIED";	break;
		}
		strw += "("+what+")";

		String stre = "";
		
		switch( extra ) {
		case MediaPlayer.MEDIA_ERROR_IO:		stre = "MEDIA_ERROR_IO";		break;
		case MediaPlayer.MEDIA_ERROR_MALFORMED:	stre = "MEDIA_ERROR_MALFORMED";	break;
		case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:stre = "MEDIA_ERROR_UNSUPPORTED";	break;
		case MediaPlayer.MEDIA_ERROR_TIMED_OUT:	stre = "MEDIA_ERROR_TIMED_OUT";	break;
		}
		stre += "("+what+")";
		
		
//		switch( what )
		Log.i("", "Error : " + strw + ", " + stre);
		
		return false;
	}
}
