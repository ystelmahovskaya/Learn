package com.example.yuliiastelmakhovska.test;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by yuliiastelmakhovska on 2017-02-09.
 */

public class DownloadService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;


    private static final String TAG = "DownloadService";


    public DownloadService() {
        super(DownloadService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        String url = intent.getStringExtra("url");

        Bundle bundle = new Bundle();

        if (!TextUtils.isEmpty(url)) {

            /* Update UI: Download Service is Running */
            // receiver.send(STATUS_RUNNING, Bundle.EMPTY);
            ArrayList<Chapter> videos = new ArrayList<>();

            try {
                videos = downloadData(url);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DownloadException e) {
                e.printStackTrace();
            }


                /* Sending result back to activity */
            if (null != videos && videos.size() > 0) {

                bundle.putParcelableArrayList("result", videos);
                receiver.send(STATUS_FINISHED, bundle);
            } else {

                bundle.putParcelableArrayList("result", videos);
                receiver.send(STATUS_FINISHED, bundle);
            }
        }
        this.stopSelf();
    }

    private ArrayList<Chapter> downloadData(String requestUrl) throws IOException, DownloadException {
        //   Log.d(TAG, "downloadData works!");
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;

        /* forming th java.net.URL object */
        URL url = new URL(requestUrl);
        urlConnection = (HttpURLConnection) url.openConnection();

        /* optional request header */
        urlConnection.setRequestProperty("Content-Type", "application/json");

        /* optional request header */
        urlConnection.setRequestProperty("Accept", "application/json");

        /* for Get request */

        urlConnection.setRequestMethod("GET");
        int statusCode = urlConnection.getResponseCode();

        /* 200 represents HTTP OK */
        if (statusCode == 200) {
            Log.i("statusCode", "200");
            inputStream = new BufferedInputStream(urlConnection.getInputStream());
            String response = convertInputStreamToString(inputStream);
            ArrayList<Chapter> videos = parseResult(response);

            return videos;
        } else {
            throw new DownloadException("Failed to fetch data!!");
        }
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";

        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

            /* Close Stream */
        if (null != inputStream) {
            inputStream.close();
        }

        return result;
    }

    private ArrayList<Chapter> parseResult(String result) {

        ArrayList<Chapter> videos = new ArrayList<>();
        ContentRepo repo = new ContentRepo(getApplicationContext());
        repo.deleteAll();
        try {

            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                Chapter chapter = new Chapter();
                if (jsonArray.getJSONObject(i).has("description")) {
                    chapter.setDescription(jsonArray.getJSONObject(i).getString("description"));
                }
                if (jsonArray.getJSONObject(i).has("imageSourse")) {
                    chapter.setImageSourse(jsonArray.getJSONObject(i).getInt("imageSourse"));
                }
                if (jsonArray.getJSONObject(i).has("level")) {
                    chapter.setLevel(jsonArray.getJSONObject(i).getInt("level"));
                }
                if (jsonArray.getJSONObject(i).has("name")) {
                    chapter.setName(jsonArray.getJSONObject(i).getString("name"));
                }

                if (jsonArray.getJSONObject(i).has("list")) {

                    JSONArray v = jsonArray.getJSONObject(i).getJSONArray("list");
                    for (int j = 0; j < v.length(); j++) {
                        Video video = new Video();
                        if (v.getJSONObject(j).has("name")) {
                            video.setName(v.getJSONObject(j).getString("name"));
                        }
                        if (v.getJSONObject(j).has("subtitleDelay")) {
                            video.setSubtitleDelay(v.getJSONObject(j).getInt("subtitleDelay"));
                        }
                        ArrayList<ContentType> vid = new ArrayList<>();
                        vid.add(video);
                        chapter.setList(vid);
                    }
                }

                videos.add(chapter);

                repo.insert(chapter);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        repo.close();
        return videos;
    }
//    private void showNotification(int number, int distance) {
//        Log.i(TAG, "showNotification: in ");
//        // Bitmap bm = BitmapFactory.decodeResource(this.getResources(), R.drawable.cat);
//        Resources res = getResources();
//        String numberOfTimes = res.getQuantityString(R.plurals.numberOfTimesCalled, number, number).concat("\n").concat(res.getQuantityString(R.plurals.numberOfRides, distance, distance));
//      //  String dist = res.getQuantityString(R.plurals.numberOfTimesCalled, distance, distance);
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.ride)
//                        .setContentTitle("Ski")
//                        .setContentText(numberOfTimes)
//                     //   .setContentText(dist)
//                        //The above three is minimum to show notification
//                        .setAutoCancel(true);
//        //.setLargeIcon(bm);
//
//
//        NotificationManager mNotificationManager =
//                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//        // mId allows you to update the notification later on.
//        int mId = 100;
//        mNotificationManager.notify(mId, mBuilder.build());
//    }
//}

    class DownloadException extends Exception {

        public DownloadException(String message) {
            super(message);
            Log.d("DownloadException", message);
        }

        public DownloadException(String message, Throwable cause) {
            super(message, cause);
            Log.d("Throwable", "");
        }
    }
}
