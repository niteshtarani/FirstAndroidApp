package com.example.TextnMail;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Readmsg extends Activity
{
	String s;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msglist);
		
		ListView smslist = (ListView) findViewById(R.id.lv);
		
		final String[] msgs=readsms();
		
		smslist.setAdapter(new ArrayAdapter<String>(Readmsg.this,android.R.layout.simple_list_item_1,msgs));
		
		smslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				s = msgs[arg2];
				finish();
			}
			
		});
		
	}
	
	
	
	public String[] readsms()
	{
		int i=0;
		  Uri uriSMSURI = Uri.parse("content://sms/inbox");
	      Cursor cur = getContentResolver().query(uriSMSURI, null, null, null,null);
	      String[] tempmsg = new String[2*cur.getCount()];
	      while (cur.moveToNext()) 
	      {
	          tempmsg[i] = "From/To :" + cur.getString(2);
	          i++;
	          tempmsg[i] = " "+cur.getString(cur.getColumnIndexOrThrow("body"));
	          i++;
	      }
	      return tempmsg;
	}
	
	public void finish() {
		  // Prepare data intent 
		  Intent data = new Intent();
		  data.putExtra("returnKey", s);
		  setResult(RESULT_OK, data);
		  super.finish();
		} 
}
