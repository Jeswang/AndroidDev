package com.example.xinyu.hometown;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xinyu on 3/19/17.
 */

public class ChatUserListActivity extends AppCompatActivity implements ChatUserListFragment.OnUserSelectedListener, ChatContentListFragment.OnUserSelectedListener {
    String selectedStateName;
    ChatUserListFragment stateList;
    TextView selectedState;
    Map<String, Person> chatUser = new HashMap<String, Person>();

    private FirebaseAuth mAuth;

    boolean mDualPane;
    String chatUser1;
    String chatUser2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        stateList = (ChatUserListFragment) getFragmentManager().findFragmentById(R.id.userList);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("userList");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // A new user has been added, add it to the displayed list
                Person newUser = dataSnapshot.getValue(Person.class);
                chatUser.put(newUser.getEmail(), newUser);
                stateList.reload(newUser.getNickname());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.select_chat_map_menu, menu);
        return true;
    }

    public void selectChatMap(MenuItem selectedMenu) {
        Intent go = new Intent(this,ChatMapActivity.class);
        Intent getIntent = getIntent();
        String email = getIntent.getStringExtra("email");
        String chatUser2 = chatUser.get(email).getNickname();
        go.putExtra("chatUser2",chatUser2);
        startActivity(go);
    }

    public void sendMessage(MenuItem selectedMenu) {
        if(chatUser1 == null || chatUser2 == null) {
            return;
        }
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
                String m_Text = input.getText().toString();
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

    public void logOut(MenuItem selectedMenu) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        finish();
    }

    public void onUserSelected(String userName) {
        View detailsFrame = findViewById(R.id.chatDetails);
        Intent getIntent = getIntent();
        String email = getIntent.getStringExtra("email");
        chatUser1 = userName;
        chatUser2 = chatUser.get(email).getNickname();
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
        if(mDualPane) {
            // Check what fragment is currently shown, replace if needed.
            ChatContentListFragment details = (ChatContentListFragment) getFragmentManager().findFragmentById(R.id.contentList);
            if (details == null || !details.previousUser.equals(userName)) {
                // Make new fragment to show this selection.
                details = new ChatContentListFragment();
                details.previousUser = userName;

                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.chatDetails, details);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                details.loadUser(userName,chatUser2);
            }
        } else {

            Intent go = new Intent(this,ChatContentListActivity.class);
            go.putExtra("chatUser1",userName);
            go.putExtra("chatUser2",chatUser2);
            startActivity(go);
        }
    }
}