package com.example.yuliiastelmakhovska.test;

import android.os.Parcel;

/**
 * Created by yuliiastelmakhovska on 2017-05-22.
 */

public class Video implements ContentType {
    String name;
    int subtitleDelay;

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
    public Video(){}

    public Video(Parcel in) {
        name = in.readString();
        subtitleDelay=in.readInt();
    }

    @Override
    public void setName(String name) {
this.name=name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setSubtitleDelay(int subtitleDelay) {
        this.subtitleDelay = subtitleDelay;
    }

    public int getSubtitleDelay() {
        return subtitleDelay;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(subtitleDelay);
    }
}
