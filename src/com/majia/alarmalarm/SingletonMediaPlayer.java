package com.majia.alarmalarm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.widget.Toast;
import android.os.Vibrator;

public class SingletonMediaPlayer {
	private MediaPlayer mMediaPlayer;
	private AudioManager audioManager;
	private SharedPreferences sharedPreferences;
	private CountDownTimer ct;
	private AlertDialog a;
	private AlertDialog b;
	private Vibrator v;
	
	private static SingletonMediaPlayer instance = new SingletonMediaPlayer();
	
	private SingletonMediaPlayer(){
		mMediaPlayer = new MediaPlayer();
	}
	
	public static SingletonMediaPlayer getInstance(){
		return instance;
	}
	
	public boolean notNull(){
		if(mMediaPlayer == null){
			return false;
		}else{
			return true;
		}
	}
	
	public void cancelVibrate(){
		if(v != null)
			v.cancel();
	}
	
	public void cancelCountDownTimer(){
		if(ct != null){
			ct.cancel();
		}
	}
		
	public void release(){
		mMediaPlayer.reset();
		mMediaPlayer.release();
		mMediaPlayer = null;
	}
	
	public boolean isPlaying(){
		if(mMediaPlayer != null){
			if(mMediaPlayer.isPlaying()){
				return true;
			}else{
				return false;
			}
		}
		return false; 
	}
	
	public void reset(){
		mMediaPlayer.reset();
	}
	
	public void stop(){
		mMediaPlayer.stop();
	}
	
	public void play_setDataSource(String filename){
		if(mMediaPlayer == null){
			mMediaPlayer = new MediaPlayer();
		}
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
	
	public void alertDialog_early(AlertDialog alertDialog_early){
		a = alertDialog_early;
	}
	
	public void playSound_early(Context context) {     
		v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		mMediaPlayer = new MediaPlayer();
		audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		boolean vib;
		
		if(sharedPreferences.getBoolean("vib_checkBox_early", false)){
			vib = true;
        }else{
        	vib = false;
        }
		//add this to avoid jerky sound at the beginning
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 1, 0);
		
        if(!sharedPreferences.getBoolean("increas_checkBox_early", false)){
        	audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 
        			sharedPreferences.getInt("seekBar_early", 
        					audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)), 0);
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
        			if(vib)
        				v.vibrate(1000*60*1);
        		} catch (Exception e) { }
        	}else{
        		int songEarly = sharedPreferences.getInt("selected_early_song_id", R.raw.beautiful_dance_keys);
        		mMediaPlayer = MediaPlayer.create(context.getApplicationContext(), songEarly);
        		mMediaPlayer.setLooping(true);
        		mMediaPlayer.start();
        		if(vib)
        			v.vibrate(1000*60*1);

        		//final AlertDialog a = alertDialog_early;
        		//stop playing after certain amount of time   
        		long alarmEndTime = 1000*59*1;   //end after 1 min
        		ct = new CountDownTimer(alarmEndTime, 1000) {
        			int i = 0;
        			public void onTick(long millisUntilFinished) {
        				// do something on interval
        				//do nothing
        				if(sharedPreferences.getBoolean("increas_checkBox_early", false)){
        					if(i < sharedPreferences.getInt("seekBar_early", 
        							audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC))){
        						audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i++, 0);
        					}
        				}
        			}

        			public void onFinish() {
        				if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
        					mMediaPlayer.stop();
        					mMediaPlayer.reset();
        					mMediaPlayer.release();
        					mMediaPlayer = null;
        					v.cancel();
        					if(a != null)
        						a.dismiss();
        				}
        			}
        		}.start();	
        	}
        }
	}
	
	public void alertDialog_must(AlertDialog alertDialog_must){
		b = alertDialog_must;
	}
	
	public void playSound_must(Context context){
		v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		mMediaPlayer = new MediaPlayer();
		audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		boolean vib;
		
		if(sharedPreferences.getBoolean("vib_checkBox_must", false)){
			vib = true;
        }else{
        	vib = false;
        }
		//add this to avoid jerky sound at the beginning
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 1, 0);
        
        if(!sharedPreferences.getBoolean("increas_checkBox_must", false)){
        	audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 
        			sharedPreferences.getInt("seekBar_must", 
        					audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)), 0);
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
        			if(vib)
        				v.vibrate(1000*60*3);
        		} catch (Exception e) { }
        	}else{
        		int songMust = sharedPreferences.getInt("selected_must_song_id", R.raw.chariots_of_fire);
            	mMediaPlayer = MediaPlayer.create(context.getApplicationContext(), songMust);
            	mMediaPlayer.setLooping(true);
            	mMediaPlayer.start();
            	if(vib)
    				v.vibrate(1000*60*3);
        	}

        	//stop playing after certain amount of time   
	 		long alarmEndTime = 1000*60*3;   //end after 3 min
	 		ct = new CountDownTimer(alarmEndTime, 1000) {
	 			int i = 0;
	 		     public void onTick(long millisUntilFinished) {
	 		    	 // do something on interval
	 		    	 //do nothing
	 		    	 if(sharedPreferences.getBoolean("increas_checkBox_must", false)){
	 		    		 if(i < sharedPreferences.getInt("seekBar_must", 
	 		    				 audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC))){
	 		    			 audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i++, 0);
	 		    		 }
	 		    	 }
	 		     }

	 		     public void onFinish() {
	 		    	 if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
	 		    		 mMediaPlayer.stop();
	 		    		 mMediaPlayer.reset();
	 		    		 mMediaPlayer.release();
	 		    		 mMediaPlayer = null;
	 		    		 v.cancel();
	 		    		 if(b != null)
	 		    			 b.dismiss();
	 		    	 }
	 		     }
	 		}.start();	
        }
        
	}
}
