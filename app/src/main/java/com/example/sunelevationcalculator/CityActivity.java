package com.example.sunelevationcalculator;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.DatePicker;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    String storedCity = "";
    String storedState = "";
    String storedCountry = "";

    String[] resultTimes;
    String[] resultElevations;

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

        getUserLocationFromDatabase();

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

    private void getUserLocationFromDatabase()
    {
        // Save it to Room database for the user
        final AppDatabase appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();

        new Thread(new Runnable() // always do db stuff on another thread
        { @Override public void run()
        {
            User users [];
            users = appDatabase.userDao().loadAllUsers();
            System.out.println("PRINTING USERS");
            System.out.println(users);

            if (users.length != 0)
            {
                User user = users[0];

                String ucity = user.getUserCity();
                String uState = user.getUserState();
                String uCountry = user.getUserCountry();

                storedCity = ucity;
                storedState = uState;
                storedCountry = uCountry;
                setUserDefaults();
            }

            appDatabase.close();
        }
        }).start();
    }

    private void setUserDefaults()
    {
        EditText cityInput = findViewById(R.id.editTextTextPersonName2);
        EditText stateInput = findViewById(R.id.editTextTextPersonName3);
        EditText countryInput = findViewById(R.id.editTextTextPersonName4);

        cityInput.setText(storedCity);
        stateInput.setText(storedState);
        countryInput.setText(storedCountry);
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

    public String getInterval()
    {
        // check the state of the buttons on submit and return a string of the interval in minutes
        if(fifeteen) {return "15";}
        if(thirty){return "30";}
        if(sixty){return "60";}
        else {return "5";} // 5 is the default if something fails

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

    public void onClickSubmit(View view)
    {
        System.out.println("CHECKING DATA");
        // Check if there is data in all three of the fields
        final EditText cityInput = findViewById(R.id.editTextTextPersonName2);
        final EditText stateInput = findViewById(R.id.editTextTextPersonName3);
        final EditText countryInput = findViewById(R.id.editTextTextPersonName4);
        String city = cityInput.getText().toString();
        String state = stateInput.getText().toString();
        String country = countryInput.getText().toString();

        // if any of these do not have input
        // print what each is
        System.out.println("City input " + cityInput.getText());
        System.out.println("State input " + stateInput.getText());
        System.out.println("Country input " + countryInput.getText());

        if ((city.length() == 0) || (state.length() == 0) || (country.length() == 0))
        {
            final Handler handler = new Handler();

            System.out.println("FIELD MISSING FROM INPUT DATA!");
            TextView errorLabel = findViewById(R.id.textView8);
            errorLabel.setText("Missing Fields");

            final int red_color = Integer.parseInt("e60505", 16)+0xFF000000;
            final int black_color = Integer.parseInt("000000", 16)+0xFF000000;

            final Runnable input_finish_checker = new Runnable() {
                public void run() {

                }
            };  // DON'T THINK I NEED THIS ANYMORE

            if (city.length() == 0)
            {
                cityInput.setText("Required");
                cityInput.setTextColor(red_color);

                cityInput.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(final CharSequence s, int start, int before,
                                              int count) {

                        System.out.println("CITY TEXT CHANGED!!");
                        cityInput.removeTextChangedListener(this);
                        // delete the "Required" and change font to black
                            handler.removeCallbacks(input_finish_checker);
                            cityInput.setText("");
                            cityInput.setTextColor(black_color);

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }

            if (state.length() == 0)
            {
                stateInput.setText("Required");
                stateInput.setTextColor(red_color);
                stateInput.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(final CharSequence s, int start, int before,
                                              int count) {

                        System.out.println("STATE TEXT CHANGED!!");
                        stateInput.removeTextChangedListener(this);
                        // delete the "Required" and change font to black
                        handler.removeCallbacks(input_finish_checker);
                        stateInput.setText("");
                        stateInput.setTextColor(black_color);

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
            if (country.length() == 0)
            {
                countryInput.setText("Required");
                countryInput.setTextColor(red_color);
                countryInput.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(final CharSequence s, int start, int before,
                                              int count) {

                        System.out.println("COUNTRY TEXT CHANGED!!");
                        countryInput.removeTextChangedListener(this);
                        // delete the "Required" and change font to black
                        handler.removeCallbacks(input_finish_checker);
                        countryInput.setText("");
                        countryInput.setTextColor(black_color);

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }

            return;
        }

        else if ((city != "Required") && (state != "Required") && (country != "Required"))
        {
            // there are no blank fields and 'Required' text has been changed
            System.out.println("ALL FIELDS HAVE INPUT DATA");
            // GET RID OF MISSING FIELDS LABEL TEXT
            TextView errorLabel = findViewById(R.id.textView8);
            errorLabel.setText("");

            // call the backend
            Boolean success = callBackend(city, state, country);

            if (success) {
                System.out.println("Successful call to callBackend");

                }
            else
            {
                // Transition to table with error flag and message set
                //Intent i = new Intent(this, TableResults.class);
                //startActivity(i);
            }
        }
    }

    private Boolean callBackend(String city, String state, String country)
    {
        String url = createUrl(city, state, country);

        RequestQueue queue;

        queue = Volley.newRequestQueue(this);

        // cancelling all requests about this search if in queue
        queue.cancelAll(url); // DON"T KNOW IF I DID THIS RIGHT

        // first StringRequest: getting items searched
        StringRequest stringRequest = searchNameStringRequest(url);
        stringRequest.setTag(url); // DON"T KNOW IF I DID THIS RIGHT

        // executing the request (adding to queue)
        queue.add(stringRequest);

        // THIS HAS TO WAIT UNTIL TABLE DATA IS AVAILABLE
        //Intent i = new Intent(this, TableResults.class);
        //i.putExtra("Times", "Hello"
        //);
        //startActivity(i);

        return true;
    }

    // BASED ON EXAMPLE HERE: https://wtmimura.com/post/calling-api-on-android-studio/
    private StringRequest searchNameStringRequest(String url) {

        // 1st param => type of method (GET/PUT/POST/PATCH/etc)
        // 2nd param => complete url of the API
        // 3rd param => Response.Listener -> Success procedure
        // 4th param => Response.ErrorListener -> Error procedure
        return new StringRequest(Request.Method.GET, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                // try/catch block for returned JSON data
                // see API's documentation for returned format
                try
                {
                    // PARSE THE RESULTS
                    System.out.println(response);
                    JSONObject json = new JSONObject(response);
                    String result = json.toString();
                    System.out.println(result);

                    //remove the braces on the front and back of string
                    result = removeFirstAndLastCharacters(result); // DOESN'T SEEM TO WORK

                    String[] parts = result.split(",");
                    System.out.println(parts[0]);
                    System.out.println(parts[parts.length - 1]);

                    int size = parts.length;
                    String[] times = new String[size];
                    for(int i=0; i<size; i++) {
                        times[i] = "None";
                    }
                    String[] elevations = new String[size];
                    for(int i=0; i<size; i++) {
                        elevations[i] = "None";
                    }

                    int j = 0;
                    // for each part
                    for(int i = 0; i < size; i++) {
                        String p = parts[j];
                        String[] b = p.split("\"");
                        if (b.length > 1) {
                            String time = b[1];
                            String elevation = b[2];
                            elevation = removeFirstAndLastCharacters(elevation);
                            times[j] = time;
                            elevations[j] = elevation;
                        }
                        j = j + 1;
                        if (j + 1 == parts.length) {
                            break;
                        }
                    }

                    // print times
                    System.out.println("Printing times and elevation pairs");
                    for(int k = 0; k < times.length; k++) { System.out.println(times[k] + " " + elevations[k]); }

                    // logistics figured out here: https://stackoverflow.com/questions/21754014/start-activity-from-a-background-thread
                    Intent newIntent = new Intent(getBaseContext(), TableResults.class);
                    newIntent.putExtra("size", size
                    );
                    newIntent.putExtra("Times", times
                    );
                    newIntent.putExtra("Elevations", elevations
                    );
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplication().startActivity(newIntent);
                    //startActivity(newIntent);

                }
                catch (JSONException e)
                {
                    // ERROR
                    System.out.println("Type 1 error on calling backend");
                }
            } // public void onResponse(String response)
        }, // Response.Listener<String>()
                new Response.ErrorListener()
                {
                    // 4th param - method onErrorResponse lays the code procedure of error return
                    // ERROR
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        // display a simple message on the screen
                        System.out.println("Type 2 Error on calling backend");

                    }
                });
    }

    public String removeFirstAndLastCharacters(String s)
    {
        s = removeFirstChar(s);
        s = removeLastChar(s);
        return s;
    }

    public String removeFirstChar(String str)
    {
        if (str.length() > 0) {return str.substring(1);}
        return str;
    }

    public String removeLastChar(String str)
    {
        if (str != null && str.length() > 0 ) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    private String createUrl(String city, String state, String country)
    {
        System.out.println();

        String root2 = "https://sun-elevation-compute.wn.r.appspot.com/sun_address?";
        String _year = "year=";
        String _month = "month=";
        String _day = "day=";
        String _city = "city=";
        String _state = "state=";
        String _country = "country=";
        String and = "&";
        String _interval = "interval=";
        String userInterval = getInterval();


        city = city.trim();
        city = city.replaceAll("\\s+", "+");
        city.trim();
        city = city.replaceAll("\\s+", "+");
        System.out.println("Edited city input " + city);

        state = state.trim();
        state = state.replaceAll("\\s+", "+");
        System.out.println("Edited state input " + state);

        country = country.trim();
        country = country.replaceAll("\\s+", "+");
        System.out.println("Edited country input " + country);

        String fullUrl = root2 + _year + userYear + and + _month + userMonth + and + _day + userDay + and + _city + city + and + _country + country + and + _state + state + and + _interval + userInterval;

        return fullUrl;
    }

    private String[] parseReturn()
    {
        // Maybe don't need to return a String, instead just set a global or pass it to a segue
        return new String[]{"something"};
    }
}

