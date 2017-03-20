package com.example.xinyu.hometown;

import android.app.Activity;
import android.app.ListFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;


/**
 * Created by xinyu on 3/18/17.
 */

public class CountryListFragment extends ListFragment {
    String[] numbersText;
    public interface OnCountrySelectedListener {
        public void onCountrySelected(String countryName);
    }

    OnCountrySelectedListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnCountrySelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCountrySelectedListener");
        }
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mListener.onCountrySelected(numbersText[(int)position]);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                inflater.getContext(), android.R.layout.simple_list_item_1,
                numbersText);
        setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
