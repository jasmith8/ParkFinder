package com.example.smithjarod_parkfinder;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smithjarod_parkfinder.objects.AddressObject;
import com.example.smithjarod_parkfinder.objects.DetailParkObject;
import com.synnapps.carouselview.CarouselView;

import java.util.ArrayList;

public class DetailParkActivity extends AppCompatActivity {
    public static final String TAG = "TAG.DetailParkActivity";

    TextView parkName;
    TextView locationDetail;
    TextView descriptionDetail;
    TextView hoursDetail;
    TextView feesDetail;
    TextView activitiesDetail;
    CarouselView carouselView;

    ArrayList<DetailParkObject> parkObjects;

    String parkNameText;
    String parkUrlText;
    String descriptionDetailText;
    String hoursDetailText;
    String feesDetailText;
    String activitiesDetailText;
    String parkId;
    ArrayList<AddressObject> addresses;
    String[] imageArray;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_park);
        Log.d(TAG, "onCreate: ");
        parkObjects = new ArrayList<>();

        parkName = findViewById(R.id.parkName);
        locationDetail = findViewById(R.id.locationDetail);
        descriptionDetail = findViewById(R.id.descriptionDetail);
        hoursDetail = findViewById(R.id.hoursDetail);
        feesDetail = findViewById(R.id.feesDetail);
        activitiesDetail = findViewById(R.id.activitiesDetail);
        carouselView = findViewById(R.id.carouselView);


    }
}
