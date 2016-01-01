package com.hallym.login_v1001;

import java.util.ArrayList;

import com.hallym.login_v1001.R;
import com.hallym.network.Delay_Thread;
import com.hallym.network.MessageManager;
import com.hallym.network.MessageReciver;
import com.hallym.network.networkState;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MAIN extends Activity {
	Button reader, login, creat, send, help, option;
	 String name, pass, msg = "";
	 boolean loginCheck = false;	 
	 boolean Main_accept = true;
			 
	 TextView textView;
	 
	 ProgressDialog mProgress;
	 MessageReciver messageReceiver;
	 MessageManager messageManager;
	 Dialog dlg;
	 
	 
	 SharedPreferences sharedPreferences;
	 
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		//jinhan 07_30
		networkState.aList = new ArrayList<String>();
		networkState.aListAdress = new ArrayList<String>();
	
		sharedPreferences 
	    = getSharedPreferences(networkState.PREFS_NAME, MODE_PRIVATE);;//나혼자만
	        
	        //id, pass load
	    networkState.name = sharedPreferences.getString("name", "noname");
	    networkState.pass = sharedPreferences.getString("pass", "nopass");
	    networkState.login_Check = sharedPreferences.getBoolean("loginCheck", false);
	    
	    
	    
	        //option load
	    networkState.op_audio = sharedPreferences.getBoolean("audio", true);
	    networkState.op_picture = sharedPreferences.getBoolean("picture", true);
	    networkState.op_video = sharedPreferences.getBoolean("video", true);
	    networkState.op_text = sharedPreferences.getBoolean("text", true);
	    
	    networkState.SIZE = sharedPreferences.getInt("size", 0);
	    Log.i("M",  ""+networkState.SIZE);
	    
	    for(int i = 0; i < networkState.SIZE; i++)
	    {
	    	networkState.aList.add(sharedPreferences.getString("list"+i, "nocomponent"));
	    	networkState.aListAdress.add(sharedPreferences.getString("listAdress"+i, "nocomponent" ));
	    }
	    
	    for(int i=0; i<networkState.aList.size(); ++i) {
			Log.i("M", i + ". " + networkState.aList.get(i) );
		}
	    
	    
	    if(networkState.name.equals("noname"))
	    {
	    	Intent login = new Intent(MAIN.this, LoginMain.class);
			startActivity(login);
			MAIN.this.finish();
	    }
	    
	   
	    if(networkState.login_Check == false)
	    //if( false )
	        {
	        	
		        mProgress = new ProgressDialog(this);
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
	
						new AlertDialog.Builder(MAIN.this)
						.setTitle("접속 실패")
						.setMessage("로그인이 실패 했습니다. 로그인 창으로 이동하시겠습니까?")
						.setPositiveButton("이동", mClick)
						.setNegativeButton("취소", mClick)
						.show();
					}
				});
				mProgress.show();
				
				messageManager = new MessageManager();
				print(networkState.name);
				print(networkState.pass);
				messageManager.setMessage("Login_Check", networkState.name, networkState.pass, "UMUL3", "123456");
				
		        messageReceiver = new MessageReciver(handler, messageManager);
				messageReceiver.start();
	        }
	        else
	        {
	        	
	        	mProgress = new ProgressDialog(this);
				mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				mProgress.setTitle("Connecting");
				mProgress.setMessage("Wait...");	
				mProgress.setCancelable(false);	
				mProgress.show();
				
				
	        	print("시작");
				Delay_Thread delay = new Delay_Thread(handler);
				delay.start();  
				print("스레드 시작");
	        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	
	DialogInterface.OnClickListener mClick = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			
			if (whichButton == DialogInterface.BUTTON_POSITIVE) {
				Intent login = new Intent(MAIN.this, LOGIN.class);
				startActivity(login);
				MAIN.this.finish();
			}
			
			if(whichButton == DialogInterface.BUTTON_NEGATIVE){					
				Intent loginmain = new Intent(MAIN.this, LoginMain.class);
				startActivity(loginmain);
				MAIN.this.finish();
			}

		}
	};
	

	protected void onStop() {
		//앱이 중지가 될때
        super.onStop();  
        	
    }	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		sharedPreferences = getSharedPreferences(networkState.PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit(); //편집
        editor.putString("name",networkState.name);
        editor.putString("pass",networkState.pass);
        editor.putBoolean("loginCheck", networkState.login_Check);
        editor.commit(); //저장
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public void print(Object message) {
		//textView.append(message + "\n");
		Toast t = Toast.makeText(getApplicationContext(), (String)message, Toast.LENGTH_LONG);
		t.show();
	}

	Handler handler = new Handler() {
		public void handleMessage(Message message) {
			super.handleMessage(message);
			
			/*
			if(message.arg1 == 1){
				mProgress.dismiss();
				print("프로그레스바 종료");
			}
						
			print((String)message.obj);
			print("MAIN");
			
			if(message.arg2 == 10)
			{
				messageReceiver.setFlag(true);
				print("접속을 종료 합니다.");
				messageReceiver.onDestroy();
			}
			*/
			
			
			if(message.what == 1)
			{
				if(message.arg1 == 101)
				{
					networkState.login_Check = !networkState.login_Check;
					mProgress.dismiss();
					print("프로그레스바 종료");
					
					messageReceiver.setFlag(true);
					print("접속을 종료 합니다.");
					messageReceiver.onDestroy();
					
					//Intent loginmain = new Intent(MAIN.this, LoginMain.class);
					//startActivity(loginmain);
					
					Intent reader = new Intent(MAIN.this, Reader.class);
					startActivity(reader);
					MAIN.this.finish();
				}
			}
			
			
			
			if(message.what == 2)
			{
				if(message.arg2 == 10)
				{
					mProgress.dismiss();
					//print("프로그레스바 종료");
					
					Intent reader = new Intent(MAIN.this, Reader.class);
					startActivity(reader);
					MAIN.this.finish();
				}
				
			}
			
		}
	};
}
