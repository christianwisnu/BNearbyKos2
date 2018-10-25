package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.chris.nearbykos2.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import control.Link;
import control.Utils;
import model.ColHomeDetail;

/**
 * Created by Chris on 21/06/2017.
 */

public class AdpListUserKos extends ArrayAdapter<ColHomeDetail> {

    private List<ColHomeDetail> columnslist;
    private LayoutInflater vi;
    private int Resource;
    private int lastPosition = -1;
    private ViewHolder holder;
    private Activity parent;
    private Context context;
    private ListView lsvchoose;
    private static final int SEND_UPLOAD = 201;

    public AdpListUserKos(Context context, int resource, List<ColHomeDetail> objects) {
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
            holder.ImgGmbrListKos	= 	 (ImageView)v.findViewById(R.id.ImgColListKos);
            holder.TvAlamatListKos	= 	 (TextView)v.findViewById(R.id.TvColListKosAlamat);
            holder.TvNamaListKos	=	 (TextView)v.findViewById(R.id.TvColListKosNama);
            holder.TvStatusListKos	=	 (TextView)v.findViewById(R.id.TvColListKosStatus);
            holder.TvHargaListKos	=	 (TextView)v.findViewById(R.id.TvColListKosHarga);
            v.setTag(holder);
        }else{
            holder 	= (ViewHolder)v.getTag();
        }

        NumberFormat rupiah	= NumberFormat.getNumberInstance(new Locale("in", "ID"));
        String url 		= Link.FileImage+columnslist.get(position).getGambar();
        holder.hargaListKos = columnslist.get(position).getHarga();
        holder.TvNamaListKos.setText(columnslist.get(position).getNamaKos());
        holder.TvAlamatListKos.setText(columnslist.get(position).getAlamat());
        holder.TvStatusListKos.setText(columnslist.get(position).getId_sewaStatus()==1?"Man Only":
            columnslist.get(position).getId_sewaStatus()==2?"Women Only":"All");
        holder.TvHargaListKos.setText("Rp. "+ rupiah.format(holder.hargaListKos));
        if (holder.ImgGmbrListKos.getTag()==null||!holder.ImgGmbrListKos.getTag().equals(url)){
            Utils.GetImage(url, holder.ImgGmbrListKos, getContext());
            holder.ImgGmbrListKos.setTag(url);
        }
        return v;
    }

    static class ViewHolder{
        private ImageView ImgGmbrListKos;
        private TextView TvNamaListKos;
        private TextView TvAlamatListKos;
        private TextView TvStatusListKos;
        private TextView TvHargaListKos;
        private double hargaListKos;
    }
}
