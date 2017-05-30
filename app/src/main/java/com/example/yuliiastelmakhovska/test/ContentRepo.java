package com.example.yuliiastelmakhovska.test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yuliiastelmakhovska on 2017-05-30.
 */

public class ContentRepo extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DictionaryDb";
    private static final int VERSION = 2;

    public ContentRepo(Context context)
    {
        super(context,DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE ContentVideo (name TEXT,"+
                "description TEXT,"+
                "image TEXT,"+
                "level INTEGER," +
                "video_name TEXT," +
                "video_subtitle_delay INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
