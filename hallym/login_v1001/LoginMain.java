package com.hallym.login_v1001;

import com.hallym.login_v1001.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.Button;

public class LoginMain extends Activity {
	Button login, creat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_main);
		
		login = (Button)findViewById(R.id.loginmain_login);
		login.setOnClickListener(new Button.OnClickListener() {		
			public void onClick(android.view.View v) {
				// TODO Auto-generated method stub
				Intent login = new Intent(LoginMain.this, LOGIN.class);
				startActivity(login);
				LoginMain.this.finish();
			}
		});
		
		creat = (Button)findViewById(R.id.loginmain_creat);
		creat.setOnClickListener(new Button.OnClickListener() {		
			public void onClick(android.view.View v) {
				// TODO Auto-generated method stub
				Intent creat = new Intent(LoginMain.this, CREAT.class);
				startActivity(creat);
				LoginMain.this.finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_main, menu);
		return true;
	}

}
