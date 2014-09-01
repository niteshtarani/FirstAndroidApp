package com.example.TextMailnChat;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Readmsgutil extends Activity{

	String copy="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msglistbody);
		
		Bundle getbun = getIntent().getExtras();
		String number = getbun.getString("addr");
		
		ListView msglist = (ListView) findViewById(R.id.bodylist);
		
		final String msgs[] = readsmsbody(number);
		
		msglist.setAdapter(new ArrayAdapter<String>(Readmsgutil.this,android.R.layout.simple_list_item_1,msgs));
		
		msglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			copy = msgs[arg2];
			finish();
		}
		
	});
		
		
	}
	
	public String[] readsmsbody(String number)
	{
		String addressof = "address='"+number+"'";
		int i=0;
		  Uri uriSMSURI = Uri.parse("content://sms/");
		  String[] projection = new String[] { "_id", "address", "person", "body", "date", "type" };
	      Cursor cur = getContentResolver().query(uriSMSURI, projection, addressof, null,null);
	      String[] allmsgs = new String[cur.getCount()];
	      while (cur.moveToNext()) 
	      {
	          allmsgs[i] = cur.getString(cur.getColumnIndexOrThrow("body"));
	          i++;
	      }
	      return allmsgs;
	}
	
	public void finish() {
		  // Prepare data intent 
		  Intent mydata = new Intent();
		  mydata.putExtra("msgbody", copy);
		  setResult(RESULT_OK, mydata);
		  super.finish();
		} 
	

}
