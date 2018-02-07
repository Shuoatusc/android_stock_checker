package com.ryanliu.hw9_v13;

import java.util.Comparator;

/**
 * Created by RyanLiu on 11/27/17.
 */

public class change_comparator implements Comparator<Stock> {
    @Override
    public int compare(Stock s1, Stock s2) {
        if(s1.change<s2.change) return -1;
        else if(s1.change>s2.change) return 1;
        else return 0;
    }
}
