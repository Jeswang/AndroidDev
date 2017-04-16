package com.example.xinyu.hometown;

import android.app.Activity;
import android.app.FragmentTransaction;
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

public class ChatUserListFragment extends ListFragment {
    ArrayAdapter<String> adapter;
    public interface OnUserSelectedListener {
        public void onUserSelected(String userName);
    }

    ChatUserListFragment.OnUserSelectedListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ChatUserListFragment.OnUserSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnStateSelectedListener");
        }
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mListener.onUserSelected(adapter.getItem(position));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);
        adapter = new ArrayAdapter<String>(
                inflater.getContext(), android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        return view;
    }
    public void reload(String s) {
        adapter.add(s);
        adapter.notifyDataSetChanged();
    }
}