package com.example.yuliiastelmakhovska.test;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by yuliiastelmakhovska on 2017-05-13.
 */

public class WordViewModel extends BaseObservable {
    @Bindable
    ObservableArrayList<Word> words = new ObservableArrayList<>();

    public WordViewModel (){

    }

    public void setWords(ObservableArrayList<Word> words) {
        this.words=words;
    }

    public ObservableArrayList<Word> getWords() {
        return words;
    }
    @BindingAdapter("app:items")
    public static void bindList(RecyclerView view, ObservableArrayList<Word> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(layoutManager);
        view.setAdapter(new WordAdapter(list,view.getContext()));

    }
}
