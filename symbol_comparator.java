package com.ryanliu.hw9_v13;

import java.util.Comparator;

/**
 * Created by RyanLiu on 11/27/17.
 */

public class symbol_comparator implements Comparator<Stock> {
    @Override
    public int compare(Stock s1, Stock s2) {
        return s1.symbol.compareTo(s2.symbol);
    }
}
