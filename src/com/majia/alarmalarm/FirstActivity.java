package com.majia.alarmalarm;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.app.FragmentManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class FirstActivity extends FragmentActivity 
implements CompoundButton.OnCheckedChangeListener, View.OnTouchListener, EditDialogListener,
OnSharedPreferenceChangeListener{
	private TextView TV_earliest;
	private TextView TV_interval;
	private ImageView interval_frame_selected;
	private ImageView interval_frame_unselected;
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
	
	private NotificationManager notificationManager;
	private Notification notification;
	private static final int NOTIFICATION_EX = 1;
	
	/**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 8;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;
    
    /**
     * A simple pager adapter that represents 5 {@link ScreenSlidePageFragment} objects, in
     * sequence.
     */
    private class SlideImageAdapter extends FragmentStatePagerAdapter {
        public SlideImageAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
        	//mySetting.savePreferences("PageNumber", position);
            return SlideImageFragment.create(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_first);
		
		switch1 = (Switch) findViewById(R.id.switch1);
		switch1.setOnCheckedChangeListener(this);
		
		TV_earliest = (TextView) findViewById(R.id.textView_earliest);
		TV_interval = (TextView) findViewById(R.id.textView_interval);
		TV_must = (TextView) findViewById(R.id.textView_must);
		TV_earliest.setOnTouchListener(this);
		TV_interval.setOnTouchListener(this);
		TV_must.setOnTouchListener(this);
		interval_frame_selected = (ImageView) findViewById(R.id.interval_frame_selected);
		interval_frame_unselected = (ImageView) findViewById(R.id.interval_frame_unselected);
		
		mySetting = new MySettings(this);
		//routine for loading the settings
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		sharedPreferences.registerOnSharedPreferenceChangeListener(this);
		
		hourMinutePicker_earliest = new HourMinuteTimePicker();	
		hourMinutePicker_must = new HourMinuteTimePicker();
		
		boolean switch_state = sharedPreferences.getBoolean("Switch_State", false);
		switch1.setChecked(switch_state);
		
		String Earliest_Alarm = sharedPreferences.getString("Earliest_Alarm", "7:00 AM");
		//make "am/pm" half as smaller
		Spannable span_E = new SpannableString(Earliest_Alarm);
		span_E.setSpan(new RelativeSizeSpan(0.5f), Earliest_Alarm.length()-3, 
			Earliest_Alarm.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		TV_earliest.setText(span_E);
				
		boolean toggle_state = sharedPreferences.getBoolean("Toggle_State", false);
		if(toggle_state){
			String nap_interval = sharedPreferences.getString("nap_interval", "0");
			TV_interval.setTextColor(Color.parseColor("#FFFFFF"));
			TV_interval.setText("Nap Interval : " + nap_interval + " min");
			interval_frame_selected.setVisibility(View.VISIBLE);
			interval_frame_unselected.setVisibility(View.INVISIBLE);
		}else{
		//	TV_interval.setTextColor(Color.parseColor("#A0A0A0"));
			TV_interval.setText("");
		//	TV_interval.setVisibility(View.INVISIBLE);
			interval_frame_selected.setVisibility(View.INVISIBLE);
			interval_frame_unselected.setVisibility(View.VISIBLE);
		}
		String Must_Wakeup_Alarm = sharedPreferences.getString("Must_Wakeup_Alarm", "7:30 AM");
		Spannable span_M = new SpannableString(Must_Wakeup_Alarm);
		span_M.setSpan(new RelativeSizeSpan(0.5f), Must_Wakeup_Alarm.length()-3, 
				Must_Wakeup_Alarm.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		TV_must.setText(span_M);	
		
		// Instantiate a ViewPager and a PagerAdapter.
		mPager = (ViewPager) findViewById(R.id.pager);
		mPagerAdapter = new SlideImageAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(sharedPreferences.getInt("PageNumber", 0));
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When changing pages, reset the action bar actions since they are dependent
                // on which page is currently active. An alternative approach is to have each
                // fragment expose actions itself (rather than the activity exposing actions),
                // but for simplicity, the activity provides the actions in this sample.
    			int n = mPager.getCurrentItem();
    			mySetting.savePreferences("PageNumber", n);
            }
        });
	}
	
	float x0;
	float y0;
	float x1;
	float y1;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			x0 = event.getX();
			y0 = event.getY();
			break;
			
		case MotionEvent.ACTION_UP:
			x1 = event.getX();
			y1 = event.getY();
			
			if(Math.abs(x0 - x1) < 20 && Math.abs(y0 - y1) < 20){
				if(v.equals(TV_earliest)){
					switchState = 1;
					hourMinutePicker_earliest.show(getFragmentManager(), "timePicker_early");
					break;
				}
				if(v.equals(TV_interval)){
					switchState = 2;
					myNumPicker = new NumPicker();
					FragmentManager fm = getFragmentManager();
					myNumPicker.show(fm, "numberPicker");
					break;
				}
				if(v.equals(TV_must)){
					switchState = 3;
					hourMinutePicker_must.show(getFragmentManager(), "timePicker_must");
					break;
				}
			}
		}
		return true;
	}

	int switchState = 4;
	@Override
	public void onFinishEditDialog(String inputText) {
		switch(switchState){
		case 1:	mySetting.savePreferences("Earliest_Alarm", inputText);
		
				//save the time settings
				mySetting.savePreferences("early_hour", hourMinutePicker_earliest.hour);
				mySetting.savePreferences("early_min", hourMinutePicker_earliest.minutes);
		
				//make "am/pm" half as smaller
				Spannable span_E = new SpannableString(inputText);
				span_E.setSpan(new RelativeSizeSpan(0.5f), inputText.length()-3, 
						inputText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				TV_earliest.setText(span_E);
				//set the alarm for early alarm
		//		setEarlyAlarm();
		//		mySetting.savePreferences("Switch_State", true);
				
		//		switch1.setChecked(true);
				break;
				
		case 2: mySetting.savePreferences("nap_interval", inputText);
						
				boolean toggle_state = sharedPreferences.getBoolean("Toggle_State", false);
				if(toggle_state){
					TV_interval.setTextColor(Color.parseColor("#FFFFFF"));
					TV_interval.setText("nap interval : " + inputText + " min");
					interval_frame_selected.setVisibility(View.VISIBLE);
					interval_frame_unselected.setVisibility(View.INVISIBLE);
				}else{
				//	TV_interval.setTextColor(Color.parseColor("#FF0000"));
					TV_interval.setText("");
				//	TV_interval.setVisibility(View.INVISIBLE);
					interval_frame_selected.setVisibility(View.INVISIBLE);
					interval_frame_unselected.setVisibility(View.VISIBLE);
				}
				break;
				
		case 3: mySetting.savePreferences("Must_Wakeup_Alarm", inputText);
		
				//save the time settings
				mySetting.savePreferences("must_hour", hourMinutePicker_must.hour);
				mySetting.savePreferences("must_min", hourMinutePicker_must.minutes);
				
				Spannable span_M = new SpannableString(inputText);
				span_M.setSpan(new RelativeSizeSpan(0.5f), inputText.length()-3, 
						inputText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				TV_must.setText(span_M);
				//set the alarm for must alarm
			//	setMustAlarm();
			//	mySetting.savePreferences("Switch_State", true);
				
			//	switch1.setChecked(true);
				break;
		}		
	}
	
	@Override
	protected void onResume() {
        super.onResume();
	}
	
	boolean tracker = true;
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked){
			CharSequence contentTitle = "Alarmalarm";
		    CharSequence contentText = "is set";
			
			notification = new Notification.Builder(this)
			.setContentTitle(contentTitle)
			.setContentText(contentText)
			.setSmallIcon(R.drawable.alarmalarm_icon)
			.build();
			
			notificationManager = (NotificationManager)getSystemService(this.NOTIFICATION_SERVICE);	
			notificationManager.notify(NOTIFICATION_EX, notification);
			
			mySetting.savePreferences("Switch_State", true);
		}else{		
			notificationManager.cancel(NOTIFICATION_EX); 
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
	        
		if(key == "Switch_State"){
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
	
		if(key == "early_hour" || key == "early_min"){
			if(sharedPreferences.getBoolean("Switch_State", false)){
				setEarlyAlarm();
			}
		}
		
		if(key == "must_hour" || key == "must_min"){
			if(sharedPreferences.getBoolean("Switch_State", false)){
				setMustAlarm();
			}
		}
		
		if(key == "nap_interval" && sharedPreferences.getBoolean("Switch_State", false)
				&& sharedPreferences.getBoolean("Toggle_State", false)){
			setEarlyAlarm();
		}
		
		if(key == "Toggle_State" && sharedPreferences.getBoolean("Switch_State", false)){
			setEarlyAlarm();
		}
	}
	
	private void setEarlyAlarm(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int e_h = sharedPreferences.getInt("early_hour", 7);
		calendar.set(Calendar.HOUR_OF_DAY, e_h);
		int e_m = sharedPreferences.getInt("early_min", 0);
		calendar.set(Calendar.MINUTE, e_m);
//		calendar.set(Calendar.SECOND, hourMinutePicker_earliest.seconds);
		if(calendar.getTimeInMillis() < System.currentTimeMillis()){
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		Intent intent1 = new Intent(this, MainActivity.class);   //define alarm callback which activity
		intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent1.putExtra("methodName_early","earlyAlarmDialog");
		alarmIntent_early = PendingIntent.getActivity(this, 12345, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
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
		Intent intent2 = new Intent(this, MainActivity.class);   //define alarm callback which activity
		intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent2.putExtra("methodName_early","earlyAlarmDialog");
		alarmIntent_early = PendingIntent.getActivity(this, 12345, intent2, PendingIntent.FLAG_CANCEL_CURRENT);
		alarmMgr = (AlarmManager)this.getSystemService(Activity.ALARM_SERVICE);
		alarmMgr.cancel(alarmIntent_early);
	}
	
	private void setMustAlarm(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int m_h = sharedPreferences.getInt("must_hour", 7);
		calendar.set(Calendar.HOUR_OF_DAY, m_h);
		int m_m = sharedPreferences.getInt("must_min", 30);
		calendar.set(Calendar.MINUTE, m_m);
//		calendar.set(Calendar.SECOND, hourMinutePicker_must.seconds);
		if(calendar.getTimeInMillis() < System.currentTimeMillis()){
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		Intent intent3 = new Intent(this, MainActivity.class);   //define alarm callback which activity
		intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent3.putExtra("methodName_must","mustAlarmDialog");
		alarmIntent_must = PendingIntent.getActivity(this, 23456, intent3, PendingIntent.FLAG_CANCEL_CURRENT);
		alarmMgr = (AlarmManager)this.getSystemService(Activity.ALARM_SERVICE);

		alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent_must);
	}
	
	private void cancelMustAlarm(){
		Intent intent4 = new Intent(this, MainActivity.class);   //define alarm callback which activity
		intent4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent4.putExtra("methodName_must","mustAlarmDialog");
		alarmIntent_must = PendingIntent.getActivity(this, 23456, intent4, PendingIntent.FLAG_CANCEL_CURRENT);
		alarmMgr = (AlarmManager)this.getSystemService(Activity.ALARM_SERVICE);
		alarmMgr.cancel(alarmIntent_must);
	}
}
