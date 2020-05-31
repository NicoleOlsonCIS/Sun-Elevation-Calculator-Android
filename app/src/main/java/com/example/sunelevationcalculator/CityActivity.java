package com.example.sunelevationcalculator;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.Calendar;

// selected button color: #4CAF50
// unselected button color: #9C27B0

public class CityActivity extends AppCompatActivity {

    int userYear;
    int userMonth;
    int userDay;
    int userInterval = 5; // default user interval is 5 min
    String unselected = "#9C27B0";
    String selected = "#4CAF50";
    View fiveMinButton;
    View fifteenMinButton;
    View thirtyMinButton;
    View sixtyMinButton;

    Boolean five = true;
    Boolean fifeteen = false;
    Boolean thirty = false;
    Boolean sixty = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        fiveMinButton = findViewById(R.id.button2);
        fifteenMinButton = findViewById(R.id.button5);
        thirtyMinButton = findViewById(R.id.button6);
        sixtyMinButton = findViewById(R.id.button7);

        // default 5 min button to "selected" and the rest unselected
        fiveMinButton.setBackgroundColor(Color.parseColor(selected));
        fifteenMinButton.setBackgroundColor(Color.parseColor(unselected));
        thirtyMinButton.setBackgroundColor(Color.parseColor(unselected));
        sixtyMinButton.setBackgroundColor(Color.parseColor(unselected));

        five = true;
        fifeteen = false;
        thirty = false;
        sixty = false;

        System.out.println ("CITY ACTIVITY SCREEN NOW UP");
        System.out.println ("Storing today's date as selected date");

        Calendar c = Calendar.getInstance();
        int d = c.get(Calendar.DAY_OF_MONTH);
        int m = c.get(Calendar.MONTH);
        int y = c.get(Calendar.YEAR);

        System.out.println ("Today's date is " + d + " " + m + " " + y);

        userYear = y;
        userMonth = m;
        userDay = d;

        // from: https://www.semicolonworld.com/question/49188/android-ondatechangedlistener-how-do-you-set-this
        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                userYear = year;
                userMonth = month;
                userDay = dayOfMonth;
                System.out.println("New date set! year: " + userYear + " month: " + userMonth + " day: " + userDay); // set a global variable when user changes scroll date
            }
        });
    }

    public void onClick5min(View view)
    {
        //System.out.println("5 min button clicked!");
        if (five == false) { changeButtonState(5); } // nothing happens if you click a button that's already in "selected" state
    }

    public void onClick15min(View view)
    {
        //System.out.println("15 min button clicked!");
        if (fifeteen == false) { changeButtonState(15); }
    }

    public void onClick30min(View view)
    {
        //System.out.println("30 min button clicked!");
        if (thirty == false) { changeButtonState(30); }
    }

    public void onClick60min(View view)
    {
        //System.out.println("60 min button clicked!");
        if (sixty == false) { changeButtonState(60); }
    }

    private void changeButtonState(int minutesSelected)
    {
        System.out.println(minutesSelected + " button selected!");

        if (minutesSelected == 5)
        {
            five = true;
            fifeteen = false;
            thirty = false;
            sixty = false;
            fiveMinButton.setBackgroundColor(Color.parseColor(selected));
            fifteenMinButton.setBackgroundColor(Color.parseColor(unselected));
            thirtyMinButton.setBackgroundColor(Color.parseColor(unselected));
            sixtyMinButton.setBackgroundColor(Color.parseColor(unselected));
        }
        else if (minutesSelected == 15)
        {
            five = false;
            fifeteen = true;
            thirty = false;
            sixty = false;
            fiveMinButton.setBackgroundColor(Color.parseColor(unselected));
            fifteenMinButton.setBackgroundColor(Color.parseColor(selected));
            thirtyMinButton.setBackgroundColor(Color.parseColor(unselected));
            sixtyMinButton.setBackgroundColor(Color.parseColor(unselected));
        }
        else if (minutesSelected == 30)
        {
            five = false;
            fifeteen = false;
            thirty = true;
            sixty = false;
            fiveMinButton.setBackgroundColor(Color.parseColor(unselected));
            fifteenMinButton.setBackgroundColor(Color.parseColor(unselected));
            thirtyMinButton.setBackgroundColor(Color.parseColor(selected));
            sixtyMinButton.setBackgroundColor(Color.parseColor(unselected));
        }
        else if (minutesSelected == 60)
        {
            five = false;
            fifeteen = false;
            thirty = false;
            sixty = true;
            fiveMinButton.setBackgroundColor(Color.parseColor(unselected));
            fifteenMinButton.setBackgroundColor(Color.parseColor(unselected));
            thirtyMinButton.setBackgroundColor(Color.parseColor(unselected));
            sixtyMinButton.setBackgroundColor(Color.parseColor(selected));
        }
    }
}

