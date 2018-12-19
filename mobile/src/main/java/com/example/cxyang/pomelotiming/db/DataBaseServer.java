package com.example.cxyang.pomelotiming.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.cxyang.pomelotiming.Plan.Plan;
import com.ldf.calendar.model.CalendarDate;

import java.util.ArrayList;
import java.util.List;

public class DataBaseServer {

    private MySQLHelper sqlHelper;

    public DataBaseServer(Context context, String name) {
        sqlHelper = new MySQLHelper(context, name);
    }
    public void addRecord(String packageName, String startTime, String endTime, long totalTime) {
        SQLiteDatabase db = this.sqlHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("package_name", packageName);
        values.put("start_time", startTime);
        values.put("end_time", endTime);
        values.put("total_time", totalTime);

        db.insert("record", null, values);
        db.close();
    }
    public int findPlan(String start_date, String start_time) {
        SQLiteDatabase localSQLiteDatabase = this.sqlHelper.getWritableDatabase();
        Cursor localCursor = localSQLiteDatabase.rawQuery("select * from planList  " +
                "where date = ? and start_time = ? ", new String[]{start_date, start_time});
        int cnt = 0;
        while (localCursor.moveToNext())
            cnt ++;
        localSQLiteDatabase.close();
        return cnt;
    }
    public List<Plan> getPlanListByDay(CalendarDate day) {
        List<Plan> localArrayList=new ArrayList<Plan>();
        SQLiteDatabase localSQLiteDatabase = this.sqlHelper.getWritableDatabase();
        Cursor localCursor = localSQLiteDatabase.rawQuery("select start_time, end_time, name from planList  " +
                "where date = ? order by start_time", new String[]{day.toString()});

        while (localCursor.moveToNext())
        {
            Plan temp = new Plan();
            temp.set_start_time(localCursor.getString(localCursor.getColumnIndex("start_time")));
            temp.set_end_time(localCursor.getString(localCursor.getColumnIndex("end_time")));
            temp.set_name(localCursor.getString(localCursor.getColumnIndex("name")));
            temp.set_date(day.toString());
            localArrayList.add(temp);
        }
        localSQLiteDatabase.close();
        return localArrayList;
    }

    public void AddPlan(Plan p) {
        SQLiteDatabase db = this.sqlHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", p.get_date());
        values.put("start_time", p.get_start_time());
        values.put("end_time", p.get_end_time());
        values.put("name", p.get_name());
        values.put("done", 0);

        db.insert("planList", null, values);
        db.close();
    }

    public void DeletePlan(Plan p) {
        SQLiteDatabase db = this.sqlHelper.getWritableDatabase();
        db.delete("planList", "start_time = ? and end_time = ?", new String[]{p.get_start_time(), p.get_end_time()});
        db.close();
    }
}
