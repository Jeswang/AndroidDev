package com.example.xinyu.hometown;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by xinyu on 3/19/17.
 */

public class ChatContentListFragment extends ListFragment {
    ArrayAdapter<String> adapter;
    public String previousUser = "";
    public interface OnUserSelectedListener {
        public void onUserSelected(String userName);
    }

    ChatContentListFragment.OnUserSelectedListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ChatContentListFragment.OnUserSelectedListener) activity;
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

    public void loadUser(String user1, String user2) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("chatList/"+user1+"-"+user2);
        final FragmentManager fm = getFragmentManager();

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // A new user has been added, add it to the displayed list
                Message newMessage = dataSnapshot.getValue(Message.class);
                String content = newMessage.getUser() + ": " + newMessage.getContent() + " time: " + newMessage.getTimeString();
                reload(content);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}