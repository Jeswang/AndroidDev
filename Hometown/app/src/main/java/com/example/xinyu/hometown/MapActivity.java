package com.example.xinyu.hometown;

import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinyu on 3/19/17.
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    List<UserInfo> userInfoList;
    RequestQueue queue;
    private boolean mapLoaded;
    private boolean userLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getUserInfoRequest();
        mapLoaded = false;
        userLoaded = false;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mapLoaded = true;
        viewUserMap();
    }

    public void getUserInfoRequest() {
        Log.i("rew", "Start");
        Response.Listener<JSONArray> success = new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray response) {
                Log.d("rew", response.toString());
                userInfoList = new ArrayList<UserInfo>();
                Gson gson = new Gson();
                if (response != null) {
                    int len = response.length();
                    UserInfo[] users = gson.fromJson(response.toString(), UserInfo[].class);
                    for (int i = 0; i < len; i++) {
                        userInfoList.add(users[i]);
                    }
                }
                userLoaded = true;
                viewUserMap();
            }
        };
        Response.ErrorListener failure = new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d("rew", error.toString());
            }
        };
        String url ="http://bismarck.sdsu.edu/hometown/users";
        JsonArrayRequest getRequest = new JsonArrayRequest( url, success, failure);
        queue = Volley.newRequestQueue(this);
        queue.add(getRequest);
    }

    public void viewUserMap() {
        if(mapLoaded && userLoaded) {
            for(int i = 0; i < userInfoList.size(); i++) {
                double latitude = Double.parseDouble(userInfoList.get(i).latitude);
                double longitude = Double.parseDouble(userInfoList.get(i).longitude);
                System.out.println("lat: "+latitude+"lng: "+longitude );
                LatLng latlng = new LatLng(latitude, longitude);
                MarkerOptions classRoom = new MarkerOptions().position(latlng).title(userInfoList.get(i).nickname);
                mMap.addMarker(classRoom);
            }
        }
    }
}
