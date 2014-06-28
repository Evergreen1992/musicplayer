package com.player.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * �����ʾ��ͼ��view
 * @author Evergreen
 *
 */
public class LricView extends View{
	
	Canvas canvas = null ;
	Paint paint = new Paint();
	String[] lrcStrs = null ; //���и��
	int pointer = 0 ;//��ǰλ��ָ��
	
	public LricView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		this.canvas = canvas ;
		
		if( lrcStrs != null ){
			showLrcs();//��ʾ���
		}
	}
	
	/**
	 * �����ʾ
	 */
	public void showLrcs(){
		//��ǰ���
		
		for( int i = pointer - 5 ; i < pointer + 5; i++ ){
			if( i >= 0 && i < this.lrcStrs.length){
				if( pointer == i)
					paint.setColor(Color.RED);
				else
					paint.setColor(Color.WHITE);
				paint.setTextSize(30);
				canvas.drawText(lrcStrs[i], 0 , 200 + i % 10 * 50, paint);
			}
		}
	}
	
	public void refresh(){
		this.pointer ++ ;
		this.invalidate();
	}
	
	public void setLrcStrs(String[] lrcStrs) {
		this.lrcStrs = lrcStrs;
	}
	
	public void setPointer(int pointer){
		this.pointer = pointer ;
	}
	
	public int getPointer(){
		return pointer ;
	}
}