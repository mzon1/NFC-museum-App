package com.hallym.login_v1001;

import java.util.ArrayList;

import com.hallym.login_v1001.R;
import com.hallym.network.networkState;
import com.hallym.streaming.EX3MainActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

public class List_view_test extends Activity {
	ArrayList<String> Items;
	ArrayAdapter<String> Adapter;

	ListView list;
	Button delete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_view_test);
		
		//MyListAdapter MyAdapter = new MyListAdapter(this, R.layout.custom_list, networkState.aList);
		Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, networkState.aList);
	
		list = (ListView)findViewById(R.id.list_V_list);
		list.setAdapter(Adapter);
		//list.setAdapter(MyAdapter);
		list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		list.setOnItemClickListener(mItemClickListener);
		
		delete = (Button)findViewById(R.id.list_V_del);

		delete.setOnClickListener(new Button.OnClickListener() {		
			public void onClick(android.view.View v) {
				// TODO Auto-generated method stub
				SparseBooleanArray sb = list.getCheckedItemPositions();
				if(sb.size() !=0)
				{
					for(int i = list.getCount() -1; i>=0; i--)
					{
						if(sb.get(i))
						{
							networkState.aList.remove(i);
							networkState.aListAdress.remove(i);
							networkState.SIZE = networkState.aList.size();
						}
					}
					list.clearChoices();
					Adapter.notifyDataSetChanged();
				}
				/*
				int pos;
				pos = list.getCheckedItemPosition();
				if (pos != ListView.INVALID_POSITION) {
					networkState.aList.remove(pos);
					list.clearChoices();
					Adapter.notifyDataSetChanged();
					Toast.makeText(List_view_test.this, "delete", Toast.LENGTH_SHORT).show();
				}
				*/		
			}
		});
	}
	/*
	public void mOnClick(View v) {
		switch (v.getId()) {
			case R.id.list_V_del:
				int pos;
				pos = list.getCheckedItemPosition();
				if (pos != ListView.INVALID_POSITION) {
					Items.remove(pos);
					list.clearChoices();
					Adapter.notifyDataSetChanged();
					Toast.makeText(List_view_test.this, "delete", Toast.LENGTH_SHORT).show();
				}
				break;	
		}		
	}
	*/
	
	AdapterView.OnItemClickListener mItemClickListener = 
			new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView parent, View view, int position, long id) {
				String mes;
				mes = "Select Item = " + networkState.aList.get(position);
				mes = "Select Item = " + networkState.aListAdress.get(position);
				Toast.makeText(List_view_test.this, mes, Toast.LENGTH_SHORT).show();		
				
				Intent active = new Intent(List_view_test.this, EX3MainActivity.class);
				active.putExtra("URL", networkState.aListAdress.get(position));
				startActivity(active);	
			}
		};
		

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_list_view_test, menu);
		return true;
	}
	
	public class MyListAdapter extends BaseAdapter {
		Context maincon;
		LayoutInflater Inflater;
		ArrayList<String> arSrc;
		int layout;

		public MyListAdapter(Context context, int alayout, ArrayList<String> aarSrc) {
			maincon = context;
			Inflater = (LayoutInflater)context.getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			arSrc = aarSrc;
			layout = alayout;
		}

		public int getCount() {
			return arSrc.size();
		}

		public String getItem(int position) {
			return arSrc.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		// 각 항목의 뷰 생성
		public View getView(int position, View convertView, ViewGroup parent) {
			final int pos = position;
			if (convertView == null) {
				convertView = Inflater.inflate(layout, parent, false);
			}

			CheckedTextView ctv = (CheckedTextView)convertView.findViewById(R.id.text1);
			ctv.setText(arSrc.get(position));

			Button btn = (Button)convertView.findViewById(R.id.button1);
			btn.setOnClickListener(new Button.OnClickListener() {
				public void onClick(android.view.View v) {
					String str = arSrc.get(pos) + "를 리플레이.";
					Toast.makeText(maincon, str, Toast.LENGTH_SHORT).show();
				}
			});
			
			Button del = (Button)convertView.findViewById(R.id.button2);
			btn.setOnClickListener(new Button.OnClickListener() {
				public void onClick(android.view.View v) {
					String str = arSrc.get(pos) + "를 삭제.";
					Toast.makeText(maincon, str, Toast.LENGTH_SHORT).show();
					networkState.aList.remove(pos);
					networkState.aListAdress.remove(pos);
					networkState.SIZE = networkState.aList.size();
					
					list.clearChoices();
					Adapter.notifyDataSetChanged();
				}
			});

			return convertView;
		}
	}


}

