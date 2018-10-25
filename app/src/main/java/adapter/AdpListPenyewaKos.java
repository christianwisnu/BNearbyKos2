package adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.example.chris.nearbykos2.AddHomeKos;
import com.example.chris.nearbykos2.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import control.AppController;
import control.Link;
import model.ColTransaksi;

/**
 * Created by christian on 04/01/18.
 */

public class AdpListPenyewaKos extends ArrayAdapter<ColTransaksi> {

    private List<ColTransaksi> columnslist;
    private LayoutInflater vi;
    private AlertDialog alert;
    private int Resource;
    private int lastPosition = -1;
    private ViewHolder holder;
    private Activity parent;
    private Context context;
    private ListView lsvchoose;
    private static final int SEND_UPLOAD = 201;
    private String slasid;
    private String update = "updateSelesaiBooking.php";

    public AdpListPenyewaKos(Context context, int resource, List<ColTransaksi> objects) {
        super(context, resource,  objects);
        this.context = context;
        vi	=	(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource		= resource;
        columnslist		= objects;
    }

    @Override
    public View getView (final int position, View convertView, final ViewGroup parent){
        View v	=	convertView;
        if (v == null){
            holder	=	new ViewHolder();
            v= vi.inflate(Resource, null);
            holder.ImgKeluarKos	= 	 (ImageView) v.findViewById(R.id.ImgColPenyewaKeluar);
            holder.TvNamaPenyewa	= 	 (TextView)v.findViewById(R.id.TvColListPenyewaKosNama);
            holder.TvTelpPenyewa	=	 (TextView)v.findViewById(R.id.TvColListPenyewaKosTelp);
            holder.TvTglMasukPenyewa	=	 (TextView)v.findViewById(R.id.TvColListPenyewaKosTglMasuk);
            holder.TvTglKeluarPenyewa	=	 (TextView)v.findViewById(R.id.TvColListPenyewaKosTglKeluar);
            holder.TvStatusPenyewa	=	 (TextView)v.findViewById(R.id.TvColListPenyewaKosStatus);
            v.setTag(holder);
        }else{
            holder 	= (ViewHolder)v.getTag();
        }

        holder.TvNamaPenyewa.setText(columnslist.get(position).getNamaPenyewa());
        holder.TvTelpPenyewa.setText("Telp: "+columnslist.get(position).getTelpPenyewa());
        holder.TvTglMasukPenyewa.setText("Tgl Masuk: "+columnslist.get(position).getTglMasuk());
        holder.TvTglKeluarPenyewa.setText("Tgl Keluar: "+columnslist.get(position).getTglKeluar());

        String status ;
        if(columnslist.get(position).getStatus().equals("A")){
            status="ADA";
            holder.TvStatusPenyewa.setText(status);
            holder.TvStatusPenyewa.setTextColor(Color.BLUE);
            holder.ImgKeluarKos.setVisibility(View.VISIBLE);
            holder.TvTglKeluarPenyewa.setVisibility(View.INVISIBLE);
        }else if(columnslist.get(position).getStatus().equals("D")){
            status="KELUAR";
            holder.TvStatusPenyewa.setText(status);
            holder.TvStatusPenyewa.setTextColor(Color.RED);
            holder.ImgKeluarKos.setVisibility(View.INVISIBLE);
            holder.TvTglKeluarPenyewa.setVisibility(View.VISIBLE);
        }else{
            status = "";
            holder.TvStatusPenyewa.setText(status);
            holder.TvStatusPenyewa.setTextColor(Color.RED);
            holder.ImgKeluarKos.setVisibility(View.INVISIBLE);
        }

        holder.ImgKeluarKos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {//status=A
                AlertDialog.Builder msMaintance = new AlertDialog.Builder(getContext());
                msMaintance.setCancelable(false);
                msMaintance.setMessage("Yakin Penyewa ini akan keluar? ");
                msMaintance.setNegativeButton("Ya", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateData(Link.FilePHP+update, String.valueOf(columnslist.get(position).getIdBooking()));
                    }
                });

                msMaintance.setPositiveButton("Tidak", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alert.dismiss();
                    }
                });
                alert	=msMaintance.create();
                alert.show();

            }
        });
        return v;
    }

    private void updateData(String save, final String idBooking){
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
                            if (Sucsess >0 ){
                                Toast.makeText(getContext(),
                                        "Sukses!", Toast.LENGTH_LONG)
                                        .show();
                                Intent i = new Intent(getContext(), AddHomeKos.class);
                                i.putExtra("Status", 3);
                                getContext().startActivity(i);
                            }else{
                                Toast.makeText(getContext(),
                                        "update gagal!", Toast.LENGTH_SHORT)
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
                params.put("idBooking", String.valueOf(idBooking));
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

    static class ViewHolder{
        private ImageView ImgKeluarKos;
        private TextView TvNamaPenyewa;
        private TextView TvTelpPenyewa;
        private TextView TvTglMasukPenyewa;
        private TextView TvTglKeluarPenyewa;
        private TextView TvStatusPenyewa;
    }
}
