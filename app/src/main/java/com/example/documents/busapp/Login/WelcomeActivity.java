package com.example.documents.busapp.Login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.documents.busapp.Login.RegistrationActivity;
import com.example.documents.busapp.MainActivity;
import com.example.documents.busapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity {

    Button signout;
    private FirebaseAuth mAuth;
    TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mAuth = FirebaseAuth.getInstance();
        signout = (Button)findViewById(R.id.signout);
        username = (TextView)findViewById(R.id.tvname);

        if (mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));

        }
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null){
            username.setText("Welcome ," + user.getDisplayName());
        }

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                finish();
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });
    }
}
