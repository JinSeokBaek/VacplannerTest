package com.js980112.vacplannertest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {

    public MyDBHelper(Context context) {
        super(context, "MyData.db",null,1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1=String.format("create table %s(" +
                "%s integer primary key autoincrement," +
                "%s text," +
                "%s BLOB);","WishList","w_num","w_title","w_img");
        db.execSQL(query1);

    }
}
