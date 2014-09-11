package com.example.TextMailnChat;

import com.example.TextMailnChat.R;

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
	String s="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msglist);
		
		ListView smslist = (ListView) findViewById(R.id.lv);
		
		final String[] addrss=readsms();
		
		smslist.setAdapter(new ArrayAdapter<String>(Readmsg.this,android.R.layout.simple_list_item_1,addrss));
		
		smslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent fetchmsgintent = new Intent("com.example.TextMailnChat.READMSGUTIL");
				Bundle b = new Bundle();
				s=addrss[arg2];
				b.putString("addr", addrss[arg2]);
				fetchmsgintent.putExtras(b);
				startActivityForResult(fetchmsgintent, 1);
				
			}
			
		});
		
	}
	
	
	
	public String[] readsms()
	{
		int i=0;
		  Uri uriSMSURI = Uri.parse("content://sms/");
		  String[] projection = new String[] { "_id", "address", "person", "body", "date", "type" };
	      Cursor cur = getContentResolver().query(uriSMSURI, projection, null, null,null);
	      String[] tempmsg = new String[cur.getCount()];
	      while (cur.moveToNext()) 
	      {
	          tempmsg[i] = cur.getString(cur.getColumnIndexOrThrow("address"));
	          i++;
	      }
	      return removedup(tempmsg);
	}
	
	String[] removedup(String src[])
	{
		int len=src.length;
		for(int i=0;i<len;i++)
		{
			for(int j=i+1;j<len;)
			{
				if(src[i].equals(src[j]))
					src[j]=src[--len];
				else
					j++;
			}
		}
		String returnstr[]= new String[len];
		for(int i=0;i<len;i++)
			returnstr[i]=src[i];
		
		return returnstr;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_OK && null != data)
		{
			s=data.getExtras().getString("msgbody");
			if(!s.equalsIgnoreCase(""))
				finish();
		}
		
	}

	
	public void finish() {
		  // Prepare data intent 
		  Intent data = new Intent();
		  data.putExtra("returnKey", s);
		  setResult(RESULT_OK, data);
		  super.finish();
		} 
}
