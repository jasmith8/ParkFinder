package com.example.smithjarod_parkfinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smithjarod_parkfinder.fragments.NationalFragment;
import com.example.smithjarod_parkfinder.fragments.StateFragment;
import com.example.smithjarod_parkfinder.objects.ParkObject;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    public static final String TAG = "TAG.SearchActivity";
    public static final String NATIONAL_STATE="com.example.smithjarod_parkfinder.NATIONAL_STATE";
    public static final String NATIONAL_STATE_KEY="com.example.smithjarod_parkfinder.NATIONAL_STATE_KEY";
    String typeOfList;

    TabLayout tabLayout;
    TabLayout.Tab tabLayoutTab;
    Context context = this;
    int selectedTab;
    int defaultTab = 0;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ArrayList<ParkObject> parkObjects = new ArrayList<>();
    Parks_Helper parks_helper = new Parks_Helper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);
        tabLayout = this.findViewById(R.id.tabLayout);
        typeOfList = getIntent().getStringExtra(MainActivity.EXTRA_INFO);
        Log.d(TAG, "onCreate: "+typeOfList);
        if(typeOfList.contains(MainActivity.PARKS)){
            Log.d(TAG, "onCreate: get parks");
            parkObjects =  parkObjects = parks_helper.parkObjects(true, this,"ALL");
        } else {
            Log.d(TAG, "onCreate: get camps");
            parkObjects =  parkObjects = parks_helper.parkObjects(false, this,"ALL");
        }
        getSupportFragmentManager().executePendingTransactions();

        sharedPreferences = context.getSharedPreferences(NATIONAL_STATE,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        selectedTab = sharedPreferences.getInt(NATIONAL_STATE_KEY,defaultTab);

        chooseNationalState(selectedTab,editor);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedTab = tab.getPosition();
                chooseNationalState(selectedTab,editor);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
    
    void chooseNationalState(int selectedTab, SharedPreferences.Editor editor){
        if (selectedTab ==0){
            editor.putInt(NATIONAL_STATE_KEY,selectedTab);
            openNationalView();

        }else {
            editor.putInt(NATIONAL_STATE_KEY,selectedTab);
            openStateView();;
        }
    }

    void openNationalView(){
        Log.d(TAG, "openNationalView: ");
        getSupportFragmentManager().beginTransaction().
                replace(R.id.mainFrame, NationalFragment.newInstance(parkObjects)).commit();
    }

    void openStateView(){
        Log.d(TAG, "openStateView: ");
        getSupportFragmentManager().beginTransaction().
                replace(R.id.mainFrame, StateFragment.newInstance(parkObjects)).commit();
    }


}
