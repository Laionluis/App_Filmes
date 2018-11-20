package com.example.laion.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class Filmes extends SQLiteOpenHelper {
    
    public static final String DATABASE_NAME = "Filmes.db";
    public static final String FILMES_TABLE_NAME = "filmes";
    public static final String FILMES_COLUMN_ID = "id";
    public static final String FILMES_COLUMN_NAME = "name";
    public static final String FILMES_COLUMN_DATA = "data";
    public static final String FILMES_COLUMN_CHECK = "checked";
    public static final String FILMES_COLUMN_SELECTED = "isItemSelected";
    private HashMap hp;

    public Filmes(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table filmes " +
                        "(id integer primary key, name text, data text, checked integer, isItemSelected integer)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS filmes");
        onCreate(db);
    }

    public boolean insertFilme (String name, String data, int checked, int isItemSelected) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("data", data);
        contentValues.put("checked", checked);
        contentValues.put("isItemSelected", isItemSelected);
        db.insert("filmes", null, contentValues);
        return true;
    }

    public Estrutura_Lista getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from filmes where id="+id+"", null );
        boolean checked = res.getInt(res.getColumnIndex(FILMES_COLUMN_CHECK)) > 0;
        boolean selected = res.getInt(res.getColumnIndex(FILMES_COLUMN_SELECTED)) > 0;
        Estrutura_Lista resultado = new Estrutura_Lista(res.getString(res.getColumnIndex(FILMES_COLUMN_NAME)),
                                                        res.getString(res.getColumnIndex(FILMES_COLUMN_DATA)),
                                                        checked, selected);

        return resultado;
    }

    public ArrayList<Estrutura_Lista> ObterFilmesPorNome(String nome) {
        ArrayList<Estrutura_Lista> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] a = new String[1];
        a[0]       = "%"+nome+"%";
        Cursor res =  db.rawQuery( "select * from filmes where name LIKE ? ", a );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            boolean checked = res.getInt(res.getColumnIndex(FILMES_COLUMN_CHECK)) > 0;
            boolean selected = res.getInt(res.getColumnIndex(FILMES_COLUMN_SELECTED)) > 0;
            array_list.add(new Estrutura_Lista(res.getString(res.getColumnIndex(FILMES_COLUMN_NAME)),
                    res.getString(res.getColumnIndex(FILMES_COLUMN_DATA)),
                    checked, selected));
            res.moveToNext();
        }
        return array_list;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, FILMES_TABLE_NAME);
        return numRows;
    }

    public boolean updateFilme (Integer id, String name, String data, int checked, int isItemSelected) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("data", data);
        contentValues.put("checked", checked);
        contentValues.put("isItemSelected", isItemSelected);
        db.update("filmes", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteFilme (String nome) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("filmes", "name = ? ", new String[] { nome });
    }

    public ArrayList<Estrutura_Lista> getAllFilmes(boolean isStart) {
        ArrayList<Estrutura_Lista> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from filmes", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            boolean checked = res.getInt(res.getColumnIndex(FILMES_COLUMN_CHECK)) > 0;
            boolean selected;
            if(!isStart)
                selected = res.getInt(res.getColumnIndex(FILMES_COLUMN_SELECTED)) > 0;
            else selected = false;
            array_list.add(new Estrutura_Lista(res.getString(res.getColumnIndex(FILMES_COLUMN_NAME)),
                                               res.getString(res.getColumnIndex(FILMES_COLUMN_DATA)),
                                               checked, selected));
            res.moveToNext();
        }
        return array_list;
    }
}
