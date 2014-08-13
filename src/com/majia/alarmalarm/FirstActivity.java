package com.majia.alarmalarm;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class FirstActivity extends FragmentActivity 
implements CompoundButton.OnCheckedChangeListener, View.OnTouchListener, EditDialogListener,
OnSharedPreferenceChangeListener{
	private TextView TV_earliest;
	private TextView TV_interval;
	private TextView TV_must;
	private Switch switch1;
	private HourMinuteTimePicker hourMinutePicker_earliest;
	private HourMinuteTimePicker hourMinutePicker_must;
	private NumPicker myNumPicker;
	private MySettings mySetting;
	private AlarmManager alarmMgr;
	private PendingIntent alarmIntent_early;
	private PendingIntent alarmIntent_must;
	private SharedPreferences sharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_first);
		
		Switch mSwitch = (Switch)findViewById(R.id.switch1);
		if(mSwitch != null){
			mSwitch.setOnCheckedChangeListener(this);
		}
		
		switch1 = (Switch) findViewById(R.id.switch1);
		TV_earliest = (TextView) findViewById(R.id.textView_earliest);
		TV_interval = (TextView) findViewById(R.id.textView_interval);
		TV_must = (TextView) findViewById(R.id.textView_must);
		TV_earliest.setOnTouchListener(this);
		TV_interval.setOnTouchListener(this);
		TV_must.setOnTouchListener(this);
		
		mySetting = new MySettings(this);
		//routine for loading the settings
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		sharedPreferences.registerOnSharedPreferenceChangeListener(this);
		
		hourMinutePicker_earliest = new HourMinuteTimePicker();	
		hourMinutePicker_must = new HourMinuteTimePicker();
		
		boolean switch_state = sharedPreferences.getBoolean("Switch_State", false);
/*		 
		//routine for setting up switch and alarm
		if(switch_state){			
			//set the alarm
			setAlarm()
		}else{
			// If the alarm has been set, cancel it.   Don's know if this works???
			cancelAlarm();
		}
*/
		switch1.setChecked(switch_state);
		
		String Earliest_Alarm = sharedPreferences.getString("Earliest_Alarm", "0:00");
		//make "am/pm" half as smaller
		Spannable span_E = new SpannableString(Earliest_Alarm);
		span_E.setSpan(new RelativeSizeSpan(0.5f), Earliest_Alarm.length()-3, 
			Earliest_Alarm.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		TV_earliest.setText(span_E);
				
		boolean toggle_state = sharedPreferences.getBoolean("Toggle_State", false);
		if(toggle_state){
			String nap_interval = sharedPreferences.getString("nap_interval", "0");
			TV_interval.setText("Nap Interval : " + nap_interval + " min");
		}else{
			TV_interval.setText("nap interval disabled");
		}
		String Must_Wakeup_Alarm = sharedPreferences.getString("Must_Wakeup_Alarm", "0:00");
		Spannable span_M = new SpannableString(Must_Wakeup_Alarm);
		span_M.setSpan(new RelativeSizeSpan(0.5f), Must_Wakeup_Alarm.length()-3, 
				Must_Wakeup_Alarm.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		TV_must.setText(span_M);	
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		
		case MotionEvent.ACTION_MOVE:	//get touch x y coordinates
			//x = (int) event.getX();
			//y = (int) event.getY();
			
		case MotionEvent.ACTION_DOWN:
			if(v.equals(TV_earliest)){
				//Toast.makeText(this, "tv1", Toast.LENGTH_SHORT).show();
				hourMinutePicker_earliest.show(getFragmentManager(), "timePicker");
				switchState = 1;
				break;
			}
			if(v.equals(TV_interval)){
				myNumPicker = new NumPicker();
				FragmentManager fm = getSupportFragmentManager();
				myNumPicker.show(fm, "numberPicker");
				switchState = 2;
				break;
			}
			if(v.equals(TV_must)){
				hourMinutePicker_must.show(getFragmentManager(), "timePicker");
				switchState = 3;
				break;
			}
		}
		return true;
	}

	int switchState = 3;
	@Override
	public void onFinishEditDialog(String inputText) {
		switch(switchState){
		case 1:	mySetting.savePreferences("Earliest_Alarm", inputText);
				//make "am/pm" half as smaller
				Spannable span_E = new SpannableString(inputText);
				span_E.setSpan(new RelativeSizeSpan(0.5f), inputText.length()-3, 
						inputText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				TV_earliest.setText(span_E);
				//set the alarm for early alarm
				setEarlyAlarm();
				Toast.makeText(this, "setEarlyAlarm", Toast.LENGTH_SHORT).show();
				mySetting.savePreferences("Switch_State", true);
				break;
				
		case 2: mySetting.savePreferences("nap_interval", inputText);
		
				//setEarlyAlarm();
				
				boolean toggle_state = sharedPreferences.getBoolean("Toggle_State", false);
				if(toggle_state){
					TV_interval.setText("nap interval : " + inputText + " min");
				}else{
					TV_interval.setText("nap interval disabled");
				}
				break;
				
		case 3: mySetting.savePreferences("Must_Wakeup_Alarm", inputText);
				Spannable span_M = new SpannableString(inputText);
				span_M.setSpan(new RelativeSizeSpan(0.5f), inputText.length()-3, 
						inputText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				TV_must.setText(span_M);
				//set the alarm for must alarm
				Toast.makeText(this, "setMustAlarm", Toast.LENGTH_SHORT).show();
				setMustAlarm();
				mySetting.savePreferences("Switch_State", true);
				break;
		}		
	}
	
	@Override
	protected void onResume() {
        super.onResume();
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked){
			mySetting.savePreferences("Switch_State", true);
		}else{
			mySetting.savePreferences("Switch_State", false);
		}
	}

	@Override
	protected void onStart() {	 
	    super.onStart();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		
        //Toast.makeText(this, "share listener", Toast.LENGTH_SHORT).show();
        
		if(key == "Switch_State"){
        	//Toast.makeText(this, "key: Switch_State", Toast.LENGTH_SHORT).show();
        	boolean switch_state = sharedPreferences.getBoolean("Switch_State", false);
        	switch1.setChecked(switch_state);
        	if(switch_state){
        		setEarlyAlarm();
        		setMustAlarm();
        	}else{
        		cancelEarlyAlarm();
        		cancelMustAlarm();
        	}
        }
		
		if(key == "nap_interval" && sharedPreferences.getBoolean("Switch_State", false)
				&& sharedPreferences.getBoolean("Toggle_State", false)){
			setEarlyAlarm();
			Toast.makeText(this, "key: nap_interval", Toast.LENGTH_SHORT).show();
		}
		
		if(key == "Toggle_State" && sharedPreferences.getBoolean("Switch_State", false)){
			setEarlyAlarm();
			Toast.makeText(this, "key: Toggle_State", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void setEarlyAlarm(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, hourMinutePicker_earliest.hour);
		calendar.set(Calendar.MINUTE, hourMinutePicker_earliest.minutes);
		if(calendar.getTimeInMillis() < System.currentTimeMillis()){
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		Intent intent = new Intent(this, MainActivity.class);   //define alarm callback which activity
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("methodName_early","earlyAlarmDialog");
		alarmIntent_early = PendingIntent.getActivity(this, 12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		alarmMgr = (AlarmManager)this.getSystemService(Activity.ALARM_SERVICE);

		boolean toggle_state = sharedPreferences.getBoolean("Toggle_State", false);
		if(toggle_state){
			int repeatMinutes = Integer.parseInt(sharedPreferences.getString("nap_interval", "5"));
			alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
			        1000 * 60 * repeatMinutes, alarmIntent_early);
		}else{
			alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent_early);
		}
	}
	
	private void cancelEarlyAlarm(){
		Intent intent = new Intent(this, MainActivity.class);   //define alarm callback which activity
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("methodName_early","earlyAlarmDialog");
		alarmIntent_early = PendingIntent.getActivity(this, 12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		alarmMgr = (AlarmManager)this.getSystemService(Activity.ALARM_SERVICE);
		alarmMgr.cancel(alarmIntent_early);
	}
	
	private void setMustAlarm(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, hourMinutePicker_must.hour);
		calendar.set(Calendar.MINUTE, hourMinutePicker_must.minutes);
		if(calendar.getTimeInMillis() < System.currentTimeMillis()){
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		Intent intent = new Intent(this, MainActivity.class);   //define alarm callback which activity
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("methodName_must","mustAlarmDialog");
		alarmIntent_must = PendingIntent.getActivity(this, 23456, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		alarmMgr = (AlarmManager)this.getSystemService(Activity.ALARM_SERVICE);

		alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent_must);
	}
	
	private void cancelMustAlarm(){
		Intent intent = new Intent(this, MainActivity.class);   //define alarm callback which activity
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("methodName_must","mustAlarmDialog");
		alarmIntent_must = PendingIntent.getActivity(this, 23456, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		alarmMgr = (AlarmManager)this.getSystemService(Activity.ALARM_SERVICE);
		alarmMgr.cancel(alarmIntent_must);
	}
}
