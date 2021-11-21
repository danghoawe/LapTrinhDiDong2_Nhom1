package com.example.cuahangarea_realfood.adapter;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.cuahangarea_realfood.Firebase_Manager;
import com.example.cuahangarea_realfood.R;
import com.example.cuahangarea_realfood.Validate;
import com.example.cuahangarea_realfood.model.DanhGia;
import com.example.cuahangarea_realfood.model.KhachHang;
import com.example.cuahangarea_realfood.model.SanPham;
import com.example.cuahangarea_realfood.screen.ThongTinDonHangActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tapadoo.alerter.Alerter;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class DanhGiaSanPhamAdapter extends RecyclerView.Adapter<DanhGiaSanPhamAdapter.MyViewHolder> {
    Activity context;
    int resource;
    ArrayList<DanhGia> danhGias;

    public ArrayList<DanhGia> getDanhGias() {
        return danhGias;
    }

    public void setDanhGias(ArrayList<DanhGia> danhGias) {
        this.danhGias = danhGias;
        notifyDataSetChanged();
    }

    Firebase_Manager firebase_manager = new Firebase_Manager();
    Validate validate = new Validate();
    public DanhGiaSanPhamAdapter(Activity context, int resource, ArrayList<DanhGia> danhGias) {
        this.context = context;
        this.resource = resource;
        this.danhGias = danhGias;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView linearLayout = (CardView) context.getLayoutInflater().inflate(viewType, parent, false);
        return new MyViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull DanhGiaSanPhamAdapter.MyViewHolder holder, int position) {
        DanhGia danhGia = danhGias.get(position);

        if (danhGia == null) {
            return;
        }
        holder.tvDanhGia.setText(danhGia.getNoiDung());
        String date = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(danhGia.getNgayDanhGia());
        holder.tvThoiGian.setText(date);
        holder.ratingBar.setRating(danhGia.getRating());
        if (danhGia.getNoiDungShopTraLoi().isEmpty())
        {
            holder.lnTraLoi.setVisibility(View.GONE);
            holder.lnSend.setVisibility(View.VISIBLE);
        }
        else {
            holder.lnTraLoi.setVisibility(View.VISIBLE);
            holder.lnSend.setVisibility(View.GONE);


            String date2 = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(danhGia.getNgayShopTraLoi());
            holder.tvThoiGianTraLoi.setText(date2);
            holder.tvTraLoi.setText(danhGia.getNoiDungShopTraLoi());
        }
        firebase_manager.mDatabase.child("KhachHang").child(danhGia.getIDKhachHang()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                KhachHang khachHang = snapshot.getValue(KhachHang.class);
                holder.tvTenKhachHang.setText(khachHang.getTenKhachHang());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        firebase_manager.mDatabase.child("SanPham").child(danhGia.getIDSanPham()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SanPham sanPham = snapshot.getValue(SanPham.class);
                if (sanPham!=null)
                {
                    holder.tvTenSanPham.setText(sanPham.getTenSanPham());
                    holder.tvGia.setText(sanPham.getGia()+" VND");
                    firebase_manager. storageRef.child("SanPham").child(firebase_manager.auth.getUid()).child(sanPham.getIDSanPham()).child(sanPham.getImages().get(0)).
                            getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            try {
                                Glide.with(context)
                                        .load(uri.toString())
                                        .into(holder.ivAnhSanPham);
                            }catch (Exception e)
                            {
                                Log.d("link",uri.toString());
                            }
                        }
                    });
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        firebase_manager.storageRef.child("KhachHang").child(danhGia.getIDKhachHang()).child("AvatarKhachHang").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    Glide.with(context).load(uri.toString()).into(holder.ivAvatar);
                }catch (Exception e)
                {

                }

            }
        });
        holder.imageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate.isBlank(holder.edtTraLoi))
                {
                    DanhGia temp = danhGia;
                    temp.setNoiDungShopTraLoi(holder.edtTraLoi.getText().toString());
                    temp.setNgayShopTraLoi(new Date());
                    danhGias.set(position,temp);
                    notifyDataSetChanged();
                    firebase_manager.mDatabase.child("DanhGia").child(temp.getIDDanhGia()).setValue(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull  Task<Void> task) {
                            Alerter.create(context)
                                    .setTitle("Thông báo")
                                    .setText("Bạn đã trả lời đánh giá của: KH"+danhGia.getIDKhachHang() +" về sản phẩm #"+ danhGia.getIDSanPham() )
                                    .setBackgroundColorRes(R.color.success_stroke_color) // or setBackgroundColorInt(Color.CYAN)
                                    .show();
                        }
                    }) ;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return resource;
    }

    @Override
    public int getItemCount() {
        return danhGias.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenKhachHang, tvThoiGian, tvDanhGia,tvTenSanPham,tvGia,tvTraLoi,tvThoiGianTraLoi;
        RatingBar ratingBar;
        ImageView ivAvatar,ivAnhSanPham,imageSend;
        EditText edtTraLoi;
        LinearLayout lnTraLoi,lnSend;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenKhachHang = itemView.findViewById(R.id.tvTenKhachHang);
            tvThoiGian = itemView.findViewById(R.id.tvThoiGian);
            tvDanhGia = itemView.findViewById(R.id.tvDanhGia);
            ivAvatar = itemView.findViewById(R.id.civAvatar);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            ivAnhSanPham = itemView.findViewById(R.id.ivSanPham);
            tvTenSanPham = itemView.findViewById(R.id.tvTenSanPham);
            tvGia = itemView.findViewById(R.id.tvGia);
            edtTraLoi = itemView.findViewById(R.id.edtTraloi);
            imageSend = itemView.findViewById(R.id.imageSend);
            lnSend = itemView.findViewById(R.id.lnTralois);
            lnTraLoi = itemView.findViewById(R.id.lnTraloi);
            tvTraLoi = itemView.findViewById(R.id.tvShopTraLoi);
            tvThoiGianTraLoi = itemView.findViewById(R.id.tvThoiGianShopTraLoi);
        }
    }
}