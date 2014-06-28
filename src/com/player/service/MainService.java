package com.player.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

//主要的服务类
public class MainService extends Service{
	
	@Override
	public IBinder onBind(Intent arg0) {
		System.out.println("MainService  *****");
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("MainService启动*****");
		//启动检查
		initMusic();
	}

	@Override
	public void onDestroy() {
		System.out.println("MainService停止*****");
		super.onDestroy();
	}
	
	//服务启动检测。读取音乐文件
	public void initMusic(){
		System.out.println("*************启动音乐检测***************");
	}
}