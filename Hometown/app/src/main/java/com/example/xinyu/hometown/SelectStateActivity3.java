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

/**
 * Created by xinyu on 3/19/17.
 */

public class SelectStateActivity3 extends AppCompatActivity implements StateListFragment3.OnStateSelectedListener {
    String selectedStateName;
    StateListFragment3 stateList;
    TextView selectedState;
    int numberOfMessage = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_state);
        findViewById(R.id.selected_state).setVisibility(View.INVISIBLE);
        //
        Intent getIntent = getIntent();
        String chatUser1 = getIntent.getStringExtra("chatUser1");
        String chatUser2 = getIntent.getStringExtra("chatUser2");
        //
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("chatList/"+chatUser1+"-"+chatUser2);
        final FragmentManager fm = getFragmentManager();


        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // A new user has been added, add it to the displayed list
                Message newMessage = dataSnapshot.getValue(Message.class);
                SelectStateActivity3.this.numberOfMessage++;

                if (stateList == null) {
                    stateList = new StateListFragment3();
                    stateList.numbersText = new String[1];

                    stateList.numbersText[0] = newMessage.getUser() + ": " + newMessage.getContent() + " time: " + newMessage.getTimeString();

                    fm.beginTransaction().add(android.R.id.content, stateList).commit();
                } else {
                    String[] newNumbersText = new String[numberOfMessage];
                    for(int i = 0; i < numberOfMessage-1; i++) {
                        newNumbersText[i] = stateList.numbersText[i];
                    }
                    newNumbersText[numberOfMessage-1] = newMessage.getUser() + ": " + newMessage.getContent() + " time: " + newMessage.getTimeString();
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
        selectedStateName = stateName;
        selectedState = (TextView)findViewById(R.id.selected_state);
        selectedState.setText(selectedStateName);
        SharedPreferences sharedPref = this.getSharedPreferences("preference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.saved_state), selectedState.getText().toString());
        editor.commit();
        finish();
    }
}