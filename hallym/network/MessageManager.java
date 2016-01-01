package com.hallym.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MessageManager
{
	byte [] Mesbyte = new byte[512];
	public String transData = new String();
	
	public MessageManager()
	{
		
	}
	public void setMessage(String Sign,String email, String pass, String id, String delay) 
	{
		//transData = state + '\0' + email + '\0' + pass + '\0' + id + '\0' + delay + '\0';
		transData = Sign+'\0'+ email + '\0' + pass + '\0' + id + '\0' + delay + '\0';
		Mesbyte = transData.getBytes();
		
		/*
		OutputStream out = sc.getOutputStream();
		
		out.write(Mesbyte);
		out.flush();
		*/
	}
	
	public String ReciveMessage() throws IOException
	{
		byte buf[] = new byte[1024];
		//추가해야합니다.
		
		/*
		InputStream in = sc.getInputStream();
		in.read(buf);
		*/
		
		String ret = new String(buf,"EUC-KR");
		
		return ret;
	}
}