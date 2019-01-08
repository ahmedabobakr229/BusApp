package com.example.documents.busapp.Driver;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.documents.busapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

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
    private FirebaseUser mCurrentUser;

    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o);

        mLatitudeText = (TextView) findViewById((R.id.latitude_text));
        mLongitudeText = (TextView) findViewById((R.id.longitude_text));
        addData = (Button)findViewById(R.id.addToFirebase);

        getActivity();
      //  registerLocationUpdates();
//        locationManager = (LocationManager)getActivity().getSystemService(LOCATION_SERVICE);
//        locationManager.requestLocationUpdates(provider, 1L, 1f, locationListener);
//        locationManager.requestLocationUpdates();

        buildGoogleApiClient();

        initializeLocationManager();

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5 * 1000);
        locationRequest.setFastestInterval(2 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        mLocationDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(current_uid);

        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                latitude = mLatitudeText.getText().toString();
                longtude = mLongitudeText.getText().toString();

                mLocationDatabase.child("lat").setValue(latitude).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){


                            Toast.makeText(OActivity.this, "Changed Successfully", Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(getApplicationContext(), "There was some error in saving Changes.", Toast.LENGTH_LONG).show();

                        }

                    }
                });

                mLocationDatabase.child("lng").setValue(longtude).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){


                            Toast.makeText(OActivity.this, "Changed Successfully", Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(getApplicationContext(), "There was some error in saving Changes.", Toast.LENGTH_LONG).show();

                        }

                    }
                });

            }
        });
    }



    private void getActivity() {

    }


    private void initializeLocationManager() {

        locationListener = new LocationListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onLocationChanged(Location location) {
                latitude = String.valueOf(location.getLatitude());
                longtude = String.valueOf(location.getLongitude());




                if (TextUtils.isEmpty(latitude) || TextUtils.isEmpty(longtude) || latitude == null || longtude == null) {
                        Log.i(TAG , "you must be out door !");
                        Toast.makeText(OActivity.this, "you must be out door", Toast.LENGTH_SHORT).show();

                        latitude = mLatitudeText.getText().toString();
                        longtude = mLongitudeText.getText().toString();

                        mLocationDatabase.child("lat").setValue(latitude).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){


                                    Toast.makeText(OActivity.this, "Changed Successfully", Toast.LENGTH_SHORT).show();

                                } else {

                                    Toast.makeText(getApplicationContext(), "There was some error in saving Changes.", Toast.LENGTH_LONG).show();

                                }

                            }
                        });

                        mLocationDatabase.child("lng").setValue(longtude).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){


                                    Toast.makeText(OActivity.this, "Changed Successfully", Toast.LENGTH_SHORT).show();

                                } else {

                                    Toast.makeText(getApplicationContext(), "There was some error in saving Changes.", Toast.LENGTH_LONG).show();

                                }

                            }
                        });


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

    //___________________________________________________________________________________________

//    @SuppressLint("LongLogTag")
//    private void initializeLocationManager() {
//        Log.e(TAG, "initializeLocationManager");
//        locationListener = new LocationListener() {
//            @SuppressLint("LongLogTag")
//            @Override
//            public void onLocationChanged(Location location) {
//                try {
//                    latitude = String.valueOf(location.getLatitude());
//                    longtude = String.valueOf(location.getLongitude());
//                    if (TextUtils.isEmpty(latitude) || TextUtils.isEmpty(longtude) || latitude == null || longtude == null) {
//                        Log.i(TAG , "you must be out door !");
//                        Toast.makeText(OActivity.this, "you must be out door", Toast.LENGTH_SHORT).show();
//
//                        latitude = mLatitudeText.getText().toString();
//                        longtude = mLongitudeText.getText().toString();
//
//                        mLocationDatabase.child("lat").setValue(latitude).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//
//                                if(task.isSuccessful()){
//
//
//                                    Toast.makeText(OActivity.this, "Changed Successfully", Toast.LENGTH_SHORT).show();
//
//                                } else {
//
//                                    Toast.makeText(getApplicationContext(), "There was some error in saving Changes.", Toast.LENGTH_LONG).show();
//
//                                }
//
//                            }
//                        });
//
//                        mLocationDatabase.child("lng").setValue(longtude).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//
//                                if(task.isSuccessful()){
//
//
//                                    Toast.makeText(OActivity.this, "Changed Successfully", Toast.LENGTH_SHORT).show();
//
//                                } else {
//
//                                    Toast.makeText(getApplicationContext(), "There was some error in saving Changes.", Toast.LENGTH_LONG).show();
//
//                                }
//
//                            }
//                        });
//
//
//                    } else {
//                        Log.i(TAG , latitude + "   " + longtude);
//                        // setof(latitude , longtude);
//                    }
//
//                } catch (Exception e) {
//                    //  Toast.makeText(getContext(), "you must be out door !", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//                Log.i(TAG  , "onProviderEnabled");
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//
//            }
//        };
//
//    }

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

        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
//        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());

    }
}
