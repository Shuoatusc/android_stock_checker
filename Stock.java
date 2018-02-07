package com.ryanliu.hw9_v13;

import android.util.Log;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;
import java.text.DecimalFormat;
/**
 * Created by RyanLiu on 11/22/17.
 */

public class Stock {
    public String symbol;
    public String today_date;
    public double last_price;
    public String previous_date;
    public int volume_today;
    public double today_open;
    public double today_close;
    public double previous_close;
    public String timestamp;
    public double today_high;
    public double today_low;
    public double change;
    public double change_percent;
    public String days_range;
    public double close;
    private static final String TAG ="Stock_class";


    public Stock(){

    } //constructor#1
    public Stock(JSONObject jobject){

        try {
            symbol=jobject.getJSONObject("Meta Data").getString("2. Symbol");
            //today_date=jobject.getJSONObject("Meta Data").getString("3. Last Refreshed");
            JSONObject timeobject =  jobject.getJSONObject("Time Series (Daily)");
            today_date=timeobject.names().getString(0);
            previous_date = timeobject.names().getString(1);
            volume_today=Integer.parseInt(jobject.getJSONObject("Time Series (Daily)").getJSONObject(today_date).getString("5. volume"));
            today_open=Double.parseDouble(jobject.getJSONObject("Time Series (Daily)").getJSONObject(today_date).getString("1. open"));
            today_close=Double.parseDouble(jobject.getJSONObject("Time Series (Daily)").getJSONObject(today_date).getString("4. close"));
            today_high=Double.parseDouble(jobject.getJSONObject("Time Series (Daily)").getJSONObject(today_date).getString("2. high"));
            today_low=Double.parseDouble(jobject.getJSONObject("Time Series (Daily)").getJSONObject(today_date).getString("3. low"));
            last_price=today_close;
            previous_close=Double.parseDouble(jobject.getJSONObject("Time Series (Daily)").getJSONObject(previous_date).getString("4. close"));
            DecimalFormat df = new DecimalFormat("####0.00");
            change=Double.parseDouble(df.format(today_close-previous_close));
            change_percent=change*100.00/previous_close;
            change_percent=Double.parseDouble(df.format(change_percent));
            SimpleDateFormat dateFormat_open = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat dateFormat_close = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat_open.setTimeZone(TimeZone.getTimeZone("EST"));
            dateFormat_close.setTimeZone(TimeZone.getTimeZone("EST"));
            Date current_time = new Date();
            int from = 630;
            int to = 1300;
            Calendar c= Calendar.getInstance();
            c.setTime(current_time);
            int t=c.get(Calendar.HOUR_OF_DAY)*100+c.get(Calendar.MINUTE);
            if(t>=from&&t<=to){//market still open
                timestamp=dateFormat_open.format(current_time)+" EST";
                close=previous_close;
            }
            else{//market closed
                timestamp=dateFormat_close.format(current_time)+" 16:00:00 EST";
                close=today_close;
            }
            days_range=""+today_low+" - "+today_high;
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }


    }//constructor#2

    public Stock(String symbol,Double price,Double change,Double change_percent) {
        this.symbol = symbol;
        this.today_close=price;
        this.change = change;
        this.change_percent=change_percent;
    }
}//class stock
