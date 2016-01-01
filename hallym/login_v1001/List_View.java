package com.hallym.login_v1001;

import java.util.ArrayList;

import com.hallym.login_v1001.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class List_View extends Activity {

	ArrayList<String> Items;
	ArrayAdapter<String> Adapter;
	ListView list;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_view);

		Items = new ArrayList<String>();
		Items.add("First");
		Items.add("Second");
		Items.add("Third");

		Adapter = new ArrayAdapter<String>(this, android.R.layout.
				simple_list_item_single_choice, Items);
		list = (ListView)findViewById(R.id.list_view_list);
		list.setAdapter(Adapter);
		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		//list.setOnItemClickListener(mItemClickListener);
	}

	public void mOnClick(View v) {
		//EditText ed = (EditText)findViewById(R.id.newitem);
		switch (v.getId()) {
		/*
		case R.id.add:
			String text = ed.getText().toString();
			if (text.length() != 0) {
				Items.add(text);
				ed.setText("");
				Adapter.notifyDataSetChanged();
			}
			break;
			*/
		case R.id.list_view_delete:
			int pos;
			pos = list.getCheckedItemPosition();
			if (pos != ListView.INVALID_POSITION) {
				Items.remove(pos);
				list.clearChoices();
				Adapter.notifyDataSetChanged();
			}
			break;
			
		}
		
	}
	
	
	AdapterView.OnItemClickListener mItemClickListener = 
		new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView parent, View view, int position, long id) {
			String mes;
			mes = "Select Item = " + Items.get(position);
			Toast.makeText(List_View.this,mes,Toast.LENGTH_SHORT).show();
		}
	};
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_list__view, menu);
		return true;
	}

}
