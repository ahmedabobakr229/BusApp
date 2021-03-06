package com.example.documents.busapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splashctivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashctivity);

        Thread thread = new Thread(){
            public void run(){
                try {
                    sleep(4000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(new Intent(Splashctivity.this , MainActivity.class));
                    finish();
                }
            }
        };
        thread.start();
    }
}
