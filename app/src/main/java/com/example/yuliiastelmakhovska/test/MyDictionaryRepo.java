package com.example.yuliiastelmakhovska.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.databinding.ObservableArrayList;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by yuliiastelmakhovska .
 */

class MyDictionaryRepo extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "DictionaryDb";
    private static final int VERSION = 2;



    public MyDictionaryRepo(Context context)
    {
        super(context,DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("","onCreate Database!");
        db.execSQL("CREATE TABLE Dictionary (word TEXT,"+
                "transcription TEXT,"+
                "translation_sw TEXT,"+
        "translation_ru TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if( oldVersion == 1 && newVersion == 2)
        {
            //Change name on notes id column to notes _id
            db.beginTransaction();
            try {
                //Rename existing table
                db.execSQL("ALTER TABLE Dictionary RENAME TO Dictionary;");
                //Create new Notes table with _id column name
                db.execSQL("CREATE TABLE Dictionary ((word TEXT,"+
                                "transcription TEXT,"+
                                "translation_sw TEXT,"+
                                "translation_ru TEXT");

                db.execSQL("INSERT INTO Dictionary(word,transcription,translation_sw,translation_ru) " +
                        "SELECT word,transcription,translation_sw,translation_ru " +
                        "FROM tmp_Dictionary;");
                //Remove old table
                db.execSQL("DROP TABLE tmp_Dictionary;");
                db.setTransactionSuccessful();
            }catch (SQLiteException e){}
            finally {
                db.endTransaction();
            }


        }
    }
//

    public void insert(String word,String transcription,String translation_sw,String translation_ru)
    {
        SQLiteDatabase DictionaryDb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("word", word);
        values.put("transcription", transcription);
        values.put("translation_sw", translation_sw);
        values.put("translation_ru", translation_ru);
        try {
            DictionaryDb.insertOrThrow("Dictionary", null, values);
            Log.i("insert","in");
        }
        catch (android.database.sqlite.SQLiteConstraintException e){Log.e("Dictionary.insert", e.toString());}

    }


    public ObservableArrayList<Word> getWordFromDictionary() throws ParseException {
        SQLiteDatabase DictionaryDb = this.getReadableDatabase();
        Cursor cursor = DictionaryDb.rawQuery("SELECT * FROM Dictionary", null);
        ObservableArrayList<Word> list = new ObservableArrayList<>();

        int column_word = cursor.getColumnIndex("word");
        int column_transcription = cursor.getColumnIndex("transcription");
        int column_translation_sw = cursor.getColumnIndex("translation_sw");
        int column_translation_ru = cursor.getColumnIndex("translation_ru");

        while(cursor.moveToNext())
        {
           Word word= new Word();
            word.setWord(cursor.getString(column_word));
            word.setTranscription(cursor.getString(column_transcription));
            ArrayList<String> translation= new ArrayList();
            translation.add(cursor.getString(column_translation_sw));
            translation.add(cursor.getString(column_translation_ru));
word.setTranslations(translation);
            list.add(word);
        }
        cursor.close();
        DictionaryDb.close();
        Log.i("repo",""+list.size());
        return list;
    }

    public boolean isInDictionaryDb(String word){

      SQLiteDatabase DictionaryDb = getReadableDatabase();

        SQLiteStatement s = DictionaryDb.compileStatement( "select count(*) from Dictionary where word='" + word + "'; " );

        long numRows = s.simpleQueryForLong();
        if(numRows!=0){
            return true;
        }
        return false;
    }

    public void deleteItemFromDB(Word word){
        SQLiteDatabase DictionaryDb = this.getWritableDatabase();
        DictionaryDb.delete("Dictionary","word=? ",new String[]{word.getWord()});
//        for(int i=0; i<FullscreenMainActivity.controller.poiListFromDB.size(); i++){TODO inplement delete from list
//            if(FullscreenMainActivity.controller.poiListFromDB.get(i).getLocation().getAltitude()==poi.getLocation().getAltitude()&&
//                    FullscreenMainActivity.controller.poiListFromDB.get(i).getLocation().getLongitude()==poi.getLocation().getLongitude()){
//                FullscreenMainActivity.controller.poiListFromDB.remove(i);
//            }
//        }
        DictionaryDb.close();
    }
    public void deleteAll(){
        SQLiteDatabase DictionaryDb = this.getWritableDatabase();
        DictionaryDb.execSQL("delete from Dictionary");
//        FullscreenMainActivity.controller.poiListFromDB.clear();//TODO inplement redraw
        DictionaryDb.close();
    }
}




