package com.ryanliu.hw9_v13;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by RyanLiu on 11/24/17.
 */

public class news extends Fragment {
    private static final String TAG ="newsActivity";

    public news() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG,"news");

        View view = inflater.inflate(R.layout.fragment_news, container, false);



        return view;

    }



}
