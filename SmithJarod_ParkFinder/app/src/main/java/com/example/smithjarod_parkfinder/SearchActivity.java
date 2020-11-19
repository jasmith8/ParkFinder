package com.example.smithjarod_parkfinder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
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
    //TabLayout.Tab tabLayoutTab;
    Context context = this;
    int selectedTab;
    int defaultTab = 0;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ArrayList<ParkObject> parkObjects = new ArrayList<>();
    ArrayList<ParkObject> campObjects = new ArrayList<>();
    Parks_Helper parks_helper = new Parks_Helper();
    boolean isPark = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);
        tabLayout = this.findViewById(R.id.tabLayout);
        typeOfList = getIntent().getStringExtra(MainActivity.EXTRA_INFO);
        Log.d(TAG, "onCreate: "+typeOfList);
        if(typeOfList.contains(MainActivity.PARKS)){
            Log.d(TAG, "onCreate: get parks");
            isPark = true;
            parkObjects = parks_helper.parkObjects(isPark, this,"ALL");
        } else {
            Log.d(TAG, "onCreate: get camps");
            isPark = false;
            campObjects = parks_helper.parkObjects(isPark, this,"ALL");
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
        if (isPark) {
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.mainFrame, NationalFragment.newInstance(parkObjects, isPark)).commit();
        } else {
            Log.d(TAG, "openNationalView: "+campObjects.size());
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.mainFrame, NationalFragment.newInstance(campObjects, isPark)).commit();
        }
    }

    void openStateView(){
        Log.d(TAG, "openStateView: ");
        if (isPark) {
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.mainFrame, StateFragment.newInstance(parkObjects, isPark)).commit();
        } else {
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.mainFrame, StateFragment.newInstance(campObjects,isPark)).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //TODO SEND TO FAV LIST
        Intent intent= new Intent(this, FavoritesActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}
