package com.example.yuliiastelmakhovska.test;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yuliiastelmakhovska.test.databinding.ChapterItemBinding;

/**
 * Created by yuliiastelmakhovska on 2017-05-12.
 */

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ChapterItemBinding binder;
        CardView card;

        public ViewHolder(View v) {
            super(v);
            binder = DataBindingUtil.bind(v);
            card=(CardView)v.findViewById(R.id.chapterCard);
        }
    }

    private ObservableArrayList<Chapter> list;

    public ChapterAdapter(ObservableArrayList<Chapter> l) {
        list = l;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapter_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Chapter ch = list.get(position);
        if( ch.getList().get(0)instanceof Video) {

        }
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = null;

                if(ch.getList().get(0)instanceof Word) {
                     intent = new Intent(v.getContext(), WordsExersices.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("list", ch.getList());
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);

                }
                if( ch.getList().get(0)instanceof Video) {
                    intent = new Intent(v.getContext(), VideoActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("list", ch.getList().get(0));
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                }
                if( ch.getList().get(0)instanceof Question) {
                    intent = new Intent(v.getContext(), QuizActivity.class);

                    Bundle bundle = new Bundle();
                    Log.i("onBindViewHolder",""+ch.getList().size());
                    bundle.putParcelableArrayList("list", ch.getList());
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                }
            }
        });

        holder.binder.setChapter(ch);
        holder.binder.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        else
            return 0;
    }


}
