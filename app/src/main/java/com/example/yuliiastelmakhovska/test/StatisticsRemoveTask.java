package com.example.yuliiastelmakhovska.test;

import android.os.AsyncTask;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yuliiastelmakhovska on 2017-05-23.
 */

public class StatisticsRemoveTask extends AsyncTask<Void, Void, Void> {


    @Override
    protected Void doInBackground(Void... params) {

        URL url;
        HttpURLConnection connection = null;
        try {
            url = new URL("http://"+MainActivity.ip+"/delete/statistics/"+MainActivity.user_id);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty(
                    "Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("DELETE");
            connection.connect();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

}

