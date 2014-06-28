package com.player.activity;

import com.player.audioplayer.R;
import com.player.service.LricService;
import com.player.view.LricView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

//设置界面activity
public class SettingActivity extends Activity {
	
	Button returnButton = null ;//返回按钮
	LricView lricView = null ; //歌词显示控件
	TextView currentSong = null ; //当前音乐
	LricService lricService = null ; //歌词服务类

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		System.out.println("************创建setting activity*************");
		setContentView(R.layout.setting);
		
		//返回主菜单按钮
		returnButton = (Button)this.findViewById(R.id.return_main_button);
		returnButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				backToMain();
			}
		});

		currentSong = (TextView)this.findViewById(R.id.current_song);
		lricView = (LricView)this.findViewById(R.id.lric_view);
		
		lricService = new LricService(lricView, this);
	}
	
	//刷新歌曲名称
	public void updateSongName(String name){
		this.currentSong.setText(name);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("************销毁setting activity*************");
	}

	public void backToMain(){
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
}