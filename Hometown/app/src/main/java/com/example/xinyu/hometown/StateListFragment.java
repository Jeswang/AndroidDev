package com.example.xinyu.hometown;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by xinyu on 3/19/17.
 */

public class StateListFragment extends ListFragment {
    String[] numbersText;
    public interface OnStateSelectedListener {
        public void onStateSelected(String countryName);
    }

    StateListFragment.OnStateSelectedListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (StateListFragment.OnStateSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnStateSelectedListener");
        }
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mListener.onStateSelected(numbersText[(int)position]);
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