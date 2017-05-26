package com.example.yuliiastelmakhovska.test;

import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.util.Log;

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
        two.addTranslation("two");
        two.addTranslation("два");

        Word three = new Word();
        three.setWord("three");
        three.setTranscription("[θriː]");
        three.addTranslation("tre");
        three.addTranslation("три");
        numbers.add(one);
        numbers.add(two);
        numbers.add(three);


        Chapter num = new Chapter("Numbers","description",(R.drawable.ic_menu_camera),numbers);
        chaptersWords.add(num);

        Video video= new Video();
        video.setName("lozano");
        video.setSubtitleDelay(12000);
        videos.add(video);
        Chapter vid = new Chapter("Ted","description",(R.drawable.lozano),videos);
        Chapter vid2= new Chapter("Ted","description",(R.drawable.lozano),videos);
        Chapter vid3= new Chapter("Ted","description",(R.drawable.lozano),videos);
        Chapter vid4= new Chapter("Ted","description",(R.drawable.lozano),videos);
        chaptersVideos.add(vid);
        chaptersVideos.add(vid2);
        chaptersVideos.add(vid3);
        chaptersVideos.add(vid4);

        Question question1= new Question();
        question1.setCorrectAnswer(2);
        question1.setAnswers(new ArrayList<Answer>());
        question1.setName("question1");
        question1.getAnswers().add(new Answer(1,"answer1"));
        question1.getAnswers().add(new Answer(2,"answer2"));
        question1.getAnswers().add(new Answer(3,"answer3"));
        question1.getAnswers().add(new Answer(4,"answer4"));

        Question question2= new Question();
        question2.setCorrectAnswer(3);
        question2.setAnswers(new ArrayList<Answer>());
        question2.setName("question2");
        question2.getAnswers().add(new Answer(1,"answer1"));
        question2.getAnswers().add(new Answer(2,"answer2"));
        question2.getAnswers().add(new Answer(3,"answer3"));
        question2.getAnswers().add(new Answer(4,"answer4"));
        ObservableArrayList<ContentType> questions= new ObservableArrayList<>();
        questions.add(question1);
        questions.add(question2);


        Chapter q= new Chapter("Present Simple", "",(R.drawable.ic_menu_gallery),questions);

      chaptersQuiz.add(q);
    }

    public static final Content ourInstance = new Content();
    static Content getInstance() {
        return ourInstance;
    }

    public static ObservableArrayList<Chapter> getChaptersWords() {
        return chaptersWords;
    }
    public static ObservableArrayList<Chapter> getChaptersVideos() {
        return chaptersVideos;
    }

    public static ObservableArrayList<Chapter> getChaptersQuiz() {
        return chaptersQuiz;
    }

    public static ObservableArrayList<ContentType> getNumbers() {
        return numbers;
    }

//    @BindingAdapter("app:items")
//    public static void bindList(RecyclerView view, ObservableArrayList<Word> list) {
//        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
//        view.setLayoutManager(layoutManager);
//        view.setAdapter(new WordAdapter(list));
//
//    }
}
