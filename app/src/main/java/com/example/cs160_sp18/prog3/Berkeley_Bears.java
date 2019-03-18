package com.example.cs160_sp18.prog3;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
//import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;


public class Berkeley_Bears extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = "Berkeley Bears";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    String username;
    Button updatelocation;

    private int[] images = {R.drawable.mlk_bear, R.drawable.outside_stadium, R.drawable.macchi_bears,
            R.drawable.les_bears, R.drawable.strawberry_creek, R.drawable.south_hall,
            R.drawable.bell_bears, R.drawable.bench_bears, R.drawable.in_stadium};
    private String[] titles = {"Class of 1927 Bear", "Stadium Entrance Bear", "Macchi Bear",
            "Les Bear", "Strawberry Creek Bear", "South Hall Bear",
            "Great Bear Bell Bear", "Campanile Esplanade Bear", "In Stadium Bear"};
    private double[] album_latitudes = {37.869288, 37.871305, 37.874118,
            37.871707, 37.869861, 37.871382,
            37.872061599999995, 37.87233810000001, 37.999999};
    private double[] album_longitudes = {-122.260125, -122.252516, -122.258778,
            -122.253602, -122.261148, -122.258355,
            -122.2578123, -122.25792999999999,     -122.999999};

    // for location purposes ---->

    private Location album_Location;


    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000; //10 sec
    private long FASTEST_INTERVAL = 2000;   //1 sec

    private int locationRequestCode = 1000;

    double currentlongitude;
    double currentlatitude;
    private FusedLocationProviderClient fusedLocationProviderClient;


    //to update the locations via lati and longi

    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    static final int MY_PERMISSION_REQUEST_ACCESS_LOCATION = 1;

    //when clicking on an image/album
    ImageView image;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berkeley__bears);
        Intent intent = getIntent();
        username = intent.getStringExtra("pass");
//        Log.d("Username", "Value: checking--->" + (username));

        updatelocation = findViewById(R.id.my_toolbarbutton);
        recyclerView = findViewById(R.id.my_recycler_view);
        image = findViewById(R.id.album);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        adapter = new MyAdapter(images, titles,currentlatitude,currentlongitude,album_latitudes,
                album_longitudes,album_Location,username);
        recyclerView.setAdapter(adapter);




        //location purposes--->
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();
        updatelocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getlocation();
//                Log.d("ADebugTag", "Value: " + Double.toString(currentlatitude));
//                Log.d("Location", "Value: " + (album_latitudes[0]));
//                Log.d("Username", "Value: " + (username));


//                Toast.makeText(getApplicationContext(), String.valueOf(currentlongitude), Toast.LENGTH_SHORT).show();
                adapter = new MyAdapter(images, titles,currentlatitude,currentlongitude,album_latitudes
                        ,album_longitudes,album_Location,username);
                recyclerView.setAdapter(adapter);
            }
        });

    }
    public void getlocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    locationRequestCode);
        } else {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                currentlatitude = location.getLatitude();
                                currentlongitude = location.getLongitude();
                                album_Location = location;
                            }
                        }
                    });
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection Suspended!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());

    }

    //creating request and updating location
    protected void createLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    @Override
    protected void onStart(){
        mGoogleApiClient.connect();
        super.onStart();
    }
    @Override
    protected void onStop(){
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            //set the locations
            currentlongitude = location.getLongitude();
            currentlatitude = location.getLatitude();
            album_Location = location;


        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Berkeley_Bears.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_REQUEST_ACCESS_LOCATION);

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);

    }


    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_ACCESS_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(Berkeley_Bears.this,
                            "permission was granted, :)",
                            Toast.LENGTH_LONG).show();

                    try{
                        LocationServices.FusedLocationApi.requestLocationUpdates(
                                mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
                    }catch(SecurityException e){
                        Toast.makeText(Berkeley_Bears.this,
                                "SecurityException:\n" + e.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(Berkeley_Bears.this,
                            "permission denied, ...:(",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
