package com.example.xinyu.hometown;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by xinyu on 3/19/17.
 */

public class SetLatLngActivity extends AppCompatActivity implements OnMapReadyCallback{
    private GoogleMap nMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_lat_lng);
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map_latlng);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        nMap = googleMap;
        SharedPreferences sharedPref = this.getSharedPreferences("preference", Context.MODE_PRIVATE);
        double latitude = Double.parseDouble(sharedPref.getString("saved_lat", "0"));
        double longitude = Double.parseDouble(sharedPref.getString("saved_lng", "0"));
        if(latitude != 0 && longitude != 0) {
            LatLng latlng = new LatLng(latitude, longitude);
            MarkerOptions classRoom = new MarkerOptions().position(latlng).title("current location");
            nMap.addMarker(classRoom);
            CameraUpdate center=
                    CameraUpdateFactory.newLatLng(latlng);
            nMap.moveCamera(center);
        }
        nMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {

                MarkerOptions marker = new MarkerOptions().position(
                        new LatLng(point.latitude, point.longitude)).title("New Marker");
                nMap.addMarker(marker);
                //latLng = (TextView)findViewById(R.id.latlng);
                //latLng = point.latitude +" / " + point.longitude);
                System.out.println(point.latitude+"---"+ point.longitude);
                SharedPreferences sharedPref = getSharedPreferences("preference", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.saved_lat), String.valueOf(point.latitude));
                editor.putString(getString(R.string.saved_lng), String.valueOf(point.longitude));
                editor.commit();
                finish();
            }
        });
    }
}
