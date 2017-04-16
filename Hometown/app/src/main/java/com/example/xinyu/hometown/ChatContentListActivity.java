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

public class ChatContentListActivity extends AppCompatActivity implements ChatContentListFragment.OnUserSelectedListener {
    ChatContentListFragment contentList;
    String chatUser1;
    String chatUser2;

    private String m_Text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_content_layout);
        contentList = (ChatContentListFragment) getFragmentManager().findFragmentById(R.id.contentList);
        Intent getIntent = getIntent();
        chatUser1 = getIntent.getStringExtra("chatUser1");
        chatUser2 = getIntent.getStringExtra("chatUser2");
        contentList.loadUser(chatUser1,chatUser2);
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
    public void onUserSelected(String userName) {

    }
}