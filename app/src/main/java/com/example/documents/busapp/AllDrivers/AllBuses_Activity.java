package com.example.documents.busapp.AllDrivers;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.PersistableBundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.documents.busapp.R;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllBuses_Activity extends AppCompatActivity implements UserClick, OnMapReadyCallback {

    private Toolbar mToolbar;

    private RecyclerView mUsersList;

    private DatabaseReference mUsersDatabase;

    private LinearLayoutManager mLayoutManager;
    private UserAdapter userAdapter;
    TextView name;
    MapView mapView;
    private static final String MapViewBundle_Key = "MapViewBundleKey";

    ArrayList lon = new ArrayList();
    ArrayList lat = new ArrayList();

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_buses_);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MapViewBundle_Key);
        }

        searchView = (SearchView) findViewById(R.id.search_bar);

        //-----------------------------------------
        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
        //-----------------------------------------

        mLayoutManager = new LinearLayoutManager(this);

        mUsersList = (RecyclerView) findViewById(R.id.bus_list);
        name = (TextView) findViewById(R.id.driver_info);

        mUsersList.setLayoutManager(mLayoutManager);
        mUsersList.setHasFixedSize(true);

        fetchFeeds();


    }

    @Override
    public void asd(Buss buss) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MapViewBundle_Key);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MapViewBundle_Key, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }


    public ArrayList<Buss> fetchFeeds() {

        final ArrayList<Buss> feeds = new ArrayList<>();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        database.child("Users").child("Drivers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {

                        Buss feed = new Buss(noteDataSnapshot.child("numBus").getValue(String.class)
                                , noteDataSnapshot.child("avaSeats").getValue(String.class)
                                , noteDataSnapshot.child("Stations").getValue(String.class));
                        feed.setId(noteDataSnapshot.getKey());

                        lon.add(Double.parseDouble(noteDataSnapshot.child("lng").getValue().toString()));
                        lat.add(Double.parseDouble(noteDataSnapshot.child("lat").getValue().toString()));
                        feeds.add(feed);
                    }
                    userAdapter = new UserAdapter(AllBuses_Activity.this);
                    userAdapter.setUsersData(feeds, AllBuses_Activity.this);
                    mUsersList.setAdapter(userAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return feeds;
    }
//
//    @Override
//    public void onMapReady(GoogleMap map) {
//
//        //map.addMarker(new MarkerOptions().position(new LatLng(0,0)).title("marker"));
//        if (ActivityCompat.checkSelfPermission(AllBuses_Activity.this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
//                (AllBuses_Activity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            return;
//        }else {
//            map.setMyLocationEnabled(true);
//        }
//
//    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    private void moveToCurrentLocation(LatLng currentLocation)
    {



    }

    @Override
    public void onMapReady(final GoogleMap map) {
        Toast.makeText(this, "test" + lon.size(), Toast.LENGTH_SHORT).show();
        final ArrayList<Buss> feeds = new ArrayList<>();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        float zoomLevel = 16.0f;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(32,32),zoomLevel));
        map.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

        try {
            database.child("Users").child("Drivers").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null) {
                        for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                            try {
                                Buss feed = new Buss(noteDataSnapshot.child("numBus").getValue().toString()
                                        , noteDataSnapshot.child("avaSeats").getValue().toString()
                                        , noteDataSnapshot.child("Stations").getValue().toString());
                                feed.setId(noteDataSnapshot.getKey());

                                double d = Double.parseDouble(noteDataSnapshot.child("lng").getValue().toString());
                                double l = Double.parseDouble(noteDataSnapshot.child("lat").getValue().toString());
                                map.addMarker(new MarkerOptions().position(new LatLng(l,d)).
                                        title(String.valueOf(noteDataSnapshot.child("numBus").getValue())));

                                // Double lat=(Double) snapshot.child().getValue();
                                // Double.parseDouble(noteDataSnapshot.child("lat").getValue().toString());
                                //(Double)noteDataSnapshot.child("lng").getValue();
                                feeds.add(feed);
                            }catch (Exception e){

                            }

                        }
                        userAdapter = new UserAdapter(AllBuses_Activity.this);
                        userAdapter.setUsersData(feeds, AllBuses_Activity.this);
                        mUsersList.setAdapter(userAdapter);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });


           // map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("fhk"));
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "working", Toast.LENGTH_SHORT).show();
                    return;
                }
                map.setMyLocationEnabled(true);
            }catch (Exception e){

            }


        }catch (Exception e){

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
