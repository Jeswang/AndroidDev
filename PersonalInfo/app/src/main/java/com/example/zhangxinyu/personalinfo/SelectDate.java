package com.example.zhangxinyu.personalinfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SelectDate extends AppCompatActivity {
    DatePicker birthdatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date);
        birthdatePicker = (DatePicker)findViewById(R.id.date_picker);
    }
    public void dateDone(View button){
        SharedPreferences sharedPref = this.getSharedPreferences("preference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Calendar calendar = this.getCalendarFromDatePicker(birthdatePicker);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format.format(calendar.getTime());
        editor.putString(getString(R.string.saved_birthdate), formatted);
        editor.commit();
        finish();
    }
    public void dateCancel(View button){
        finish();
    }
    public static java.util.Calendar getCalendarFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar;
    }
}
