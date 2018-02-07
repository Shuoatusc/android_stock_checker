package com.ryanliu.hw9_v13;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RyanLiu on 11/24/17.
 */

class news_listAdapter extends BaseAdapter{
        private static ArrayList<news_type> newsList;
        private LayoutInflater mInflater;

    public news_listAdapter(Context context, ArrayList<news_type> results) {
        newsList=results;

        mInflater = LayoutInflater.from(context);
    }
    public int getCount() {
        return newsList.size();
    }
    public Object getItem(int position) {
        return newsList.get(position);
    }
    public long getItemId(int position) {
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.news_list, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.news_title);
            holder.author = (TextView) convertView.findViewById(R.id.news_author);
            holder.pubdate = (TextView) convertView.findViewById(R.id.news_date);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(newsList.get(position).title_news);
        holder.author.setText("Author: "+newsList.get(position).author_news);
        holder.pubdate.setText("Date: "+newsList.get(position).pubdate_news);
        return convertView;
    }

    static class ViewHolder {
        TextView title;
        TextView author;
        TextView pubdate;
    }
}
