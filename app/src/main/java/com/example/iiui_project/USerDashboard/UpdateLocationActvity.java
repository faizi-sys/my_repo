package com.example.iiui_project.USerDashboard;


import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.iiui_project.Model.AppUser;
import com.example.iiui_project.R;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateLocationActvity extends FragmentActivity implements OnMapReadyCallback {

    public static final int GPS_REQUEST_CODE = 9003;
    DatabaseReference UserFirebaseDatabasae;
    String UserName, Password, Emails, Phone, Lattitude, Longitude, picUri, Cnic, status, Skills, City;
    String Age, Price;

    String PushKey;


    String CarNumber = "";

    private GoogleMap mMap;
    private boolean mLocationPermissionGrandted;
    private FusedLocationProviderClient mfusedLocationProviderClient;
    public static final int permissioncode = 1234;


    FirebaseAuth mAuth;
    FirebaseUser user;
    public static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_map_house);
        getLocatioPermissionGranted();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        UserFirebaseDatabasae = FirebaseDatabase.getInstance().getReference("User");
        PushKey = getIntent().getStringExtra("pushkey");


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        addClickListener(mMap);

        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        if(isServiceOK()){
        if(isGpsEnabled()){
        getLocatioPermissionGranted();
        if (mLocationPermissionGrandted) {
            geTDeviseLoaction();
           if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
        }}}


    }

    private void addClickListener(GoogleMap map) {
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                final String lat = String.valueOf(point.latitude);
                final String longi = String.valueOf(point.longitude) ;
                Handler handler = new Handler() ;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                },500);
                new AlertDialog.Builder(UpdateLocationActvity.this)
                        .setTitle("Location Manual Update")
                        .setMessage("Do You Want Manually add Location")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                setResult(RESULT_OK, new Intent().putExtra("Yes", true));
                                try {
                                    AppUser usersModelClass = new AppUser(
                                            UserName, Emails, Password, Cnic,  Phone,Age, lat, longi,  picUri,  status, "Driver", "None", "None ", PushKey, "None", City  , CarNumber
                                    );
                                    UserFirebaseDatabasae.child(user.getUid()).setValue(usersModelClass);
                                    Log.d("current", "location successsfufll");
                                }catch (Exception e){
                                    Toast.makeText(getApplicationContext() , "net slow" , Toast.LENGTH_LONG).show();
                                }
                            }

                        }).create().show();
            }
        });
    }
    public  void  getLocatioPermissionGranted(){
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION ,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGrandted =  true ;
            }
            else{
                ActivityCompat.requestPermissions(this , permission,permissioncode);
            }
        }
        else{
            ActivityCompat.requestPermissions(this , permission,permissioncode);
        }
    }
    private void geTDeviseLoaction() {
        mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionGrandted) {
                Task location = mfusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            try {
                                Location location1 = (Location) task.getResult();

                                 if(location1 !=null) {
                                     moveCamera(new LatLng(location1.getLatitude(), location1.getLongitude()), 15f);
                                 }

                            }catch (Exception e ){
                                Toast.makeText(getApplicationContext() , e.toString() , Toast.LENGTH_LONG).show();
                                Log.d("error in update"  , e.toString());
                            }
                            }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext() , e.toString() , Toast.LENGTH_LONG).show();
                        Log.d("error in update"  , e.toString());
                    }
                });
            }

        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    void moveCamera(LatLng latLng, float Zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, Zoom));
        LatLng CurrentLocation = new LatLng(latLng.latitude, latLng.longitude);
        mMap.addMarker(new MarkerOptions().position(CurrentLocation).title(" Your Current Location ").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));


        final String lat = String.valueOf(latLng.latitude);
        final String longi = String.valueOf(latLng.longitude) ;

        Handler handler = new Handler() ;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        },500);
        new AlertDialog.Builder(this)
                .setTitle("Current Location Update")
                .setMessage("Do You Want Current Location")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        setResult(RESULT_OK, new Intent().putExtra("Yes", true));
                        try {
                            AppUser usersModelClass = new AppUser(

                                    UserName, Emails, Password, Cnic,  Phone,Age, Lattitude, Longitude,  picUri,  status, "Driver", "None", "None ",getIntent().getStringExtra("pushkey") , "None", City  , CarNumber);
                            UserFirebaseDatabasae.child(PushKey).setValue(usersModelClass);
                            Log.d("current", "location successsfufll");
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext() , "net slow" , Toast.LENGTH_LONG).show();
                        }
                    }

                }).create().show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GPS_REQUEST_CODE){
            if(isGpsEnabled()){
                Toast.makeText(getApplicationContext() , "ISEnabled" , Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext() , "NOt enabled" , Toast.LENGTH_LONG).show();
            }
        }
    }
    private  Boolean isGpsEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean providerEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(providerEnabled){
            return true ;
        }
        else{
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("GPS Permission")
                    .setMessage("Please Enable Gps")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(i , GPS_REQUEST_CODE);
                        }
                    }).setCancelable(false)
                    .show();
        }
        return  false ;
    }
    @Override
    protected void onStart() {
        super.onStart();


        UserFirebaseDatabasae.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    AppUser usersModelClass = dataSnapshot1.getValue(AppUser.class);
                    if(user.getUid().equals(usersModelClass.getPushid())){
                        UserName = usersModelClass.getName();
                        Password = usersModelClass.getPassword();
                        Emails = usersModelClass.getEmail() ;
                        Phone = usersModelClass.getPhone();
                        Lattitude = usersModelClass.getLattitude();
                        Longitude = usersModelClass.getLongitude();
                        picUri = usersModelClass.getPicUri();
                        Cnic = usersModelClass.getCnic();
                        status = usersModelClass.getStatus();
                        Skills = usersModelClass.getUsertype();
                        City = usersModelClass.getLocation();
                        Age = usersModelClass.getAge() ;
                        Price = usersModelClass.getPrice();
                        Toast.makeText(UpdateLocationActvity.this, "ok", Toast.LENGTH_SHORT).show();

                        CarNumber = usersModelClass.getCarNumber();
                    }
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private  Boolean isServiceOK(){
        GoogleApiAvailability googleApi = GoogleApiAvailability.getInstance();
        int result = googleApi.isGooglePlayServicesAvailable(this);
        if(result == ConnectionResult.SUCCESS){
            return true ;
        }
        else if (googleApi.isUserResolvableError(result)){
            Dialog dialog = googleApi.getErrorDialog(this , result , 9002 ,task->
                    Toast.makeText(this, "Dialog is cancel", Toast.LENGTH_SHORT).show());

            dialog.show();
        }else{
            Toast.makeText(this, "Google play service requreired", Toast.LENGTH_SHORT).show();

        }
        return false ;
    }

}
