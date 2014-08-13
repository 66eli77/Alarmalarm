package com.majia.alarmalarm;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

public class EarlySongActivity extends Activity implements View.OnTouchListener{
	private TextView back;
	private ListView listView;
	private List<DataList> rowItems;
    DataList listData;
    EarlyAdapterList adapter;
    SharedPreferences sharedPreferences;
    private MySettings mySetting;
    
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
	    listView = (ListView) findViewById(R.id.listView_early);
	    back.setText("<");
	    
	    back.setOnTouchListener(this);
	    
	    rowItems = new ArrayList<DataList>();
	    listData = new DataList("On eagles wings", R.raw.on_eagles_wings);
        rowItems.add(listData);
        listData = new DataList("hallelujah", R.raw.hallelujah);
        rowItems.add(listData);
        listData = new DataList("Korean baby", R.raw.korean_baby);
        rowItems.add(listData);
        listData = new DataList("Good morning", R.raw.good_morning);
        rowItems.add(listData);
        listData = new DataList("Ave maria", R.raw.ave_maria);
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
	public boolean onTouch(View v, MotionEvent event) {
		if(v.equals(back)){
			List<PlayerAndId> mediaList = adapter.getMediaList();
			for(int i = 0; i < mediaList.size(); i++){
        		try{
        			mediaList.get(i).getPlayer().stop();
        			mediaList.get(i).getPlayer().release();
        		}catch(Exception ex) { 
        			ex.printStackTrace();
        		} 	
        	}
			
			int selectedSong = sharedPreferences.getInt("selected_early_song_id", 0);
			for(int j = 0; j < rowItems.size(); j++){
				if(rowItems.get(j).getSong() == selectedSong){
					mySetting.savePreferences("selected_early_song", rowItems.get(j).getRdText());
					break;
				}
			}
			finish();
		}
		return true;
	}

}
