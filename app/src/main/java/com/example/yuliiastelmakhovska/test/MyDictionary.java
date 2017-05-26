package com.example.yuliiastelmakhovska.test;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yuliiastelmakhovska.test.databinding.ActivityMyDictionaryBinding;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class MyDictionary extends Fragment {
    public View rootView;
    MyDictionaryRepo dictionaryRepo;
    ActivityMyDictionaryBinding binding;
    WordViewModel wordViewModel;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    public MyDictionary(){

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_my_dictionary, container, false);

         wordViewModel= new WordViewModel();
        dictionaryRepo= new MyDictionaryRepo(this.getContext());
        try {
            wordViewModel.setWords(dictionaryRepo.getWordFromDictionary());
            Log.i("wordViewModel",""+wordViewModel.getWords().size());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        binding = DataBindingUtil.bind(rootView); ;
        binding.setModel(wordViewModel);

        rootView.setTag(this);
        rootView.setId(R.id.fragment_dictionary_view);
        Log.i(rootView.getTag().toString(),"tag");

        return rootView;
    }
    public void promptSpeechInput(String s) {
        Log.e("promptSpeechInput","MyDictionary");
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
    /**
     * Receiving speech input
     * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder.setMessage(result.get(0)+" is recognized");
                    alertDialogBuilder.setNeutralButton("ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                  return;
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }

                break;
            }

        }
    }


//        @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        dictionaryRepo= new MyDictionaryRepo(getApplicationContext());
//        WordViewModel wordViewModel= new WordViewModel();
//        try {
//            wordViewModel.setWords(dictionaryRepo.getWordFromDictionary());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        //setContentView(R.layout.activity_my_dictionary);
//        ActivityWordsExersicesBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_words_exersices);
//        binding.setModel(wordViewModel);
//    }
}
