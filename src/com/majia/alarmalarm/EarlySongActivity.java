package com.majia.alarmalarm;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

public class EarlySongActivity extends Activity implements View.OnTouchListener{
	private TextView back;
	static TextView localSongEarly;
	private ListView listView;
	private List<DataList> rowItems;
	private DataList listData;
	private EarlyAdapterList adapter;
	private SharedPreferences sharedPreferences;
    private MySettings mySetting;
    private SingletonMediaPlayer player = SingletonMediaPlayer.getInstance();
    
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	    //opening transition animations
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
	    setContentView(R.layout.activity_early_song); 
	    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
	    mySetting = new MySettings(this);
	    
	    back = (TextView) findViewById(R.id.earlysong_back);
	    localSongEarly = (TextView) findViewById(R.id.local_song_early);
	    listView = (ListView) findViewById(R.id.listView_early);
	    back.setText("<");
	    
	    String n = sharedPreferences.getString("local_name_early", 
	    		"Selecte song from your local collection");
	    localSongEarly.setText(n);
	    
	    back.setOnTouchListener(this);
	    localSongEarly.setOnTouchListener(this);
	    
	    rowItems = new ArrayList<DataList>();
	    listData = new DataList("On eagles wings", R.raw.on_eagles_wings);
        rowItems.add(listData);
        listData = new DataList("Beautiful dance keys", R.raw.beautiful_dance_keys);
        rowItems.add(listData);
        listData = new DataList("Korean baby", R.raw.korean_baby);
        rowItems.add(listData);
        listData = new DataList("Choirs deluxe pack", R.raw.choirs_deluxe_pack);
        rowItems.add(listData);
        listData = new DataList("Ave maria", R.raw.ave_maria);
        rowItems.add(listData);
        listData = new DataList("Click drum", R.raw.click_drum_loop);
        rowItems.add(listData);
        listData = new DataList("Frozen", R.raw.frozen_tone);
        rowItems.add(listData);
        listData = new DataList("Fuzz percussion bongo", R.raw.fuzz_percussion_bongo);
        rowItems.add(listData);
        listData = new DataList("Godfather guitar", R.raw.godfather_guitar);
        rowItems.add(listData);
        listData = new DataList("Harp psychotropic circle", R.raw.harp_psychotropic_circle);
        rowItems.add(listData);
        listData = new DataList("Heart touching flute", R.raw.heart_touching_flute);
        rowItems.add(listData);
        listData = new DataList("I just called", R.raw.i_just_called);
        rowItems.add(listData);
        listData = new DataList("Kill bill whistle", R.raw.kill_bill___whistle);
        rowItems.add(listData);
        listData = new DataList("Little drummer boy", R.raw.little_drummer_boy);
        rowItems.add(listData);
        listData = new DataList("Lumia", R.raw.lumia);
        rowItems.add(listData);
        listData = new DataList("Morning", R.raw.morning);
        rowItems.add(listData);
        listData = new DataList("Ping pong", R.raw.ping_pong_sound);
        rowItems.add(listData);
        listData = new DataList("Satie psychotropic circle", R.raw.satie_psychotropic_circle);
        rowItems.add(listData);

        adapter=new EarlyAdapterList(this, R.layout.activity_early_song_radio, rowItems);
        listView.setAdapter(adapter);
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
	    //closing transition animations
	    overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
	}
	
	@Override
	protected void onResume() {
        super.onResume();
        String n = sharedPreferences.getString("local_name_early", 
	    		"Selecte song from your local collection");
        localSongEarly.setText(n);
	    if(sharedPreferences.getBoolean("local_boolean_early", false)){
	    	localSongEarly.setBackgroundColor(Color.YELLOW);
	    	adapter.uncheckSelected();
	    }else{
	    	localSongEarly.setBackgroundColor(Color.WHITE);
	    }
	}


	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(v.equals(back)){
			if(player.isPlaying())
				player.stop();
			
			int selectedSong = sharedPreferences.getInt("selected_early_song_id", 0);
			for(int j = 0; j < rowItems.size(); j++){
				if(rowItems.get(j).getSong() == selectedSong){
					mySetting.savePreferences("selected_early_song", rowItems.get(j).getRdText());
					break;
				}
			}
			finish();
		}
		
		if(v.equals(localSongEarly)){
			if(player.isPlaying())
				player.stop();
			Intent localSongIntent = new Intent(this, LocalSongEarlyActivity.class);
			localSongIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(localSongIntent);
		}
		return true;
	}

}
