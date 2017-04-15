package com.example.xinyu.hometown;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by xinyu on 4/12/17.
 */

public class FirebaseCustomArrayAdapter extends ArrayAdapter<Person> {
    private final LayoutInflater mInflater;

    public FirebaseCustomArrayAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_2);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<Person> data) {
        clear();
        if (data != null) {
            for (Person appEntry : data) {
                add(appEntry);
            }
        }
    }

    /**
     * Populate new items in the list.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            view = mInflater.inflate(R.layout.firebase_item, parent, false);
        } else {
            view = convertView;
        }

        Person item = getItem(position);
        ((TextView)view.findViewById(R.id.firebase_item_nickname)).setText(item.getNickname());
        return view;
    }
}
