package com.example.iiui_project.MapPackage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
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

public class SetMapHouse extends FragmentActivity implements OnMapReadyCallback {

    public static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private GoogleMap mMap;
    private FusedLocationProviderClient mfusedLocationProviderClient;
    private boolean mLocationPermissionGrandted;
    public static final int permissioncode = 1234;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_map_house);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        final String[] Lattitude = new String[1];
        final String[] Longitude = new String[1];
        mMap = googleMap;
        getLocatioPermissionGranted();
        geTDeviseLoaction();


//        getLocation();

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);








        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {
                new AlertDialog.Builder(SetMapHouse.this)
                        .setTitle("Location Add Manually")
                        .setMessage("Click to Add Location Manually")
                        .setNegativeButton("No" , null)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LatLng CurrentLocation = new LatLng(latLng.latitude, latLng.longitude);
                                mMap.addMarker(new MarkerOptions().position(CurrentLocation).title(" Your Current Location ").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                                moveCamera(new LatLng(latLng.latitude, latLng.longitude), 15f);

                                Lattitude[0] = String.valueOf(latLng.latitude);
                                Longitude[0] = String.valueOf(latLng.longitude);

                                Intent i =  new Intent();

                                i.putExtra("Longitude" , Longitude[0]);
                                i.putExtra("Lattitude" , Lattitude[0]);
                                setResult(RESULT_OK , i);

                                finish();

                            }
                        }).show();
            }
        });
    }



    private void geTDeviseLoaction() {
        mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionGrandted ) {

                final Task location = mfusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            if(task.getResult() !=null) {


                                Location location1 = (Location) task.getResult();
                                if(location1 !=null) {

                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "enabling wifi", Toast.LENGTH_LONG).show();

                            }
                        }
                    }

                });
            }
        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    void moveCamera(LatLng latLng, float Zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, Zoom));
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            Toast.makeText(getApplicationContext() , "permission ok" , Toast.LENGTH_LONG ).show();
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if(ContextCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION )!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this ,new String[]{ Manifest.permission.ACCESS_FINE_LOCATION},1);
                }

            }

        }

    }

}
