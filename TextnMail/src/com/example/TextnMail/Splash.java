package com.example.TextnMail;

import com.example.TextnMail.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;

public class Splash extends Activity{
	
	MediaPlayer mysong;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.splash);
		
		mysong=MediaPlayer.create(Splash.this, R.raw.song);
		
		SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		boolean set_music=getPrefs.getBoolean("check_sound", true);
		
		if(set_music)
		mysong.start();
		
		Thread timer = new Thread()
		{
			public void run()
			{
				try
				{
					sleep(3000);
				}
				catch(InterruptedException e)
				{
					System.out.print(e);
				}
				finally
				{
					Intent openmain=new Intent("com.example.TextnMail.TEXT");
					startActivity(openmain);
					
				}
				
			}
		};
		timer.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mysong.release();
		finish();
	}
	

}
