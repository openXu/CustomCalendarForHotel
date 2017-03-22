package com.hotel.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * autour: ox
 * date: 2017/3/22 9:41
 * className: CustomCalendar
 * version:
 * description: 自定义日历控件
 */
public class CalendarLayout extends LinearLayout {

    private String TAG = "CalendarLayout";

    private LinearLayout ll_left,  ll_right;
    private TextView tv_month;
    private CustomCalendar cal;

    private int monthIndex = 0;  //当前显示的月份索引

    private ArrayList<MonthDayBean.MonthBean> months;

    public CalendarLayout(Context context) {
        this(context, null);
    }
    public CalendarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public CalendarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //将控件组合挂载到自己身上
        LayoutInflater.from(context).inflate(R.layout.calendar_layout, this, true);
        ll_left = (LinearLayout) findViewById(R.id.ll_left);
        ll_right = (LinearLayout) findViewById(R.id.ll_right);
        tv_month = (TextView) findViewById(R.id.tv_month);
        cal = (CustomCalendar) findViewById(R.id.cal);

        ll_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setMonth(-1);
            }
        });
        ll_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setMonth(1);
            }
        });
        tv_month.setText("");
    }

    public void setData(ArrayList<MonthDayBean.MonthBean> months){
        this.months = months;
        if(months!=null) {
            refreshMonth();
        }
    }

    private void setMonth(int add){
        int index = monthIndex+add;
        if(index<0){
            monthIndex = 0;
            Toast.makeText(getContext(), "到头了", Toast.LENGTH_SHORT).show();
        }else if(index>=months.size()){
            monthIndex = months.size()-1;
            Toast.makeText(getContext(), "到尾了", Toast.LENGTH_SHORT).show();
        }else{
            monthIndex = index;
        }
        refreshMonth();
    }

    private void refreshMonth(){
        cal.setData(months.get(monthIndex));
        String month = months.get(monthIndex).getMonth();
        String[] months = month.split("-");
        int m = Integer.parseInt(months[1]);
        tv_month.setText(months[0]+"年"+(m<10?"0"+m:m)+"月");
    }



}
