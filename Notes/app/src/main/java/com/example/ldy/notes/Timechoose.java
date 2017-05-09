package com.example.ldy.notes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

public class Timechoose extends AppCompatActivity {
    TimePicker timePicker;
    int hour;
    int minute1;
    public static String titleTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timechoose);
        timePicker = (TimePicker)findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener()
        {

            @Override
            public void onTimeChanged(TimePicker view
                    , int hourOfDay, int minute)
            {
                hour = hourOfDay;
                minute1 = minute;
//
            }
        });
    }

    public void SAVETIME(View view){
        Intent go = new Intent(this,NewEvent.class);
        NewEvent.hourchoose=hour;
        NewEvent.minutechoose=minute1;
        NewEvent.titlenow = titleTime;
        startActivity(go);
    }
}
