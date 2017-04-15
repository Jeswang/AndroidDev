package com.example.xinyu.hometown;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xinyu on 3/19/17.
 */

public class SelectStateActivity2 extends AppCompatActivity implements StateListFragment2.OnStateSelectedListener {
    String selectedStateName;
    StateListFragment2 stateList;
    TextView selectedState;
    int numberOfUser = 0;
    Map<String, Person> chatUser = new HashMap<String, Person>();
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
                SelectStateActivity2.this.numberOfUser++;

                if (stateList == null) {
                    stateList = new StateListFragment2();
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
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void onStateSelected(String stateName) {
        Intent getIntent = getIntent();
        String email = getIntent.getStringExtra("email");
        String chatUser2 = chatUser.get(email).getNickname();
        Intent go = new Intent(this,SelectStateActivity3.class);
        go.putExtra("chatUser1",stateName);
        go.putExtra("chatUser2",chatUser2);
        startActivity(go);
    }
}