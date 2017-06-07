package com.example.yuliiastelmakhovska.test;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by yuliiastelmakhovska on 2017-05-06.
 */

public class Word implements Parcelable , Serializable, ContentType {
    String word;
    String transcription;
    ArrayList<String> translations= new ArrayList<>();

public Word(){}
    protected Word(Parcel in) {
        word = in.readString();
        transcription = in.readString();
        translations = in.createStringArrayList();
    }

    public static final Creator<Word> CREATOR = new Creator<Word>() {
        @Override
        public Word createFromParcel(Parcel in) {
            return new Word(in);
        }

        @Override
        public Word[] newArray(int size) {
            return new Word[size];
        }
    };

    public String getWord() {
        return word;
    }

    public String getTranscription() {
        return transcription;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }

    public ArrayList<String> getTranslations() {
        return translations;
    }

    public void setTranslations(ArrayList<String> translations) {
        this.translations = translations;
    }
    public void addTranslation(String translation){
        this.translations.add(translation);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(word);
        dest.writeString(transcription);
        dest.writeStringList(translations);
    }


    @Override
    public void setName(String name) {

    }

    @Override
    public String getName() {
        return word;
    }
}
