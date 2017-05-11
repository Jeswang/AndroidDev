package com.example.ldy.notes;

import android.content.Intent;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static String usernow;
    DatePicker datePicker;
    private int Tday;
    private int Tyear;
    private int Tmonth;
    private int chooseday;
    private int chooseyear;
    private int choosemonth;
    ListView Listview;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    public static List<String > dataemail = new ArrayList<>();
    public static List<String > dataTitle = new ArrayList<>();
    public static List<String > dataContent = new ArrayList<>();
    public static List<String > dataReminder = new ArrayList<>();
    public static List<String > dataTime = new ArrayList<>();
    private List<String> choose = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        Tyear = datePicker.getYear();
        Tday = datePicker.getDayOfMonth();
        Tmonth = datePicker.getMonth();
        Listview = (ListView)findViewById(R.id.LISTVIEW);
        chooseday = Tday;
        choosemonth = Tmonth;
        chooseyear = Tyear;
        NewEvent.yearchoose=Tyear;
        NewEvent.daychoose=Tday;
        NewEvent.monthchoose=Tmonth;

       // Listview.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strs));

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("rnote/");
        ValueEventListener refListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataTitle.clear();
                dataReminder.clear();
                dataContent.clear();
                dataemail.clear();
                dataTime.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    dataemail.add(postSnapshot.child("email").getValue().toString());
                    dataContent.add(postSnapshot.child("content").getValue().toString());
                    dataReminder.add(postSnapshot.child("reminder").getValue().toString());
                    dataTitle.add(postSnapshot.child("title").getValue().toString());
                    dataTime.add(postSnapshot.child("createTime").getValue().toString());
                }
                Log.i("mmm",dataTitle.get(1));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        ref.addValueEventListener(refListener);
        Log.i("time",dataTime.get(0));
       // Listview.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataTitle));
        Listview.setOnItemClickListener(new ItemClickEvent());
        Log.i("user", usernow);
        Log.i("year", Integer.valueOf(Tyear).toString());
        datePicker.init(Tyear, Tmonth, Tday, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int month, int day) {
                chooseyear=year;
                choosemonth=month+1;
                chooseday=day;
                choose.clear();
                String mm = chooseyear+"."+choosemonth+"."+chooseday;
                for (int i=0;i<dataTime.size();i++){
                    if(dataTime.get(i).equals(mm)){
                        choose.add(dataTitle.get(i));
                    }
                }
                Listview.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, choose));
            }
        });
    }

    private final class ItemClickEvent implements AdapterView.OnItemClickListener {
        @Override
        //这里需要注意的是第三个参数arg2，这是代表单击第几个选项
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            //通过单击事件，获得单击选项的内容
            String text = Listview.getItemAtPosition(arg2)+"";
            Log.i("choose",choose.get(arg2));
            Shownote.chooseTitle=choose.get(arg2);
            Shownote.chooseNote=dataContent;
            Shownote.chooseTitleHere=dataTitle;
            Intent go = new Intent(getApplication(),Shownote.class);
            startActivity(go);
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
       // menu.add(0, 1, 0, "New Event");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.addnew,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //int id = item.getItemId();
        switch (item.getItemId()){
            case R.id.newEvent:
                Intent go = new Intent(this, NewEvent.class);
                startActivity(go);
                break;
            case R.id.LOGout:
                mAuth.signOut();
                Intent go1 = new Intent(this, Signin.class);
                startActivity(go1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
