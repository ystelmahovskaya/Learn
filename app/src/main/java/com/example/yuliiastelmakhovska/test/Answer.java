package com.example.yuliiastelmakhovska.test;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by yuliiastelmakhovska on 2017-05-23.
 */

public class Answer implements Parcelable,Serializable {

    int number;
    String text;
    public  Answer(int number, String text){
        this.number=number;
        this.text=text;
    }
    public Answer(Parcel in) {
        number = in.readInt();
        text=in.readString();

    }

    public Answer(){}
    public static final Creator<Answer> CREATOR = new Creator<Answer>() {
        @Override
        public Answer createFromParcel(Parcel in) {
            return new Answer(in);
        }

        @Override
        public Answer[] newArray(int size) {
            return new Answer[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    public int getNumber() {
        return number;
    }

    public String getText() {
        return text;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
dest.writeInt(getNumber());
        dest.writeString(getText());
    }
}
