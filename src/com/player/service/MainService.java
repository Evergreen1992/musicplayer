package com.player.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

//��Ҫ�ķ�����
public class MainService extends Service{
	
	@Override
	public IBinder onBind(Intent arg0) {
		System.out.println("MainService  *****");
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("MainService����*****");
		//�������
		initMusic();
	}

	@Override
	public void onDestroy() {
		System.out.println("MainServiceֹͣ*****");
		super.onDestroy();
	}
	
	//����������⡣��ȡ�����ļ�
	public void initMusic(){
		System.out.println("*************�������ּ��***************");
	}
}