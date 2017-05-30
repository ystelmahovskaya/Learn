package com.example.yuliiastelmakhovska.test;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * Created by yuliiastelmakhovska on 2017-05-23.
 */

public class ResultsPostTask extends AsyncTask<Integer, Void, Void> {


    @Override
    protected Void doInBackground(Integer... params) {

        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL("http://"+MainActivity.ip+"/Statistics");
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            String urlParameters= "user_id=" + MainActivity.user_id +
                    "&score=" + params[0].toString()+
                    "&date=" + new Date().toString();
            Log.i("date",""+new Date().toString());
            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());
            wr.writeBytes (urlParameters);
            wr.flush ();
            wr.close ();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }
}
