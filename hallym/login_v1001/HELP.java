package com.hallym.login_v1001;

import com.hallym.login_v1001.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class HELP extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hel, menu);
		return true;
	}

}
