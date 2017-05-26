package com.example.yuliiastelmakhovska.test;

import java.util.ArrayList;

/**
 * Created by yuliiastelmakhovska on 2017-05-12.
 */

public class Chapter {
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
        description = description;
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
}
