package fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chris.nearbykos2.R;

/**
 * Created by christian on 18/01/18.
 */

public class FAwalCust extends Fragment {

    private View vupload;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vupload     = inflater.inflate(R.layout.awal_cust,container,false);
        return vupload;
    }
}
