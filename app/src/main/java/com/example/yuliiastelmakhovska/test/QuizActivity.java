package com.example.yuliiastelmakhovska.test;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.yuliiastelmakhovska.test.databinding.ActivityQuizBinding;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {
ObservableArrayList<Question> questions= new ObservableArrayList<>();
    ActivityQuizBinding binding;
    int index = 0;
    int score=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_quiz);
        Intent intent= getIntent();
        ArrayList<Question> q = intent.getParcelableArrayListExtra("list");
        questions.addAll(q);
         binding= DataBindingUtil.setContentView(this, R.layout.activity_quiz);
            binding.setQuestion(questions.get(index));

    }


    public void quizLoop(View view) {
        int callButton = Integer.parseInt(view.getTag().toString());
        if(questions.get(index).getCorrectAnswer()==callButton){
            score+=1;
        }
        if(index<questions.size()-1) {
            index = index + 1;
            binding.setQuestion(questions.get(index));
        }
        else {
           createDialog((score/questions.size())*10);
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
