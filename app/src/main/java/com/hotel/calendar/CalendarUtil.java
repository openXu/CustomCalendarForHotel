package com.hotel.calendar;

import android.content.Context;
import android.graphics.Paint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarUtil {
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 *
	 * @param pxValue
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 *
	 * @param spValue
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}



	public static final String FROMAT_MONTH ="yyyy-MM" ;
	public static final String FROMAT_DAY ="yyyy-MM-dd" ;

	/**获取月份标题*/
	public static String getMonthStr(Date month){
		SimpleDateFormat df = new SimpleDateFormat(FROMAT_MONTH);
		return df.format(month);
	}
	public static Date str2Date(String str, String fromat){
		try {
			SimpleDateFormat df = new SimpleDateFormat(fromat);
			return df.parse(str);
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	public static String date2Str(Date date, String fromat){
		try {
			SimpleDateFormat df = new SimpleDateFormat(fromat);
			return df.format(date);
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	public static Date getDayDate(String str){
		try {
			SimpleDateFormat df = new SimpleDateFormat(FROMAT_DAY);
			return df.parse(str);
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	public static Date getDayDate(Date date){
		try {
			String str = date2Str(date, FROMAT_DAY);
			return getDayDate(str);
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}



	public static float getFontlength(Paint paint, String str) {
		return paint.measureText(str);
	}
	public static float getFontHeight(Paint paint)  {
		Paint.FontMetrics fm = paint.getFontMetrics();
		return fm.descent - fm.ascent;
	}
	public static float getFontLeading(Paint paint)  {
		Paint.FontMetrics fm = paint.getFontMetrics();
		return fm.leading- fm.ascent;
	}











}
