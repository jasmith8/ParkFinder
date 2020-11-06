package com.example.smithjarod_parkfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    public static final String EXTRA_INFO = "com.example.smithjarod_parkfinder.EXTRA_INFO";
    public static final String PARKS = "parks";
    public static final String CAMPS = "camps";
    Button parkButton;
    Button campButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parkButton = (Button) this.findViewById(R.id.parks);
        campButton = (Button) this.findViewById(R.id.camps);
        parkButton.setOnClickListener(this::onClick);
        campButton.setOnClickListener(this::onClick);
    }


    @Override
    public void onClick(View v) {

        Intent intent= new Intent(this, SearchActivity.class);
        switch (v.getId()){
            case R.id.parks:
                intent.putExtra(EXTRA_INFO, PARKS);
                startActivity(intent);
                break;
            case R.id.camps:
                intent.putExtra(EXTRA_INFO, CAMPS);
                startActivity(intent);
        }


    }
}