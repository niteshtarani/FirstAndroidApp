package com.example.TextMailnChat;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.Toast;

public class Launchbyspeech extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

		try 
		{
			startActivityForResult(intent,1);
		}
		
		catch (ActivityNotFoundException a)
		{
			Toast t = Toast.makeText(getApplicationContext(),"Oops! Your device doesn't support Speech to Text",Toast.LENGTH_SHORT);
			t.show();
			finish();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode==1) { 
			if (resultCode == RESULT_OK && null != data) {

				ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				
				String msg = text.get(0);
				int m = msg.indexOf("message");
				int e = msg.indexOf("email");
				String finalmsg="";
				
				if(m!=-1)
				{
					finalmsg = msg.substring(msg.indexOf("message")+8);
					Intent opentxt=new Intent("com.example.TextMailnChat.TEXT");
					Bundle b = new Bundle();
					b.putString("txtmsg", finalmsg);
					opentxt.putExtras(b);
					finish();
					startActivity(opentxt);
				}
					
				
				else if(e!=-1)
				{
					finalmsg = msg.substring(msg.indexOf("email")+6);
					Intent openmail = new Intent("com.example.TextMailnChat.EMAIL");
					Bundle b1 = new Bundle();
					b1.putString("msg_email", finalmsg);
					openmail.putExtras(b1);
					finish();
					startActivity(openmail);
				}
					
				
				else
				{
					Toast t = Toast.makeText(getApplicationContext(),"Sorry, could not understand the command",Toast.LENGTH_LONG);
					t.show();
					finish();
				}
				
				
			}
		}
	}

}
