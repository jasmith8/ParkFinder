package com.example.smithjarod_parkfinder.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.smithjarod_parkfinder.DetailParkActivity;
import com.example.smithjarod_parkfinder.Map_Helper;
import com.example.smithjarod_parkfinder.objects.ParkObject;
import com.example.smithjarod_parkfinder.objects.StateObject;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MapsFragment extends MapFragment implements OnMapReadyCallback,
        GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {
    public static final String TAG = "TAG.MapsFragment";
    public static final String ARRAY = "com.example.smithjarod_parkfinder.fragments.ARRAY";
    public static final String ISPARK = "com.example.smithjarod_parkfinder.fragments.ISPARK";
    public static final String STATE_CODE = "com.example.smithjarod_parkfinder.fragments.STATE_CODE";

    private GoogleMap mMap;
    private double getGetStateLat=0;
    private double getStateLon=0;
    private double addLat=0;
    private double addlon=0;
    private int markerCounter=0;
    private ArrayList<ParkObject> parkObjects = new ArrayList<>();
    private ArrayList<StateObject> stateObjects = new ArrayList<>();
    private ArrayList<ParkObject> filteredObjects = new ArrayList<>();
    boolean isPark;
    String stateCode;
    Map_Helper helper = new Map_Helper();
    LatLng latLng;
    Context context;


    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Log.d(TAG, "onCreateView: ");
        viewGroup.removeAllViews();
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.d(TAG, "onCreate: ");
        context = getContext();
        Bundle getBundle = getArguments();
        parkObjects = (ArrayList<ParkObject>) getBundle.getSerializable(ARRAY);
        stateObjects = helper.getStateInfo();
        isPark = getBundle.getBoolean(ISPARK);
        stateCode = getBundle.getString(STATE_CODE);
        for (StateObject obj: stateObjects){
            if (obj.stateCode().equals(stateCode)){
                getGetStateLat = obj.lat();
                getStateLon = obj.lon();
            }
        }

        filteredObjects = (ArrayList<ParkObject>) parkObjects.stream().filter(parkObject -> !parkObject.getLatitude().isEmpty()).collect(Collectors.toList());
        latLng = new LatLng(getGetStateLat,getStateLon);
    }

    private void zoomCamera(){
        Log.d(TAG, "zoomCamera: "+latLng);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,5);
        mMap.animateCamera(cameraUpdate);
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
        mMap.setInfoWindowAdapter(this);
        mMap.setOnInfoWindowClickListener(this);
        zoomCamera();
        addMapLocations();
    }


    void addMapLocations(){
        if (mMap ==null){
            return;
        }
        Log.d(TAG, "addMapLocations: "+filteredObjects.size());
        for(ParkObject obj:filteredObjects){
            Log.d(TAG, "addMapLocations: "+obj.getName());
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    String tagString = ""+marker.getTag();
                    int tagInt = Integer.parseInt(tagString);
                    Log.d(TAG, "onInfoWindowClick: ");
                    //open detail screen from here
                    String parkId = obj.getParkId();
                    Intent intent= new Intent(context, DetailParkActivity.class);
                    Log.d(TAG, "onItemClick: ispark "+isPark);
                    if (isPark){
                        intent.putExtra(DetailParkActivity.EXTRA_INFO, DetailParkActivity.PARKS);
                    } else {
                        intent.putExtra(DetailParkActivity.EXTRA_INFO, DetailParkActivity.CAMPS);
                    }
                    intent.putExtra(DetailParkActivity.EXTRA_INFO_2,parkId);
                    startActivity(intent);
                }
            });

            addLat = Double.parseDouble(obj.getLatitude());
            addlon = Double.parseDouble(obj.getLongitude());
            LatLng latLng = new LatLng(addLat,addlon);
            Log.d(TAG, "addMapLocations: "+latLng);
            Marker marker = mMap.addMarker(new MarkerOptions().title(obj.getName()).position(latLng));
            marker.setTag(markerCounter);
            markerCounter++;
        }
    }
}