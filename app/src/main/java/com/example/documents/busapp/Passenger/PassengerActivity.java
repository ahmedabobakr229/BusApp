package com.example.documents.busapp.Passenger;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.documents.busapp.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PassengerActivity extends AppCompatActivity {

    Button login_btn , register;
    EditText email , password ;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    private static final String TAG = "FACELOG";
    LoginButton facebook_login ;
    CallbackManager callbackManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger);

        login_btn = (Button)findViewById(R.id.login_id_pass);
        register = (Button)findViewById(R.id.register_id_pass);
        facebook_login = (LoginButton) findViewById(R.id.login_button_pass);

        email = (EditText)findViewById(R.id.email_id_pass);
        password = (EditText)findViewById(R.id.pass_id_pass);
        mAuth = FirebaseAuth.getInstance();

        facebook_login.setReadPermissions("email" , "public_profile");
        callbackManager = CallbackManager.Factory.create();

        facebook_login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });

        progressDialog = new ProgressDialog(this);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = email.getText().toString().trim();
                String Pass = password.getText().toString().trim();

                if (!TextUtils.isEmpty(Email) || !TextUtils.isEmpty(Pass)){
                    progressDialog.setTitle("Logging in");
                    progressDialog.setMessage("Please wait while checking your information");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    loginUser(Email , Pass);
                }

                // Toast.makeText(LoginActivity.this, "ffffff", Toast.LENGTH_SHORT).show();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PassengerActivity.this,Pass_RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        Log.d(TAG, "handleFacebookAccessToken:" + accessToken);

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(PassengerActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser currentUser) {
        Toast.makeText(this, "you are logged in", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(PassengerActivity.this,Pass_InfoActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!= null){
            updateUI(currentUser);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void loginUser(String email, String pass) {

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(PassengerActivity.this, "sucsess.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Intent i = new Intent(PassengerActivity.this,Pass_InfoActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {

                            Toast.makeText(PassengerActivity.this, "Please make sure your information is true.",
                                    Toast.LENGTH_LONG).show();
                            progressDialog.hide();

                        }
                    }
                });
    }
}
