package com.example.yuliiastelmakhovska.test;


import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yuliiastelmakhovska.test.databinding.ChapterListLayoutBinding;

/**
 * Created by yuliiastelmakhovska on 2017-05-12.
 */

public class ChaptersList extends Fragment {

ObservableArrayList<Chapter>chapters;
    public ChaptersList(ObservableArrayList chapters){
        super();
        this.chapters=chapters;
        Log.i("ChaptersList",""+chapters.size());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.chapter_list_layout, container, false);
        ChapterViewModel chapterViewModel= new ChapterViewModel();
        chapterViewModel.setChapters(chapters);
        ChapterListLayoutBinding binding = DataBindingUtil.bind(rootView); ;
        binding.setModel(chapterViewModel);
//

        return rootView;
    }

    public void setChapters(ObservableArrayList<Chapter> chapters) {
        this.chapters = chapters;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Chapters");

    }

}
