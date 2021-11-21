package com.example.cuahangarea_realfood.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cuahangarea_realfood.Firebase_Manager;
import com.example.cuahangarea_realfood.R;
import com.example.cuahangarea_realfood.SetOnLongClick;
import com.example.cuahangarea_realfood.TrangThai.TrangThaiDonHang;
import com.example.cuahangarea_realfood.model.DonHang;
import com.example.cuahangarea_realfood.model.DonHangInfo;
import com.example.cuahangarea_realfood.model.KhachHang;
import com.example.cuahangarea_realfood.model.Shipper;
import com.example.cuahangarea_realfood.screen.ThongTinDonHangActivity;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import karpuzoglu.enes.com.fastdialog.Animations;
import karpuzoglu.enes.com.fastdialog.DismissListener;
import karpuzoglu.enes.com.fastdialog.FastDialog;
import karpuzoglu.enes.com.fastdialog.FastDialogBuilder;
import karpuzoglu.enes.com.fastdialog.Positions;
import karpuzoglu.enes.com.fastdialog.PositiveClick;
import karpuzoglu.enes.com.fastdialog.Type;

public class DonHang_BepAdapter extends RecyclerView.Adapter<DonHang_BepAdapter.MyViewHolder> implements Filterable {
    private Activity context;
    private int resource;
    private ArrayList<DonHang> arrayList = null;
    ArrayList<DonHang> source;
    public SetOnLongClick setOnLongClick;
    ArrayList<Shipper>shippers = new ArrayList<>();
    ShipperAdapter shipperAdapter ;

    Firebase_Manager firebase_manager = new Firebase_Manager();
    public SetOnLongClick getSetOnLongClick() {
        return setOnLongClick;
    }
    public void setSetOnLongClick(SetOnLongClick setOnLongClick) {
        this.setOnLongClick = setOnLongClick;
    }

    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    public DonHang_BepAdapter(Activity context, int resource, ArrayList<DonHang> arrayList) {
        this.context = context;
        this.resource = resource;
        this.arrayList = arrayList;
        this.source = arrayList;
    }


    @NonNull
    @Override
    public DonHang_BepAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        CardView cardView = (CardView) layoutInflater.inflate(viewType, parent, false);
        return new DonHang_BepAdapter.MyViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull DonHang_BepAdapter.MyViewHolder holder, int position) {
        if (arrayList.get(position)==null)
        {
            return;
        }
        DonHang donHang = arrayList.get(position);
        holder.txtID.setText(donHang.getIDDonHang().substring(0, 9));
        holder.txtTrangThaiDonHang.setText(firebase_manager.GetStringTrangThaiDonHang(donHang.getTrangThai()));
        holder.txtTongTien.setText(donHang.getTongTien() + "");
        holder.txtDiaChi.setText(donHang.getDiaChi() + "");
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm dd/MM/yyyy");
        String strDate = formatter.format(donHang.getNgayTao());
        holder.txtTime.setText(strDate);
        holder.txtGhiChu.setText(donHang.getGhiChu_KhachHang()+"");
        LoadButton(holder, donHang.getTrangThai());
        firebase_manager.mDatabase.child("KhachHang").child(donHang.getIDKhachHang()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                KhachHang khachHang = snapshot.getValue(KhachHang.class);
                holder.txtTenKhach.setText(khachHang.getTenKhachHang());
                holder.txtDiaChi.setText(khachHang.getDiaChi());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.txtSoDienThoai.setText(donHang.getSoDienThoai());

        firebase_manager.mDatabase.child("DonHangInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.txtSanPham.setText("");
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DonHangInfo donHangInfo = dataSnapshot.getValue(DonHangInfo.class);
                    if (donHang.getTrangThai() != TrangThaiDonHang.Shipper_GiaoThanhCong) {
                        if (donHangInfo.getIDDonHang().equals(donHang.getIDDonHang())) {
                            holder.txtSanPham.setText(holder.txtSanPham.getText() + donHangInfo.getSanPham().getTenSanPham() + "(" + donHangInfo.getSoLuong() + ")" + ", ");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.txtXemChiTiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DonHang temp = donHang;
                Intent intent = new Intent(context, ThongTinDonHangActivity.class);
                Gson gson = new Gson();
                String data = gson.toJson(donHang);
                intent.putExtra("donhang", data);
                context.startActivity(intent);
            }
        });

        holder.btnDatThoiGian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DonHang temp = donHang;
                temp.setTrangThai(TrangThaiDonHang.SHOP_DangChuanBihang);
                temp.setGhiChuCuaHang(holder.dateAndTimePicker.getDate().toString());
                arrayList.set(position, temp);
                notifyDataSetChanged();
                firebase_manager.Ghi_DonHang(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        notifyDataSetChanged();
                        LoadButton(holder, temp.getTrangThai());
                    }
                });
            }
        });

        holder.btnDaChuanBiXOng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DonHang temp = donHang;
                temp.setTrangThai(TrangThaiDonHang.SHOP_DaChuanBiXong);
                arrayList.set(position, temp);
                notifyDataSetChanged();
                firebase_manager.Ghi_DonHang(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        notifyDataSetChanged();
                        LoadButton(holder, temp.getTrangThai());
                    }
                });
            }
        });
        holder.btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FastDialog dialog = FastDialog.w(context)
                                .setTitleText("Thông báo")
                                .setText("Vui lòng nhập lí do hủy đơn")
                                .setHint("Please enter text")
                                .setAnimation(Animations.GROW_IN)
                                .positiveText("Hủy đơn")
                                .negativeText("Quay lại")
                                .create();
                dialog.positiveClickListener(new PositiveClick() {
                    @Override
                    public void onClick(View view) {
                        if (dialog.getInputText()!=null)
                        {
                            DonHang temp = donHang;
                            temp.setTrangThai(TrangThaiDonHang.Bep_DaHuyDonHang);
                            temp.setGhiChuCuaHang(dialog.getInputText());
                            arrayList.set(position, temp);
                            notifyDataSetChanged();
                            Toast.makeText(context, "Đơn hàng đã được hủy vì lí do: "+dialog.getInputText(), Toast.LENGTH_LONG).show();
                            firebase_manager.Ghi_DonHang(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    notifyDataSetChanged();
                                    LoadButton(holder, temp.getTrangThai());
                                }
                            });
                        }
                        else {
                            Toast.makeText(context, "Vui lòng không để trống!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });
        holder.btnGiaoHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DonHang temp = donHang;
                temp.setTrangThai(TrangThaiDonHang.SHOP_DangGiaoShipper);
                temp.setIDShipper(shippers.get(holder.spDSShipper.getSelectedPosition()).getiDShipper());
                arrayList.set(position, temp);
                notifyDataSetChanged();
                Toast.makeText(context, "Vui lòng chờ shipper đến lấy hàng", Toast.LENGTH_SHORT).show();
                firebase_manager.Ghi_DonHang(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        notifyDataSetChanged();
                        LoadButton(holder, temp.getTrangThai());
                    }
                });
            }
        });
        holder.btnDaGiaoHangChoShipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DonHang temp = donHang;
                temp.setTrangThai(TrangThaiDonHang.Shipper_DaLayHang);
                arrayList.set(position, temp);
                notifyDataSetChanged();
                Toast.makeText(context, "Shipper đã lấy hàng", Toast.LENGTH_SHORT).show();
                firebase_manager.Ghi_DonHang(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        notifyDataSetChanged();
                        LoadButton(holder, temp.getTrangThai());
                    }
                });
            }
        });
        holder.btnDaGiaoHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DonHang temp = donHang;
                temp.setTrangThai(TrangThaiDonHang.Shipper_DangGiaoHang);
                arrayList.set(position, temp);
                notifyDataSetChanged();
                firebase_manager.Ghi_DonHang(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        notifyDataSetChanged();
                        LoadButton(holder, temp.getTrangThai());
                    }
                });
            }
        });

    }

    private void LoadButton(MyViewHolder holder, TrangThaiDonHang trangThai) {
        if (trangThai == TrangThaiDonHang.SHOP_DangChuanBihang) {
            holder.lnHuyLiDo.setVisibility(View.VISIBLE);

        } else {
            holder.lnHuyLiDo.setVisibility(View.GONE);
        }
        if (trangThai == TrangThaiDonHang.SHOP_DaGiaoChoBep) {
            holder.lnDatThoiGian.setVisibility(View.VISIBLE);

        } else {
            holder.lnDatThoiGian.setVisibility(View.GONE);
        }
        if (trangThai == TrangThaiDonHang.SHOP_DaChuanBiXong) {
            holder.btnDaGiaoHang.setVisibility(View.VISIBLE);

        } else {
            holder.btnDaGiaoHang.setVisibility(View.GONE);
        }




    }

    private void LoadShipper(MyViewHolder holder) {
        firebase_manager.mDatabase.child("Shipper").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull  Task<DataSnapshot> task) {
                shippers.clear();
                for (DataSnapshot dataSnapshot: task.getResult().getChildren()
                     ) {
                    Shipper shipper = dataSnapshot.getValue(Shipper.class);
                    shippers.add(shipper);
                }
                shipperAdapter = new ShipperAdapter(context,R.layout.item_shipper,shippers);
                holder.spDSShipper.setAdapter(shipperAdapter);
            }
        });
    }


    //Hàm để get layout type
    @Override
    public int getItemViewType(int position) {

        return resource;
    }

    //trả về số phần tử
    @Override
    public int getItemCount() {
        if (arrayList==null)
        {
            return  0;
        }
        return arrayList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                ArrayList<DonHang> list = new ArrayList<>();
                if (strSearch.isEmpty()) {
                    list = source;
                } else {
                    for (DonHang donHang : source) {
                        //Thêm cái địa chỉ vs SDT vào đây
                        if (donHang.getIDDonHang().contains(strSearch)||
                                donHang.getTrangThai().toString().equals(strSearch)||
                                donHang.getIDDonHang().contains(strSearch)||
                                donHang.getDiaChi().contains(strSearch))
                        {
                            list.add(donHang);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                arrayList = (ArrayList<DonHang>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    //Define RecylerVeiw Holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtXemChiTiet,txtGhiChu, txtID, txtTrangThaiDonHang, txtTenKhach, txtDiaChi, txtSoDienThoai, txtTongTien, txtSanPham, txtTime;
        ImageView imageView;
        RecyclerView rcvItemGiohang;
        ProgressBar progressBar;
        Button btnDaChuanBiXOng, btnDaGiaoHang,btnHoantac, btnHuy,btnDatThoiGian,btnGiaoHang,btnDaGiaoHangChoShipper;
        LinearLayout lnHuyLiDo,lnDatThoiGian,lnShipper,lnXacNhangShipper,lnXacNhanGiaoHang;
        SingleDateAndTimePicker dateAndTimePicker;
        SearchableSpinner spDSShipper;
        public MyViewHolder(View itemView) {
            super(itemView);
            txtID = itemView.findViewById(R.id.tv_IDDonHang);
            txtSoDienThoai = itemView.findViewById(R.id.txtSoDienThoai);
            txtTrangThaiDonHang = itemView.findViewById(R.id.txtTrangThaiDonHang);
            txtTenKhach = itemView.findViewById(R.id.txtTenKhach);
            txtTongTien = itemView.findViewById(R.id.txtTongTien);
            txtDiaChi = itemView.findViewById(R.id.txtDiaChi);
            txtSanPham = itemView.findViewById(R.id.txtSanPham);
            btnDaChuanBiXOng = itemView.findViewById(R.id.btnDaChuanBiXOng);
            progressBar = itemView.findViewById(R.id.progessbar);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtXemChiTiet = itemView.findViewById(R.id.txtXemChiTiet);
            btnHuy = itemView.findViewById(R.id.btnHuy);
            btnDatThoiGian = itemView.findViewById(R.id.btnDatThoiGian);
            lnDatThoiGian = itemView.findViewById(R.id.lnDatThoiGian);
            lnHuyLiDo = itemView.findViewById(R.id.lnHuyLiDo);
            lnShipper = itemView.findViewById(R.id.lnShipper);
            dateAndTimePicker = itemView.findViewById(R.id.single_day_picker);
            spDSShipper = itemView.findViewById(R.id.spDSShipper);
            btnGiaoHang = itemView.findViewById(R.id.btnGiaoHang);
            btnDaGiaoHangChoShipper = itemView.findViewById(R.id.btnDaGiaoHangChoShipper);
            lnXacNhangShipper = itemView.findViewById(R.id.lnXacNhangShipper);
            txtGhiChu = itemView.findViewById(R.id.txtGhiChu);
            btnDaGiaoHang = itemView.findViewById(R.id.btnDaGiaoHang);

        }
    }
}
