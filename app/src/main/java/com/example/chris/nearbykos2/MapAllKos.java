package com.example.chris.nearbykos2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

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

/**
 * Created by christian on 20/07/17.
 */

public class MapAllKos extends AppCompatActivity implements OnMapReadyCallback {

    protected LatLng mCenterLocation = new LatLng( 39.7392, -104.9903 );
    private GoogleMap mMap;
    private double latitude, longtitude, latgpsg, longgpas;
    private ClusterManager<StringClusterItem> mClusterManager;
    private String namaKos, telp, idUser, jsonString;
    private String getData	="getListAllKos.php";
    private ImageView ImgSwitchView,ImgZoomIn,ImgZoomOut, ImgLocation, imgBack;
    private TextView tvstatus;
    private ProgressBar prbstatus;
    private GPSTracker gps;
    private int Tag=1;
    NumberFormat rupiah	= NumberFormat.getNumberInstance(new Locale("in", "ID"));
    private ArrayList<ColHomeDetail> columnlist= new ArrayList<ColHomeDetail>();

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
        ImgLocation      = (ImageView) findViewById(R.id.imgMyLocationAllKos);
        imgBack      = (ImageView) findViewById(R.id.ImbMapAllKosBack);
        tvstatus	= (TextView)findViewById(R.id.TvStatusUploadAllKos);
        prbstatus	= (ProgressBar)findViewById(R.id.PrbStatusUploadAllKos);

        final SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frgMapsAllKos);
        mapFragment.getMapAsync(this);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImgLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                gps 	  = new GPSTracker(MapAllKos.this);
                latgpsg  = gps.getLatitude();
                longgpas = gps.getLongitude();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latgpsg, longgpas)));
            }
        });

        ImgSwitchView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
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
                // TODO Auto-generated method stub
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });

        ImgZoomOut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        tvstatus.setVisibility(View.GONE);
        prbstatus.setVisibility(View.GONE);

        mMap=googleMap;

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
                        //Toast.makeText(MapAllKos.this, "Cluster item click", Toast.LENGTH_SHORT).show();
                        // if true, click handling stops here and do not show info view, do not move camera
                        // you can avoid this by calling:
                        renderer.getMarker(clusterItem).showInfoWindow();
                        return false;
                    }
                });

        mClusterManager.getMarkerCollection()
                .setOnInfoWindowAdapter(new CustomInfoViewAdapter(LayoutInflater.from(this)));

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
                        startActivity(i);
                    }
                });

        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(mClusterManager);
        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        mMap.setOnMarkerClickListener(mClusterManager);
        ImgLocation.performClick();
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        for(ColHomeDetail entity: columnlist){
            mClusterManager.addItem(new StringClusterItem(entity.getNamaKos()+" \nTlp: "+entity.getTlpCust(),
                    entity.getLatitude(), entity.getLongtitude(), entity));
        }
        mClusterManager.cluster();
    }

    private void initMarkers() {
        ClusterManager<ClusterMarkerLocation> clusterManager = new ClusterManager<ClusterMarkerLocation>( this, mMap );
        mMap.setOnCameraChangeListener(clusterManager);
        double lat;
        double lng;
        Random generator = new Random();
        for( int i = 0; i < 1000; i++ ) {
            lat = generator.nextDouble() / 3;
            lng = generator.nextDouble() / 3;
            if( generator.nextBoolean() ) {
                lat = -lat;
            }
            if( generator.nextBoolean() ) {
                lng = -lng;
            }
            clusterManager.addItem( new ClusterMarkerLocation( new LatLng( mCenterLocation.latitude + lat,
                    mCenterLocation.longitude + lng ) ) );
        }
    }

    public static class StringClusterItem implements ClusterItem {

        private String title;
        private LatLng position;
        private ColHomeDetail entity;

        public StringClusterItem(double lat, double lng) {
            this.position = new LatLng(lat, lng);
        }

        public StringClusterItem(String title, double lat, double lng, ColHomeDetail column) {
            this.title = title;
            this.position = new LatLng(lat, lng);;
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
