package com.hallym.testnfc;

import java.util.ArrayList;
import java.util.List;

import com.hallym.network.networkState;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Parcelable;
import android.util.Log;

public class NFCReader {
	
	public static String[] getNFCDataStringList(Intent intent) {
		ArrayList<String> arrMsgs = new ArrayList<String>();
		String msg[] = null;
		
		if(intent != null) {
			Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
	
			if (tag != null) {
				// NFC 태그 인식(msg 여부 관계없이)
				byte[] tagId = tag.getId();
				arrMsgs.add("" + tagId);
				
				///////////////////
				
				Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
	
				if (rawMsgs != null) {
					// NDEF 태그 인식
		
					NdefMessage[] msgs;
					if (rawMsgs != null) {
						msgs = new NdefMessage[rawMsgs.length];
						for (int i = 0; i < rawMsgs.length; i++) {
							msgs[i] = (NdefMessage) rawMsgs[i];
							arrMsgs.add( getTag(msgs[i]) );
						}
					}
				}
				
				msg = new String[ arrMsgs.size() ];
				for(int i=0; i<arrMsgs.size(); ++i)
					msg[i] = arrMsgs.get(i);
			}
		}
		
		return msg;
	}
	
	public static String getNFCDataString(Intent intent) {
		String msg = "";
		
		if(intent != null) {
			Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
	
			if (tag != null) {
				// NFC 태그 인식(msg 여부 관계없이)
				byte[] tagId = tag.getId();
				msg += tagId;
//				Log.i("", "" + tagId + "");
				
				///////////////////
				
				Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
	
				if (rawMsgs != null) {
					// NDEF 태그 인식
		
					NdefMessage[] msgs;
					if (rawMsgs != null) {
						msgs = new NdefMessage[rawMsgs.length];
						for (int i = 0; i < rawMsgs.length; i++) {
							msgs[i] = (NdefMessage) rawMsgs[i];
							
							Log.i("", "" + i + ". " + getTag(msgs[i]) );
						}
					}
				}
			}
		}
		
		return msg;
	}
	
	public static String getTag(NdefMessage mMessage) {
		List<ParsedRecord> records = NdefMessageParser.parse(mMessage);
		final int size = records.size();
		String msg = "";
		
		for (int i = 0; i < size; i++) {
			ParsedRecord record = records.get(i);

			int recordType = record.getType();
			String recordStr = "";
			if (recordType == ParsedRecord.TYPE_TEXT) {
				//recordStr = ((TextRecord) record).getText();
				
				switch(i)
				{
					case 0:
						recordStr = ((TextRecord) record).getText();
						break;
					case  1 :
						String x = "";
						x = ((TextRecord) record).getText();
						networkState.user_x = (int)Double.parseDouble(x);
						
						break;
					case  2 :
						String y = "";
						y = ((TextRecord) record).getText();
						networkState.user_y = (int)Double.parseDouble(y);
						break;
				}
				
				
			} else if (recordType == ParsedRecord.TYPE_URI) {
				recordStr = ((UriRecord) record).getUri().toString();
			}

			Log.d(""+i, "record string : " + recordStr);
			msg += recordStr;
			Log.e("msg", "msg :" + msg);
		}

		return msg;
	}

}
