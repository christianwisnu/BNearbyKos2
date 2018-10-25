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

import com.example.chris.nearbykos2.BayarDP;
import com.example.chris.nearbykos2.R;

import java.util.List;

import control.Link;
import control.Utils;
import model.ColHomeBooking;

/**
 * Created by Chris on 25/06/2017.
 */

public class AdpListBookingUser extends ArrayAdapter<ColHomeBooking>  {

    private List<ColHomeBooking> columnslist;
    private LayoutInflater vi;
    private int Resource;
    private int lastPosition = -1;
    private ViewHolder holder;
    private Activity parent;
    private Context context;
    private ListView lsvchoose;
    private static final int SEND_UPLOAD = 201;
    private String slasid;



    public AdpListBookingUser(Context context, int resource, List<ColHomeBooking> objects) {
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
            holder.ImgGmbrListKos	= 	 (ImageView)v.findViewById(R.id.ImgColListBookingUserKos);
            holder.TvAlamatListKos	= 	 (TextView)v.findViewById(R.id.TvColListBookingUserKosAlamat);
            holder.TvNamaListKos	=	 (TextView)v.findViewById(R.id.TvColListBookingUserKosNama);
            holder.TvStatusListKos	=	 (TextView)v.findViewById(R.id.TvColListBookingUserKosStatus);
            holder.TvDP = (TextView)v.findViewById(R.id.TvColBookingUserDP);
            v.setTag(holder);
        }else{
            holder 	= (ViewHolder)v.getTag();
        }

        String url 		= Link.FileImage+columnslist.get(position).getGambarKos();
        holder.TvNamaListKos.setText(columnslist.get(position).getNamaKos());
        holder.TvAlamatListKos.setText(columnslist.get(position).getAlamatKos());
        String status ;
        if(columnslist.get(position).getStatusBooking().equals("N")){
            status="Belum terkonfirmasi";
            holder.TvStatusListKos.setText(status);
            holder.TvStatusListKos.setTextColor(Color.MAGENTA);
        }else if(columnslist.get(position).getStatusBooking().equals("D")){
            status="Status ditolak";
            holder.TvStatusListKos.setText(status);
            holder.TvStatusListKos.setTextColor(Color.RED);
        }else{
            status = "Status diterima";
            holder.TvStatusListKos.setText(status);
            holder.TvStatusListKos.setTextColor(Color.BLUE);
        }

        if (holder.ImgGmbrListKos.getTag()==null||!holder.ImgGmbrListKos.getTag().equals(url)){
            Utils.GetImage(url, holder.ImgGmbrListKos, getContext());
            holder.ImgGmbrListKos.setTag(url);
        }

        holder.TvDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), BayarDP.class);
                i.putExtra("idBooking", columnslist.get(position).getIdBooking());
                i.putExtra("idCust", columnslist.get(position).getIdCust());
                getContext().startActivity(i);
            }
        });
        return v;
    }

    static class ViewHolder{
        private ImageView ImgGmbrListKos;
        private TextView TvNamaListKos;
        private TextView TvAlamatListKos;
        private TextView TvStatusListKos;
        private TextView TvDP;
    }


}
