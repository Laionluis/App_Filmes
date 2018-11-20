package com.example.laion.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class Estrutura_Lista implements Parcelable {
    public String nome;
    public String data;
    public boolean checked;
    public boolean isItemSelected;

    Estrutura_Lista(String name, String data, boolean checked,boolean isItemSelected) {
        this.nome = name;
        this.data = data;
        this.checked = checked;
        this.isItemSelected = isItemSelected;
    }

    private Estrutura_Lista(Parcel in) {
        nome = in.readString();
        data = in.readString();
        checked = in.readInt() != 0;
        isItemSelected = in.readInt() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeString(data);
        dest.writeInt(checked ? 1 : 0);
        dest.writeInt(isItemSelected ? 1 : 0);
    }

    public static final Parcelable.Creator<Estrutura_Lista> CREATOR = new Parcelable.Creator<Estrutura_Lista>() {
        public Estrutura_Lista createFromParcel(Parcel in) {
            return new Estrutura_Lista(in);
        }

        public Estrutura_Lista[] newArray(int size) {
            return new Estrutura_Lista[size];
        }
    };

    @Override
    public String toString() {
        return nome + data + checked + isItemSelected;
    }

}

