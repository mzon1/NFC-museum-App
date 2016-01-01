package com.hallym.streaming;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;

import com.example.android.trivialdrivesample.util.IabHelper;
import com.example.android.trivialdrivesample.util.IabResult;
import com.example.android.trivialdrivesample.util.Inventory;
import com.example.android.trivialdrivesample.util.Purchase;
import com.hallym.login_v1001.R;
import com.hallym.network.MessageManager;
import com.hallym.network.MessageReciver;
import com.hallym.network.networkState;
import com.hallym.testnfc.NFCReader;
import com.hallym.time.CurrentTime;
import com.hallym.time.MyToast;

public class EX3MainActivity extends Activity {

	private NfcAdapter nfcAdapter;
	private PendingIntent pendingIntent;

	private String uristr = "";
	
	SharedPreferences sharedPreferences;

	private String teststore[] = new String[3];

	boolean a = true;

	private Context context;

	private TabSpec teimSpec;
	private EX4Layout teimLayout;
	private TabSpec soSpec;
	private EX4Layout soLayout;
	private TabSpec viSpec;
	
	//jinhan 07_30
	private TabSpec QuSpec;
	private EX4Layout QuLayout;
	//

	private EX4Layout viLayout;

	private static final String TAB_TEXTIMAGE_TAG = "TeIm";
	private static final String TAB_TEXTIMAGE_NAME = "Text & Image";
	private static final String TAB_SOUND_TAG = "So";
	private static final String TAB_SOUND_NAME = "Sound";
	private static final String TAB_VIDEO_TAG = "Vi";
	private static final String TAB_VIDEO_NAME = "Video";
	
	//jinhan 07_30
	private static final String TAB_QUIZ_TAG = "Qu";
	private static final String TAB_QUIZ_NAME = "Quiz";
	//

	TabHost tabHost;

	MessageReciver messageReceiver;
	MessageManager messageManager;

	// 탭 처음 생성시 호출되는 콜백함수용 변수
	// 불필요한 리소스를 로드하지않아도 됨
	// TabContentFactory tcf = null;

	// 탭 교체시마다 호출되는 콜백함수용 변수
	// 이동한 탭의 이름이 호출
	TabHost.OnTabChangeListener tabChangeListener = null;
	
	
	// In App Billing
	IabHelper mHelper;
    static final String TEST_1 = "nfc_museum_test1";
    static final String TEST_2 = "nfc_museum_test2";
    static final String TEST_X = "nfc_museum_testX";
    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("onCreate", "onCreate");

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_ex3main);

		context = this;

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		// nfc 사용 준비
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		//Intent intent = new Intent();

		pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

		tabHost = (TabHost) findViewById(R.id.ex3_TabHost);
		tabHost.setup();
		
		Intent get = getIntent();
		uristr = get.getStringExtra("URL");

		setupTabs();

//		// In App Billing
//		mHelper = new IabHelper(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkb3+ptRotqhezbBsKJ9gEZpea9ntiK9ghmDCPdj/zYzkXJKUCGYWYhyWJugus+meSnTsKBIkhRvV8PrKt8INBcrjAH4VtLnO08hRdUKAOWsOgISftFc3couOpswKJx8acDeeERDx5Mcx+el5PikpFItvxCjkvIe9wHAeK5pyqyJWrZROfW+yH2Hv39KgQZaaj80fo+OMKxJh6ziiKersDzZxUGy95cBjn5XspqUda1wFia5AnnIZXDRQ+nMB2ZbagpYJ/SGx+dnkYljL9Q8RtytQNz+giazZngtFHk3bnIWOioyfhBqO5RmaaNJSmoQqgGGvcXLJvdo23WPUPOJ7LQIDAQAB");
//		mHelper.enableDebugLogging(true);
//		mHelper.startSetup(
//				new IabHelper.OnIabSetupFinishedListener() {
//					@Override
//					public void onIabSetupFinished(IabResult result) {
//						if( result.isSuccess() ) {
//							// Setup is Success...
//			            	Log.e("", "Setup is Success");
//						}
//						
////						ArrayList<String> additionalSkuList = new ArrayList<String>();
////						
////						additionalSkuList.add(TEST_1);
////						additionalSkuList.add(TEST_2);
////						mHelper.queryInventoryAsync(true, additionalSkuList,
////								mGotInventoryListener);
//						
//						mHelper.queryInventoryAsync( mGotInventoryListener );
//					}
//				});
	}
	
	// 구매 목록 확인 리스너
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
        	
        	if( result.isFailure() ) {
        		// handle error
            	Log.e("", "result Fail...");
        		return;
        	}

        	//*
        	String Price1 = inventory.getSkuDetails(TEST_1).getPrice();
        	Log.e("", "$" + Price1);
        	String Price2 = inventory.getSkuDetails(TEST_2).getPrice();
        	Log.e("", "$" + Price2);
        	/*/
            Purchase premiumPurchase = inventory.getPurchase(TEST_1);
            if( premiumPurchase == null ) {
            	// Fail...
            	Log.e("", "TEST 1 Fail...");
            }
            
            Purchase infiniteGasPurchase = inventory.getPurchase(TEST_1);
            if( infiniteGasPurchase == null ) {
            	// Fail...
            	Log.e("", "TEST 2 Fail...");
            }
            //*/
            
            // mHelper.consumeAsync(TEST_X, mConsumeFinishedListener);
        }
    };
    
    // 구매 실행 리스너
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
        	
        	if( result.isFailure() ) {
        		return;
        	}

            Log.d("In App Billing", "Purchase successful.");        	
        	
            


            if( purchase.getSku().equals(TEST_1) ) {
            	Log.i( "In App Billing", TEST_1 + "buy" );
//            	new DownloadImageTask().execute(urlstr);
            	
//                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
            }
            else if( purchase.getSku().equals(TEST_2) ) {
            	Log.i( "In App Billing", TEST_2 + "buy" );
//                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
            }
        }
    };
    
    // 구매 완료 리스너
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
        	
            if (result.isSuccess()) {
                Log.d( "In App Billing", "success buy!");

                Log.d( "In App Billing", "OrderID : "+purchase.getOrderId());
                Log.d( "In App Billing", "PackageName : " + purchase.getPackageName());
                Log.d( "In App Billing", "PurchaseTime :" + purchase.getPurchaseTime());
                Log.d( "In App Billing", "PurchaseState : " + purchase.getPurchaseState());
                Log.d( "In App Billing", "developerPayload : " + purchase.getDeveloperPayload());
                Log.d( "In App Billing", "PurchaseToken : " + purchase.getToken());
                Log.d( "In App Billing", "Siginature : " + purchase.getSignature());
                
            }
            else {
                Log.d( "In App Billing", "error");
            }
        }
    };
	
	
    protected void onNewIntent(Intent intent) {
    	Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		if (tag != null) {		
			CurrentTime ct = new CurrentTime();

			String msgs[] = NFCReader.getNFCDataStringList(intent);
			String datas[] = msgs[1].split(",");
			
			for(String s : msgs) {Log.i("NFC msg", s);}
			for(String s : datas) {Log.i("NFC msg", s);}
			
			uristr = datas[1];
			
			teststore[0] = datas[0];
			teststore[1] = ct.currentTime();
			teststore[2] = uristr;

			
			networkState.user_x = (int)Float.parseFloat(datas[2]);
			networkState.user_y = (int)Float.parseFloat(datas[3]);
			
			networkState.aList.add(datas[0] + ", " + ct.currentTime());
			networkState.aListAdress.add(datas[1]);
			
			networkState.SIZE = networkState.aList.size();
		
			if (networkState.login_Check) {
				messageManager = new MessageManager();
				messageManager.setMessage("NFC_Check", networkState.name,
						networkState.pass, teststore[0], "contact_tag");

				messageReceiver = new MessageReciver(handler, messageManager);
				messageReceiver.start();
			}
				
			tabHost.clearAllTabs();
			setupTabs();

		}
    }
    

	@Override
	protected void onResume() {
		// Log.i("onResume", "onResume");
		super.onResume();

		if (nfcAdapter != null) {
			// Activity가 화면에 보이고있을때에만 nfc태그 인식토록
			nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
		}

		
		/*
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
			Intent intent = getIntent();
			CurrentTime ct = new CurrentTime();

			String msgs[] = NFCReader.getNFCDataStringList(intent);

			//*
			// mod 2013/06/20
			// Log.i("NFC ID", msgs[0]);
			// Log.i("NFC msgs", msgs[1]);
			String datas[] = msgs[1].split(",");
			
			for(String s : msgs) {Log.i("NFC msg", s);}
			for(String s : datas) {Log.i("NFC msg", s);}
			
			uristr = datas[1];
			
			teststore[0] = datas[0];
			teststore[1] = ct.currentTime();
			teststore[2] = uristr;

			
			networkState.user_x = (int)Float.parseFloat(datas[2]);
			networkState.user_y = (int)Float.parseFloat(datas[3]);
			
			
			// for(int i=0; i<msgs.length; ++i) {
			// Log.i("", i + ". " + msgs[i] );
			// }
			
			
			
			uristr = msgs[1];

			teststore[0] = msgs[0];
			teststore[1] = ct.currentTime();
			teststore[2] = uristr;
			
			// networkState.aList.add(msgs[0] + ", " + ct.currentTime());
			// networkState.aListAdress.add(uristr);
			 
			

			
			  Log.e("TEST", msgs[0]); Log.e("TEST", msgs[1]); int count = 0;
			  for(int i = 0; i < msgs.length; i++) { count++; } Log.e("TEST",
			  "count : " + count); //Log.e("Y", msgs[3]);
			 
			  for(int i=0; i<msgs.length; ++i) { Log.i("", i + ". " + msgs[i]
			  ); }
			 

		} else {
			Intent get = getIntent();
			adressState = get.getStringExtra("Address");
			if (adressState.equals("Address")) {
				uristr = "http://203.253.248.122/exhibit/exhibit0001/";
			} else {
				uristr = adressState;
				a = !a;
			}
		}

		*/

		
	}

	private void setupTabs() {
		viSpec = tabHost.newTabSpec(TAB_VIDEO_TAG);
		viSpec.setContent(new TabContentFactory() {
			public View createTabContent(String tag) {
				viLayout = new EX4VideoLayout(EX3MainActivity.this, uristr);
				viLayout.setUri(uristr);
				// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);

				return viLayout;
			}
		});
		viSpec.setIndicator(TAB_VIDEO_NAME,
				getResources().getDrawable(R.drawable.icon_video));

		soSpec = tabHost.newTabSpec(TAB_SOUND_TAG);
		soSpec.setContent(new TabContentFactory() {
			public View createTabContent(String tag) {
				soLayout = new EX4SoundLayout(EX3MainActivity.this, uristr);
				soLayout.setUri(uristr);
				// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

				return soLayout;
			}
		});
		soSpec.setIndicator(TAB_SOUND_NAME,
				getResources().getDrawable(R.drawable.icon_music));

		teimSpec = tabHost.newTabSpec(TAB_TEXTIMAGE_TAG);
		teimSpec.setContent(new TabContentFactory() {
			public View createTabContent(String tag) {
				teimLayout = new EX4TextImageLayout(EX3MainActivity.this);
				teimLayout.setUri(uristr);
				// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

				registerForContextMenu(teimLayout);
				return teimLayout;
			}
		});
		teimSpec.setIndicator(TAB_TEXTIMAGE_NAME,
				getResources().getDrawable(R.drawable.icon_pic));
		
		//jinhan 07_30
		QuSpec = tabHost.newTabSpec(TAB_QUIZ_TAG);
		QuSpec.setContent(new TabContentFactory() {
			
			@Override
			public View createTabContent(String tag) {
				// TODO Auto-generated method stub
				teimLayout = new EX4TextImageLayout(EX3MainActivity.this);
				teimLayout.setUri(uristr);
				// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

				registerForContextMenu(teimLayout);
				return teimLayout;
			}
		});
		QuSpec.setIndicator(TAB_QUIZ_NAME);
		

		tabHost.addTab(viSpec);
		tabHost.addTab(soSpec);
		tabHost.addTab(teimSpec);
		//jinhan 07_30
		tabHost.addTab(QuSpec);
		//

		tabChangeListener = new OnTabChangeListener() {
			public void onTabChanged(String tabId) {

				if (tabId.equalsIgnoreCase(TAB_VIDEO_TAG)) {
					if (viLayout != null)
						viLayout.setEvent(EX4Layout.EVENT_CHANGEIN);
					if (soLayout != null)
						soLayout.setEvent(EX4Layout.EVENT_CHANGEOUT);
					if (teimLayout != null)
						teimLayout.setEvent(EX4Layout.EVENT_CHANGEOUT);
					
					//jinhan 07_30
					if (QuLayout != null)
						QuLayout.setEvent(EX4Layout.EVENT_CHANGEOUT);
					//
					
					// Log.i("TabChange", "Change : " + TAB_VIDEO_TAG);
				} else if (tabId.equalsIgnoreCase(TAB_SOUND_TAG)) {
					if (viLayout != null)
						viLayout.setEvent(EX4Layout.EVENT_CHANGEOUT);
					if (soLayout != null)
						soLayout.setEvent(EX4Layout.EVENT_CHANGEIN);
					if (teimLayout != null)
						teimLayout.setEvent(EX4Layout.EVENT_CHANGEOUT);
					
					//jinhan 07_30
					if (QuLayout != null)
						QuLayout.setEvent(EX4Layout.EVENT_CHANGEOUT);
					//
					
					// Log.i("TabChange", "Change : " + TAB_SOUND_TAG);
				} else if (tabId.equalsIgnoreCase(TAB_TEXTIMAGE_TAG)) {
					if (viLayout != null)
						viLayout.setEvent(EX4Layout.EVENT_CHANGEOUT);
					if (soLayout != null)
						soLayout.setEvent(EX4Layout.EVENT_CHANGEOUT);
					if (teimLayout != null)
						teimLayout.setEvent(EX4Layout.EVENT_CHANGEIN);
					
					//jinhan 07_30
					if (QuLayout != null)
						QuLayout.setEvent(EX4Layout.EVENT_CHANGEOUT);
					//
					
					// Log.i("TabChange", "Change : " + TAB_TEXTIMAGE_TAG);
				} 
				//jinhan 07_30
				else if (tabId.equalsIgnoreCase(TAB_QUIZ_TAG)) {
					if (viLayout != null)
						viLayout.setEvent(EX4Layout.EVENT_CHANGEOUT);
					if (soLayout != null)
						soLayout.setEvent(EX4Layout.EVENT_CHANGEOUT);
					if (teimLayout != null)
						teimLayout.setEvent(EX4Layout.EVENT_CHANGEOUT);
					
					//jinhan 07_30
					if (QuLayout != null)
						QuLayout.setEvent(EX4Layout.EVENT_CHANGEIN);
					//
					
					// Log.i("TabChange", "Change : " + TAB_QUIZ_TAG);
				}
			}
		};
		tabHost.setOnTabChangedListener(tabChangeListener);

		int code;
		code = modeState();

		tabHost.setCurrentTab(code);

		Log.e("ADD", "add ");
	}

	@Override
	protected void onPause() {
		if (nfcAdapter != null) {
			// Activity가 화면에 보이고있을때에만 nfc태그 인식토록
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
			Log.i("E", i + ". " + networkState.aList.get(i) );
		}
        
        editor.commit(); //저장
	}


	@Override
	protected void onDestroy() {
		if (viLayout != null)
			viLayout.onDestroy();
		if (soLayout != null)
			soLayout.onDestroy();
		if (teimLayout != null)
			teimLayout.onDestroy();
		
		//jinhan 07_30
		if(QuLayout != null)
			QuLayout.onDestroy();
		//

		// In App Billing
		if( mHelper!=null )	mHelper.dispose();
		mHelper = null;
		
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
//		if (a) {
//			networkState.aList.add(teststore[0] + ", " + teststore[1]);
//			networkState.aListAdress.add(teststore[2]);
//		}

		// Toast.makeText(this, "Back button pressed.",
		// Toast.LENGTH_SHORT).show();
		// Intent reader = new Intent(EX3MainActivity.this, Reader.class);
		// reader.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// reader.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		// startActivity(reader);
		super.onBackPressed();

	}

	int modeState() {
		int state = -1;
		if (networkState.op_audio) {
			state = 1;
		} else if (networkState.op_picture) {
			state = 2;
		} else if (networkState.op_video) {
			state = 0;
		}
		//jinhan 07_30
		else if (networkState.op_quiz ) {
			state = 3;
		}
		//
		return state;
	}

	// modify 2013/05/29
	public static final int ID_SAVEIMAGE = 0;
	public static final int ID_VIEWIMAGE = 1;

	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenu.ContextMenuInfo menuInfo) {

		WebView.HitTestResult hitTestResult = (((EX4TextImageLayout) teimLayout)
				.getWV()).getHitTestResult();
		final String urlstr = hitTestResult.getExtra();

		MenuItem.OnMenuItemClickListener Imagehandler = new MenuItem.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
				case ID_SAVEIMAGE:
//					Log.i("", "Image Save");
					Log.i("", "Buy Image");
					

			        mHelper.launchPurchaseFlow( (Activity)context, TEST_1, 10001, mPurchaseFinishedListener, "");
//					new DownloadImageTask().execute(urlstr);

					break;
				case ID_VIEWIMAGE:
					Log.i("", "Image View");
					break;
				}

				return true;
			}
		};

		switch (hitTestResult.getType()) {
		case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE:
		case WebView.HitTestResult.IMAGE_TYPE:
			Log.i("", "Image : " + urlstr);

			// Menu options for an image.
			// set the header title to the image url
			menu.setHeaderTitle(hitTestResult.getExtra());
			menu.add(0, ID_SAVEIMAGE, 0, "Save Image")
					.setOnMenuItemClickListener(Imagehandler);
			menu.add(0, ID_VIEWIMAGE, 0, "View Image")
					.setOnMenuItemClickListener(Imagehandler);

			break;
		case WebView.HitTestResult.UNKNOWN_TYPE:
			Log.i("", "Unknown");
			break;
		}
	}

	private class DownloadImageTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			Log.i("", "Image Save : " + params[0]);

			String result = "";
			String DownloadPath = Environment.getExternalStorageDirectory()
					+ "/Pictures/";

			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(params[0]);
			HttpResponse response;
			try {
				response = httpclient.execute(httpget);
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					URL url = new URL(params[0]);

					// Grabs the file part of the URL string
					String fileName = url.getFile();

					// Make sure we are grabbing just the filename
					int index = fileName.lastIndexOf("/");
					if (index >= 0)
						fileName = fileName.substring(index);

					// Create a temporary file
					File tempFile = new File(DownloadPath, fileName);
					if (!tempFile.exists())
						tempFile.createNewFile();

					InputStream inputStream = entity.getContent();
					BufferedInputStream bufferedInputStream = new BufferedInputStream(
							inputStream);
					// Read bytes into the buffer
					ByteArrayBuffer buffer = new ByteArrayBuffer(50);
					int current = 0;
					while ((current = bufferedInputStream.read()) != -1) {
						buffer.append((byte) current);
					}

					// Write the buffer to the file
					FileOutputStream stream = new FileOutputStream(tempFile);
					stream.write(buffer.toByteArray());
					stream.close();

					result = params[0] + "\nImage Download!\n" + DownloadPath
							+ fileName;
				}
			} catch (ClientProtocolException e) {
				result = "fail...";
				e.printStackTrace();
			} catch (IOException e) {
				result = "fail...";
				e.printStackTrace();
			}

			return result;
		}

		@Override
		protected void onPostExecute(String result) {

			MyToast.showToast_Time(context, result);
			// super.onPostExecute(result);
		}
	};

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

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if( !mHelper.handleActivityResult(requestCode, resultCode, data) ) {
			super.onActivityResult(requestCode, resultCode, data);
		}
		else {
			Log.i( "", "onActivityResult handled by IABUtil.");
		}
		
	}
}
