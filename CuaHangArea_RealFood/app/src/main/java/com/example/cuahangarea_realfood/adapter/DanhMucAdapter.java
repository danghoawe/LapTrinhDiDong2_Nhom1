package com.example.cuahangarea_realfood.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.example.cuahangarea_realfood.R;
import com.example.cuahangarea_realfood.SetOnClick;
import com.example.cuahangarea_realfood.SetOnLongClick;
import com.example.cuahangarea_realfood.model.DanhMuc;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.ArrayList;


public class DanhMucAdapter extends RecyclerView.Adapter<DanhMucAdapter.MyViewHolder> {
    private Activity context;
    private int resource;
    private ArrayList<DanhMuc> arrayList;
    public SetOnLongClick setOnLongClick;
    public SetOnLongClick getSetOnLongClick() {
        return setOnLongClick;
    }
    public SetOnClick setOnClick;

    public SetOnClick getSetOnClick() {
        return setOnClick;
    }

    public void setSetOnClick(SetOnClick setOnClick) {
        this.setOnClick = setOnClick;
    }

    public void setSetOnLongClick(SetOnLongClick setOnLongClick) {
        this.setOnLongClick = setOnLongClick;
    }

    StorageReference storageRef  = FirebaseStorage.getInstance().getReference();
    public DanhMucAdapter(Activity context, int resource, ArrayList<DanhMuc> arrayList) {
        this.context = context;
        this.resource = resource;
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        LinearLayout cardView = (LinearLayout) layoutInflater.inflate(viewType, parent, false);
        return new MyViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DanhMuc danhMuc = arrayList.get(position);
        holder.txtDanhMuc.setText(danhMuc.getTenDanhMuc());
        storageRef.child("DanhMuc").child(danhMuc.getIDDanhMuc()).child("image").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                try {
                    Glide.with(context)
                            .load(task.getResult().toString())
                            .into(holder.imageView);
                    holder.progressBar.setVisibility(View.GONE);
                    Log.d("link",task.getResult().toString());
                }catch (Exception e)
                {
                    Log.d("DanhMuc: ",e.getMessage());
                }

            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (setOnLongClick!=null)
                {
                    setOnLongClick.onLongClick(position);
                }
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setOnClick!=null){
                    setOnClick.onClick(position);
                }
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
        TextView txtDanhMuc;
        ImageView imageView;
        ProgressBar progressBar;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgAnh);
            txtDanhMuc = itemView.findViewById(R.id.txtDanhMuc);
            progressBar = itemView.findViewById(R.id.progessbar);
        }
    }
}
