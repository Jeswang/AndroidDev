package com.example.xinyu.hometown;

import android.app.FragmentManager;
import android.content.Context;
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

import org.json.JSONArray;

/**
 * Created by xinyu on 3/18/17.
 */

public class SelectCountryActivity extends AppCompatActivity implements CountryListFragment.OnCountrySelectedListener {
    String selectedCountryName;
    CountryListFragment countryList;
    TextView selectedCountry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_country);
        getRequest();
        findViewById(R.id.selected_country).setVisibility(View.INVISIBLE);
    }
    public void getRequest() {
        Log.i("rew", "Start");
        Response.Listener<JSONArray> success = new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray response) {
                Log.d("rew", response.toString());
                FragmentManager fm = getFragmentManager();
                if (fm.findFragmentById(android.R.id.content) == null) {
                    countryList = new CountryListFragment();
                    countryList.numbersText = new String[response.length()];
                    if (response != null) {
                        int len = response.length();
                        for (int i = 0; i < len; i++) {
                            countryList.numbersText[i] = response.optString(i);
                        }
                    }
                    fm.beginTransaction().add(android.R.id.content, countryList).commit();
                }
            } };
        Response.ErrorListener failure = new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d("rew", error.toString());
            }
        };
        String url ="http://bismarck.sdsu.edu/hometown/countries";
        JsonArrayRequest getRequest = new JsonArrayRequest( url, success, failure);
        RequestQueue queue = Volley.newRequestQueue(this); queue.add(getRequest);
    }

    public void onCountrySelected(String countryName) {
        selectedCountryName = countryName;
        selectedCountry = (TextView)findViewById(R.id.selected_country);
        selectedCountry.setText(selectedCountryName);
        SharedPreferences sharedPref = this.getSharedPreferences("preference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.saved_country), selectedCountry.getText().toString());
        editor.commit();
        finish();
    }
}
