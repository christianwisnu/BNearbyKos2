package com.example.chris.nearbykos2;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.StringRequest;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TreeMap;

import control.AppController;
import control.Link;

/**
 * Created by Chris on 21/06/2017.
 */

public class InfoKos extends AppCompatActivity implements OnMapReadyCallback,
        BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private GoogleMap mGoogleMap;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    private ImageView imgBack;
    private TextView txtJudul, txtNama, txtHarga, txtId, txtJmlh, txtPnjgLbr, txtListrik, txtOcupant, txtKontak,
        txtTelp, txtFasilitas, txtAlamat, telefon, sms, txtSisa, txtCekin;
    private String namaKos, listrik, ocupant, namaCust, telp, fasilitas, alamat, harga, slasid, idUser,
    gambar1, gambar2, gambar3, gambar4, gambar5;
    private Integer idKos, jmlh, panjang, lebar, idCust, hargaAsli;
    private double lat, longt;
    private Button btnBooking, btnCekin;
    private static final long INTERVAL = 1000 * 2 * 1; //2 detik
    private static final long FASTEST_INTERVAL = 1000 * 1 * 1; // 1 detik
    private float zoomLevel = (float) 16.0;
    private String addBooking	="addBooking.php";
    private String cekBooking = "getCountBookingUser.php";
    private String cekBookingAktif = "getCountBookingUserAktif.php";
    private ProgressDialog dialog;
    private Integer countId, sisa;
    private SliderLayout sliderLayout;
    private TreeMap<Integer,String> treeMap;
    private Integer a=0;
    private SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
    private Calendar dateAndTime = Calendar.getInstance();
    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_kos);
        Bundle i = getIntent().getExtras();
        if (i != null){
            try {
                idKos = i.getInt("id_kos");
                idUser = i.getString("idUser");
                idCust = i.getInt("i_idcust");
                namaCust = i.getString("namaCust");
                jmlh = i.getInt("i_jmlkamar");
                alamat = i.getString("vc_alamat");
                namaKos = i.getString("vc_namakost");
                lebar = i.getInt("i_lebar");
                panjang = i.getInt("i_panjang");
                listrik = i.getString("c_statuslistrik");
                ocupant = i.getString("i_idsewastatus");
                telp = i.getString("tlpCust");
                fasilitas = i.getString("fasilitas");
                lat = i.getDouble("lat");
                longt = i.getDouble("longt");
                harga = i.getString("harga");
                hargaAsli = i.getInt("hargaAsli");
                gambar1 = i.getString("gambar");
                gambar2 = i.getString("gambar2");
                gambar3 = i.getString("gambar3");
                gambar4 = i.getString("gambar4");
                gambar5 = i.getString("gambar5");
                sisa = i.getInt("i_sisa");
            } catch (Exception e) {}
        }

        treeMap = new TreeMap<Integer,String>();
        txtJudul		= (TextView)findViewById(R.id.TvTittleInfoKos);
        txtNama		= (TextView)findViewById(R.id.txtNamaInfoKos);
        txtHarga		= (TextView)findViewById(R.id.txtHargaInfoKos);
        txtId		= (TextView)findViewById(R.id.txtIdInfoKos);
        txtJmlh		= (TextView)findViewById(R.id.txtJmlKamarInfoKos);
        txtPnjgLbr		= (TextView)findViewById(R.id.txtPnjgLbrInfoKos);
        txtListrik		= (TextView)findViewById(R.id.txtListrikInfoKos);
        txtOcupant		= (TextView)findViewById(R.id.txtSewaInfoKos);
        txtKontak		= (TextView)findViewById(R.id.txtKontakInfoKos);
        txtTelp		= (TextView)findViewById(R.id.txtTelfInfoKos);
        txtFasilitas		= (TextView)findViewById(R.id.txtFasilitasInfoKos);
        txtAlamat = (TextView)findViewById(R.id.txtAlamatInfoKos);
        txtSisa = (TextView)findViewById(R.id.txtSisaKamarInfoKos);
        telefon = (TextView)findViewById(R.id.txtTelp);
        sms = (TextView)findViewById(R.id.txtSms);
        btnBooking = (Button)findViewById(R.id.btnBookingInfoKos);
        imgBack = (ImageView)findViewById(R.id.ImbBackInfoKos);
        sliderLayout = (SliderLayout) findViewById(R.id.slider);

        txtJudul.setText(namaKos);
        txtNama.setText(namaKos);
        txtId.setText("Id Kos: "+idKos);
        txtHarga.setText("Harga Rp. "+harga);
        txtJmlh.setText("Jumlah Kamar: "+String.valueOf(jmlh));
        txtPnjgLbr.setText("Ukuran Kamar: "+String.valueOf(panjang)+"X"+String.valueOf(lebar));
        txtListrik.setText("Include Listrik: "+listrik);
        txtOcupant.setText("Occupant: "+ ocupant);
        txtKontak.setText("Contact Person: "+namaCust);
        txtTelp.setText("Phone: "+telp);
        txtFasilitas.setText("Fasilitas: "+fasilitas);
        txtAlamat.setText(alamat);
        txtSisa.setText("Sisa Kamar: "+String.valueOf(sisa));

        treeMap.put(1, gambar1==null||gambar1.equals("null")||gambar1.equals("")?"":Link.FileImage+gambar1);
        treeMap.put(2, gambar2==null||gambar2.equals("null")||gambar2.equals("")?"":Link.FileImage+gambar2);
        treeMap.put(3, gambar3==null||gambar3.equals("null")||gambar3.equals("")?"":Link.FileImage+gambar3);
        treeMap.put(4, gambar4==null||gambar4.equals("null")||gambar4.equals("")?"":Link.FileImage+gambar4);
        treeMap.put(5, gambar5==null||gambar5.equals("null")||gambar5.equals("")?"":Link.FileImage+gambar5);

        for(Integer name : treeMap.keySet()){
            String gambar=treeMap.get(name);
            TextSliderView textSliderView = new TextSliderView(InfoKos.this);
            if(gambar.equals("")){
                textSliderView
                        .description("Gambar "+String.valueOf(name))
                        .image(R.drawable.no_image)
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(this);
            }else{
                textSliderView
                        .description("Gambar "+String.valueOf(name))
                        .image(gambar)
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(this);
            }

            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra","Gambar "+String.valueOf(name));
            sliderLayout.addSlider(textSliderView);
            a++;
        }

        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(6000);
        sliderLayout.addOnPageChangeListener(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(AppIndex.API).build();
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frgMapsInfoKos);
        fm.getMapAsync(this);
        mGoogleMap = fm.getMap();
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
        createLocationRequest();

        telefon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_VIEW);
                callIntent.setData(Uri.parse("tel:"+telp));
                startActivity(callIntent);
            }
        });

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address",telp);
                startActivity(smsIntent);
            }
        });

        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sisa==0){
                    Toast.makeText(getApplicationContext(),
                            "Kamar kos sudah penuh!", Toast.LENGTH_LONG)
                            .show();
                }else{
                    save();
                }
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void save(){
        dialog = new ProgressDialog(InfoKos.this);
        dialog.setCancelable(true);
        dialog.setMessage("Loading ...\nPlease Wait!");
        dialog.show();
        GetCountData(Link.FilePHP+cekBooking);
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        /*Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://net.simplifiedcoding.googlemapsdistancecalc/http/host/path")
        );
        AppIndex.AppIndexApi.start(mGoogleApiClient, viewAction);*/
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        sliderLayout.stopAutoCycle();
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        /*Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://net.simplifiedcoding.googlemapsdistancecalc/http/host/path")
        );
        AppIndex.AppIndexApi.end(mGoogleApiClient, viewAction);*/
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;
        LatLng latLng = new LatLng(lat, longt);
        mGoogleMap.addMarker(new MarkerOptions().position(latLng)
                .title(namaKos)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,200));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
        //mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel), 200, null);
    }

    private void GetCountData(String Url){
        JsonObjectRequest jsonget = new JsonObjectRequest(Request.Method.GET, Url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // TODO Auto-generated method stub
                        try {
                            int sucses= response.getInt("success");
                            Log.i("Status", String.valueOf(sucses));
                            if (sucses==1){
                                JSONArray JsonArray = response.getJSONArray("uploade");
                                JSONObject object = JsonArray.getJSONObject(0);
                                countId = object.getInt("count");
                                if(countId>0){
                                    Toast.makeText(getApplicationContext(),
                                            "Kos sedang proses booking. \nHarap hubungi pemilik kos untuk konfirmasi lebih lanjut.", Toast.LENGTH_LONG)
                                            .show();
                                    dialog.hide();
                                }else{
                                    GetCountDataAktif(Link.FilePHP+cekBookingAktif);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, null){

            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new HashMap<String, String>();
                params.put("idKos", String.valueOf(idKos));
                params.put("idCust", String.valueOf(idCust));
                params.put("idUser", idUser);
                return params;
            }

            @Override
            public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                java.util.Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        AppController.getInstance().getRequestQueue().getCache().invalidate(Url, true);
        AppController.getInstance().addToRequestQueue(jsonget);
    }

    private void GetCountDataAktif(String Url){
        JsonObjectRequest jsonget = new JsonObjectRequest(Request.Method.GET, Url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // TODO Auto-generated method stub
                        try {
                            int sucses= response.getInt("success");
                            Log.i("Status", String.valueOf(sucses));
                            if (sucses==1){
                                JSONArray JsonArray = response.getJSONArray("uploade");
                                JSONObject object = JsonArray.getJSONObject(0);
                                countId = object.getInt("count");
                                if(countId>0){
                                    Toast.makeText(getApplicationContext(),
                                            "Anda sekarang sedang dalam kos ini", Toast.LENGTH_LONG)
                                            .show();
                                }else{
                                    dialogBox();
                                }
                                dialog.hide();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, null){

            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new HashMap<String, String>();
                params.put("idKos", String.valueOf(idKos));
                params.put("idCust", String.valueOf(idCust));
                params.put("idUser", idUser);
                return params;
            }

            @Override
            public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                java.util.Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        AppController.getInstance().getRequestQueue().getCache().invalidate(Url, true);
        AppController.getInstance().addToRequestQueue(jsonget);
    }

    private void dialogBox(){
        LayoutInflater li = LayoutInflater.from(InfoKos.this);
        View promptsView = li.inflate(R.layout.cek_in_booking, null);
        //btnCekin = (Button) promptsView.findViewById(R.id.btnTglCekIn);
        txtCekin = (TextView) promptsView.findViewById(R.id.eTglCekIn);
        txtCekin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingTanggalFrom();
            }
        });
        AlertDialog.Builder msMaintance = new AlertDialog.Builder(InfoKos.this);
        msMaintance.setView(promptsView);
        msMaintance.setCancelable(false);
        msMaintance.setNegativeButton("Book Now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(txtCekin.getText().toString().equals("")){
                    Toast.makeText(InfoKos.this,
                            "Tanggal cek-in harap diisi", Toast.LENGTH_LONG)
                            .show();
                }else{
                    addBooking(Link.FilePHP+addBooking);
                }
            }
        });
        msMaintance.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alert.dismiss();
            }
        });
        alert = msMaintance.create();
        alert.show();
    }

    private void addBooking(String save){
        dialog.show();
        StringRequest register = new StringRequest(Request.Method.POST, save, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                VolleyLog.d("Respone", response.toString());
                try {
                    JSONObject jsonrespon = new JSONObject(response);
                    int Sucsess = jsonrespon.getInt("success");
                    slasid		=String.valueOf(Sucsess);
                    Log.i("Suceses", String.valueOf(Sucsess));
                    if (Sucsess > 0 ){
                        Toast.makeText(InfoKos.this,
                                "Data berhasil disimpan", Toast.LENGTH_LONG)
                                .show();
                        alert.dismiss();
                        finish();
                    }else{
                        Toast.makeText(InfoKos.this,
                                "Gagal Coba Lagi", Toast.LENGTH_LONG)
                                .show();
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.d(TAG, error.toString());
            }
        }){
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new HashMap<String, String>();
                params.put("idKos", String.valueOf(idKos));
                params.put("idCust", String.valueOf(idCust));
                params.put("idUser", idUser);
                params.put("harga", String.valueOf(hargaAsli));
                params.put("cekIn", txtCekin.getText().toString()+" 00:00:00");
                return params;
            }
            @Override
            public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                java.util.Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(register);
    }

    DatePickerDialog.OnDateSetListener dFrom =new DatePickerDialog.OnDateSetListener(){

        @Override

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // TODO Auto-generated method stub
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, month);
            dateAndTime.set(Calendar.DAY_OF_MONTH, day);
            updatelabelFrom();
        }
    };

    private void updatelabelFrom(){
        txtCekin.setText(df1.format(dateAndTime.getTime()));
    }

    private void settingTanggalFrom() {
        // TODO Auto-generated method stub
        new DatePickerDialog(InfoKos.this, dFrom, dateAndTime.get(Calendar.YEAR),dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH)).show();
    }
}
