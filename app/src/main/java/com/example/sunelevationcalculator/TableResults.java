package com.example.sunelevationcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class TableResults extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_results);

        Intent mIntent = getIntent();
        int intValue = mIntent.getIntExtra("size", 0);
        System.out.println("The size value passed is " + intValue);
        String[] timesReceived = mIntent.getStringArrayExtra("Times");
        String[] elevationsReceived = mIntent.getStringArrayExtra("Elevations");

        if ((timesReceived != null) && (timesReceived != null))
        {
            System.out.println("Reieved data into Table Results . java'");
            for(int i = 0; i < timesReceived.length; i++)
            {
                System.out.println("Time " + timesReceived[i] + " Elevation " + elevationsReceived[i]);
            }
        }

    }
}