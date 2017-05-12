package com.example.ldy.notes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    /*public static String usernow;
    DatePicker datePicker;

    private int Tday;
    private int Tyear;
    private int Tmonth;
    private int chooseDay;
    private int chooseYear;
    private int chooseMonth;
    ListView Listview;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    public static List<String > dataEmail = new ArrayList<>();
    public static List<String > dataTitle = new ArrayList<>();
    public static List<String > dataContent = new ArrayList<>();
    public static List<String > dataReminder = new ArrayList<>();
    public static List<String > dataTime = new ArrayList<>();
    private List<String> choose = new ArrayList<>();*/

    private String mUserId;
    private CalendarView calendarView;
    private ListView listView;
    private DatabaseReference dbref;
    private ArrayList<Note> notesList;
    private Map<String, ArrayList<Note>> notesMap;

    private FirebaseAuth mAuth;
    private boolean isDateSelected;
    private String chosenDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isDateSelected = false;

        mAuth = FirebaseAuth.getInstance();

        Intent getIntent = getIntent();
        mUserId = getIntent.getStringExtra("userId");

        initView();
    }

    public void initView() {
        calendarView = (CalendarView) findViewById(R.id.calendarView);
        listView = (ListView)findViewById(R.id.listView);
        notesList =  new ArrayList<>();
        notesMap = new HashMap<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbref = database.getReference("notes/" + mUserId);
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notesList.clear();
                notesMap.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Note newNote = postSnapshot.getValue(Note.class);
                    newNote.setKey(postSnapshot.getKey());
                    notesList.add(newNote);
                    if(notesMap.containsKey(newNote.getDate())) {
                        notesMap.get(newNote.getDate()).add(newNote);
                    } else {
                        notesMap.put(newNote.getDate(), new ArrayList<Note>());
                        notesMap.get(newNote.getDate()).add(newNote);
                    }
                }
                updateListView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
            @Override
            public void onSelectedDayChange (CalendarView calendarView, int year, int month, int dayOfMonth) {
                isDateSelected = true;
                month++;
                chosenDate = String.valueOf(year) + " - " + String.valueOf(month) + " - " + String.valueOf(dayOfMonth);
                updateListView();
            }
        });

    }
    public void updateListView() {
        ArrayList<String> notesTitle = new ArrayList<>();
        if (isDateSelected) {
            if (notesMap.get(chosenDate) != null) {
                for (Note note : notesMap.get(chosenDate) ) {
                    notesTitle.add(note.getTitle());
                }
            }
        } else {
            for (Note note : notesList ) {
                notesTitle.add(note.getTitle());
            }
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, notesTitle);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new itemClickEvent());
    }

    private final class itemClickEvent implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            Note note = notesList.get(arg2);
            Intent go = new Intent(getApplication(), NewActivity.class);
            go.putExtra("noteKey", note.getKey());
            startActivity(go);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                Intent go = new Intent(this, NewActivity.class);
                startActivity(go);
                break;
            case R.id.logout:
                mAuth.signOut();
                Intent go1 = new Intent(this, LoginActivity.class);
                startActivity(go1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

        /*


        Listview = (ListView)findViewById(R.id.listView);
        chooseDay = Tday;
        chooseMonth = Tmonth;
        chooseYear = Tyear;
        NewActivity.yearchoose=Tyear;
        NewActivity.daychoose=Tday;
        NewActivity.monthchoose=Tmonth;

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
                dataEmail.clear();
                dataTime.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    dataEmail.add(postSnapshot.child("email").getValue().toString());
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
        Listview.setOnItemClickListener(new itemClickEvent());
        Log.i("user", usernow);
        Log.i("year", Integer.valueOf(Tyear).toString());
        datePicker.init(Tyear, Tmonth, Tday, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int month, int day) {
                chooseYear=year;
                chooseMonth=month+1;
                chooseDay=day;
                choose.clear();
                String mm = chooseYear+"."+chooseMonth+"."+chooseDay;
                for (int i=0;i<dataTime.size();i++){
                    if(dataTime.get(i).equals(mm)){
                        choose.add(dataTitle.get(i));
                    }
                }
                Listview.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, choose));
            }
        });*/

    /* private final class itemClickEvent implements AdapterView.OnItemClickListener {
        @Override
        //这里需要注意的是第三个参数arg2，这是代表单击第几个选项
       public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            //通过单击事件，获得单击选项的内容
            String text = Listview.getItemAtPosition(arg2)+"";
            Log.i("choose",choose.get(arg2));
            NoteActivity.chooseTitle=choose.get(arg2);
            NoteActivity.chooseNote=dataContent;
            NoteActivity.chooseTitleHere=dataTitle;
            Intent go = new Intent(getApplication(),NoteActivity.class);
            startActivity(go);
        }
    }*/


    public boolean onCreateOptionsMenu(Menu menu) {
       // menu.add(0, 1, 0, "New Event");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add,menu);
        return true;
    }

    /*public boolean onOptionsItemSelected(MenuItem item) {
        //int id = item.getItemId();
        switch (item.getItemId()){
            case R.id.newEvent:
                Intent go = new Intent(this, NewActivity.class);
                startActivity(go);
                break;
            case R.id.LOGout:
                mAuth.signOut();
                Intent go1 = new Intent(this, LoginActivity.class);
                startActivity(go1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
