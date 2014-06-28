package com.player.service;

import java.io.File;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map;
import android.annotation.SuppressLint;
import android.os.Environment;
import android.os.Handler;

import com.player.activity.SettingActivity;
import com.player.utils.LrcReader;
import com.player.view.LricView;

/**
 * 歌词服务类
 * @author Evergreen
 *
 */
@SuppressLint("UseSparseArrays") 
public class LricService {

	
	LricView lricView = null ;
	SettingActivity activity = null ; 
	
	int timeDelay = 10 ; //歌词线程时间刷新间隔
	long currentTime = 0 ; //当前线程时间
	int timeToBe = 0 ; //需要显示的歌词句子的时间
	
	Iterator<Integer> timePointer = null ; //
	Map<Integer, String> lrc = new TreeMap<Integer, String>();//歌词信息
	String lrcPath = "/ttpod/song/test.lrc" ;//歌词路径
	
	Handler timeHandler = new Handler();
	Runnable timeRunnable = new Runnable(){
		public void run(){
			timeJudgeAndContentDisplay();
			refreshSongName();
			
			currentTime += timeDelay ; // 时间增加
			timeHandler.postDelayed(this, timeDelay);
		}
	};
	
	//更新歌曲名
	public void refreshSongName(){
		if( MusicService.cuttent >= 0 && MusicService.cuttent < MusicService.fileNames.length)
			this.activity.updateSongName(MusicService.fileNames[MusicService.cuttent]);
	}
	
	//时间的判断
	public void timeJudgeAndContentDisplay(){
		
		if( (int)this.currentTime == (int)timeToBe){
			this.lricView.refresh();
			
			if( timePointer.hasNext()){
				timeToBe = timePointer.next();
			}
		}else{
			
		}
		
	}
	
	public LricService(LricView lricView, SettingActivity activity){
		this.activity = activity ;
		timeHandler.postDelayed(timeRunnable,timeDelay);//线程开始运行
		this.lricView = lricView ;
		refresh(MusicService.cuttent);//初始信息
	}
	
	//初始化
	public void refresh(int position){
		String url = MusicService.filePaths[position] ;
		String lrcPath = Environment.getExternalStorageDirectory() + "/MusicPlayer/lrcs" +url.substring(url.lastIndexOf("/"), url.lastIndexOf(".")) + ".lrc";
		System.out.println("当前歌词路径:" + lrcPath);
		File lrcFile = new File(lrcPath);
		
		if( lrcFile.exists()){
			//歌词信息初始化
			lrc = LrcReader.readLrc(lrcPath);
		}else{
			//歌词信息初始化
			lrc = LrcReader.readLrc(Environment.getExternalStorageDirectory() + this.lrcPath);
			System.out.println("未找到歌词!");
		}
		
		//时间初始化
		currentTime = 0 ;
		
		String[] strs = new String[lrc.keySet().size()];
		Iterator<Integer> pointer = lrc.keySet().iterator();
		
		timePointer = lrc.keySet().iterator();
		
		
		int index = 0 ; 
		while( pointer.hasNext()){
			strs[index] = lrc.get(pointer.next());
			index ++ ;
		}
		
		this.lricView.setLrcStrs(strs);
		
		if( lrc.keySet().size() > 0 ){
			timeToBe = timePointer.next();
			System.out.println("初始:" + timeToBe);
		}
	}
}