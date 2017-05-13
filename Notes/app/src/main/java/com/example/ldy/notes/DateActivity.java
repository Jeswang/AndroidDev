package com.example.ldy.notes;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

public class DateActivity extends AppCompatActivity {

    private DatePicker datePicker;
    private String chosenDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);

        datePicker = (DatePicker)findViewById(R.id.dateChoose);

    }

    public void saveDate(View view) {
        int date = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();
        chosenDate = String.valueOf(year) + " - " + String.valueOf(month) + " - " + String.valueOf(date);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("chosenDate", chosenDate);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
