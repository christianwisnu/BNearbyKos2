package com.example.chris.nearbykos2;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import control.CustomClusterRenderer;
import control.CustomInfoViewAdapter;
import control.GPSTracker;
import model.ClusterMarkerLocation;
import model.ColHomeDetail;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * Created by christian on 20/07/17.
 */

public class MapAllKos extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener{

    protected LatLng mCenterLocation = new LatLng( 39.7392, -104.9903 );
    private GoogleMap mMap;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private double latitude, longtitude, latgpsg, longgpas;
    private ClusterManager<StringClusterItem> mClusterManager;
    private String namaKos, telp, idUser, jsonString;
    private String getData	="getListAllKos.php";
    private ImageView ImgSwitchView,ImgZoomIn,ImgZoomOut, imgBack;
    private TextView tvstatus;
    private ProgressBar prbstatus;
    private GPSTracker gps;
    private int Tag=1;
    LocationRequest mLocationRequest;
    NumberFormat rupiah	= NumberFormat.getNumberInstance(new Locale("in", "ID"));
    private ArrayList<ColHomeDetail> columnlist= new ArrayList<ColHomeDetail>();
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final long INTERVAL = 1000 * 2 * 1; //2 detik
    private static final long FASTEST_INTERVAL = 1000 * 1 * 1; // 1 detik
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gson gson = new Gson();
        setContentView(R.layout.map_all_kos);
        Bundle i = getIntent().getExtras();
        if (i != null){
            try {
                idUser = i.getString("idUser");
                jsonString = i.getString("list");
            } catch (Exception e) {
                e.getMessage();
            }
        }
        Type listColHomeDetail = new TypeToken<List<ColHomeDetail>>() {}.getType();
        columnlist = gson.fromJson(jsonString, listColHomeDetail );
        ImgSwitchView   = (ImageView) findViewById(R.id.imgMapsAllKos);
        ImgZoomIn       = (ImageView) findViewById(R.id.imgZoomInAllKos);
        ImgZoomOut      = (ImageView) findViewById(R.id.imgZoomOutAllKos);
        //ImgLocation      = (ImageView) findViewById(R.id.imgMyLocationAllKos);
        imgBack      = (ImageView) findViewById(R.id.ImbMapAllKosBack);
        tvstatus	= (TextView)findViewById(R.id.TvStatusUploadAllKos);
        prbstatus	= (ProgressBar)findViewById(R.id.PrbStatusUploadAllKos);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frgMapsAllKos);
        mapFragment.getMapAsync(this);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        ImgSwitchView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (Tag) {
                    case 0:
                        ImgSwitchView.setImageResource(R.drawable.ic_lightearth);
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        Tag = 1;
                        break;
                    case 1:
                        ImgSwitchView.setImageResource(R.drawable.ic_darkearth);
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        Tag = 0;
                        break;
                }
            }
        });

        ImgZoomIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });

        ImgZoomOut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onConnected( Bundle bundle) {
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
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed( ConnectionResult connectionResult) {

    }

    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
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
    protected void onResume() {
        super.onResume();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        tvstatus.setVisibility(View.GONE);
        prbstatus.setVisibility(View.GONE);
        mMap=googleMap;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            //buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        /*ImgLocation.performClick();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        // Showing / hiding your current location
        mMap.setMyLocationEnabled(true);
        // Enable / Disable zooming controls
        // Enable / Disable my location button
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        // Enable / Disable Compass icon
        mMap.getUiSettings().setCompassEnabled(true);
        // Enable / Disable Rotate gesture
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        // Enable / Disable zooming functionality
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        //Enable / Disable Button Zooming
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13), 200, null);
        createLocationRequest();*/

        mClusterManager = new ClusterManager<StringClusterItem>(this, mMap);

        final CustomClusterRenderer renderer = new CustomClusterRenderer(this, mMap, mClusterManager);

        mClusterManager.setRenderer(renderer);

        mClusterManager.setOnClusterClickListener(
                new ClusterManager.OnClusterClickListener<StringClusterItem>() {

                    @Override
                    public boolean onClusterClick(Cluster<StringClusterItem> cluster) {
                        LatLngBounds.Builder builder = LatLngBounds.builder();
                        for (ClusterItem item : cluster.getItems()) {
                            builder.include(item.getPosition());
                        }
                        // Get the LatLngBounds
                        final LatLngBounds bounds = builder.build();

                        // Animate camera to the bounds
                        try {
                            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return true;
                    }
                });

        mClusterManager.setOnClusterItemClickListener(
                new ClusterManager.OnClusterItemClickListener<StringClusterItem>() {

                    @Override
                    public boolean onClusterItemClick(StringClusterItem clusterItem) {
                        renderer.getMarker(clusterItem).showInfoWindow();
                        return false;
                    }
                });

        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(
                new CustomInfoViewAdapter(LayoutInflater.from(this))
        );

        mClusterManager.setOnClusterItemInfoWindowClickListener(
                new ClusterManager.OnClusterItemInfoWindowClickListener<StringClusterItem>() {

                    @Override
                    public void onClusterItemInfoWindowClick(StringClusterItem stringClusterItem) {
                        Intent i = new Intent(MapAllKos.this, InfoKos.class);
                        i.putExtra("id_kos", stringClusterItem.getEntity().getId_kos());
                        i.putExtra("idUser", idUser);
                        i.putExtra("i_idcust", stringClusterItem.getEntity().getId_cust());
                        i.putExtra("namaCust", stringClusterItem.getEntity().getNamaCust());
                        i.putExtra("i_jmlkamar", stringClusterItem.getEntity().getJmlhKamar());
                        i.putExtra("vc_alamat", stringClusterItem.getEntity().getAlamat());
                        i.putExtra("vc_namakost", stringClusterItem.getEntity().getNamaKos());
                        i.putExtra("i_lebar", stringClusterItem.getEntity().getLebar());
                        i.putExtra("i_panjang", stringClusterItem.getEntity().getPanjang());
                        i.putExtra("c_statuslistrik", stringClusterItem.getEntity().getStatusListrik().equals("Y")?"Include":"Exclude");
                        i.putExtra("i_idsewastatus", stringClusterItem.getEntity().getId_sewaStatus()==1?"Man Only":
                                stringClusterItem.getEntity().getId_sewaStatus()==2?"Women Only":"All");
                        i.putExtra("tlpCust", stringClusterItem.getEntity().getTlpCust());
                        i.putExtra("fasilitas", stringClusterItem.getEntity().getFasilitas());
                        i.putExtra("lat", stringClusterItem.getEntity().getLatitude());
                        i.putExtra("longt", stringClusterItem.getEntity().getLongtitude());
                        i.putExtra("harga", "Rp. "+rupiah.format(stringClusterItem.getEntity().getHarga()));
                        i.putExtra("hargaAsli", stringClusterItem.getEntity().getHarga());
                        i.putExtra("gambar", stringClusterItem.getEntity().getGambar());
                        i.putExtra("gambar2", stringClusterItem.getEntity().getGambar2());
                        i.putExtra("gambar3", stringClusterItem.getEntity().getGambar3());
                        i.putExtra("gambar4", stringClusterItem.getEntity().getGambar4());
                        i.putExtra("gambar5", stringClusterItem.getEntity().getGambar5());
                        i.putExtra("c_kodekota", stringClusterItem.getEntity().getKodeKota());
                        i.putExtra("i_sisa", stringClusterItem.getEntity().getSisa());
                        startActivity(i);
                    }
                });

        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(mClusterManager);
        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        mMap.setOnMarkerClickListener(mClusterManager);
        //ImgLocation.performClick();
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        for(ColHomeDetail entity: columnlist){
            mClusterManager.addItem(new StringClusterItem(entity.getNamaKos()+" \nTlp: "+entity.getTlpCust()+
                    "\nSisa kamar: "+entity.getSisa(),
                    entity.getLatitude(), entity.getLongtitude(), entity));
        }
        mClusterManager.cluster();
    }

    public static class StringClusterItem implements ClusterItem {

        private String title;
        private LatLng position;
        private ColHomeDetail entity;

        public StringClusterItem(String title, double lat, double lng, ColHomeDetail column) {
            this.title = title;
            this.position = new LatLng(lat, lng);
            this.entity = column;
        }

        @Override
        public LatLng getPosition() {
            return position;
        }

        public String getTitle() {
            return title;
        }

        public ColHomeDetail getEntity(){
            return entity;
        }
    }
}
