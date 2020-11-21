package com.example.smithjarod_parkfinder.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.smithjarod_parkfinder.objects.ParkObject;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsFragment extends MapFragment implements OnMapReadyCallback,
        GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {
    public static final String TAG = "TAG.MapsFragment";
    public static final String ARRAY = "com.example.smithjarod_parkfinder.fragments.ARRAY";
    public static final String ISPARK = "com.example.smithjarod_parkfinder.fragments.ISPARK";
    public static final String STATE_CODE = "com.example.smithjarod_parkfinder.fragments.STATE_CODE";

    private GoogleMap mMap;
    private double lat=0;
    private double lon=0;
    private int markerCounter=0;
    private ArrayList<ParkObject> parkObjects = new ArrayList<>();
    boolean isPark;
    String stateCode;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Log.d(TAG, "onCreateView: ");
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    public static MapsFragment newInstance(ArrayList<ParkObject> _obj, boolean _isPark, String _stateCode) {
        //Log.d(TAG, "newInstance: ");
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARRAY,_obj);
        args.putBoolean(ISPARK, _isPark);
        args.putString(STATE_CODE, _stateCode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.d(TAG, "onCreate: ");
        Bundle getBundle = getArguments();
        parkObjects = (ArrayList<ParkObject>) getBundle.getSerializable(ARRAY);
        Log.d(TAG, "onCreate: "+parkObjects.size());
        isPark = getBundle.getBoolean(ISPARK);
        Log.d(TAG, "onCreate: "+isPark);
        stateCode = getBundle.getString(STATE_CODE);
        Log.d(TAG, "onCreate: "+stateCode);

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
//        mMap.setInfoWindowAdapter(this);
//        mMap.setOnInfoWindowClickListener(this);
//        addMapLocations();
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
            Log.d(TAG, "addMapLocations: "+latLng);
            Marker marker = mMap.addMarker(new MarkerOptions().title(obj.getName()).position(latLng));
            marker.setTag(markerCounter);
            markerCounter++;
        }
    }
}