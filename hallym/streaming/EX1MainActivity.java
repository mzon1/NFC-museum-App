package com.hallym.streaming;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;

import com.hallym.login_v1001.R;
import com.hallym.testnfc.MainActivity;


public class EX1MainActivity extends Activity implements OnClickListener {

	private ImageButton btText, btPic, btMusic, btVideo;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_ex1main);

		btText = (ImageButton) findViewById(R.id.Ex1_btText);
		btText.setOnClickListener(this);
		btPic = (ImageButton) findViewById(R.id.Ex1_btPic);
		btPic.setOnClickListener(this);
		btMusic = (ImageButton) findViewById(R.id.Ex1_btMusic);
		btMusic.setOnClickListener(this);
		btVideo = (ImageButton) findViewById(R.id.Ex1_btVideo);
		btVideo.setOnClickListener(this);
	}

	public void onClick(View v) {
		String path = null;
		
		if( v==btText ) {}
		else if( v==btPic ) {}
		else if( v==btMusic ) {}
		else if( v==btVideo ) {
			Intent intent;
			path = MainActivity.Path;

			intent = new Intent(this, EX1VideoActivity.class);
//			intent = new Intent( Intent.ACTION_VIEW, Uri.parse(path) );
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intent.putExtra("Path", path);
			
			startActivity(intent);
		}
	}

}
