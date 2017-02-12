package com.example.zhangxinyu.personalinfo;

import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SelectCountry extends AppCompatActivity implements CountryListFragment.OnCountrySelectedListener, StateListFragment.OnStateSelectedListener {

    String selectedCountryName;
    String selectedStateName;
    CountryListFragment countryList;
    StateListFragment stateList;
    TextView selectedCountryState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_country);
        FragmentManager fm = getFragmentManager();

        if (fm.findFragmentById(android.R.id.content) == null) {
            countryList = new CountryListFragment();
            fm.beginTransaction().add(android.R.id.content, countryList).commit();
        }
        findViewById(R.id.selected_country_state).setVisibility(View.INVISIBLE);
        findViewById(R.id.btn_country_done).setVisibility(View.INVISIBLE);
        findViewById(R.id.btn_country_cancel).setVisibility(View.INVISIBLE);
    }
    public void onCountrySelected(String countryName, int stateArrayId) {
        selectedCountryName = countryName;
        FragmentManager fm = getFragmentManager();
        stateList = new StateListFragment();
        String[] stateArray = getResources().getStringArray(stateArrayId);
        stateList.numbers_text = stateArray;
        fm.beginTransaction().replace(android.R.id.content, stateList).commit();
        fm.popBackStack();
    }
    public void onStateSelected(String stateName) {
        selectedStateName = stateName;
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().remove(stateList).commit();
        fm.popBackStack();
        selectedCountryState = (TextView)findViewById(R.id.selected_country_state);
        selectedCountryState.setText(selectedCountryName + "-" + selectedStateName);
        findViewById(R.id.selected_country_state).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_country_done).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_country_cancel).setVisibility(View.VISIBLE);
    }
    public void countryDone(View button) {
        SharedPreferences sharedPref = this.getSharedPreferences("preference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.saved_country), selectedCountryState.getText().toString());
        editor.commit();
        finish();
    }
    public void countryCancel(View button) {
        finish();
    }

}
