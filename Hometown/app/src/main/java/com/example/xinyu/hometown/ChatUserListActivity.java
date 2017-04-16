package com.example.xinyu.hometown;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class ChatUserListActivity extends AppCompatActivity implements ChatUserListFragment.OnUserSelectedListener {
    String selectedStateName;
    ChatUserListFragment stateList;
    TextView selectedState;
    int numberOfUser = 0;
    Map<String, Person> chatUser = new HashMap<String, Person>();

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_state);
        findViewById(R.id.selected_state).setVisibility(View.INVISIBLE);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("userList");
        final FragmentManager fm = getFragmentManager();
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // A new user has been added, add it to the displayed list
                Person newUser = dataSnapshot.getValue(Person.class);
                chatUser.put(newUser.getEmail(), newUser);
                ChatUserListActivity.this.numberOfUser++;

                if (stateList == null) {
                    stateList = new ChatUserListFragment();
                    stateList.numbersText = new String[1];

                    stateList.numbersText[0] = newUser.getNickname();

                    fm.beginTransaction().add(android.R.id.content, stateList).commit();
                } else {
                    String[] newNumbersText = new String[numberOfUser];
                    for(int i = 0; i < numberOfUser-1; i++) {
                        newNumbersText[i] = stateList.numbersText[i];
                    }
                    newNumbersText[numberOfUser-1] = newUser.getNickname();
                    stateList.numbersText = newNumbersText;
                    stateList.reload();
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

    public void logOut(MenuItem selectedMenu) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        finish();
    }

    public void onUserSelected(String userName) {
        Intent getIntent = getIntent();
        String email = getIntent.getStringExtra("email");
        String chatUser2 = chatUser.get(email).getNickname();
        Intent go = new Intent(this,ChatContentListActivity.class);
        go.putExtra("chatUser1",userName);
        go.putExtra("chatUser2",chatUser2);
        startActivity(go);
    }
}