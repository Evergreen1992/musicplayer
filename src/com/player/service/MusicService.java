package com.player.service;

import com.player.activity.MainActivity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;

//音乐播放服务类
public class MusicService {
	
	//全局访问的静态变量
	public static String[] fileNames = null ;//播放文件名数组
	public static String[] filePaths = null ; //文件对应的地址
	public static int cuttent = 0 ; //当前播放的第几个
	
	
	MediaPlayer mediaPlayer = null ;//
	int position = 0 ; //播放进度
	String defaultPath = "/ttpod/song/";//
	public boolean pause = false ; //是否暂停
	MainActivity mainActivity = null ;
	int timeDelay = 1000 ;//线程时间刷新间隔***
	
	//handler处理时间刷新问题
	Handler timeHandler = new Handler();
	Runnable timeRunnable = new Runnable(){
		public void run(){
			if( pause == false)//刷新时间和进度条
			{
				mainActivity.updateTime();
				mainActivity.seekBarTime(mediaPlayer.getCurrentPosition()/1000);
			}
			timeHandler.postDelayed(this, timeDelay);
		}
	};
	
	
	public MusicService(MainActivity mc){
		this.mainActivity = mc ;
		mediaPlayer = new MediaPlayer();
	}
	
	
	/**
	 * 获取播放器当前播放进度
	 * @return
	 */
	public int getPosition(){
		if( mediaPlayer == null )
			return 0;
		else
			return this.mediaPlayer.getCurrentPosition();
	}
	
	public MediaPlayer getMediaPlayer(){
		return this.mediaPlayer;
	}
	
	/**
	 * 获取音乐文件列表数组
	 * @return
	 */
	public String[] getAllFileNames(){
		return fileNames;
	}
	
	/**
	 * 获取当前播放的文件名
	 * @return
	 */
	public String getCurrentName(){
		if( cuttent < fileNames.length)
			return fileNames[cuttent];
		else
			return "当前未播放";
	}
	
	
	//暂停后继续播放
	public void playAfterApause(){
		this.mediaPlayer.seekTo(this.position);
	}

	//播放音乐
	public void play(){
		this.play(filePaths[cuttent]);
	}
	
	/**
	 * 指定播放第position条音乐
	 * @param position
	 */
	public void play(int position){
		cuttent = position ;
		this.play();
	}
	
	/**
	 * 播放path位置的音乐
	 * @param path
	 */
	public void play(String path){
		try{
			timeHandler.postDelayed(timeRunnable,timeDelay);//开始计时
			//清空进度
			this.position = 0 ;
			
    		mediaPlayer.reset();
    		mediaPlayer.setDataSource(path);
    		mediaPlayer.prepare();//缓冲
    		mediaPlayer.setOnPreparedListener(new OnPreparedListener(){
				public void onPrepared(MediaPlayer arg0) {
					mainActivity.seekBarInit(mediaPlayer.getDuration()/1000);
					mediaPlayer.start();
				}
    		});
    		
    		//播放结束事件监听
    		mediaPlayer.setOnCompletionListener(new OnCompletionListener(){

				public void onCompletion(MediaPlayer arg0) {
					next();//下一曲
					
				}
    		});
    	}catch(Exception e){
    		e.printStackTrace();
    	}
	}
	
	public void stop(){
		if( this.mediaPlayer != null ){
			this.mediaPlayer.stop();
		}
	}
	
	public void next(){
		if( cuttent + 1 < fileNames.length)
		{	
			cuttent ++ ;
			this.play();
			this.mainActivity.setSongName();
		}
	}
	
	public void before(){
		if( cuttent - 1 >= 0){
			cuttent -- ;
			this.play();
			this.mainActivity.setSongName();
		}
	}
}