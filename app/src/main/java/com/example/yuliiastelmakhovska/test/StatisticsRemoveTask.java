package com.example.yuliiastelmakhovska.test;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by yuliiastelmakhovska on 2017-05-23.
 */

public class StatisticsRemoveTask extends AsyncTask<String, Void, Boolean> {


    @Override
    protected Boolean doInBackground(String... params) {
        Log.i("StatisticsRemoveTask","params"+params[0].toString());
//
//        URL url;
//        HttpURLConnection connection = null;
//        try {
//            url = new URL("http://"+MainActivity.ip+"/Statistics/delete/"+MainActivity.user_id);
//            connection = (HttpURLConnection) url.openConnection();
//            connection.setDoOutput(true);
//            connection.setRequestProperty(
//                    "Content-Type", "application/x-www-form-urlencoded");
//            connection.setRequestMethod("DELETE");
//
//            connection.connect();
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//
//        } finally {
//
//            if (connection != null) {
//                connection.disconnect();
//            }
//        }
//        return null;

        URL url = null;
        try {
            url = new URL("http://"+MainActivity.ip+"/"+params[0]+"/delete/"+MainActivity.user_id);
            Log.i("",""+url.toString());
        } catch (MalformedURLException exception) {
            exception.printStackTrace();
        }
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            httpURLConnection.setRequestMethod("DELETE");
            System.out.println(httpURLConnection.getResponseCode());
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return true;
    }


}

