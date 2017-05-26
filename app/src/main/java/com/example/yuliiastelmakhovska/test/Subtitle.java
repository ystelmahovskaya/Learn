package com.example.yuliiastelmakhovska.test;

/**
 * Created by yuliiastelmakhovska on 2017-05-21.
 */

public class Subtitle {
    int startTime;
    int endTime;
    String text;
    public Subtitle(int startTime,int endTime,String text ){
        this.startTime=startTime;
        this.endTime=endTime;
        this.text=text;
    }
    public Subtitle(){}

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return String.valueOf(startTime)+text;
    }
}
