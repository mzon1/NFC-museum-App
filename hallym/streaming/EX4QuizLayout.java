package com.hallym.streaming;

import com.hallym.login_v1001.R;
import com.hallym.login_v1001.R.layout;
import com.hallym.login_v1001.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class EX4QuizLayout extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ex4_quiz_layout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_ex4_quiz_layout, menu);
		return true;
	}

}
