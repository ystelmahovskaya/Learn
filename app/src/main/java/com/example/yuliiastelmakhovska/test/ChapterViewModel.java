package com.example.yuliiastelmakhovska.test;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

/**
 * Created by yuliiastelmakhovska on 2017-05-13.
 */

public class ChapterViewModel extends BaseObservable {
    @Bindable
    ObservableArrayList<Chapter> chapters = new ObservableArrayList<>();
    public ChapterViewModel (){

    }

    public void setChapters(ObservableArrayList<Chapter> chapters) {
       this.chapters=chapters;
    }

    public ObservableArrayList<Chapter> getChapters() {
        return chapters;
    }

    @BindingAdapter("android:src")
    public static void setImageResource(ImageView imageView, int resource){
        imageView.setImageResource(resource);
    }

    @BindingAdapter("app:items")
    public static void bindList(RecyclerView view, ObservableArrayList<Chapter> list) {
     //   LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        view.setAdapter(new ChapterAdapter(list));
    }
}
