package com.example.ldy.notes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewEvent extends AppCompatActivity {
    EditText Title;
    EditText Starts;
    EditText Time;
    EditText Note;
    Button Save2;
    public static int yearchoose;
    public static int daychoose;
    public static int monthchoose;
    public static int hourchoose;
    public static int minutechoose;
    FirebaseDatabase database;
    public static String Usernow;
    public static String titlenow;

    public Note newNote;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newevent);
        Starts = (EditText)findViewById(R.id.Starts);
        Title = (EditText)findViewById(R.id.Title);
        Time = (EditText)findViewById(R.id.Time);
        Note = (EditText)findViewById(R.id.Note);
        Save2 = (Button)findViewById(R.id.Save2);
        Starts.setText(Integer.valueOf(yearchoose).toString()+"."+Integer.valueOf(monthchoose+1).toString()+"."+Integer.valueOf(daychoose).toString());
        Time.setText(Integer.valueOf(hourchoose).toString()+" : "+Integer.valueOf(minutechoose).toString());
        Title.setText(titlenow);
        database = FirebaseDatabase.getInstance();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.saveevent,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //int id = item.getItemId();
        switch (item.getItemId()){
            case R.id.cancelEvent:
                Intent go = new Intent(this, MainActivity.class);
                startActivity(go);
                break;
            case R.id.saveEvent:
                newNote = new Note();
                newNote.setTitle(Title.getText().toString());
                newNote.setContent(Note.getText().toString());
                newNote.setReminder(Time.getText().toString());
                newNote.setCreateTime(Starts.getText().toString());
                newNote.setEmail(Usernow);
               // FirebaseDatabase database = FirebaseDatabase.getInstance();
                ref = database.getReference("rnote/");
                DatabaseReference newNoteRef = ref.push();
                newNoteRef.setValue(newNote);
                newNote.setKey(newNoteRef.getKey());
                Intent go1 = new Intent(this, MainActivity.class);
                startActivity(go1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void timechoose(View view){
        Timechoose.titleTime = Title.getText().toString();
       Intent go = new Intent(this,Timechoose.class);
        startActivity(go);
    }
    public void datechoose(View view){
        Datachoose.titleDate = Title.getText().toString();
        Intent go = new Intent(this,Datachoose.class);
        startActivity(go);
    }
    public void CANCEL(View view){
        Intent go = new Intent(this,MainActivity.class);
        startActivity(go);
    }



}
