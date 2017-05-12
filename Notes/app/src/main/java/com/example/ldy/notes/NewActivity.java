package com.example.ldy.notes;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NewActivity extends AppCompatActivity {

    private EditText mTitle;
    private EditText mDate;
    private EditText mTime;
    private EditText mContent;

    private Note newNote;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference ref;

    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        mTitle = (EditText)findViewById(R.id.noteTitle);
        mDate = (EditText)findViewById(R.id.setDate);
        mTime = (EditText)findViewById(R.id.setTime);
        mContent = (EditText)findViewById(R.id.noteContent);

        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(v);
            }
        });

        mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(v);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String mUserId = user.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        ref = mDatabase.getReference("notes/" + mUserId);

        Intent getIntent = getIntent();
        key = getIntent.getStringExtra("noteKey");

        if (key != null) {
            ref.child(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Note newNote = dataSnapshot.getValue(Note.class);
                    mTitle.setText(newNote.getTitle());
                    mContent.setText(newNote.getContent());
                    mDate.setText(newNote.getDate());
                    mTime.setText(newNote.getTime());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save,menu);
        return true;
    }

    public void setDate(View v){
        Intent go = new Intent(this, DateActivity.class);
        startActivityForResult(go, 1);
    }

    public void setTime(View v){
        Intent go = new Intent(this, TimeActivity.class);
        startActivityForResult(go, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) {
                    String returnValue = data.getStringExtra("chosenDate");
                    mDate.setText(returnValue);
                }
                break;
            }
            case (2) : {
                if (resultCode == Activity.RESULT_OK) {
                    String returnValue = data.getStringExtra("chosenTime");
                    mTime.setText(returnValue);
                }
                break;
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.saveEvent:
                saveEvent();
                break;
            case R.id.cancelEvent:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveEvent() {
        newNote = new Note();
        newNote.setTitle(mTitle.getText().toString());
        newNote.setContent(mContent.getText().toString());
        newNote.setDate(mDate.getText().toString());
        newNote.setTime(mTime.getText().toString());
        DatabaseReference newNoteRef;
        if (key != null) {
            newNoteRef = ref.child(key);
        } else {
            newNoteRef = ref.push();
        }
        newNoteRef.setValue(newNote);
        newNote.setKey(newNoteRef.getKey());
        finish();
    }
}
