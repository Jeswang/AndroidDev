package com.example.ldy.notes;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

public class TimeActivity extends AppCompatActivity {
    /*TimePicker timePicker;
    int hour;
    int minute1;
    public static String titleTime;*/

    private TimePicker timePicker;
    private String chosenTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        timePicker = (TimePicker)findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                int hour = hourOfDay;
                int minutes = minute;
                chosenTime = String.valueOf(hour) + " : " + String.valueOf(minutes);
            }
        });
    }

    public void saveTime(View view) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("chosenTime", chosenTime);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    /*public void SAVETIME(View view){
        Intent go = new Intent(this,NewActivity.class);
        NewActivity.hourchoose=hour;
        NewActivity.minutechoose=minute1;
        NewActivity.titlenow = titleTime;
        startActivity(go);
    }*/
}
