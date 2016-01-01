package com.hallym.testnfc;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.hallym.login_v1001.R;
import com.hallym.streaming.EX1MainActivity;

public class MainActivity extends Activity implements OnClickListener {

	// public static final String Path = "http://210.115.229.245:8080/Test/BADAKtooth3_ff.m3u8";
	// public static final String Path = "http://210.115.229.245:8080/Test/Mr.Okojyo01_list.m3u8";
	public static final String Path = "http://210.115.229.245:8080/Test/Streaming/EOE_2/EOE_2_f.m3u8";
	private Intent intent;
	private NdefMessage msgs[];
	private Button btEx1, btEx2;

	@Override
	public void onNewIntent(Intent intent) {
		// onResume은 이 메서드 이후에 호출되므로 이 인텐트를 얻어 처리한다.
		setIntent(intent);
	}
	public void onResume() {
		super.onResume();
		
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
			Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			if (rawMsgs != null) {
				msgs = new NdefMessage[rawMsgs.length];
				for (int i = 0; i < rawMsgs.length; i++) {
					msgs[i] = (NdefMessage) rawMsgs[i];
				}
			}
		}// process the msgs array
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		btEx1 = (Button) findViewById(R.id.Main_btEx1);
		btEx1.setOnClickListener(this);
		btEx2 = (Button) findViewById(R.id.Main_btEx2);
		btEx2.setOnClickListener(this);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void onClick(View v) {
		if (v == btEx1) {
			Intent intent = new Intent(this, EX1MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
		}
	}
}