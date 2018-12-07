package com.example.chris.nearbykos2;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import control.GPSTracker;

/**
 * Created by christian on 19/06/17.
 */

public class Map extends FragmentActivity implements
        GoogleMap.OnMapClickListener, com.google.android.gms.location.LocationListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mGoogleMap;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    private Marker marker;
    private Geocoder geocoder;
    private LatLng latLng;
    private static final String TAG = "LocationActivity";
    private GPSTracker gps;
    private ImageView ImgSwitchView,ImgZoomIn,ImgZoomOut, ImgLocation;
    private double latitude, longtitude;
    List<Address> addressesAwal;
    private static final long INTERVAL = 1000 * 2 * 1; //2 detik
    private static final long FASTEST_INTERVAL = 1000 * 1 * 1; // 1 detik
    private StringBuilder sb = new StringBuilder();
    private int Tag=1;
    private Button btnOk;
    private TextView txtAlamat;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        ImgSwitchView   = (ImageView) findViewById(R.id.imgMaps);
        ImgZoomIn       = (ImageView) findViewById(R.id.imgZoomIn);
        ImgZoomOut      = (ImageView) findViewById(R.id.imgZoomOut);
        ImgLocation      = (ImageView) findViewById(R.id.imgMyLocation);
        btnOk           = (Button)findViewById(R.id.bOkMap);
        txtAlamat   = (TextView)findViewById(R.id.TvMapDescription);
        geocoder = new Geocoder(Map.this, Locale.getDefault());

        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.putExtra("latitude", latLng.latitude);
                intent.putExtra("longtitude", latLng.longitude);
                intent.putExtra("alamat", sb.toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        ImgLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                gps 	  = new GPSTracker(Map.this);
                double latgpsg  = gps.getLatitude();
                double longgpas = gps.getLongitude();
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latgpsg, longgpas)));
            }
        });

        ImgSwitchView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                switch (Tag) {
                    case 0:
                        ImgSwitchView.setImageResource(R.drawable.ic_lightearth);
                        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        Tag = 1;
                        break;
                    case 1:
                        ImgSwitchView.setImageResource(R.drawable.ic_darkearth);
                        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        Tag = 0;
                        break;
                }
            }
        });

        ImgZoomIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });

        ImgZoomOut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });

        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frgMaps);
        fm.getMapAsync(this);
        /*ImgLocation.performClick();
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        // Showing / hiding your current location
        mGoogleMap.setMyLocationEnabled(true);
        // Enable / Disable zooming controls
        // Enable / Disable my location button
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        // Enable / Disable Compass icon
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        // Enable / Disable Rotate gesture
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);
        // Enable / Disable zooming functionality
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        //Enable / Disable Button Zooming
        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(13), 200, null);

        mGoogleMap.setOnMapClickListener(this);*/
    }

    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            }
        }
        else {
            //buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }
        ImgLocation.performClick();
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        // Showing / hiding your current location
        mGoogleMap.setMyLocationEnabled(true);
        // Enable / Disable zooming controls
        // Enable / Disable my location button
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        // Enable / Disable Compass icon
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        // Enable / Disable Rotate gesture
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);
        // Enable / Disable zooming functionality
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        //Enable / Disable Button Zooming
        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(13), 200, null);
        mGoogleMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng point) {
        latLng = point;
        List<Address> addresses = new ArrayList<>();
        geocoder = new Geocoder(this, Locale.getDefault());
        sb = new StringBuilder();
        try {
            addresses = geocoder.getFromLocation(point.latitude, point.longitude,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String postalCode = addresses.get(0).getPostalCode();
        sb.append(address).append(", ").append(postalCode);
        txtAlamat.setText(sb.toString());
        if (marker != null) {marker.remove();}
        marker = mGoogleMap.addMarker(new MarkerOptions().position(point).title("You are here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mGoogleMap.clear();
        System.gc();
    }

    @Override
    protected void onDestroy() {
        mGoogleMap.clear();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart fired ..............");
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onLocationChanged(Location location) {
        /*Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        gps = new GPSTracker(Map.this);
        latitude = gps.getLatitude();
        longtitude = gps.getLongitude();*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d(TAG, "Location update stopped .......................");
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }
    }*/

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }
}
