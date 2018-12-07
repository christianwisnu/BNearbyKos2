package control;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;

import com.example.chris.nearbykos2.MapAllKos;
import com.example.chris.nearbykos2.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

/**
 * Created by christian on 20/07/17.
 */

public class CustomClusterRenderer extends DefaultClusterRenderer<MapAllKos.StringClusterItem> {

    private final IconGenerator mClusterIconGenerator;
    private final Context mContext;

    public CustomClusterRenderer(Context context, GoogleMap map,
                                 ClusterManager<MapAllKos.StringClusterItem> clusterManager) {
        super(context, map, clusterManager);

        mContext = context;
        mClusterIconGenerator = new IconGenerator(mContext.getApplicationContext());
    }

    @Override
    protected void onBeforeClusterItemRendered(MapAllKos.StringClusterItem item,
                                                         MarkerOptions markerOptions) {
        //EDIT DULU SEUKURAN home
        final BitmapDescriptor markerDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.home);
        markerOptions.icon(markerDescriptor).snippet(item.getTitle());
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<MapAllKos.StringClusterItem> cluster,
                                                     MarkerOptions markerOptions) {

        mClusterIconGenerator.setBackground(
                ContextCompat.getDrawable(mContext, R.drawable.background_circle));
        mClusterIconGenerator.setTextAppearance(R.style.AppTheme_WhiteTextAppearance);
        final Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
    }
}