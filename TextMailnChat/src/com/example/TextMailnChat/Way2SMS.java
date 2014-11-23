package com.example.TextMailnChat;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

import com.example.TextMailnChat.R;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

class MessageSender extends AsyncTask<String, Void, String>
{
	private String username;
	private String password;
	private String to;
	private String msg;
	private ContentResolver cr;
	private ProgressDialog dialog;
	private Context context;
	private boolean logsent;
	private boolean bgsend;
	
	public MessageSender(String a, String b, String c, String d, ContentResolver e, ProgressDialog f, Context g)
	{
		username = a;
		password = b;
		to = c;
		msg = d;
		cr = e;
		dialog = f;
		context = g;
		SharedPreferences settings = context.getSharedPreferences("Way2Droid", Context.MODE_PRIVATE);
		logsent = settings.getBoolean("logsent", true);
		bgsend = settings.getBoolean("bgsend", false);
	}
	
	@Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(!bgsend)
        	dialog.dismiss();
		show("SMS sent");
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(bgsend)
        {
        	show("Sending message!");
        }
        else
        {
	        dialog.setMessage("Sending SMS. Please wait!");
			dialog.setCancelable(false);
			dialog.setIndeterminate(true);
			dialog.setTitle("");
			dialog.show();
        }
    }
	
	private void show(String s)
    {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

	@Override
	protected String doInBackground(String... params) {
		try
		{
			URL url = new URL("http://www.foamsnet.com/smsapi/send.php?username=" + username + "&password=" + password + "&to=" + to + "&msg=" + URLEncoder.encode(msg,"UTF-8"));
	        URLConnection urlc = url.openConnection();
	        BufferedReader sin = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
	        String inputLine = sin.readLine();
	        inputLine = inputLine == null?"null":inputLine; 
	        sin.close();
	        if(logsent)
	        {
	        	ContentResolver contentResolver = cr;
		        ContentValues values = new ContentValues();
		        values.put("address", "+91" + inputLine.split(" ")[3]);
		        values.put("body", msg);
		        contentResolver.insert(Uri.parse("content://sms/sent"), values);
	        }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
}

public class Way2SMS extends Activity implements OnClickListener {
	
    /** Called when the activity is first created. */
	Button text_sendw2s,text_contactsw2s,text_inboxw2s;
	ImageButton text_voicew2s;
	EditText txtPhoneNow2s,txtSMSw2s;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.way2sms);
        
        text_sendw2s=(Button) findViewById(R.id.text_sendbtnw2s);
		txtPhoneNow2s=(EditText) findViewById(R.id.editTextPhoneNow2s);
		txtSMSw2s=(EditText) findViewById(R.id.editTextSMSw2s);
		text_voicew2s=(ImageButton) findViewById(R.id.text_voicebtnw2s);
		text_contactsw2s = (Button) findViewById(R.id.contacts_btnw2s);
		text_inboxw2s = (Button) findViewById(R.id.text_inboxbtnw2s);
		
		text_sendw2s.setOnClickListener(this);
		text_voicew2s.setOnClickListener(this);
		text_contactsw2s.setOnClickListener(this);
		text_inboxw2s.setOnClickListener(this);
		
		Bundle recv2 = getIntent().getExtras();
		txtSMSw2s.setText(recv2.getString("msg_w2s"));
    }
    
    
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected())
            return true;
        return false;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
    	switch(item.getItemId())
		{
		case R.id.about_menu:
			
			Intent intnt_about = new Intent("com.example.TextMailnChat.ABOUT");
			startActivity(intnt_about);
			
			break;
			
		case R.id.Text_menu:
			
			finish();
			Intent intnt_text = new Intent("com.example.TextMailnChat.TEXT");
			Bundle bt = new Bundle();
			bt.putString("txtmsg", txtSMSw2s.getText().toString());
			intnt_text.putExtras(bt);
			startActivity(intnt_text);
			break;
			
		case R.id.email_menu:
			
			finish();
			Intent intnt_email = new Intent("com.example.TextMailnChat.EMAIL");
			Bundle b4 = new Bundle();
			b4.putString("msg_email", txtSMSw2s.getText().toString());
			intnt_email.putExtras(b4);
			startActivity(intnt_email);
			break;
			
		case R.id.pref_menu:
			
			Intent intnt_pref = new Intent("com.example.TextMailnChat.PREFS");
			startActivity(intnt_pref);
			
			break;
			
		case R.id.settings_menu:
			
			Intent intents = new Intent(Way2SMS.this, Settings.class);
	    	startActivity(intents);
	    	
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
    
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
    	super.onActivityResult(reqCode, resultCode, data);
    	String phoneNumber = "";
    	switch (reqCode) {
    		case 0 :
  
				if (resultCode == RESULT_OK && null != data)
				{
					 Uri uri = data.getData();
					 Cursor cursor=getContentResolver().query(uri, null, ContactsContract.Contacts.HAS_PHONE_NUMBER + " = 1", null, null);

					    while (cursor.moveToNext())
					    { 
					    	String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)); 
					   
					    	Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null); 
					     
					    	while (phones.moveToNext())
					    	{ 
					    		phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));                 
					    	} 
					    	phones.close();
					    	phoneNumber = phoneNumber.replaceAll("-", "");
					    	phoneNumber = phoneNumber.replaceAll(" ", "");
					    	txtPhoneNow2s.setText(phoneNumber);
					    }
					
				}
    			   		break;
    		case 2: 
    			if (resultCode == RESULT_OK && null != data) {

    				ArrayList<String> text = data
    						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
    				
    				String readstr = txtSMSw2s.getText().toString();
    				if(readstr.equals(""))
    					txtSMSw2s.setText(text.get(0));
    				else
    					txtSMSw2s.setText(readstr+" "+text.get(0));
    				
    			}
    			break;
    			
    		case 3:
				if (resultCode == RESULT_OK && null != data)
				{
					String result = data.getExtras().getString("returnKey");
					if(result.compareTo("")!=0)
					{
						String readstr = txtSMSw2s.getText().toString();
						if(readstr.equals(""))
							txtSMSw2s.setText(result);
						else
							txtSMSw2s.setText(readstr+" "+result);
					}
						
				}
				
				break;
    			
    	  }
    }
    
    private void show(String s)
    {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
    
    
	//@Override
	public void onClick(View v) {
		SharedPreferences settings = getApplicationContext().getSharedPreferences("Way2Droid", Context.MODE_PRIVATE);
		
		switch(v.getId())
		{
		case R.id.contacts_btnw2s:
	
				Intent contactintent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(contactintent, 0);
				
			break;
		
		case R.id.text_sendbtnw2s:

			if(!isOnline())
			{
				show("You cannot send messages without Internet access! Please enable mobile data or wifi and try again!");
				return;
			}
			//SharedPreferences settings = getApplicationContext().getSharedPreferences("Way2Droid", Context.MODE_PRIVATE);
			String username = settings.getString("username", "-1");
			String password = settings.getString("password", "-1");
			if(username.equalsIgnoreCase("-1") || password.equalsIgnoreCase("-1"))
			{
				show("Please provide username and password in settings before sending messages!");
				return;
			}
			String dest = ((EditText) findViewById(R.id.editTextPhoneNow2s)).getText().toString().trim();
			String message = ((EditText) findViewById(R.id.editTextSMSw2s)).getText().toString().trim();
			if(dest.equalsIgnoreCase("") || message.equalsIgnoreCase(""))
			{
				show("Please enter valid to address and message!");
				return;
			}
			if(dest.indexOf(":") > -1)
				dest = dest.split(":")[1];
			dest = settings.getString("d" + dest, dest);
			try
			{
				ProgressDialog dialog = new ProgressDialog(this);
				MessageSender m = new MessageSender(username, password, dest, message, getContentResolver(), dialog, getApplicationContext());
				m.execute((String[])null);
				ContentValues my_values = new ContentValues();
				my_values.put("address", dest);
				my_values.put("body", message);
				getContentResolver().insert(Uri.parse("content://sms/inbox"), my_values);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			break;
			
		case R.id.text_inboxbtnw2s:
			
			Intent inbx = new Intent("com.example.TextMailnChat.READMSG");
			startActivityForResult(inbx, 3);
			
			break;
			
			case R.id.text_voicebtnw2s:
			
			Intent intent = new Intent(
					RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

			try 
			{
				startActivityForResult(intent, 2);
			}
			
			catch (ActivityNotFoundException a)
			{
				Toast t = Toast.makeText(getApplicationContext(),
						"Ops! Your device doesn't support Speech to Text",
						Toast.LENGTH_SHORT);
				t.show();
			}
			
			break;
		}
		
	}
}