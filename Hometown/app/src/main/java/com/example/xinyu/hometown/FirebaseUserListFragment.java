package com.example.xinyu.hometown;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

/**
 * Created by xinyu on 4/12/17.
 */

public class FirebaseUserListFragment extends ListFragment {
    List<Person> persons;
    FirebaseCustomArrayAdapter adapter;
    public interface OnUserSelectedListener {
        public void onUserSelected(Person ListName);
        public void userListViewCreated();
    }
    FirebaseUserListFragment.OnUserSelectedListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (FirebaseUserListFragment.OnUserSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnUserSelectedListener");
        }
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mListener.onUserSelected(persons.get(position));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        adapter = new FirebaseCustomArrayAdapter(inflater.getContext());
        setListAdapter(adapter);
        adapter.setData(persons);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    public void updateData(List<Person> persons) {
        this.persons = persons;
        adapter.setData(persons);
    }
    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        mListener.userListViewCreated();
    }
    public void reload() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
}
