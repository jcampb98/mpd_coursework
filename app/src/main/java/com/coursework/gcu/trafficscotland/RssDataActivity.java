package com.coursework.gcu.trafficscotland;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Name: Joshua Campbell
 * Matric No: S2024472
 */

public class RssDataActivity extends AppCompatActivity implements  OnMapReadyCallback {
    private GoogleMap mMap;
    Intent intent;
    ParseClass mParseClass;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        double[] parsedMapPos = getLatAndLong(mParseClass.getGeorss());
        LatLng mapPos = new LatLng(parsedMapPos[0], parsedMapPos[1]);
        mMap.addMarker(new MarkerOptions().position(mapPos).title(mParseClass.getTitle()).visible(true));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mapPos, 14));
    }

    public double[] getLatAndLong(String latlng) {
        String lat = latlng.substring(0, latlng.indexOf(' '));
        String lng = latlng.substring(latlng.indexOf(' '));
        double[] result = {Double.parseDouble(lat), Double.parseDouble(lng)};
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_rss);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        TextView title = findViewById(R.id.traffic_act_title);
        TextView description = findViewById(R.id.traffic_act_desc);
        TextView startDate = findViewById(R.id.traffic_act_start_date);
        TextView endDate = findViewById(R.id.traffic_act_end_date);
        TextView link = findViewById(R.id.traffic_act_link);
        TextView rssgeo = findViewById(R.id.traffic_act_geo);
        TextView pubDate = findViewById(R.id.traffic_act_pub);
        TextView roadworksLn = findViewById(R.id.traffic_act_road_ln);

        intent = getIntent();
        mParseClass = intent.getParcelableExtra("TRAFFIC_DATA");

        title.setText(mParseClass.getTitle());
        Log.e("MyTag", mParseClass.getTitle());
        description.setText(mParseClass.getDescription());
        startDate.setText(mParseClass.getStartDateAsString());
        endDate.setText(mParseClass.getEndDateAsString());
        rssgeo.setText(mParseClass.getGeorss());
        pubDate.setText(mParseClass.getPubDate());
        roadworksLn.setText(mParseClass.getRoadworksLength()+""); // convert to string
    }
}
