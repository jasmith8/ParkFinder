package com.example.smithjarod_parkfinder;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FavoritesActivity extends AppCompatActivity {
    public static final String TAG = "TAG.FavoritesActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_listview_fragment);

    }
}
