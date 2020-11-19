package com.example.smithjarod_parkfinder.fragments;

import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.smithjarod_parkfinder.objects.ParkObject;
import com.example.smithjarod_parkfinder.R;
import com.google.android.gms.maps.MapFragment;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class StateFragment extends Fragment  implements AdapterView.OnItemSelectedListener, LocationListener {
    public static final String TAG = "TAG.StateFragment";
    public static final String ARRAY = "com.example.smithjarod_parkfinder.fragments.ARRAY";
    public static final String ISPARK = "com.example.smithjarod_parkfinder.fragments.ISPARK";



    ArrayList<ParkObject> parkObjects = new ArrayList<>();
    Spinner stateSpinner;
    Spinner mapOrListSpinner;
    String stateSelected = null;
    boolean listSelected = true;
    String[] states;
    String[] stateCodes;
    String[] mapOrList;
    boolean isPark;
    ArrayList<ParkObject> filteredList = new ArrayList<>();

    public static StateFragment newInstance(ArrayList<ParkObject> objects, boolean isPark) {

        Bundle args = new Bundle();
        args.putSerializable(NationalFragment.ARRAY,objects);
        args.putBoolean(ISPARK, isPark);
        StateFragment fragment = new StateFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_state_fragment,container,false);
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        states = getResources().getStringArray(R.array.statesArray);
        stateCodes =  getResources().getStringArray(R.array.stateCodesArray);
        mapOrList = getResources().getStringArray(R.array.listOrMapsArray);
        stateSelected = states[0];
        listSelected = true;
        parkObjects = (ArrayList<ParkObject>) getArguments().getSerializable(ARRAY);
        isPark = getArguments().getBoolean(ISPARK);

        Log.d(TAG, "onActivityCreated: "+parkObjects.size());
        stateSpinner= getView().findViewById(R.id.stateSpinner);
        ArrayAdapter<String> statesAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1,states);
        stateSpinner.setAdapter(statesAdapter);
        stateSpinner.setOnItemSelectedListener(this);

        mapOrListSpinner= getView().findViewById(R.id.mapOrListSpinner);
        ArrayAdapter<String> mapOrListAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1,mapOrList);
        mapOrListSpinner.setAdapter(mapOrListAdapter);
        mapOrListSpinner.setOnItemSelectedListener(this);

        stateSelected = stateCodes[0];
        filteredList.clear();
        filteredList = (ArrayList<ParkObject>) parkObjects.stream().filter(parkObject -> parkObject.getState().contains(stateSelected)).collect(Collectors.toList());
        //TODO:LOAD FRAGMENT

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent ==stateSpinner){
            stateSelected = stateCodes[position];
            filteredList.clear();
            filteredList = (ArrayList<ParkObject>) parkObjects.stream().filter(parkObject -> parkObject.getState().contains(stateSelected)).collect(Collectors.toList());
        }

        //TODO: LOAD FRAGMENT
        if(parent == mapOrListSpinner){
            if (position ==0){
                listSelected = true;
                Log.d(TAG, "onItemSelected: list selected");
            } else {
                listSelected = false;
                Log.d(TAG, "onItemSelected: map selected");
            }
        }

        if (listSelected){
            Log.d(TAG, "onItemSelected: open list");
            openList();
        } else {
            Log.d(TAG, "onItemSelected: open map");
            openMap();
        }
    }

    void openMap(){
        MapFragment mapFragment = MapFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARRAY,filteredList);
        mapFragment.setArguments(bundle);
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.mapFrame,mapFragment).commit();
    }

    void openList(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.stateFrame, StateListFragment.newInstance(filteredList, isPark)).commit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}
