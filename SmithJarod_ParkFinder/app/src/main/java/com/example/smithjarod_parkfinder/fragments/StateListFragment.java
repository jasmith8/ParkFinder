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

@SuppressWarnings("unchecked")
public class StateListFragment extends ListFragment implements AdapterView.OnItemClickListener{
    public static final String TAG = "TAG.StateFragment";
    public static final String ARRAY = "com.example.smithjarod_parkfinder.fragments.ARRAY";
    public static final String ISPARK = "com.example.smithjarod_parkfinder.fragments.ISPARK";
    ArrayList<ParkObject> parkObjects = new ArrayList<>();
    boolean isPark ;
    public StateListFragment(){

    }

    public static StateListFragment newInstance(ArrayList<ParkObject> objects, boolean isPark) {

        Bundle args = new Bundle();
        Log.d(TAG, "newInstance: "+objects.size());
        args.putSerializable(NationalFragment.ARRAY,objects);
        args.putBoolean(ISPARK, isPark);
        StateListFragment fragment = new StateListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        container.removeAllViews();
        return inflater.inflate(R.layout.search_listview_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        parkObjects = new ArrayList<>();
        parkObjects = (ArrayList<ParkObject>)getArguments().getSerializable(ARRAY) ;
        isPark = getArguments().getBoolean(ISPARK);
        Log.d(TAG, "onActivityCreated: "+parkObjects.size());

        //noinspection rawtypes,rawtypes
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1,parkObjects){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                TextView textView1 = view.findViewById(android.R.id.text1);
                textView1.setText(parkObjects.get(position).getName());
                return view;
            }
        };
        setListAdapter(adapter);
        ListView lv = getListView();
        lv.setOnItemClickListener((parent, view, position, id) -> {
            Log.d(TAG, "onItemClick: ");
            String parkId = parkObjects.get(position).getParkId();
            Intent intent= new Intent(getContext(), DetailParkActivity.class);
            Log.d(TAG, "onItemClick: ispark "+isPark);
            if (isPark){
                intent.putExtra(DetailParkActivity.EXTRA_INFO, DetailParkActivity.PARKS);
            } else {
                intent.putExtra(DetailParkActivity.EXTRA_INFO, DetailParkActivity.CAMPS);
            }
            intent.putExtra(DetailParkActivity.EXTRA_INFO_2,parkId);
            startActivity(intent);
        });
        lv.setEmptyView(getView().findViewById(android.R.id.empty));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
