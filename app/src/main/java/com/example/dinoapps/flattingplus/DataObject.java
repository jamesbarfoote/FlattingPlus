package com.example.dinoapps.flattingplus;

/**
 * Created by james on 2/28/2016.
 */
public class DataObject {
    private String Title;
    private String Content;
    private String Time;

    DataObject (String t, String c, String created){
        Title = t;
        Content = c;
        Time = created;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String mText1) {
        this.Title = mText1;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String mText2) {
        this.Content = mText2;
    }

    public String getTime() { return Time;}
}