package fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import com.example.chris.nearbykos2.AddHomeKos;
import com.example.chris.nearbykos2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapter.AdpHomeKos;
import control.AppController;
import control.Link;
import model.ColHomeDetail;
import session.SessionManager;

/**
 * Created by Chris on 16/06/2017.
 */

public class FHomeKos extends Fragment {

    private View vupload;
    private ImageView ImgAdd;
    private AdpHomeKos adapter;
    private ListView lsvupload;
    private ArrayList<ColHomeDetail> columnlist= new ArrayList<ColHomeDetail>();
    private TextView tvstatus;
    private ProgressBar prbstatus;
    private String getUpload	="getKosByCust.php?idCust=";
    private SessionManager session;
    private String idCust, status;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle	 = this.getArguments();
        if (bundle!=null){
            idCust	= bundle.getString("idCust");
            status	= bundle.getString("status");
        }

        vupload     = inflater.inflate(R.layout.list_homedetail,container,false);
        ImgAdd		= (ImageView)vupload.findViewById(R.id.ImgListAddKos);
        lsvupload	= (ListView)vupload.findViewById(R.id.LsvDetailHome);
        tvstatus	= (TextView)vupload.findViewById(R.id.TvStatusDetailHome);
        prbstatus	= (ProgressBar)vupload.findViewById(R.id.PrbStatusDetailHome);

        adapter		= new AdpHomeKos(getActivity(), R.layout.col_homedetail, columnlist, status);
        lsvupload.setAdapter(adapter);
        GetDataUpload(Link.FilePHP+getUpload+idCust);

        if(!status.equals("1")){
            ImgAdd.setVisibility(View.INVISIBLE);
        }

        ImgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(getActivity(), AddHomeKos.class);
                i.putExtra("Status", 1);
                i.putExtra("idCust", idCust);
                startActivity(i);
            }
        });

        lsvupload.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> Parent, View view, int position,
                                    long id) {
                if(status.equals("2")){
                    changeFragment(String.valueOf(columnlist.get(position).getId_kos()));
                }else if(status.equals("3")){
                    changeFragmentPenyewa(String.valueOf(columnlist.get(position).getId_kos()),
                            columnlist.get(position).getNamaKos().trim());
                }
            }
        });

        return vupload;
    }

    private void changeFragment(String idKos){
        FListBookingCust fragment2 = new FListBookingCust();
        FragmentManager fragmentManager = getFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransitionStyle(fragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.FrmMainMenu, fragment2);
        fragmentTransaction.commit();
        Bundle extras = new Bundle();
        extras.putString("idKos", idKos);
        fragment2.setArguments(extras);
    }

    private void changeFragmentPenyewa(String idKos, String namaKos){
        FListPenyewaKos fragment2 = new FListPenyewaKos();
        FragmentManager fragmentManager = getFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransitionStyle(fragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.FrmMainMenu, fragment2);
        fragmentTransaction.commit();
        Bundle extras = new Bundle();
        extras.putString("idKos", idKos);
        extras.putString("namaKos", namaKos);
        fragment2.setArguments(extras);
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
                            Log.i("Status", String.valueOf(sucses));
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
                                    colums.setGambar2(object.getString("vc_gambar2"));
                                    colums.setGambar3(object.getString("vc_gambar3"));
                                    colums.setGambar4(object.getString("vc_gambar4"));
                                    colums.setGambar5(object.getString("vc_gambar5"));
                                    colums.setLebar(object.getInt("i_lebar"));
                                    colums.setPanjang(object.getInt("i_panjang"));
                                    colums.setStatusListrik(object.getString("c_statuslistrik"));
                                    colums.setJmlhKamar(object.getInt("i_jmlkamar"));
                                    colums.setFasilitas(object.getString("t_fasilitas"));
                                    colums.setLatitude(object.getDouble("d_latitude"));
                                    colums.setLongtitude(object.getDouble("d_longtitude"));
                                    colums.setHarga(object.getInt("n_harga"));
                                    colums.setKodeKota(object.getString("c_kodekota"));
                                    colums.setNamaKota(object.getString("vc_namakota"));
                                    colums.setSisa(object.getInt("i_sisa"));
                                    colums.setRating(object.getDouble("total_rating"));
                                    colums.setCountUser(object.getInt("count_user"));
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
        columnlist= new ArrayList<ColHomeDetail>();
        adapter		= new AdpHomeKos(getActivity(), R.layout.col_homedetail, columnlist, status);
        lsvupload.setAdapter(adapter);
        GetDataUpload(Link.FilePHP+getUpload+idCust);
    }
}