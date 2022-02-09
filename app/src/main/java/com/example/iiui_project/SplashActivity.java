package com.example.iiui_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.iiui_project.LoginPackage.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashactivity);



        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep( 40);
                } catch (Exception e) {

                } finally {

                    startActivity(new Intent( getApplicationContext() , LoginActivity.class));
                    finish();
                }
            };
        };
        splashTread.start();

    }
}
