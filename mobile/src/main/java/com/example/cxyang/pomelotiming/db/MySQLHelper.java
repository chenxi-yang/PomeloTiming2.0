package com.example.cxyang.pomelotiming.db;

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
        String create_record = "create table record(id integer primary key autoincrement, package_name char(50), start_time char(20), end_time char(20), total_time integer)";
        db.execSQL(create_plan);
        db.execSQL(create_record);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists planList");
        db.execSQL("drop table if exists record");
        onCreate(db);
    }
}
