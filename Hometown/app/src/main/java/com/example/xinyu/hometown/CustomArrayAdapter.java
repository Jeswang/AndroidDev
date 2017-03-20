package com.example.xinyu.hometown;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by xinyu on 3/19/17.
 */

public class CustomArrayAdapter extends ArrayAdapter<UserInfo> {
    private final LayoutInflater mInflater;

    public CustomArrayAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_2);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<UserInfo> data) {
        clear();
        if (data != null) {
            for (UserInfo appEntry : data) {
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
            view = mInflater.inflate(R.layout.single_item, parent, false);
        } else {
            view = convertView;
        }

        UserInfo item = getItem(position);
        ((TextView)view.findViewById(R.id.list_nickname)).setText(item.nickname);
        ((TextView)view.findViewById(R.id.list_county)).setText(item.country);
        ((TextView)view.findViewById(R.id.list_state)).setText(item.state);
        ((TextView)view.findViewById(R.id.list_city)).setText(item.city);

        return view;
    }
}
