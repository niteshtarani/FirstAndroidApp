package com.example.TextMailnChat;

import java.util.ArrayList;

import com.example.TextMailnChat.R;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.speech.RecognizerIntent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Email extends Activity implements OnClickListener{
	
	Button email_send,email_inbx;
	ImageButton email_voice;
	EditText etaddress,etsubject,etbody;
	String sub,addr,body,selectedLang;
	protected static final int RESULT_SPEECH = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		email_send = (Button) findViewById(R.id.email_sendbtn);
		email_voice = (ImageButton) findViewById(R.id.email_voicebtn);
		etaddress = (EditText) findViewById(R.id.email_address_);
		etsubject = (EditText) findViewById(R.id.email_subject);
		etbody = (EditText) findViewById(R.id.email_body);
		email_inbx = (Button) findViewById(R.id.email_inbox);
		
		email_send.setOnClickListener(this);
		email_voice.setOnClickListener(this);
		email_inbx.setOnClickListener(this);
		
		SharedPreferences getlang = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		selectedLang = getlang.getString("select_lang", "1");
		
		Bundle recv1 = getIntent().getExtras();
		etbody.setText(recv1.getString("msg_email"));
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		super.onOptionsItemSelected(item);
		
		switch(item.getItemId())
		{
		case R.id.about_menu:
			
			Intent int_about = new Intent("com.example.TextMailnChat.ABOUT");
			startActivity(int_about);
			
			break;
			
		case R.id.Text_menu:
			
			finish();
			Intent int_text = new Intent("com.example.TextMailnChat.TEXT");
			Bundle bt = new Bundle();
			bt.putString("txtmsg", etbody.getText().toString());
			int_text.putExtras(bt);
			startActivity(int_text);
			break;
			
		case R.id.pref_menu:
			
			Intent int_pref = new Intent("com.example.TextMailnChat.PREFS");
			startActivity(int_pref);
			
			break;
			
		case R.id.way2sms_menu:
			
			finish();
			Intent waysms_int = new Intent("com.example.TextMailnChat.WAY2SMS");
			Bundle b3 = new Bundle();
			b3.putString("msg_w2s", etbody.getText().toString());
			waysms_int.putExtras(b3);
			startActivity(waysms_int);
			break;
			
		case R.id.chat_menu:
		
			Intent chatintent = new Intent("com.example.TextMailnChat.BLUETOOTHCHAT");
			startActivity(chatintent);
			break;
			
		case R.id.widget_menu:
			
			Intent widgetpopup = new Intent("com.example.TextMailnChat.WIDGETINSTRUCTIONS");
			startActivity(widgetpopup);
			break;
						
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId())
		{
		case R.id.email_voicebtn:
			Intent intent = new Intent(
					RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

			try 
			{
				startActivityForResult(intent, RESULT_SPEECH);
			} 
			
			catch (ActivityNotFoundException a)
			{
				Toast t = Toast.makeText(getApplicationContext(),
						"Ops! Your device doesn't support Speech to Text",
						Toast.LENGTH_SHORT);
				t.show();
			}
			break;
			
		case R.id.email_sendbtn:
			addr=etaddress.getText().toString();
			sub=etsubject.getText().toString();
			body=etbody.getText().toString();
			
			String addressArray[]={addr};
			
			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
			
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, addressArray);
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, sub);
			emailIntent.setType("plain/text");
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
			
			startActivity(emailIntent);
			break;
			
		case R.id.email_inbox:
			
			Intent inbx = new Intent("com.example.TextMailnChat.READMSG");
			startActivityForResult(inbx, 3);
			
			break;
			
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RESULT_SPEECH: 
			if (resultCode == RESULT_OK && null != data) {

				ArrayList<String> text = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				String readstr = etbody.getText().toString();
				if(readstr.equals(""))
					etbody.setText(text.get(0));
				else
					etbody.setText(readstr+" "+text.get(0));
			}
			break;
			
		case 3:
			if (resultCode == RESULT_OK && null != data)
			{
				String result = data.getExtras().getString("returnKey");
				if(result.compareTo("")!=0)
				{
					String readstr = etbody.getText().toString();
					if(readstr.equals(""))
						etbody.setText(result);
					else
						etbody.setText(readstr+" "+result);
				}
					
			}
			
			break;

		}
	}
	
}
