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
        String urlQuiz = intent.getStringExtra("urlQuiz");
        String urlWords = intent.getStringExtra("urlWords");


        Bundle bundle = new Bundle();

        if (!TextUtils.isEmpty(url)) {

            /* Update UI: Download Service is Running */
            // receiver.send(STATUS_RUNNING, Bundle.EMPTY);
            ArrayList<Chapter> videos = new ArrayList<>();
            ArrayList<Chapter> quiz = new ArrayList<>();
            ArrayList<Chapter> words = new ArrayList<>();
            try {
                videos = downloadData(url, 1);
                quiz= downloadData(urlQuiz, 2);
                words=downloadData(urlWords,3);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DownloadException e) {
                e.printStackTrace();
            }


                /* Sending result back to activity */
            if (null != videos && videos.size()>0 &&null != quiz && quiz.size() > 0) {

                bundle.putParcelableArrayList("result", videos);
                bundle.putParcelableArrayList("resultQuiz", quiz);
                bundle.putParcelableArrayList("resultWords", words);
                receiver.send(STATUS_FINISHED, bundle);
            } else {

                bundle.putParcelableArrayList("result", videos);
                bundle.putParcelableArrayList("resultQuiz", quiz);
                bundle.putParcelableArrayList("resultWords", words);
                receiver.send(STATUS_FINISHED, bundle);
            }
        }
        this.stopSelf();
    }

    private ArrayList<Chapter> downloadData(String requestUrl, int parameter) throws IOException, DownloadException {
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
            ArrayList<Chapter> list=null;
            if(parameter==1) {
                list = parseResult(response);
            }
            else if(parameter==2){
                list = parseResultQuiz(response);
            }
            else if(parameter==3){
                list = parseResultWord(response);
            }
            return list;
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

                repo.insertVideoChapters(chapter);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        repo.close();
        return videos;
    }




    private ArrayList<Chapter> parseResultQuiz(String result) {

        ArrayList<Chapter> quiz = new ArrayList<>();
        ContentRepo repo = new ContentRepo(getApplicationContext());
        repo.deleteAllQuiz();
        try {

            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                String id=null;
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
                if (jsonArray.getJSONObject(i).has("_id")) {
                    id=jsonArray.getJSONObject(i).getString("_id");
                }

                if (jsonArray.getJSONObject(i).has("list")) {
                    ArrayList<ContentType> q = new ArrayList<>();
                    JSONArray v = jsonArray.getJSONObject(i).getJSONArray("list");
                    for (int j = 0; j < v.length(); j++) {

                        Question question= new Question();
                        if (v.getJSONObject(j).has("text")) {
                            question.setName(v.getJSONObject(j).getString("text"));
                        }
                        if (v.getJSONObject(j).has("correctAnswer")) {
                            question.setCorrectAnswer(v.getJSONObject(j).getInt("correctAnswer"));
                        }
                        if (v.getJSONObject(j).has("answers")) {
                            JSONArray answers = v.getJSONObject(j).getJSONArray("answers");
                            ArrayList<Answer>answerArrayList= new ArrayList<>();
                            for (int k = 0; k < answers.length(); k++) {
                                if (answers.getJSONObject(k).has("text")) {
                                    answerArrayList.add(new Answer(k+1,answers.getJSONObject(k).getString("text")));
                                }
                            }
                            question.setAnswers(answerArrayList);
                            q.add(question);
                        }


                        chapter.setList(q);
                    }
                }

                quiz.add(chapter);

                repo.insertQuizChapters(chapter,id);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        repo.close();
        Log.i("quiz size",""+quiz.size());
        return quiz;
    }




    private ArrayList<Chapter> parseResultWord(String result) {

        ArrayList<Chapter> words = new ArrayList<>();
        ContentRepo repo = new ContentRepo(getApplicationContext());
       repo.deleteAllWords();
        try {

            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                String id=null;
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
                if (jsonArray.getJSONObject(i).has("_id")) {
                    id=jsonArray.getJSONObject(i).getString("_id");
                }

                if (jsonArray.getJSONObject(i).has("list")) {
                    ArrayList<ContentType> q = new ArrayList<>();
                    JSONArray v = jsonArray.getJSONObject(i).getJSONArray("list");
                    for (int j = 0; j < v.length(); j++) {

                        Word word= new Word();

                        if (v.getJSONObject(j).has("word")) {
                            word.setWord(v.getJSONObject(j).getString("word"));
                            Log.i("setWord",""+v.getJSONObject(j).getString("word"));
                        }
                        if (v.getJSONObject(j).has("transcription")) {
                            word.setTranscription(v.getJSONObject(j).getString("transcription"));
                        }
                        if (v.getJSONObject(j).has("translations")) {
                            JSONArray trans = v.getJSONObject(j).getJSONArray("translations");
                            ArrayList<String>translations = new ArrayList<>();

                           translations.add(trans.getString(0));
                            translations.add(trans.getString(1));

                            word.setTranslations(translations);
                            q.add(word);
                        }


                        chapter.setList(q);
                    }
                }

                words.add(chapter);

                repo.insertWordsChapters(chapter,id);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        repo.close();
        Log.i("quiz size",""+words.size());
        return words;
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
