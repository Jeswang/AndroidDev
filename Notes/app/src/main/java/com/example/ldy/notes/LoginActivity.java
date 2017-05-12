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

public class LoginActivity extends AppCompatActivity {
    private EditText mEmailView;
    private EditText mPasswordView;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (EditText)findViewById(R.id.email);
        mPasswordView = (EditText)findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            loginSuccess();
        }
        //mDatabase = FirebaseDatabase.getInstance();
        //DatabaseReference ref = mDatabase.getReference("rnote/");

        /*ValueEventListener refListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MainActivity.dataTitle.clear();
                MainActivity.dataReminder.clear();
                MainActivity.dataContent.clear();
                MainActivity.dataEmail.clear();
                MainActivity.dataTime.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    MainActivity.dataEmail.add(postSnapshot.child("email").getValue().toString());
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
        ref.addValueEventListener(refListener);*/
    }
    public void signIn(View view){
        String mEmail = mEmailView.getText().toString();
        String mPassword = mPasswordView.getText().toString();
        mAuth.signInWithEmailAndPassword(mEmail, mPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                       loginSuccess();
                    }
                    if (!task.isSuccessful()) {
                        Log.i("rew", "signInWithEmail:failed", task.getException());
                        Toast.makeText(getApplicationContext(), "wrong email or password", Toast.LENGTH_SHORT).show();
                    }
                    }
                });
    }

    public void loginSuccess() {
        FirebaseUser user = mAuth.getCurrentUser();
        String mUserId = user.getUid();
        Intent go = new Intent(LoginActivity.this,MainActivity.class);
        go.putExtra("userId", mUserId);
        startActivity(go);
    }
    public void signUp(View view){
        String email1= mEmailView.getText().toString();
        String password1=mPasswordView.getText().toString();
        mAuth.createUserWithEmailAndPassword(email1,password1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
                loginSuccess();
            }
            else{
                Toast.makeText(LoginActivity.this,"Already email or password 6 digits at least",Toast.LENGTH_SHORT).show();
            }
            }
        });
    }
}
