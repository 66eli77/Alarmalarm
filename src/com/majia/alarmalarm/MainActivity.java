package com.majia.alarmalarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

public class MainActivity extends TabActivity {
	private TabHost myTabHost;
	private MySettings mySetting;
	private SharedPreferences sharedPreferences;
	private SingletonMediaPlayer player = SingletonMediaPlayer.getInstance();
	
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
		
		if(getIntent() != null && getIntent().getExtras() != null && (flags & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) == 0 ){
			if(getIntent().getStringExtra("methodName_early") != null && getIntent().getStringExtra("methodName_early").equals("earlyAlarmDialog")){
				new AlertDialog.Builder(this)
					.setTitle("Wake Up")
						.setMessage("for now, both buttons will stop the alarm")
				 		    .setPositiveButton("stop", new DialogInterface.OnClickListener() {
				 		        public void onClick(DialogInterface dialog, int which) { 
				 		        	if(player.isPlaying())
				 		        		player.stop();
				 		        	
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
				 		        	if(player.isPlaying())
				 		        		player.stop();
				 		        }
				 		     })
				 		    .setIcon(android.R.drawable.ic_dialog_alert)
				 		    .setCancelable(false)
				 		     .show();
				player.playSound_early(this);				 		   	 		  
			}
			if(getIntent().getStringExtra("methodName_must") != null && getIntent().getStringExtra("methodName_must").equals("mustAlarmDialog")){
				//stop early alarm first
				if(player.isPlaying())
					player.stop();
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
									//SingletonMediaPlayer player = SingletonMediaPlayer.getInstance();
									if(player.isPlaying())
										player.stop();
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
				
				player.playSound_must(this);
			}
		}
	}
}
