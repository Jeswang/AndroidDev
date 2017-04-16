package com.example.xinyu.hometown;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by xinyu on 3/19/17.
 */

public class ChatContentListActivity extends AppCompatActivity {
    ChatContentListFragment contentList;
    int numberOfMessage = 0;
    String chatUser1;
    String chatUser2;

    private String m_Text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_state);
        findViewById(R.id.selected_state).setVisibility(View.INVISIBLE);
        //
        Intent getIntent = getIntent();
        chatUser1 = getIntent.getStringExtra("chatUser1");
        chatUser2 = getIntent.getStringExtra("chatUser2");
        //
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("chatList/"+chatUser1+"-"+chatUser2);
        final FragmentManager fm = getFragmentManager();


        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // A new user has been added, add it to the displayed list
                Message newMessage = dataSnapshot.getValue(Message.class);
                ChatContentListActivity.this.numberOfMessage++;

                if (contentList == null) {
                    contentList = new ChatContentListFragment();
                    contentList.numbersText = new String[1];

                    contentList.numbersText[0] = newMessage.getUser() + ": " + newMessage.getContent() + " time: " + newMessage.getTimeString();

                    fm.beginTransaction().add(android.R.id.content, contentList).commit();
                } else {
                    String[] newNumbersText = new String[numberOfMessage];
                    for(int i = 0; i < numberOfMessage-1; i++) {
                        newNumbersText[i] = contentList.numbersText[i];
                    }
                    newNumbersText[numberOfMessage-1] = newMessage.getUser() + ": " + newMessage.getContent() + " time: " + newMessage.getTimeString();
                    contentList.numbersText = newNumbersText;
                    contentList.reload();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_menu, menu);
        return true;
    }

    public void sendMessage(MenuItem selectedMenu) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Message");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference mList = database.getReference("chatList/"+chatUser1+"-"+chatUser2);
                Message m = new Message();
                m.user = chatUser2;
                m.content = m_Text;
                m.time = System.currentTimeMillis()/1000;
                mList.push().setValue(m);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}