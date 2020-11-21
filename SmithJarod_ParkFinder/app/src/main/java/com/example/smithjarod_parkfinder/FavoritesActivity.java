package com.example.smithjarod_parkfinder;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.smithjarod_parkfinder.objects.FavoriteObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class FavoritesActivity extends AppCompatActivity {
    public static final String TAG = "TAG.FavoritesActivity";
    public static final String NATIONAL_STATE="com.example.smithjarod_parkfinder.NATIONAL_STATE";
    public static final String NATIONAL_STATE_KEY="com.example.smithjarod_parkfinder.NATIONAL_STATE_KEY";
    String _name;
    boolean _isPark;
    String _idCode;
    ArrayList<FavoriteObject> favoriteObjects = new ArrayList<>();
    private DatabaseReference mDatabase;
    FirebaseUser user;
    String userID;
    Context context = this;
    ListView lv;
    ConstraintLayout cs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_listview_fragment);
        lv = this.findViewById(android.R.id.list);
        cs = this.findViewById(android.R.id.empty);
        TextView message = this.findViewById(R.id.textView);
        message.setText(R.string.loading);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {

            ArrayAdapter adapter;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()){
                    boolean _pIsPark = Boolean.parseBoolean(snap.child("isPark").getValue().toString());
                    String _pName = snap.child("pName").getValue().toString();
                    String _pID = snap.child("pID").getValue().toString();
                    FavoriteObject object = new FavoriteObject(_pName,_pIsPark,_pID);
                    favoriteObjects.add(object);
                }
                Log.d(TAG, "onCreate: "+favoriteObjects.size());
                if (favoriteObjects.size()>1){
                    cs.setVisibility(View.GONE);
                } else {
                    message.setText(R.string.empty_list_try_another);
                }


                adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, android.R.id.text1,favoriteObjects){
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                        View view = super.getView(position, convertView, parent);
                        TextView textView1 = view.findViewById(android.R.id.text1);
                        textView1.setText(favoriteObjects.get(position).name());
                        Log.d(TAG, "getView: "+favoriteObjects.get(position).name());
                        return view;
                    }
                };
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String parkId = favoriteObjects.get(position).idCode();
                        boolean is_park = favoriteObjects.get(position).isPark();
                        Intent intent= new Intent(context, DetailParkActivity.class);
                        if (is_park){
                            intent.putExtra(DetailParkActivity.EXTRA_INFO, DetailParkActivity.PARKS);
                        } else {
                            intent.putExtra(DetailParkActivity.EXTRA_INFO, DetailParkActivity.CAMPS);
                        }
                        intent.putExtra(DetailParkActivity.EXTRA_INFO_2,parkId);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: "+error);
            }
        });


    }


}
