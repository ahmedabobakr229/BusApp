package com.example.documents.busapp.Passenger;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.documents.busapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Pass_RegisterActivity extends AppCompatActivity {

    EditText ed_name, ed_email, ed_pass;
    Button regi;

    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass__register);

        ed_name = findViewById(R.id.name_pass);
        ed_email = findViewById(R.id.email_pass);
        ed_pass = findViewById(R.id.pass_pass);
        regi = findViewById(R.id.regstr_pass);

        mProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        regi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getEmail = ed_email.getText().toString().trim();
                String getPass = ed_pass.getText().toString().trim();

                mProgress.setTitle("Registering User");
                mProgress.setMessage("Wait while we create your account");
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.show();

                CallSignUp(getEmail , getPass);
            }
        });
    }
    private void CallSignUp(final String email, String pass) {
        mAuth.createUserWithEmailAndPassword(email , pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Testing","Signup successful" + task.isSuccessful());

                        if (!task.isSuccessful()){
                            Toast.makeText(Pass_RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            mProgress.dismiss();
                        }
                        else {

                            FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = current_user.getUid();

                            mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Passengers").child(uid);

                            HashMap<String , String> userMap = new HashMap<>();
                            userMap.put("email" , email);
                            userMap.put("lat","Hi there");
                            userMap.put("lng","Hi there");

                            mDatabase.setValue(userMap);

                            /*  */
                        }
                        if (task.isSuccessful()){
                            userProfile();
                            Toast.makeText(Pass_RegisterActivity.this, "Created Account", Toast.LENGTH_SHORT).show();
                            Log.d("TESTING", "Created Account");
                            mProgress.hide();
                        }
                    }
                });
    }
    private void userProfile() {

        FirebaseUser user = mAuth.getCurrentUser();
        if (user!=null){
            UserProfileChangeRequest profileupdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(ed_name.getText().toString().trim()).build();

            user.updateProfile(profileupdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()){
                        Log.d("TESTING","User profile updated.");
                    }
                }
            });

        }
    }
}
