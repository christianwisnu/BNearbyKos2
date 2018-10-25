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
import java.util.Objects;

import adapter.AdpListBookingCust;
import control.AppController;
import control.Link;
import model.ColHomeBooking;

/**
 * Created by Chris on 26/06/2017.
 */

public class FListBookingCust extends Fragment {

    private String idKos;
    private View vupload;
    private AdpListBookingCust adapter;
    private ListView lsvupload;
    private ArrayList<ColHomeBooking> columnlist= new ArrayList<ColHomeBooking>();
    private TextView tvstatus;
    private ProgressBar prbstatus;
    private String getData	="getListBookingByKos.php?idKos=";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle	 = this.getArguments();
        if (bundle!=null){
            idKos	= bundle.getString("idKos");
        }
        vupload     = inflater.inflate(R.layout.list_bookingcust,container,false);
        lsvupload	= (ListView)vupload.findViewById(R.id.LsvListBookingCustKos);
        tvstatus	= (TextView)vupload.findViewById(R.id.TvStatusListBookingCustKos);
        prbstatus	= (ProgressBar)vupload.findViewById(R.id.PrbStatusListBookingCustKos);

        adapter		= new AdpListBookingCust(getActivity(), R.layout.col_bookingcust, columnlist);
        lsvupload.setAdapter(adapter);
        GetDataUpload(Link.FilePHP+getData+idKos);
        return vupload;
    }

    private void GetDataUpload(String Url){
        tvstatus.setVisibility(View.GONE);
        prbstatus.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonget = new JsonObjectRequest(Request.Method.GET, Url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // TODO Auto-generated method stub
                        try {
                            int sucses= response.getInt("success");
                            Log.i("Status", String.valueOf(sucses));
                            if (sucses==1){
                                tvstatus.setVisibility(View.GONE);
                                prbstatus.setVisibility(View.GONE);
                                adapter.clear();
                                JSONArray JsonArray = response.getJSONArray("uploade");
                                for (int i = 0; i < JsonArray.length(); i++) {
                                    JSONObject object = JsonArray.getJSONObject(i);
                                    ColHomeBooking colums 	= new ColHomeBooking();
                                    colums.setIdBooking(object.getInt("i_idbooking"));
                                    colums.setIdKos(object.getInt("id_kos"));
                                    colums.setNamaKos(object.getString("vc_namakost"));
                                    colums.setGambarKos(object.getString("vc_gambar"));
                                    colums.setAlamatKos(object.getString("vc_alamat"));
                                    colums.setIdCust(object.getInt("i_idcust"));
                                    colums.setNamaCust(object.getString("namaCust"));
                                    colums.setIdUser(object.getInt("i_iduser"));
                                    colums.setNamaUser(object.getString("namaUser"));
                                    colums.setHarga(object.getInt("n_harga"));
                                    colums.setCStat(object.getString("c_stat"));
                                    colums.setStatusBooking(object.getString("c_statusbooking"));
                                    colums.setTelpUser(object.getString("c_telp"));
                                    colums.setTglSurvey(object.getString("dt_cekin"));
                                    colums.setStatusDP(object.getString("c_bayar"));
                                    colums.setFotoDP(object.getString("vc_fotodp"));
                                    colums.setKodeBank(object.getString("kode_bank"));
                                    colums.setNamaBank(object.getString("nama_bank"));
                                    colums.setNoRek(object.getString("vc_norek"));
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
                        //lsvupload.invalidate();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    tvstatus.setVisibility(View.VISIBLE);
                    tvstatus.setText("Check Koneksi Internet Anda");
                    prbstatus.setVisibility(View.GONE);
                } else if (error instanceof AuthFailureError) {
                    //TODO
                    tvstatus.setVisibility(View.VISIBLE);
                    tvstatus.setText("AuthFailureError");
                    prbstatus.setVisibility(View.GONE);
                } else if (error instanceof ServerError) {
                    //TODO
                    tvstatus.setVisibility(View.VISIBLE);
                    tvstatus.setText("Check ServerError");
                    prbstatus.setVisibility(View.GONE);
                } else if (error instanceof NetworkError) {
                    //TODO
                    tvstatus.setVisibility(View.VISIBLE);
                    tvstatus.setText("Check NetworkError");
                    prbstatus.setVisibility(View.GONE);
                } else if (error instanceof ParseError) {
                    //TODO
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
        adapter		= new AdpListBookingCust(getActivity(), R.layout.col_bookingcust, columnlist);
        lsvupload.setAdapter(adapter);
        GetDataUpload(Link.FilePHP+getData+idKos);
    }
}
