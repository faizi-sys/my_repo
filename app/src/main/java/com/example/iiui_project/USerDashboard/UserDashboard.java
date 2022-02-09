package com.example.iiui_project.USerDashboard;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.iiui_project.LoginPackage.LoginActivity;
import com.example.iiui_project.R;
import com.example.iiui_project.See_Notification_Accept_User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class UserDashboard extends AppCompatActivity {
    Button ViewDriver  , AddDrivees;
    Button Profile ;
    Button DeriveRq ;
    FirebaseAuth mAuth ;
    FirebaseUser user ;
    Button ViewResponse ;
    Button SignOutBtn ;
    TextView textVeiw ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboarduser);


        (textVeiw = findViewById(R.id.na)).setText(getIntent().getStringExtra("name"));
        ( SignOutBtn = findViewById(R.id.buyerSignout))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext()  , EmergencyAleryMEssage.class));
                    }
                });
        ( ViewResponse = findViewById(R.id.btn3))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext() , See_Notification_Accept_User.class));
                    }
                });

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        (DeriveRq = findViewById(R.id.addderive)).setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext() , Add_Personal_Derive.class));
            }

        });

        Button SignOut ;
        (SignOut = findViewById(R.id.btn4)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });



        (ViewDriver = findViewById(R.id.viewderve)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext() , ViewDriverActivity.class));
            }
        });

        (Profile = findViewById(R.id.profile)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext() , UserUpdateActivityUser.class);
                i.putExtra("push" ,user.getUid() );
                startActivity(i);

            }
        });





    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(UserDashboard.this)
                .setTitle("User Logout")
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
