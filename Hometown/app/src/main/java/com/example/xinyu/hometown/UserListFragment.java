package com.example.xinyu.hometown;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import static android.R.id.list;

/**
 * Created by xinyu on 3/19/17.
 */

public class UserListFragment extends ListFragment {
    List<UserInfo> userInfos;
    CustomArrayAdapter adapter;
    public interface OnUserSelectedListener {
        public void onUserSelected(UserInfo ListName);
        public void userListViewCreated();
    }
    UserListFragment.OnUserSelectedListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (UserListFragment.OnUserSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnUserSelectedListener");
        }
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mListener.onUserSelected(userInfos.get(position));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        adapter = new CustomArrayAdapter(inflater.getContext());
        setListAdapter(adapter);
        adapter.setData(userInfos);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    public void updateData(List<UserInfo> userInfos) {
        this.userInfos = userInfos;
        adapter.setData(userInfos);
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
