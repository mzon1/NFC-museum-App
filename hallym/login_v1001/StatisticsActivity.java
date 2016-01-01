package com.hallym.login_v1001;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class StatisticsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statistics);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.statistics, menu);
		return true;
	}

}
