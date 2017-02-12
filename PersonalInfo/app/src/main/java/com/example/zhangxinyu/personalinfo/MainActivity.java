package com.example.zhangxinyu.personalinfo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText firstNameText;
    EditText familyNameText;
    EditText ageText;
    EditText emailText;
    EditText phoneText;
    TextView birthdateText;
    TextView countryText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firstNameText = (EditText)findViewById(R.id.first_name);
        familyNameText = (EditText)findViewById(R.id.family_name);
        ageText = (EditText)findViewById(R.id.age);
        emailText = (EditText)findViewById(R.id.email);
        phoneText = (EditText)findViewById(R.id.phone);
        birthdateText = (TextView)findViewById(R.id.birthdate);
        countryText = (TextView)findViewById(R.id.country_state);
        SharedPreferences sharedPref = this.getSharedPreferences("preference", Context.MODE_PRIVATE);
        String firstName = sharedPref.getString(getString(R.string.saved_first_name), "");
        String familyName = sharedPref.getString(getString(R.string.saved_family_name), "");
        String age = sharedPref.getString(getString(R.string.saved_age), "");
        String email = sharedPref.getString(getString(R.string.saved_email), "");
        String phone= sharedPref.getString(getString(R.string.saved_phone), "");
        String countryState = sharedPref.getString(getString(R.string.saved_country),"");
        firstNameText.setText(firstName);
        familyNameText.setText(familyName);
        ageText.setText(age);
        emailText.setText(email);
        phoneText.setText(phone);
    }

    public void selectDate(View button) {
        Intent go = new Intent(this,SelectDate.class);
        startActivity(go);
    }
    public void selectCountry(View button) {
        Intent go = new Intent(this,SelectCountry.class);
        startActivity(go);
    }
    public void onClick(View button) {
        SharedPreferences sharedPref = this.getSharedPreferences("preference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.saved_first_name), firstNameText.getText().toString());
        editor.putString(getString(R.string.saved_family_name), familyNameText.getText().toString());
        editor.putString(getString(R.string.saved_age), ageText.getText().toString());
        editor.putString(getString(R.string.saved_email), emailText.getText().toString());
        editor.putString(getString(R.string.saved_phone), phoneText.getText().toString());
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPref = this.getSharedPreferences("preference", Context.MODE_PRIVATE);
        String birthdate = sharedPref.getString(getString(R.string.saved_birthdate), "");
        birthdateText.setText(birthdate);
        String countryState = sharedPref.getString(getString(R.string.saved_country), "");
        countryText.setText(countryState);
    }
}
