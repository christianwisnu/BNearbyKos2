package com.example.chris.nearbykos2;

import android.app.Activity;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import adapter.AdpKota;
import control.AppController;
import control.CheckValidation;
import control.Link;
import control.Utils;
import model.ColFasilitas;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by christian on 13/06/17.
 */

public class AddHomeKos extends Activity{

    private TextInputLayout inputLayoutNamaKos, inputLayoutHarga, inputLayoutTotalKamar, inputLayoutLebarKamar,
            inputLayoutPanjangKamar;
    private EditText eNamaKos, eAlamat, eharga, eTotalKamar, eLebar, ePanjang, eFoto, eFasilitas;
    private Spinner spPeruntukan, spListrik;
    private Button btnMap, btnAddFoto, btnSave, btnFasilitas;
    private ImageView imgFoto2, imgFoto3, imgFoto4, imgFoto5, imgBack;
    private String pathFoto2, pathFoto3, pathFoto4, pathFoto5, alamat, simage1, encodedString, convertedPath, slasid,
            simage2, simage3, simage4, simage5, daftarKodeFasilitas, daftarNamaFasilitas;
    private static int RESULT_LOAD_IMG = 1;
    private double latitude, longtitude;
    private ProgressDialog dialog;
    private String addKost	="addKost.php";
    private String editKost	="editKos.php";
    //private String addListKost	="addListKost.php";
    private String cekNo = "getMaxNumber.php";
    private String idCust, namaKos, alamatEdit, gambar, statListrik, idSewa,
            gambar2, gambar3, gambar4, gambar5;
    private Integer status, noTrans, idKos, harga, panjang, lebar, jmlhKamar, sisa;
    private Bitmap bitmap;
    private String url 		= Link.FileImage;
    CharSequence[] items = null;
    List<CharSequence> charSequences = new ArrayList<>();
    ArrayList<ColFasilitas> seletedItems=new ArrayList();
    private ArrayList<ColFasilitas> mArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addhomekos);
        Bundle i = getIntent().getExtras();
        if (i != null){
            try {
                status = i.getInt("Status");
                idCust = i.getString("idCust");
                idKos = i.getInt("id_kos");
                idSewa = i.getString("i_idsewastatus");
                namaKos = i.getString("vc_namakost");
                alamatEdit = i.getString("vc_alamat");
                gambar = i.getString("vc_gambar");
                gambar2 = i.getString("vc_gambar2");
                gambar3 = i.getString("vc_gambar3");
                gambar4 = i.getString("vc_gambar4");
                gambar5 = i.getString("vc_gambar5");
                panjang=  i.getInt("i_panjang");
                lebar = i.getInt("i_lebar");
                statListrik = i.getString("c_statuslistrik");
                jmlhKamar = i.getInt("i_jmlkamar");
                daftarKodeFasilitas = i.getString("t_fasilitas");
                latitude = i.getDouble("d_latitude");
                longtitude = i.getDouble("d_longtitude");
                harga = i.getInt("n_harga");
                //sisa = i.getInt("i_sisa");
                //kodeKota = i.getString("c_kodekota");
                //namaKota = i.getString("vc_namakota");
            } catch (Exception e) {}
        }

        if(status==3){
            finish();
        }
        inputLayoutNamaKos = (TextInputLayout)findViewById(R.id.input_layout_namakos);
        inputLayoutHarga = (TextInputLayout)findViewById(R.id.input_layout_harga);
        inputLayoutTotalKamar = (TextInputLayout)findViewById(R.id.input_layout_jmlkamar);
        inputLayoutLebarKamar = (TextInputLayout)findViewById(R.id.input_layout_lebarkamar);
        inputLayoutPanjangKamar = (TextInputLayout)findViewById(R.id.input_layout_panjangkamar);

        eNamaKos		= (EditText)findViewById(R.id.eAddKosNamaKos);
        eharga  		= (EditText)findViewById(R.id.eAddKosHargaKos);
        eTotalKamar     = (EditText)findViewById(R.id.eAddKosJmlKamarKos);
        eLebar          = (EditText)findViewById(R.id.eAddKosLebarKamarKos);
        ePanjang        = (EditText)findViewById(R.id.eAddKosPanjangKamarKos);
        spListrik       = (Spinner) findViewById(R.id.spAddKosStatusListrik);
        spPeruntukan    = (Spinner) findViewById(R.id.spAddPeruntukanKos);
        eAlamat         = (EditText)findViewById(R.id.eAddKosAlamatKos);
        eFoto           = (EditText)findViewById(R.id.eAddKosPathFotoKos);
        eFasilitas      = (EditText)findViewById(R.id.eAddKosFasilitasKos);
        //eKota           = (EditText)findViewById(R.id.eAddKosKotaKos);

        btnMap          = (Button)findViewById(R.id.bAddKosMap);
        btnAddFoto      = (Button)findViewById(R.id.bAddFotoKos);
        btnSave         = (Button)findViewById(R.id.bSaveAddKos);
        //btnKota         = (Button)findViewById(R.id.bAddKosKota);
        btnFasilitas    = (Button)findViewById(R.id.bAddKosFasilitas);

        imgFoto2        = (ImageView)findViewById(R.id.imgAddKosFoto2);
        imgFoto3        = (ImageView)findViewById(R.id.imgAddKosFoto3);
        imgFoto4        = (ImageView)findViewById(R.id.imgAddKosFoto4);
        imgFoto5        = (ImageView)findViewById(R.id.imgAddKosFoto5);
        imgBack         = (ImageView)findViewById(R.id.ImbAddKosBack);

        eNamaKos.addTextChangedListener(new MyTextWatcher(eNamaKos));
        eharga.addTextChangedListener(new MyTextWatcher(eharga));
        eTotalKamar.addTextChangedListener(new MyTextWatcher(eTotalKamar));
        eLebar.addTextChangedListener(new MyTextWatcher(eLebar));
        ePanjang.addTextChangedListener(new MyTextWatcher(ePanjang));

        loadJSON();
        eAlamat.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                CheckValidation.hasText(eAlamat, getResources().getString(R.string.err_msg_alamatkos));
            }
        });

        eFoto.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                CheckValidation.hasText(eFoto, getResources().getString(R.string.err_msg_fotokos));
            }
        });

        /*eKota.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                CheckValidation.hasText(eKota, getResources().getString(R.string.err_msg_kotakos));
            }
        });*/

        if(status==2){
            eNamaKos.setText(namaKos);
            eharga.setText(String.valueOf(harga));
            eTotalKamar.setText(String.valueOf(jmlhKamar));
            eLebar.setText(String.valueOf(lebar));
            ePanjang.setText(String.valueOf(panjang));
            eAlamat.setText(alamatEdit);
            eFasilitas.setText(daftarKodeFasilitas);
            eFoto.setText(gambar);

            if(statListrik.equals("Y"))
                spListrik.setSelection(0);
            else
                spListrik.setSelection(1);

            if(idSewa.equals("1"))
                spPeruntukan.setSelection(0);
            else if(idSewa.equals("2"))
                spPeruntukan.setSelection(1);
            else
                spPeruntukan.setSelection(2);

            if(gambar==null || gambar.equals("") || gambar.equals("null") ){
                simage1=null;
            }else{
                simage1=gambar;
            }
            if(gambar2==null || gambar2.equals("") || gambar2.equals("null") ){
                imgFoto2.setImageResource(R.drawable.no_image);
                simage2=null;
            }else{
                Utils.GetImage(url+gambar2, imgFoto2, AddHomeKos.this);
                imgFoto2.setTag(url);
                simage2=gambar2;
            }
            if(gambar3==null || gambar3.equals("") || gambar3.equals("null") ){
                imgFoto3.setImageResource(R.drawable.no_image);
                simage3=null;
            }else{
                Utils.GetImage(url+gambar3, imgFoto3, AddHomeKos.this);
                imgFoto3.setTag(url);
                simage3=gambar3;
            }
            if(gambar4==null || gambar4.equals("") || gambar4.equals("null")){
                imgFoto4.setImageResource(R.drawable.no_image);
                simage4=null;
            }else{
                Utils.GetImage(url+gambar4, imgFoto4, AddHomeKos.this);
                imgFoto4.setTag(url);
                simage4=gambar4;
            }
            if(gambar5==null || gambar5.equals("") || gambar5.equals("null")){
                imgFoto5.setImageResource(R.drawable.no_image);
                simage5=null;
            }else{
                Utils.GetImage(url+gambar5, imgFoto5, AddHomeKos.this);
                imgFoto5.setTag(url);
                simage5=gambar5;
            }
        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAddFoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });

        imgFoto2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 2);
            }
        });

        imgFoto3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 3);
            }
        });

        imgFoto4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 4);
            }
        });

        imgFoto5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 5);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(validation()){
                    dialog = new ProgressDialog(AddHomeKos.this);
                    dialog.setCancelable(true);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.setMessage("Proses Menyimpan ...");
                    //tipeLogin = String.valueOf(spjurusan.getSelectedItem());
                    if (status==1){
                        getMaxNumber(Link.FilePHP+cekNo);
                    }else if (status==2){
                        editData(Link.FilePHP + editKost);
                    }
                }
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplication(), Map.class);
                startActivityForResult(i, 6);
            }
        });

        /*btnKota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddHomeKos.this, SearchListKota.class);
                startActivityForResult(i,7);
            }
        });*/

        btnFasilitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (status==1){//ADD

                }else if (status==2){//EDIT
                    String fasilitas = eFasilitas.getText().toString();
                    String[] split = fasilitas.split(",");
                    for(CharSequence cs : items){
                        int x = 0;
                        String data = cs.toString();
                        String[] a = data.split("\\|");
                        for(String b : split){
                            if(a[0].equals(b)){
                                x++;
                                break;
                            }
                        }
                        if(x==0){
                            pilihan.add(false);
                        }else if(x==1){
                            pilihan.add(true);
                        }
                    }
                    listCentang = pilihan.toArray(new Boolean[pilihan.size()]);
                }*/

                seletedItems = new ArrayList<ColFasilitas>();
                AlertDialog dialog = new AlertDialog.Builder(AddHomeKos.this)
                        .setTitle("Pilih Fasilitas")
                        .setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    seletedItems.add(mArrayList.get(indexSelected));
                                } else if (seletedItems.contains(indexSelected)) {
                                    // Else, if the item is already in the array, remove it
                                    for(ColFasilitas entity:seletedItems){
                                        ColFasilitas klik = mArrayList.get(indexSelected);
                                        if(entity.getKodeFasilitas().equals(klik.getKodeFasilitas())){
                                            seletedItems.remove(entity);
                                            break;
                                        }
                                    }
                                }
                            }
                        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                eFasilitas.setText("");
                                if(!seletedItems.isEmpty()){
                                    daftarKodeFasilitas=null;
                                    daftarNamaFasilitas=null;
                                    StringBuilder sbKode = new StringBuilder();
                                    StringBuilder sbNama = new StringBuilder();
                                    for(ColFasilitas entity : seletedItems){
                                        sbKode.append(entity.getKodeFasilitas()).append(",");
                                        sbNama.append(entity.getNamaFasilitas()).append(",");
                                    }
                                    daftarKodeFasilitas=sbKode.toString().substring(0, sbKode.toString().length() -1);
                                    daftarNamaFasilitas=sbNama.toString().substring(0, sbNama.toString().length() -1);
                                    eFasilitas.setText(daftarNamaFasilitas);
                                }
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //  Your code when user clicked on Cancel
                            }
                        }).create();
                dialog.show();
            }
        });
    }

    private void loadJSON(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Link.FilePHP2)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponse> call = request.getFasilitas();
        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                JSONResponse jsonResponse = response.body();
                mArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getFasilitas()));
                for(ColFasilitas entity : mArrayList){
                    charSequences.add(new String("("+entity.getKodeFasilitas()+") "+entity.getNamaFasilitas()));
                }
                items = charSequences.toArray(new CharSequence[charSequences.size()]);
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void editData(String save){
        StringRequest simpan = new StringRequest(Request.Method.POST, save,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        //VolleyLog.d(TAG, response.toString());
                        try {
                            JSONObject jsonrespon = new JSONObject(response);
                            int Sucsess = jsonrespon.getInt("success");
                            slasid		=String.valueOf(Sucsess);
                            //Log.i(TAG, String.valueOf(Sucsess));
                            if (Sucsess >0 ){
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(),
                                        "Edit Jenis Barang gagal!", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                //VolleyLog.d(TAG, error.toString());
            }
        }){
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new HashMap<String, String>();
                String statusSewa = String.valueOf(spPeruntukan.getSelectedItem());
                String statusListrik = String.valueOf(spListrik.getSelectedItem());
                params.put("id_kos", String.valueOf(idKos));
                //params.put("i_idcust", idCust);
                params.put("i_idsewastatus", statusSewa.equals("Man Only")?"1":
                        statusSewa.equals("Women Only")?"2":"3");
                params.put("vc_namakost", eNamaKos.getText().toString());
                params.put("vc_alamat", eAlamat.getText().toString());
                params.put("vc_gambar", simage1);
                params.put("vc_gambar2", simage2);
                params.put("vc_gambar3", simage3);
                params.put("vc_gambar4", simage4);
                params.put("vc_gambar5", simage5);
                params.put("i_lebar", eLebar.getText().toString());
                params.put("i_panjang", ePanjang.getText().toString());
                params.put("c_statuslistrik", statusListrik.equals("Include")?"Y":"N");
                params.put("i_jmlkamar", eTotalKamar.getText().toString());
                params.put("t_fasilitas", daftarKodeFasilitas);
                params.put("d_latitude", String.valueOf(latitude));
                params.put("d_longtitude", String.valueOf(longtitude));
                params.put("n_harga", eharga.getText().toString());
                return params;
            }
            @Override
            public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                java.util.Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(simpan);
    }

    private boolean validation() {
        boolean val = true;
        if (!CheckValidation.hasText(eNamaKos, getResources().getString(R.string.err_msg_namakos)))
            val = false;
        if (!CheckValidation.hasText(eharga, getResources().getString(R.string.err_msg_hargakos)))
            val = false;
        if (!CheckValidation.hasText(eTotalKamar, getResources().getString(R.string.err_msg_jmlkamar)))
            val = false;
        if (!CheckValidation.hasText(eLebar, getResources().getString(R.string.err_msg_lebarkos)))
            val = false;
        if (!CheckValidation.hasText(ePanjang, getResources().getString(R.string.err_msg_panjangkos)))
            val = false;
        if (!CheckValidation.hasText(eAlamat, getResources().getString(R.string.err_msg_alamatkos)))
            val = false;
        if (!CheckValidation.hasText(eFasilitas, getResources().getString(R.string.err_msg_fasilitas)))
            val = false;
        if (!CheckValidation.hasText(eFoto, getResources().getString(R.string.err_msg_fotokos)))
            val = false;
        return val;
    }

    private void getMaxNumber(String Url){//getMaxNo.php
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
                                if(object.getString("count").equals("null"))
                                    noTrans=1;
                                else{
                                    Integer maxNo = object.getInt("count");
                                    noTrans = maxNo+1;
                                }
                                saveKost(Link.FilePHP+addKost);
                            }else{
                                Toast.makeText(getApplicationContext(),
                                        "Tidak bisa set nomor transaksi!", Toast.LENGTH_LONG)
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, null){
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }

            @Override
            public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                java.util.Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                return params;
            }
        };
        AppController.getInstance().getRequestQueue().getCache().invalidate(Url, true);
        AppController.getInstance().addToRequestQueue(jsonget);
    }

    private void saveKost(String save){
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
                        Toast.makeText(AddHomeKos.this,
                                "Data berhasil disimpan", Toast.LENGTH_LONG)
                                .show();
                        finish();

                    }else{
                        Toast.makeText(AddHomeKos.this,
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
                String statusSewa = String.valueOf(spPeruntukan.getSelectedItem());
                String statusListrik = String.valueOf(spListrik.getSelectedItem());
                params.put("id_kos", String.valueOf(noTrans));
                params.put("i_idcust", idCust);
                params.put("i_idsewastatus", statusSewa.equals("Man Only")?"1":
                    statusSewa.equals("Women Only")?"2":"3");
                params.put("vc_namakost", eNamaKos.getText().toString());
                params.put("vc_alamat", eAlamat.getText().toString());
                params.put("vc_gambar", simage1);
                params.put("vc_gambar2", simage2);
                params.put("vc_gambar3", simage3);
                params.put("vc_gambar4", simage4);
                params.put("vc_gambar5", simage5);
                params.put("i_lebar", eLebar.getText().toString());
                params.put("i_panjang", ePanjang.getText().toString());
                params.put("c_statuslistrik", statusListrik.equals("Include")?"Y":"N");
                params.put("i_jmlkamar", eTotalKamar.getText().toString());
                params.put("t_fasilitas", daftarKodeFasilitas);
                params.put("d_latitude", String.valueOf(latitude));
                params.put("d_longtitude", String.valueOf(longtitude));
                params.put("n_harga", eharga.getText().toString());
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

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_layout_namakos:
                    validateNamaKos();
                    break;
                case R.id.input_layout_harga:
                    validateHarga();
                    break;
                case R.id.input_layout_jmlkamar:
                    validateJmlKamar();
                    break;
                case R.id.input_layout_lebarkamar:
                    validateLebar();
                    break;
                case R.id.input_layout_panjangkamar:
                    validatePanjang();
                    break;
            }
        }
    }

    /*private boolean validateFasilitas() {
        if (eFasilitas.getText().toString().trim().isEmpty()) {
            inputLayoutFasilitas.setError(getString(R.string.err_msg_fasilitas));
            requestFocus(eFasilitas);
            return false;
        } else {
            inputLayoutFasilitas.setErrorEnabled(false);
        }
        return true;
    }*/

    private boolean validatePanjang() {
        if (ePanjang.getText().toString().trim().isEmpty()) {
            inputLayoutPanjangKamar.setError(getString(R.string.err_msg_panjangkos));
            requestFocus(ePanjang);
            return false;
        } else {
            inputLayoutPanjangKamar.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateLebar() {
        if (eLebar.getText().toString().trim().isEmpty()) {
            inputLayoutLebarKamar.setError(getString(R.string.err_msg_lebarkos));
            requestFocus(eLebar);
            return false;
        } else {
            inputLayoutLebarKamar.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateJmlKamar() {
        if (eTotalKamar.getText().toString().trim().isEmpty()) {
            inputLayoutTotalKamar.setError(getString(R.string.err_msg_jmlkamar));
            requestFocus(eTotalKamar);
            return false;
        } else {
            inputLayoutTotalKamar.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateNamaKos() {
        if (eNamaKos.getText().toString().trim().isEmpty()) {
            inputLayoutNamaKos.setError(getString(R.string.err_msg_namakos));
            requestFocus(eNamaKos);
            return false;
        } else {
            inputLayoutNamaKos.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateHarga() {
        if (eharga.getText().toString().trim().isEmpty()) {
            inputLayoutHarga.setError(getString(R.string.err_msg_hargakos));
            requestFocus(eharga);
            return false;
        } else {
            inputLayoutHarga.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 6) {//map
            if(resultCode == RESULT_OK) {//map
                alamat = data.getStringExtra("alamat");
                latitude = data.getDoubleExtra("latitude",0);
                longtitude = data.getDoubleExtra("longtitude",0);
                eAlamat.setText(alamat);
            }
        }/*else if (requestCode == 7) {//kota
            if(resultCode == RESULT_OK) {
                kodeKota = data.getStringExtra("kodeKota");
                namaKota = data.getStringExtra("namaKota");
                eKota.setText(namaKota);
            }
        }*/else if (requestCode == 1 && resultCode == this.RESULT_OK && null != data) {//add foto
            try {
                simage1 = getNameImage().toString() + ".jpg";
                Uri selectedImage = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                convertedPath = getRealPathFromURI(selectedImage);
                eFoto.setText(convertedPath);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byte_arr = stream.toByteArray();
                encodedString = Base64.encodeToString(byte_arr, Base64.DEFAULT);
                //uploadImage(Link.FileUpload, simage1);
                uploadingImage(Link.FileUpload, simage1);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }else if (requestCode == 2 && resultCode == this.RESULT_OK && null != data) {//gmbar foto2
            simage2	= getNameImage().toString()+".jpg";
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = this.getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Utils.GetCycleImage("file:///"+picturePath, imgFoto2, this);
            String fileNameSegments[] = picturePath.split("/");
            Bitmap myImg = BitmapFactory.decodeFile(picturePath);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Must compress the Image to reduce image size to make upload easy
            myImg.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byte_arr = stream.toByteArray();
            // Encode Image to String
            encodedString = Base64.encodeToString(byte_arr, Base64.DEFAULT);
            uploadingImage(Link.FileUpload, simage2);
        }else if (requestCode == 3 && resultCode == this.RESULT_OK && null != data) {//gmbar foto3
            simage3	= getNameImage().toString()+".jpg";
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = this.getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Utils.GetCycleImage("file:///"+picturePath, imgFoto3, this);
            String fileNameSegments[] = picturePath.split("/");
            Bitmap myImg = BitmapFactory.decodeFile(picturePath);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Must compress the Image to reduce image size to make upload easy
            myImg.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            byte[] byte_arr = stream.toByteArray();
            // Encode Image to String
            encodedString = Base64.encodeToString(byte_arr, Base64.DEFAULT);
            uploadingImage(Link.FileUpload, simage3);
        }else if (requestCode == 4 && resultCode == this.RESULT_OK && null != data) {//gmbar foto4
            simage4	= getNameImage().toString()+".jpg";
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = this.getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Utils.GetCycleImage("file:///"+picturePath, imgFoto4, this);
            String fileNameSegments[] = picturePath.split("/");
            Bitmap myImg = BitmapFactory.decodeFile(picturePath);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Must compress the Image to reduce image size to make upload easy
            myImg.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            byte[] byte_arr = stream.toByteArray();
            // Encode Image to String
            encodedString = Base64.encodeToString(byte_arr, Base64.DEFAULT);
            uploadingImage(Link.FileUpload, simage4);
        }else if (requestCode == 5 && resultCode == this.RESULT_OK && null != data) {////gmbar foto5
            simage5	= getNameImage().toString()+".jpg";
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = this.getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Utils.GetCycleImage("file:///"+picturePath, imgFoto5, this);
            String fileNameSegments[] = picturePath.split("/");
            Bitmap myImg = BitmapFactory.decodeFile(picturePath);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Must compress the Image to reduce image size to make upload easy
            myImg.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            byte[] byte_arr = stream.toByteArray();
            // Encode Image to String
            encodedString = Base64.encodeToString(byte_arr, Base64.DEFAULT);
            uploadingImage(Link.FileUpload, simage5);
        }
    }

    private void uploadingImage(String url, final String ImageName){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Upload ke server...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(AddHomeKos.this, "Gambar berhasil diupload!" , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        //Showing toast
                        Toast.makeText(AddHomeKos.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new HashMap<String, String>();
                params.put("image", encodedString);
                params.put("filename", ImageName);
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private String getNameImage() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd-HHmmss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader cursorLoader = new CursorLoader(
                this,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        int column_index =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
