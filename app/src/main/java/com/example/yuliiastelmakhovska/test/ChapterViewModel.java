package com.example.yuliiastelmakhovska.test;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by yuliiastelmakhovska on 2017-05-13.
 */

public class ChapterViewModel extends BaseObservable {
    @Bindable
    ObservableArrayList<Chapter> chapters = new ObservableArrayList<>();
    @Bindable
    ObservableArrayList<Chapter> videos = new ObservableArrayList<>();

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
        Bitmap bitmap = null;
        try {
             bitmap= new GetIcon().execute("http://"+MainActivity.ip+"/images/"+resource+".jpeg").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        imageView.setImageBitmap(bitmap);
    }


    @BindingAdapter("app:items")
    public static void bindList(RecyclerView view, ObservableArrayList<Chapter> list) {
     //   LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        view.setAdapter(new ChapterAdapter(list));
    }
    static class GetIcon extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            URL url = null;
            try {

                Log.d("RemoteImageHandler", "URL " + params[0]);
                url = new URL(params[0]);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setDoInput(true);
                c.connect();
                InputStream is = c.getInputStream();
                Bitmap img;
                img = BitmapFactory.decodeStream(is);
                return img;
            } catch (MalformedURLException e) {
                Log.d("RemoteImageHandler", "fetchImage passed invalid URL ");
            } catch (IOException e) {
                Log.d("RemoteImageHandler", "fetchImage IO exception: " + e);
            }
            return null;
        }
    }
}
