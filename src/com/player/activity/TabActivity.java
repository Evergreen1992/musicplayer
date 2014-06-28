package com.player.activity;

import com.player.audioplayer.R;

import android.app.Activity;
import android.os.Bundle;

public class TabActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.tab_layout);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
