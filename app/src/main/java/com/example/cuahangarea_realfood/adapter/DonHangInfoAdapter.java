package com.example.cuahangarea_realfood.adapter;

import android.app.Activity;
import android.net.Uri;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.developer.kalert.KAlertDialog;
import com.example.cuahangarea_realfood.Firebase_Manager;
import com.example.cuahangarea_realfood.R;

import com.example.cuahangarea_realfood.model.DonHangInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class DonHangInfoAdapter extends RecyclerView.Adapter<DonHangInfoAdapter.MyViewHolder> {
    private Activity context;
    private int resource;
    private ArrayList<DonHangInfo> donHangInfos;
    private Firebase_Manager firebase_manager = new Firebase_Manager();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private KAlertDialog kAlertDialog;
    private CheckBoxListener checkBoxListener;


    public DonHangInfoAdapter(Activity context, int resource, ArrayList<DonHangInfo> donHangInfos) {
        this.context = context;
        this.resource = resource;
        this.donHangInfos = donHangInfos;
    }

    private SparseBooleanArray booleanArray = new SparseBooleanArray();

    public SparseBooleanArray getBooleanArray() {
        return booleanArray;
    }

    public void setBooleanArray(SparseBooleanArray booleanArray) {
        this.booleanArray = booleanArray;
    }

    @NonNull
    @Override
    public DonHangInfoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout linearLayout = (LinearLayout) context.getLayoutInflater().inflate(viewType, parent, false);
        return new DonHangInfoAdapter.MyViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull DonHangInfoAdapter.MyViewHolder holder, int position) {
        DonHangInfo donHangInfo = donHangInfos.get(position);
        holder.tvTenSanPham.setText(donHangInfo.getSanPham().getTenSanPham());
        String gia = String.valueOf(Integer.valueOf(donHangInfo.getSanPham().getGia()));
        holder.tvGia.setText(gia);
        holder.tvSoLuong.setText("X "+ donHangInfo.getSoLuong());
        firebase_manager.storageRef.child("SanPham").child(donHangInfo.getSanPham().getIDCuaHang()).child(donHangInfo.getSanPham().getIDSanPham()).child(donHangInfo.getSanPham().getImages().get(0)).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Glide.with(context)
                        .load(task.getResult().toString())
                        .into(holder.ivSanPham);
                holder.pbLoadItemGioHang.setVisibility(View.GONE);
            }
        });

    }


    @Override
    public int getItemViewType(int position) {
        return resource;
    }

    @Override
    public int getItemCount() {
        return donHangInfos.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView  tvTenSanPham, tvGia,tvSoLuong;
        ImageView  ivSanPham;
        ProgressBar pbLoadItemGioHang;
        View.OnClickListener onClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSanPham = itemView.findViewById(R.id.ivSanPham);
            tvTenSanPham = itemView.findViewById(R.id.tvTenSanPham);
            tvGia = itemView.findViewById(R.id.tvGia);
            pbLoadItemGioHang = itemView.findViewById(R.id.pbLoadItemGioHang);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuong);
        }
    }

    public interface CheckBoxListener {
        void getGiaGioHang();
    }



}
