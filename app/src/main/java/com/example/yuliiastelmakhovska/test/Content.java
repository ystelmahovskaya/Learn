package com.example.yuliiastelmakhovska.test;

import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by yuliiastelmakhovska on 2017-05-06.
 */

public class Content extends BaseObservable {

public static ObservableArrayList<Chapter> chaptersWords=new ObservableArrayList<>();
    public static ObservableArrayList<Chapter> chaptersVideos=new ObservableArrayList<>();
    public static ObservableArrayList<Chapter> chaptersQuiz=new ObservableArrayList<>();

    public static ObservableArrayList <ContentType> numbers=new ObservableArrayList<>();
    public static ObservableArrayList <ContentType> videos=new ObservableArrayList<>();


    public Content() {



        Log.d("Content","Content");
        Word one = new Word();
        one.setWord("one");
        one.setTranscription("[wʌn]");
        one.addTranslation("ett");
        one.addTranslation("один");

        Word two = new Word();
        two.setWord("two");
        two.setTranscription("[tuː]");
        two.addTranslation("två");
        two.addTranslation("два");

        Word three = new Word();
        three.setWord("three");
        three.setTranscription("[θriː]");
        three.addTranslation("tre");
        three.addTranslation("три");
        numbers.add(one);
        numbers.add(two);
        numbers.add(three);


        Chapter num = new Chapter(2,"Numbers","description",(R.drawable.ic_menu_camera),numbers);
        chaptersWords.add(num);

        Video video= new Video();
        video.setName("lozano");
        video.setSubtitleDelay(12000);
        videos.add(video);
        Chapter vid = new Chapter(1,"Ted","description",(R.drawable.lozano),videos);
        Chapter vid2= new Chapter(2,"Ted","description",(R.drawable.lozano),videos);
        Chapter vid3= new Chapter(3, "Ted","description",(R.drawable.lozano),videos);
        Chapter vid4= new Chapter(1, "Ted","description",(R.drawable.lozano),videos);
        chaptersVideos.add(vid);
        chaptersVideos.add(vid2);
        chaptersVideos.add(vid3);
        chaptersVideos.add(vid4);

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


        Chapter q= new Chapter(1,"Present Simple", "",(R.drawable.ic_menu_gallery),questions);

      chaptersQuiz.add(q);
    }

    public static final Content ourInstance = new Content();
    static Content getInstance() {
        return ourInstance;
    }

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

}
