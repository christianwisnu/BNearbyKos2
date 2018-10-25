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
 * Created by Chris on 25/06/2017.
 */

public class AdpListImageKos extends ArrayAdapter<ColHomeDetail> {

    private List<ColHomeDetail> columnslist;
    private LayoutInflater vi;
    private int Resource;
    private int lastPosition = -1;
    private ViewHolder holder;
    private Activity parent;
    private Context context;
    private ListView lsvchoose;
    private static final int SEND_UPLOAD = 201;

    public AdpListImageKos(Context context, int resource, List<ColHomeDetail> objects) {
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
            holder.ImgGmbrListKos	= 	 (ImageView)v.findViewById(R.id.ImgColListImageGmbarKos);
            holder.TvNamaListKos	=	 (TextView)v.findViewById(R.id.TvColListImageNamaKos);
            v.setTag(holder);
        }else{
            holder 	= (ViewHolder)v.getTag();
        }

        String url 		= Link.FileImage+columnslist.get(position).getGambar();
        holder.TvNamaListKos.setText(columnslist.get(position).getNamaKos());
        if (holder.ImgGmbrListKos.getTag()==null||!holder.ImgGmbrListKos.getTag().equals(url)){
            Utils.GetImage(url, holder.ImgGmbrListKos, getContext());
            holder.ImgGmbrListKos.setTag(url);
        }
        return v;
    }

    static class ViewHolder{
        private ImageView ImgGmbrListKos;
        private TextView TvNamaListKos;
    }
}
