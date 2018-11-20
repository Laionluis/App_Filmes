package com.example.laion.myapplication;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

public class Filme_suggestion implements SearchSuggestion {

    private String titulo_filme;
    private int id;
    private boolean mIsHistory = false;

    public Filme_suggestion(String suggestion, int id) {
        this.titulo_filme = suggestion.toLowerCase();
        this.id = id;
    }

    public Filme_suggestion(Parcel source) {
        this.titulo_filme = source.readString();
        this.mIsHistory = source.readInt() != 0;
    }

    public void setIsHistory(boolean isHistory) {
        this.mIsHistory = isHistory;
    }

    public boolean getIsHistory() {
        return this.mIsHistory;
    }

    @Override
    public String getBody() {
        return titulo_filme;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(titulo_filme);
        dest.writeInt(mIsHistory ? 1 : 0);
    }

    public static final Creator<Filme_suggestion> CREATOR = new Creator<Filme_suggestion>() {
        @Override
        public Filme_suggestion createFromParcel(Parcel in) {
            return new Filme_suggestion(in);
        }

        @Override
        public Filme_suggestion[] newArray(int size) {
            return new Filme_suggestion[size];
        }
    };
}
