package com.example.iiui_project.MapPackage;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.iiui_project.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback , GoogleApiClient.OnConnectionFailedListener {

    final String[] Lattitude = new String[1];
    final String[] Longitude = new String[1];
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private GoogleMap mMap;


    public static final String TAG = "ErRRRRRRRRRRRRRRRRRRRRRRr" ;
    private FusedLocationProviderClient mfusedLocationProviderClient;
    public static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final int permissioncode = 1234;
public static final LatLngBounds  latB = new LatLngBounds(new LatLng(-40 , -168), new LatLng(71 , 36) );

    private AutoCompleteTextView mSearchView ;
    DatabaseReference databaseReference ;
    private boolean mLocationPermissionGrandted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        getLocatioPermissionGranted();

;        mSearchView = findViewById(R.id.searchfor);
                init();

        // Retrieve the AutoCompleteTextView that will display Place suggestions.


        // Register a listener that receives callbacks when a suggestion has been selected

        // Retrieve the TextViews that will display details and attributions of the selected place.

        // Set up the adapter that will retrieve suggestions from the Places Geo Data Client.

    }

    private void init() {




        mSearchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {

                if(actionId  == EditorInfo.IME_ACTION_SEARCH || actionId  == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER
                )
                {
                    Toast.makeText(MapsActivity.this, "create view", Toast.LENGTH_SHORT).show();

                    getoLocate();
                }

                return false;
            }
        });
        keyboardHide();
    }

    private void getoLocate() {

        String searchvar = mSearchView.getText().toString();
        Geocoder geocoder =new Geocoder(MapsActivity.this);;
        List<Address> addresses = new ArrayList<>();

        try {
            addresses = geocoder.getFromLocationName(searchvar  , 1);
        } catch (IOException e) {
            e.printStackTrace();
        }


        if(addresses.size()>0){
            Address address = addresses.get(0);
            Toast.makeText(this, "" + address.toString(), Toast.LENGTH_SHORT).show();
            LatLng latLng = new LatLng(address.getLatitude() , address.getLongitude());
            moveCamera(latLng , 16f , (address.getCountryName()  + " " + address.getAddressLine(1)));

        }
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

        getLocatioPermissionGranted();
        // Add a marker in Sydney and move the camera
        geTDeviseLoaction();
        mMap.setMyLocationEnabled(true);

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);



        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {
                new AlertDialog.Builder(MapsActivity.this)
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



    void moveCamera(LatLng latLng, float Zoom , String title) {

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, Zoom));

        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in " + title ));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    void  keyboardHide(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

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
                            Location location1 = (Location) task.getResult();

                            try {
                                LatLng CurrentLocation = new LatLng(location1.getLatitude(), location1.getLongitude());
                                mMap.addMarker(new MarkerOptions().position(CurrentLocation).title(" Your Current Location ").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));

                            }catch (Exception e){
                                Toast.makeText(getApplicationContext() , "enabling gps" , Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }

        } catch (SecurityException e) {
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
    void moveCamera(LatLng latLng, float Zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, Zoom));
    }



}

