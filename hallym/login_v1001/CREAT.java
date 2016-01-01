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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class CREAT extends Activity {
	
	MessageReciver messageReceiver;
	
	MessageManager messageManager;
	
	ProgressDialog mProgress;
	
	Button creat;
	
	EditText em_et;
	EditText pw_et;
	EditText rpw_et;
	
	Spinner age;
	Spinner sex;
	
	String sexstr, agestr;
	
	TextView textView;
	
	SharedPreferences sharedPreferences;
	
	boolean mInitSpinner;
	
	String test = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creat);	
		
		 creat = (Button)findViewById(R.id.creat_bt_cr);
		 
		 textView = (TextView)findViewById(R.id.creat_tv_em);
		 
		 em_et = (EditText)findViewById(R.id.creat_et_em);
		 pw_et = (EditText)findViewById(R.id.creat_et_pw);
		 rpw_et = (EditText)findViewById(R.id.creat_et_rpw);
		 
		 age = (Spinner)findViewById(R.id.spinner1);
		 age.setPrompt("나이를 선택하세요");
		 sex = (Spinner)findViewById(R.id.spinner2);
		 age.setPrompt("성별을 선택하세요");
		 
		 final ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.age_array, android.R.layout.simple_spinner_item);
		 adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 age.setAdapter(adapter1);
		 
		 age.setOnItemSelectedListener(new OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, View view, 
						int position, long id) {
					
					if (mInitSpinner == false) {
						mInitSpinner = true;
						return;
					}
					
					agestr = (String)adapter1.getItem(position);
					Toast.makeText(CREAT.this, agestr,
							Toast.LENGTH_SHORT).show();
				}
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});
		 
		 final ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.sex_array, android.R.layout.simple_spinner_item);
		 adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 sex.setAdapter(adapter2);
		 
		 sex.setOnItemSelectedListener(new OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, View view, 
						int position, long id) {
					/* 초기화시의 선택 제외시
					if (mInitSpinner == false) {
						mInitSpinner = true;
						return;
					}
					//*/
					sexstr = (String)adapter2.getItem(position);
					Toast.makeText(CREAT.this, sexstr,
							Toast.LENGTH_SHORT).show();
				}
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});
		 
		 creat.setOnClickListener(new Button.OnClickListener() {		
				@SuppressWarnings("deprecation")
				public void onClick(android.view.View v) {
					// TODO Auto-generated method stub
					
					test = "creat," + em_et.getText().toString() + " " + pw_et.getText().toString() + " "
							+ rpw_et.getText().toString();
					
					if(!pw_et.getText().toString().equals(rpw_et.getText().toString()))
					{
						Toast t = Toast.makeText(getApplicationContext(), "password wong", Toast.LENGTH_LONG);
						t.show();
						return;
					}
						
					
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
					
					messageManager = new MessageManager();
					messageManager.setMessage("Account_Input", em_et.getText().toString(), pw_et.getText().toString(), sexstr, agestr);
					
					messageReceiver = new MessageReciver(handler, messageManager);
					messageReceiver.start();
				}
			});
		 
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		sharedPreferences = getSharedPreferences(networkState.PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit(); //편집
        editor.putString("name",em_et.getText().toString());
        editor.putString("pass",pw_et.getText().toString());
        editor.commit(); 
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_creat, menu);
		return true;
	}
	
	public void print(Object message) {
		textView.append(message + "\n");
		Toast t = Toast.makeText(getApplicationContext(), (String)message, Toast.LENGTH_LONG);
		t.show();
	}
	
	public void onBackPressed() {
		Intent loginmain = new Intent(CREAT.this, LoginMain.class);
		startActivity(loginmain);
		CREAT.this.finish();
	};
	
	Handler handler = new Handler() {
		public void handleMessage(Message message) {
			super.handleMessage(message);
			if(message.arg1 == 1){
				mProgress.dismiss();
				print("프로그레스바 종료");
			}
						
			print((String)message.obj);
			
			if(message.arg2 == 10)
			{
				messageReceiver.setFlag(true);
				print("접속을 종료 합니다.");
				messageReceiver.onDestroy();
				
				Intent reader = new Intent(CREAT.this, Reader.class);
				startActivity(reader);
				CREAT.this.finish();
			}
			
			if(message.arg2 == 100)
			{
				print("이미 존재하는 아이디 입니다.");
			}
		}
	};

}
