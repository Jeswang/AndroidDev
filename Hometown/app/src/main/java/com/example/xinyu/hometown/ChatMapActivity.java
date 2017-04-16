package com.example.xinyu.hometown;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinyu on 4/15/17.
 */

public class ChatMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    public List<Person> allUsers;
    public boolean mapLoaded;
    public boolean userLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapLoaded = false;
        userLoaded = false;
        allUsers = new ArrayList<Person>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("userList");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // A new user has been added, add it to the displayed list
                Person newUser = dataSnapshot.getValue(Person.class);
                allUsers.add(newUser);
                userLoaded = true;
                ChatMapActivity.this.viewUserMap();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mapLoaded = true;
        viewUserMap();
    }

    public void viewUserMap() {
        if(mapLoaded && userLoaded) {
            for(int i = 0; i < allUsers.size(); i++) {
                double latitude = Double.parseDouble(allUsers.get(i).getLatitude());
                double longitude = Double.parseDouble(allUsers.get(i).getLongitude());
                //System.out.println("lat: "+latitude+"lng: "+longitude );
                LatLng latlng = new LatLng(latitude, longitude);
                MarkerOptions classRoom = new MarkerOptions().position(latlng).title(allUsers.get(i).getNickname());
                mMap.addMarker(classRoom);
            }
        }
    }
}
