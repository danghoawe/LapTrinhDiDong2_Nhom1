package com.example.cuahangarea_realfood.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cuahangarea_realfood.Firebase_Manager;
import com.example.cuahangarea_realfood.R;
import com.example.cuahangarea_realfood.SetOnLongClick;
import com.example.cuahangarea_realfood.model.DanhMuc;
import com.example.cuahangarea_realfood.model.DonHang;
import com.example.cuahangarea_realfood.model.SanPham;
import com.example.cuahangarea_realfood.model.Voucher;
import com.example.cuahangarea_realfood.screen.ThongTinMaGiamGiaActivity;
import com.example.cuahangarea_realfood.screen.ThongTinSanPhamActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.ArrayList;


public class MaGiamGiaAdapter extends RecyclerView.Adapter<MaGiamGiaAdapter.MyViewHolder> implements Filterable {
    private Activity context;
    private int resource;
    private ArrayList<Voucher> arrayList;
    private ArrayList<Voucher> source;
    public SetOnLongClick setOnLongClick;
    Firebase_Manager firebase_manager = new Firebase_Manager();
    public SetOnLongClick getSetOnLongClick() {
        return setOnLongClick;
    }

    public void setSetOnLongClick(SetOnLongClick setOnLongClick) {
        this.setOnLongClick = setOnLongClick;
    }

    StorageReference storageRef  = FirebaseStorage.getInstance().getReference();
    public MaGiamGiaAdapter(Activity context, int resource, ArrayList<Voucher> arrayList) {
        this.context = context;
        this.resource = resource;
        this.arrayList = arrayList;
        source =arrayList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        CardView cardView = (CardView) layoutInflater.inflate(viewType, parent, false);
        return new MyViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Voucher voucher = arrayList.get(position);
        holder.txtMaGiamGia.setText(voucher.getMaGiamGia());
        holder.txtHanSuDung.setText(voucher.getHanSuDung().toString());
        holder.txtSoPhanTramGiam.setText(voucher.getPhanTramGiam()+"%");
        holder.txtSoTienGiam.setText(voucher.getGiaGiam()+"VND");
        firebase_manager.mDatabase.child("SanPham").child(voucher.getSanPham().getIDSanPham()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    SanPham sanPham = snapshot.getValue(SanPham.class);
                    holder.txtTenSanPham.setText(sanPham.getTenSanPham());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (voucher.getPhanTramGiam()!=0)
        {
            holder.txtSoPhanTramGiam.setVisibility(View.VISIBLE);
            holder.txtSoTienGiam.setVisibility(View.GONE);

        }
        else {
            holder.txtSoTienGiam.setVisibility(View.VISIBLE);
            holder.txtSoPhanTramGiam.setVisibility(View.GONE);

        }
        holder.txtSoTienGiam.setText(voucher.getGiaGiam()+"VND");
        holder.txtSoPhanTramGiam.setText(voucher.getPhanTramGiam()+"%");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ThongTinMaGiamGiaActivity.class);
                Gson gson = new Gson();
                String data = gson.toJson(voucher);
                intent.putExtra("voucher", data);
                context.startActivity(intent);
            }
        });
    }

    //Hàm để get layout type
    @Override
    public int getItemViewType(int position) {
        //1 list có 2 view
//        if(position%2==0)
//        {
//            ID layout A
//        }
//        else
//        {
//            ID layout B
//        }
        return resource;
    }

    //trả về số phần tử
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    //Define RecylerVeiw Holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtMaGiamGia;
        TextView txtSoTienGiam;
        TextView txtSoPhanTramGiam;
        TextView txtHanSuDung;
        TextView txtTenSanPham;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtSoTienGiam = itemView.findViewById(R.id.txtSoTienGiam);
            txtMaGiamGia = itemView.findViewById(R.id.txtMaGiamGia);
            txtSoPhanTramGiam = itemView.findViewById(R.id.txtSoPhanTramGiam);
            txtHanSuDung = itemView.findViewById(R.id.txtHanSuDung);
            txtTenSanPham = itemView.findViewById(R.id.txtTenSanPham);

        }
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()) {
                    arrayList = source;
                } else {
                    ArrayList<Voucher> list = new ArrayList<>();
                    for (Voucher voucher: source) {
                        if (voucher.getMaGiamGia().toString().equals(strSearch)||voucher.getSanPham().getTenSanPham().contains(strSearch)) {
                            list.add(voucher);
                        }
                    }
                    arrayList = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = arrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                arrayList = (ArrayList<Voucher>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}
