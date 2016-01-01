package com.hallym.streaming;

import android.content.Context;
import android.webkit.WebView;

public class EX4TextImageLayout extends EX4Layout {
	
	private WebView webView;
	
	public WebView getWV() {return webView;}

	public EX4TextImageLayout(Context context) {
		super(context);

		setFocusable(false);
		
		webView = new WebView(context);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setSupportZoom(true);
		
		this.addView(webView);
	}

	@Override
	public void setUri(String uri) {
		webView.loadUrl(uri + "textimage.html");
	}

	public int setEvent(int eventNum) {
		switch(eventNum) {
		case EVENT_CHANGEIN:
			return EVENT_RETURN_END;
		case EVENT_CHANGEOUT:
			return EVENT_RETURN_END;
		default:
			return EVENT_RETURN_ERROR;
		}
	}
}
