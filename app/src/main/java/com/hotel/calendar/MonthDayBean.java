package com.hotel.calendar;

import java.util.ArrayList;

/**
 * author : ox
 * create at : 2017/3/22 10:36
 * project : CustomCalendar
 * version : 1.0
 * class describe：
 */
public class MonthDayBean {

    private String code;
    private ArrayList<MonthBean> datas;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<MonthBean> getDatas() {
        return datas;
    }

    public void setDatas(ArrayList<MonthBean> datas) {
        this.datas = datas;
    }

    public static class MonthBean {
        private String month;        //当前月
        private ArrayList<Day> days;   //日期数组

        public ArrayList<Day> getDays() {
            return days;
        }

        public void setDays(ArrayList<Day> days) {
            this.days = days;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }
    }

    public static class Day {

        /*{
        "date": "2017-3-1",
            "is_buy": "0",
            "is_select": "0",
            "num": "1",
            "price": 0,
            "set": "10",
            "week": "3"
    },*/
        private String date;    //日期
        private int is_buy;     //是否可买  1是0否
        private int is_select;  //是否已选择  1是0否
        private int num;        //日期号码
        private float price;    //价格
        private int set;        //剩余数量
        private int week;       //周几 0周日  1周一  6周六

        private STATUS  status;

        public enum STATUS{
            STATUS_BEFORE, //今天之前
            STATUS_OUT,    //已租满
            STATUS_CLICK,  //可以被点击
            STATUS_UNCLICK,  //不可以被点击
            STATUS_SELECT, //选中
        }

        public STATUS getStatus() {
            return status;
        }

        public void setStatus(STATUS status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "Day{" +
                    "date='" + date + '\'' +
                    ", is_buy=" + is_buy +
                    ", is_select=" + is_select +
                    ", num=" + num +
                    ", price=" + price +
                    ", set=" + set +
                    ", status=" + status +
                    ", week=" + week +
                    '}';
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getIs_buy() {
            return is_buy;
        }

        public void setIs_buy(int is_buy) {
            this.is_buy = is_buy;
        }

        public int getIs_select() {
            return is_select;
        }

        public void setIs_select(int is_select) {
            this.is_select = is_select;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public int getSet() {
            return set;
        }

        public void setSet(int set) {
            this.set = set;
        }

        public int getWeek() {
            return week;
        }

        public void setWeek(int week) {
            this.week = week;
        }
    }


}
