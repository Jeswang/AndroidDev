package com.example.ldy.notes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class NoteActivity extends AppCompatActivity {
    EditText noteshow;
    Button back;
    public static String chooseTitle;
    public static List<String > chooseNote = new ArrayList<>();
    public static List<String > chooseTitleHere = new ArrayList<>();
    String chooseNoteShow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shownote);
        noteshow = (EditText)findViewById(R.id.shownotetest);
        back = (Button)findViewById(R.id.BACKNOTE);
        for (int i=0;i<chooseNote.size();i++){
            if(chooseTitleHere.get(i).equals(chooseTitle)){
                chooseNoteShow=chooseNote.get(i).toString();
            }
        }
        noteshow.setText(chooseNoteShow);
    }

    public void BACKTONOTE(View view){
        Intent go =new Intent(this, MainActivity.class);
        startActivity(go);
    }
}
