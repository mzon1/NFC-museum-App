package com.hallym.fading;

import com.hallym.login_v1001.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;


public class Loading extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		Handler handler = new Handler();
		handler.postDelayed(new nextscene(), 1500);
	}
	class nextscene implements Runnable{
		public void run(){
			startActivity(new Intent(getApplication(),Loading2.class));
			overridePendingTransition(R.anim.fade, R.anim.fade2);
			Loading.this.finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.loading, menu);
		return true;
	}

}
