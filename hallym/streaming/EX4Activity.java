package com.hallym.streaming;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.Window;

import com.hallym.testnfc.NFCReader;
import com.hallym.time.MyToast;

public class EX4Activity extends Activity {
	
	private NfcAdapter nfcAdapter;
	private PendingIntent pendingIntent;
	
	private EX4Layout layout;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		
		// nfc 사용 준비
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
		
		
		// 레이아웃 초기화
		int code;
    	SharedPreferences pref = getSharedPreferences("config", MODE_PRIVATE);
    	code = pref.getInt("MODE", -1);
		
		switch(code) {
		case -1:
			MyToast.showToast_Time(this, "code error");
			finish();
			break;
		case 0:
			layout = new EX4VideoLayout( this );
			setContentView( layout );
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
			break;
		case 1:
//			MyToast.showToast_Time(this, "404 Sound not found");
			layout = new EX4SoundLayout( this );
			setContentView( layout );
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//			finish();
			break;
		case 2:
			layout = new EX4TextImageLayout( this );
			setContentView( layout );
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			break;
		}
	}


	@Override
	protected void onResume() {
		super.onResume();

		if (nfcAdapter != null) {
			// Activity가 화면에 보이고있을때에만 nfc태그 인식토록
			nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
		}
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            Intent intent = getIntent();
            
    		String msgs[] = NFCReader.getNFCDataStringList(intent);
    		
    		layout.setUri( msgs[1] );
    		
//    		for(int i=0; i<msgs.length; ++i) {
//    			Log.i("", i + ". " + msgs[i] );
//    		}
        }
		else {
			layout.setUri( "http://210.115.229.245:8080/exhibit/exhibit0001/" );
		}
	}

	@Override
	protected void onPause() {
		if (nfcAdapter != null) {
			// Activity가 화면에 보이고있을때에만 nfc태그 인식토록
			nfcAdapter.disableForegroundDispatch(this);
		}

		super.onPause();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
//		String msgs[] = NFCReader.getNFCDataStringList(intent);
//		
//		for(int i=0; i<msgs.length; ++i) {
//			Log.i("", i + ". " + msgs[i] );
//		}
	}
}
