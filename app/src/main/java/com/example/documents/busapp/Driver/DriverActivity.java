package com.example.documents.busapp.Driver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.documents.busapp.Login.LoginActivity;
import com.example.documents.busapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverActivity extends AppCompatActivity {

    Button enable_location ,enter , logout;
    private DatabaseReference mDriverDatabase;
    private FirebaseUser mCurrentUser;
    private ProgressDialog mProgress;
    private TextInputLayout mNum , ava;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);


        logout =  (Button)findViewById(R.id.logout);
        enable_location = (Button)findViewById(R.id.enable_id);
        enter = (Button)findViewById(R.id.save_id);

        mNum = (TextInputLayout) findViewById(R.id.numOfBus);
        ava = (TextInputLayout) findViewById(R.id.seats_id);

        String status_value = getIntent().getStringExtra("number_of_bus");
        String seats = getIntent().getStringExtra("Available_Seatsِ");

        mNum.getEditText().setText(status_value);
        ava.getEditText().setText(seats);

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
             Intent i = new Intent(DriverActivity.this , OActivity.class);
             startActivity(i);
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

         }
     });

    }

    private void sendToMain() {
            Intent i = new Intent(DriverActivity.this,LoginActivity.class);
            startActivity(i);
            finish();
    }
}
