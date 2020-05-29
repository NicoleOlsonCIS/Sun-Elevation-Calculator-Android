package com.example.sunelevationcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickName(View view)
    {
        // go to Name input Activity
        Intent myIntent = new Intent(getBaseContext(), CityActivity.class);
        startActivity(myIntent);
    }

    public void onClickCoordinates(View view)
    {
        // go to Coordinates input Actvity
    }

    public void onClickAlerts(View view)
    {
        // go to Alerts lnading page Activity
    }
}