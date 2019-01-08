package com.example.documents.busapp.Passenger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.documents.busapp.AllDrivers.AllBuses_Activity;
import com.example.documents.busapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class Pass_InfoActivity extends AppCompatActivity {


    Button catchBus , enableLocation , logout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass__info);

        catchBus = (Button)findViewById(R.id.catch_id_pass);
        enableLocation = (Button)findViewById(R.id.enable_id_pass);
        logout = (Button)findViewById(R.id.logoutBtn);

        catchBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Pass_InfoActivity.this,AllBuses_Activity.class);
                startActivity(i);
            }
        });

        enableLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Pass_InfoActivity.this, "Current location", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Pass_InfoActivity.this,LocationDriverActivity.class);
                startActivity(i);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                sendToMain();
            }
        });
    }

    private void sendToMain() {
        Intent i = new Intent(Pass_InfoActivity.this,PassengerActivity.class);
        startActivity(i);
        finish();
    }
}
