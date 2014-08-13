package com.majia.alarmalarm;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;

public class EarlyAdapterList extends ArrayAdapter<DataList> {

    Context context;
    ViewHolderEarly holder;
    private RadioButton mCurrentlyCheckedRB;
    private int song;
    private List<PlayerAndId> mediaList = new ArrayList<PlayerAndId>();
    private MySettings mySetting;
    SharedPreferences sharedPreferences;
    
    public EarlyAdapterList(Context context, int textViewResourceId, List<DataList> items) {
        super(context, textViewResourceId, items);
        this.context = context;
        mySetting = new MySettings(context);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }
    
    public List<PlayerAndId> getMediaList(){
    	return mediaList;
    }

    private class ViewHolderEarly {
        RadioButton radioBtn;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        holder = null;
        DataList rowItem = getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_early_song_radio, null);
            holder = new ViewHolderEarly();
            holder.radioBtn = (RadioButton) convertView.findViewById(R.id.radioButton_early);
            convertView.setTag(holder);
        } else
            holder = (ViewHolderEarly) convertView.getTag();

        song = rowItem.getSong();
        int selectedSong = sharedPreferences.getInt("selected_early_song_id", 0);
        if(song == selectedSong){
        	holder.radioBtn.setChecked(true);
        	mCurrentlyCheckedRB = holder.radioBtn;
		}else{
			holder.radioBtn.setChecked(false);
		}
        
        holder.radioBtn.setOnClickListener(new OnClickListener() {
        	int mySong = song;
        	MediaPlayer mediaPlayer = MediaPlayer.create(context.getApplicationContext(), mySong);
        	MediaPlayerList mpl = new MediaPlayerList(mediaPlayer, mediaList, mySong);
        	
            @Override
            public void onClick(View v) {
            	//MediaPlayer mediaPlayer;
            	for(int i = 0; i < mediaList.size(); i++){
            		if(mediaList.get(i).getPlayer().isPlaying()){

            			mediaList.get(i).getPlayer().pause();
            			mediaList.get(i).getPlayer().seekTo(0);
            		}
            	}
            	mediaPlayer.start();
            	mySetting.savePreferences("selected_early_song_id", mySong);

                mCurrentlyCheckedRB.setChecked(false);
                ((RadioButton) v).setChecked(true);
                mCurrentlyCheckedRB = (RadioButton) v;
            }
        });

        holder.radioBtn.setText(rowItem.getRdText());
        return convertView;
    }

}