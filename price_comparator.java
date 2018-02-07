package com.ryanliu.hw9_v13;

import java.util.Comparator;

/**
 * Created by RyanLiu on 11/27/17.
 */

public class price_comparator implements Comparator<Stock> {
    @Override
    public int compare(Stock s1, Stock s2) {
        if(s1.today_close<s2.today_close) return -1;
        else if(s1.today_close>s2.today_close) return 1;
        else return 0;
    }

}
