##1.图片替换：
>
    修改月份左右箭头：
    res/drawable-xhdpi:
    custom_calendar_row_left.png
    custom_calendar_row_right.png
    修改已租、选中、不可选三种状态下背景
    res/drawable-xhdpi:
    bg_out.9.png          //已租
    bg_select.9.png       //选中
    bg_unclick.9.png      //不可选

    入住日期、离店日期气泡背景：
    res/layout/calendar_layout.xml中修改ll_pop_start&calendar_shape_pop的background属性：
    calendar_shape_pop.xml
    取消选择的红叉叉图片：
    icon_calendar_cancel.png

##2. 修改日历上文字大小、文字颜色、间隔值等请参考:
com.hotel.calendar.CustomCalendar类中的：
```Java
    /**星期栏字体颜色设置*/
    private int tc_week = Color.parseColor("#9e9e9d");
    /**日期文字各种状态下的文字颜色*/
    private int tc_day_before= Color.parseColor("#a9a9a9");    //过期(今天之前)
    private int tc_day_out= Color.parseColor("#808080");       //已租
    private int tc_day_click = Color.parseColor("#000000");    //可点击
    private int tc_day_unclick = Color.parseColor("#696969");  //不可点击
    private int tc_day_select = Color.parseColor("#ffffff");   //选中
    /**价格各种状态下文字颜色*/
    private int tc_money_out= Color.parseColor("#808080");       //已满
    private int tc_money_click = Color.parseColor("#000000");      //可点击
    private int tc_money_unclick = Color.parseColor("#696969");  //不可点击
    private int tc_money_select = Color.parseColor("#ffffff");     //选中
    /**字体大小设置*/
    private float ts_week = 13;   //星期栏字体大小
    private float ts_day = 12;    //日期字体大小
    private float ts_money = 10;  //价格字体大小

    /**间距设置dip单位*/
    private int weekSpac = 8;   //星期栏下面的空隙
    private int lineSpac = 3;   //日历行间距
    private int lieSpac = 3;    //日历列间距
    private int textSpac = 0;   //日期与价格之间的距离
    private int textPad = 3;    //日历item中字体与背景上下的间隙
```

##3. 修改日历与屏幕左右的间隔：
res/layout/calendar_layout.xml中找到下面的代码，然后修改layout_marginLeft & layout_marginRight：
```xml
<com.hotel.calendar.CustomCalendar
        android:id="@+id/cal"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginLeft="20dp"
        android:layout_marginRight="20dp"
        android:layout_below="@+id/ll_top"/>
```

