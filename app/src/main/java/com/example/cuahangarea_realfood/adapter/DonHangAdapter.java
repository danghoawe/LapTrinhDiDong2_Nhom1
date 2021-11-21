package com.example.cuahangarea_realfood.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.developer.kalert.KAlertDialog;
import com.example.cuahangarea_realfood.Firebase_Manager;
import com.example.cuahangarea_realfood.R;
import com.example.cuahangarea_realfood.SetOnLongClick;
import com.example.cuahangarea_realfood.TrangThai.TrangThaiDonHang;
import com.example.cuahangarea_realfood.TrangThai.TrangThaiThongBao;
import com.example.cuahangarea_realfood.model.BaoCaoShipper;
import com.example.cuahangarea_realfood.model.DanhMuc;
import com.example.cuahangarea_realfood.model.DonHang;
import com.example.cuahangarea_realfood.model.DonHangInfo;
import com.example.cuahangarea_realfood.model.KhachHang;
import com.example.cuahangarea_realfood.model.SanPham;
import com.example.cuahangarea_realfood.model.Shipper;
import com.example.cuahangarea_realfood.model.ThongBao;
import com.example.cuahangarea_realfood.screen.ThongTinCuaHangActivity;
import com.example.cuahangarea_realfood.screen.ThongTinDonHangActivity;
import com.example.cuahangarea_realfood.screen.ThongTinSanPhamActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.nordan.dialog.Animation;
import com.nordan.dialog.DialogType;
import com.nordan.dialog.NordanAlertDialog;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import karpuzoglu.enes.com.fastdialog.Animations;
import karpuzoglu.enes.com.fastdialog.FastDialog;
import karpuzoglu.enes.com.fastdialog.PositiveClick;
import karpuzoglu.enes.com.fastdialog.Type;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.MyViewHolder> implements Filterable {
    private Activity context;
    private int resource;
    private ArrayList<DonHang> arrayList;
    ArrayList<DonHang> source;

    public SetOnLongClick setOnLongClick;
    Firebase_Manager firebase_manager = new Firebase_Manager();

    public SetOnLongClick getSetOnLongClick() {
        return setOnLongClick;
    }

    public void setSetOnLongClick(SetOnLongClick setOnLongClick) {
        this.setOnLongClick = setOnLongClick;
    }

    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    public DonHangAdapter(Activity context, int resource, ArrayList<DonHang> arrayList) {
        this.context = context;
        this.resource = resource;
        this.arrayList = arrayList;
        this.source = arrayList;
    }


    @NonNull
    @Override
    public DonHangAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        CardView cardView = (CardView) layoutInflater.inflate(viewType, parent, false);
        return new DonHangAdapter.MyViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull DonHangAdapter.MyViewHolder holder, int position) {
        DonHang donHang = arrayList.get(position);
        firebase_manager.SetColorOfStatus(donHang.getTrangThai(),holder.lnheader,holder.txtID);
        ArrayList<DonHangInfo>donHangInfos = new ArrayList<>();
        Shipper shipper;
        holder.txtID.setText(donHang.getIDDonHang().substring(0, 25));
        holder.txtTrangThaiDonHang.setText(firebase_manager.GetStringTrangThaiDonHang(donHang.getTrangThai()));
        holder.txtGhiChu.setText(donHang.getGhiChu_KhachHang()+"");


        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);


        float parseFloat = Float.parseFloat(donHang.getTongTien()+"");
        String price =format.format(parseFloat);
        holder.txtTongTien.setText(price);
       // firebase_manager.SetColor(donHang.getTrangThai(),holder.txtTrangThaiDonHang);

        holder.txtDiaChi.setText(donHang.getDiaChi() + "");
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm dd/MM/yyyy");
        String strDate= formatter.format(donHang.getNgayTao());
        holder.txtTime.setText(strDate);
        LoadButton(holder, donHang.getTrangThai());

        firebase_manager.mDatabase.child("KhachHang").child(donHang.getIDKhachHang()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                KhachHang khachHang = snapshot.getValue(KhachHang.class);
                holder.txtTenKhach.setText(khachHang.getTenKhachHang());
                holder.txtSoDienThoai.setText(khachHang.getSoDienThoai());
                holder.txtDiaChi.setText(khachHang.getDiaChi());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        firebase_manager.mDatabase.child("DonHangInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.txtSanPham.setText("");
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DonHangInfo donHangInfo = dataSnapshot.getValue(DonHangInfo.class);
                    if (donHang.getTrangThai() != TrangThaiDonHang.Shipper_GiaoThanhCong) {
                        if (donHangInfo.getIDDonHang().equals(donHang.getIDDonHang())) {
                            donHangInfos.add(donHangInfo);
                            holder.txtSanPham.setText(holder.txtSanPham.getText() + donHangInfo.getSanPham().getTenSanPham() + "(" + donHangInfo.getSoLuong() + ")" + ", ");
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.btnXacNhanCoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DonHang temp = donHang;
                temp.setTrangThai(TrangThaiDonHang.SHOP_DaGiaoChoBep);
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

        holder.btnHoantac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DonHang temp = donHang;
                temp.setTrangThai(TrangThaiDonHang.SHOP_ChoXacNhanChuyenTien);
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

        holder.btnXacNhanHoanHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DonHang temp = donHang;
                temp.setTrangThai(TrangThaiDonHang.Shipper_DaTraHang);
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

        holder.btnXacNhanTraTien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DonHang temp = donHang;
                temp.setTrangThai(TrangThaiDonHang.Shipper_DaChuyenTien);
                arrayList.set(position, temp);
                notifyDataSetChanged();
                firebase_manager.Ghi_DonHang(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        notifyDataSetChanged();
                        LoadButton(holder, temp.getTrangThai());
                        for (DonHangInfo donHangInfo:
                                donHangInfos
                             ) {
                            SanPham sanPham = donHangInfo.getSanPham();
                            sanPham.setSoLuongBanDuoc(sanPham.getSoLuongBanDuoc()+1);
                            firebase_manager.Ghi_SanPham(sanPham);
                        }
                    }
                });
            }
        });

        holder.btnHuyDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog= new NordanAlertDialog.Builder(context)
                        .setDialogType(DialogType.QUESTION)
                        .setAnimation(Animation.SLIDE)
                        .isCancellable(true)
                        .setTitle("Thông báo")
                        .setMessage("Bạn muốn hủy đơn hàng ? ")
                        .setPositiveBtnText("Oke")
                        .onPositiveClicked(() -> {
                            DonHang temp = donHang;
                            temp.setTrangThai(TrangThaiDonHang.SHOP_HuyDonHang);
                            arrayList.set(position, temp);
                            notifyDataSetChanged();
                            firebase_manager.Ghi_DonHang(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    notifyDataSetChanged();
                                    LoadButton(holder, temp.getTrangThai());
                                    Toast.makeText(context, "Hủy thành công", Toast.LENGTH_SHORT).show();

                                }
                            });})
                        .setNegativeBtnText("Hủy")
                        .onNegativeClicked(() -> {})
                        .build();

                dialog.show();

            }
        });

        holder.lnBaoCaoShipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FastDialog dialog = FastDialog.w(context)
                        .setTitleText("Thông báo")
                        .setText("Bạn muốn báo cáo Shipper này?")
                        .setHint("Vui lòng nhập lí do")
                        .setAnimation(Animations.GROW_IN)
                        .positiveText("Báo cáo")
                        .negativeText("Hủy")
                        .create();


                dialog.positiveClickListener(new PositiveClick() {
                    @Override
                    public void onClick(View view) {
                        if (dialog.getInputText()!=null)
                        {
                            String uuid = UUID.randomUUID().toString().replace("-", "");
                            BaoCaoShipper baoCaoShipper = new BaoCaoShipper(uuid,firebase_manager.auth.getUid(),donHang.getIDShipper(),dialog.getInputText(),"Thông báo",new Date());
                            Toast.makeText(context, "Bạn đã báo cáo shipper  vì lí do: "+dialog.getInputText(), Toast.LENGTH_LONG).show();
                            firebase_manager.mDatabase.child("BaoCao_CuaHang_Shipper").child(uuid).setValue(baoCaoShipper);
                            firebase_manager.storageRef.child("CuaHang").child(firebase_manager.auth.getUid()).child("Avatar").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    ThongBao thongBao = new ThongBao(uuid,"Cửa hàng "+ firebase_manager.auth.getUid()+" đã báo cáo về Shipper"+ donHang.getIDShipper() + " vì : "+ dialog.getInputText(),"thông báo","","admin",uri.toString(), TrangThaiThongBao.ChuaXem,new Date());
                                    firebase_manager.mDatabase.child("ThongBao").child("admin").child(uuid).setValue(thongBao);
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

        holder.btnGiaoHangThanhCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DonHang temp = donHang;
                temp.setTrangThai(TrangThaiDonHang.Shipper_DaChuyenTien);
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
        holder.btnGiaoHangThatBai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DonHang temp = donHang;
                temp.setTrangThai(TrangThaiDonHang.Shipper_DaTraHang);
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

        if (trangThai == TrangThaiDonHang.SHOP_ChoXacNhanChuyenTien||trangThai == TrangThaiDonHang.Bep_DaHuyDonHang) {
            holder.btnXacNhanCoc.setVisibility(View.VISIBLE);
            holder.btnHuyDonHang.setVisibility(View.VISIBLE);
            holder.lnNew.setVisibility(View.VISIBLE);
            holder.imgTick.setVisibility(View.GONE);

        } else {
            holder.btnXacNhanCoc.setVisibility(View.GONE);
            holder.btnHuyDonHang.setVisibility(View.GONE);
            holder.lnNew.setVisibility(View.GONE);
            holder.imgTick.setVisibility(View.VISIBLE);
        }



        if (trangThai == TrangThaiDonHang.SHOP_DangChuanBihang) {
            holder.btnHoantac.setVisibility(View.VISIBLE);

        } else {
            holder.btnHoantac.setVisibility(View.GONE);
        }

        if (trangThai == TrangThaiDonHang.Shipper_DangGiaoHang)
        {
            holder.lnXacNhanGiaoHang.setVisibility(View.VISIBLE);
        }
        else {
            holder.lnXacNhanGiaoHang.setVisibility(View.GONE);
        }


    }


    //Hàm để get layout type
    @Override
    public int getItemViewType(int position) {

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
                    ArrayList<DonHang> list = new ArrayList<>();
                    for (DonHang donHang: source) {
                        if (donHang.getTrangThai().toString().equals(strSearch)||donHang.getIDDonHang().contains(strSearch)) {
                            list.add(donHang);
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
                arrayList = (ArrayList<DonHang>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    //Define RecylerVeiw Holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenShipper,txtGhiChu,txtDiaChiShipper,txtSoDtShipper,txtBaoCaoShipper,txtXemChiTiet,txtID, txtTrangThaiDonHang, txtTenKhach, txtDiaChi, txtSoDienThoai, txtTongTien, txtSanPham,txtTime;
        ImageView imageView,imgTick;
        RecyclerView rcvItemGiohang;
        ProgressBar progressBar;
        Button btnGiaoHangThanhCong,btnGiaoHangThatBai,btnXacNhanCoc,btnHoantac,btnXacNhanTraTien,btnXacNhanHoanHang,btnHuyDonHang;
        LinearLayout lnBaoCaoShipper,lnTTshipper,lnNew,lnheader,lnXacNhanGiaoHang;
        public MyViewHolder(View itemView) {
            super(itemView);
            txtID = itemView.findViewById(R.id.tv_IDDonHang);
            txtSoDienThoai = itemView.findViewById(R.id.txtSoDienThoai);
            txtTrangThaiDonHang = itemView.findViewById(R.id.txtTrangThaiDonHang);
            txtTenKhach = itemView.findViewById(R.id.txtTenKhach);
            txtTongTien = itemView.findViewById(R.id.txtTongTien);
            txtDiaChi = itemView.findViewById(R.id.txtDiaChi);
            txtSanPham = itemView.findViewById(R.id.txtSanPham);
            btnXacNhanCoc = itemView.findViewById(R.id.btnXacNhanCoc);
            progressBar = itemView.findViewById(R.id.progessbar);
            btnHoantac = itemView.findViewById(R.id.btnHoanTac1);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtXemChiTiet = itemView.findViewById(R.id.txtXemChiTiet);
            btnXacNhanTraTien = itemView.findViewById(R.id.btnXacNhanTraTien);
            btnXacNhanHoanHang = itemView.findViewById(R.id.btnXacNhanHoanHang);
            btnHuyDonHang = itemView.findViewById(R.id.btnHuyDonHang);
            txtBaoCaoShipper = itemView.findViewById(R.id.txtBaoCaoShipper);
            lnBaoCaoShipper = itemView.findViewById(R.id.lnBaoCaoShipper);
            txtTenShipper = itemView.findViewById(R.id.txtTenShipper);
            txtDiaChiShipper = itemView.findViewById(R.id.txtDiaChiShipper);
            txtSoDtShipper = itemView.findViewById(R.id.txtSoDienThoaiShipper);
            lnTTshipper = itemView.findViewById(R.id.lnTTShipper);
            lnNew = itemView.findViewById(R.id.lnNew);
            imgTick = itemView.findViewById(R.id.imgTick);
            lnheader = itemView.findViewById(R.id.lnheader);
            txtGhiChu = itemView.findViewById(R.id.txtGhiChu);
            lnXacNhanGiaoHang = itemView.findViewById(R.id.lnXacNhanGiaoHang);
            btnGiaoHangThanhCong = itemView.findViewById(R.id.btnGiaoHangThanhCong);
            btnGiaoHangThatBai = itemView.findViewById(R.id.btnGiaoHangThatBai);
        }
    }
}
