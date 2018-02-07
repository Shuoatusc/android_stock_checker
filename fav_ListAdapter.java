package com.ryanliu.hw9_v13;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by RyanLiu on 11/26/17.
 */

public class fav_ListAdapter extends ArrayAdapter<Stock> {
    private LayoutInflater mInflater;
    private ArrayList<Stock> input_stock;
    private int mViewResourceId;


    public fav_ListAdapter(@NonNull Context context, int textViewResourceId, ArrayList<Stock> input_stock) {
        super(context, textViewResourceId,input_stock);
        this.input_stock=input_stock;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId=textViewResourceId;
    }
    public View getView(int position, View convertView, ViewGroup parents){
        convertView = mInflater.inflate(mViewResourceId,null);
        Stock mystock = input_stock.get(position);
        if(input_stock!=null){
            TextView fsymbol = (TextView) convertView.findViewById(R.id.favList_symbol);
            TextView fprice  = (TextView) convertView.findViewById(R.id.favList_price);
            TextView fchange = (TextView) convertView.findViewById(R.id.favList_change);
            if(fsymbol!=null){
                fsymbol.setText(mystock.symbol);
            }
            if(fprice!=null){
                fprice.setText(String.valueOf(mystock.today_close));
            }
            if(fchange!=null){
                fchange.setText(String.valueOf(mystock.change)+"("+String.valueOf(mystock.change_percent)+"%)");
                if(mystock.change>0){
                    fchange.setTextColor(Color.GREEN);
                }
                else if(mystock.change<0){
                    fchange.setTextColor(Color.RED);
                }
            }
        }
    return convertView;
    }

}
