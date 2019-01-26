package com.example.documents.busapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class EveryBus_Activity extends AppCompatActivity {


    TextView bus_num , available_seats , stations ;
    DatabaseReference databaseReference ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_every_bus_);

        bus_num = (TextView)findViewById(R.id.busNum_every);
        available_seats = (TextView)findViewById(R.id.ava_every);
        stations = (TextView)findViewById(R.id.station_every);

        String number  = getIntent().getStringExtra("busNum");
        String seats = getIntent().getStringExtra("AvaSeats");
        String stationy = getIntent().getStringExtra("Stations");

        available_seats.setText(seats);
        bus_num.setText(number);
        stations.setText(stationy);

    }
}
