
//
// CURRENT BUGS:
// Need to re-code Thread Runnable so that it can stop, getting warning about it not "closing"
//
//

package com.example.sunelevationcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;




public class MainActivity extends AppCompatActivity {

    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    double latitude = -1000;
    double longitude = -1000;
    String userCity = "";
    String userState = "";
    String userCountry = "";
    Boolean userSaved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int count = 0;

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Granted. Start getting the location information
                getLastLocation();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                    System.out.println("Got user location, latitude " + latitude + " longitude " + longitude);
                                    parseData();
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    private void parseData()
    {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            //String postalCode = addresses.get(0).getPostalCode();
            //String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
            //System.out.println(address);
            userCity = city;
            userState = state;
            userCountry = country;


        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        System.out.println("ADDRESSES " + addresses);
        System.out.println();

        if (userSaved == false) { saveUserLocation(); } // trying to prevent bug of this running multiple times
    }

    private void saveUserLocation()
    {

        // Save it to Room database for the user
        final AppDatabase appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();


        new Thread(new Runnable() // always do db stuff on another thread
        { @Override public void run()
        {
            User user = new User();
            user.setUserCity(userCity);
            user.setUserState(userState);
            user.setUserCountry(userCountry);
            user.setUserLatitude(latitude);
            user.setUserLongitude(longitude);
            appDatabase.userDao().insertUsers (user);
            userSaved = true;
        }
        }).start();
        //Handler handler= new Handler();
        //handler.removeCallbacks(runnable); // NEED TO FIGURE OUT HOW TO STOP THREADS : W/System: A resource failed to call close.

        printUser();
        appDatabase.close();
        return;
    }

    private void printUser()
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
                Double uLatitude = user.getUserLatitude();
                Double uLongitude = user.getUserLongitude();

                System.out.println("User data retrieved from Database: ");
                System.out.println("City: " + ucity);
                System.out.println("State: " + uState);
                System.out.println("Country: " + uCountry);
                System.out.println("Latitude: " + uLatitude);
                System.out.println("Longitude: " + uLongitude);

            }

            appDatabase.close();


        }
        }).start();
        //Handler handler= new Handler();
        //handler.removeCallbacks(runnable);
    }


    //private void storeAddress(String city, String state, String country)
    //{
    //    System.out.println("Storing address!");
    //}


    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            //latTextView.setText(mLastLocation.getLatitude()+"");
            //lonTextView.setText(mLastLocation.getLongitude()+"");
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            System.out.println("Got user location, latitude " + latitude + " longitude " + longitude);
        }
    };

    @Override
    public void onResume(){
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
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

