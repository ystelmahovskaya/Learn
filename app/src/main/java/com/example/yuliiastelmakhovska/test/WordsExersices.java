package com.example.yuliiastelmakhovska.test;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.yuliiastelmakhovska.test.databinding.ActivityWordsExersicesBinding;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;
import java.util.Locale;

public class WordsExersices extends AppCompatActivity {
    private final int REQ_CODE_SPEECH_INPUT = 100;
    WordViewModel wordViewModel = new WordViewModel();
    ArrayList<String> wordOriginal=new ArrayList<>();
    ArrayList<String> wordPronounces = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        final ArrayList<Word> words= bundle.getParcelableArrayList("list");
        ObservableArrayList wordsObs= new ObservableArrayList();
        wordsObs.addAll(words);


        wordViewModel.setWords(wordsObs);
        ActivityWordsExersicesBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_words_exersices);
       binding.setModel(wordViewModel);



//        setContentView(R.layout.activity_words_exersices);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button check= (Button)findViewById(R.id.checkresult);
        check.setOnClickListener(new View.OnClickListener() {
            double pointsCorrect=0;
            @Override
            public void onClick(View v) {
                for (int i=0; i<wordOriginal.size(); i++){
                    if (wordOriginal.get(i).equals(wordPronounces.get(i))){
                        Log.e(wordOriginal.get(i), wordPronounces.get(i));
                        pointsCorrect+=1;
                    }
                }
                int pointsPercentage= (int) (pointsCorrect * 100/ words.size());
                createDialog(pointsPercentage);
            }

        });
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
                ResultsPostTask resultsPostTask= new ResultsPostTask();
                resultsPostTask.execute(score);
                finish();

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

//        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.test);
//
//        SharePhoto photo = new SharePhoto.Builder()
//                .setBitmap(image)
//                .setCaption(text)
//                .build();

        ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
    }

    public void promptSpeechInput(String s) {
        wordOriginal.add(s);
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "say "+s);

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
//            Toast.makeText(getApplicationContext(),
//                    getString(new String("speech_not_supported")),
//                    Toast.LENGTH_SHORT).show();
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

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("onActivityResult","");
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
wordPronounces.add(result.get(0));
                    Log.e("",""+result.get(0));
                }
                else{
                    wordPronounces.add("");
                }
                break;
            }

        }
    }




}
