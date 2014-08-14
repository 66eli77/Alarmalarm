package com.majia.alarmalarm;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LocalSongEarlyActivity extends Activity implements View.OnTouchListener{
	private ListView musiclist;
	private Cursor musiccursor;
	private int music_column_index;
	private int count;
	private MediaPlayer mMediaPlayer;
    private TextView back;
    private MySettings mySetting;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          this.requestWindowFeature(Window.FEATURE_NO_TITLE);
          overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
          
          setContentView(R.layout.activity_local_song);
          
          back = (TextView) findViewById(R.id.localsong_back);
          back.setText("<");
          back.setOnTouchListener(this);
          
          mySetting = new MySettings(this);
          
          init_phone_music_grid();
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
    		mMediaPlayer.reset();
    		finish();
    	}
    	return true;
    }

    
    private void init_phone_music_grid() {
    	System.gc();
    	String[] proj = { MediaStore.Audio.Media._ID,
    			MediaStore.Audio.Media.DATA,
    			MediaStore.Audio.Media.DISPLAY_NAME};
    			//MediaStore.Video.Media.SIZE };
    	    	
    	ContentResolver cr=getContentResolver();
    	Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    	musiccursor = cr.query(uri, proj, null, null, null);
    	count = musiccursor.getCount();
    	musiclist = (ListView) findViewById(R.id.local_song_List);
        musiclist.setAdapter(new MusicAdapter(getApplicationContext()));

        musiclist.setOnItemClickListener(musicgridlistener);
        mMediaPlayer = new MediaPlayer();
    }
    
    private OnItemClickListener musicgridlistener = new OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
              System.gc();
              music_column_index = musiccursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
              musiccursor.moveToPosition(position);
              String filename = musiccursor.getString(music_column_index);

              try {
                    if (mMediaPlayer.isPlaying()) {
                          mMediaPlayer.reset();
                    }
                    TextView s = (TextView) v;
                    String a = s.getText().toString();
                    mySetting.savePreferences("local_name_early", a);
                    mySetting.savePreferences("local_data_early", filename);
                    mySetting.savePreferences("local_boolean_early", true);
                   // Toast.makeText(LocalSongActivity.this, "name " + a, Toast.LENGTH_SHORT).show();
                    mMediaPlayer.setDataSource(filename);
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
              } catch (Exception e) { }
        }
  };
  
  public class MusicAdapter extends BaseAdapter {
      private Context mContext;

      public MusicAdapter(Context c) {
            mContext = c;
      }

      public int getCount() {
            return count;
      }

      public Object getItem(int position) {
            return position;
      }

      public long getItemId(int position) {
            return position;
      }

      public View getView(int position, View convertView, ViewGroup parent) {
            System.gc();
            TextView tv = new TextView(mContext.getApplicationContext());
            String id = null;
            if (convertView == null) {
                  music_column_index = musiccursor
                		  .getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
                  musiccursor.moveToPosition(position);
                  id = musiccursor.getString(music_column_index);
               //   music_column_index = musiccursor
                //		  .getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
                  musiccursor.moveToPosition(position);
                  //id += " Size(KB):" + musiccursor.getString(music_column_index);
                  tv.setTextColor(Color.BLACK);
                  tv.setTextSize(18);
                  tv.setText(id);
            } else
                  tv = (TextView) convertView;
            return tv;
      }
  }
  
  
}
