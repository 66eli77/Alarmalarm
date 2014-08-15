package com.majia.alarmalarm;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;

public class MustAdapterList extends ArrayAdapter<DataList> {

	Context context;
	ViewHolderMust holder;
    private RadioButton mCurrentlyCheckedRB;
    private int song;
    private MySettings mySetting;
    SharedPreferences sharedPreferences;
    private SingletonMediaPlayer player = SingletonMediaPlayer.getInstance();
    
    public MustAdapterList(Context context, int textViewResourceId, List<DataList> items) {
        super(context, textViewResourceId, items);
        this.context = context;
        mySetting = new MySettings(context);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }
    
    private class ViewHolderMust {
        RadioButton radioBtn;
    }
    
    public void uncheckSelected(){
    	if(mCurrentlyCheckedRB != null)
    		mCurrentlyCheckedRB.setChecked(false);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        holder = null;
        DataList rowItem = getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_must_song_radio, null);
            holder = new ViewHolderMust();
            holder.radioBtn = (RadioButton) convertView.findViewById(R.id.radioButton_must);
            convertView.setTag(holder);
        } else
            holder = (ViewHolderMust) convertView.getTag();

        song = rowItem.getSong();
        int selectedSong = sharedPreferences.getInt("selected_must_song_id", 0);
        //Toast.makeText(context, "in" , Toast.LENGTH_SHORT).show();
        if(sharedPreferences.getBoolean("local_boolean_must", false)){
        	holder.radioBtn.setChecked(false);
        	mCurrentlyCheckedRB = holder.radioBtn;
        }else{
        	if(song == selectedSong){
        		holder.radioBtn.setChecked(true);
        		mCurrentlyCheckedRB = holder.radioBtn;
        	}else{
        		holder.radioBtn.setChecked(false);
        	}
        }
        
        holder.radioBtn.setOnClickListener(new OnClickListener() {
        	int mySong = song;
        	
            @Override
            public void onClick(View v) {
            	player.stop();
            	player.play_inAdapterList(context, mySong);
            	mySetting.savePreferences("selected_must_song_id", mySong);
            	mySetting.savePreferences("local_boolean_must", false);
            	
            	MustSongActivity.localSongMust.setBackgroundColor(Color.WHITE);

            	if (mCurrentlyCheckedRB != null) {
            		mCurrentlyCheckedRB.setChecked(false);
            		((RadioButton) v).setChecked(true);
            		mCurrentlyCheckedRB = (RadioButton) v;
            	}
            }
        });

        holder.radioBtn.setText(rowItem.getRdText());
        return convertView;
    }

}
