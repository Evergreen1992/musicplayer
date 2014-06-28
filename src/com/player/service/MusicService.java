package com.player.service;

import com.player.activity.MainActivity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;

//���ֲ��ŷ�����
public class MusicService {
	
	//ȫ�ַ��ʵľ�̬����
	public static String[] fileNames = null ;//�����ļ�������
	public static String[] filePaths = null ; //�ļ���Ӧ�ĵ�ַ
	public static int cuttent = 0 ; //��ǰ���ŵĵڼ���
	
	
	MediaPlayer mediaPlayer = null ;//
	int position = 0 ; //���Ž���
	String defaultPath = "/ttpod/song/";//
	public boolean pause = false ; //�Ƿ���ͣ
	MainActivity mainActivity = null ;
	int timeDelay = 1000 ;//�߳�ʱ��ˢ�¼��***
	
	//handler����ʱ��ˢ������
	Handler timeHandler = new Handler();
	Runnable timeRunnable = new Runnable(){
		public void run(){
			if( pause == false)//ˢ��ʱ��ͽ�����
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
	 * ��ȡ��������ǰ���Ž���
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
	 * ��ȡ�����ļ��б�����
	 * @return
	 */
	public String[] getAllFileNames(){
		return fileNames;
	}
	
	/**
	 * ��ȡ��ǰ���ŵ��ļ���
	 * @return
	 */
	public String getCurrentName(){
		if( cuttent < fileNames.length)
			return fileNames[cuttent];
		else
			return "��ǰδ����";
	}
	
	
	//��ͣ���������
	public void playAfterApause(){
		this.mediaPlayer.seekTo(this.position);
	}

	//��������
	public void play(){
		this.play(filePaths[cuttent]);
	}
	
	/**
	 * ָ�����ŵ�position������
	 * @param position
	 */
	public void play(int position){
		cuttent = position ;
		this.play();
	}
	
	/**
	 * ����pathλ�õ�����
	 * @param path
	 */
	public void play(String path){
		try{
			timeHandler.postDelayed(timeRunnable,timeDelay);//��ʼ��ʱ
			//��ս���
			this.position = 0 ;
			
    		mediaPlayer.reset();
    		mediaPlayer.setDataSource(path);
    		mediaPlayer.prepare();//����
    		mediaPlayer.setOnPreparedListener(new OnPreparedListener(){
				public void onPrepared(MediaPlayer arg0) {
					mainActivity.seekBarInit(mediaPlayer.getDuration()/1000);
					mediaPlayer.start();
				}
    		});
    		
    		//���Ž����¼�����
    		mediaPlayer.setOnCompletionListener(new OnCompletionListener(){

				public void onCompletion(MediaPlayer arg0) {
					next();//��һ��
					
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