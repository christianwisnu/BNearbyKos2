package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.NetworkError;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.ParseError;
import com.android.volley.error.ServerError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.StringRequest;
import com.example.chris.nearbykos2.InfoKos;
import com.example.chris.nearbykos2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import adapter.AdpListUserKos;
import control.AppController;
import control.Link;
import model.ColHomeDetail;

/**
 * Created by Chris on 21/06/2017.
 */

public class FListKos extends Fragment {

    private String idUser,sql;
    private View vupload;
    private AdpListUserKos adapter;
    private ListView lsvupload;
    private ArrayList<ColHomeDetail> columnlist= new ArrayList<ColHomeDetail>();
    private TextView tvstatus;
    private ProgressBar prbstatus;
    private String getData	="getListKriteriaKos.php";
    private String getDataAll	="getListAllKos.php";
    NumberFormat rupiah	= NumberFormat.getNumberInstance(new Locale("in", "ID"));

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle	 = this.getArguments();
        if (bundle!=null){
            idUser	= bundle.getString("idUser");
            sql	= bundle.getString("sql");
        }
        vupload     = inflater.inflate(R.layout.list_kos,container,false);
        lsvupload	= (ListView)vupload.findViewById(R.id.LsvListUserKos);
        tvstatus	= (TextView)vupload.findViewById(R.id.TvStatusListUserKos);
        prbstatus	= (ProgressBar)vupload.findViewById(R.id.PrbStatusListUserKos);

        adapter		= new AdpListUserKos(getActivity(), R.layout.col_listuser, columnlist);
        lsvupload.setAdapter(adapter);

        if(!sql.equals("ALL"))
            GetDataUpload(Link.FilePHP+getData);
        else
            GetDataUpload(Link.FilePHP+getDataAll);

        lsvupload.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> Parent, View view, int position,
                                    long id) {
                Intent i = new Intent(getActivity(), InfoKos.class);
                i.putExtra("id_kos", columnlist.get(position).getId_kos());
                i.putExtra("idUser", idUser);
                i.putExtra("i_idcust", columnlist.get(position).getId_cust());
                i.putExtra("namaCust", columnlist.get(position).getNamaCust());
                i.putExtra("i_jmlkamar", columnlist.get(position).getJmlhKamar());
                i.putExtra("vc_alamat", columnlist.get(position).getAlamat());
                i.putExtra("vc_namakost", columnlist.get(position).getNamaKos());
                i.putExtra("i_lebar", columnlist.get(position).getLebar());
                i.putExtra("i_panjang", columnlist.get(position).getPanjang());
                i.putExtra("c_statuslistrik", columnlist.get(position).getStatusListrik().equals("Y")?"Include":"Exclude");
                i.putExtra("i_idsewastatus", columnlist.get(position).getId_sewaStatus()==1?"Man Only":
                        columnlist.get(position).getId_sewaStatus()==2?"Women Only":"All");
                i.putExtra("tlpCust", columnlist.get(position).getTlpCust());
                i.putExtra("fasilitas", columnlist.get(position).getFasilitas());
                i.putExtra("lat", columnlist.get(position).getLatitude());
                i.putExtra("longt", columnlist.get(position).getLongtitude());
                i.putExtra("harga", "Rp. "+rupiah.format(columnlist.get(position).getHarga()));
                i.putExtra("hargaAsli", columnlist.get(position).getHarga());
                i.putExtra("gambar", columnlist.get(position).getGambar());
                i.putExtra("gambar2", columnlist.get(position).getGambar2());
                i.putExtra("gambar3", columnlist.get(position).getGambar3());
                i.putExtra("gambar4", columnlist.get(position).getGambar4());
                i.putExtra("gambar5", columnlist.get(position).getGambar5());
                i.putExtra("c_kodekota", columnlist.get(position).getKodeKota());
                i.putExtra("i_sisa", columnlist.get(position).getSisa());
                i.putExtra("rating", columnlist.get(position).getRating());
                i.putExtra("countUser", columnlist.get(position).getCountUser());
                getActivity().startActivity(i);
            }
        });

        return vupload;
    }

    private void GetDataUpload(String Url){
        tvstatus.setVisibility(View.GONE);
        prbstatus.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonget = new JsonObjectRequest(Request.Method.GET, Url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int sucses= response.getInt("success");
                            if (sucses==1){
                                tvstatus.setVisibility(View.GONE);
                                prbstatus.setVisibility(View.GONE);
                                adapter.clear();
                                JSONArray JsonArray = response.getJSONArray("kriteriaKos");
                                for (int i = 0; i < JsonArray.length(); i++) {
                                    JSONObject object = JsonArray.getJSONObject(i);
                                    ColHomeDetail colums 	= new ColHomeDetail();
                                    colums.setId_kos(object.getInt("id_kos"));
                                    colums.setId_cust(object.getInt("id_cust"));
                                    colums.setId_sewaStatus(object.getInt("id_sewaStatus"));
                                    colums.setNamaKos(object.getString("namaKos"));
                                    colums.setAlamat(object.getString("alamat"));
                                    colums.setGambar(object.getString("gambar"));
                                    colums.setLebar(object.getInt("lebar"));
                                    colums.setPanjang(object.getInt("panjang"));
                                    colums.setStatusListrik(object.getString("statusListrik"));
                                    colums.setJmlhKamar(object.getInt("jmlhKamar"));
                                    colums.setFasilitas(object.getString("fasilitas"));
                                    colums.setLatitude(object.getDouble("latitude"));
                                    colums.setLongtitude(object.getDouble("longtitude"));
                                    colums.setHarga(object.getInt("harga"));
                                    colums.setNamaCust(object.getString("namaCust"));
                                    colums.setTlpCust(object.getString("tlpCust"));
                                    colums.setEmailCust(object.getString("emailCust"));
                                    colums.setGambar2(object.getString("gambar2"));
                                    colums.setGambar3(object.getString("gambar3"));
                                    colums.setGambar4(object.getString("gambar4"));
                                    colums.setGambar5(object.getString("gambar5"));
                                    colums.setKodeKota(object.getString("kodeKota"));
                                    colums.setSisa(object.getInt("sisa"));
                                    colums.setRating(object.getDouble("total_rating"));
                                    colums.setCountUser(object.getInt("count_user"));
                                    // list gmbar bl
                                    columnlist.add(colums);
                                }
                            }else{
                                tvstatus.setVisibility(View.VISIBLE);
                                tvstatus.setText("Tidak Ada Data");
                                prbstatus.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    tvstatus.setVisibility(View.VISIBLE);
                    tvstatus.setText("Check Koneksi Internet Anda");
                    prbstatus.setVisibility(View.GONE);
                } else if (error instanceof AuthFailureError) {
                    tvstatus.setVisibility(View.VISIBLE);
                    tvstatus.setText("AuthFailureError");
                    prbstatus.setVisibility(View.GONE);
                } else if (error instanceof ServerError) {
                    tvstatus.setVisibility(View.VISIBLE);
                    tvstatus.setText("Check ServerError");
                    prbstatus.setVisibility(View.GONE);
                } else if (error instanceof NetworkError) {
                    tvstatus.setVisibility(View.VISIBLE);
                    tvstatus.setText("Check NetworkError");
                    prbstatus.setVisibility(View.GONE);
                } else if (error instanceof ParseError) {
                    tvstatus.setVisibility(View.VISIBLE);
                    tvstatus.setText(error.toString());
                    prbstatus.setVisibility(View.GONE);
                }
            }
        }){
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new HashMap<String, String>();
                params.put("querysql", sql);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                return params;
            }
        };
        AppController.getInstance().getRequestQueue().getCache().invalidate(Url, true);
        AppController.getInstance().addToRequestQueue(jsonget);
    }

    @Override
    public void onResume() {
        super.onResume();
        columnlist= new ArrayList<ColHomeDetail>();
        adapter		= new AdpListUserKos(getActivity(), R.layout.col_listuser, columnlist);
        lsvupload.setAdapter(adapter);
        if(!sql.equals("ALL"))
            GetDataUpload(Link.FilePHP+getData);
        else
            GetDataUpload(Link.FilePHP+getDataAll);
    }
}