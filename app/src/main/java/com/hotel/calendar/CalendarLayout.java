package com.hotel.calendar;

import android.content.Context;
import android.graphics.PointF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
public class CalendarLayout extends RelativeLayout {

    private String TAG = "CalendarLayout";

    private LinearLayout ll_top, ll_left,  ll_right, ll_pop_start, ll_pop_end;
    private TextView tv_month;
    private ImageView iv_start_cancel, iv_end_cancel;
    private CustomCalendar cal;

    private int topHeight;
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
        ll_top = (LinearLayout) findViewById(R.id.ll_top);
        ll_left = (LinearLayout) findViewById(R.id.ll_left);
        ll_right = (LinearLayout) findViewById(R.id.ll_right);
        ll_pop_start = (LinearLayout) findViewById(R.id.ll_pop_start);
        ll_pop_end = (LinearLayout) findViewById(R.id.ll_pop_end);
        tv_month = (TextView) findViewById(R.id.tv_month);
        iv_start_cancel = (ImageView) findViewById(R.id.iv_start_cancel);
        iv_end_cancel = (ImageView) findViewById(R.id.iv_end_cancel);
        cal = (CustomCalendar) findViewById(R.id.cal);
        cal.setParentLayout(this);

        ll_pop_start.setVisibility(View.GONE);
        ll_pop_end.setVisibility(View.GONE);

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
        iv_start_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.setEmpty(true);
                ll_pop_start.setVisibility(View.GONE);
                ll_pop_end.setVisibility(View.GONE);
            }
        });
        iv_end_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.setEmpty(false);
                ll_pop_end.setVisibility(View.GONE);
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
        //切换月份时，需要隐藏气泡
        ll_pop_start.setVisibility(View.GONE);
        ll_pop_end.setVisibility(View.GONE);
        cal.setData(months.get(monthIndex));
        String month = months.get(monthIndex).getMonth();
        String[] months = month.split("-");
        int m = Integer.parseInt(months[1]);
        tv_month.setText(months[0]+"年"+(m<10?"0"+m:m)+"月");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        topHeight = ll_top.getHeight();
        Log.v(TAG, "layoutsize变化了topHeight="+topHeight);
    }

    /**
     * 显示气泡
     * @param point item的坐标（相对于日历），x:item中心点   y：item的top
     * @param type
     * @param changeLine 换行
     */
    public void showPop(PointF point, int type, boolean changeLine){
        if(type==1){
            ll_pop_start.setVisibility(View.VISIBLE);
            Log.v(TAG, "顶点坐标："+point.x+"  *  "+point.y);
            ll_pop_start.measure(0,0);
            int popH = ll_pop_start.getMeasuredHeight();
            int popW = ll_pop_start.getMeasuredWidth();
            Log.v(TAG, "cal在layout坐标："+cal.getX()+"  *  "+cal.getY());
            Log.v(TAG, "pop宽高："+popW+"  *  "+popH);
            int x = (int)cal.getX() + (int)(point.x-popW/2);
            int y = (int)cal.getY() + (int)(point.y-popH);
            Log.v(TAG, "显示开始气泡："+x+"  *  "+y);
            ll_pop_start.setX(x);
            ll_pop_start.setY(y-CalendarUtil.dip2px(getContext(),3));
        }else if(type == 2){
            ll_pop_end.setVisibility(View.VISIBLE);
            ll_pop_end.measure(0,0);
            int popH = ll_pop_end.getMeasuredHeight();
            int popW = ll_pop_end.getMeasuredWidth();
            int x = (int)cal.getX() + (int)(point.x-popW/2);
            if(changeLine){
                int y = (int)cal.getY() + (int)(point.y);
                ll_pop_end.setX(x);
                ll_pop_end.setY(y+CalendarUtil.dip2px(getContext(),3));
            }else{
                int y = (int)cal.getY() + (int)(point.y-popH);
                ll_pop_end.setX(x);
                ll_pop_end.setY(y-CalendarUtil.dip2px(getContext(),3));
            }
        }

    }

}
