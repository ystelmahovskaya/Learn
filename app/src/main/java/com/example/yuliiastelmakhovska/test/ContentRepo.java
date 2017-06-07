package com.example.yuliiastelmakhovska.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.databinding.ObservableArrayList;
import android.util.Log;

import com.google.gson.Gson;

import java.text.ParseException;
import java.util.ArrayList;

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

        db.execSQL("CREATE TABLE ContentQuiz ("+
                "name TEXT,"+
                "description TEXT,"+
                "image INTEGER,"+
                "level INTEGER," +
                "id TEXT)");

        db.execSQL("CREATE TABLE QuizQuestions ("+
                "text TEXT,"+
                "correctAnswer INTEGER,"+
                "answer1 TEXT," +
                "answer2 TEXT,"+
                "answer3 TEXT,"+
                "answer4 TEXT,"+
                "id_Quiz TEXT)");

        db.execSQL("CREATE TABLE ContentWords ("+
                "name TEXT,"+
                "description TEXT,"+
                "image INTEGER,"+
                "level INTEGER," +
                "id TEXT)");

        db.execSQL("CREATE TABLE WordItem ("+
                        "word TEXT,"+
                "transcription TEXT,"+
                        "translation_sw TEXT,"+
                        "translation_ru TEXT,"+
                "id_Words TEXT)");



    }

    public ObservableArrayList<Chapter> getQuizChapters(int level) throws ParseException {
        SQLiteDatabase ContentDb = this.getReadableDatabase();
        Cursor cursor = ContentDb.rawQuery("SELECT * FROM ContentQuiz where level='"+level+"'", null);
        ObservableArrayList<Chapter> list = new ObservableArrayList<>();


        int column_name = cursor.getColumnIndex("name");
        int column_description = cursor.getColumnIndex("description");
        int column_image = cursor.getColumnIndex("image");
        int column_level = cursor.getColumnIndex("level");
        int column_id= cursor.getColumnIndex("id");


        while(cursor.moveToNext())
        {
            ObservableArrayList <ContentType> questions=new ObservableArrayList<>();
            Cursor cursorquestions = ContentDb.rawQuery("SELECT * FROM QuizQuestions where id_Quiz='"+cursor.getString(column_id)+"'", null);
            int column_correctAnswer = cursorquestions.getColumnIndex("correctAnswer");
            int column_answer1 = cursorquestions.getColumnIndex("answer1");
            int column_answer2 = cursorquestions.getColumnIndex("answer2");
            int column_answer3 = cursorquestions.getColumnIndex("answer3");
            int column_answer4= cursorquestions.getColumnIndex("answer4");
            int column_text= cursorquestions.getColumnIndex("text");

            while(cursorquestions.moveToNext()) {

                Question question= new Question();
                question.setName(cursorquestions.getString(column_text));
                question.setCorrectAnswer(cursorquestions.getInt(column_correctAnswer));
                ArrayList<Answer> answers = new ArrayList<>();
                answers.add(new Answer(1,cursorquestions.getString(column_answer1)));
                answers.add(new Answer(2,cursorquestions.getString(column_answer2)));
                answers.add(new Answer(3,cursorquestions.getString(column_answer3)));
                answers.add(new Answer(4,cursorquestions.getString(column_answer4)));
                question.setAnswers(answers);
questions.add(question);
                Log.i("questions",""+questions.size());

            }

            Chapter chapter= new Chapter();
            chapter.setLevel(cursor.getInt(column_level));
            chapter.setName(cursor.getString(column_name));
            chapter.setDescription(cursor.getString(column_description));
            chapter.setImageSourse(cursor.getInt(column_image));
            chapter.setList(questions);
            list.add(chapter);
            cursorquestions.close();
        }
        cursor.close();

        ContentDb.close();
        String json = new Gson().toJson(list);
        Log.i("json",json);
        return list;
    }


    public ObservableArrayList<Chapter> getWordsChapters(int level) throws ParseException {
        SQLiteDatabase ContentDb = this.getReadableDatabase();
        Cursor cursor = ContentDb.rawQuery("SELECT * FROM ContentWords where level='"+level+"'", null);
        ObservableArrayList<Chapter> list = new ObservableArrayList<>();


        int column_name = cursor.getColumnIndex("name");
        int column_description = cursor.getColumnIndex("description");
        int column_image = cursor.getColumnIndex("image");
        int column_level = cursor.getColumnIndex("level");
        int column_id= cursor.getColumnIndex("id");


        while(cursor.moveToNext())
        {
            ObservableArrayList <ContentType> words=new ObservableArrayList<>();
            Cursor cursorWords = ContentDb.rawQuery("SELECT * FROM WordItem where id_Words='"+cursor.getString(column_id)+"'", null);
            int column_word = cursorWords.getColumnIndex("word");
            int column_transcription = cursorWords.getColumnIndex("transcription");
            int column_translation_sw = cursorWords.getColumnIndex("translation_sw");
            int column_translation_ru = cursorWords.getColumnIndex("translation_ru");

            while(cursorWords.moveToNext()) {
                Word word = new Word();
                word.setWord(cursorWords.getString(column_word));
                word.setTranscription(cursorWords.getString(column_transcription));
                ArrayList<String>translations= new ArrayList<>();
                translations.add(cursorWords.getString(column_translation_sw));
                translations.add(cursorWords.getString(column_translation_ru));
                word.setTranslations(translations);
                words.add(word);

            }

            Chapter chapter= new Chapter();
            chapter.setLevel(cursor.getInt(column_level));
            chapter.setName(cursor.getString(column_name));
            chapter.setDescription(cursor.getString(column_description));
            Log.i("setDescription",""+chapter.getDescription());
            chapter.setImageSourse(cursor.getInt(column_image));
            chapter.setList(words);
            list.add(chapter);
            cursorWords.close();
        }
        cursor.close();

        ContentDb.close();
        String json = new Gson().toJson(list);
        Log.i("getWordsChapters",json);
        return list;
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
            Log.i("setDescription",""+chapter.getDescription());
            chapter.setImageSourse(cursor.getInt(column_image));
            chapter.setList(videos);
            list.add(chapter);
        }
        cursor.close();
        ContentDb.close();
        return list;
    }

    public void insertVideoChapters(Chapter chapter)
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

    public void insertQuizChapters(Chapter chapter,String id)
    {

        SQLiteDatabase ContentDb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", chapter.getName());
        values.put("description", chapter.getDescription());
        values.put("image", chapter.getImageSourse());
        values.put("level", chapter.getLevel());
        values.put("id", id);
        for (int i=0; i<chapter.list.size();i++){
            Log.i("chapter.list.size",""+chapter.list.size());
            ContentValues valuesquestion = new ContentValues();
            Question question= (Question)chapter.getList().get(i);
            valuesquestion.put("text", question.getName());
            valuesquestion.put("correctAnswer", question.getCorrectAnswer());
            valuesquestion.put("answer1", question.getAnswers().get(0).getText());
            valuesquestion.put("answer2", question.getAnswers().get(1).getText());
            valuesquestion.put("answer3", question.getAnswers().get(2).getText());
            valuesquestion.put("answer4", question.getAnswers().get(3).getText());
            valuesquestion.put("id_Quiz", id);
            ContentDb.insertOrThrow("QuizQuestions", null, valuesquestion);
        }

        try {

            ContentDb.insertOrThrow("ContentQuiz", null, values);

        }
        catch (android.database.sqlite.SQLiteConstraintException e){
            Log.e("Favorites.insert", e.toString());}

    }

    public void insertWordsChapters(Chapter chapter,String id)
    {

        SQLiteDatabase ContentDb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", chapter.getName());
        values.put("description", chapter.getDescription());
        values.put("image", chapter.getImageSourse());
        values.put("level", chapter.getLevel());
        values.put("id", id);
        for (int i=0; i<chapter.list.size();i++){
            Log.i("chapter.list.size",""+chapter.list.size());
            ContentValues valuesWord = new ContentValues();
            Word word= (Word)chapter.list.get(i);
            valuesWord.put("word", word.getName());
            valuesWord.put("transcription", word.getTranscription());
            valuesWord.put("translation_sw", word.getTranslations().get(0));
            valuesWord.put("translation_ru", word.getTranslations().get(1));
            valuesWord.put("id_Words", id);
            ContentDb.insertOrThrow("WordItem", null, valuesWord);
        }

        try {

            ContentDb.insertOrThrow("ContentWords", null, values);

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
    public void deleteAllQuiz()  {
        SQLiteDatabase Db = this.getWritableDatabase();
        try {
            Db.execSQL("delete from ContentQuiz");
            Db.execSQL("delete from QuizQuestions");
        }
        catch (SQLiteException e){}
            Db.close();
    }
    public void deleteAllWords() {
        SQLiteDatabase Db = this.getWritableDatabase();
        try{

        Db.execSQL("delete from ContentWords");
        Db.execSQL("delete from WordItem");
    }
        catch (SQLiteException e){}
        Db.close();
    }

}
