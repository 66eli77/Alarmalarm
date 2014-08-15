package com.majia.alarmalarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;

public class SingletonMediaPlayer {
	private MediaPlayer mMediaPlayer;
	private AudioManager audioManager;
	private SharedPreferences sharedPreferences;
	
	private static SingletonMediaPlayer instance = new SingletonMediaPlayer();
	
	private SingletonMediaPlayer(){
		mMediaPlayer = new MediaPlayer();
	}
	
	public static SingletonMediaPlayer getInstance(){
		return instance;
	}
	
	public boolean isPlaying(){
		if(mMediaPlayer.isPlaying()){
			return true;
		}else{
			return false;
		}
	}
	
	public void reset(){
		mMediaPlayer.reset();
	}
	
	public void stop(){
		mMediaPlayer.stop();
	}
	
	public void play_setDataSource(String filename){
		try{
			mMediaPlayer.reset();
			mMediaPlayer.setDataSource(filename);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
		} catch (Exception e) { }
	}
	
	public void play_inAdapterList(Context context, int song){
		mMediaPlayer = MediaPlayer.create(context.getApplicationContext(), song);
		mMediaPlayer.start();
	}
	
	public void playSound_early(Context context) {      
		audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		
		if(sharedPreferences.getBoolean("vib_checkBox_early", false)){
        	audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        }else{
        	audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
        
        if(!sharedPreferences.getBoolean("increas_checkBox_early", false)){
        	audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 
        			sharedPreferences.getInt("seekBar_early", 4), 0);
        }
        if (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) != 0) {
        	
        	boolean local_early = sharedPreferences.getBoolean("local_boolean_early", false);
        	if(local_early){
        		String filename_early = sharedPreferences.getString("local_data_early", "opps");
        		try{
        			mMediaPlayer.reset();
        			mMediaPlayer.setDataSource(filename_early);
        			mMediaPlayer.prepare();
        			mMediaPlayer.setLooping(true);
        			mMediaPlayer.start();
        		} catch (Exception e) { }
        	}else{
        		int songEarly = sharedPreferences.getInt("selected_early_song_id", R.raw.satie_psychotropic_circle);
        		mMediaPlayer = MediaPlayer.create(context.getApplicationContext(), songEarly);
        		mMediaPlayer.setLooping(true);
        		mMediaPlayer.start();
        	}
        	
          //stop playing after certain amount of time   
	 		long alarmEndTime = 1000*60*1;   //end after 1 min
	 		new CountDownTimer(alarmEndTime, 1000) {
	 			int i = 0;
	 		     public void onTick(long millisUntilFinished) {
	 		    	 // do something on interval
	 		    	 //do nothing
	 		    	 if(sharedPreferences.getBoolean("increas_checkBox_early", false)){
	 		    		 if(i < sharedPreferences.getInt("seekBar_early", 4)){
	 		    			 audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i++, 0);
	 		    		 }
	 		    	 }
	 		     }

	 		     public void onFinish() {
	 		    	mMediaPlayer.stop();
	 		     }
	 		}.start();	
        }
		
	}
	
	public void playSound_must(Context context){
		audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		
		if(sharedPreferences.getBoolean("vib_checkBox_must", false)){
        	audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        }else{
        	audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
        
        if(!sharedPreferences.getBoolean("increas_checkBox_must", false)){
        	audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 
        			sharedPreferences.getInt("seekBar_must", 7), 0);
        }
        if (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) != 0) {
        	
        	boolean local_early = sharedPreferences.getBoolean("local_boolean_must", false);
        	if(local_early){
        		String filename_early = sharedPreferences.getString("local_data_must", "opps");
        		try{
        			mMediaPlayer.reset();
        			mMediaPlayer.setDataSource(filename_early);
        			mMediaPlayer.prepare();
        			mMediaPlayer.setLooping(true);
        			mMediaPlayer.start();
        		} catch (Exception e) { }
        	}else{
        		int songMust = sharedPreferences.getInt("selected_must_song_id", R.raw.chariots_of_fire);
            	mMediaPlayer = MediaPlayer.create(context.getApplicationContext(), songMust);
            	mMediaPlayer.setLooping(true);
            	mMediaPlayer.start();
        	}
                                         
          //stop playing after certain amount of time   
	 		long alarmEndTime = 1000*60*3;   //end after 3 min
	 		new CountDownTimer(alarmEndTime, 1000) {
	 			int i = 0;
	 		     public void onTick(long millisUntilFinished) {
	 		    	 // do something on interval
	 		    	 //do nothing
	 		    	 if(sharedPreferences.getBoolean("increas_checkBox_must", false)){
	 		    		 if(i < sharedPreferences.getInt("seekBar_must", 7)){
	 		    			 audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i++, 0);
	 		    		 }
	 		    	 }
	 		     }

	 		     public void onFinish() {
	 		    	mMediaPlayer.stop();
	 		     }
	 		}.start();	
        }
        
	}
}
