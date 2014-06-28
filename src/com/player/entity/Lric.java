package com.player.entity;

import java.util.HashMap;
import java.util.Map;


//歌词对象
public class Lric {
	
	private String title = "未知";//歌名
	private String album = "未知";//专辑
	private String artist = "未知";//艺人
	private Map<Integer, String> detail = new HashMap<Integer, String>();//歌词信息
	
	public Map<Integer, String> getDetail() {
		return detail;
	}
	public void setDetail(Map<Integer, String> detail) {
		this.detail = detail;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
}
