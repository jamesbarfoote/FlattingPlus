package com.example.dinoapps.flattingplus;

/**
 * Created by james on 2/28/2016.
 */
public class DataObject {
    private String Title;
    private String Content;

    DataObject (String text1, String text2){
        Title = text1;
        Content = text2;
    }

    public String getmText1() {
        return Title;
    }

    public void setmText1(String mText1) {
        this.Title = mText1;
    }

    public String getmText2() {
        return Content;
    }

    public void setmText2(String mText2) {
        this.Content = mText2;
    }
}