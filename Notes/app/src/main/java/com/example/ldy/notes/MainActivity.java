package com.example.ldy.notes;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

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

    public boolean onCreateOptionsMenu(Menu menu) {
       // menu.add(0, 1, 0, "New Event");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add,menu);
        return true;
    }

}
