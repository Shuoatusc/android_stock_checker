package com.ryanliu.hw9_v13;

import android.content.Context;
import android.util.Log;

/**
 * Created by RyanLiu on 11/28/17.
 */

public class CustomJavaScriptInterface {
    Context mContext;

    /** Instantiate the interface and set the context */
    CustomJavaScriptInterface(Context c) {
        mContext = c;
    }


    /** retrieve the ids */
    public void getIds(final String myIds) {
            Log.d("interface",myIds);
        //Do somethings with the Ids

    }
}
