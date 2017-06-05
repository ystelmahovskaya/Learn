package com.example.yuliiastelmakhovska.test;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by yuliiastelmakhovska on 2017-05-06.
 */

public class Content extends BaseObservable implements  DownloadResultReceiver.Receiver {

    private DownloadResultReceiver mReceiver;

public static ObservableArrayList<Chapter> chaptersWords=new ObservableArrayList<>();
    public static ObservableArrayList<Chapter> chaptersVideos=new ObservableArrayList<>();
    public static ObservableArrayList<Chapter> chaptersQuiz=new ObservableArrayList<>();

    public static ObservableArrayList <ContentType> numbers=new ObservableArrayList<>();
    public static ObservableArrayList <ContentType> videos=new ObservableArrayList<>();
    Context context;


    public Content(Context context) {
        this.context=context;
        mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        notifyChange();



//        Log.d("Content","Content");
//        Word one = new Word();
//        one.setWord("one");
//        one.setTranscription("[wʌn]");
//        one.addTranslation("ett");
//        one.addTranslation("один");
//
//        Word two = new Word();
//        two.setWord("two");
//        two.setTranscription("[tuː]");
//        two.addTranslation("två");
//        two.addTranslation("два");
//
//        Word three = new Word();
//        three.setWord("three");
//        three.setTranscription("[θriː]");
//        three.addTranslation("tre");
//        three.addTranslation("три");
//        numbers.add(one);
//        numbers.add(two);
//        numbers.add(three);


//        Chapter num = new Chapter(2,"Numbers","description",(R.drawable.numeros),numbers);
//        chaptersWords.add(num);
//
//        Video video= new Video();
//        video.setName("lozano");
//        video.setSubtitleDelay(12000);
//        videos.add(video);
//        Chapter vid = new Chapter(1,"Ted","description",(R.drawable.lozano),videos);
//        Chapter vid2= new Chapter(2,"Ted","description",(R.drawable.lozano),videos);
//        Chapter vid3= new Chapter(3, "Ted","description",(R.drawable.lozano),videos);
//        Chapter vid4= new Chapter(1, "Ted","description",(R.drawable.lozano),videos);
//        chaptersVideos.add(vid);
//        chaptersVideos.add(vid2);
//        chaptersVideos.add(vid3);
//        chaptersVideos.add(vid4);

        Question question1= new Question();
        question1.setCorrectAnswer(1);
        question1.setAnswers(new ArrayList<Answer>());
        question1.setName("We sometimes ____ books.");
        question1.getAnswers().add(new Answer(1,"read"));
        question1.getAnswers().add(new Answer(2,"readed"));
        question1.getAnswers().add(new Answer(3,"reading"));
        question1.getAnswers().add(new Answer(4,"reads"));

        Question question2= new Question();
        question2.setCorrectAnswer(2);
        question2.setAnswers(new ArrayList<Answer>());
        question2.setName("Emily ____ to the disco");
        question2.getAnswers().add(new Answer(1,"go"));
        question2.getAnswers().add(new Answer(2,"goes"));
        question2.getAnswers().add(new Answer(3,"went"));
        question2.getAnswers().add(new Answer(4,"gone"));
        ObservableArrayList<ContentType> questions= new ObservableArrayList<>();
        questions.add(question1);
        questions.add(question2);


        Chapter q= new Chapter(1,"Present Simple", "",1,questions);
        Chapter q1= new Chapter(1,"Past Simple", "",1,questions);
        Chapter q2= new Chapter(1,"Future Indefinite", "",1,questions);


      chaptersQuiz.add(q);
        chaptersQuiz.add(q1);
        chaptersQuiz.add(q2);


    }

//    public static final Content ourInstance = new Content(con);
//    static Content getInstance() {
//        return ourInstance;
//    }

    public static ObservableArrayList<Chapter> getChaptersWords() {


        String json = new Gson().toJson(chaptersWords);
        Log.i("ch",""+json);
        return chaptersWords;
    }
    public static ObservableArrayList<Chapter> getChaptersVideos() {
        String json = new Gson().toJson(chaptersVideos);
        Log.i("ch",""+json);
        return chaptersVideos;
    }

    public static ObservableArrayList<Chapter> getChaptersQuiz() {
        String json = new Gson().toJson(chaptersQuiz);
        Log.i("ch",""+json);
        return chaptersQuiz;
    }

    public static ObservableArrayList<ContentType> getNumbers() {
        return numbers;
    }


    public void sendRequest(){
        Log.i("sendRequest","");
        Intent intent = new Intent(context, DownloadService.class);
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
//        String seasonPref = sharedPref.getString("season_number", "11");
//        String profileId = sharedPref.getString("profile_id", "3206");

        String url = "http://"+MainActivity.ip+"/ContentVideos";
        String urlQuiz = "http://"+MainActivity.ip+"/ContentQuiz";


        intent.putExtra("url", url);
        intent.putExtra("urlQuiz", urlQuiz);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("requestId", 101);
        context.startService(intent);

    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        switch (resultCode) {
            case DownloadService.STATUS_RUNNING:

                break;
            case DownloadService.STATUS_FINISHED:
                /* Hide progress & extract result from bundle */
                ArrayList<Chapter>listtmp = resultData.getParcelableArrayList("result");
                chaptersVideos.clear();
                chaptersVideos.addAll(listtmp);
                ArrayList<Chapter>listtmpquiz = resultData.getParcelableArrayList("resultQuiz");
                chaptersQuiz.clear();
                chaptersQuiz.addAll(listtmpquiz);

                    notifyChange();

                break;
            case DownloadService.STATUS_ERROR:
                /* Handle the error */
                String error = resultData.getString(Intent.EXTRA_TEXT);
                //  Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                break;
        }
    }
}
