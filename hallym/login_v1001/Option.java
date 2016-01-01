package com.hallym.login_v1001;


import com.hallym.login_v1001.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class Option extends Activity {
	Switch sw_video;
	Switch sw_audio;
	Switch sw_picture;
	Switch sw_text;
	
	Button bt_compl;
	Button bt_canl;
	
	RadioGroup rg;
	
	boolean b_vi;
	boolean b_au;
	boolean b_pi;
	boolean b_te;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_option);
		
		Intent get_Main = getIntent();
		
		b_vi = get_Main.getBooleanExtra("video", true);
		b_au = get_Main.getBooleanExtra("audio", true);
		b_pi = get_Main.getBooleanExtra("picture", true);
		b_te = get_Main.getBooleanExtra("text", true);
		
		/*
		sw_video = (Switch)findViewById(R.id.op_video); 
		sw_video.setChecked(b_vi);
		sw_video.setOnCheckedChangeListener(sw_v_ck);
		
		sw_audio = (Switch)findViewById(R.id.op_Audio); 
		sw_audio.setChecked(b_au);
		sw_audio.setOnCheckedChangeListener(sw_a_ck);
		
		sw_picture = (Switch)findViewById(R.id.op_picture); 
		sw_picture.setChecked(b_pi);
		sw_picture.setOnCheckedChangeListener(sw_p_ck);
		
		sw_text = (Switch)findViewById(R.id.op_text); 
		sw_text.setChecked(b_te);
		sw_text.setOnCheckedChangeListener(sw_t_ck);
		*/
		
		rg = (RadioGroup) findViewById(R.id.op_radio_g);
		
		if(b_vi)
		{
			rg.check(R.id.op_radio_0);	
		}
		else if(b_au)
		{
			rg.check(R.id.op_radio_1);	
		}
		else if(b_pi)
		{
			rg.check(R.id.op_radio_2);	
		}
		
		
        rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
	        	
				switch(checkedId)
				{
					case R.id.op_radio_0:
						b_vi = true;
						b_au = false;
						b_pi = false;
					// video
					break;
					case R.id.op_radio_1:
						b_vi = false;
						b_au = true;
						b_pi = false;
					// sound
					break;
					case R.id.op_radio_2:
						b_vi = false;
						b_au = false;
						b_pi = true;
					// text+picture
					break;
				}	     
			}
        });

		
		
		bt_compl = (Button)findViewById(R.id.op_complete);
		bt_compl.setOnClickListener(new Button.OnClickListener() {		
			public void onClick(android.view.View v) {
				// TODO Auto-generated method stub
				Intent intent = getIntent();
				intent.putExtra("vedio", b_vi);
				intent.putExtra("audio", b_au);
				intent.putExtra("picture", b_pi);
				intent.putExtra("text", b_te);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		
		bt_canl = (Button)findViewById(R.id.op_cancel);
		bt_canl.setOnClickListener(new Button.OnClickListener() {		
			public void onClick(android.view.View v) {
				// TODO Auto-generated method stub
				setResult(RESULT_CANCELED);
				finish();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.option, menu);
		return true;
	}
	
	public void onStop()
	{
		super.onStop();	
	}
	
	public Switch.OnCheckedChangeListener sw_v_ck = new Switch.OnCheckedChangeListener()
	{
		 public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			 b_vi = arg1;
			 Toast.makeText(Option.this, "value : " + b_vi, 
						Toast.LENGTH_LONG).show();
		 }
	};
	
	public Switch.OnCheckedChangeListener sw_a_ck = new Switch.OnCheckedChangeListener()
	{
		 public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			 b_au = arg1;
			 Toast.makeText(Option.this, "value : " + b_au, 
						Toast.LENGTH_LONG).show();
		 }
	};
	
	public Switch.OnCheckedChangeListener sw_p_ck = new Switch.OnCheckedChangeListener()
	{
		 public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			 b_pi = arg1;
			 Toast.makeText(Option.this, "value : " + b_pi, 
						Toast.LENGTH_LONG).show();
		 }
	};
	
	public Switch.OnCheckedChangeListener sw_t_ck = new Switch.OnCheckedChangeListener()
	{
		 public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			 b_te = arg1;
			 Toast.makeText(Option.this, "value : " + b_te, 
						Toast.LENGTH_LONG).show();
		 }
	};


}
