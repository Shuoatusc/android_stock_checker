package com.ryanliu.hw9_v13;

import java.util.Comparator;

/**
 * Created by RyanLiu on 11/27/17.
 */

public class change_percent_comparator implements Comparator<Stock> {
    @Override
    public int compare(Stock s1, Stock s2) {
        if(s1.change_percent<s2.change_percent) return -1;
        else if(s1.change_percent>s2.change_percent) return 1;
        else return 0;
    }

}
