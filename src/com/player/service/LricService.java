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
 * ��ʷ�����
 * @author Evergreen
 *
 */
@SuppressLint("UseSparseArrays") 
public class LricService {

	
	LricView lricView = null ;
	SettingActivity activity = null ; 
	
	int timeDelay = 10 ; //����߳�ʱ��ˢ�¼��
	long currentTime = 0 ; //��ǰ�߳�ʱ��
	int timeToBe = 0 ; //��Ҫ��ʾ�ĸ�ʾ��ӵ�ʱ��
	
	Iterator<Integer> timePointer = null ; //
	Map<Integer, String> lrc = new TreeMap<Integer, String>();//�����Ϣ
	String lrcPath = "/ttpod/song/test.lrc" ;//���·��
	
	Handler timeHandler = new Handler();
	Runnable timeRunnable = new Runnable(){
		public void run(){
			timeJudgeAndContentDisplay();
			refreshSongName();
			
			currentTime += timeDelay ; // ʱ������
			timeHandler.postDelayed(this, timeDelay);
		}
	};
	
	//���¸�����
	public void refreshSongName(){
		if( MusicService.cuttent >= 0 && MusicService.cuttent < MusicService.fileNames.length)
			this.activity.updateSongName(MusicService.fileNames[MusicService.cuttent]);
	}
	
	//ʱ����ж�
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
		timeHandler.postDelayed(timeRunnable,timeDelay);//�߳̿�ʼ����
		this.lricView = lricView ;
		refresh(MusicService.cuttent);//��ʼ��Ϣ
	}
	
	//��ʼ��
	public void refresh(int position){
		String url = MusicService.filePaths[position] ;
		String lrcPath = Environment.getExternalStorageDirectory() + "/MusicPlayer/lrcs" +url.substring(url.lastIndexOf("/"), url.lastIndexOf(".")) + ".lrc";
		System.out.println("��ǰ���·��:" + lrcPath);
		File lrcFile = new File(lrcPath);
		
		if( lrcFile.exists()){
			//�����Ϣ��ʼ��
			lrc = LrcReader.readLrc(lrcPath);
		}else{
			//�����Ϣ��ʼ��
			lrc = LrcReader.readLrc(Environment.getExternalStorageDirectory() + this.lrcPath);
			System.out.println("δ�ҵ����!");
		}
		
		//ʱ���ʼ��
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
			System.out.println("��ʼ:" + timeToBe);
		}
	}
}