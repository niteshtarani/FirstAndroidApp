package com.example.TextnMail;

import java.util.ArrayList;

import com.example.TextnMail.R;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.speech.RecognizerIntent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.telephony.SmsManager;

public class Text extends Activity implements OnClickListener{

	Button text_send,text_contacts,text_clear;
	ImageButton text_voice;
	EditText txtPhoneNo,txtSMS;
	
	protected static final int RESULT_SPEECH = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text);
		
		text_send=(Button) findViewById(R.id.text_sendbtn);
		txtPhoneNo=(EditText) findViewById(R.id.editTextPhoneNo);
		txtSMS=(EditText) findViewById(R.id.editTextSMS);
		text_voice=(ImageButton) findViewById(R.id.text_voicebtn);
		text_contacts = (Button) findViewById(R.id.contacts_btn);
		text_clear = (Button) findViewById(R.id.text_clearbtn);
		
		text_send.setOnClickListener(this);
		text_voice.setOnClickListener(this);
		text_contacts.setOnClickListener(this);
		text_clear.setOnClickListener(this);
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
			
			Intent int_about = new Intent("com.example.TextnMail.ABOUT");
			startActivity(int_about);
			
			break;
		
		case R.id.email_menu:
			
			finish();
			Intent int_email = new Intent("com.example.TextnMail.EMAIL");
			Bundle b1 = new Bundle();
			b1.putString("msg_email", txtSMS.getText().toString());
			int_email.putExtras(b1);
			startActivity(int_email);
			break;
			
		case R.id.pref_menu:
			
			Intent int_pref = new Intent("com.example.TextnMail.PREFS");
			startActivity(int_pref);
			
			break;
			
		case R.id.way2sms_menu:
			
			finish();
			Intent waysms_int = new Intent("com.example.TextnMail.WAY2SMS");
			Bundle b2 = new Bundle();
			b2.putString("msg_w2s", txtSMS.getText().toString());
			waysms_int.putExtras(b2);
			startActivity(waysms_int);
			break;
			
		case R.id.inbox_menu:
			
			Intent inbx = new Intent("com.example.TextnMail.READMSG");
			startActivityForResult(inbx, 3);
			break;
			
		}
		return true;
	}

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.text_sendbtn:
			
			String phoneNo=txtPhoneNo.getText().toString();
			String SMS=txtSMS.getText().toString();
			
			sendSMS(phoneNo, SMS);
			break;
			
		case R.id.text_voicebtn:
			
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
			
		case R.id.contacts_btn:
			
			Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,Contacts.CONTENT_URI);
			startActivityForResult(contactPickerIntent, 2);
			
			break;
			
		case R.id.text_clearbtn:
			txtSMS.setText("");
			txtPhoneNo.setText("");
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
				
				txtSMS.setText(txtSMS.getText().toString()+" "+text.get(0));
				
			}
			break;
			
			case 2:
				
				String phoneNumber="";
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
					    	txtPhoneNo.setText(phoneNumber);
					    }
					
				}
			break;
			
			case 3:
				if (resultCode == RESULT_OK && null != data)
				{
					txtSMS.setText(data.getExtras().getString("returnKey"));
				}
				
				break;
		}
		
	}
	
	public void sendSMS(String phone,String message)
	{
		String SENT = "SMS_SENT";
	    String DELIVERED = "SMS_DELIVERED";

	    Intent sentIntent = new Intent(SENT);
	    Intent deliveredIntent = new Intent(DELIVERED);
	    PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,sentIntent, 0);        
	    PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,deliveredIntent, 0);
	    
	    BroadcastReceiver sentReceiver = new BroadcastReceiver()
	    {
	    	@Override public void onReceive(Context c, Intent in)
	    	{
	    		switch (getResultCode())
	    		{
                case Activity.RESULT_OK:
                    Toast.makeText(getBaseContext(), "SMS sent ",Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Toast.makeText(getBaseContext(), "Generic failure",Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    Toast.makeText(getBaseContext(), "No service",Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    Toast.makeText(getBaseContext(), "Null PDU",Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    Toast.makeText(getBaseContext(), "Radio off",Toast.LENGTH_SHORT).show();
                    break;
	    		}
	    	}
	    };
	    
	    BroadcastReceiver deliverReceiver = new BroadcastReceiver()
	    {
	    	@Override public void onReceive(Context c, Intent in)
	    	{
	    		switch (getResultCode())
	    		{
                case Activity.RESULT_OK:
                    Toast.makeText(getBaseContext(), "SMS delivered", Toast.LENGTH_SHORT).show();
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(getBaseContext(), "SMS not delivered", Toast.LENGTH_SHORT).show();
                    break;                      
	    		}  
	    	}
	    };
	    
	    SmsManager mysms=SmsManager.getDefault();
	    
	    ArrayList<String> multiSMS = mysms.divideMessage(message);
	    ArrayList<PendingIntent> sentIns = new ArrayList<PendingIntent>();
	    ArrayList<PendingIntent> deliverIns = new ArrayList<PendingIntent>();
	    
	    for(int i=0; i< multiSMS.size(); i++)
	    {
	    sentIns.add(sentPI);
	    deliverIns.add(deliveredPI);
	    }
	    
		try
		{
			registerReceiver(sentReceiver, new IntentFilter(SENT));
	        registerReceiver(deliverReceiver, new IntentFilter(DELIVERED));
			
			mysms.sendMultipartTextMessage(phone, null, multiSMS, sentIns, deliverIns);
			
		} 
		catch (Exception e)
		{				
			Toast.makeText(getApplicationContext(),	"SMS faild, please try again later!",Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		 ContentValues my_values = new ContentValues();
		  my_values.put("address", phone);
		  my_values.put("body", message);
		  getContentResolver().insert(Uri.parse("content://sms/inbox"), my_values);
	}
}
