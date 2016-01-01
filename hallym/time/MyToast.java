package com.hallym.time;

import java.util.Date;

import android.content.Context;
import android.widget.Toast;

public class MyToast {
	public static void showToast(Context con, String str) {
		Toast.makeText(con, str, Toast.LENGTH_SHORT).show();
	}
	public static void showToast_Time(Context con, String str) {
		long time = System.currentTimeMillis();
		String date = new Date(time).toLocaleString();
		
		MyToast.showToast(con, str + "\n" + date);
	}
}
