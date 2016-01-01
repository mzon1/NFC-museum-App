package com.hallym.fading;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

import com.hallym.login_v1001.MAIN;
import com.hallym.login_v1001.R;

public class Loading2 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading2);
		Handler handler = new Handler();
		handler.postDelayed(new nextscene(), 1500);//
		
	}
	class nextscene implements Runnable{
		public void run(){
			startActivity(new Intent(getApplication(),MAIN.class));
			overridePendingTransition(R.anim.fade, R.anim.fade2);
			Loading2.this.finish();
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.loading2, menu);
		return true;
	}

}
