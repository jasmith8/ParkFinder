package com.example.smithjarod_parkfinder.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.smithjarod_parkfinder.objects.ParkObject;
import com.example.smithjarod_parkfinder.R;

import java.util.ArrayList;
import java.util.Arrays;

public class StateListFragment extends ListFragment implements AdapterView.OnItemClickListener{
    public static final String TAG = "TAG.StateFragment";
    public static final String ARRAY = "com.example.smithjarod_parkfinder.fragments.ARRAY";

    ArrayList<ParkObject> parkObjects = new ArrayList<>();

    public StateListFragment(){

    }

    public static StateListFragment newInstance(ArrayList<ParkObject> objects) {

        Bundle args = new Bundle();
        args.putSerializable(NationalFragment.ARRAY,objects);
        StateListFragment fragment = new StateListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.search_listview_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //TODO: LOAD THE ARRAY
        parkObjects = new ArrayList<>();
        parkObjects = (ArrayList<ParkObject>)getArguments().getSerializable(ARRAY) ;
        Log.d(TAG, "onActivityCreated: "+parkObjects.size());

        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1,parkObjects){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                TextView textView1 = view.findViewById(android.R.id.text1);
                String[] getStateCodesArray = getResources().getStringArray(R.array.stateCodesArray);
                String[] getStatesArray = getResources().getStringArray(R.array.statesArray);
                String getStateCode = parkObjects.get(position).getName();
                int index = Arrays.asList(getStateCodesArray).indexOf(getStateCode);
                //String getFullStateName = getStatesArray[index];
                textView1.setText(parkObjects.get(position).getName());
                return view;
            }
        };
        setListAdapter(adapter);
        getListView();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
