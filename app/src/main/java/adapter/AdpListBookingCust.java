package adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

import control.AppController;
import control.Link;
import control.Utils;
import model.ColHomeBooking;

/**
 * Created by Chris on 26/06/2017.
 */

public class AdpListBookingCust extends ArrayAdapter<ColHomeBooking> {

    private List<ColHomeBooking> columnslist;
    private LayoutInflater vi;
    private int Resource;
    private AlertDialog alert;
    private int lastPosition = -1;
    private ViewHolder holder;
    private Activity parent;
    private Context context;
    private ListView lsvchoose;
    private static final int SEND_UPLOAD = 201;
    private String slasid;
    private String update = "updateBooking.php";
    private ImageView imgBukti;
    private TextView txtKet;
    private String url 		= Link.FileImage;

    public AdpListBookingCust(Context context, int resource, List<ColHomeBooking> objects) {
        super(context, resource,  objects);
        this.context = context;
        vi	=	(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource		= resource;
        columnslist		= objects;
    }

    @Override
    public View getView (final int position, final View convertView, final ViewGroup parent){
        View v	=	convertView;
        if (v == null){
            holder	=	new ViewHolder();
            v= vi.inflate(Resource, null);
            holder.ImgGmbrListKos	= 	 (ImageView)v.findViewById(R.id.ImgColGmbrBookongCust);
            holder.ImgSetuju	= 	 (ImageView)v.findViewById(R.id.ImgColSetujuBookingCust);
            holder.ImgTolak	= 	 (ImageView)v.findViewById(R.id.ImgColTolakBookingCust);
            holder.ImgDP	= 	 (TextView) v.findViewById(R.id.TvColCekDPBookingCust);
            holder.TvNamaUser	= 	 (TextView)v.findViewById(R.id.TvColNamaUserBookingCust);
            holder.TvNamaListKos	=	 (TextView)v.findViewById(R.id.TvColNamaKosBookingCust);
            holder.TvTelpUser	=	 (TextView)v.findViewById(R.id.TvColTelpUserBookingCust);
            holder.TvSurvey	=	 (TextView)v.findViewById(R.id.TvColTglSurveyBookingCust);
            holder.TvDP = (TextView)v.findViewById(R.id.TvColStatusDPBookingCust);
            v.setTag(holder);
        }else{
            holder 	= (ViewHolder)v.getTag();
        }

        String url 		= Link.FileImage+columnslist.get(position).getGambarKos();
        holder.TvNamaListKos.setText(columnslist.get(position).getNamaKos());
        holder.TvNamaUser.setText("Nama Penyewa: "+columnslist.get(position).getNamaUser());
        holder.TvTelpUser.setText("Telp Penyewa: "+columnslist.get(position).getTelpUser());
        holder.TvSurvey.setText("Tgl c/i: " +columnslist.get(position).getTglSurvey());
        holder.TvDP.setText(columnslist.get(position).getStatusDP().equals("S")?"Sudah DP":"Belum DP");

        if (holder.ImgGmbrListKos.getTag()==null||!holder.ImgGmbrListKos.getTag().equals(url)){
            Utils.GetImage(url, holder.ImgGmbrListKos, getContext());
            holder.ImgGmbrListKos.setTag(url);
        }

        /*if (columnslist.get(position).getStatusDP().equals("S")){
            holder.ImgDP.setVisibility(View.VISIBLE);
        }else{
            holder.ImgDP.setVisibility(View.INVISIBLE);
        }*/

        holder.ImgDP.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {//status=A
                dialogBox(columnslist.get(position).getFotoDP(), columnslist.get(position).getNamaBank(),
                        columnslist.get(position).getNoRek());
            }
        });

        holder.ImgSetuju.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {//status=A
                AlertDialog.Builder msMaintance = new AlertDialog.Builder(getContext());
                msMaintance.setCancelable(false);
                msMaintance.setMessage("Yakin akan diterima? ");
                msMaintance.setNegativeButton("Ya", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateData(Link.FilePHP+update, "A", columnslist.get(position).getIdUser(),
                                columnslist.get(position).getIdKos(), columnslist.get(position).getIdCust());
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

        holder.ImgTolak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {//status=D
                AlertDialog.Builder msMaintance = new AlertDialog.Builder(getContext());
                msMaintance.setCancelable(false);
                msMaintance.setMessage("Yakin akan ditolak? ");
                msMaintance.setNegativeButton("Ya", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateData(Link.FilePHP+update, "D", columnslist.get(position).getIdUser(),
                                columnslist.get(position).getIdKos(), columnslist.get(position).getIdCust());
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

    private void dialogBox(String image, String namaBank, String noRek){
        LayoutInflater li = LayoutInflater.from(this.context);
        View promptsView = li.inflate(R.layout.cek_dp, null);
        imgBukti = (ImageView) promptsView.findViewById(R.id.imgBuktiCekFotoDP);
        txtKet = (TextView)promptsView.findViewById(R.id.txtCekDpKeterangan);
        Utils.GetImage(url+image, imgBukti, this.context);
        imgBukti.setTag(url);
        txtKet.setText("Transfer ke "+namaBank+", dengan No Rek: "+noRek);
        AlertDialog.Builder msMaintance = new AlertDialog.Builder(this.context);
        msMaintance.setView(promptsView);
        msMaintance.setCancelable(false);
        msMaintance.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alert.dismiss();
            }
        });
        alert = msMaintance.create();
        alert.show();
    }

    private void updateData(String save, final String status, final Integer idUser, final Integer idKos, final Integer idCust){
        StringRequest simpan = new StringRequest(Request.Method.POST, save,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
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
                            e.getMessage();
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
                params.put("status", status);
                params.put("idKos", String.valueOf(idKos));
                params.put("idUser", String.valueOf(idUser));
                params.put("idCust", String.valueOf(idCust));
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
        private ImageView ImgGmbrListKos;
        private ImageView ImgSetuju;
        private ImageView ImgTolak;
        private TextView ImgDP;
        private TextView TvNamaListKos;
        private TextView TvNamaUser;
        private TextView TvTelpUser;
        private TextView TvSurvey;
        private TextView TvDP;
    }

}
