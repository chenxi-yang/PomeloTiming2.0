package com.example.cxyang.pomelotiming;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.ViewPager;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;

import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ldf.calendar.Utils;
import com.ldf.calendar.component.CalendarAttr;
import com.ldf.calendar.component.CalendarViewAdapter;
import com.ldf.calendar.interf.OnSelectDateListener;
import com.ldf.calendar.model.CalendarDate;
import com.ldf.calendar.view.Calendar;
import com.ldf.calendar.view.MonthPager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class DisplayMessageActivity extends AppCompatActivity implements View.OnClickListener {

    //public static final String localHost = "http://localhost:80";
    public static final String serverHost = "http://45.32.5.192:80";
    public static final String SERVER_URL = serverHost;

    private String userid;

    TextView tvYear;
    TextView tvMonth;
    TextView backToday;
    CoordinatorLayout content;
    MonthPager monthPager;
    RecyclerView rvToDoList;


    private ArrayList<Calendar> currentCalendars = new ArrayList<>();
    private CalendarViewAdapter calendarAdapter;
    private OnSelectDateListener onSelectDateListener;
    private int mCurrentPage = MonthPager.CURRENT_DAY_INDEX;
    private RecycleAdapter adapter;
    private Context context;
    private DataBaseServer db;
    private CalendarDate currentDate;
    private boolean initiated = false;

    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        userid = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        //TextView textView = findViewById(R.id.textView);
        //textView.setText(message);

        TextView button = findViewById(R.id.new_plan);
        button.setOnClickListener(this);

        context = this;
        content = findViewById(R.id.content);
        monthPager = findViewById(R.id.calendar_view);
        //此处强行setViewHeight，毕竟你知道你的日历牌的高度
        monthPager.setViewHeight(Utils.dpi2px(context, 270));
        tvYear = findViewById(R.id.show_year_view);
        tvMonth = findViewById(R.id.show_month_view);
        backToday = findViewById(R.id.back_today_button);


        db = new DataBaseServer(this, "plan.db");
        initCurrentDate();
        List<Plan> nameList = db.getPlanListByDay(currentDate);

        rvToDoList = findViewById(R.id.list);
        rvToDoList.setHasFixedSize(true);

        rvToDoList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecycleAdapter(this, nameList, db);
        rvToDoList.setAdapter(adapter);
        rvToDoList.setItemAnimator(new DefaultItemAnimator());

        initCalendarView();
        initToolbarClickListener();

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    }
    private List<String> InitData() {
        List<String> mDatas = new ArrayList<String>();
        mDatas.add("Current Date: " + currentDate.toString());

        return mDatas;
    }
    public void onClick(View v)
    {
        final Intent intent = new Intent(this, PlanSettingActivity.class); // intent: switch from setting to results showing
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.layoutdialog, null);

        AlertDialog dlg = new AlertDialog.Builder(this)
                .setTitle("New Plan")
                .setView(textEntryView)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public String getTime(String date, String time) {
                                String ret = "";
                                int i;
                                // Change Date to TimeStamp
                                for (i = 0; i < 4; i ++) ret = ret + date.charAt(i);
                                if (date.charAt(6) == '/' || date.charAt(6) == '.') {
                                    ret = ret + "0" + date.charAt(5);
                                    if (date.length() == 8)
                                        ret = ret + "0" + date.charAt(7);
                                    else ret = ret + date.charAt(7) + date.charAt(8);
                                } else {
                                    ret = ret + date.charAt(5) + date.charAt(6);
                                    if (date.length() == 9)
                                        ret = ret + "0" + date.charAt(8);
                                    else ret = ret + date.charAt(8) + date.charAt(9);
                                }
                                // Change Time to TimeStamp
                                if (time.charAt(1) == ':' || date.charAt(1) == '.') {
                                    ret = ret + "0" + time.charAt(0);
                                    if (time.length() == 3)
                                        ret = ret + "0" + time.charAt(2);
                                    else ret = ret + time.charAt(2) + time.charAt(3);
                                } else {
                                    ret = ret + time.charAt(0) + time.charAt(1);
                                    if (time.length() == 4)
                                        ret = ret + "0" + time.charAt(3);
                                    else ret = ret + time.charAt(3) + time.charAt(4);
                                }
                                return ret;
                            }
                            private void setAlarmClock(String time) {
                                Intent intent = new Intent(DisplayMessageActivity.this, AlarmActivity.class);

                                java.util.Calendar calendar = java.util.Calendar.getInstance();
                                calendar.setTimeInMillis(System.currentTimeMillis());

                                java.util.Calendar alarm_time = java.util.Calendar.getInstance();

                                int year = Integer.parseInt(time.substring(0, 4));
                                int month = Integer.parseInt(time.substring(4, 6));
                                int day = Integer.parseInt(time.substring(6, 8));
                                int hour = Integer.parseInt(time.substring(8, 10));
                                int minute = Integer.parseInt(time.substring(10, 12));

                                alarm_time.set(year, month - 1, day, hour, minute);

                                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                                PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                                    am.setExact(AlarmManager.RTC_WAKEUP, alarm_time.getTimeInMillis(), pi);
                                else
                                    am.set(AlarmManager.RTC_WAKEUP, alarm_time.getTimeInMillis(), pi);

                            }
                            
                            public void onClick(DialogInterface dialog, int whichButton) {

                                String st_planName = ((EditText) textEntryView.findViewById(R.id.text_planname)).getText().toString();

                                EditText start_date = textEntryView.findViewById(R.id.startdate);
                                EditText end_date = textEntryView.findViewById(R.id.enddate);
                                EditText start_time = textEntryView.findViewById(R.id.starttime);
                                EditText end_time = textEntryView.findViewById(R.id.endtime);

                                String st_startTime = getTime(start_date.getText().toString(), start_time.getText().toString());
                                String st_endTime = getTime(end_date.getText().toString(), end_time.getText().toString());

        /*                        HashMap<String, String> params = new HashMap<String, String>();
                                params.put("start_time", st_startTime);
                                params.put("end_time", st_endTime);
                                params.put("plan_name", st_planName);

                                Log.e("post", params.toString());
                                String reqString = SERVER_URL + "/home/plan_setting/" + userid;

                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, reqString, new JSONObject(params),
                                        new Response.Listener<JSONObject>(){
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                Log.e("normalResponse", response.toString());
                                                System.out.println(response.toString());
                                            }
                                        }, new Response.ErrorListener(){
                                    @Override
                                    public void onErrorResponse(VolleyError arg0){
                                        Log.e("errorResponse", arg0.toString());
                                    }
                                });
                                MySingleton.getInstance(DisplayMessageActivity.this).addToRequestQueue(jsonObjectRequest);*/

                                Plan newplan = new Plan(start_date.getText().toString(), start_time.getText().toString(), end_time.getText().toString(), st_planName);

                                db.AddPlan(newplan);
                                List<Plan> namelist = db.getPlanListByDay(currentDate);

                                adapter.ChangeData(namelist);
<<<<<<< HEAD
                                setAlarmClock(st_startTime);
=======

                                startActivity(intent);
>>>>>>> 1b0e1225b3f92011bd22ca1142bf2965e409a425
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {}
                        })
                .create();
        dlg.show();
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !initiated) {
            refreshMonthPager();
            initiated = true;
        }
    }

    /*
     * 如果你想以周模式启动你的日历，请在onResume是调用
     * Utils.scrollTo(content, rvToDoList, monthPager.getCellHeight(), 200);
     * calendarAdapter.switchToWeek(monthPager.getRowIndex());
     * */

    @Override
    protected void onResume() {
        Utils.scrollTo(content, rvToDoList, monthPager.getCellHeight(), 200);
        calendarAdapter.switchToWeek(monthPager.getRowIndex());
        super.onResume();
    }

    /**
     * 初始化对应功能的listener
     *
     * @return void
     */

    private void initToolbarClickListener() {
        backToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBackToDayBtn();
            }
        });
    }

    /**
     * 初始化currentDate
     *
     * @return void
     */

    private void initCurrentDate() {
        currentDate = new CalendarDate();
        tvYear.setText(currentDate.getYear() + "年");
        tvMonth.setText(currentDate.getMonth() + "");
    }

    /**
     * 初始化CustomDayView，并作为CalendarViewAdapter的参数传入
     */

    private void initCalendarView() {
        initListener();
        CustomDayView customDayView = new CustomDayView(context, R.layout.custom_day);
        calendarAdapter = new CalendarViewAdapter(
                context,
                onSelectDateListener,
                CalendarAttr.WeekArrayType.Monday,
                customDayView);
        calendarAdapter.setOnCalendarTypeChangedListener(new CalendarViewAdapter.OnCalendarTypeChanged() {
            @Override
            public void onCalendarTypeChanged(CalendarAttr.CalendarType type) {
                rvToDoList.scrollToPosition(0);
            }
        });
        initMarkData();
        initMonthPager();
    }

    /**
     * 初始化标记数据，HashMap的形式，可自定义
     * 如果存在异步的话，在使用setMarkData之后调用 calendarAdapter.notifyDataChanged();
     */

    private void initMarkData() {
        HashMap<String, String> markData = new HashMap<>();
        markData.put("2017-8-9", "1");
        markData.put("2017-7-9", "0");
        markData.put("2017-6-9", "1");
        markData.put("2017-6-10", "0");
        calendarAdapter.setMarkData(markData);
    }

    private void initListener() {
        onSelectDateListener = new OnSelectDateListener() {
            @Override
            public void onSelectDate(CalendarDate date) {
                refreshClickDate(date);
            }

            @Override
            public void onSelectOtherMonth(int offset) {
                //偏移量 -1表示刷新成上一个月数据 ， 1表示刷新成下一个月数据
                monthPager.selectOtherMonth(offset);
            }
        };
    }


    private void refreshClickDate(CalendarDate date) {
        currentDate = date;
        tvYear.setText(date.getYear() + "年");
        tvMonth.setText(date.getMonth() + "");

        List<Plan> namelist = db.getPlanListByDay(currentDate);
        adapter.ChangeData(namelist);
    }

    /**
     * 初始化monthPager，MonthPager继承自ViewPager
     *
     * @return void
     */

    private void initMonthPager() {
        monthPager.setAdapter(calendarAdapter);
        monthPager.setCurrentItem(MonthPager.CURRENT_DAY_INDEX);
        monthPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                position = (float) Math.sqrt(1 - Math.abs(position));
                page.setAlpha(position);
            }
        });
        monthPager.addOnPageChangeListener(new MonthPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPage = position;
                currentCalendars = calendarAdapter.getPagers();
                if (currentCalendars.get(position % currentCalendars.size()) != null) {
                    CalendarDate date = currentCalendars.get(position % currentCalendars.size()).getSeedDate();
                    currentDate = date;
                    tvYear.setText(date.getYear() + "年");
                    tvMonth.setText(date.getMonth() + "");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void onClickBackToDayBtn() {
        refreshMonthPager();
    }

    private void refreshMonthPager() {
        CalendarDate today = new CalendarDate();
        calendarAdapter.notifyDataChanged(today);
        tvYear.setText(today.getYear() + "年");
        tvMonth.setText(today.getMonth() + "");
    }
}
