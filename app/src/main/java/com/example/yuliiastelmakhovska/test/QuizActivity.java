package com.example.yuliiastelmakhovska.test;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.yuliiastelmakhovska.test.databinding.ActivityQuizBinding;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class QuizActivity extends AppCompatActivity {
    ScheduledExecutorService scheduler;
    ScheduledFuture<?> future;
    ScheduledFuture<?> countdown;
    ScheduledFuture<?> color;
ObservableArrayList<Question> questions= new ObservableArrayList<>();
    TextView timer;

    ActivityQuizBinding binding;
    int index = 0;
    double score=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent= getIntent();
        ArrayList<Question> q = intent.getParcelableArrayListExtra("list");
        questions.addAll(q);
         binding= DataBindingUtil.setContentView(this, R.layout.activity_quiz);
         binding.setQuestion(questions.get(index));
        scheduler = Executors.newScheduledThreadPool(1);
        timer= (TextView) findViewById(R.id.timer);
        future= scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(index<questions.size()-1) {
                            index = index + 1;
                            binding.setQuestion(questions.get(index));
                            countdown.cancel(true);
                            setCountdown();
                        }
                    }
                });

            }
        }, 10, 10, TimeUnit.SECONDS);

setCountdown();




    }

    public void setCountdown(){
        countdown= scheduler.scheduleAtFixedRate(new Runnable() {
            int count=10;
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        timer.setText(String.valueOf(count));
                        count-=1;

                    }
                });

            }
        }, 0, 1, TimeUnit.SECONDS);
    }



    public void quizLoop(View view) {
        final Button button= (Button)view;
        int callButton = Integer.parseInt(view.getTag().toString());

        if(questions.get(index).getCorrectAnswer()==callButton){
            score+=1;
            button.setBackgroundColor(Color.GREEN);
            color= scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    button.setBackgroundColor(Color.LTGRAY);
                }
            },75,TimeUnit.MILLISECONDS);
        }
        else{

            button.setBackgroundColor(Color.RED);
            color= scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    button.setBackgroundColor(Color.LTGRAY);
                }
            },75,TimeUnit.MILLISECONDS);


        }
        if(index<questions.size()-1) {
            index = index + 1;
            binding.setQuestion(questions.get(index));
            countdown.cancel(true);
future.cancel(true);
            future= scheduler.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(index<questions.size()-1) {
                                index = index + 1;
                                binding.setQuestion(questions.get(index));
                            }

                        }
                    });

                }
            }, 10, 10, TimeUnit.SECONDS);
            setCountdown();
        }
        else {
            createDialog((int)((score/questions.size())*100));
            future.cancel(true);
            countdown.cancel(true);
            scheduler.shutdown();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void createDialog(final int score){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Score is "+score+" points");
        alertDialogBuilder.setNeutralButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //todo async task
                       ResultsPostTask resultsPostTask= new ResultsPostTask();
                        resultsPostTask.execute(score);
                        finish();
                    }
                });

        alertDialogBuilder.setPositiveButton("Share",new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                shareOnFb("I'v got "+score+" points at Test app today!");
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void shareOnFb( String text) {

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                .setQuote(text)
                .build();
        ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
    }

}
