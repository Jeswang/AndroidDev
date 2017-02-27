package com.example.xinyu.simpledrawing;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private SimpleDrawingView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = (SimpleDrawingView)findViewById(R.id.view);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(true);
        switch(item.getItemId()) {
            case R.id.draw:
                displayText("Drawing");
                view.mode = 0;
                break;
            case R.id.delete:
                displayText("Deleting");
                view.mode = 1;
                break;
            case R.id.move:
                displayText("Moving");
                view.mode = 2;
                break;
            case R.id.blue:
                view.color = Color.BLUE;
                break;
            case R.id.red:
                view.color = Color.RED;
                break;
            case R.id.green:
                view.color = Color.GREEN;
                break;
            case R.id.black:
                view.color = Color.BLACK;
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void displayText(String text) {
        TextView output = (TextView)findViewById(R.id.current_mode);
        output.setText(text);
    }
}
