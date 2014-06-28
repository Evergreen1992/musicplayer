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

//��activity
public class MainActivity extends Activity implements OnGestureListener, OnTouchListener{
	
	ListView listView = null ;
	MusicService musicService = null ;
	TextView info = null ;
	Handler handler = new Handler();
	SeekBar seekBar = null ; //���Ž�����
	Button button = null ; //���š���ͣ��ť
	TextView time = null ;//ʱ����ʾ
	String[] musicNames = null ; //������������
	
	int minutes = 0 ; //��ǰ���ŵķ���
	int seconds = 0 ; //��ǰ���ŵ�������
	GestureDetector mGestureDetector = null ;
	
	
	//��ɾ���޸ĵ����ݣ�������������������
	public void initMusicService(){
		List<String> names = new ArrayList<String>();
		List<String> paths = new ArrayList<String>();
		
		//��ȡϵͳ�����ļ�
		Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		cursor.moveToFirst();
		
		System.out.println("�ܹ�:" + cursor.getColumnCount() + "�������ļ�");
		
		
		//����ý�����ݿ�
		while (!cursor.isAfterLast()) { 
	       
			String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));  

			String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));    
			names.add(title);
			paths.add(url);
			
			cursor.moveToNext(); 		       
	     }
		
		
		
		String[] fileNames = new String[names.size()];
		String[] urls = new String[names.size()];
		//��ӵ�����
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
        
        //������̨����
        Intent intent = new Intent();
        intent.setAction("com.player.service.MainService");
        startService(intent);
        
        initMusicService();//
        //musicService = new MusicService(this);
        info = (TextView)this.findViewById(R.id.info);
        button = (Button)this.findViewById(R.id.play);
        time = (TextView)this.findViewById(R.id.currenttime);
        listView = (ListView)this.findViewById(R.id.list_view);
        
        
        initListView();//��ʼ��listView;
        
        //�������������
        seekBar = (SeekBar)this.findViewById(R.id.seekbar);
    }
	
	@SuppressWarnings("deprecation")
	public void initListView(){
		musicNames = musicService.getAllFileNames();
		
        listView.setAdapter(new ListViewAdapter());
		//listView.setAdapter(adapter);
        listView.setOnItemClickListener(new listViewClickListener());
        
        //��Ļ��������
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
    		if( this.musicService.getMediaPlayer().isPlaying() == true){//��ͣ����
    			((Button)v).setBackground(getResources().getDrawable(R.drawable.pause));
    			this.musicService.getMediaPlayer().pause();
    			this.musicService.pause = true ;
    		}
    		else{//��������
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
    	//���ø�����
    	info.setText(this.musicService.getCurrentName());
    }
    
    protected void onDestroy() {
    	this.musicService = null ;
    	System.out.println("***����activity*****");
    	
    	Intent intent = new Intent();
    	intent.setAction("com.player.service.MainService");
    	stopService(intent);
    	
		super.onDestroy();
	}
    
	//���ð�ť����ͼƬ
	@SuppressLint("NewApi") 
	public void changePic(){
		button.setBackground(getResources().getDrawable(R.drawable.playing));
	}

	//�˵�����
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.exit_button:
			new AlertDialog.Builder(this) 
			.setTitle("ϵͳ��ʾ")
			.setMessage("ȷ���˳���������")
			.setPositiveButton("��", new dialogButtonListener())
			.setNegativeButton("��", new dialogButtonListener())
			.show();
			break;
		case R.id.setting:
			break;
		}
		
		return super.onContextItemSelected(item);
	}
	
	
	//������ʱ������
	public void updateTime(){
		int time = this.musicService.getMediaPlayer().getCurrentPosition();
		this.minutes = (time / 1000 ) / 60 ;//���÷�����
		this.seconds = time / 1000 - minutes * 60  ;
		String str = minutes + ":" + seconds ;
		this.time.setText(str);
	}
	
	//���������ʱ������
	public void seekBarInit(int fulltime){
		this.seekBar.setMax(fulltime);
	}
	
	//������ʱ������
	public void seekBarTime(int time){
		this.seekBar.setProgress(time);
	}
	
	//ListView item����¼�
	private final class listViewClickListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> parent, View v1, int arg2, long arg3) {
			musicService.play(arg2);
			info.setText(musicService.getCurrentName());
			changePic();
		}
    }
	
	//listView������
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
             * ����item���
             */
            mTextView.setHeight(100);
            
            return mTextView; 
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	
	//�Ի������
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

	
	//�����¼�����
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