package fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.chris.nearbykos2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapter.AdpListPenyewaKos;
import control.AppController;
import control.Link;
import model.ColTransaksi;

/**
 * Created by christian on 04/01/18.
 */

public class FListPenyewaKos extends Fragment {

    private String idKos;
    private View vupload;
    private AdpListPenyewaKos adapter;
    private ListView lsvupload;
    private ArrayList<ColTransaksi> columnlist= new ArrayList<ColTransaksi>();
    private TextView tvstatus, txtNamaKos;
    private ProgressBar prbstatus;
    private String getData	="getListPenyewaKos.php?idKos=";
    private String namaKos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle	 = this.getArguments();
        if (bundle!=null){
            idKos	= bundle.getString("idKos");
            namaKos = bundle.getString("namaKos");
        }
        vupload     = inflater.inflate(R.layout.list_penyewa,container,false);
        lsvupload	= (ListView)vupload.findViewById(R.id.LsvListPenyewaKos);
        tvstatus	= (TextView)vupload.findViewById(R.id.TvStatusListPenyewaKos);
        prbstatus	= (ProgressBar)vupload.findViewById(R.id.PrbStatusListPenyewaKos);
        txtNamaKos  = (TextView)vupload.findViewById(R.id.TvNamaKosListPenyewaKos);

        adapter		= new AdpListPenyewaKos(getActivity(), R.layout.col_penyewa, columnlist);
        lsvupload.setAdapter(adapter);
        GetDataUpload(Link.FilePHP+getData+idKos);
        txtNamaKos.setText("Daftar Penyewa "+namaKos);
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
                                    ColTransaksi colums 	= new ColTransaksi();
                                    colums.setIdBooking(object.getInt("i_idbooking"));
                                    colums.setIdPenyewa(object.getInt("i_iduser"));
                                    colums.setNamaPenyewa(object.getString("vc_namalengkap"));
                                    colums.setIdKos(object.getInt("id_kos"));
                                    colums.setTelpPenyewa(object.getString("c_telp"));
                                    colums.setTglMasuk(object.getString("dt_mulaisewa"));
                                    colums.setTglKeluar(object.getString("dt_selesaisewa"));
                                    colums.setStatus(object.getString("c_stat"));
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
        columnlist= new ArrayList<ColTransaksi>();
        adapter		= new AdpListPenyewaKos(getActivity(), R.layout.col_penyewa, columnlist);
        lsvupload.setAdapter(adapter);
        GetDataUpload(Link.FilePHP+getData+idKos);
    }
}
