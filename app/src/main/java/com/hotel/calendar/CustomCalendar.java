package com.hotel.calendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import static com.openxu.calendar.MonthDayBean.Day.STATUS.STATUS_BEFORE;
import static com.openxu.calendar.MonthDayBean.Day.STATUS.STATUS_CLICK;
import static com.openxu.calendar.MonthDayBean.Day.STATUS.STATUS_OUT;
import static com.openxu.calendar.MonthDayBean.Day.STATUS.STATUS_SELECT;
import static com.openxu.calendar.MonthDayBean.Day.STATUS.STATUS_UNCLICK;

/**
 * autour: ox
 * date: 2017/3/22 9:41
 * className: CustomCalendar
 * version:
 * description: 自定义日历控件
 */
public class CustomCalendar extends View{

    private String TAG = "CustomCalendar";

    /*颜色设置*/
    private int tc_week = Color.parseColor("#9e9e9d");

    private int tc_day_before= Color.parseColor("#a9a9a9");   //过期

    private int tc_day_out= Color.parseColor("#808080");      //已满
    private int tc_day_click = Color.parseColor("#000000");     //可点击
    private int tc_day_unclick = Color.parseColor("#696969");     //不可点击
    private int tc_day_select = Color.parseColor("#ffffff");     //选中

    private int tc_money_out= Color.parseColor("#808080");       //已满
    private int tc_money_click = Color.parseColor("#000000");      //可点击
    private int tc_money_unclick = Color.parseColor("#696969");  //不可点击
    private int tc_money_select = Color.parseColor("#ffffff");     //选中

    /*字体大小设置*/
    private float ts_week = 13;
    private float ts_day = 12;
    private float ts_money = 10;
    /*间距设置*/
    private int weekSpac = 8;  //星期下面间距
    private int lineSpac = 3;  //行间距
    private int lieSpac = 3;    //列间距
    private int textSpac = 0;   //字体上下间距
    private int textPad = 3;    //item上下pad

    private Paint mPaint;
    private Paint bgPaint;

    /*计算*/
    private int columnWidth;       //每列宽度
    private int weekHeight, dayHeight, moneyHeight, oneHeight;

    private Date monthDate; //当前的月份
    private boolean isCurrentMonth;       //展示的月份是否是当前月
    private Date today;
    private int currentDay;
    private String startDay, endDay;

    private int dayOfMonth;    //月份天数
    private int firstIndex;    //当月第一天位置索引
    private int todayWeekIndex;//今天是星期几
    private int firstLineNum, lastLineNum; //第一行、最后一行能展示多少日期
    private int lineNum;      //日期行数
    private String[] WEEK_STR = new String[]{"日", "一", "二", "三", "四", "五", "六", };


    public CustomCalendar(Context context) {
        this(context, null);
    }
    public CustomCalendar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public CustomCalendar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        ts_week = CalendarUtil.sp2px(getContext(), ts_week);
        ts_day = CalendarUtil.sp2px(getContext(), ts_day);
        ts_money = CalendarUtil.sp2px(getContext(), ts_money);

        weekSpac = CalendarUtil.dip2px(getContext(), weekSpac);
        lineSpac = CalendarUtil.dip2px(getContext(), lineSpac);
        lieSpac = CalendarUtil.dip2px(getContext(), lieSpac);
        textSpac = CalendarUtil.dip2px(getContext(), textSpac);
        textPad = CalendarUtil.dip2px(getContext(), textPad);

        initCompute();

    }
    private void initCompute(){
        mPaint = new Paint();
        bgPaint = new Paint();
        mPaint.setAntiAlias(true); //抗锯齿
        bgPaint.setAntiAlias(true); //抗锯齿

        mPaint.setTextSize(ts_week);
        weekHeight = (int)CalendarUtil.getFontHeight(mPaint) + weekSpac;
        mPaint.setTextSize(ts_day);
        dayHeight = (int)CalendarUtil.getFontHeight(mPaint);
        mPaint.setTextSize(ts_money);
        moneyHeight =(int) CalendarUtil.getFontHeight(mPaint);
        //每行高度
        oneHeight = textPad*2 + dayHeight + textSpac + moneyHeight;
        Log.w(TAG, "weekHeight="+weekHeight+"  dayHeight="+dayHeight+"   moneyHeight="+moneyHeight+" oneHeight="+oneHeight);
        //默认当前月份
        String cDateStr = CalendarUtil.getMonthStr(new Date());

//        setMonth(cDateStr);
    }

    /**设置月份*/
    private void setMonth(String Month){
        //设置的月份（2017年01月）
        monthDate = CalendarUtil.str2Date(Month, CalendarUtil.FROMAT_MONTH);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        today = CalendarUtil.getDayDate(new Date());
        //获取今天是多少号
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        todayWeekIndex = calendar.get(Calendar.DAY_OF_WEEK)-1;

        Date cM = CalendarUtil.str2Date(CalendarUtil.getMonthStr(new Date()), CalendarUtil.FROMAT_MONTH);
        //判断是否为当月
        if(cM.getTime() == monthDate.getTime()){
            isCurrentMonth = true;
        }else{
            isCurrentMonth = false;
        }
        Log.d(TAG, "设置月份："+monthDate+"   今天"+currentDay+"号, 是否为当前月："+isCurrentMonth);
        calendar.setTime(monthDate);
        dayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        //第一行1号显示在什么位置（星期几）
        firstIndex = calendar.get(Calendar.DAY_OF_WEEK)-1;
        lineNum = 1;
        //第一行能展示的天数
        firstLineNum = 7-firstIndex;
        lastLineNum = 0;
        int shengyu = dayOfMonth - firstLineNum;
        while (shengyu>7){
            lineNum ++;
            shengyu-=7;
        }
        if(shengyu>0){
            lineNum ++;
            lastLineNum = shengyu;
        }
        Log.i(TAG, CalendarUtil.getMonthStr(monthDate)+"一共有"+dayOfMonth+"天,第一天的索引是："+firstIndex+"   有"+lineNum+
                "行，第一行"+firstLineNum+"个，最后一行"+lastLineNum+"个");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //宽度 = 填充父窗体
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);   //获取宽的尺寸
        columnWidth = (widthSize - 6*lieSpac) / 7;
        //高度 = 标题高度 + 星期高度 + 日期行数*每行高度
        float height = weekHeight + (lineNum * oneHeight) + (lineNum-1)*lineSpac;
        Log.v(TAG, " 星期高度："+weekHeight+" 每行高度："+oneHeight+
                " 行数："+ lineNum + "  \n控件高度："+height);
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                (int)height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(map==null || map.size()<=0)
            return;
        drawWeek(canvas);
        drawDayAndPeice(canvas);
    }

    /**绘制绘制星期*/
    private void drawWeek(Canvas canvas){
        //背景
      /*  bgPaint.setColor(Color.RED);
        RectF rect = new RectF(0, 0, getWidth(), weekHeight);
        canvas.drawRect(rect, bgPaint);*/
        //绘制星期
        mPaint.setTextSize(ts_week);
        mPaint.setColor(tc_week);
        for(int i = 0; i < WEEK_STR.length; i++){
            int len = (int)CalendarUtil.getFontlength(mPaint, WEEK_STR[i]);
            int x = i * (columnWidth+lieSpac) + (columnWidth - len)/2;
            canvas.drawText(WEEK_STR[i], x, CalendarUtil.getFontLeading(mPaint), mPaint);
        }
    }

    private void drawDayAndPeice(Canvas canvas){
        //某行开始绘制的Y坐标
        int top = weekHeight;
        //行
        for(int line = 0; line < lineNum; line++){
            if(line == 0){
                //第一行
                drawDayAndPre(canvas, top, firstLineNum, 0, firstIndex);
            }else if(line == lineNum-1){
                //最后一行
                top += (oneHeight+lineSpac);
                drawDayAndPre(canvas, top, lastLineNum, firstLineNum+(line-1)*7, 0);
            }else{
                //满行
                top += (oneHeight+lineSpac);
                drawDayAndPre(canvas, top, 7, firstLineNum+(line-1)*7, 0);
            }
        }
    }

    /**
     * 绘制某一行的日期
     * @param canvas
     * @param top 顶部坐标
     * @param count 此行需要绘制的日期数量（不一定都是7天）
     * @param overDay 已经绘制过的日期，从overDay+1开始绘制
     * @param startIndex 此行第一个日期的星期索引
     */
    private void drawDayAndPre(Canvas canvas, int top,
                               int count, int overDay, int startIndex){
//        Log.e(TAG, "总共"+dayOfMonth+"天  有"+lineNum+"行"+ "  已经画了"+overDay+"天,下面绘制："+count+"天");
        int topDay = top + textPad;
        int topMoney = topDay + dayHeight +textSpac;
//        Log.e(TAG, "top="+top+"  textPad="+textPad+"  dayHeight="+dayHeight+"  textSpac="+textSpac);
        Rect rect;
     /*   bgPaint.setColor(Color.CYAN);
        rect = new Rect(0, topDay, getWidth(), topDay+dayHeight);
        canvas.drawRect(rect, bgPaint);

        bgPaint.setColor(Color.BLUE);
        rect = new Rect(0, topMoney, getWidth(), topMoney + moneyHeight + textPad);
        canvas.drawRect(rect, bgPaint);*/

        mPaint.setTextSize(ts_day);
        float dayTextLeading = CalendarUtil.getFontLeading(mPaint);
        mPaint.setTextSize(ts_money);
        float moneyTextLeading = CalendarUtil.getFontLeading(mPaint);

        Bitmap bmp_out = BitmapFactory.decodeResource(getResources(),
                R.drawable.bg_out);
        Bitmap bmp_select = BitmapFactory.decodeResource(getResources(),
                R.drawable.bg_select);
        Bitmap bmp_unclick = BitmapFactory.decodeResource(getResources(),
                R.drawable.bg_unclick);

//        Log.v(TAG, "当前日期："+currentDay+"   选择日期："+selectDay+"  是否为当前月："+isCurrentMonth);
        for(int i = 0; i<count; i++){
            int left = (startIndex + i)*(columnWidth+lieSpac);
            int day = (overDay+i+1);
            MonthDayBean.Day dayDate = map.get(day);
            //背景
            //创建一个ninePatch的对象实例，第一个参数是bitmap、第二个参数是byte[]，这里其实要求我们传入
            //如何处理拉伸方式，当然我们不需要自己传入，因为“.9.png”图片自身有这些信息数据，
            //也就是我们用“9妹”工具操作的信息！ 我们直接用“.9.png”图片自身的数据调用getNinePatchChunk()即可
            //第三个参数是图片源的名称，这个参数为可选参数，直接null~就OK~
            String textStr = day+"";
            String moneyStr = "¥"+map.get(day).getPrice();
            switch (dayDate.getStatus()){
                case STATUS_BEFORE:
                    //只需要显示日期
                    mPaint.setTextSize(ts_day);
                    mPaint.setColor(tc_day_before);
                    int len = (int)CalendarUtil.getFontlength(mPaint, textStr);
                    int x = left + (columnWidth - len)/2;
                    canvas.drawText(textStr, x, top+(oneHeight-dayHeight)/2 + dayTextLeading, mPaint);

                    break;
                case STATUS_OUT:
                    //已经组满
                    NinePatch np = new NinePatch(bmp_out, bmp_out.getNinePatchChunk(), null);
                    rect = new Rect(left, top, left+columnWidth, top+oneHeight);
                    np.draw(canvas, rect);

                    drawDayAndMoney(canvas, left, topDay, topMoney,
                            tc_day_out, tc_money_out, dayTextLeading, moneyTextLeading,
                            textStr, "已租");
                    break;
                case STATUS_CLICK:
                    //可以点击，无背景
                    if(isCurrentMonth && currentDay == day){
                        textStr = "今天";
                    }
                    if(dayDate.getIs_buy()==0){
                        moneyStr = "已租";
                    }
                    drawDayAndMoney(canvas, left, topDay, topMoney, tc_day_click, tc_money_click,
                            dayTextLeading, moneyTextLeading,
                            textStr, moneyStr);
                    break;
                case STATUS_UNCLICK:
                    np = new NinePatch(bmp_unclick, bmp_unclick.getNinePatchChunk(), null);
                    rect = new Rect(left, top, left+columnWidth, top+oneHeight);
                    np.draw(canvas, rect);

                    if(dayDate.getIs_buy()==0){
                        moneyStr = "已租";
                    }
                    drawDayAndMoney(canvas, left, topDay, topMoney,tc_day_unclick, tc_money_unclick,
                            dayTextLeading, moneyTextLeading,
                            textStr, moneyStr);
                    break;
                case STATUS_SELECT:
                    np = new NinePatch(bmp_select, bmp_select.getNinePatchChunk(), null);
                    rect = new Rect(left, top, left+columnWidth, top+oneHeight);
                    np.draw(canvas, rect);

                    if(dayDate.getIs_buy()==0){
                        moneyStr = "已租";
                    }
                    drawDayAndMoney(canvas, left, topDay, topMoney,
                            tc_day_select, tc_money_select,
                            dayTextLeading, moneyTextLeading,
                            textStr, moneyStr);

                    break;
            }

        }
    }

    private void drawDayAndMoney(Canvas canvas, int left,
                                 int topDay, int topMoney,
                                 int dayColor, int moneyColor,
                                 float dayTextLeading, float moneyTextLeading,
                                 String dayStr, String moneyStr){
        mPaint.setTextSize(ts_day);
        mPaint.setColor(dayColor);
        int len = (int)CalendarUtil.getFontlength(mPaint, dayStr);
        int x = left + (columnWidth - len)/2;
        canvas.drawText(dayStr, x, topDay + dayTextLeading, mPaint);

        mPaint.setTextSize(ts_money);
        mPaint.setColor(moneyColor);
        len = (int)CalendarUtil.getFontlength(mPaint, moneyStr);
        x = left + (columnWidth - len)/2;
        canvas.drawText(moneyStr, x, topMoney + moneyTextLeading, mPaint);
    }


    private MonthDayBean.MonthBean monthBean;
    private Map<Integer, MonthDayBean.Day> map;
    public void setData(MonthDayBean.MonthBean monthBean){
        this.monthBean = monthBean;
        setMonth(monthBean.getMonth());
        map = new TreeMap<>(
                new Comparator<Integer>() {
                    public int compare(Integer obj1, Integer obj2) {
                        return obj1.compareTo(obj2);
                    }
                });
        for(MonthDayBean.Day day : monthBean.getDays()){
            map.put(day.getNum(), day);
        }

        refreshStstus();
        requestLayout();
        invalidate();
    }

    private void refreshStstus(){
        Log.e(TAG, "刷新数据");
        Iterator<Integer> keyI = map.keySet().iterator();
        if(TextUtils.isEmpty(startDay)){
            Log.w(TAG, "未选中日期");
            while (keyI.hasNext()){
                MonthDayBean.Day day =map.get(keyI.next());
                Date date = CalendarUtil.getDayDate(day.getDate());
                if(date.getTime() < today.getTime()){
                    //过期
                    day.setStatus(STATUS_BEFORE);
                    Log.w(TAG, day.getDate()+"过期");
                }else{
                    //未过期is_buy;     //是否可买  1是0否
                    if(day.getIs_buy() == 1){
                        day.setStatus(STATUS_CLICK);
                        Log.i(TAG, day.getDate()+"可点击"+day.getIs_buy());
                    }else{
                        day.setStatus(STATUS_OUT);
                        Log.w(TAG, day.getDate()+"租满了"+day.getIs_buy());
                    }
                }
            }
        }else{
            Date startDate = CalendarUtil.getDayDate(startDay);
            //已经选中了开始日期
            if(TextUtils.isEmpty(endDay)){
                boolean hasOut = false;  //是否有排满的
                //未选中结束日期
                while (keyI.hasNext()){
                    MonthDayBean.Day day =map.get(keyI.next());
                    Date date = CalendarUtil.getDayDate(day.getDate());
                    if(date.getTime() < today.getTime()){
                        //过期
                        day.setStatus(STATUS_BEFORE);
                        Log.w(TAG, day.getDate()+"过期");
                    }else{
                        if(date.getTime() < startDate.getTime()){
                            //未过期，但是在开始日期之前，不能点击
                            day.setStatus(STATUS_UNCLICK);
                            Log.w(TAG, day.getDate()+"在开始日期之前，不能点击");
                        }else if(startDate.getTime() == date.getTime()){
                            day.setStatus(STATUS_SELECT);
                            Log.e(TAG, day.getDate()+"是开始日期，选中");
                        }else{
                            //开始日期之后的，如果没有排满就可点击
                            if(!hasOut){
                                //未过期is_buy;     //是否可买  1是0否
                                if(day.getIs_buy() == 1){
                                    day.setStatus(STATUS_CLICK);
                                    Log.i(TAG, day.getDate()+"在开始日期之后，可以点击"+day.getIs_buy());
                                }else{
                                    day.setStatus(STATUS_CLICK);
                                    Log.i(TAG, day.getDate()+"在开始日期之后，可以点击"+day.getIs_buy());
                                    hasOut = true;
                                }
                            }else{
                                day.setStatus(STATUS_UNCLICK);
                                Log.w(TAG, day.getDate()+"在开始日期之后，但是之前有排满的，不可以点击"+day.getIs_buy());
                            }
                        }
                    }
                }
            }else{
                Date endDate = CalendarUtil.getDayDate(endDay);
                while (keyI.hasNext()){
                    MonthDayBean.Day day =map.get(keyI.next());
                    Date date = CalendarUtil.getDayDate(day.getDate());
                    if(date.getTime() < today.getTime()){
                        //过期
                        day.setStatus(STATUS_BEFORE);
                        Log.w(TAG, day.getDate()+"过期");
                    }else{
                        if(date.getTime() < startDate.getTime()){
                            //未过期，但是在开始日期之前，不能点击
                            day.setStatus(STATUS_UNCLICK);
                            Log.w(TAG, day.getDate()+"在开始日期之前，不能点击");
                        }else if(startDate.getTime() == date.getTime() || date.getTime() < endDate.getTime()){
                            Log.e(TAG, day.getDate()+"在选中区域，选中");
                            day.setStatus(STATUS_SELECT);
                        }else if(date.getTime() == endDate.getTime()) {
                            Log.e(TAG, day.getDate()+"为结束日期，选中");
                            day.setStatus(STATUS_SELECT);
                        }else{
                            day.setStatus(STATUS_UNCLICK);
                            Log.w(TAG, day.getDate()+"不能点击");
                        }
                    }
                }
            }
        }
    }



    /****************************事件处理↓↓↓↓↓↓↓****************************/
    //焦点坐标
    private PointF startPoint = new PointF();
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startPoint.set(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if(Math.abs(startPoint.x - event.getX())>30 || Math.abs(startPoint.y - event.getY())>30){
                    Log.e(TAG, "不是点击哦");
                }else{
                    if(event.getY()>weekHeight){
                        touchDay(event);
                    }else{
                        Log.e(TAG, "点击星期");
                    }
                }
                break;
        }
        return true;
    }


    /**事件点在 日期区域 范围内*/
    private void touchDay(MotionEvent event){
        //根据Y坐标找到焦点行
        boolean availability = false;  //事件是否有效
        //日期部分
        float top = weekHeight+oneHeight;
        int foucsLine = 1;
        while(foucsLine<=lineNum){
            if(top>=event.getY()){
                availability = true;
                break;
            }
            top += oneHeight;
            foucsLine ++;
        }
        if(availability){
            //根据X坐标找到具体的焦点日期
            int xIndex = (int)event.getX() / (columnWidth+lieSpac);
            if((event.getX() / columnWidth-xIndex)>0){
                xIndex += 1;
            }
//            Log.e(TAG, "列宽："+columnWidth+"  x坐标余数："+(point.x / columnWidth));
            if(xIndex<=0)
                xIndex = 1;   //避免调到上一行最后一个日期
            if(xIndex>7)
                xIndex = 7;   //避免调到下一行第一个日期
//            Log.e(TAG, "事件在日期部分，第"+foucsLine+"/"+lineNum+"行, "+xIndex+"列");
            if(foucsLine == 1){
                //第一行
                if(xIndex<=firstIndex){
                    Log.e(TAG, "点到开始空位了");
                }else{
                    setSelectedDay(xIndex-firstIndex);
                }
            }else if(foucsLine == lineNum){
                //最后一行
                if(xIndex>lastLineNum){
                    Log.e(TAG, "点到结尾空位了");
                }else{
                    setSelectedDay(firstLineNum + (foucsLine-2)*7+ xIndex);
                }
            }else{
                setSelectedDay(firstLineNum + (foucsLine-2)*7+ xIndex);
            }
        }else{
            //超出日期区域后，视为事件结束，响应最后一个选择日期的回调
        }
    }

    /**设置选中的日期*/
    private void setSelectedDay(int day){
        Log.w(TAG, "选中："+day);

        MonthDayBean.Day.STATUS status = map.get(day).getStatus();
        switch (status){
            case STATUS_BEFORE:
                Log.e(TAG, "点击过期日");
                break;
            case STATUS_OUT:
                Log.e(TAG, "点击租满");
                break;
            case STATUS_CLICK:
                Log.e(TAG, "点击有效");
                if(TextUtils.isEmpty(startDay)){
                    startDay = map.get(day).getDate();
                }else if(TextUtils.isEmpty(endDay)){
                    endDay = map.get(day).getDate();
                }
                refreshStstus();
                invalidate();
                break;
            case STATUS_UNCLICK:
                Log.e(TAG, "点击不能点击的");
                break;
            case STATUS_SELECT:
                Log.e(TAG, "点击已经选择的");
                break;
        }

    }


}
