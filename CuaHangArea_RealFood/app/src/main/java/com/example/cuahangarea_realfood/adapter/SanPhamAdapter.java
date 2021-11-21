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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cuahangarea_realfood.R;
import com.example.cuahangarea_realfood.screen.ThongTinSanPhamActivity;
import com.example.cuahangarea_realfood.SetOnLongClick;
import com.example.cuahangarea_realfood.model.SanPham;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.text.NumberFormat;
import java.util.ArrayList;


public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.MyViewHolder> implements Filterable {
    private Activity context;
    private int resource;
    private ArrayList<SanPham> arrayList;
    private ArrayList<SanPham> source;
    public SetOnLongClick setOnLongClick;

    public SetOnLongClick getSetOnLongClick() {
        return setOnLongClick;
    }

    public void setSetOnLongClick(SetOnLongClick setOnLongClick) {
        this.setOnLongClick = setOnLongClick;
    }

    StorageReference storageRef  = FirebaseStorage.getInstance().getReference();
    FirebaseAuth  auth= FirebaseAuth.getInstance();
    public SanPhamAdapter(Activity context, int resource, ArrayList<SanPham> arrayList) {
        this.context = context;
        this.resource = resource;
        this.arrayList = arrayList;
        source = arrayList;
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
        if (arrayList.size()>0)
        {
            SanPham sanPham = arrayList.get(position);
            holder.txtTenSanPham.setText(sanPham.getTenSanPham());
            NumberFormat format = NumberFormat.getCurrencyInstance();
            format.setMaximumFractionDigits(0);


            float parseFloat = Float.parseFloat(sanPham.getGia());
            String price =format.format(parseFloat);
            holder.txtGia.setText(price);
            storageRef.child("SanPham").child(auth.getUid()).child(sanPham.getIDSanPham()).child(sanPham.getImages().get(0)).
                    getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    try {
                        Glide.with(context)
                                .load(uri.toString())
                                .into(holder.imageView);
                        holder.progressBar.setVisibility(View.GONE);
                    }catch (Exception e)
                    {
                        Log.d("link",uri.toString());
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
                    Intent intent = new Intent(context, ThongTinSanPhamActivity.class);
                    Gson gson = new Gson();
                    String data = gson.toJson(sanPham);
                    intent.putExtra("sanPham", data);
                    context.startActivity(intent);
                }
            });
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

    //trả về số phần tử
    @Override
    public int getItemCount() {
        return arrayList.size();
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
                    ArrayList<SanPham> list = new ArrayList<>();
                    for (SanPham sanPham : source) {
                        if (sanPham.getTenSanPham().toLowerCase().contains(strSearch.toLowerCase())||
                                sanPham.getChiTietSanPham().toLowerCase().contains(strSearch.toLowerCase())||
                                sanPham.getGia().toLowerCase().contains(strSearch.toLowerCase())||
                                sanPham.getIDDanhMuc().toLowerCase().equals(strSearch.toLowerCase())||
                                sanPham.getIDLoai().toLowerCase().equals(strSearch.toLowerCase())) {
                            list.add(sanPham);
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
                arrayList = (ArrayList<SanPham>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    //Define RecylerVeiw Holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenSanPham;
        TextView txtGia;
        ImageView imageView;
        ProgressBar progressBar;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgAnh);
            txtTenSanPham = itemView.findViewById(R.id.txtTenSanPham);
            txtGia = itemView.findViewById(R.id.txtGia);
            progressBar = itemView.findViewById(R.id.progessbar);
        }
    }
}
