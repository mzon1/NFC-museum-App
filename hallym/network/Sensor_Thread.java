package com.hallym.network;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import android.app.Activity;



public class Sensor_Thread extends Thread implements SensorEventListener{
	private SensorManager smanager; 
	private Sensor sacc, sgyro, sorien, gra;
	
	private int sensing_time= SensorManager.SENSOR_DELAY_NORMAL;					//50000이라는 수치를 변경했음. 2013_12_11
	private float[][] AccValue=new float[3][11];
	private float[][] GyroValue=new float[3][11];
	private float[][] OriValue=new float[3][11];	
	private float[][] GraValue= new float[3][11];
	private float[] Ysum1=new float[11];
	private float[] Ysum2=new float[11];
	private boolean mStop = true;
	
	Context ct;

    
    private float wave=0;
    private float[] wdev=new float[11];
    private float wvar=0;
	private float wgave=0;
	private float[] wgdev=new float[11];
	private float wgvar=0;
	private float wgavex=0;
	private float wgavez=0;
	private float wgvarx=0;
	private float wgvarz=0;
	private float[] wgdevx=new float[11];
	private float[] wgdevz=new float[11];
	private float ppp=0;
	private float mpp=0;
	
    private int step=0;
    private double status=0;
    private float phead=0;
    private float thead=320;
	private int heading=0;
	private int[] chead=new int[8];

	private int[] th=new int[11];
    

	int cnt=0;
	int len = 0;


	private double lcount[] = new double[5];


	private String status_all, landmark;

	
	int millis;
	Handler mHandler;
	
	public Sensor_Thread(Handler mHandler, Context ct) {
		// TODO Auto-generated constructor stub
		millis = 1000;
		this.mHandler = mHandler;
		
		this.ct = ct;
		
		smanager.registerListener(this, sacc, sensing_time);
		smanager.registerListener(this, sgyro, sensing_time);
		smanager.registerListener(this, sorien, sensing_time);
		smanager.registerListener(this, gra, sensing_time);
		
		sacc = smanager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		sgyro = smanager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		sorien = smanager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		gra=smanager.getDefaultSensor(Sensor.TYPE_GRAVITY);
	}
	
	public Sensor_Thread(int millis) {
		// TODO Auto-generated constructor stub
		this.millis = millis;
	}
	
	public void run() {
		for(int i=0;i<11;i++){             // 변수 초기화
		    	AccValue[0][i]=0;
		    	AccValue[1][i]=0;
		    	AccValue[2][i]=0;
		    	GyroValue[0][i]=0;
		    	GyroValue[1][i]=0;
		    	GyroValue[2][i]=0;
		    	OriValue[0][i]=0;
		    	OriValue[1][i]=0;
		    	OriValue[2][i]=0;
		    	wdev[i]=0;
		    	wgdev[i]=0;
		    	wgdevx[i]=0;
		    	wgdevz[i]=0;
		    	GraValue[0][i]=0;
		    	GraValue[1][i]=0;
		    	GraValue[2][i]=0;
		    	Ysum1[i]=0;
		    	Ysum2[i]=0;
		    }
		    for(int i=0; i<5; i++){
		    	lcount[i]=0;
		    }
		    for(int i=0; i<8; i++){
		    	chead[i]=0;
		    }
		    
		    smanager = (SensorManager)ct.getSystemService(Context.SENSOR_SERVICE); // sensor manger 선언
		    
		    /*
		    if(WManager.isWifiEnabled()==false){ // wifi가 꺼져있으면 켜주는 부분
				Log.d(Tag, "WIFI On !!!");
		    	WManager.setWifiEnabled(true);
		    	Toast.makeText(MainActivity.this, "WIFI ON !!!", Toast.LENGTH_LONG).show();
			}
			*/

		    
		    status_all="";
		    landmark="";
	}
	
	
	public void Find_PlanA(){
		if(AccValue[2][5]==exmax(AccValue[2])){
			if(AccValue[2][5]>0.5){
				if(Math.abs(GraValue[0][10])>=4){
					step+=1;
					status=1;
					//initWIFIScan();
					phead=thead;
					thead=OriValue[0][10];
					if(phead-thead>0){
	                    if((thead>=232.5+5)&&(thead<=277.5))
	                        heading=0;
	                    else if((thead>=277.5+5)&&(thead<=322.5))
	                        heading=1;
	                    else if((thead>=322.5+5)&&(thead<360))
	                        heading=2;
	                    else if((thead>=0)&&(thead<=7.5))
	                        heading=2;
	                    else if((thead>=7.5+5)&&(thead<=52.5))
	                        heading=3;
	                    else if((thead>=52.5+5)&&(thead<=97.5))
	                        heading=4;
	                    else if((thead>=97.5+5)&&(thead<=142.5))
	                        heading=5;
	                    else if((thead>=142.5+5)&&(thead<=187.5))
	                        heading=6;
	                    else if((thead>=187.5+5)&&(thead<=232.5))
	                    	heading=7;
					}else if(phead-thead<0){
	                    if((thead<=277.5-5)&&(thead>=232.5))
	                    	heading=0;
	                    else if((thead<=232.5-5)&&(thead>=187.5))
	                    	heading=7;
	                    else if((thead<=187.5-5)&&(thead>=142.5))
	                    	heading=6;
	                    else if((thead<=142.5-5)&&(thead>=97.5))
	                    	heading=5;
	                    else if((thead<=97.5-5)&&(thead>=52.5))
	                    	heading=4;
	                    else if((thead<=52.5-5)&&(thead>=7.5))
	                    	heading=3;
	                    else if((thead<=7.5-5)&&(thead>0))
	                    	heading=2;
	                    else if((thead<=360)&&(thead>=322.5))
	                    	heading=2;
	                    else if((thead<=322.5-5)&&(thead>=277.5))
	                    	heading=1;
					}
					status_all+=""+step+","+status+","+heading+"\n";
					//tview.setText("step:"+step+"\nstatus:"+status+"\nhead:"+heading+"\nlandmark:"+maxland);
				}else if(Math.abs(GraValue[1][10])>=4){
					step+=1;
					status=1.5;
					//initWIFIScan();
					phead=thead;
					thead=OriValue[0][10];
					if(phead-thead>0){
	                    if((thead>=232.5+5)&&(thead<=277.5))
	                        heading=0;
	                    else if((thead>=277.5+5)&&(thead<=322.5))
	                        heading=1;
	                    else if((thead>=322.5+5)&&(thead<360))
	                        heading=2;
	                    else if((thead>=0)&&(thead<=7.5))
	                        heading=2;
	                    else if((thead>=7.5+5)&&(thead<=52.5))
	                        heading=3;
	                    else if((thead>=52.5+5)&&(thead<=97.5))
	                        heading=4;
	                    else if((thead>=97.5+5)&&(thead<=142.5))
	                        heading=5;
	                    else if((thead>=142.5+5)&&(thead<=187.5))
	                        heading=6;
	                    else if((thead>=187.5+5)&&(thead<=232.5))
	                    	heading=7;
					}else if(phead-thead<0){
	                    if((thead<=277.5-5)&&(thead>=232.5))
	                    	heading=0;
	                    else if((thead<=232.5-5)&&(thead>=187.5))
	                    	heading=7;
	                    else if((thead<=187.5-5)&&(thead>=142.5))
	                    	heading=6;
	                    else if((thead<=142.5-5)&&(thead>=97.5))
	                    	heading=5;
	                    else if((thead<=97.5-5)&&(thead>=52.5))
	                    	heading=4;
	                    else if((thead<=52.5-5)&&(thead>=7.5))
	                    	heading=3;
	                    else if((thead<=7.5-5)&&(thead>0))
	                    	heading=2;
	                    else if((thead<=360)&&(thead>=322.5))
	                    	heading=2;
	                    else if((thead<=322.5-5)&&(thead>=277.5))
	                    	heading=1;
					}
					status_all+=""+step+","+status+","+heading+"\n";
					//tview.setText("step:"+step+"\nstatus:"+status+"\nhead:"+heading+"\nlandmark:"+maxland);
				}
			}                 
		}
	}

	public void Find_PlanB(){
		if(AccValue[1][5]==exmax(AccValue[1])&&AccValue[1][5]>5){
			ppp=AccValue[1][5];
			if(mpp!=0){
				if(Math.abs(ppp)>Math.abs(mpp)){
					if(GraValue[0][5]>4){
						step+=1;
			        	//initWIFIScan();
			        	status=2.4;
			        	for(int i=0;i<11;i++){
		            		phead=thead;
			            	thead=OriValue[0][i];
			            	if(phead-thead>0){
			            		if((thead>=232.5+5)&&(thead<=277.5))
		                            th[i]=0;
		                        else if((thead>=277.5+5)&&(thead<=322.5))
		                        	th[i]=1;
		                        else if((thead>=322.5+5)&&(thead<360))
		                        	th[i]=2;
		                        else if((thead>=0)&&(thead<=7.5))
		                        	th[i]=2;
		                        else if((thead>=7.5+5)&&(thead<=52.5))
		                        	th[i]=3;
		                        else if((thead>=52.5+5)&&(thead<=97.5))
		                        	th[i]=4;
		                        else if((thead>=97.5+5)&&(thead<=142.5))
		                        	th[i]=5;
		                        else if((thead>=142.5+5)&&(thead<=187.5))
		                        	th[i]=6;
		                        else if((thead>=187.5+5)&&(thead<=232.5))
		                        	th[i]=7;
							}else if(phead-thead<0){
		                        if((thead<=277.5-5)&&(thead>=232.5))
		                        	th[i]=0;
		                        else if((thead<=232.5-5)&&(thead>=187.5))
		                        	th[i]=7;
		                        else if((thead<=187.5-5)&&(thead>=142.5))
		                        	th[i]=6;
		                        else if((thead<=142.5-5)&&(thead>=97.5))
		                        	th[i]=5;
		                        else if((thead<=97.5-5)&&(thead>=52.5))
		                        	th[i]=4;
		                        else if((thead<=52.5-5)&&(thead>=7.5))
		                        	th[i]=3;
		                        else if((thead<=7.5-5)&&(thead>0))
		                        	th[i]=2;
		                        else if((thead<=360)&&(thead>=322.5))
		                        	th[i]=2;
		                        else if((thead<=322.5-5)&&(thead>=277.5))
		                        	th[i]=1;
			                }
			            }
			        	for(int i=0;i<11;i++){
			        		if(th[i]==0) chead[0]+=1;
			        		else if(th[i]==1) chead[1]+=1;
			        		else if(th[i]==2) chead[2]+=1;
			        		else if(th[i]==3) chead[3]+=1;
			        		else if(th[i]==4) chead[4]+=1;
			        		else if(th[i]==5) chead[5]+=1;
			        		else if(th[i]==6) chead[6]+=1;
			        		else if(th[i]==7) chead[7]+=1;
			        	}
			        	if(iexmax(chead)==chead[0]) heading=0;
			        	else if(iexmax(chead)==chead[1]) heading=1;
			        	else if(iexmax(chead)==chead[2]) heading=2;
			        	else if(iexmax(chead)==chead[3]) heading=3;
			        	else if(iexmax(chead)==chead[4]) heading=4;
			        	else if(iexmax(chead)==chead[5]) heading=5;
			        	else if(iexmax(chead)==chead[6]) heading=6;
			        	else if(iexmax(chead)==chead[7]) heading=7;
			        	
			        	for(int i=0; i<8; i++){
			            	chead[i]=0;
			            }
			        	status_all+=""+step+","+status+","+heading+"\n";
						//tview.setText("step:"+step+"\nstatus:"+status+"\nhead:"+heading+"\nlandmark:"+maxland);
					}else if(GraValue[0][5]<-4){
						step+=1;
			        	//initWIFIScan();
			        	status=2.6;
			        	for(int i=0;i<11;i++){
		            		phead=thead;
			            	thead=OriValue[0][i];
			            	if(phead-thead>0){
			            		if((thead>=37.5+5)&&(thead<=82.5))
		                            th[i]=0;
		                        else if((thead>=82.5+5)&&(thead<=127.5))
		                        	th[i]=1;
		                        else if((thead>=127.5+5)&&(thead<=172.5))
		                        	th[i]=2;
		                        else if((thead>=172.5+5)&&(thead<=217.5))
		                        	th[i]=3;
		                        else if((thead>=217.5+5)&&(thead<=262.5))
		                        	th[i]=4;
		                        else if((thead>=262.5+5)&&(thead<=307.5))
		                        	th[i]=5;
		                        else if((thead>=307.5+5)&&(thead<=352.5))
		                        	th[i]=6;
		                        else if((thead>=352.5+5)&&(thead<=360))
		                        	th[i]=7;
		                        else if((thead>=0)&&(thead<=37.5))
		                        	th[i]=7;
							}else if(phead-thead<0){
		                        if((thead<=82.5-5)&&(thead>=37.5))
		                        	th[i]=0;
		                        else if((thead<=37.5-5)&&(thead>=0))
		                        	th[i]=7;
		                        else if((thead<=360)&&(thead>=352.5))
		                        	th[i]=7;
		                        else if((thead<=352.5-5)&&(thead>=307.5))
		                        	th[i]=6;
		                        else if((thead<=307.5-5)&&(thead>=262.5))
		                        	th[i]=5;
		                        else if((thead<=262.5-5)&&(thead>=217.5))
		                        	th[i]=4;
		                        else if((thead<=217.5-5)&&(thead>=172.5))
		                        	th[i]=3;
		                        else if((thead<=172.5)&&(thead>=127.5))
		                        	th[i]=2;
		                        else if((thead<=172.5-5)&&(thead>=82.5))
		                        	th[i]=1;
			                }
			            }
			        	for(int i=0;i<11;i++){
			        		if(th[i]==0) chead[0]+=1;
			        		else if(th[i]==1) chead[1]+=1;
			        		else if(th[i]==2) chead[2]+=1;
			        		else if(th[i]==3) chead[3]+=1;
			        		else if(th[i]==4) chead[4]+=1;
			        		else if(th[i]==5) chead[5]+=1;
			        		else if(th[i]==6) chead[6]+=1;
			        		else if(th[i]==7) chead[7]+=1;
			        	}
			        	if(iexmax(chead)==chead[0]) heading=0;
			        	else if(iexmax(chead)==chead[1]) heading=1;
			        	else if(iexmax(chead)==chead[2]) heading=2;
			        	else if(iexmax(chead)==chead[3]) heading=3;
			        	else if(iexmax(chead)==chead[4]) heading=4;
			        	else if(iexmax(chead)==chead[5]) heading=5;
			        	else if(iexmax(chead)==chead[6]) heading=6;
			        	else if(iexmax(chead)==chead[7]) heading=7;
			        	
			        	for(int i=0; i<8; i++){
			            	chead[i]=0;
			            }
			        	status_all+=""+step+","+status+","+heading+"\n";
						//tview.setText("step:"+step+"\nstatus:"+status+"\nhead:"+heading+"\nlandmark:"+maxland);
					}
				}else{
					if(GraValue[0][5]>4){
						step+=1;
			        	//initWIFIScan();
			        	status=2.2;
			        	for(int i=0;i<11;i++){
		            		phead=thead;
			            	thead=OriValue[0][i];
			            	if(phead-thead>0){
			            		if((thead>=232.5+5)&&(thead<=277.5))
		                            th[i]=0;
		                        else if((thead>=277.5+5)&&(thead<=322.5))
		                        	th[i]=1;
		                        else if((thead>=322.5+5)&&(thead<360))
		                        	th[i]=2;
		                        else if((thead>=0)&&(thead<=7.5))
		                        	th[i]=2;
		                        else if((thead>=7.5+5)&&(thead<=52.5))
		                        	th[i]=3;
		                        else if((thead>=52.5+5)&&(thead<=97.5))
		                        	th[i]=4;
		                        else if((thead>=97.5+5)&&(thead<=142.5))
		                        	th[i]=5;
		                        else if((thead>=142.5+5)&&(thead<=187.5))
		                        	th[i]=6;
		                        else if((thead>=187.5+5)&&(thead<=232.5))
		                        	th[i]=7;
							}else if(phead-thead<0){
		                        if((thead<=277.5-5)&&(thead>=232.5))
		                        	th[i]=0;
		                        else if((thead<=232.5-5)&&(thead>=187.5))
		                        	th[i]=7;
		                        else if((thead<=187.5-5)&&(thead>=142.5))
		                        	th[i]=6;
		                        else if((thead<=142.5-5)&&(thead>=97.5))
		                        	th[i]=5;
		                        else if((thead<=97.5-5)&&(thead>=52.5))
		                        	th[i]=4;
		                        else if((thead<=52.5-5)&&(thead>=7.5))
		                        	th[i]=3;
		                        else if((thead<=7.5-5)&&(thead>0))
		                        	th[i]=2;
		                        else if((thead<=360)&&(thead>=322.5))
		                        	th[i]=2;
		                        else if((thead<=322.5-5)&&(thead>=277.5))
		                        	th[i]=1;
			                }
			            }
			        	for(int i=0;i<11;i++){
			        		if(th[i]==0) chead[0]+=1;
			        		else if(th[i]==1) chead[1]+=1;
			        		else if(th[i]==2) chead[2]+=1;
			        		else if(th[i]==3) chead[3]+=1;
			        		else if(th[i]==4) chead[4]+=1;
			        		else if(th[i]==5) chead[5]+=1;
			        		else if(th[i]==6) chead[6]+=1;
			        		else if(th[i]==7) chead[7]+=1;
			        	}
			        	if(iexmax(chead)==chead[0]) heading=0;
			        	else if(iexmax(chead)==chead[1]) heading=1;
			        	else if(iexmax(chead)==chead[2]) heading=2;
			        	else if(iexmax(chead)==chead[3]) heading=3;
			        	else if(iexmax(chead)==chead[4]) heading=4;
			        	else if(iexmax(chead)==chead[5]) heading=5;
			        	else if(iexmax(chead)==chead[6]) heading=6;
			        	else if(iexmax(chead)==chead[7]) heading=7;
			        	
			        	for(int i=0; i<8; i++){
			            	chead[i]=0;
			            }
			        	status_all+=""+step+","+status+","+heading+"\n";
						//tview.setText("step:"+step+"\nstatus:"+status+"\nhead:"+heading+"\nlandmark:"+maxland);
					}else if(GraValue[0][5]<-4){
						step+=1;
			        	//initWIFIScan();
			        	status=2;
			        	for(int i=0;i<11;i++){
		            		phead=thead;
			            	thead=OriValue[0][i];
			            	if(phead-thead>0){
			            		if((thead>=37.5+5)&&(thead<=82.5))
		                            th[i]=0;
		                        else if((thead>=82.5+5)&&(thead<=127.5))
		                        	th[i]=1;
		                        else if((thead>=127.5+5)&&(thead<=172.5))
		                        	th[i]=2;
		                        else if((thead>=172.5+5)&&(thead<=217.5))
		                        	th[i]=3;
		                        else if((thead>=217.5+5)&&(thead<=262.5))
		                        	th[i]=4;
		                        else if((thead>=262.5+5)&&(thead<=307.5))
		                        	th[i]=5;
		                        else if((thead>=307.5+5)&&(thead<=352.5))
		                        	th[i]=6;
		                        else if((thead>=352.5+5)&&(thead<=360))
		                        	th[i]=7;
		                        else if((thead>=0)&&(thead<=37.5))
		                        	th[i]=7;
							}else if(phead-thead<0){
		                        if((thead<=82.5-5)&&(thead>=37.5))
		                        	th[i]=0;
		                        else if((thead<=37.5-5)&&(thead>=0))
		                        	th[i]=7;
		                        else if((thead<=360)&&(thead>=352.5))
		                        	th[i]=7;
		                        else if((thead<=352.5-5)&&(thead>=307.5))
		                        	th[i]=6;
		                        else if((thead<=307.5-5)&&(thead>=262.5))
		                        	th[i]=5;
		                        else if((thead<=262.5-5)&&(thead>=217.5))
		                        	th[i]=4;
		                        else if((thead<=217.5-5)&&(thead>=172.5))
		                        	th[i]=3;
		                        else if((thead<=172.5)&&(thead>=127.5))
		                        	th[i]=2;
		                        else if((thead<=172.5-5)&&(thead>=82.5))
		                        	th[i]=1;
			                }
			            }
			        	for(int i=0;i<11;i++){
			        		if(th[i]==0) chead[0]+=1;
			        		else if(th[i]==1) chead[1]+=1;
			        		else if(th[i]==2) chead[2]+=1;
			        		else if(th[i]==3) chead[3]+=1;
			        		else if(th[i]==4) chead[4]+=1;
			        		else if(th[i]==5) chead[5]+=1;
			        		else if(th[i]==6) chead[6]+=1;
			        		else if(th[i]==7) chead[7]+=1;
			        	}
			        	if(iexmax(chead)==chead[0]) heading=0;
			        	else if(iexmax(chead)==chead[1]) heading=1;
			        	else if(iexmax(chead)==chead[2]) heading=2;
			        	else if(iexmax(chead)==chead[3]) heading=3;
			        	else if(iexmax(chead)==chead[4]) heading=4;
			        	else if(iexmax(chead)==chead[5]) heading=5;
			        	else if(iexmax(chead)==chead[6]) heading=6;
			        	else if(iexmax(chead)==chead[7]) heading=7;
			        	
			        	for(int i=0; i<8; i++){
			            	chead[i]=0;
			            }
			        	status_all+=""+step+","+status+","+heading+"\n";
						//tview.setText("step:"+step+"\nstatus:"+status+"\nhead:"+heading+"\nlandmark:"+maxland);
					}
				}
			}else{
				step+=1;
				//initWIFIScan();
	        	ppp=AccValue[1][5];
	        	status=0;
	        	status_all+=""+step+","+status+","+heading+"\n";
				//tview.setText("step:"+step+"\nstatus:"+status+"\nhead:"+heading+"\nlandmark:"+maxland);
			}
        }else if(AccValue[1][5]==exmin(AccValue[1])&&AccValue[1][5]<-5){
        	mpp=AccValue[1][5];
        	if(ppp!=0){
				if(Math.abs(mpp)>Math.abs(ppp)){
					if(GraValue[0][5]>4){
						step+=1;
			        	//initWIFIScan();
			        	status=2.2;
			        	for(int i=0;i<11;i++){
		            		phead=thead;
			            	thead=OriValue[0][i];
			            	if(phead-thead>0){
			            		if((thead>=232.5+5)&&(thead<=277.5))
		                            th[i]=0;
		                        else if((thead>=277.5+5)&&(thead<=322.5))
		                        	th[i]=1;
		                        else if((thead>=322.5+5)&&(thead<360))
		                        	th[i]=2;
		                        else if((thead>=0)&&(thead<=7.5))
		                        	th[i]=2;
		                        else if((thead>=7.5+5)&&(thead<=52.5))
		                        	th[i]=3;
		                        else if((thead>=52.5+5)&&(thead<=97.5))
		                        	th[i]=4;
		                        else if((thead>=97.5+5)&&(thead<=142.5))
		                        	th[i]=5;
		                        else if((thead>=142.5+5)&&(thead<=187.5))
		                        	th[i]=6;
		                        else if((thead>=187.5+5)&&(thead<=232.5))
		                        	th[i]=7;
							}else if(phead-thead<0){
		                        if((thead<=277.5-5)&&(thead>=232.5))
		                        	th[i]=0;
		                        else if((thead<=232.5-5)&&(thead>=187.5))
		                        	th[i]=7;
		                        else if((thead<=187.5-5)&&(thead>=142.5))
		                        	th[i]=6;
		                        else if((thead<=142.5-5)&&(thead>=97.5))
		                        	th[i]=5;
		                        else if((thead<=97.5-5)&&(thead>=52.5))
		                        	th[i]=4;
		                        else if((thead<=52.5-5)&&(thead>=7.5))
		                        	th[i]=3;
		                        else if((thead<=7.5-5)&&(thead>0))
		                        	th[i]=2;
		                        else if((thead<=360)&&(thead>=322.5))
		                        	th[i]=2;
		                        else if((thead<=322.5-5)&&(thead>=277.5))
		                        	th[i]=1;
			                }
			            }
			        	for(int i=0;i<11;i++){
			        		if(th[i]==0) chead[0]+=1;
			        		else if(th[i]==1) chead[1]+=1;
			        		else if(th[i]==2) chead[2]+=1;
			        		else if(th[i]==3) chead[3]+=1;
			        		else if(th[i]==4) chead[4]+=1;
			        		else if(th[i]==5) chead[5]+=1;
			        		else if(th[i]==6) chead[6]+=1;
			        		else if(th[i]==7) chead[7]+=1;
			        	}
			        	if(iexmax(chead)==chead[0]) heading=0;
			        	else if(iexmax(chead)==chead[1]) heading=1;
			        	else if(iexmax(chead)==chead[2]) heading=2;
			        	else if(iexmax(chead)==chead[3]) heading=3;
			        	else if(iexmax(chead)==chead[4]) heading=4;
			        	else if(iexmax(chead)==chead[5]) heading=5;
			        	else if(iexmax(chead)==chead[6]) heading=6;
			        	else if(iexmax(chead)==chead[7]) heading=7;
			        	
			        	for(int i=0; i<8; i++){
			            	chead[i]=0;
			            }
			        	status_all+=""+step+","+status+","+heading+"\n";
						//tview.setText("step:"+step+"\nstatus:"+status+"\nhead:"+heading+"\nlandmark:"+maxland);
					}else if(GraValue[0][5]<-4){
						step+=1;
			        	//initWIFIScan();
			        	status=2;
			        	for(int i=0;i<11;i++){
		            		phead=thead;
			            	thead=OriValue[0][i];
			            	if(phead-thead>0){
			            		if((thead>=37.5+5)&&(thead<=82.5))
		                            th[i]=0;
		                        else if((thead>=82.5+5)&&(thead<=127.5))
		                        	th[i]=1;
		                        else if((thead>=127.5+5)&&(thead<=172.5))
		                        	th[i]=2;
		                        else if((thead>=172.5+5)&&(thead<=217.5))
		                        	th[i]=3;
		                        else if((thead>=217.5+5)&&(thead<=262.5))
		                        	th[i]=4;
		                        else if((thead>=262.5+5)&&(thead<=307.5))
		                        	th[i]=5;
		                        else if((thead>=307.5+5)&&(thead<=352.5))
		                        	th[i]=6;
		                        else if((thead>=352.5+5)&&(thead<=360))
		                        	th[i]=7;
		                        else if((thead>=0)&&(thead<=37.5))
		                        	th[i]=7;
							}else if(phead-thead<0){
		                        if((thead<=82.5-5)&&(thead>=37.5))
		                        	th[i]=0;
		                        else if((thead<=37.5-5)&&(thead>=0))
		                        	th[i]=7;
		                        else if((thead<=360)&&(thead>=352.5))
		                        	th[i]=7;
		                        else if((thead<=352.5-5)&&(thead>=307.5))
		                        	th[i]=6;
		                        else if((thead<=307.5-5)&&(thead>=262.5))
		                        	th[i]=5;
		                        else if((thead<=262.5-5)&&(thead>=217.5))
		                        	th[i]=4;
		                        else if((thead<=217.5-5)&&(thead>=172.5))
		                        	th[i]=3;
		                        else if((thead<=172.5)&&(thead>=127.5))
		                        	th[i]=2;
		                        else if((thead<=172.5-5)&&(thead>=82.5))
		                        	th[i]=1;
			                }
			            }
			        	for(int i=0;i<11;i++){
			        		if(th[i]==0) chead[0]+=1;
			        		else if(th[i]==1) chead[1]+=1;
			        		else if(th[i]==2) chead[2]+=1;
			        		else if(th[i]==3) chead[3]+=1;
			        		else if(th[i]==4) chead[4]+=1;
			        		else if(th[i]==5) chead[5]+=1;
			        		else if(th[i]==6) chead[6]+=1;
			        		else if(th[i]==7) chead[7]+=1;
			        	}
			        	if(iexmax(chead)==chead[0]) heading=0;
			        	else if(iexmax(chead)==chead[1]) heading=1;
			        	else if(iexmax(chead)==chead[2]) heading=2;
			        	else if(iexmax(chead)==chead[3]) heading=3;
			        	else if(iexmax(chead)==chead[4]) heading=4;
			        	else if(iexmax(chead)==chead[5]) heading=5;
			        	else if(iexmax(chead)==chead[6]) heading=6;
			        	else if(iexmax(chead)==chead[7]) heading=7;
			        	
			        	for(int i=0; i<8; i++){
			            	chead[i]=0;
			            }
			        	status_all+=""+step+","+status+","+heading+"\n";
						//tview.setText("step:"+step+"\nstatus:"+status+"\nhead:"+heading+"\nlandmark:"+maxland);
					}
				}else{
					if(GraValue[0][5]>4){
						step+=1;
			        	//initWIFIScan();
			        	status=2.4;
			        	for(int i=0;i<11;i++){
		            		phead=thead;
			            	thead=OriValue[0][i];
			            	if(phead-thead>0){
			            		if((thead>=232.5+5)&&(thead<=277.5))
		                            th[i]=0;
		                        else if((thead>=277.5+5)&&(thead<=322.5))
		                        	th[i]=1;
		                        else if((thead>=322.5+5)&&(thead<360))
		                        	th[i]=2;
		                        else if((thead>=0)&&(thead<=7.5))
		                        	th[i]=2;
		                        else if((thead>=7.5+5)&&(thead<=52.5))
		                        	th[i]=3;
		                        else if((thead>=52.5+5)&&(thead<=97.5))
		                        	th[i]=4;
		                        else if((thead>=97.5+5)&&(thead<=142.5))
		                        	th[i]=5;
		                        else if((thead>=142.5+5)&&(thead<=187.5))
		                        	th[i]=6;
		                        else if((thead>=187.5+5)&&(thead<=232.5))
		                        	th[i]=7;
							}else if(phead-thead<0){
		                        if((thead<=277.5-5)&&(thead>=232.5))
		                        	th[i]=0;
		                        else if((thead<=232.5-5)&&(thead>=187.5))
		                        	th[i]=7;
		                        else if((thead<=187.5-5)&&(thead>=142.5))
		                        	th[i]=6;
		                        else if((thead<=142.5-5)&&(thead>=97.5))
		                        	th[i]=5;
		                        else if((thead<=97.5-5)&&(thead>=52.5))
		                        	th[i]=4;
		                        else if((thead<=52.5-5)&&(thead>=7.5))
		                        	th[i]=3;
		                        else if((thead<=7.5-5)&&(thead>0))
		                        	th[i]=2;
		                        else if((thead<=360)&&(thead>=322.5))
		                        	th[i]=2;
		                        else if((thead<=322.5-5)&&(thead>=277.5))
		                        	th[i]=1;
			                }
			            }
			        	for(int i=0;i<11;i++){
			        		if(th[i]==0) chead[0]+=1;
			        		else if(th[i]==1) chead[1]+=1;
			        		else if(th[i]==2) chead[2]+=1;
			        		else if(th[i]==3) chead[3]+=1;
			        		else if(th[i]==4) chead[4]+=1;
			        		else if(th[i]==5) chead[5]+=1;
			        		else if(th[i]==6) chead[6]+=1;
			        		else if(th[i]==7) chead[7]+=1;
			        	}
			        	if(iexmax(chead)==chead[0]) heading=0;
			        	else if(iexmax(chead)==chead[1]) heading=1;
			        	else if(iexmax(chead)==chead[2]) heading=2;
			        	else if(iexmax(chead)==chead[3]) heading=3;
			        	else if(iexmax(chead)==chead[4]) heading=4;
			        	else if(iexmax(chead)==chead[5]) heading=5;
			        	else if(iexmax(chead)==chead[6]) heading=6;
			        	else if(iexmax(chead)==chead[7]) heading=7;
			        	
			        	for(int i=0; i<8; i++){
			            	chead[i]=0;
			            }
			        	status_all+=""+step+","+status+","+heading+"\n";
						//tview.setText("step:"+step+"\nstatus:"+status+"\nhead:"+heading+"\nlandmark:"+maxland);
					}else if(GraValue[0][5]<-4){
						step+=1;
			        	//initWIFIScan();
			        	status=2.6;
			        	for(int i=0;i<11;i++){
		            		phead=thead;
			            	thead=OriValue[0][i];
			            	if(phead-thead>0){
			            		if((thead>=37.5+5)&&(thead<=82.5))
		                            th[i]=0;
		                        else if((thead>=82.5+5)&&(thead<=127.5))
		                        	th[i]=1;
		                        else if((thead>=127.5+5)&&(thead<=172.5))
		                        	th[i]=2;
		                        else if((thead>=172.5+5)&&(thead<=217.5))
		                        	th[i]=3;
		                        else if((thead>=217.5+5)&&(thead<=262.5))
		                        	th[i]=4;
		                        else if((thead>=262.5+5)&&(thead<=307.5))
		                        	th[i]=5;
		                        else if((thead>=307.5+5)&&(thead<=352.5))
		                        	th[i]=6;
		                        else if((thead>=352.5+5)&&(thead<=360))
		                        	th[i]=7;
		                        else if((thead>=0)&&(thead<=37.5))
		                        	th[i]=7;
							}else if(phead-thead<0){
		                        if((thead<=82.5-5)&&(thead>=37.5))
		                        	th[i]=0;
		                        else if((thead<=37.5-5)&&(thead>=0))
		                        	th[i]=7;
		                        else if((thead<=360)&&(thead>=352.5))
		                        	th[i]=7;
		                        else if((thead<=352.5-5)&&(thead>=307.5))
		                        	th[i]=6;
		                        else if((thead<=307.5-5)&&(thead>=262.5))
		                        	th[i]=5;
		                        else if((thead<=262.5-5)&&(thead>=217.5))
		                        	th[i]=4;
		                        else if((thead<=217.5-5)&&(thead>=172.5))
		                        	th[i]=3;
		                        else if((thead<=172.5)&&(thead>=127.5))
		                        	th[i]=2;
		                        else if((thead<=172.5-5)&&(thead>=82.5))
		                        	th[i]=1;
			                }
			            }
			        	for(int i=0;i<11;i++){
			        		if(th[i]==0) chead[0]+=1;
			        		else if(th[i]==1) chead[1]+=1;
			        		else if(th[i]==2) chead[2]+=1;
			        		else if(th[i]==3) chead[3]+=1;
			        		else if(th[i]==4) chead[4]+=1;
			        		else if(th[i]==5) chead[5]+=1;
			        		else if(th[i]==6) chead[6]+=1;
			        		else if(th[i]==7) chead[7]+=1;
			        	}
			        	if(iexmax(chead)==chead[0]) heading=0;
			        	else if(iexmax(chead)==chead[1]) heading=1;
			        	else if(iexmax(chead)==chead[2]) heading=2;
			        	else if(iexmax(chead)==chead[3]) heading=3;
			        	else if(iexmax(chead)==chead[4]) heading=4;
			        	else if(iexmax(chead)==chead[5]) heading=5;
			        	else if(iexmax(chead)==chead[6]) heading=6;
			        	else if(iexmax(chead)==chead[7]) heading=7;
			        	
			        	for(int i=0; i<8; i++){
			            	chead[i]=0;
			            }
			        	status_all+=""+step+","+status+","+heading+"\n";
						//tview.setText("step:"+step+"\nstatus:"+status+"\nhead:"+heading+"\nlandmark:"+maxland);
					}
				}
			}else{
				step+=1;
				//initWIFIScan();
	        	mpp=AccValue[1][5];
	        	status=0;
	        	status_all+=""+step+","+status+","+heading+"\n";
				//tview.setText("step:"+step+"\nstatus:"+status+"\nhead:"+heading+"\nlandmark:"+maxland);
			}
        }
	}
	
	public void Find_PlanC(){
		if(GyroValue[0][5]==exmax(GyroValue[0])&&GyroValue[0][5]>2.5){
			if(GraValue[1][5]>5){
				step+=2;
	        	//initWIFIScan();
	        	status=3;
	        	for(int i=0;i<11;i++){
            		phead=thead;
	            	thead=OriValue[0][i];
	            	if(phead-thead>0){
	            		if((thead>=232.5+5)&&(thead<=277.5))
                            th[i]=0;
                        else if((thead>=277.5+5)&&(thead<=322.5))
                        	th[i]=1;
                        else if((thead>=322.5+5)&&(thead<360))
                        	th[i]=2;
                        else if((thead>=0)&&(thead<=7.5))
                        	th[i]=2;
                        else if((thead>=7.5+5)&&(thead<=52.5))
                        	th[i]=3;
                        else if((thead>=52.5+5)&&(thead<=97.5))
                        	th[i]=4;
                        else if((thead>=97.5+5)&&(thead<=142.5))
                        	th[i]=5;
                        else if((thead>=142.5+5)&&(thead<=187.5))
                        	th[i]=6;
                        else if((thead>=187.5+5)&&(thead<=232.5))
                        	th[i]=7;
					}else if(phead-thead<0){
                        if((thead<=277.5-5)&&(thead>=232.5))
                        	th[i]=0;
                        else if((thead<=232.5-5)&&(thead>=187.5))
                        	th[i]=7;
                        else if((thead<=187.5-5)&&(thead>=142.5))
                        	th[i]=6;
                        else if((thead<=142.5-5)&&(thead>=97.5))
                        	th[i]=5;
                        else if((thead<=97.5-5)&&(thead>=52.5))
                        	th[i]=4;
                        else if((thead<=52.5-5)&&(thead>=7.5))
                        	th[i]=3;
                        else if((thead<=7.5-5)&&(thead>0))
                        	th[i]=2;
                        else if((thead<=360)&&(thead>=322.5))
                        	th[i]=2;
                        else if((thead<=322.5-5)&&(thead>=277.5))
                        	th[i]=1;
	                }
	            }
	        	for(int i=0;i<11;i++){
	        		if(th[i]==0) chead[0]+=1;
	        		else if(th[i]==1) chead[1]+=1;
	        		else if(th[i]==2) chead[2]+=1;
	        		else if(th[i]==3) chead[3]+=1;
	        		else if(th[i]==4) chead[4]+=1;
	        		else if(th[i]==5) chead[5]+=1;
	        		else if(th[i]==6) chead[6]+=1;
	        		else if(th[i]==7) chead[7]+=1;
	        	}
	        	if(iexmax(chead)==chead[0]) heading=0;
	        	else if(iexmax(chead)==chead[1]) heading=1;
	        	else if(iexmax(chead)==chead[2]) heading=2;
	        	else if(iexmax(chead)==chead[3]) heading=3;
	        	else if(iexmax(chead)==chead[4]) heading=4;
	        	else if(iexmax(chead)==chead[5]) heading=5;
	        	else if(iexmax(chead)==chead[6]) heading=6;
	        	else if(iexmax(chead)==chead[7]) heading=7;
	        	
	        	for(int i=0; i<8; i++){
	            	chead[i]=0;
	            }
	        	status_all+=""+step+","+status+","+heading+"\n";
				//tview.setText("step:"+step+"\nstatus:"+status+"\nhead:"+heading+"\nlandmark:"+maxland);
			}else if(GraValue[1][5]<-5){
				step+=2;
	        	//initWIFIScan();
	        	status=3.6;
	        	for(int i=0;i<11;i++){
            		phead=thead;
	            	thead=OriValue[0][i];
	            	if(phead-thead>0){
	            		if((thead>=37.5+5)&&(thead<=82.5))
                            th[i]=0;
                        else if((thead>=82.5+5)&&(thead<=127.5))
                        	th[i]=1;
                        else if((thead>=127.5+5)&&(thead<=172.5))
                        	th[i]=2;
                        else if((thead>=172.5+5)&&(thead<=217.5))
                        	th[i]=3;
                        else if((thead>=217.5+5)&&(thead<=262.5))
                        	th[i]=4;
                        else if((thead>=262.5+5)&&(thead<=307.5))
                        	th[i]=5;
                        else if((thead>=307.5+5)&&(thead<=352.5))
                        	th[i]=6;
                        else if((thead>=352.5+5)&&(thead<=360))
                        	th[i]=7;
                        else if((thead>=0)&&(thead<=37.5))
                        	th[i]=7;
					}else if(phead-thead<0){
                        if((thead<=82.5-5)&&(thead>=37.5))
                        	th[i]=0;
                        else if((thead<=37.5-5)&&(thead>=0))
                        	th[i]=7;
                        else if((thead<=360)&&(thead>=352.5))
                        	th[i]=7;
                        else if((thead<=352.5-5)&&(thead>=307.5))
                        	th[i]=6;
                        else if((thead<=307.5-5)&&(thead>=262.5))
                        	th[i]=5;
                        else if((thead<=262.5-5)&&(thead>=217.5))
                        	th[i]=4;
                        else if((thead<=217.5-5)&&(thead>=172.5))
                        	th[i]=3;
                        else if((thead<=172.5)&&(thead>=127.5))
                        	th[i]=2;
                        else if((thead<=172.5-5)&&(thead>=82.5))
                        	th[i]=1;
	                }
	            }
	        	for(int i=0;i<11;i++){
	        		if(th[i]==0) chead[0]+=1;
	        		else if(th[i]==1) chead[1]+=1;
	        		else if(th[i]==2) chead[2]+=1;
	        		else if(th[i]==3) chead[3]+=1;
	        		else if(th[i]==4) chead[4]+=1;
	        		else if(th[i]==5) chead[5]+=1;
	        		else if(th[i]==6) chead[6]+=1;
	        		else if(th[i]==7) chead[7]+=1;
	        	}
	        	if(iexmax(chead)==chead[0]) heading=0;
	        	else if(iexmax(chead)==chead[1]) heading=1;
	        	else if(iexmax(chead)==chead[2]) heading=2;
	        	else if(iexmax(chead)==chead[3]) heading=3;
	        	else if(iexmax(chead)==chead[4]) heading=4;
	        	else if(iexmax(chead)==chead[5]) heading=5;
	        	else if(iexmax(chead)==chead[6]) heading=6;
	        	else if(iexmax(chead)==chead[7]) heading=7;
	        	
	        	for(int i=0; i<8; i++){
	            	chead[i]=0;
	            }
	        	status_all+=""+step+","+status+","+heading+"\n";
				//tview.setText("step:"+step+"\nstatus:"+status+"\nhead:"+heading+"\nlandmark:"+maxland);
			}
        }else if(GyroValue[0][5]==exmin(GyroValue[0])&&GyroValue[0][5]<-2.5){
			if(GraValue[1][5]>5){
				step+=2;
	        	//initWIFIScan();
	        	status=3.2;
	        	for(int i=0;i<11;i++){
            		phead=thead;
	            	thead=OriValue[0][i];
	            	if(phead-thead>0){
	            		if((thead>=232.5+5)&&(thead<=277.5))
                            th[i]=0;
                        else if((thead>=277.5+5)&&(thead<=322.5))
                        	th[i]=1;
                        else if((thead>=322.5+5)&&(thead<360))
                        	th[i]=2;
                        else if((thead>=0)&&(thead<=7.5))
                        	th[i]=2;
                        else if((thead>=7.5+5)&&(thead<=52.5))
                        	th[i]=3;
                        else if((thead>=52.5+5)&&(thead<=97.5))
                        	th[i]=4;
                        else if((thead>=97.5+5)&&(thead<=142.5))
                        	th[i]=5;
                        else if((thead>=142.5+5)&&(thead<=187.5))
                        	th[i]=6;
                        else if((thead>=187.5+5)&&(thead<=232.5))
                        	th[i]=7;
					}else if(phead-thead<0){
                        if((thead<=277.5-5)&&(thead>=232.5))
                        	th[i]=0;
                        else if((thead<=232.5-5)&&(thead>=187.5))
                        	th[i]=7;
                        else if((thead<=187.5-5)&&(thead>=142.5))
                        	th[i]=6;
                        else if((thead<=142.5-5)&&(thead>=97.5))
                        	th[i]=5;
                        else if((thead<=97.5-5)&&(thead>=52.5))
                        	th[i]=4;
                        else if((thead<=52.5-5)&&(thead>=7.5))
                        	th[i]=3;
                        else if((thead<=7.5-5)&&(thead>0))
                        	th[i]=2;
                        else if((thead<=360)&&(thead>=322.5))
                        	th[i]=2;
                        else if((thead<=322.5-5)&&(thead>=277.5))
                        	th[i]=1;
	                }
	            }
	        	for(int i=0;i<11;i++){
	        		if(th[i]==0) chead[0]+=1;
	        		else if(th[i]==1) chead[1]+=1;
	        		else if(th[i]==2) chead[2]+=1;
	        		else if(th[i]==3) chead[3]+=1;
	        		else if(th[i]==4) chead[4]+=1;
	        		else if(th[i]==5) chead[5]+=1;
	        		else if(th[i]==6) chead[6]+=1;
	        		else if(th[i]==7) chead[7]+=1;
	        	}
	        	if(iexmax(chead)==chead[0]) heading=0;
	        	else if(iexmax(chead)==chead[1]) heading=1;
	        	else if(iexmax(chead)==chead[2]) heading=2;
	        	else if(iexmax(chead)==chead[3]) heading=3;
	        	else if(iexmax(chead)==chead[4]) heading=4;
	        	else if(iexmax(chead)==chead[5]) heading=5;
	        	else if(iexmax(chead)==chead[6]) heading=6;
	        	else if(iexmax(chead)==chead[7]) heading=7;
	        	
	        	for(int i=0; i<8; i++){
	            	chead[i]=0;
	            }
	        	status_all+=""+step+","+status+","+heading+"\n";
				//tview.setText("step:"+step+"\nstatus:"+status+"\nhead:"+heading+"\nlandmark:"+maxland);
			}else if(GraValue[1][5]<-5){
				step+=2;
	        	//initWIFIScan();
	        	status=3.4;
	        	for(int i=0;i<11;i++){
            		phead=thead;
	            	thead=OriValue[0][i];
	            	if(phead-thead>0){
	            		if((thead>=37.5+5)&&(thead<=82.5))
                            th[i]=0;
                        else if((thead>=82.5+5)&&(thead<=127.5))
                        	th[i]=1;
                        else if((thead>=127.5+5)&&(thead<=172.5))
                        	th[i]=2;
                        else if((thead>=172.5+5)&&(thead<=217.5))
                        	th[i]=3;
                        else if((thead>=217.5+5)&&(thead<=262.5))
                        	th[i]=4;
                        else if((thead>=262.5+5)&&(thead<=307.5))
                        	th[i]=5;
                        else if((thead>=307.5+5)&&(thead<=352.5))
                        	th[i]=6;
                        else if((thead>=352.5+5)&&(thead<=360))
                        	th[i]=7;
                        else if((thead>=0)&&(thead<=37.5))
                        	th[i]=7;
					}else if(phead-thead<0){
                        if((thead<=82.5-5)&&(thead>=37.5))
                        	th[i]=0;
                        else if((thead<=37.5-5)&&(thead>=0))
                        	th[i]=7;
                        else if((thead<=360)&&(thead>=352.5))
                        	th[i]=7;
                        else if((thead<=352.5-5)&&(thead>=307.5))
                        	th[i]=6;
                        else if((thead<=307.5-5)&&(thead>=262.5))
                        	th[i]=5;
                        else if((thead<=262.5-5)&&(thead>=217.5))
                        	th[i]=4;
                        else if((thead<=217.5-5)&&(thead>=172.5))
                        	th[i]=3;
                        else if((thead<=172.5)&&(thead>=127.5))
                        	th[i]=2;
                        else if((thead<=172.5-5)&&(thead>=82.5))
                        	th[i]=1;
	                }
	            }
	        	for(int i=0;i<11;i++){
	        		if(th[i]==0) chead[0]+=1;
	        		else if(th[i]==1) chead[1]+=1;
	        		else if(th[i]==2) chead[2]+=1;
	        		else if(th[i]==3) chead[3]+=1;
	        		else if(th[i]==4) chead[4]+=1;
	        		else if(th[i]==5) chead[5]+=1;
	        		else if(th[i]==6) chead[6]+=1;
	        		else if(th[i]==7) chead[7]+=1;
	        	}
	        	if(iexmax(chead)==chead[0]) heading=0;
	        	else if(iexmax(chead)==chead[1]) heading=1;
	        	else if(iexmax(chead)==chead[2]) heading=2;
	        	else if(iexmax(chead)==chead[3]) heading=3;
	        	else if(iexmax(chead)==chead[4]) heading=4;
	        	else if(iexmax(chead)==chead[5]) heading=5;
	        	else if(iexmax(chead)==chead[6]) heading=6;
	        	else if(iexmax(chead)==chead[7]) heading=7;
	        	
	        	for(int i=0; i<8; i++){
	            	chead[i]=0;
	            }
	        	status_all+=""+step+","+status+","+heading+"\n";
				//tview.setText("step:"+step+"\nstatus:"+status+"\nhead:"+heading+"\nlandmark:"+maxland); //
			}
		
		}

	}
	
	/*
	void save_data(String tstr1, String tstr2){															//이건 내 코드에 필요 없음.
		String ext = Environment.getExternalStorageState();
		String sdpath="";
        if(ext.equals(Environment.MEDIA_MOUNTED)){
        	sdpath = Environment.getExternalStorageDirectory().getAbsolutePath();
        }else{
        	sdpath = Environment.MEDIA_UNMOUNTED;
        }
        File file1 = new File(sdpath + "/s_planABC/status.txt");
        File file2 = new File(sdpath + "/s_planABC/maxland.txt");
        File dir = new File(sdpath + "/s_planB");
		dir.mkdir();
		try{
			FileOutputStream fos = new FileOutputStream(file1);    
			fos.write(tstr1.getBytes());
			fos.close();
			save_state++;
			Toast.makeText(getBaseContext(), "Status Save Success", Toast.LENGTH_SHORT).show();
		}catch(FileNotFoundException e){
			Toast.makeText(getBaseContext(), "File Not Found"+e.getMessage(), Toast.LENGTH_SHORT).show();
		}catch(SecurityException e){
			Toast.makeText(getBaseContext(), "Security Exception", Toast.LENGTH_SHORT).show();
		}catch(Exception e){
			Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		try{
			FileOutputStream fos = new FileOutputStream(file2);    
			fos.write(tstr2.getBytes());
			fos.close();
			save_state++;
			Toast.makeText(getBaseContext(), "Landmark Save Success", Toast.LENGTH_SHORT).show();
		}catch(FileNotFoundException e){
			Toast.makeText(getBaseContext(), "File Not Found"+e.getMessage(), Toast.LENGTH_SHORT).show();
		}catch(SecurityException e){
			Toast.makeText(getBaseContext(), "Security Exception", Toast.LENGTH_SHORT).show();
		}catch(Exception e){
			Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		if(save_state==2)
			tview.setText("Save Success");
	}
	*/
	
	float exmax(float[] a){
		float b=0;
		for(int i=0;i<11;i++){
			if(b<a[i]) b= a[i];
		}
		return b;
	}
	int iexmax(int[] a){
		int b=0;
		for(int i=0;i<8;i++){
			if(b<a[i]) b=a[i];
		}
		return b;
	}
	float exmin(float[] a){
		float b=9999;
		for(int i=0;i<11;i++){
			if(b>a[i]) b= a[i];
		}
		return b;
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("deprecation")
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if(mStop==false){
			
			if(Sensor.TYPE_LINEAR_ACCELERATION==event.sensor.getType()){
				for(int i=0; i<10; i++){
					AccValue[0][i]=AccValue[0][i+1];
					AccValue[1][i]=AccValue[1][i+1];
					AccValue[2][i]=AccValue[2][i+1];
				}
				AccValue[0][10]=event.values[0];
				AccValue[1][10]=event.values[1];
				AccValue[2][10]=event.values[2];	
				wave=0;
				wvar=0;
				for(int i=0;i<11;i++){
					wave+=(float)Math.sqrt(Math.pow(AccValue[0][i], 2)+Math.pow(AccValue[1][i], 2)+Math.pow(AccValue[2][i], 2));
				}
				wave=wave/11;
				for(int i=0;i<11;i++){
					wdev[i]=((float)Math.sqrt(Math.pow(AccValue[0][i], 2)+Math.pow(AccValue[1][i], 2)+Math.pow(AccValue[2][i], 2)))-wave;
				}
				for(int i=0;i<11;i++){
					wvar+=Math.pow(wdev[i], 2);
				}
				wvar=wvar/11;
				
//				for(int i=0;i<10;i++){
//					Ysum1[i]=Ysum1[i+1];
//					//Ysum2[i]=Ysum2[i+1];
//				}
//				for(int i=0;i<11;i++){
//					Ysum1[10]+=AccValue[1][i];
//				}
////				for(int i=0;i<11;i++){
////					Ysum2[10]+=Ysum1[i];
////				}
				
				if(wvar>0.15){
					if(wgvar<0.2){
						Find_PlanA();
					}
				}
				
			}
			if(Sensor.TYPE_GYROSCOPE==event.sensor.getType()){
				for(int i=0; i<10; i++){
					GyroValue[0][i]=GyroValue[0][i+1];
					GyroValue[1][i]=GyroValue[1][i+1];
					GyroValue[2][i]=GyroValue[2][i+1];
				}
				GyroValue[0][10]=event.values[0];
				GyroValue[1][10]=event.values[1];
				GyroValue[2][10]=event.values[2];
				wgave=0;
				wgvar=0;
				wgavex=0;
				wgvarx=0;
				wgavez=0;
				wgvarz=0;
				for(int i=0;i<11;i++){
					wgave+=(float)Math.sqrt(Math.pow(GyroValue[0][i], 2)+Math.pow(GyroValue[1][i], 2)+Math.pow(GyroValue[2][i], 2));
				}
				wgave=wgave/11;
				for(int i=0;i<11;i++){
					wgdev[i]=((float)Math.sqrt(Math.pow(GyroValue[0][i], 2)+Math.pow(GyroValue[1][i], 2)+Math.pow(GyroValue[2][i], 2)))-wgave;
				}
				for(int i=0;i<11;i++){
					wgvar+=Math.pow(wgdev[i], 2);
				}
				wgvar=wgvar/11;
				
				for(int i=0;i<11;i++){
					wgavex+=GyroValue[0][i];
					wgavez+=GyroValue[2][i];
				}
				wgavex=wgavex/11;
				wgavez=wgavez/11;
				for(int i=0;i<11;i++){
					wgdevx[i]=GyroValue[0][i]-wgavex;
					wgdevz[i]=GyroValue[2][i]-wgavez;
				}
				for(int i=0;i<11;i++){
					wgvarx+=Math.pow(wgdevx[i], 2);
					wgvarz+=Math.pow(wgdevz[i], 2);
				}
				wgvarx=wgvarx/11;
				wgvarz=wgvarz/11;
				if(wvar>0.15){
					if(wgvarz<wgvarx&&wgvar>=0.1){
						Find_PlanC();
					}
					else if(wgvarz>wgvarx&&wgvar>=0.1){
						Find_PlanB();
					}
				}
			}
			if(Sensor.TYPE_ORIENTATION==event.sensor.getType()){
				for(int i=0; i<10; i++){
					OriValue[0][i]=OriValue[0][i+1];
					OriValue[1][i]=OriValue[1][i+1];
					OriValue[2][i]=OriValue[2][i+1];
				}
				OriValue[0][10]=event.values[0];
				OriValue[1][10]=event.values[1];
				OriValue[2][10]=event.values[2];
			}
			if(Sensor.TYPE_GRAVITY==event.sensor.getType()){
				for(int i=0; i<10; i++){
					GraValue[0][i]=GraValue[0][i+1];
					GraValue[1][i]=GraValue[1][i+1];
					GraValue[2][i]=GraValue[2][i+1];
				}
				GraValue[0][10]=event.values[0];
				GraValue[1][10]=event.values[1];
				GraValue[2][10]=event.values[2];
			}
		}
	}

}
