package com.hallym.streaming;

import android.content.Context;
import android.widget.FrameLayout;

public class EX4Layout extends FrameLayout {

	public static final int EVENT_CHANGEOUT = 0;
	public static final int EVENT_CHANGEIN = 1;
	

	public static final int EVENT_RETURN_ERROR = -1;
	public static final int EVENT_RETURN_END = 0;
	public static final int EVENT_RETURN_NULL = 1;
	
	public EX4Layout(Context context) {
		super(context);
	}

	public void onDestroy() {}
	public void setUri(String uri) {}
	public int setEvent(int eventNum) {return EVENT_RETURN_ERROR;}
}
