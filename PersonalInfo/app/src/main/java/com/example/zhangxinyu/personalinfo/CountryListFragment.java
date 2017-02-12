package com.example.zhangxinyu.personalinfo;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by zhangxinyu on 2/12/17.
 */

public class CountryListFragment extends ListFragment
{
    String[] numbersText = new String[] { "China", "India", "Mexico", "USA" };
    int[] stateArrayIds = new int[] {R.array.china, R.array.india, R.array.mexico, R.array.usa};
    public interface OnCountrySelectedListener {
        public void onCountrySelected(String countryName, int stateArrayId);
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
        mListener.onCountrySelected(numbersText[(int)id], stateArrayIds[(int)id]);
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
