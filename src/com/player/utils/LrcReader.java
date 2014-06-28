package com.player.utils;

import android.annotation.SuppressLint;
import java.io.*;
import java.util.TreeMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LrcReader {
	
	static String regxTime = "[0-9]{2}+:[0-9]{2}.[0-9]{2}";//Ê±¼äÆ¥Åä×Ö·û´®
	static String regxContent = "\\[[0-9]{2}+:[0-9]{2}.[0-9]{2}\\]";//¸è´ÊÄÚÈÝ×Ö·û´®
	//168570
	/**
	 * 
	 * @param str 
	 * @param regex 
	 */
	public static String regexMathch(String str, String regex){
		String timeStr = null ; //
		//
		Pattern pattern = Pattern.compile(regxTime);
		Matcher timeMacher = pattern.matcher(str);
		
		while (timeMacher.find()) { 
			timeStr = timeMacher.group();
		} 
		
		return timeStr ;
	}
	
	//
	@SuppressLint("UseSparseArrays") 
	public static Map<Integer, String> readLrc(String path){
		Map<Integer, String> result = new TreeMap<Integer, String>();
		File file = new File(path);
		BufferedReader br = null ;
		
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String str = null ;
			String time = null ;
			String content = null ;
			
			while((str =  br.readLine()) != null ){
				if( ( time = regexMathch(str , regxTime) ) != null){
					content = str.replaceAll(regxContent, " ") ;
					result.put( timeSwitch(time) , content);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if( br != null )
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
		return result ;
	}
	
	/**
	 * Ê±¼ä×ª»»
	 * @param time
	 * @return
	 */
	public static Integer timeSwitch(String time){
		int minute = Integer.parseInt(time.substring(0, 2));//·ÖÖÓÊý
		float second = (0.00f + Float.parseFloat(time.substring(3, 8)));//ÃëÖÓ
		int finalTime = (int)( minute * 60 + second ) * 1000 ;
		return finalTime; 
	}
}