package com.example.smithjarod_parkfinder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = "TAG.MainActivity";
    public static final String EXTRA_INFO = "com.example.smithjarod_parkfinder.EXTRA_INFO";
    public static final int REQUEST_LOCATION_PERMISSIONS = 0x01001;
    private static final int RC_SIGN_IN = 123;
    public static final String PARKS = "parks";
    public static final String CAMPS = "camps";
    Button parkButton;
    Button campButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!hasPermissions()) {
            requestPermissions();
        }
        parkButton = (Button) this.findViewById(R.id.parks);
        campButton = (Button) this.findViewById(R.id.camps);
        parkButton.setOnClickListener(this);
        campButton.setOnClickListener(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            firebaseLogin();

        }
        Toast.makeText(this,"Logged In",Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onClick(View v) {

        Intent intent= new Intent(this, SearchActivity.class);

        boolean isConnected = false;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
            cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED){
            Log.d(TAG, "onClick: CONNECTED");
            Toast.makeText(this, R.string.loading_data,Toast.LENGTH_LONG).show();

            switch (v.getId()){
                case R.id.parks:
                    intent.putExtra(EXTRA_INFO, PARKS);
                    startActivity(intent);
                    break;
                case R.id.camps:
                    intent.putExtra(EXTRA_INFO, CAMPS);
                    startActivity(intent);
            }
        } else {
            Log.d(TAG, "onClick: NOT CONNECTED");
            Toast.makeText(this, R.string.no_internet,Toast.LENGTH_SHORT).show();
        }


    }

    private boolean hasPermissions() {
        boolean hasLoc = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
        boolean hasCouLoc = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
        boolean hasInternet = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                == PackageManager.PERMISSION_GRANTED;
        boolean hasNetwork = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
                == PackageManager.PERMISSION_GRANTED;
        boolean hasStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
        boolean hasState = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED;

        boolean allTrue;
        allTrue = hasLoc && hasCouLoc && hasInternet && hasNetwork && hasStorage && hasState;

        return allTrue;
    }
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE},
                REQUEST_LOCATION_PERMISSIONS);
    }

    private void firebaseLogin(){
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.AnonymousBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            Log.d(TAG, "onActivityResult: "+resultCode);
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(this,"LOGGED IN: "+user.getDisplayName(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onActivityResult: "+user.getDisplayName());
            } else {
                Log.d(TAG, "onActivityResult: ERROR: "+response.getError());
                Log.d(TAG, "onActivityResult: code: "+response.getError().getErrorCode());
            }
        }
    }
}