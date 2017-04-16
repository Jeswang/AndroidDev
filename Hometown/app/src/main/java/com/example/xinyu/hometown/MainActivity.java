package com.example.xinyu.hometown;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements TextWatcher {
    TextView countryText;
    TextView stateText;
    TextView latLngText;
    EditText nicknameEdit;
    EditText passwordEdit;
    EditText yearEdit;
    EditText cityEdit;
    EditText emailEdit;
    boolean isNicknameValid;
    boolean isPasswordValid;
    boolean isYearValid;
    RequestQueue queue;

    private static final String TAG = "EmailPassword";
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private DatabaseHelper namesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        countryText = (TextView)findViewById(R.id.country);
        stateText = (TextView)findViewById(R.id.state);
        latLngText = (TextView)findViewById(R.id.latlng);
        nicknameEdit = (EditText)findViewById(R.id.nickname);
        nicknameEdit.addTextChangedListener(this);
        passwordEdit = (EditText)findViewById(R.id.password);
        passwordEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() < 3) {
                    isPasswordValid = false;
                }
                else {
                    isPasswordValid = true;
                }
            }
        });
        yearEdit = (EditText)findViewById(R.id.year);
        yearEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0) {
                    isYearValid = false;
                    return;
                }
                int y = Integer.parseInt(s.toString());
                if(y > 2017 || y < 1970) {
                    isYearValid = false;
                }
                else {
                    isYearValid = true;
                }
            }
        });
        cityEdit = (EditText)findViewById(R.id.city);
        emailEdit = (EditText)findViewById(R.id.email);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    public void selectCountry(View button) {
        Intent go = new Intent(this,SelectCountryActivity.class);
        startActivity(go);
    }
    public void selectState(View button) {
        Intent go = new Intent(this,SelectStateActivity.class);
        startActivity(go);
    }
    public void selectList(MenuItem selectedMenu) {
        Intent go = new Intent(this,SelectListActivity.class);
        startActivity(go);
    }
    public void selectMap(MenuItem selectedMenu) {
        Intent go = new Intent(this,MapActivity.class);
        startActivity(go);
    }
    public void setLatLng(View button) {
        Intent go = new Intent(this,SetLatLngActivity.class);
        startActivity(go);
    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPref = this.getSharedPreferences("preference", Context.MODE_PRIVATE);
        String country = sharedPref.getString(getString(R.string.saved_country), "");
        String state = sharedPref.getString(getString(R.string.saved_state),"");
        String latLng = sharedPref.getString("saved_lat", "")+ "\n" + sharedPref.getString("saved_lng", "");
        countryText.setText(country);
        stateText.setText(state);
        latLngText.setText(latLng);
    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
           getNicknameRequest(s.toString());
    }
    public void afterTextChanged(Editable s) {}
    public void beforeTextChanged(CharSequence s, int start,int count, int after) {}

    public void getNicknameRequest(String s) {
        Log.i("rew", "Start");
        Response.Listener<Boolean> success = new Response.Listener<Boolean>() {
            public void onResponse(Boolean response) {Log.d("rew", response.toString());
                isNicknameValid = !response;
            }
        };
        Response.ErrorListener failure = new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d("rew", error.toString());
            }
        };
        String url = "http://bismarck.sdsu.edu/hometown/nicknameexists?name="+s;
        BooleanRequest getRequest = new BooleanRequest(0,url,"",success,failure);
        queue = Volley.newRequestQueue(this);
        queue.add(getRequest);
    }
    public void userSubmit(View button) {
        if(!isNicknameValid || !isPasswordValid || !isYearValid || countryText.length()==0 || stateText.length()==0 || cityEdit.length()==0) {
            Context context = getApplicationContext();
            CharSequence text = "Input Invalid";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else {
            getUserInfoPost();
            getUserInfoPostFirebase();
        }
    }
    public void userLogin(View button) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent go = new Intent(this,ChatUserListActivity.class);
            go.putExtra("email",user.getEmail());
            startActivity(go);
        } else {
            Intent go = new Intent(this,SignedInActivity.class);
            startActivity(go);
        }

    }
    public void getUserInfoPost() {
        Log.i("rew", "Start");
        Response.Listener<JSONObject> success = new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                Context context = getApplicationContext();
                CharSequence text = "Submit Success";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        };
        Response.ErrorListener failure = new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d("rew", error.toString());
                Context context = getApplicationContext();
                CharSequence text = "Submit Error";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        };
        String url ="http://bismarck.sdsu.edu/hometown/adduser";
        SharedPreferences sharedPref = this.getSharedPreferences("preference", Context.MODE_PRIVATE);
        try {
            String jsonString;
            if(sharedPref.getString("saved_lat", "").length()==0 || sharedPref.getString("saved_lng", "").length()==0) {
                Intent go = new Intent(this,MapActivity.class);
                startActivity(go);
                return;
            } else {
                jsonString = "{\"nickname\":\""+nicknameEdit.getText()+
                        "\",\"password\":\""+passwordEdit.getText()+
                        "\",\"country\":\""+countryText.getText()+
                        "\",\"state\":\""+stateText.getText()+
                        "\",\"city\":\""+cityEdit.getText()+
                        "\",\"year\":"+yearEdit.getText()+
                        ",\"latitude\":"+sharedPref.getString("saved_lat", "")+
                        ",\"longitude\":"+sharedPref.getString("saved_lng", "")+"}";
            }
            //System.out.println(jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST,url, jsonBody, success, failure);
            queue = Volley.newRequestQueue(this);
            queue.add(getRequest);
        } catch(Exception e) { }
    }
    public void getUserInfoPostFirebase() {
        String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                SharedPreferences sharedPref = MainActivity.this.getSharedPreferences("preference", Context.MODE_PRIVATE);
                String email = emailEdit.getText().toString();
                String nickname = nicknameEdit.getText().toString();
                String longitude = sharedPref.getString("saved_lng", "").toString();
                String latitude = sharedPref.getString("saved_lat", "").toString();
                Person user = new Person(email,nickname,longitude,latitude);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference userList = database.getReference("userList");
                userList.child(nickname).setValue(user);
            }
        });
    }
}
