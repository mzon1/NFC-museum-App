package com.hallym.login_v1001;


import com.hallym.streaming.*;
import com.hallym.login_v1001.R;
import com.hallym.network.MessageManager;
import com.hallym.network.MessageReciver;
import com.hallym.network.networkState;
import com.hallym.streaming.EX3MainActivity;
import com.hallym.testnfc.NFCReader;
import com.hallym.time.CurrentTime;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.Toast;

public class Reader extends Activity {
	Button logout, map, option, list;
	
	 final static int ACT_OPTION = 0;
	 
	 SharedPreferences sharedPreferences;
	 
	 private NfcAdapter nfcAdapter;
	private PendingIntent pendingIntent;
	
	MessageReciver messageReceiver;
	MessageManager messageManager;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reader);
		
		option = (Button)findViewById(R.id.reader_option);
		
		option.setOnClickListener(new Button.OnClickListener() {		
			public void onClick(android.view.View v) {
				// TODO Auto-generated method stub
				Intent option = new Intent(Reader.this, Option.class);
				
				option.putExtra("video", networkState.op_video);
				option.putExtra("audio", networkState.op_audio);
				option.putExtra("picture", networkState.op_picture);
				option.putExtra("text", networkState.op_text);
				startActivityForResult(option, ACT_OPTION);	
			}
		});
		
		logout = (Button)findViewById(R.id.reader_logout);
		
		logout.setOnClickListener(new Button.OnClickListener() {		
			public void onClick(android.view.View v) {
				// TODO Auto-generated method stub
				Intent loginmain = new Intent(Reader.this,LoginMain.class);
				networkState.login_Check = false;
				startActivity(loginmain);
				Reader.this.finish();
			}
		});
		
		map = (Button)findViewById(R.id.reader_map);
		
		map.setOnClickListener(new Button.OnClickListener(){
			
			public void onClick(android.view.View v)
			{
				Intent view = new Intent(Reader.this,View.class);
				view.putExtra("picture", 1);
				startActivity(view);
			}
		});
		
		list = (Button)findViewById(R.id.reader_list);
		
		list.setOnClickListener(new Button.OnClickListener() {		
			public void onClick(android.view.View v) {
				// TODO Auto-generated method stub
				
				Intent listview = new Intent(Reader.this, List_view_test.class);
				startActivity(listview);
			}
		});
		
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reader, menu);
		return true;
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK) // 액티비티가 정상적으로 종료되었을 경우
		{
			if(requestCode==ACT_OPTION) 
			{               
				networkState.op_video = data.getBooleanExtra("vedio", true);	
				networkState.op_audio = data.getBooleanExtra("audio", true);	
				networkState.op_picture = data.getBooleanExtra("picture", true);	
				networkState.op_text = data.getBooleanExtra("text", true);	
			}
		}
	}
	
	protected void onStop() {
		//앱이 중지가 될때
        super.onStop();  
        	
    }
	
	@Override
	protected void onPause() {
		if (nfcAdapter != null) {
			nfcAdapter.disableForegroundDispatch(this);
		}
		
		super.onPause();
		sharedPreferences = getSharedPreferences(networkState.PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit(); //편집
        
        editor.putBoolean("video", networkState.op_video);
        editor.putBoolean("audio", networkState.op_audio);
        editor.putBoolean("picture", networkState.op_picture);
        editor.putBoolean("text", networkState.op_text);
        
        editor.putBoolean("loginCheck", networkState.login_Check);
        
        editor.putInt("size", networkState.SIZE);
	    
        
	    for(int i = 0; i < networkState.SIZE; i++)
	    {
	    	editor.putString("list"+i, networkState.aList.get(i));
	    	editor.putString("listAdress"+i, networkState.aListAdress.get(i));
	    }
	    Log.i("M",  ""+networkState.SIZE);
	    for(int i=0; i<networkState.aList.size(); ++i) {
			Log.i("R", i + ". " + networkState.aList.get(i) );
		}
        
        editor.commit(); //저장
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (nfcAdapter != null) {
			nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
		}
	}

	@SuppressLint("InlinedApi")
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		
			CurrentTime ct = new CurrentTime();

			String msgs[] = NFCReader.getNFCDataStringList(intent);

			//*
			// mod 2013/06/20
			// Log.i("NFC ID", msgs[0]);
			// Log.i("NFC msgs", msgs[1]);
			String datas[] = msgs[1].split(",");
			
			for(String s : msgs) {Log.i("NFC msg", s);}
			for(String s : datas) {Log.i("NFC msg", s);}
			
			networkState.aList.add(datas[0] + ", " + ct.currentTime());
			networkState.aListAdress.add(datas[1]);
			
			networkState.SIZE = networkState.aList.size();
			
			networkState.user_x = (int)Float.parseFloat(datas[2]);
			networkState.user_y = (int)Float.parseFloat(datas[3]);
			
			if (networkState.login_Check) {
				messageManager = new MessageManager();
				messageManager.setMessage("NFC_Check", networkState.name,
						networkState.pass, datas[0], "contact_tag");

				messageReceiver = new MessageReciver(handler, messageManager);
				messageReceiver.start();
			}
			// for(int i=0; i<msgs.length; ++i) {
			// Log.i("", i + ". " + msgs[i] );
			// }
			/*/
			
			
			uristr = msgs[1];

			teststore[0] = msgs[0];
			teststore[1] = ct.currentTime();
			teststore[2] = uristr;
			
			
			 * 
			 //*/

			/*
			 * Log.e("TEST", msgs[0]); Log.e("TEST", msgs[1]); int count = 0;
			 * for(int i = 0; i < msgs.length; i++) { count++; } Log.e("TEST",
			 * "count : " + count); //Log.e("Y", msgs[3]);
			 * 
			 * for(int i=0; i<msgs.length; ++i) { Log.i("", i + ". " + msgs[i]
			 * ); }
			 */
		
		
		Intent reader = new Intent(Reader.this, EX3MainActivity.class);
		reader.putExtra("URL", datas[1]);
		startActivity(reader);
		
	}
	
	
	
	Handler handler = new Handler() {
		public void handleMessage(Message message) {
			super.handleMessage(message);
			if (message.what == 3) {
				if (message.arg2 == 40) {
					messageReceiver.setFlag(true);
					messageReceiver.onDestroy();
				}
			}
		}
	};

}
