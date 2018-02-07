package com.ryanliu.hw9_v13;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.function.DoubleToLongFunction;

/**
 * Created by RyanLiu on 11/29/17.
 */

public class detail_table_Adapter extends ArrayAdapter<info_object> {
    private LayoutInflater mInflater;
    private ArrayList<info_object> minfo;
    private int mViewResourceId;

    public detail_table_Adapter(@NonNull Context context, int textViewResourceId, ArrayList<info_object> input_info) {
        super(context, textViewResourceId,input_info);
        this.minfo=input_info;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId=textViewResourceId;
    }
    public View getView(int position, View convertView, ViewGroup parents){
        convertView = mInflater.inflate(mViewResourceId,null);
        info_object myinfomation = minfo.get(position);
        if(myinfomation!=null){
            TextView dcateg = (TextView) convertView.findViewById(R.id.category_details);
            TextView dvalue  = (TextView) convertView.findViewById(R.id.value_details);
            ImageView dimg   = (ImageView)convertView.findViewById(R.id.image_details);
            TextView dbackup = (TextView) convertView.findViewById(R.id.value_backup);
            if(dcateg!=null){
                dcateg.setText(myinfomation.category);
            }
            if(dvalue!=null){
                dvalue.setText(myinfomation.value);
            }
            if(dimg!=null){
                if(myinfomation.category=="Change"){
                    if(myinfomation.change>0){
                        dvalue.setVisibility(View.GONE);
                        dbackup.setText(myinfomation.value);
                        dbackup.setVisibility(View.VISIBLE);
                        dimg.setImageResource(R.drawable.up);
                    }
                    else if(myinfomation.change<0){
                        dvalue.setVisibility(View.GONE);
                        dbackup.setText(myinfomation.value);
                        dbackup.setVisibility(View.VISIBLE);
                        dimg.setImageResource(R.drawable.down);
                    }
                }
            }
        }
        return convertView;
    }



}
