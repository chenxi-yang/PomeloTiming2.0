package com.example.cxyang.pomelotiming;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLHelper extends SQLiteOpenHelper {
    private Context MyContext;
    public MySQLHelper(Context context, String name) {
        super(context, name, null, 1);
        MyContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_plan = "create table planList(id integer primary key autoincrement, date char(10), start_time char(10), end_time char(10), name text, done integer)";
        db.execSQL(create_plan);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists planList");
        onCreate(db);
    }
}
