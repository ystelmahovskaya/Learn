package com.example.yuliiastelmakhovska.test;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class VideoActivity extends AppCompatActivity {
    ProgressDialog pDialog;
    VideoView videoview;
    TextView textView;
    MediaController mediacontroller;
    ArrayList<Subtitle>subtitles= new ArrayList<>();
    int position=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        final Video video= bundle.getParcelable("list");

        GetSubtitlesTask getSubtitlesTask= new GetSubtitlesTask();
        try {
            subtitles=getSubtitlesTask.execute(video.name+".srt").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_video);
        videoview = (VideoView) findViewById(R.id.videoView);
        textView=(TextView)findViewById(R.id.textView2);

        pDialog = new ProgressDialog(VideoActivity.this);
        // Set progressbar message
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        // Show progressbar
       // pDialog.show();

        try {
            // Start the MediaController
             mediacontroller = new MediaController(
                    VideoActivity.this);
            mediacontroller.setAnchorView(videoview);


            videoview.setMediaController(mediacontroller);
            videoview.setVideoPath("http://"+MainActivity.ip+"/content/"+video.name+".mp4");

            videoview.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent motionEvent)
                {

                    if (videoview.isPlaying())
                    {
                        videoview.pause();
//textView.setText(String.valueOf(videoview.getCurrentPosition()));
                        position = videoview.getCurrentPosition();
                        for(int i=0; i<subtitles.size(); i++){
                            if(position>=subtitles.get(i).startTime+video.subtitleDelay&& position<=subtitles.get(i).endTime+video.subtitleDelay){
                                textView.setText(subtitles.get(i).text);
                            }
                        }

                        return false;
                    }
                    else
                    {

                      videoview.seekTo(position);
                        textView.setText("");
                        videoview.start();
                        return false;
                    }
                }
            });
            videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                // Close the progress bar and play the video
                public void onPrepared(MediaPlayer mp) {
                    pDialog.dismiss();
                    videoview.start();
                }
            });
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }





//        videoview.requestFocus();
//


    }

//    @Override
//    public void onPause() {
//
//        super.onPause();
//        Log.d(String.valueOf(videoview.getCurrentPosition()), "onPause called");
//
//        videoview.pause();
//    }
    public class GetSubtitlesTask extends AsyncTask<String, Void, ArrayList<Subtitle>> {
        ArrayList<Subtitle>subtitlesTask=new ArrayList<>();
        @Override
        protected ArrayList<Subtitle> doInBackground(String... params) {
            Log.e("doInBackground","");
            try {
                subtitlesTask= getSubtitles(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (LoginActivity.UserLoginTask.DownloadException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (DownloadException e) {
                e.printStackTrace();
            }
            Log.e("doInBackground",""+subtitlesTask.size());
            return subtitlesTask;
        }


        protected ArrayList<Subtitle> getSubtitles(String name) throws IOException, LoginActivity.UserLoginTask.DownloadException, JSONException, DownloadException {
            ArrayList<Subtitle>subtitles;
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;

            String RequestUrl = "http://"+MainActivity.ip+"/content/sub/"+name;
            URL url = null;
            try {
                url = new URL(RequestUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
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
                subtitles = parseSubtitles(response);

            } else {
                throw new DownloadException("Failed to fetch data!!");
            }

            return subtitles;
        }

        private ArrayList<Subtitle> parseSubtitles(String response) throws JSONException {


            ArrayList<Subtitle>subtitles= new ArrayList<>();
            try {


                JSONArray jsonArray = new JSONArray(response);
                for(int i=0; i<jsonArray.length();i++){
                    Subtitle subtitle= new Subtitle();
                    if (jsonArray.getJSONObject(i).has("startTime")) {
subtitle.setStartTime(jsonArray.getJSONObject(i).getInt("startTime"));
                    }
                    if (jsonArray.getJSONObject(i).has("endTime")) {
                        subtitle.setEndTime(jsonArray.getJSONObject(i).getInt("endTime"));
                    }
                    if (jsonArray.getJSONObject(i).has("text")) {
                        subtitle.setText(jsonArray.getJSONObject(i).getString("text"));
                    }
                    Log.i("",""+subtitle);
                    subtitles.add(subtitle);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("subtitles",""+subtitles.size());
            return subtitles;
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

        class DownloadException extends Exception {

            public DownloadException(String message) {
                super(message);
                Log.d("DownloadException", message);
            }
        }

    }
}


