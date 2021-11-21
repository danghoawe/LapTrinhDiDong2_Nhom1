package com.example.cuahangarea_realfood.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.cuahangarea_realfood.R;
import com.example.cuahangarea_realfood.model.NganHang;
import com.example.cuahangarea_realfood.model.SanPham;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import gr.escsoft.michaelprimez.searchablespinner.interfaces.ISpinnerSelectedView;

public class NganHangAdapter extends ArrayAdapter<NganHang>  implements Filterable, ISpinnerSelectedView {
    ArrayList<NganHang> objects ;
    ArrayList<NganHang> source ;
    Context context;
    int resource;

    public NganHangAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull ArrayList<NganHang> objects) {
        super(context, resource, textViewResourceId, objects);
        this.objects = objects;
        this.context = context;
        this.resource = resource;
        this.source = objects;
    }

    @Nullable
    @Override
    public NganHang getItem(int position) {
        return objects.get(position);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initVeiw(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable  View convertView, @NonNull  ViewGroup parent) {
        return initVeiw(position, convertView, parent);
    }
    private View initVeiw(int position,   View convertView,   ViewGroup parent)
    {
        convertView  = LayoutInflater.from(context).inflate(resource,null);
        NganHang nganHang = objects.get(position);

        TextView txtTenNH = convertView.findViewById(R.id.textView);
        ImageView imgLogo = convertView.findViewById(R.id.imgLogo);

        txtTenNH.setText(nganHang.getName());
        Glide.with(context)
                .load(nganHang.getLogo())
                .into(imgLogo);

        return convertView;

    }



    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()) {
                    objects = source;
                } else {
                    ArrayList<NganHang> list = new ArrayList<>();
                    for (NganHang nganHang : source) {
                        if (nganHang.getName().toLowerCase().contains(strSearch.toLowerCase())) {
                            list.add(nganHang);
                        }
                    }
                    objects = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = objects;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                objects = (ArrayList<NganHang>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public View getNoSelectionView() {
    return null;
    }

    @Override
    public View getSelectedView(int position) {
        View  convertView  = LayoutInflater.from(context).inflate(resource,null);
        NganHang nganHang = objects.get(position);

        TextView txtTenNH = convertView.findViewById(R.id.textView);
        ImageView imgLogo = convertView.findViewById(R.id.imgLogo);

        txtTenNH.setText(nganHang.getName());
        try {
            Glide.with(context)
                    .load(nganHang.getLogo())
                    .into(imgLogo);
        }catch (Exception e)
        {
            Log.d("ERROr",e.getMessage());
        }
        return convertView;
    }
}
