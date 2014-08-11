package com.majia.alarmalarm;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends Activity implements CompoundButton.OnCheckedChangeListener,
	View.OnTouchListener, SeekBar.OnSeekBarChangeListener {
	
	private CheckBox increas_checkBox_must, vib_checkBox_must, increas_checkBox_early, vib_checkBox_early;
	private SeekBar seekBar_must, seekBar_early;
	private TextView song_selection_must, song_selection_early;	
	private ImageView about, rate;
	
	private MySettings mySetting;
	private SharedPreferences sharedPreferences;
	private AudioManager audioManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		
		mySetting = new MySettings(this);
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		audioManager = (AudioManager) this.getSystemService(this.AUDIO_SERVICE); 
		
		//initialize UI elements
		increas_checkBox_must = (CheckBox) findViewById(R.id.increas_checkBox_must);
		vib_checkBox_must = (CheckBox) findViewById(R.id.vib_checkBox_must);
		seekBar_must = (SeekBar) findViewById(R.id.seekBar_must);
		song_selection_must = (TextView) findViewById(R.id.song_selection_must);
		
		increas_checkBox_early = (CheckBox) findViewById(R.id.increas_checkBox_early);
		vib_checkBox_early = (CheckBox) findViewById(R.id.vib_checkBox_early);
		seekBar_early = (SeekBar) findViewById(R.id.seekBar_early);
		song_selection_early = (TextView) findViewById(R.id.song_selection_early);
		
		about = (ImageView) findViewById(R.id.about);
		rate = (ImageView) findViewById(R.id.rate);
		
		//set listeners for UI elements
		increas_checkBox_must.setOnCheckedChangeListener(this);
		vib_checkBox_must.setOnCheckedChangeListener(this);
		song_selection_must.setOnTouchListener(this);
		seekBar_must.setOnSeekBarChangeListener(this);
			//sets the range between 0 and the max volume  
		seekBar_must.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_RING));
		seekBar_must.setKeyProgressIncrement(1);
		
		increas_checkBox_early.setOnCheckedChangeListener(this);
		increas_checkBox_early.setOnCheckedChangeListener(this);
		song_selection_early.setOnTouchListener(this);
		seekBar_early.setOnSeekBarChangeListener(this);
			//sets the range between 0 and the max volume  
		seekBar_early.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_RING));
		seekBar_early.setKeyProgressIncrement(1);
		
		about.setOnTouchListener(this);
		rate.setOnTouchListener(this);
		
		//routine for setting up UI
		increas_checkBox_must.setChecked(sharedPreferences.getBoolean("increas_checkBox_must", false));
		vib_checkBox_must.setChecked(sharedPreferences.getBoolean("vib_checkBox_must", false));
		increas_checkBox_early.setChecked(sharedPreferences.getBoolean("increas_checkBox_early", false));
		vib_checkBox_early.setChecked(sharedPreferences.getBoolean("vib_checkBox_must", false));
		seekBar_must.setProgress(sharedPreferences.getInt("seekBar_must", 
				audioManager.getStreamMaxVolume(AudioManager.STREAM_RING))); // default to be max volume
		seekBar_early.setProgress(sharedPreferences.getInt("seekBar_early", 
				audioManager.getStreamMaxVolume(AudioManager.STREAM_RING)/2)); // default to be halt max volume
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(buttonView.equals(increas_checkBox_must)){
			mySetting.savePreferences("increas_checkBox_must", isChecked);
		}
		if(buttonView.equals(vib_checkBox_must)){
			mySetting.savePreferences("vib_checkBox_must", isChecked);
		}
		if(buttonView.equals(increas_checkBox_early)){
			mySetting.savePreferences("increas_checkBox_early", isChecked);
		}
		if(buttonView.equals(vib_checkBox_early)){
			mySetting.savePreferences("vib_checkBox_early", isChecked);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {				
			case MotionEvent.ACTION_DOWN:
				if(v.equals(song_selection_must)){
					
					break;
				}
				if(v.equals(song_selection_early)){
					
					break;
				}	
				if(v.equals(about)){
					
					break;
				}
				if(v.equals(rate)){
					
					break;
				}
		}
		return true;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if(seekBar.equals(seekBar_early) && fromUser){
			mySetting.savePreferences("seekBar_early", progress);
			Toast.makeText(this, "seekBar_early" + progress, Toast.LENGTH_SHORT).show();
		}
		if(seekBar.equals(seekBar_must) && fromUser){
			mySetting.savePreferences("seekBar_must", progress);
			Toast.makeText(this, "seekBar_must" + progress, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {	
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {	
	}
	
}
