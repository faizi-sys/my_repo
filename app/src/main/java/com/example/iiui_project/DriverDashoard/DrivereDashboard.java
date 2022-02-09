package com.example.iiui_project.DriverDashoard;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.iiui_project.DriverActivities.AddDerives;
import com.example.iiui_project.DriverActivities.ViewDerives;
import com.example.iiui_project.LoginPackage.LoginActivity;
import com.example.iiui_project.R;
import com.example.iiui_project.See_Notification;
import com.example.iiui_project.See_Notification_Accept_Driver;
import com.example.iiui_project.USerDashboard.UserDashboard;
import com.example.iiui_project.USerDashboard.UserUpdateActivityUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class DrivereDashboard extends AppCompatActivity {
    Button ViewDriver  , AddDrivees;
    Button Profile ;
    FirebaseAuth mAuth ;
    FirebaseUser user ;
    Button ViewDerive ;
    Button ViewNotficationRes ;
    Button ResponseChat ;
    Button See_Profile ;
    Button SignOutBtn ;
    Button ViewUser ;
    TextView textView ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboarddriver);


        (textView = findViewById(R.id.na)).setText(getIntent().getStringExtra("name"));
  ( SignOutBtn = findViewById(R.id.buyerSignout))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAuth.signOut();
                        startActivity(new Intent(getApplicationContext() , LoginActivity.class));
                        finish();
                    }
                });

        (See_Profile = findViewById(R.id.profile))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext() , UserUpdateActivityDriver.class);
                        i.putExtra("push" ,user.getUid() );
                        startActivity(i);
                    }
                });
        (ResponseChat = findViewById(R.id.btn4)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(
                        getApplicationContext() , See_Notification_Accept_Driver.class
                ));
            }
        });

        (ViewUser = findViewById(R.id.viewuser1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(
                        getApplicationContext() , ViewUserActivity.class
                ));
            }
        });






        (ViewNotficationRes = findViewById(R.id.btn3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext() , See_Notification.class));
            }
        });

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();



        (AddDrivees = findViewById(R.id.addderive)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext() , AddDerives.class);
                i.putExtra("push" ,user.getUid() );
                startActivity(i);
            }
        });

        (ViewDerive = findViewById(R.id.viewderve)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext() , ViewDerives.class));
            }
        });


    }


    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(DrivereDashboard.this  )
                .setTitle("Driver  Logout")
                .setMessage("Are You Sure You  Want to Logout")

                .setNegativeButton("No", null)
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        mAuth.signOut();
                        startActivity(new Intent(getApplicationContext() , LoginActivity.class));
                        finish();
                    }

                }).create().show();
    }
}
