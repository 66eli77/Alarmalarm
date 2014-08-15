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

public class MustSongActivity extends Activity implements View.OnTouchListener{
	private TextView back;
	static TextView localSongMust;
	private ListView listView;
	private List<DataList> rowItems;
    DataList listData;
    MustAdapterList adapter;
    SharedPreferences sharedPreferences;
    private MySettings mySetting;
    private SingletonMediaPlayer player = SingletonMediaPlayer.getInstance();
    
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	    //opening transition animations
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
	    setContentView(R.layout.activity_must_song); 
	    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
	    mySetting = new MySettings(this);
	    
	    back = (TextView) findViewById(R.id.mustsong_back);
	    localSongMust = (TextView) findViewById(R.id.local_song_must);
	    listView = (ListView) findViewById(R.id.listView_must);
	    back.setText("<");
	    
	    String n = sharedPreferences.getString("local_name_must", 
	    		"Selecte song from your local collection");
	    localSongMust.setText(n);
	    
	    back.setOnTouchListener(this);
	    localSongMust.setOnTouchListener(this);
	    
	    rowItems = new ArrayList<DataList>();
	    listData = new DataList("9000000", R.raw.i90000);
        rowItems.add(listData);
        listData = new DataList("AK47", R.raw.ak_47);
        rowItems.add(listData);
        listData = new DataList("Army wake up", R.raw.army_wake_up);
        rowItems.add(listData);
        listData = new DataList("Chariots of fire", R.raw.chariots_of_fire);
        rowItems.add(listData);
        listData = new DataList("If I die", R.raw.if_i_die);
        rowItems.add(listData);
        listData = new DataList("iPhone mix", R.raw.iphone_5s_5c);
        rowItems.add(listData);
        listData = new DataList("Darth Vader", R.raw.darth_vader);
        rowItems.add(listData);
        listData = new DataList("Extreme clock alarm", R.raw.extreme_clock_alarm);
        rowItems.add(listData);
        listData = new DataList("Old telephone", R.raw.old_telephone);
        rowItems.add(listData);
        listData = new DataList("Rooster crow", R.raw.rooster_crow);
        rowItems.add(listData);
        listData = new DataList("Popeye", R.raw.popeye_messagetone);
        rowItems.add(listData);
        listData = new DataList("Wake Wake Up", R.raw.best_wake_up_sound);
        rowItems.add(listData);

        adapter=new MustAdapterList(this, R.layout.activity_must_song_radio, rowItems);
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
        String n = sharedPreferences.getString("local_name_must", 
	    		"Selecte song from your local collection");
        localSongMust.setText(n);
	    if(sharedPreferences.getBoolean("local_boolean_must", false)){
	    	localSongMust.setBackgroundColor(Color.YELLOW);
	    	adapter.uncheckSelected();
	    }else{
	    	localSongMust.setBackgroundColor(Color.WHITE);
	    }
	}


	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(v.equals(back)){
			player.stop();
			
			int selectedSong = sharedPreferences.getInt("selected_must_song_id", 0);
			for(int j = 0; j < rowItems.size(); j++){
				if(rowItems.get(j).getSong() == selectedSong){
					mySetting.savePreferences("selected_must_song", rowItems.get(j).getRdText());
					break;
				}
			}
			finish();
		}
		
		if(v.equals(localSongMust)){
			player.stop();
			
			Intent localSongIntent = new Intent(this, LocalSongMustActivity.class);
			localSongIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(localSongIntent);
		}
		return true;
	}

}
