package com.example.cxyang.pomelotiming;

public class Plan {
    public String start_time;
    public String end_time;
    public String name;
    public String date;

    public Plan() {

    }
    public  Plan(String Date, String start_time, String end_time, String name) {
        this.date = Date;
        this.start_time = start_time;
        this.end_time = end_time;
        this.name = name;
    }
    public String get_start_time() {
        return start_time;
    }
    public String get_date() { return date; }
    public String get_end_time() {
        return end_time;
    }
    public String get_name() {
        return name;
    }

    public void set_start_time(String start_time) { this.start_time = start_time; }
    public void set_end_time(String end_time) { this.end_time = end_time; }
    public void set_date(String date) { this.date = date; }
    public void set_name(String name) { this.name = name; }
}
