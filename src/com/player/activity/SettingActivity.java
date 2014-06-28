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

//���ý���activity
public class SettingActivity extends Activity {
	
	Button returnButton = null ;//���ذ�ť
	LricView lricView = null ; //�����ʾ�ؼ�
	TextView currentSong = null ; //��ǰ����
	LricService lricService = null ; //��ʷ�����

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		System.out.println("************����setting activity*************");
		setContentView(R.layout.setting);
		
		//�������˵���ť
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
	
	//ˢ�¸�������
	public void updateSongName(String name){
		this.currentSong.setText(name);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("************����setting activity*************");
	}

	public void backToMain(){
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
}