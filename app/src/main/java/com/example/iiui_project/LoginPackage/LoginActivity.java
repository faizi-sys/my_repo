package com.example.iiui_project.LoginPackage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.iiui_project.DriverDashoard.DrivereDashboard;
import com.example.iiui_project.Model.AppUser;
import com.example.iiui_project.R;
import com.example.iiui_project.USerDashboard.UserDashboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    TextView Register ;
    TextInputEditText Email , Password ;
    FirebaseAuth mauth;
    Button Login ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        (Login = findViewById(R.id.email_sign_in_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProgressBar progressBar = findViewById(R.id.progress)
                ; progressBar.setVisibility(View.VISIBLE);

                mauth = FirebaseAuth.getInstance();

                Email = findViewById(R.id.edt_email);
                Password= findViewById(R.id.edt_password);

                if(Email.getText().toString().isEmpty()){
                    Email.setError("Pleause Enter Email");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

                if(Password.getText().toString().isEmpty()){
                    Password.setError("Pleause Enter Password");
                    progressBar.setVisibility(View.INVISIBLE);

                    return;
                }



                mauth.signInWithEmailAndPassword(Email.getText().toString(), Password.getText().toString())
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mauth.getCurrentUser();

                                    verifyUser(user.getUid());
                                    progressBar.setVisibility(View.INVISIBLE);
                                    // updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    // Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                    progressBar.setVisibility(View.INVISIBLE);

                                }

                                // ...
                            }
                        });


            }
        });

        (Register = findViewById(R.id.email_create_account_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( getApplicationContext() , RegistrationActivity.class));
            }
        });



    }

    private void verifyUser(String uid) {
        DatabaseReference UserDatabaseRef  = FirebaseDatabase.getInstance().getReference("User");
        UserDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    AppUser appUser = dataSnapshot1.getValue(AppUser.class);
                    if(appUser.getPushid().equals(uid)){
                       if(appUser.getUsertype().equals("User")){
                           Intent i = new Intent(getApplicationContext() , UserDashboard.class) ;
                           i.putExtra("name" , appUser.getName());
                            startActivity(i);
                           finish();
                       }
                        if(appUser.getUsertype().equals("Driver")){
                            Intent i = new Intent(getApplicationContext() , DrivereDashboard.class) ;
                            i.putExtra("name" , appUser.getName());
                            startActivity(i);
                            finish();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
}
