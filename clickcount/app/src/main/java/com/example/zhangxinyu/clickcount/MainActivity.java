package com.example.zhangxinyu.clickcount;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView countOutput;
    TextView countOutputBg;
    int count = 0;
    int backgroundCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        countOutput = (TextView)this.findViewById(R.id.countOutput);
        countOutputBg = (TextView)this.findViewById(R.id.countOutputBg);
    }

    public void increase(View button) {
        Log.i("rew", "increase");
        count++;
        countOutput.setText(Integer.toString(count));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!isChangingConfigurations()){
            backgroundCount += 1;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        countOutput.setText(Integer.toString(count));
        countOutputBg.setText(Integer.toString(backgroundCount));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt("countOutput", count);
        outState.putInt("countOutputBg", backgroundCount);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        count = savedInstanceState.getInt("countOutput");
        backgroundCount = savedInstanceState.getInt("countOutputBg");
    }
}
