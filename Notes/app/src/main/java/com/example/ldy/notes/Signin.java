package com.example.ldy.notes;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.ldy.notes.R.id.newEvent;
import static com.example.ldy.notes.R.id.textView;

public class Signin extends AppCompatActivity {
    EditText account;
    EditText password;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        account = (EditText)findViewById(R.id.editText);
        password = (EditText)findViewById(R.id.editText2);

        mAuth = FirebaseAuth.getInstance();

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("rnote/");
        ValueEventListener refListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MainActivity.dataTitle.clear();
                MainActivity.dataReminder.clear();
                MainActivity.dataContent.clear();
                MainActivity.dataemail.clear();
                MainActivity.dataTime.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    MainActivity.dataemail.add(postSnapshot.child("email").getValue().toString());
                    MainActivity.dataContent.add(postSnapshot.child("content").getValue().toString());
                    MainActivity.dataReminder.add(postSnapshot.child("reminder").getValue().toString());
                    MainActivity.dataTitle.add(postSnapshot.child("title").getValue().toString());
                    MainActivity.dataTime.add(postSnapshot.child("createTime").getValue().toString());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        ref.addValueEventListener(refListener);


    }
    public void SIGNin(View view){
       final String email = account.getText().toString();
        String passwordEnter = password.getText().toString();
        mAuth.signInWithEmailAndPassword(email, passwordEnter)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            MainActivity.usernow = email;
                            NewEvent.Usernow = email;
                            Intent go = new Intent(getApplication(),MainActivity.class);
                            startActivity(go);
                        }
                        if (!task.isSuccessful()) {
                            Log.i("rew", "signInWithEmail:failed", task.getException());
                            Toast.makeText(getApplicationContext(), "wrong email or password", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
    public void SIGNup(View view){

    }
}
