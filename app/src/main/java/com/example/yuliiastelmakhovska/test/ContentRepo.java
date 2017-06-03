package com.example.yuliiastelmakhovska.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.databinding.ObservableArrayList;
import android.util.Log;

import java.text.ParseException;

/**
 * Created by yuliiastelmakhovska on 2017-05-30.
 */

public class ContentRepo extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ContentDb";
    private static final int VERSION = 2;

    public ContentRepo(Context context)
    {
        super(context,DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE ContentVideo ("+
                "name TEXT,"+
                "description TEXT,"+
                "image INTEGER,"+
                "level INTEGER," +
                "video_name TEXT," +
                "video_subtitle_delay INTEGER)");


    }



    public ObservableArrayList<Chapter> getVideoChapters(int level) throws ParseException {
        SQLiteDatabase ContentDb = this.getReadableDatabase();
        Cursor cursor = ContentDb.rawQuery("SELECT * FROM ContentVideo where level='"+level+"'", null);
        ObservableArrayList<Chapter> list = new ObservableArrayList<>();


        int column_name = cursor.getColumnIndex("name");
        int column_description = cursor.getColumnIndex("description");
        int column_image = cursor.getColumnIndex("image");
        int column_level = cursor.getColumnIndex("level");
        int column_video_name = cursor.getColumnIndex("video_name");
        int column_video_subtitle_delay = cursor.getColumnIndex("video_subtitle_delay");

        while(cursor.moveToNext())
        {
            ObservableArrayList <ContentType> videos=new ObservableArrayList<>();
            Video v= new Video();
            v.setName(cursor.getString(column_video_name));
            v.setSubtitleDelay(cursor.getInt(column_video_subtitle_delay));
            videos.add(v);
            Chapter chapter= new Chapter();
         chapter.setLevel(cursor.getInt(column_level));
chapter.setName(cursor.getString(column_name));
chapter.setDescription(cursor.getString(column_description));
            chapter.setImageSourse(cursor.getInt(column_image));
            chapter.setList(videos);
            list.add(chapter);
        }
        cursor.close();
        ContentDb.close();
        return list;
    }

    public void insert(Chapter chapter)
    {
        SQLiteDatabase ContentDb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", chapter.getName());
        values.put("description", chapter.getDescription());
        values.put("image", chapter.getImageSourse());
        values.put("level", chapter.getLevel());
        values.put("video_name", chapter.getList().get(0).getName());
        Video v=(Video)chapter.getList().get(0);
        values.put("video_subtitle_delay",v.getSubtitleDelay());
        try {

            ContentDb.insertOrThrow("ContentVideo", null, values);

        }
        catch (android.database.sqlite.SQLiteConstraintException e){
            Log.e("Favorites.insert", e.toString());}

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void deleteAll(){
        SQLiteDatabase Db = this.getWritableDatabase();
        Db.execSQL("delete from ContentVideo");
        Db.close();
    }


}
