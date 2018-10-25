package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.chris.nearbykos2.R;

import java.util.ArrayList;

import model.ColBank;

import static android.app.Activity.RESULT_OK;

/**
 * Created by christian on 19/01/18.
 */

public class AdpBank extends RecyclerView.Adapter<AdpBank.ViewHolder> implements Filterable {
    private Context context;
    private ArrayList<ColBank> mArrayList;
    private ArrayList<ColBank> mFilteredList;

    public AdpBank(Context contextku, ArrayList<ColBank> arrayList) {
        context = contextku;
        mArrayList = arrayList;
        mFilteredList = arrayList;
    }

    @Override
    public AdpBank.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.col_kota, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.tv_kode.setText(mFilteredList.get(i).getKodeBank());
        viewHolder.tv_nama.setText(mFilteredList.get(i).getNamaBank());
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilteredList = mArrayList;
                } else {
                    ArrayList<ColBank> filteredList = new ArrayList<>();
                    for (ColBank entity : mArrayList) {
                        if (entity.getKodeBank().toLowerCase().contains(charString) ||
                                entity.getNamaBank().toLowerCase().contains(charString) ) {
                            filteredList.add(entity);
                        }
                    }
                    mFilteredList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<ColBank>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tv_kode,tv_nama;

        public ViewHolder(View view) {
            super(view);
            tv_kode = (TextView)view.findViewById(R.id.tv_kodekota);
            tv_nama = (TextView)view.findViewById(R.id.tv_namakota);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.putExtra("kodeBank", tv_kode.getText().toString());
            intent.putExtra("namaBank", tv_nama.getText().toString());
            ((Activity)context).setResult(RESULT_OK, intent);
            ((Activity)context).finish();
        }
    }
}
