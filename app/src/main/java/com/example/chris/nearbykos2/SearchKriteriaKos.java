package com.example.chris.nearbykos2;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import control.Link;
import fragment.FListKos;
import model.ColFasilitas;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.view.Gravity.START;

/**
 * Created by christian on 08/11/17.
 */

public class SearchKriteriaKos extends Activity {

    ArrayList<ColFasilitas> seletedItems=new ArrayList();
    private ArrayList<ColFasilitas> mArrayList;
    private CharSequence[] items = null;
    private List<CharSequence> charSequences = new ArrayList<>();
    private Spinner spTipe;
    private Button btnKota, btnFasilitas;
    private ProgressDialog dialog;
    private EditText eFasilitas, eKota, eMulai, eSampai;
    private TextView txtCari;
    private ImageView imgBack;
    private String daftarKodeFasilitas, daftarNamaFasilitas, kodeKota, namaKota, idUser, querySisa;
    private CheckBox ckAllFasilitas, ckAllHarga, ckAllKota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kriteria_searching_kos);
        eFasilitas  	= (EditText)findViewById(R.id.eKriteriaFasilitasKos);
        //eKota  		    = (EditText)findViewById(R.id.eKriteriaKotaKos);
        eMulai  		= (EditText)findViewById(R.id.eKriteriaMulaiHargaKos);
        eSampai  		= (EditText)findViewById(R.id.eKriteriaSampaiHargaKos);
        spTipe          = (Spinner) findViewById(R.id.spKriteriaTipeKos);
        //btnKota         = (Button)findViewById(R.id.bKriteriaKotaKos);
        btnFasilitas    = (Button)findViewById(R.id.bKriteriaFasilitasKos);
        txtCari 		= (TextView)findViewById(R.id.txtKriteriaCari);
        imgBack 		= (ImageView) findViewById(R.id.imgBackKriteriaKos);
        ckAllFasilitas  = (CheckBox) findViewById(R.id.ckAllFasilitas);
        ckAllHarga      = (CheckBox) findViewById(R.id.ckAllHarga);
        //ckAllKota       = (CheckBox) findViewById(R.id.ckAllKota);

        loadFasilitas();

        //querySisa = "a.i_sisa ";
        querySisa = "a.i_jmlkamar-(SELECT COUNT(*) FROM home_transaction d WHERE d.c_stat='A' AND d.c_statusbooking='A' AND d.id_kos = a.id_kos) AS i_sisa," +
                " COALESCE((select sum(n_nilai) from tbRating where id_kos = a.id_kos),0) AS total_rating," +
                " COALESCE((select count(*) from tbRating where id_kos = a.id_kos),0) AS count_user  ";
        ckAllFasilitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ckAllFasilitas.isChecked()){
                    eFasilitas.setText("");
                    //eFasilitas.setEnabled(false);
                    btnFasilitas.setEnabled(false);
                }else{
                    //eFasilitas.setEnabled(true);
                    btnFasilitas.setEnabled(true);
                }
            }
        });

        /*ckAllKota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ckAllKota.isChecked()){
                    eKota.setText("");
                    eKota.setEnabled(false);
                    btnKota.setEnabled(false);
                }else{
                    eKota.setEnabled(true);
                    btnKota.setEnabled(true);
                }
            }
        });*/

        ckAllHarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ckAllHarga.isChecked()){
                    eMulai.setText("0");
                    eSampai.setText("0");
                    eMulai.setEnabled(false);
                    eSampai.setEnabled(false);
                }else{
                    eMulai.setEnabled(true);
                    eSampai.setEnabled(true);
                }
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a="";
                String statusSewa = String.valueOf(spTipe.getSelectedItem());
                StringBuilder sb = new StringBuilder();
                sb.append("SELECT a.id_kos, a.i_idcust, b.vc_namalengkap, b.c_telp, b.c_email , a.i_idsewastatus, ")
                  .append("a.vc_namakost, a.vc_alamat, a.c_kodekota, c.vc_namakota, c.d_lat AS lat_kota, c.d_lng AS long_kota, ")
                  .append("a.vc_gambar, a.i_lebar, a.vc_gambar2, a.vc_gambar3, a.vc_gambar4, a.vc_gambar5, ")
                  .append("a.i_panjang, a.c_statuslistrik, a.i_jmlkamar, a.t_fasilitas, a.d_latitude, a.d_longtitude, a.n_harga, ")
                  .append(querySisa)
                  .append("FROM home_detail a ")
                  .append("JOIN cust_account b ON a.i_idcust = b.i_idcust ")
                  .append("LEFT OUTER JOIN city c ON a.c_kodekota=c.c_kodekota ")
                  .append("WHERE a.i_idsewastatus = ")
                  .append(statusSewa.equals("Man Only")?"1 ":statusSewa.equals("Women Only")?"2 ":"3 ");
                if(!ckAllHarga.isChecked()){
                    sb.append("AND a.n_harga >= "+Integer.valueOf(eMulai.getText().toString())+" AND a.n_harga <= "+Integer.valueOf(eSampai.getText().toString())+" ");
                }
                  //.append(!ckAllKota.isChecked()?"AND a.c_kodekota = '"+kodeKota+"' ":"");
                if(!ckAllFasilitas.isChecked()){
                    sb.append("AND");
                    String fasilitas = eFasilitas.getText().toString();
                    List<String> listFasilitas = Arrays.asList(fasilitas.split(","));
                    for(String key:listFasilitas){
                        sb.append(" a.t_fasilitas LIKE '%"+key.trim()+"%' OR");
                    }
                    a= sb.toString().substring(0, sb.toString().length()-2);
                    //sb.append(") ");
                }else{
                    a=sb.toString();
                }
                a = a + " ORDER BY a.id_kos ASC";
                Intent intent = new Intent();
                intent.putExtra("sql", a);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        /*btnKota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SearchKriteriaKos.this, SearchListKota.class);
                startActivityForResult(i,7);
            }
        });*/

        btnFasilitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seletedItems = new ArrayList<ColFasilitas>();
                AlertDialog dialog = new AlertDialog.Builder(SearchKriteriaKos.this)
                        .setTitle("Pilih Fasilitas")
                        .setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    seletedItems.add(mArrayList.get(indexSelected));
                                } else {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7) {//kota
            if(resultCode == RESULT_OK) {
                kodeKota = data.getStringExtra("kodeKota");
                namaKota = data.getStringExtra("namaKota");
                eKota.setText(namaKota);
            }
        }
    }

    private void loadFasilitas(){
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
}
