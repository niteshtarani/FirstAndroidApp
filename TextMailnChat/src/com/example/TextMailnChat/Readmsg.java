package com.example.TextMailnChat;

import com.example.TextMailnChat.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class Readmsg extends Activity
{
	String s="";
	ListView list;
	String[] msgs;
	String[] addr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msglist);
		
		int i=0;
		Uri uriSMSURI = Uri.parse("content://sms/inbox");
	    Cursor cur = getContentResolver().query(uriSMSURI, null, null, null,null);
	    msgs = new String[cur.getCount()];
	    addr = new String[cur.getCount()];
	    while (cur.moveToNext()) 
	    {
	        addr[i] = cur.getString(2)+": ";
	        msgs[i] = cur.getString(cur.getColumnIndexOrThrow("body"));
	        i++;
	        
	    }
	    CustomList adapter = new CustomList(Readmsg.this, msgs, addr);
	    list=(ListView)findViewById(R.id.lv);
	    list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,int position, long id)
                {
                    s=msgs[+position];
                    finish();
                }
            });
  }
	
	
	public void finish() {
		  // Prepare data intent 
		  Intent data = new Intent();
		  data.putExtra("returnKey", s);
		  setResult(RESULT_OK, data);
		  super.finish();
		} 
}
