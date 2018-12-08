package fragment;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.chris.nearbykos2.MapAllKos;
import com.example.chris.nearbykos2.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import control.AppController;
import control.Link;
import model.ColHomeDetail;
import session.SessionManager;

import static android.view.Gravity.START;

public class FHome extends Fragment {

    private View vupload;
    private ImageView imgListKos, imgBoking, imgMap, imgLogout;
    private TextView tvstatus;
    private ProgressBar prbstatus;
    private String getData	="getListAllKos.php";
    private SessionManager session;
    private String idUser, sql;
    private LocationManager locationManager ;
    private boolean GpsStatus ;
    private SliderLayout sliderLayout;
    private ArrayList<ColHomeDetail> columnlist= new ArrayList<ColHomeDetail>();
    private TreeMap<Integer,Integer> treeMap;
    private int a;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle	 = this.getArguments();
        if (bundle!=null){
            idUser	= bundle.getString("idUser");
            sql	= bundle.getString("sql");
        }

        vupload     = inflater.inflate(R.layout.menu_home,container,false);
        imgListKos	= (ImageView)vupload.findViewById(R.id.home_listkos);
        imgBoking	= (ImageView)vupload.findViewById(R.id.home_booking);
        imgMap		= (ImageView)vupload.findViewById(R.id.home_maps);
        imgLogout	= (ImageView)vupload.findViewById(R.id.home_logout);
        sliderLayout = (SliderLayout) vupload.findViewById(R.id.home_slider);

        treeMap = new TreeMap<Integer,Integer>();
        treeMap.put(1, R.drawable.l1);
        treeMap.put(2, R.drawable.l2);
        treeMap.put(3, R.drawable.l3);

        for(Integer name : treeMap.keySet()){
            Integer gambar=treeMap.get(name);
            TextSliderView textSliderView = new TextSliderView(getContext());
            textSliderView
                    .description("Gambar "+String.valueOf(name))
                    .image(gambar)
                    .setScaleType(BaseSliderView.ScaleType.Fit);

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

        imgListKos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragmentListUploadKriteria(new FListKos(), String.valueOf(idUser), "ALL");
            }
        });

        imgBoking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragmentListUploadUserKriteria(new FListBookingUser(), String.valueOf(idUser));
            }
        });

        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                session.logoutUser();
            }
        });

        imgMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckGpsStatus();
                if(GpsStatus == true){
                    getDataUpload(Link.FilePHP+getData);
                }else {
                    Toast.makeText(getContext(),"GPS harap diaktifkan terlebih dahulu!", Toast.LENGTH_LONG).show();
                }
            }
        });

        return vupload;
    }

    private void getDataUpload(String Url){
        JsonObjectRequest jsonget = new JsonObjectRequest(Request.Method.GET, Url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // TODO Auto-generated method stub
                        try {
                            int sucses= response.getInt("success");
                            Log.i("Status", String.valueOf(sucses));
                            if (sucses==1){
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
                                    // list gmbar bl
                                    columnlist.add(colums);
                                }
                                Gson gson = new Gson();
                                Intent i = new Intent(getContext(), MapAllKos.class);
                                String jsonString = gson.toJson(columnlist);
                                i.putExtra("list", jsonString);
                                i.putExtra("idUser", String.valueOf(idUser));
                                startActivityForResult(i,3);
                            }else{
                                Toast.makeText(getContext(),"Data tidak ada!", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getContext(),"Check Koneksi Internet Anda", Toast.LENGTH_LONG).show();
                    /*tvstatus.setVisibility(View.VISIBLE);
                    tvstatus.setText("Check Koneksi Internet Anda");
                    prbstatus.setVisibility(View.GONE);*/
                } else if (error instanceof AuthFailureError) {
                    //TODO
                    Toast.makeText(getContext(),"AuthFailureError", Toast.LENGTH_LONG).show();
                    /*tvstatus.setVisibility(View.VISIBLE);
                    tvstatus.setText("AuthFailureError");
                    prbstatus.setVisibility(View.GONE);*/
                } else if (error instanceof ServerError) {
                    //TODO
                    Toast.makeText(getContext(),"Check ServerError", Toast.LENGTH_LONG).show();
                    /*tvstatus.setVisibility(View.VISIBLE);
                    tvstatus.setText("Check ServerError");
                    prbstatus.setVisibility(View.GONE);*/
                } else if (error instanceof NetworkError) {
                    //TODO
                    Toast.makeText(getContext(),"Check NetworkError", Toast.LENGTH_LONG).show();
                    /*tvstatus.setVisibility(View.VISIBLE);
                    tvstatus.setText("Check NetworkError");
                    prbstatus.setVisibility(View.GONE);*/
                } else if (error instanceof ParseError) {
                    //TODO
                    Toast.makeText(getContext(),"Check ParseError", Toast.LENGTH_LONG).show();
                    /*tvstatus.setVisibility(View.VISIBLE);
                    tvstatus.setText("Check ParseError");
                    prbstatus.setVisibility(View.GONE);*/
                }
            }
        }){
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new HashMap<String, String>();
                params.put("querysql", "ALL");
                return params;
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

    private void CheckGpsStatus(){
        locationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void changeFragmentListUploadKriteria(Fragment targetFragment, String idUserku, String sqlku){
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.FrmMainMenu, targetFragment)
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .setCustomAnimations(R.anim.blink, R.anim.fade_in)
                .commit();
        Bundle extras = new Bundle();
        extras.putString("idUser", idUserku);
        extras.putString("sql", sqlku);
        targetFragment.setArguments(extras);
    }

    private void changeFragmentListUploadUserKriteria(Fragment targetFragment, String idUser){
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.FrmMainMenu, targetFragment)
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .setCustomAnimations(R.anim.blink, R.anim.fade_in)
                .commit();
        Bundle extras = new Bundle();
        extras.putString("idUser", idUser);
        targetFragment.setArguments(extras);
    }
}
