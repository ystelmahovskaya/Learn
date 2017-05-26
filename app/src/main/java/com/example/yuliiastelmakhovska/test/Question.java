package com.example.yuliiastelmakhovska.test;

import android.os.Parcel;

import java.util.ArrayList;

/**
 * Created by yuliiastelmakhovska on 2017-05-23.
 */

public class Question implements ContentType{

    String text;
    ArrayList<Answer> answers;
    int correctAnswer;

    public Question(){}
    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public Question(Parcel in) {
        text = in.readString();
        answers= (ArrayList<Answer>) in.readSerializable();
        correctAnswer=in.readInt();
    }

    @Override
    public void setName(String name) {
        this.text= name;
    }

    @Override
    public String getName() {
        return text;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setAnswers(ArrayList<Answer> answers) {
        this.answers = answers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getName());
        dest.writeSerializable(getAnswers());
        dest.writeInt(getCorrectAnswer());

    }
}
