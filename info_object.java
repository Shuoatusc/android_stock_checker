package com.ryanliu.hw9_v13;

/**
 * Created by RyanLiu on 11/29/17.
 */

public class info_object {
    public String category;
    public String value;
    public Double change;
    public info_object() {

    }
    public info_object(String input_category,String input_value){
        category=input_category;
        value=input_value;
    }
    public void setChange(Double input_change){
        change=input_change;
    }
}
