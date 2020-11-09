package com.example.smithjarod_parkfinder.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smithjarod_parkfinder.ParkObject;
import com.example.smithjarod_parkfinder.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Objects;

public class MapsFragment extends SupportMapFragment implements OnMapReadyCallback,
        GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {
    public static final String TAG = "TAG.MapsFragment";
    public static final String ARRAY = "com.example.smithjarod_parkfinder.fragments.ARRAY";

    private GoogleMap mMap;
    private double lat;
    private double lon;
    private int markerCounter=0;
    private ArrayList<ParkObject> parkObjects;



    public static MapsFragment newInstance() {
        Bundle args = new Bundle();
        MapsFragment fragment = new MapsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.d(TAG, "onCreate: ");

        parkObjects = (ArrayList<ParkObject>) Objects.requireNonNull(getArguments()).getSerializable(ARRAY);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (supportMapFragment !=null){
            supportMapFragment.getMapAsync(this);
        }
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        Log.d(TAG, "onActivityCreated: ");
        getMapAsync(this);
    }


    @Override
    public View getInfoWindow(Marker marker) {
        Log.d(TAG, "getInfoWindow: ");
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        Log.d(TAG, "getInfoContents: ");
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.d(TAG, "onInfoWindowClick: ");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: ");
        mMap = googleMap;
        //mMap.setInfoWindowAdapter(this);
        //mMap.setOnInfoWindowClickListener(this);
        //addMapLocations();
    }


    void addMapLocations(){
        if (mMap ==null){
            return;
        }
        for(ParkObject obj:parkObjects){
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    String tagString = ""+marker.getTag();
                    int tagInt = Integer.parseInt(tagString);
                    //TODO open detail activity

                }
            });

            lat = Double.parseDouble(obj.getLatitude());
            lon = Double.parseDouble(obj.getLongitude());
            LatLng latLng = new LatLng(lat,lon);
            Marker marker = mMap.addMarker(new MarkerOptions().title(obj.getName()).position(latLng));
            marker.setTag(markerCounter);
            markerCounter++;
        }
    }
}