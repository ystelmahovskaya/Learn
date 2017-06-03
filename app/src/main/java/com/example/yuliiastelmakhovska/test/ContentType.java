package com.example.yuliiastelmakhovska.test;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by yuliiastelmakhovska on 2017-05-22.
 */

public interface ContentType extends Parcelable, Serializable{
    public void setName(String name);
    public String getName();
}
