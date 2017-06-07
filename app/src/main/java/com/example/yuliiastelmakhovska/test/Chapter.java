package com.example.yuliiastelmakhovska.test;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by yuliiastelmakhovska on 2017-05-12.
 */

public class Chapter implements Parcelable {
    int level;
    String name;
    String description;
    int imageSourse;
    ArrayList<ContentType> list;
    public Chapter(){}
    public Chapter(int level,String name, String description,int imageSourse,ArrayList<ContentType> list){
        this.level=level;
        this.name=name;
        this.description=description;
        this.imageSourse=imageSourse;
        this.list=list;
    }
    public Chapter(Parcel in) {
        level = in.readInt();
        name = in.readString();
        description = in.readString();
        imageSourse = in.readInt();
        list= (ArrayList<ContentType>) in.readSerializable();

    }

    public static final Creator<Chapter> CREATOR = new Creator<Chapter>() {
        @Override
        public Chapter createFromParcel(Parcel in) {
            return new Chapter(in);
        }

        @Override
        public Chapter[] newArray(int size) {
            return new Chapter[size];
        }
    };

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getImageSourse() {
        return imageSourse;
    }

    public ArrayList<ContentType> getList() {
        return list;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageSourse(int imageSourse) {
        this.imageSourse = imageSourse;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setList(ArrayList<ContentType> list) {
        this.list = list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getLevel());
        dest.writeString(getName());
        dest.writeString(getDescription());
        dest.writeInt(getImageSourse());
        dest.writeSerializable(getList());

    }
}
