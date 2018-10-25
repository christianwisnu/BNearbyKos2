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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.example.chris.nearbykos2.AddRekeningCust;
import com.example.chris.nearbykos2.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import control.AppController;
import control.Link;
import model.ColCustRekening;

/**
 * Created by christian on 19/01/18.
 */

public class AdpCustRekening extends ArrayAdapter<ColCustRekening> {

    private List<ColCustRekening> columnslist;
    private LayoutInflater vi;
    private int Resource;
    private int lastPosition = -1;
    private ViewHolder holder;
    private Activity parent;
    private Context context;
    private ListView lsvchoose;
    private static final int SEND_UPLOAD = 201;
    private AlertDialog alert;
    private String FileDeteleted 		= "deleteRekCust.php";

    public AdpCustRekening(Context context, int resource, List<ColCustRekening> objects) {
        super(context, resource,  objects);
        this.context = context;
        vi	=	(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource		= resource;
        columnslist		= objects;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View v	=	convertView;
        if (v == null){
            holder	=	new ViewHolder();
            v= vi.inflate(Resource, null);
            holder.ImgDelete		=	 (ImageView)v.findViewById(R.id.TvColCustRekDelete);
            holder.ImgEdit			=	 (ImageView)v.findViewById(R.id.TvColCustRekEdit);
            holder.TvKodeBank	= 	 (TextView)v.findViewById(R.id.TvColCustRekKodeBank);
            holder.TvNamaBank	=	 (TextView)v.findViewById(R.id.TvColCustRekNamaBank);
            holder.TvNoRek	=	 (TextView)v.findViewById(R.id.TvColCustRekNoRekening);
            v.setTag(holder);
        }else{
            holder 	= (ViewHolder)v.getTag();
        }

        holder.ImgDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder msMaintance = new AlertDialog.Builder(getContext());
                msMaintance.setCancelable(false);
                msMaintance.setMessage("Rekening akan dihapus? ");
                msMaintance.setNegativeButton("Ya", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletedData(Link.FilePHP + FileDeteleted, position);
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

        holder.ImgEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AddRekeningCust.class);
                i.putExtra("Status", 2);
                i.putExtra("idCust", String.valueOf(columnslist.get(position).getKodeCust()));
                i.putExtra("line", columnslist.get(position).getLine());
                i.putExtra("kodeBank", columnslist.get(position).getKodeBank());
                i.putExtra("namaBank", columnslist.get(position).getNamaBank());
                i.putExtra("noRek", columnslist.get(position).getNoRek());
                getContext().startActivity(i);
            }
        });

        holder.TvKodeBank.setText("Kode Bank: "+columnslist.get(position).getKodeBank());
        holder.TvNamaBank.setText("Nama Bank: "+columnslist.get(position).getNamaBank());
        holder.TvNoRek.setText("No Rek: "+columnslist.get(position).getNoRek());
        return v;
    }

    private void deletedData(String save, final int position){
        StringRequest register = new StringRequest(Request.Method.POST, save,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        VolleyLog.d("Respone", response.toString());
                        try {
                            JSONObject jsonrespon = new JSONObject(response);
                            int Sucsess = jsonrespon.getInt("success");
                            if (Sucsess ==1 ){
                                columnslist.remove(position);
                                notifyDataSetChanged();
                            }else{
                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
            }
        }){
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new HashMap<String, String>();
                params.put("kodeCust", String.valueOf(columnslist.get(position).getKodeCust()));
                params.put("kodeBank", columnslist.get(position).getKodeBank());
                params.put("line", String.valueOf(columnslist.get(position).getLine()));
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(register);
    }

    static class ViewHolder{
        private ImageView ImgDelete;
        private ImageView ImgEdit;
        private TextView TvKodeBank;
        private TextView TvNamaBank;
        private TextView TvNoRek;
    }
}
