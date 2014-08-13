package com.majia.alarmalarm;

import java.util.List;

import android.media.MediaPlayer;

public class MediaPlayerList {
	private int mySong;

	public MediaPlayerList(MediaPlayer mediaPlayer, List<PlayerAndId> mediaList, int song){
		//this.context = c;
		//this.mySong = song;
		mediaList.add(new PlayerAndId(mediaPlayer, song));
		mySong = song;
	}
	
	public int getSongId(){
		return mySong;
	}
}

class PlayerAndId{
	int songId;
	MediaPlayer mediaPlayer;
	
	public PlayerAndId(MediaPlayer m, int id){
		this.mediaPlayer = m;
		this.songId = id;
	}
	
	public int getSongId(){
		return songId;
	}
	
	public MediaPlayer getPlayer(){
		return mediaPlayer;
	}
}