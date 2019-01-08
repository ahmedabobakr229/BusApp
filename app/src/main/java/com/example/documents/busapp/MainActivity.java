package com.example.documents.busapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.example.documents.busapp.Login.LoginActivity;
import com.example.documents.busapp.Passenger.PassengerActivity;

public class MainActivity extends AppCompatActivity {

CardView pass , drive ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pass = (CardView)findViewById(R.id.pass_id);
        drive = (CardView)findViewById(R.id.driver_id);

        drive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,PassengerActivity.class);
                startActivity(i);
            }
        });
    }
}
