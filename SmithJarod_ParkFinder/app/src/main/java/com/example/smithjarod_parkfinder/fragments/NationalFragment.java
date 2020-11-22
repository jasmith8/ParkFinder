package com.example.smithjarod_parkfinder.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.smithjarod_parkfinder.DetailParkActivity;
import com.example.smithjarod_parkfinder.objects.ParkObject;
import com.example.smithjarod_parkfinder.R;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings({"unchecked", "rawtypes"})
public class NationalFragment extends ListFragment implements AdapterView.OnItemClickListener {
    public static final String TAG = "TAG.NationalFragment";
    public static final String ARRAY = "com.example.smithjarod_parkfinder.fragments.ARRAY";
    public static final String ISPARK = "com.example.smithjarod_parkfinder.fragments.ISPARK";
    ArrayList<ParkObject> parkObjects;
    boolean isPark = true;
    public NationalFragment(){
        
    }

    public static NationalFragment newInstance(ArrayList<ParkObject> objects, boolean isPark) {
        
        Bundle args = new Bundle();
        args.putSerializable(ARRAY, objects);
        args.putBoolean(ISPARK, isPark);
        NationalFragment fragment = new NationalFragment();
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
        parkObjects = new ArrayList<>();
        parkObjects = (ArrayList<ParkObject>)getArguments().getSerializable(ARRAY) ;
        isPark = getArguments().getBoolean(ISPARK);
        Log.d(TAG, "onActivityCreated: number of objects: "+parkObjects.size());
        while(parkObjects.size() <1){
            Log.d(TAG, "onActivityCreated: loading");
        }
        loadListView();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemClick: ");
    }


    @SuppressWarnings("rawtypes")
    private void loadListView(){
        Log.d(TAG, "loadListView: loading list view");
        //noinspection rawtypes,rawtypes
        ArrayAdapter adapter= new ArrayAdapter(getContext(), android.R.layout.simple_list_item_2, android.R.id.text1,parkObjects){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                TextView textView1 = view.findViewById(android.R.id.text1);
                TextView textView2 = view.findViewById(android.R.id.text2);
                String[] getStateCodesArray = getResources().getStringArray(R.array.stateCodesArray);
                String[] getStatesArray = getResources().getStringArray(R.array.statesArray);
                String getStateCode = parkObjects.get(position).getState();
                int index = Arrays.asList(getStateCodesArray).indexOf(getStateCode);
                String getFullStateName = getStatesArray[index];
                textView1.setText(getFullStateName);
                textView2.setText(parkObjects.get(position).getName());
                return view;
            }
        };
        Log.d(TAG, "loadListView: done loading listview");
        setListAdapter(adapter);
        ListView lv = getListView();
        lv.setOnItemClickListener((parent, view, position, id) -> {
            Log.d(TAG, "onItemClick: ");
            String parkId = parkObjects.get(position).getParkId();
            Intent intent= new Intent(getContext(), DetailParkActivity.class);
            if (isPark){
                intent.putExtra(DetailParkActivity.EXTRA_INFO, DetailParkActivity.PARKS);
            } else {
                intent.putExtra(DetailParkActivity.EXTRA_INFO, DetailParkActivity.CAMPS);
            }
            intent.putExtra(DetailParkActivity.EXTRA_INFO_2,parkId);
            startActivity(intent);
        });

    }

}
