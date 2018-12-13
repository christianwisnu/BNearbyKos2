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
import com.example.chris.nearbykos2.InfoKos;
import com.example.chris.nearbykos2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import adapter.AdpListImageKos;
import control.AppController;
import control.Link;
import model.ColHomeDetail;

/**
 * Created by Chris on 25/06/2017.
 */

public class FImageListKos extends Fragment {

    private String idUser;
    private View vupload;
    private AdpListImageKos adapter;
    private ListView lsvupload;
    private ArrayList<ColHomeDetail> columnlist= new ArrayList<ColHomeDetail>();
    private TextView tvstatus;
    private ProgressBar prbstatus;
    private String getData	="getListAllKos.php";
    NumberFormat rupiah	= NumberFormat.getNumberInstance(new Locale("in", "ID"));

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle	 = this.getArguments();
        if (bundle!=null){
            idUser	= bundle.getString("idUser");
        }

        vupload     = inflater.inflate(R.layout.lis_imagekos,container,false);
        lsvupload	= (ListView)vupload.findViewById(R.id.LsvListImageUserKos);
        tvstatus	= (TextView)vupload.findViewById(R.id.TvStatusListImageUserKos);
        prbstatus	= (ProgressBar)vupload.findViewById(R.id.PrbStatusListImageUserKos);

        adapter		= new AdpListImageKos(getActivity(), R.layout.col_imagelist, columnlist);
        lsvupload.setAdapter(adapter);
        GetDataUpload(Link.FilePHP+getData);

        lsvupload.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> Parent, View view, int position,
                                    long id) {
                Intent i = new Intent(getActivity(), InfoKos.class);
                i.putExtra("id_kos", columnlist.get(position).getId_kos());
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
                i.putExtra("gambar", columnlist.get(position).getGambar());
                i.putExtra("gambar", columnlist.get(position).getGambar());
                i.putExtra("c_kodekota", columnlist.get(position).getKodeKota());
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
                                JSONArray JsonArray = response.getJSONArray("uploade");
                                for (int i = 0; i < JsonArray.length(); i++) {
                                    JSONObject object = JsonArray.getJSONObject(i);
                                    ColHomeDetail colums 	= new ColHomeDetail();
                                    colums.setId_kos(object.getInt("id_kos"));
                                    colums.setId_cust(object.getInt("i_idcust"));
                                    colums.setId_sewaStatus(object.getInt("i_idsewastatus"));
                                    colums.setNamaKos(object.getString("vc_namakost"));
                                    colums.setAlamat(object.getString("vc_alamat"));
                                    colums.setGambar(object.getString("vc_gambar"));
                                    colums.setLebar(object.getInt("i_lebar"));
                                    colums.setPanjang(object.getInt("i_panjang"));
                                    colums.setStatusListrik(object.getString("c_statuslistrik"));
                                    colums.setJmlhKamar(object.getInt("i_jmlkamar"));
                                    colums.setFasilitas(object.getString("t_fasilitas"));
                                    colums.setLatitude(object.getDouble("d_latitude"));
                                    colums.setLongtitude(object.getDouble("d_longtitude"));
                                    colums.setHarga(object.getInt("n_harga"));
                                    colums.setNamaCust(object.getString("vc_namalengkap"));
                                    colums.setTlpCust(object.getString("c_telp"));
                                    colums.setEmailCust(object.getString("c_email"));
                                    colums.setKodeKota(object.getString("c_kodekota"));
                                    colums.setSisa(object.getInt("sisa"));
                                    colums.setRating(object.getDouble("total_rating"));
                                    colums.setCountUser(object.getInt("count_user"));
                                    // list gmbar blm
                                    columnlist.add(colums);
                                }
                            }else{
                                tvstatus.setVisibility(View.VISIBLE);
                                tvstatus.setText("Tidak Ada Data");
                                prbstatus.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
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
                    tvstatus.setText("Check ParseError");
                    prbstatus.setVisibility(View.GONE);
                }
            }
        }){
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
        adapter		= new AdpListImageKos(getActivity(), R.layout.col_imagelist, columnlist);
        lsvupload.setAdapter(adapter);
        GetDataUpload(Link.FilePHP+getData);

    }

}

