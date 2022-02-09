package com.example.iiui_project.USerDashboard;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.iiui_project.R;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EmergencyAleryMEssage extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    EditText Number , Message  ;
    Button Send ;


    private static final int PERMISSION_REQUEST = 101;

    private FusedLocationProviderClient mfusedLocationProviderClient;
    private GoogleMap mMap;
    public static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final int permissioncode = 1234;
    Boolean mLocationPermissionGrandted ;

    double lat, lon;
    LocationManager locationManager;
    LocationListener locationListener;
    String Cnicget, UserName, Password, Contact, Address, Uri;
    Intent intent;
    DatabaseReference databaseReference;

    String LiveLatitude ="";
    String LiveLongitude = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_alery_message);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Number = findViewById(R.id.number);
        Message = findViewById(R.id.message1);
        Send = findViewById(R.id.send);
        Send.setOnClickListener(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);
        getLocatioPermissionGranted();

        geTDeviseLoaction() ;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
    }



    public void getLocatioPermissionGranted() {

        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGrandted = true;
            } else {
                ActivityCompat.requestPermissions(this, permission, permissioncode);
            }

        } else {
            ActivityCompat.requestPermissions(this, permission, permissioncode);
        }

    }

    private void geTDeviseLoaction() {


        mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionGrandted) {
                final Task location = mfusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location location1 = (Location) task.getResult();

                            if (location1 != null) {
                                try {


                                    LiveLatitude = String.valueOf(location1.getLatitude());
                                    LiveLongitude = String.valueOf(location1.getLongitude());




                                    Toast.makeText(getApplicationContext(), " Location" + LiveLongitude, Toast.LENGTH_LONG).show();

                                } catch (Exception e) {
                                    Toast.makeText(EmergencyAleryMEssage.this, "Gps en", Toast.LENGTH_SHORT).show();
                                }

                                try {


                                    LatLng CurrentLocation = new LatLng(location1.getLatitude(), location1.getLongitude());
                                    mMap.addMarker(new MarkerOptions().position(CurrentLocation).title(" Your Current in Location ").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                                    //   moveCamera(new LatLng(location1.getLatitude(), location1.getLongitude()), 15f);
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "enabling gps", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }

                });
            }
        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){

            case  0:
                if(grantResults.length>=0 && grantResults[0]== PackageManager.PERMISSION_DENIED){

                    MyMesssage();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Yout Dont have Permission" ,  Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void MyMesssage() {
 Toast.makeText(getApplicationContext(), "if k andar ", Toast.LENGTH_LONG).show();
        String message = "I am in Emerygency " + getAddress(Double.valueOf(LiveLatitude) , Double.valueOf(LiveLongitude) );
        Message.setText(message);
        String number = Number.getText().toString().trim();
        SmsManager smsManager = SmsManager.getDefault();

        if (!TextUtils.isEmpty(message) && !TextUtils.isEmpty(number)) {

            Toast.makeText(getApplicationContext(), " " + message + " " + number, Toast.LENGTH_LONG).show();
            try {

                ArrayList<String> parts = smsManager.divideMessage(message);
                smsManager.sendMultipartTextMessage(number, null, parts, null, null);

                Toast.makeText(getApplicationContext(), "MEssage Send ", Toast.LENGTH_LONG).show();

            }catch (Exception e){
                Toast.makeText(getApplicationContext(), "Exception" + e.getMessage().toString(), Toast.LENGTH_LONG).show();

            }
            Toast.makeText(getApplicationContext(), "MEssage Send ", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Message Is Empty", Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public void onClick(View v) {


        int id = v.getId();

        if(id == R.id.send){

            int PermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
            if(PermissionCheck == PackageManager.PERMISSION_GRANTED){

                MyMesssage();
                Toast.makeText(getApplicationContext(), "MEssage is sending", Toast.LENGTH_SHORT).show();
            }
            else {
                ActivityCompat.requestPermissions(this , new  String[]{Manifest.permission.SEND_SMS} , 0);
            }
        }
    }

    public Address getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(EmergencyAleryMEssage.this, Locale.getDefault());
        Address obj = null  ;
        try {
            List<android.location.Address> addresses = geocoder.getFromLocation(lat, lng, 1);
             obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();

            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return obj ;
    }
}

