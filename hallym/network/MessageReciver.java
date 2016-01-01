package com.hallym.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MessageReciver extends Thread {	
	public static String SERVER_IP = "210.115.229.120";
	//public static String SERVER_IP = "203.253.248.120";
	public static int SERVER_PORT = 2223;
	
	//public static String SERVER_IP = "203.253.248.120";
	//public static int SERVER_PORT = 15464;
	
	private boolean flag = false;	
	
	MessageManager message_M;
	
	public Socket socket;
		public DataInputStream input;
		public DataOutputStream output;
		
		OutputStream out;
		InputStream in;
		
		Handler mHandler;
		
		String name = "noNFC";
		
		public MessageReciver(Handler handler, MessageManager message)
		{
			mHandler = handler;
			message_M = new MessageManager();
			message_M = message;		
		}
		
		public MessageReciver(Handler handler, String name)
		{
			mHandler = handler;
			this.name = name;
		}
		
		public MessageReciver(Handler handler)
		{
			mHandler = handler;
		}
		
	public void setFlag(boolean flag){    this.flag = flag;    }
	
	public void onDestroy()
	{
		try {
			socket.close();
			
			//input.close();
			//output.close();
			
			
			in.close();
			out.close();
			
		} catch (IOException e) {
		}
	}
	
	public void run() {
		try {
			String received = "test";
			String msg = SERVER_IP + " : " + SERVER_PORT + "로 접속 중...";
					
			Message message = mHandler.obtainMessage(1, 0, 0, msg);
			mHandler.sendMessage(message);		
			
			socket = new Socket(SERVER_IP, SERVER_PORT);

			//input = new DataInputStream(socket.getInputStream());
			//output = new DataOutputStream(socket.getOutputStream());		
			
			in = socket.getInputStream();
			out = socket.getOutputStream();		

			try{
				Thread.sleep(1500);
			}catch(InterruptedException e)
			{
				e.printStackTrace();
			}
					
				while (socket != null && !flag ) {
					if (socket.isConnected()) {
						
						/*
						output.writeUTF("r`1`1`" + name  + "`");
						output.flush();
						*/				
						out.write(message_M.Mesbyte);
						out.flush();						
						
						break;
					}
				}	
				

			byte buf[] = new byte[1024];	
			while (in.read(buf) != -1 && !flag) {	
				
				received = new String(buf,"EUC-KR");
				
				received = received.trim();
				
				//message = mHandler.obtainMessage(1, 1, 10, received);				
				//mHandler.sendMessage(message);
						
				
				if(received.equals("Login_Ok"))
				{
					message = mHandler.obtainMessage(1, 101, 10, received);
					mHandler.sendMessage(message);
				}
				if(received.equals("Login_No"))
				{
					message = mHandler.obtainMessage(1, 1, 10, received);
					mHandler.sendMessage(message);
				}
				if(received.equals("Account_Ok"))
				{
					message = mHandler.obtainMessage(1, 1, 10, received);
					mHandler.sendMessage(message);
				}
				if(received.equals("ID_No"))
				{
					message = mHandler.obtainMessage(1, 1, 100, received);
					mHandler.sendMessage(message);
				}
				if(received.equals("NFC_Ok"))
				{
					message = mHandler.obtainMessage(3, 3, 40, received);
					mHandler.sendMessage(message);
				}
				
				
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isFlag() {
		return flag;
	}
}



