package com.majia.alarmalarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

public class MainActivity extends TabActivity {
	private TabHost myTabHost;
	private MySettings mySetting;
	private SharedPreferences sharedPreferences;
	private SingletonMediaPlayer player;
	
	private PowerManager pm;
	private KeyguardManager km;
	private KeyguardLock kl;
	private WakeLock wl;
	
	private AlertDialog alertDialog_early;
	private AlertDialog alertDialog_must;
	
	private Vibrator vib;
	
	@Override
	protected void onResume() {
		super.onResume();
		if(wl.isHeld())
			wl.release(); 
		kl.reenableKeyguard();
	}
	
	@Override
	public void onPause(){		
		super.onPause();

		if(wl.isHeld())
			wl.release();    // I don't need to release the wakeLock, why??
		kl.reenableKeyguard();
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		player = SingletonMediaPlayer.getInstance();
		vib = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		
		/*
		 * unlock the screen when wakeup alarm raises	
		*/
		pm = (PowerManager) getSystemService(this.POWER_SERVICE);  
        km = (KeyguardManager) getSystemService(this.KEYGUARD_SERVICE);  
        kl = km.newKeyguardLock("INFO");  
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK  
               | PowerManager.ACQUIRE_CAUSES_WAKEUP  
                | PowerManager.ON_AFTER_RELEASE, "INFO"); 
        long t = 1000*59;
        wl.acquire(t); // wake up the screen  
        kl.disableKeyguard();// dismiss the keyguard
        
        this.getWindow().setFlags(  
               // WindowManager.LayoutParams.FLAG_FULLSCREEN  
                         WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD  
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED  
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,  
            //  WindowManager.LayoutParams.FLAG_FULLSCREEN  
                         WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD  
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED  
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON  
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);  
        
		 
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
        
		if(getIntent() != null && getIntent().getExtras() != null && (flags & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) == 0 ){
			if(getIntent().getStringExtra("methodName_early") != null && getIntent().getStringExtra("methodName_early").equals("earlyAlarmDialog")){
				alertDialog_early = new AlertDialog.Builder(this)
					.setTitle("Wake Up")
						.setMessage("'done' will cancel the early alarm")
				 		    .setPositiveButton("done", new DialogInterface.OnClickListener() {
				 		        public void onClick(DialogInterface dialog, int which) { 
				 		        	if(player.notNull() && player.isPlaying()){
				 		        		player.stop();
				 		        		player.release();
				 		        		vib.cancel();
				 		        	}
				 		        	
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
				 		        	if(player.notNull() && player.isPlaying()){
				 		        		player.stop();
				 		        		player.release();
				 		        		vib.cancel();
				 		        		if(!sharedPreferences.getBoolean("Toggle_State", false))
				 		        			mySetting.savePreferences("Switch_State", false);
				 		        	}
				 		        }
				 		     })
				 		    .setIcon(android.R.drawable.ic_dialog_alert)
				 		    .setCancelable(false)
				 		    .show();
				//Toast.makeText(this, "eli in", Toast.LENGTH_SHORT).show();
				player.playSound_early(this, alertDialog_early, vib);				 		   	 		  
			}
			if(getIntent().getStringExtra("methodName_must") != null && getIntent().getStringExtra("methodName_must").equals("mustAlarmDialog")){
				//stop early alarm first
				if(player.notNull() && player.isPlaying()){
					player.stop();
					player.release();
					player.cancelCountDownTimer();
					vib.cancel();
				}
				if(alertDialog_early != null && alertDialog_early.isShowing())
					alertDialog_early.dismiss();
					
				AlarmManager alarmMgr;			 		    
		        alarmMgr = (AlarmManager)MainActivity.this.getSystemService(Activity.ALARM_SERVICE);
		        PendingIntent alarmIntent;
		       	Intent intent = new Intent(MainActivity.this, MainActivity.class);   //define alarm callback which activity
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("methodName_early","earlyAlarmDialog");
				alarmIntent = PendingIntent.getActivity(MainActivity.this, 12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
				alarmMgr.cancel(alarmIntent);
				
				alertDialog_must = new AlertDialog.Builder(this)
					.setTitle("Wake Up")
						.setMessage("Must wake up now !")
							.setPositiveButton("got it", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) { 
									if(player.notNull() && player.isPlaying()){
										player.stop();
										player.release();
										vib.cancel();
									}
										
									AlarmManager alarmMgr;			 		    
									alarmMgr = (AlarmManager)MainActivity.this.getSystemService(Activity.ALARM_SERVICE);
									PendingIntent alarmIntent;
									Intent intent = new Intent(MainActivity.this, MainActivity.class);   //define alarm callback which activity
									intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									intent.putExtra("methodName_must","mustAlarmDialog");
									alarmIntent = PendingIntent.getActivity(MainActivity.this, 23456, intent, PendingIntent.FLAG_CANCEL_CURRENT);
									alarmMgr.cancel(alarmIntent);
									mySetting.savePreferences("Switch_State", false);
								}
							})
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setCancelable(false)
							.show();
				
				player.playSound_must(this, alertDialog_must, vib);
			}
		}
	}
}
