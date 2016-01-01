package com.hallym.network;

import android.os.Handler;
import android.os.Message;

public class Delay_Thread extends Thread {
	int millis;
	Handler mHandler;
	
	public Delay_Thread(Handler mHandler) {
		// TODO Auto-generated constructor stub
		millis = 1000;
		this.mHandler = mHandler;
	}
	
	public Delay_Thread(int millis) {
		// TODO Auto-generated constructor stub
		this.millis = millis;
	}
	
	public void run() {
		try{
			Thread.sleep(millis);
			
			Message message = mHandler.obtainMessage(2, 0, 10, "test");

			mHandler.sendMessage(message);
		}catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
