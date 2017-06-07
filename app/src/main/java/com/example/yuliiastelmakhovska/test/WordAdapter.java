package com.example.yuliiastelmakhovska.test;

import android.annotation.TargetApi;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;

import com.example.yuliiastelmakhovska.test.databinding.WordlistItemBinding;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;


/**
 * Created by yuliiastelmakhovska on 2017-04-20.
 */

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder> {

    private ObservableArrayList<Word> list;
    TextToSpeech t1;
    MyDictionaryRepo repo;
    Context context;
    private final int REQ_CODE_SPEECH_INPUT = 100;


    public WordAdapter(ObservableArrayList<Word> l, Context context){
        list = l;
this.context=context;
        repo= new MyDictionaryRepo(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wordlist_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
         final Word r = list.get(position);

        holder.wordAndTranscription.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                t1=new TextToSpeech(v.getContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if(status != TextToSpeech.ERROR) {
                            t1.setLanguage(Locale.UK);
                            playNextChunk(r.getWord());
                        }
                    }
                });
            }
            private void playNextChunk(String text) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ttsGreater21(text);
                } else {
                    ttsUnder20(text);
                }}
            @SuppressWarnings("deprecation")
            private void ttsUnder20(String text) {
                HashMap<String, String> map = new HashMap<>();
                map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
                t1.speak(text, TextToSpeech.QUEUE_FLUSH, map);
            }
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            private void ttsGreater21(String text) {
                String utteranceId = this.hashCode() + "";
                Bundle params = new Bundle();
                params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "");
                t1.speak(text, TextToSpeech.QUEUE_FLUSH, params, utteranceId);
            }

        });
        holder.record.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                if(v.getContext() instanceof WordsExersices){
                    ((WordsExersices)v.getContext()).promptSpeechInput(r.word);
                    holder.record.setClickable(false);
                    holder.record.setBackgroundResource(R.drawable.ic_mic_off);

                }
                else {

                    View rootView = v.getRootView();
                  Fragment fragment =  (Fragment)(rootView.findViewById(R.id.fragment_dictionary_view).getTag());

                    if (fragment instanceof MyDictionary) {

                        ((MyDictionary) fragment).promptSpeechInput(r.word);

                    }
                }
                                             }
                                         });


            if (!r.getWord().contains(" ")&&repo.isInDictionaryDb(r.getWord())){
                holder.addToDictionary.setChecked(true);

            }

        holder.addToDictionary.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if (holder.addToDictionary.isChecked()){
                    repo.insert(r.getWord(),r.getTranscription(),r.getTranslations().get(0),r.getTranslations().get(1));
                    holder.addToDictionary.setChecked(true);

                }
                else {
                    repo.deleteItemFromDB(r);
                    holder.addToDictionary.setChecked(false);
                    if(!(v.getContext() instanceof WordsExersices)) {
                        View rootView = v.getRootView();
                        Fragment fragment = (Fragment) (rootView.findViewById(R.id.fragment_dictionary_view).getTag());
                        try {
                            ((MyDictionary) fragment).wordViewModel.setWords(((MyDictionary) fragment).dictionaryRepo.getWordFromDictionary());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        ((MyDictionary) fragment).binding.setModel(((MyDictionary) fragment).wordViewModel);
                    }
                }
            }
        });
        holder.binder.setWord(r);
        holder.binder.executePendingBindings();
    }


    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        else
            return 0;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        public WordlistItemBinding binder;
        View itemView;
        Button wordAndTranscription;
        Button record;
        ToggleButton addToDictionary;

        public ViewHolder(View v) {
            super(v);
            itemView = v;
            binder = DataBindingUtil.bind(v);
            wordAndTranscription = (Button) v.findViewById(R.id.wordAndTranscription);
            record = (Button) v.findViewById(R.id.record);
            record.setBackgroundResource(R.drawable.ic_mic);
            addToDictionary = (ToggleButton) v.findViewById(R.id.addToDictionary);
        }
    }
}
