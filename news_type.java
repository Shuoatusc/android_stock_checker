package com.ryanliu.hw9_v13;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by RyanLiu on 11/24/17.
 */

public class news_type {
            public String title_news;
            public String author_news;
            public String pubdate_news;
            public String url_news;

    public news_type() {

    }

    public news_type(JSONObject news_object) throws JSONException {
                title_news=news_object.getString("title");
                author_news=news_object.getString("author_name");
                pubdate_news=news_object.getString("pubDate");
                url_news=news_object.getString("link");
            }


}
