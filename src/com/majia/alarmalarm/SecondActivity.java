package com.majia.alarmalarm;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
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
	private TextView song_selection_must, song_selection_early, vib, mw;	
	private ImageView about, rate;
	
	private MySettings mySetting;
	private SharedPreferences sharedPreferences;
	private AudioManager audioManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
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
		
		vib = (TextView) findViewById(R.id.vibration_early);
		vib.setOnTouchListener(this);
		mw = (TextView) findViewById(R.id.title_must);
		mw.setOnTouchListener(this);
		
		//set listeners for UI elements
		increas_checkBox_must.setOnCheckedChangeListener(this);
		vib_checkBox_must.setOnCheckedChangeListener(this);
		song_selection_must.setOnTouchListener(this);
		seekBar_must.setOnSeekBarChangeListener(this);
			//sets the range between 0 and the max volume  
		seekBar_must.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		seekBar_must.setKeyProgressIncrement(1);
		
		increas_checkBox_early.setOnCheckedChangeListener(this);
		vib_checkBox_early.setOnCheckedChangeListener(this);
		song_selection_early.setOnTouchListener(this);
		seekBar_early.setOnSeekBarChangeListener(this);
			//sets the range between 0 and the max volume  
		seekBar_early.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		seekBar_early.setKeyProgressIncrement(1);
		
		about.setOnTouchListener(this);
		rate.setOnTouchListener(this);
		
		//routine for setting up UI
		boolean local_early = sharedPreferences.getBoolean("local_boolean_early", false);
    	if(local_early){
    		String n = sharedPreferences.getString("local_name_early", "error");
    		song_selection_early.setText(n);
    	}else{
    		String selectedEarlySong = sharedPreferences.getString("selected_early_song", "sound selection");
    		song_selection_early.setText(selectedEarlySong);
    	}
    	
    	boolean local_must = sharedPreferences.getBoolean("local_boolean_must", false);
    	if(local_must){
    		String n = sharedPreferences.getString("local_name_must", "error");
    		song_selection_must.setText(n);
    	}else{
    		String selectedMustSong = sharedPreferences.getString("selected_must_song", "sound selection");
    		song_selection_must.setText(selectedMustSong);
    	}
    	
		increas_checkBox_must.setChecked(sharedPreferences.getBoolean("increas_checkBox_must", false));
		vib_checkBox_must.setChecked(sharedPreferences.getBoolean("vib_checkBox_must", false));
		increas_checkBox_early.setChecked(sharedPreferences.getBoolean("increas_checkBox_early", false));
		vib_checkBox_early.setChecked(sharedPreferences.getBoolean("vib_checkBox_early", false));
		seekBar_must.setProgress(sharedPreferences.getInt("seekBar_must", 
				audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC))); // default to be max volume
		seekBar_early.setProgress(sharedPreferences.getInt("seekBar_early", 
				audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC))); // default to be max volume
	}
	
	@Override
	protected void onResume() {
        super.onResume();
        boolean local_early = sharedPreferences.getBoolean("local_boolean_early", false);
    	if(local_early){
    		String n2 = sharedPreferences.getString("local_name_early", "error");
    		song_selection_early.setText(n2);
    	}else{
    		String selectedEarlySong_2 = sharedPreferences.getString("selected_early_song", "sound selection");
    		song_selection_early.setText(selectedEarlySong_2);
    	}
		
    	boolean local_must = sharedPreferences.getBoolean("local_boolean_must", false);
    	if(local_must){
    		String nn = sharedPreferences.getString("local_name_must", "error");
    		song_selection_must.setText(nn);
    	}else{
    		String selectedMustSong_2 = sharedPreferences.getString("selected_must_song", "sound selection");
    		song_selection_must.setText(selectedMustSong_2);
    	}
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
		case MotionEvent.ACTION_UP:
			if(v.equals(about)){
				Intent aboutIntent = new Intent(this, AboutActivity.class);
				startActivity(aboutIntent);
				break;
			}
			if(v.equals(rate)){
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, 
						Uri.parse("https://play.google.com/store/apps/details?id=com.catgarden.android&hl=en"));
				startActivity(browserIntent);
				break;
			}
			if(v.equals(song_selection_must)){
				Intent MustSongIntent = new Intent(this, MustSongActivity.class);
				startActivity(MustSongIntent);
				break;
			}
			if(v.equals(song_selection_early)){
				Intent earlySongIntent = new Intent(this, EarlySongActivity.class);
				startActivity(earlySongIntent);
				break;
			}					
		}
		return true;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if(seekBar.equals(seekBar_early) && fromUser){
			mySetting.savePreferences("seekBar_early", progress);
		}
		if(seekBar.equals(seekBar_must) && fromUser){
			mySetting.savePreferences("seekBar_must", progress);
			//Toast.makeText(this, "seekBar_must" + progress, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {	
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {	
	}
	
}
