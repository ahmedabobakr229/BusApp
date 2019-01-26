package com.example.documents.busapp.Driver;

import android.Manifest;
import com.google.android.gms.appindexing.AppIndex;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.documents.busapp.Login.LoginActivity;
import com.example.documents.busapp.MyService;
import com.example.documents.busapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverActivity extends AppCompatActivity  implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    Button enable_location ,enter , logout;
    private DatabaseReference mDriverDatabase;
    private FirebaseUser mCurrentUser;
    private ProgressDialog mProgress;
    private TextInputLayout mNum , ava , stations;
    private GoogleApiClient googleApiClient;
    //----------------------------
    protected static final String TAG = "Location Services Lesson 2-1";
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    protected TextView mLatitudeText;
    protected TextView mLongitudeText;
    private Button addData ;

    PendingResult<LocationSettingsResult> result;
    LocationListener locationListener ;
    String latitude;
    String longtude;
    LocationRequest locationRequest;
    LocationManager locationManager;
    private DatabaseReference mLocationDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);


        logout =  (Button)findViewById(R.id.logout);
        enable_location = (Button)findViewById(R.id.enable_id);
        enter = (Button)findViewById(R.id.save_id);

        mNum = (TextInputLayout) findViewById(R.id.numOfBus);
        ava = (TextInputLayout) findViewById(R.id.seats_id);
        stations = (TextInputLayout)findViewById(R.id.station_id);

        //__________________________________________________

        mLatitudeText = (TextView)findViewById(R.id.latitude_textn);
        mLongitudeText = (TextView)findViewById(R.id.longitude_textn);
        buildGoogleApiClient();

       // initializeLocationManager();

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5 * 1000);
        locationRequest.setFastestInterval(2 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

       result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        //___________________________________________________

        String status_value = getIntent().getStringExtra("number_of_bus");
        String seats = getIntent().getStringExtra("Available_Seatsِ");
        String getStation = getIntent().getStringExtra("Stations");

        mNum.getEditText().setText(status_value);
        ava.getEditText().setText(seats);
        stations.getEditText().setText(getStation);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        mDriverDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(current_uid);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                sendToMain();
            }
        });

                //adding current location to firebase database .......
        enable_location.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             startService(new Intent(DriverActivity.this, MyService.class));
             if (googleApiClient == null) {
                 googleApiClient = new GoogleApiClient.Builder(getApplicationContext()).addApi(LocationServices.API).build();
                 googleApiClient.connect();
                 LocationRequest locationRequest = LocationRequest.create();
                 locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                 locationRequest.setInterval(1000);
                 locationRequest.setFastestInterval(1000);
                 LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                         .addLocationRequest(locationRequest);
                 builder.setAlwaysShow(true); // this is the key ingredient

                 PendingResult result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
                 result.setResultCallback(new ResultCallback() {
                     @Override
                     public void onResult(@NonNull Result result) {
                         final Status status = result.getStatus();
                          // final LocationSettingsStates state = result.getLocationSettingsStates();
                         switch (status.getStatusCode()) {
                             case LocationSettingsStatusCodes.SUCCESS:
                                     startService(new Intent(DriverActivity.this, MyService.class));
                                 Toast
                                         .makeText(DriverActivity.this, "Service is running", Toast.LENGTH_SHORT).show();
                                 break;
                             case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                 try {
                                     status.startResolutionForResult(DriverActivity.this, 1000);
                                 } catch (IntentSender.SendIntentException e) {
                                 }
                                 break;
                             case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                 break;
                         }
                     }

                 });
                // googleApiClient = new GoogleApiClient.Builder(DriverActivity.this).addApi(AppIndex.API).build();

             }
         }
     });

     enter.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

             mProgress = new ProgressDialog(DriverActivity.this);
             mProgress.setTitle("Saving Changes");
             mProgress.setMessage("Please wait while we save the changes");
             mProgress.show();

             String numberOfBus = mNum.getEditText().getText().toString();
             String Available_seats = ava.getEditText().getText().toString();
             String setStation = stations.getEditText().getText().toString();

             mDriverDatabase.child("numBus").setValue(numberOfBus).addOnCompleteListener(new OnCompleteListener<Void>() {
                 @Override
                 public void onComplete(@NonNull Task<Void> task) {

                     if(task.isSuccessful()){

                         mProgress.dismiss();
                         Toast.makeText(DriverActivity.this, "Changed Successfully", Toast.LENGTH_SHORT).show();

                     } else {

                         Toast.makeText(getApplicationContext(), "There was some error in saving Changes.", Toast.LENGTH_LONG).show();

                     }

                 }
             });
             mDriverDatabase.child("avaSeats").setValue(Available_seats).addOnCompleteListener(new OnCompleteListener<Void>() {
                 @Override
                 public void onComplete(@NonNull Task<Void> task) {

                     if(task.isSuccessful()){

                         mProgress.dismiss();
                         Toast.makeText(DriverActivity.this, "Changed Successfully", Toast.LENGTH_SHORT).show();

                     } else {

                         Toast.makeText(getApplicationContext(), "There was some error in saving Changes.", Toast.LENGTH_LONG).show();

                     }

                 }
             });
             mDriverDatabase.child("Stations").setValue(setStation).addOnCompleteListener(new OnCompleteListener<Void>() {
                 @Override
                 public void onComplete(@NonNull Task<Void> task) {

                     if(task.isSuccessful()){

                         mProgress.dismiss();
                         Toast.makeText(DriverActivity.this, "Changed Successfully", Toast.LENGTH_SHORT).show();

                     } else {

                         Toast.makeText(getApplicationContext(), "There was some error in saving Changes.", Toast.LENGTH_LONG).show();

                     }

                 }
             });

         }
     });

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private void initializeLocationManager() {

        locationListener = new LocationListener() {

            @SuppressLint("LongLogTag")
            @Override
            public void onLocationChanged(Location location) {

                Toast.makeText(DriverActivity.this, "ahhahahhhh", Toast.LENGTH_SHORT).show();
                latitude = String.valueOf(location.getLatitude());
                longtude = String.valueOf(location.getLongitude());

                latitude = mLatitudeText.getText().toString();
                longtude = mLongitudeText.getText().toString();
                mLocationDatabase.child("lat").setValue(latitude).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){


                            Toast.makeText(DriverActivity.this, "Changed Successfully", Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(getApplicationContext(), "There was some error in saving Changes.", Toast.LENGTH_LONG).show();

                        }

                    }
                });
                mLocationDatabase.child("lng").setValue(longtude).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){


                            Toast.makeText(DriverActivity.this, "Changed Successfully", Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(getApplicationContext(), "There was some error in saving Changes.", Toast.LENGTH_LONG).show();

                        }

                    }
                });


                if (TextUtils.isEmpty(latitude) || TextUtils.isEmpty(longtude) || latitude == null || longtude == null) {
                    Log.i(TAG , "you must be out door !");
                    Toast.makeText(DriverActivity.this, "you must be out door", Toast.LENGTH_SHORT).show();


                } else {
                    Log.i(TAG , latitude + "   " + longtude);
                    // setof(latitude , longtude);
                }

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
    }


    private void sendToMain() {
            Intent i = new Intent(DriverActivity.this,LoginActivity.class);
            startActivity(i);
            finish();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        }

    }

    @SuppressLint("LongLogTag")
    @Override
    public void onConnectionSuspended(int i) {

        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
