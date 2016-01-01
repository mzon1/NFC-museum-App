package com.hallym.login_v1001;

import com.hallym.login_v1001.R;
import com.hallym.network.MessageManager;
import com.hallym.network.MessageReciver;
import com.hallym.network.networkState;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LOGIN extends Activity {
	String return_msg;
	MessageReciver messageReceiver;
	
	EditText id_et;
	EditText pw_et;
	Button login;
	String test = "login";
	
	ProgressDialog mProgress;
	
	//why?
	
	SharedPreferences sharedPreferences;

	MessageManager messageManager;
	
	TextView textView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		id_et = (EditText)findViewById(R.id.user_ID);
		pw_et = (EditText)findViewById(R.id.user_PASSWORD);
	    login = (Button)findViewById(R.id.login);
	    textView = (TextView)findViewById(R.id.login_tv_id);
	    
 
	    login.setOnClickListener(new Button.OnClickListener() {		
			@SuppressWarnings("deprecation")
			public void onClick(android.view.View v) {
				// TODO Auto-generated method stub
				mProgress = new ProgressDialog(v.getContext());
				mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				mProgress.setTitle("Connecting");
				mProgress.setMessage("Wait...");
				mProgress.setCancelable(false);		
				mProgress.setButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog, final int whichButton) {
						mProgress.dismiss();
						messageReceiver.setFlag(true);
						print("접속을 종료 합니다.");
						//messageReceiver.onDestroy();
					}
				});
				mProgress.show();
				
				test = id_et.getText().toString() + ", " + pw_et.getText().toString();
				
				messageManager = new MessageManager();
				messageManager.setMessage("Login_Check", id_et.getText().toString(), pw_et.getText().toString(), "UMUL3", "123456");
				
				messageReceiver = new MessageReciver(handler, messageManager);
				messageReceiver.start();
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}
	
	public void print(Object message) {
		textView.append(message + "\n");
	}
	
	protected void onStop() {
		//앱이 중지가 될때
        super.onStop();  
       
    }	
	
	public void onBackPressed() {
		Intent loginmain = new Intent(LOGIN.this, LoginMain.class);
		startActivity(loginmain);
		LOGIN.this.finish();
	};
	
	Handler handler = new Handler() {
		public void handleMessage(Message message) {
			super.handleMessage(message);
			
			if(message.what == 1)
			{
				if(message.arg1 == 101)
				{
					mProgress.dismiss();
					print("프로그레스바 종료");
					
					messageReceiver.setFlag(true);
					print("접속을 종료 합니다.");
					messageReceiver.onDestroy();
					
					
					sharedPreferences = getSharedPreferences(networkState.PREFS_NAME, 0);
			        SharedPreferences.Editor editor = sharedPreferences.edit(); //편집
			        editor.putString("name",id_et.getText().toString());
			        editor.putString("pass",pw_et.getText().toString());
			        editor.commit(); 
					
					//Intent loginmain = new Intent(MAIN.this, LoginMain.class);
					//startActivity(loginmain);
					
					Intent reader = new Intent(LOGIN.this, Reader.class);
					startActivity(reader);
					LOGIN.this.finish();
				}
			}
			
			/*
			if(message.arg1 == 1){
				mProgress.dismiss();
				print("비번 틀림");
			}
			if(message.arg1 == 1){
				mProgress.dismiss();
				print("아이디 틀림");
			}			
			if(message.arg2 == 10)
			{
				messageReceiver.setFlag(true);
				print("접속을 종료 합니다.");
				messageReceiver.onDestroy();
				
				sharedPreferences = getSharedPreferences(networkState.PREFS_NAME, 0);
		        SharedPreferences.Editor editor = sharedPreferences.edit(); //편집
		        editor.putString("name",id_et.getText().toString());
		        editor.putString("pass",pw_et.getText().toString());
		        
		        editor.commit();
				
				Intent reader = new Intent(LOGIN.this, Reader.class);
				startActivity(reader);
				LOGIN.this.finish();
			}
			*/
		}
	};

}
