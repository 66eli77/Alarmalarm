package com.majia.alarmalarm;

import java.io.IOException;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

public class MainActivity extends TabActivity {
	private TabHost myTabHost;
	private MediaPlayer mMediaPlayer = new MediaPlayer();
	private MySettings mySetting;
	private SharedPreferences sharedPreferences;
	AudioManager audioManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		myTabHost = (TabHost)findViewById(android.R.id.tabhost);
		//myTabHost.getTabWidget().setDividerDrawable(R.drawable.divider); // delete divider by using transparent png 

		TabSpec spec = myTabHost.newTabSpec("Tab1"); // tabhost object	
		spec.setIndicator("", getResources().getDrawable(R.drawable.tab1_selector)); //set title
		spec.setContent(new Intent(this, FirstActivity.class));
		myTabHost.addTab(spec);
		
		TabSpec spec2 = myTabHost.newTabSpec("Tab2"); // tabhost object
		spec2.setIndicator("", getResources().getDrawable(R.drawable.tab2_selector)); //set title
		spec2.setContent(new Intent(this, SecondActivity.class));
		myTabHost.addTab(spec2);
		
		int flags = getIntent().getFlags();  
		mySetting = new MySettings(this);
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		
		if(getIntent() != null && getIntent().getExtras() != null && (flags & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) == 0 ){
			if(getIntent().getStringExtra("methodName_early") != null && getIntent().getStringExtra("methodName_early").equals("earlyAlarmDialog")){
				new AlertDialog.Builder(this)
					.setTitle("Wake Up")
						.setMessage("for now, both buttons will stop the alarm")
				 		    .setPositiveButton("stop", new DialogInterface.OnClickListener() {
				 		        public void onClick(DialogInterface dialog, int which) { 
				 		        	mMediaPlayer.stop();
				 		        	
				 		        	//cancel early alarm
				 		        	AlarmManager alarmMgr;			 		    
				 		        	alarmMgr = (AlarmManager)MainActivity.this.getSystemService(Activity.ALARM_SERVICE);
				 		        	PendingIntent alarmIntent;
				 		        	Intent intent = new Intent(MainActivity.this, MainActivity.class);   //define alarm callback which activity
				 					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				 					intent.putExtra("methodName_early","earlyAlarmDialog");
				 					alarmIntent = PendingIntent.getActivity(MainActivity.this, 12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
				 					alarmMgr.cancel(alarmIntent);
				 					//mySetting.savePreferences("Switch_State", false);
				 					//Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_SHORT).show();
				 					
				 					//cancel must alarm
									PendingIntent alarmIntent_must;
									Intent intent_must = new Intent(MainActivity.this, MainActivity.class);   //define alarm callback which activity
									intent_must.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									intent_must.putExtra("methodName_must","mustAlarmDialog");
									alarmIntent_must = PendingIntent.getActivity(MainActivity.this, 23456, intent_must, PendingIntent.FLAG_CANCEL_CURRENT);
									alarmMgr.cancel(alarmIntent_must);
									
									mySetting.savePreferences("Switch_State", false);
				 		        }
				 		     })
				 		    .setNegativeButton("nap", new DialogInterface.OnClickListener() {
				 		        public void onClick(DialogInterface dialog, int which) { 
				 		        	mMediaPlayer.stop(); 
				 		        	//Toast.makeText(MainActivity.this, "eli", Toast.LENGTH_SHORT).show();
				 		        	//mySetting.savePreferences("Switch_State", true);
				 		        }
				 		     })
				 		    .setIcon(android.R.drawable.ic_dialog_alert)
				 		    .setCancelable(false)
				 		     .show();
				playSound_early(this, getAlarmUri());				 		   	 		  
			}
			if(getIntent().getStringExtra("methodName_must") != null && getIntent().getStringExtra("methodName_must").equals("mustAlarmDialog")){
				//stop early alarm first
				mMediaPlayer.stop();
				
				AlarmManager alarmMgr;			 		    
		        alarmMgr = (AlarmManager)MainActivity.this.getSystemService(Activity.ALARM_SERVICE);
		        PendingIntent alarmIntent;
		       	Intent intent = new Intent(MainActivity.this, MainActivity.class);   //define alarm callback which activity
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("methodName_early","earlyAlarmDialog");
				alarmIntent = PendingIntent.getActivity(MainActivity.this, 12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
				alarmMgr.cancel(alarmIntent);
				
				new AlertDialog.Builder(this)
					.setTitle("Wake Up")
						.setMessage("for now, both buttons will stop the alarm")
							.setPositiveButton("must wakeup", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) { 
									mMediaPlayer.stop();
									AlarmManager alarmMgr;			 		    
									alarmMgr = (AlarmManager)MainActivity.this.getSystemService(Activity.ALARM_SERVICE);
									PendingIntent alarmIntent;
									Intent intent = new Intent(MainActivity.this, MainActivity.class);   //define alarm callback which activity
									intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									intent.putExtra("methodName_must","mustAlarmDialog");
									alarmIntent = PendingIntent.getActivity(MainActivity.this, 23456, intent, PendingIntent.FLAG_CANCEL_CURRENT);
									alarmMgr.cancel(alarmIntent);
									mySetting.savePreferences("Switch_State", false);
									//Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_SHORT).show();
								}
							})
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setCancelable(false)
							.show();
				playSound_must(this, getAlarmUri_2());
			}
		}
	}

	private void playSound_must(Context context, Uri alert) {
        try {
            mMediaPlayer.setDataSource(context, alert);
           // final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            
            if(sharedPreferences.getBoolean("vib_checkBox_must", false)){
            	audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            }else{
            	audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            }
            
            if(!sharedPreferences.getBoolean("increas_checkBox_must", false)){
            	audioManager.setStreamVolume(AudioManager.STREAM_ALARM, sharedPreferences.getInt("seekBar_must", 7), 
            			AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
            }
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
                
              //stop playing after certain amount of time   
    	 		long alarmEndTime = 1000*10;   //end after 5 seconds
    	 		new CountDownTimer(alarmEndTime, 1000) {
    	 			int i = 0;
    	 		     public void onTick(long millisUntilFinished) {
    	 		    	 // do something on interval
    	 		    	 //do nothing
    	 		    	 if(sharedPreferences.getBoolean("increas_checkBox_must", false)){
    	 		    		 if(i < sharedPreferences.getInt("seekBar_must", 7)){
    	 		    			 audioManager.setStreamVolume(AudioManager.STREAM_ALARM, i++, 
    	 		    					 AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
    	 		    		 }
    	 		    	 }
    	 		     }

    	 		     public void onFinish() {
    	 		    	mMediaPlayer.stop();
    	 		     }
    	 		}.start();	
            }
        } catch (IOException e) {
            System.out.println("OOPS");
        }
    }
	
	private void playSound_early(Context context, Uri alert) {
        try {
            mMediaPlayer.setDataSource(context, alert);
           // final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            
            if(sharedPreferences.getBoolean("vib_checkBox_early", false)){
            	audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            }else{
            	audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            }
            
            if(!sharedPreferences.getBoolean("increas_checkBox_early", false)){
            	audioManager.setStreamVolume(AudioManager.STREAM_ALARM, sharedPreferences.getInt("seekBar_early", 4), 
            			AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
            }
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
                
              //stop playing after certain amount of time   
    	 		long alarmEndTime = 1000*10;   //end after 5 seconds
    	 		new CountDownTimer(alarmEndTime, 1000) {
    	 			int i = 0;
    	 		     public void onTick(long millisUntilFinished) {
    	 		    	 // do something on interval
    	 		    	 //do nothing
    	 		    	 if(sharedPreferences.getBoolean("increas_checkBox_early", false)){
    	 		    		 if(i < sharedPreferences.getInt("seekBar_early", 4)){
    	 		    			 audioManager.setStreamVolume(AudioManager.STREAM_ALARM, i++, 
    	 		    					 AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
    	 		    		 }
    	 		    	 }
    	 		     }

    	 		     public void onFinish() {
    	 		    	mMediaPlayer.stop();
    	 		     }
    	 		}.start();	
            }
        } catch (IOException e) {
            System.out.println("OOPS");
        }
    }
 
    //Get an alarm sound. Try for an alarm. If none set, try notification, 
    //Otherwise, ringtone.
    private Uri getAlarmUri() {
        Uri alert = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert == null) {
            alert = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alert == null) {
                alert = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return alert;
    }
    
    private Uri getAlarmUri_2() {
        Uri alert = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        if (alert == null) {
            alert = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alert == null) {
                alert = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_ALARM);
            }
        }
        return alert;
    }

}
