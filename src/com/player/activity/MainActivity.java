package com.player.activity;

import java.util.ArrayList;
import java.util.List;
import com.player.audioplayer.R;
import com.player.service.MusicService;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

//主activity
public class MainActivity extends Activity implements OnGestureListener, OnTouchListener{
	
	ListView listView = null ;
	MusicService musicService = null ;
	TextView info = null ;
	Handler handler = new Handler();
	SeekBar seekBar = null ; //播放进度条
	Button button = null ; //播放、暂停按钮
	TextView time = null ;//时间显示
	String[] musicNames = null ; //所有音乐名称
	
	int minutes = 0 ; //当前播放的分钟
	int seconds = 0 ; //当前播放到的秒钟
	GestureDetector mGestureDetector = null ;
	
	
	//待删除修改的内容！！！！！！！不合理
	public void initMusicService(){
		List<String> names = new ArrayList<String>();
		List<String> paths = new ArrayList<String>();
		
		//获取系统音乐文件
		Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		cursor.moveToFirst();
		
		System.out.println("总共:" + cursor.getColumnCount() + "个音乐文件");
		
		
		//遍历媒体数据库
		while (!cursor.isAfterLast()) { 
	       
			String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));  

			String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));    
			names.add(title);
			paths.add(url);
			
			cursor.moveToNext(); 		       
	     }
		
		
		
		String[] fileNames = new String[names.size()];
		String[] urls = new String[names.size()];
		//添加到数组
		for( int i = 0 ; i< names.size(); i++){
			fileNames[i] = names.get(i);
			urls[i] = paths.get(i);
		}
		
		musicService = new MusicService(this);
		MusicService.fileNames = fileNames;
		MusicService.filePaths = urls ;
	}
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //启动后台服务。
        Intent intent = new Intent();
        intent.setAction("com.player.service.MainService");
        startService(intent);
        
        initMusicService();//
        //musicService = new MusicService(this);
        info = (TextView)this.findViewById(R.id.info);
        button = (Button)this.findViewById(R.id.play);
        time = (TextView)this.findViewById(R.id.currenttime);
        listView = (ListView)this.findViewById(R.id.list_view);
        
        
        initListView();//初始化listView;
        
        //进度条设置相关
        seekBar = (SeekBar)this.findViewById(R.id.seekbar);
    }
	
	@SuppressWarnings("deprecation")
	public void initListView(){
		musicNames = musicService.getAllFileNames();
		
        listView.setAdapter(new ListViewAdapter());
		//listView.setAdapter(adapter);
        listView.setOnItemClickListener(new listViewClickListener());
        
        //屏幕滑动监听
        listView.setOnTouchListener(this);  
        mGestureDetector = new GestureDetector(this); 
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @SuppressLint("NewApi") 
    public void musicPlay(View v){
    	switch(v.getId()){
    	case R.id.play:
    		if( this.musicService.getMediaPlayer().isPlaying() == true){//暂停播放
    			((Button)v).setBackground(getResources().getDrawable(R.drawable.pause));
    			this.musicService.getMediaPlayer().pause();
    			this.musicService.pause = true ;
    		}
    		else{//继续播放
    			((Button)v).setBackground(getResources().getDrawable(R.drawable.playing));
    			
    			if( this.musicService.pause){
    				this.musicService.getMediaPlayer().start();
    				this.musicService.pause = false;
    			}
    		}
    		break;
    	case R.id.next:
    		this.musicService.next();
    		break;
    	case R.id.before:
    		this.musicService.before();
    		break;
    	}
    	
    	//setSongName();
    }
    
    public void setSongName(){
    	//设置歌曲名
    	info.setText(this.musicService.getCurrentName());
    }
    
    protected void onDestroy() {
    	this.musicService = null ;
    	System.out.println("***销毁activity*****");
    	
    	Intent intent = new Intent();
    	intent.setAction("com.player.service.MainService");
    	stopService(intent);
    	
		super.onDestroy();
	}
    
	//设置按钮背景图片
	@SuppressLint("NewApi") 
	public void changePic(){
		button.setBackground(getResources().getDrawable(R.drawable.playing));
	}

	//菜单处理
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.exit_button:
			new AlertDialog.Builder(this) 
			.setTitle("系统提示")
			.setMessage("确定退出播放器吗？")
			.setPositiveButton("是", new dialogButtonListener())
			.setNegativeButton("否", new dialogButtonListener())
			.show();
			break;
		case R.id.setting:
			break;
		}
		
		return super.onContextItemSelected(item);
	}
	
	
	//播放器时间设置
	public void updateTime(){
		int time = this.musicService.getMediaPlayer().getCurrentPosition();
		this.minutes = (time / 1000 ) / 60 ;//设置分钟数
		this.seconds = time / 1000 - minutes * 60  ;
		String str = minutes + ":" + seconds ;
		this.time.setText(str);
	}
	
	//进度条最大时间设置
	public void seekBarInit(int fulltime){
		this.seekBar.setMax(fulltime);
	}
	
	//进度条时间设置
	public void seekBarTime(int time){
		this.seekBar.setProgress(time);
	}
	
	//ListView item点击事件
	private final class listViewClickListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> parent, View v1, int arg2, long arg3) {
			musicService.play(arg2);
			info.setText(musicService.getCurrentName());
			changePic();
		}
    }
	
	//listView适配器
	private final class ListViewAdapter extends BaseAdapter{
		
		public int getCount() {
			return musicNames.length;
		}

		public Object getItem(int arg0) {
			return arg0;
		}

		public long getItemId(int arg0) {
			return arg0;
		}

		public View getView(int arg0, View arg1, ViewGroup arg2) {
			TextView mTextView = new TextView(getApplicationContext());
			
			mTextView.setText(musicNames[arg0]);
            mTextView.setTextSize(15);
            mTextView.setTextColor(Color.WHITE);
            
            //mTextView.setGravity(V)
            /**
             * 设置item间距
             */
            mTextView.setHeight(100);
            
            return mTextView; 
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	
	//对话框监听
	private final class dialogButtonListener implements DialogInterface.OnClickListener{

		public void onClick(DialogInterface arg0, int option) {
			switch(option){
			case AlertDialog.BUTTON_POSITIVE:
				break ;
			case AlertDialog.BUTTON_NEGATIVE:
				break ;
			}
		}
	}

	
	//手势事件方法
	@Override
	public boolean onDown(MotionEvent arg0) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		if( e1.getX() - e2.getX() > 200 && Math.abs(velocityX) > 0){
			Intent intent = new Intent(this, SettingActivity.class);
			startActivity(intent);
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		return false;
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		return  mGestureDetector.onTouchEvent(arg1);
	}

}