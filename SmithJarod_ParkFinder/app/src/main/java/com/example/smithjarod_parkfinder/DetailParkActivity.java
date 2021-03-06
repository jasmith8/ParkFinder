package com.example.smithjarod_parkfinder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smithjarod_parkfinder.objects.AddressObject;
import com.example.smithjarod_parkfinder.objects.DetailParkObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

public class DetailParkActivity extends AppCompatActivity {
    public static final String TAG = "TAG.DetailParkActivity";
    public static final String EXTRA_INFO = "com.example.smithjarod_parkfinder.EXTRA_INFO";
    public static final String EXTRA_INFO_2 = "com.example.smithjarod_parkfinder.EXTRA_INFO_2";
    public static final String PARKS = "parks";
    public static final String CAMPS = "camps";

    final Parks_Helper parks_helper = new Parks_Helper();

    String typeOfDetail=null;
    String parkId;
    String address;
    TextView parkName;
    TextView locationDetail;
    TextView descriptionDetail;
    TextView hoursDetail;
    TextView feesDetail;
    TextView activitiesDetail;
    TextView activitiesTitle;
    CarouselView carouselView;

    ArrayList<DetailParkObject> parkObjects;

    ArrayList<String> imageArray = new ArrayList<>();

    private DatabaseReference mDatabase;
    boolean isPark;
    FirebaseUser user;
    String userID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_park);
        Log.d(TAG, "onCreate: ");
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        typeOfDetail =  getIntent().getStringExtra( DetailParkActivity.EXTRA_INFO);
        parkId = getIntent().getStringExtra(DetailParkActivity.EXTRA_INFO_2);
        Log.d(TAG, "onCreate: type of detail: "+typeOfDetail+" \nParkID: "+parkId);
        parkObjects = new ArrayList<>();

        parkName = findViewById(R.id.theParkName);
        locationDetail = findViewById(R.id.locationDetail);
        descriptionDetail = findViewById(R.id.descriptionDetail);
        hoursDetail = findViewById(R.id.hoursDetail);
        feesDetail = findViewById(R.id.feesDetail);
        activitiesDetail = findViewById(R.id.activitiesDetail);
        activitiesTitle = findViewById(R.id.activitiesTitle);
        carouselView = findViewById(R.id.carouselView);

        ImageListener imageListener = (position, imageView) -> Picasso.get().load(imageArray.get(position)).resize(400,400).onlyScaleDown().into(imageView);
        Log.d(TAG, "onCreate: "+typeOfDetail);
        if(typeOfDetail.contains(DetailParkActivity.PARKS)){
            Log.d(TAG, "onCreate: get parks");
            parkObjects = parks_helper.detailParkObjects(true, this,parkId);
            while (parkObjects.size()<1){
                Log.d(TAG, "onCreate: LOADING");
            }
            isPark = true;

            parkName.setText(parkObjects.get(0).getParkNameText());
            ArrayList<AddressObject> addressObjects = parkObjects.get(0).getAddresses();
            for (AddressObject obj: addressObjects){
                String type = obj.getType();
                if (type.equals("Physical")){
                    locationDetail.setText(obj.getAddress());

                }
            }
            Log.d(TAG, "onCreate: address: "+locationDetail.getText().toString());
            address = locationDetail.getText().toString();

            descriptionDetail.setText(parkObjects.get(0).getDescriptionDetailText());
            hoursDetail.setText(parkObjects.get(0).getHoursDetailText());
            feesDetail.setText(parkObjects.get(0).getFeesDetailText());


        } else {
            Log.d(TAG, "onCreate: get camps");
            isPark = false;
            parkObjects = parks_helper.detailParkObjects(false, this,parkId);
            while (parkObjects.size()<1){
                Log.d(TAG, "onCreate: LOADING");
            }
            parkName.setText(parkObjects.get(0).getParkNameText());
            ArrayList<AddressObject> addressObjects = parkObjects.get(0).getAddresses();
            for (AddressObject obj: addressObjects){
                String type = obj.getType();
                if (type.equals("Physical")){
                    locationDetail.setText(obj.getAddress());
                }
            }
            
            Log.d(TAG, "onCreate: address: "+locationDetail.getText().toString());
            address = locationDetail.getText().toString();
            descriptionDetail.setText(parkObjects.get(0).getDescriptionDetailText());
            hoursDetail.setText(parkObjects.get(0).getHoursDetailText());
            feesDetail.setText(parkObjects.get(0).getFeesDetailText());
            activitiesTitle.setText("Reservation Info:");


        }
        activitiesDetail.setText(parkObjects.get(0).getActivitiesDetailText());
        imageArray = parkObjects.get(0).getImageArray();
        carouselView.setPageCount(imageArray.size());
        carouselView.setImageListener(imageListener);
        Uri mapUri = Uri.parse("google.navigation:q=a+"+Uri.encode(address));
        Intent launchMap = new Intent(Intent.ACTION_VIEW, mapUri);
        launchMap.setPackage("com.google.android.apps.maps");
        locationDetail.setOnClickListener(v -> startActivity(launchMap));

        //DATABASE
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }
    Menu menu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu,menu);
        this.menu = menu;
        MenuItem addItem = menu.findItem(R.id.addDB);
        MenuItem remItem = menu.findItem(R.id.removeDB);
        String pID = parkObjects.get(0).getParkId();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean exists = snapshot.child("users").child(userID).child(pID).exists();
                if (exists){
                    addItem.setVisible(false);
                    remItem.setVisible(true);
                } else {
                    addItem.setVisible(true);
                    remItem.setVisible(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Log.d(TAG, "onOptionsItemSelected: "+userID);
        String pName = parkName.getText().toString();
        String pID = parkObjects.get(0).getParkId();
        MenuItem addItem = menu.findItem(R.id.addDB);
        MenuItem remItem = menu.findItem(R.id.removeDB);

        switch (item.getItemId()){
            case R.id.addDB:
                mDatabase.child("users").child(userID).child(pID).child("isPark").setValue(isPark);
                mDatabase.child("users").child(userID).child(pID).child("pID").setValue(pID);
                mDatabase.child("users").child(userID).child(pID).child("pName").setValue(pName);
                addItem.setVisible(false);
                remItem.setVisible(true);

                Toast.makeText(getApplicationContext(), R.string._added,Toast.LENGTH_SHORT).show();
                return true;
            case R.id.removeDB:
                mDatabase.child("users").child(userID).child(pID).removeValue();
                addItem.setVisible(true);
                remItem.setVisible(false);
                Toast.makeText(getApplicationContext(), R.string._removed,Toast.LENGTH_SHORT).show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
