package com.example.ldy.notes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import static com.example.ldy.notes.R.id.datePicker;

public class Datachoose extends AppCompatActivity {
    DatePicker datechoose;
    Button savedate;
    int daynow;
    int yearnow;
    int monthnow;
    int daychoose;
    int yearchoose;
    int monthchoose;
    public static String titleDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datechoose);
        datechoose = (DatePicker)findViewById(R.id.datePicker2);
        savedate = (Button)findViewById(R.id.Savedate);
        daynow=datechoose.getDayOfMonth();
        yearnow=datechoose.getYear();
        monthnow=datechoose.getMonth();
        datechoose.init(yearnow, monthnow, daynow, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int month, int day) {
                daychoose=day;
                yearchoose=year;
                monthchoose=month;
            }
        });
    }
    public void SAVEDATE(View view){
        Intent go = new Intent(this,NewEvent.class);
        NewEvent.monthchoose=monthchoose;
        NewEvent.daychoose=daychoose;
        NewEvent.yearchoose=yearchoose;
        NewEvent.titlenow = titleDate;
        startActivity(go);
    }
}
