package com.hallym.network;

import java.util.ArrayList;

public class networkState {
	public static final String PREFS_NAME = "MyPrefs"; //MyPrefs.xml∑Œ ¿˙¿Â
	
	public static boolean state = false;
	public static boolean login_Check = false;
	
	public static String name = "";
	public static String pass = "";
	
	
	public static boolean op_video = true;
	public static boolean op_audio = true;
	public static boolean op_picture = true;
	public static boolean op_text = true;
	
	//jinhan 07_30
	public static boolean op_quiz = true;
	//
	
	public static ArrayList<String> aList = new ArrayList<String>();
	public static ArrayList<String> aListAdress = new ArrayList<String>();
	
	public static int SIZE = 0;
	
	public static int user_x = 150;
	public static int user_y = 550;
}
