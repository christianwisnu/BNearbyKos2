package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.example.chris.nearbykos2.AddRekeningCust;
import com.example.chris.nearbykos2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapter.AdpCustRekening;
import control.AppController;
import control.Link;
import model.ColCustRekening;

/**
 * Created by christian on 19/01/18.
 */

public class FCustRekening extends Fragment {

    private View vupload;
    private ImageView ImgAdd;
    private AdpCustRekening adapter;
    private ListView lsvupload;
    private ArrayList<ColCustRekening> columnlist= new ArrayList<ColCustRekening>();
    private TextView tvstatus;
    private ProgressBar prbstatus;
    private String getUpload	="getRekCust.php?kodeCust=";
    private String idCust, status;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle	 = this.getArguments();
        if (bundle!=null){
            idCust	= bundle.getString("idCust");
        }

        vupload     = inflater.inflate(R.layout.list_rekening,container,false);
        ImgAdd		= (ImageView)vupload.findViewById(R.id.ImgListAddRek);
        lsvupload	= (ListView)vupload.findViewById(R.id.LsvRek);
        tvstatus	= (TextView)vupload.findViewById(R.id.TvStatusRek);
        prbstatus	= (ProgressBar)vupload.findViewById(R.id.PrbStatusRek);

        adapter		= new AdpCustRekening(getActivity(), R.layout.col_custrekening, columnlist);
        lsvupload.setAdapter(adapter);
        GetDataUpload(Link.FilePHP+getUpload+idCust);

        ImgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(getActivity(), AddRekeningCust.class);
                i.putExtra("Status", 1);
                i.putExtra("idCust", idCust);
                startActivity(i);
            }
        });

        /*lsvupload.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> Parent, View view, int position,
                                    long id) {
                // TODO Auto-generated method stub
                if(status.equals("2")){
                    changeFragment(String.valueOf(columnlist.get(position).getId_kos()));
                }else if(status.equals("3")){
                    changeFragmentPenyewa(String.valueOf(columnlist.get(position).getId_kos()),
                            columnlist.get(position).getNamaKos().trim());
                }
            }
        });*/


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
                            if (sucses==1){
                                tvstatus.setVisibility(View.GONE);
                                prbstatus.setVisibility(View.GONE);
                                adapter.clear();
                                JSONArray JsonArray = response.getJSONArray("uploade");
                                for (int i = 0; i < JsonArray.length(); i++) {
                                    JSONObject object = JsonArray.getJSONObject(i);
                                    ColCustRekening colums 	= new ColCustRekening();
                                    colums.setKodeCust(object.getInt("kodeCust"));
                                    colums.setKodeBank(object.getString("kodeBank"));
                                    colums.setNamaBank(object.getString("namaBank"));
                                    colums.setLine(object.getInt("line"));
                                    colums.setNoRek(object.getString("noRek"));
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
        adapter		= new AdpCustRekening(getActivity(), R.layout.col_custrekening, columnlist);
        lsvupload.setAdapter(adapter);
        GetDataUpload(Link.FilePHP+getUpload+idCust);
    }
}
