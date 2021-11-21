package com.example.cuahangarea_realfood.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.cuahangarea_realfood.TrangThai.TrangThaiThongBao;
import com.example.cuahangarea_realfood.model.DanhMuc;
import com.example.cuahangarea_realfood.model.ThongBao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ThongBaoAdapter extends RecyclerView.Adapter<ThongBaoAdapter.MyViewHolder> {
    private Activity context;
    private int resource;
    private ArrayList<ThongBao> arrayList;
    public SetOnLongClick setOnLongClick;

    public SetOnLongClick getSetOnLongClick() {
        return setOnLongClick;
    }

    public void setSetOnLongClick(SetOnLongClick setOnLongClick) {
        this.setOnLongClick = setOnLongClick;
    }

    Firebase_Manager firebase_manager = new Firebase_Manager();

    public ThongBaoAdapter(Activity context, int resource, ArrayList<ThongBao> arrayList) {
        this.context = context;
        this.resource = resource;
        this.arrayList = arrayList;
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
        ThongBao thongBao = arrayList.get(position);

        holder.txtTieuDe.setText(thongBao.getTieuDe());
        holder.txtNoiDung.setText(thongBao.getNoiDung());
        Log.d("Noti", thongBao.getNoiDung());
        if (thongBao.getTrangThaiThongBao() == TrangThaiThongBao.DaXem) {
            holder.linearLayout.setBackgroundColor(Color.WHITE);
        }
        if (!thongBao.getImage().isEmpty()) {
            Glide.with(context)
                    .load(thongBao.getImage())
                    .into(holder.imageView);
            holder.progressBar.setVisibility(View.GONE);
        }
        else {
            holder.imageView.setImageResource(R.drawable.tickfrontcolor);
            holder.progressBar.setVisibility(View.GONE);

        }

        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm dd/MM/yyyy");
        String strDate = formatter.format(thongBao.getDate());
        holder.txtThoiGian.setText(strDate);
        Date now = new Date();
        if (thongBao.getDate().getDate() == now.getDate()) {
            SimpleDateFormat formatters = new SimpleDateFormat("hh:mm");
            String strDates = formatters.format(thongBao.getDate());
            holder.txtThoiGian.setText(strDates);
        }


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

    //    public  String Time(Date dateStart,Date dateStop){
//
////HH converts hour in 24 hours format (0-23), day calculation
//        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
//
//        Date d1 = dateStart;
//        Date d2 = dateStop;
//
//        try {
//
//            long diff = d2.getTime() - d1.getTime();
//
//            long diffSeconds = diff / 1000 % 60;
//            long diffMinutes = diff / (60 * 1000) % 60;
//            long diffHours = diff / (60 * 60 * 1000) % 24;
//            long diffDays = diff / (24 * 60 * 60 * 1000);
//
//            System.out.print(diffDays + " days, ");
//            System.out.print(diffHours + " hours, ");
//            System.out.print(diffMinutes + " minutes, ");
//            System.out.print(diffSeconds + " seconds.");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
    //trả về số phần tử
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    //Define RecylerVeiw Holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtTieuDe;
        ImageView imageView;
        TextView txtNoiDung;
        TextView txtThoiGian;
        LinearLayout linearLayout;
        ProgressBar progressBar;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_noti);
            txtNoiDung = itemView.findViewById(R.id.txtNoiDung);
            txtTieuDe = itemView.findViewById(R.id.txtTieuDe);
            linearLayout = itemView.findViewById(R.id.lnLayout);
            txtThoiGian = itemView.findViewById(R.id.txtThoiGian);
            progressBar = itemView.findViewById(R.id.progessbar);
        }
    }
}
