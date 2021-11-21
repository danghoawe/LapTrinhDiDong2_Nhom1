package com.example.cuahangarea_realfood;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.cuahangarea_realfood.TrangThai.TrangThaiDonHang;
import com.example.cuahangarea_realfood.adapter.DanhGiaSanPhamAdapter;
import com.example.cuahangarea_realfood.adapter.DanhMucAdapter;
import com.example.cuahangarea_realfood.adapter.DonHangAdapter;
import com.example.cuahangarea_realfood.adapter.DonHang_BepAdapter;
import com.example.cuahangarea_realfood.adapter.MaGiamGiaAdapter;
import com.example.cuahangarea_realfood.adapter.SanPhamAdapter;
import com.example.cuahangarea_realfood.model.CuaHang;
import com.example.cuahangarea_realfood.model.DanhGia;
import com.example.cuahangarea_realfood.model.DanhMuc;
import com.example.cuahangarea_realfood.model.DonHang;
import com.example.cuahangarea_realfood.model.KhachHang;
import com.example.cuahangarea_realfood.model.LoaiSanPham;
import com.example.cuahangarea_realfood.model.SanPham;
import com.example.cuahangarea_realfood.model.TaiKhoanNganHang;
import com.example.cuahangarea_realfood.model.ThongBao;
import com.example.cuahangarea_realfood.model.Voucher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Firebase_Manager {
    public  DatabaseReference mDatabase ;
    public StorageReference storageRef ;
    public FirebaseAuth auth;
    public Firebase_Manager() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storageRef = FirebaseStorage.getInstance().getReference();
        auth= FirebaseAuth.getInstance();
    }

    public Task<Void> Ghi_ThongBao(ThongBao thongBao)
    {
        return  mDatabase.child("ThongBao").child(auth.getUid()).child(thongBao.getIDThongBao()).setValue(thongBao);
    }
    public Task<Void> Ghi_DonHang(DonHang donHang)
    {
        return  mDatabase.child("DonHang").child(donHang.getIDDonHang()).setValue(donHang);
    }
    public Task<Void> Ghi_NganHang(TaiKhoanNganHang taiKhoanNganHang)
    {
        return  mDatabase.child("TaiKhoanNganHang").child(taiKhoanNganHang.getId()).setValue(taiKhoanNganHang);
    }
    public Task<Void> Ghi_CuaHang(CuaHang cuaHang)
    {
      return  mDatabase.child("CuaHang").child(cuaHang.getIDCuaHang()).setValue(cuaHang);
    }
    public void Ghi_DanhMuc(DanhMuc danhMuc)
    {
        mDatabase.child("DanhMuc").child(auth.getUid()).child(danhMuc.getIDDanhMuc()).setValue(danhMuc);
    }
    public Task<Void> Ghi_Voucher(Voucher voucher)
    {
      return  mDatabase.child("Voucher").child(voucher.getIdMaGiamGia()).setValue(voucher);
    }

    public void GetSanPham(ArrayList arrayList, SanPhamAdapter sanPhamAdapter) {
        mDatabase.child("Voucher").orderByChild("idcuaHang").equalTo(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    SanPham sanPham = postSnapshot.getValue(SanPham.class);
                    arrayList.add(sanPham);
                    if (sanPhamAdapter!=null)
                    {
                        sanPhamAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        mDatabase.child("SanPham").orderByChild("idcuaHang").equalTo(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    SanPham sanPham = postSnapshot.getValue(SanPham.class);
                    arrayList.add(sanPham);
                    if (sanPhamAdapter!=null)
                    {
                        sanPhamAdapter.notifyDataSetChanged();

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void LayTenLoai(SanPham sanPham, TextView tvTenLoai) {
        mDatabase.child("LoaiSanPham").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    LoaiSanPham loaiSanPham = dataSnapshot.getValue(LoaiSanPham.class);
                    if (loaiSanPham.getiDLoai().equals(sanPham.getIDLoai())) {
                        tvTenLoai.setText(loaiSanPham.getTenLoai());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void LoadImageFood(SanPham sanPham, Context context, ImageView ivFood) {
        storageRef.child("SanPham").child(sanPham.getIDCuaHang()).child(sanPham.getIDSanPham()).child(sanPham.getImages().get(0)).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Glide.with(context)
                        .load(task.getResult().toString())
                        .into(ivFood);
            }
        });
    }
    public void LoadImageLoai(LoaiSanPham loaiSanPham, Context context, ImageView ivLoai) {
        storageRef.child("LoaiSanPham").child(loaiSanPham.getiDLoai()).child("Loại sản phẩm").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(ivLoai);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("", e.getMessage());
            }
        });
    }
    public void LoadImageKhachHang(Context context,ImageView civAvatar){
        storageRef.child("KhachHang").child(auth.getUid()).child("AvatarKhachHang").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri.toString()).into(civAvatar);
            }
        });
    }

    public void LoadTenKhachHang(TextView tvHoTen) {
        mDatabase.child("KhachHang").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                KhachHang khachHang = snapshot.getValue(KhachHang.class);
                tvHoTen.setText(khachHang.getTenKhachHang());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void GetDonHang(ArrayList arrayList, DonHangAdapter donHangAdapter, ProgressBar progressBar) {
        if (progressBar!=null)
        {
            progressBar.setVisibility(View.VISIBLE);
        }
        mDatabase.child("DonHang").orderByChild("ngayTao").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    DonHang donHang = postSnapshot.getValue(DonHang.class);
                    if (donHang.getIDCuaHang().equals(auth.getUid()))
                    {
                        if (donHang.getTrangThai()!=TrangThaiDonHang.Shipper_GiaoThanhCong&&donHang.getTrangThai()!=TrangThaiDonHang.Shipper_GiaoKhongThanhCong
                                &&donHang.getTrangThai()!=TrangThaiDonHang.Shipper_DaLayHang&&donHang.getTrangThai()!=TrangThaiDonHang.Shipper_KhongNhanGiaoHang
                                &&donHang.getTrangThai()!=TrangThaiDonHang.SHOP_ChoShipperLayHang &&donHang.getTrangThai()!=TrangThaiDonHang.SHOP_ChoXacNhanGiaoHangChoShipper
                                &&donHang.getTrangThai()!=TrangThaiDonHang.SHOP_DangGiaoShipper &&donHang.getTrangThai()!=TrangThaiDonHang.ChoShopXacNhan_Tien &&donHang.getTrangThai()!=TrangThaiDonHang.ChoShopXacNhan_TraHang)
                        {
                            arrayList.add(donHang);

                        }

                    }
                }
                if (donHangAdapter!=null)
                {
                    donHangAdapter.notifyDataSetChanged();
                }

                if (progressBar!=null)
                {
                    progressBar.setVisibility(View.GONE);
                }
                Collections.sort(arrayList, new Comparator<DonHang>() {
                    @Override
                    public int compare(DonHang o1, DonHang o2) {
                        return o2.getNgayTao().compareTo(o1.getNgayTao());
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void GetPhanHoi(ArrayList arrayList, DanhGiaSanPhamAdapter danhGiaSanPhamAdapter) {
        mDatabase.child("DanhGia").orderByChild("idcuaHang").equalTo(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    DanhGia danhGia = postSnapshot.getValue(DanhGia.class);
                    arrayList.add(danhGia);
                    Log.d("Tag",arrayList.size()+"");

                }
                Collections.sort(arrayList, new Comparator<DanhGia>() {
                    @Override
                    public int compare(DanhGia o1, DanhGia o2) {
                        return o2.getNgayDanhGia().compareTo(o1.getNgayDanhGia());
                    }
                });

                danhGiaSanPhamAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void GetDonHang_Bep(ArrayList arrayList, DonHang_BepAdapter donHangAdapter, ProgressBar progressBar) {
        if (progressBar!=null)
        {
            progressBar.setVisibility(View.VISIBLE);
        }
        mDatabase.child("DonHang").orderByChild("trangThai").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    DonHang donHang = postSnapshot.getValue(DonHang.class);
                    if (donHang.getIDCuaHang().equals(auth.getUid()))
                    {
                        if (donHang.getTrangThai()== TrangThaiDonHang.SHOP_DangChuanBihang||
                                donHang.getTrangThai()== TrangThaiDonHang.SHOP_DaChuanBiXong||
                                donHang.getTrangThai()==TrangThaiDonHang.SHOP_DaGiaoChoBep
                                )
                        {
                            arrayList.add(donHang);
                        }

                    }
                }
                if (donHangAdapter!=null)
                {
                    donHangAdapter.notifyDataSetChanged();
                }
                if (progressBar!=null)
                {
                    progressBar.setVisibility(View.GONE);
                }
                Collections.sort(arrayList, new Comparator<DonHang>() {
                    @Override
                    public int compare(DonHang o1, DonHang o2) {
                        return o2.getNgayTao().compareTo(o1.getNgayTao());
                    }
                });
                donHangAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void GetVoucher(ArrayList arrayList, MaGiamGiaAdapter maGiamGiaAdapter) {
        mDatabase.child("Voucher").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Voucher voucher = postSnapshot.getValue(Voucher.class);
                    if (voucher.getSanPham().getIDCuaHang().equals(auth.getUid()))
                    {
                        arrayList.add(voucher);
                    }
                }
                if (maGiamGiaAdapter!=null)
                {
                    maGiamGiaAdapter.notifyDataSetChanged();

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void GetSanPham_V2(ArrayList arrayList, ArrayAdapter sanPhamAdapter) {
        mDatabase.child("SanPham").orderByChild("idcuaHang").equalTo(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    SanPham sanPham = postSnapshot.getValue(SanPham.class);
                    arrayList.add(sanPham.getTenSanPham());
                }
                sanPhamAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void GetDanhSachDanhMuc(ArrayList<DanhMuc>danhMucs, DanhMucAdapter danhMucAdapter) {
        mDatabase.child("DanhMuc").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                danhMucs.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    DanhMuc danhMuc = postSnapshot.getValue(DanhMuc.class);
                    danhMucs.add(danhMuc);
                    if (danhMucAdapter!=null)
                    {
                        danhMucAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public Task<Void> Ghi_SanPham(SanPham sanPham)
    {
       return mDatabase.child("SanPham").child((sanPham.getIDSanPham())).setValue(sanPham);
    }
    public UploadTask UpImageDanhMuc(Uri truoc, String danhMuc)
    {
       return storageRef.child("DanhMuc").child(danhMuc).child("image").putFile(truoc);
    }
    public void UpImageSanPham(ArrayList<Uri> image, String idSanPham,ArrayList<String>nameImage)
    {
        for (int i = 0; i<image.size();i++
        ) {
            try {
                storageRef.child("SanPham").child(auth.getUid()).child(idSanPham).child(nameImage.get(i)).putFile(image.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Log.d("Upload Image: ","Success");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        Log.d("Upload Image: ","Faild");

                    }
                });
            }catch (Exception e)
            {
                Log.d("Firebase: UpLoad",e.getMessage());
            }
        }
    }
    public ArrayList<DanhMuc> GetDanhMuc()
    {
        ArrayList<DanhMuc> danhMucs = new ArrayList<>();
        mDatabase.child("DanhMuc").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                danhMucs.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    DanhMuc danhMuc = postSnapshot.getValue(DanhMuc.class);
                    danhMucs.add(danhMuc);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return  danhMucs;
    }

    public void Up2MatCMND(Uri truoc, Uri sau, String IDCuaHang)
    {
        storageRef.child("CuaHang").child(IDCuaHang).child("CMND_MatTruoc").putFile(truoc);
        storageRef.child("CuaHang").child(IDCuaHang).child("CMND_MatSau").putFile(sau);
    }
    public UploadTask UpAvatar(Uri Avatar)
    {
        return   storageRef.child("CuaHang").child(auth.getUid()).child("Avatar").putFile(Avatar);

    }
    public UploadTask UpWallPaper(Uri WallPaper)
    {
        return   storageRef.child("CuaHang").child(auth.getUid()).child("WallPaper").putFile(WallPaper);
    }

    public CuaHang getCuaHang(){
        final CuaHang[] cuaHang = new CuaHang[1];
        mDatabase.child("CuaHang").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               CuaHang temp = dataSnapshot.getValue(CuaHang.class);
               cuaHang[0] = temp;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return cuaHang[0];
    }
    public String GetStringTrangThaiDonHang(TrangThaiDonHang trangThaiDonHang){
        String res ="";
        if (trangThaiDonHang == TrangThaiDonHang.SHOP_HuyDonHang)
        {
            res ="Đã hủy";
        }
        if (trangThaiDonHang == TrangThaiDonHang.SHOP_ChoXacNhanChuyenTien)
        {
            res ="Chờ xác nhận chuyển tiền cọc";
        }
        if (trangThaiDonHang == TrangThaiDonHang.SHOP_DaGiaoChoBep)
        {
            res ="Đã giao đơn hàng cho bếp";
        }
        if (trangThaiDonHang == TrangThaiDonHang.SHOP_DangChuanBihang)
        {
            res ="Đang chuẩn bị hàng";
        }
        if (trangThaiDonHang == TrangThaiDonHang.SHOP_DaChuanBiXong)
        {
            res ="Đã chuẩn bị xong";
        }
        if (trangThaiDonHang == TrangThaiDonHang.SHOP_DangGiaoShipper)
        {
            res ="Đang giao shipper đi phát";
        }
        if (trangThaiDonHang == TrangThaiDonHang.SHOP_ChoShipperLayHang)
        {
            res ="Chờ shipper lấy hàng";
        }
        if (trangThaiDonHang == TrangThaiDonHang.SHOP_ChoXacNhanGiaoHangChoShipper)
        {
            res ="Chờ Shop xác nhận giao hàng cho Shipper";
        }
        if (trangThaiDonHang == TrangThaiDonHang.ChoShopXacNhan_Tien)
        {
            res ="Chờ Shop xác nhận đã nhận tiền hàng từ Shipper";
        }
        if (trangThaiDonHang == TrangThaiDonHang.ChoShopXacNhan_TraHang)
        {
            res ="Chờ Shop xác nhận đã nhận hàng trả về từ Shipper";
        }
        if (trangThaiDonHang == TrangThaiDonHang.Shipper_DaLayHang)
        {
            res ="Shipper đã lấy hàng đi giao";
        }
        if (trangThaiDonHang == TrangThaiDonHang.Shipper_KhongNhanGiaoHang)
        {
            res ="Shipper không nhận giao hàng";
        }
        if (trangThaiDonHang == TrangThaiDonHang.Shipper_DaTraHang)
        {
            res ="Đơn hàng đã được hoàn về";
        }
        if (trangThaiDonHang == TrangThaiDonHang.Shipper_DaChuyenTien)
        {
            res ="Thanh toán thành công";
        }
        if (trangThaiDonHang == TrangThaiDonHang.Shipper_GiaoKhongThanhCong)
        {
            res ="Giao hàng không thành công";
        }
        if (trangThaiDonHang == TrangThaiDonHang.Shipper_DangGiaoHang)
        {
            res ="Shipper đang giao hàng";
        }if (trangThaiDonHang == TrangThaiDonHang.Shipper_GiaoThanhCong)
        {
            res ="Shipper giao hàng thành công";
        }
        if (trangThaiDonHang == TrangThaiDonHang.KhachHang_HuyDon)
        {
            res ="Khách hàng hủy đơn hàng";
        }if (trangThaiDonHang == TrangThaiDonHang.Bep_DaHuyDonHang)
        {
            res ="Bếp đã hủy đơn";
        }
        return res;
    }
    public void SetColorOfStatus(TrangThaiDonHang trangThaiDonHang, View view,TextView textView){
        String res ="";
        textView.setTextColor(Color.WHITE);
        if (trangThaiDonHang == TrangThaiDonHang.SHOP_HuyDonHang||trangThaiDonHang == TrangThaiDonHang.KhachHang_HuyDon||
                trangThaiDonHang == TrangThaiDonHang.Shipper_DaTraHang||trangThaiDonHang == TrangThaiDonHang.Shipper_GiaoKhongThanhCong)
        {
            res ="Đã hủy";
            view.setBackgroundColor(Color.parseColor("#F0290E"));
        }
        if (trangThaiDonHang == TrangThaiDonHang.SHOP_ChoXacNhanChuyenTien||trangThaiDonHang == TrangThaiDonHang.ChoShopXacNhan_Tien
        ||trangThaiDonHang == TrangThaiDonHang.ChoShopXacNhan_TraHang||trangThaiDonHang == TrangThaiDonHang.Shipper_DaLayHang||trangThaiDonHang == TrangThaiDonHang.Shipper_DangGiaoHang)
        {
            res ="Chờ xác nhận chuyển tiền cọc";
            view.setBackgroundColor(Color.parseColor("#F0DE38"));
        }
        if (trangThaiDonHang == TrangThaiDonHang.SHOP_DaGiaoChoBep||trangThaiDonHang == TrangThaiDonHang.SHOP_DangChuanBihang
        ||trangThaiDonHang == TrangThaiDonHang.SHOP_DaChuanBiXong||trangThaiDonHang == TrangThaiDonHang.SHOP_DangGiaoShipper
        ||trangThaiDonHang == TrangThaiDonHang.SHOP_ChoShipperLayHang||trangThaiDonHang == TrangThaiDonHang.SHOP_ChoXacNhanGiaoHangChoShipper
        ||trangThaiDonHang == TrangThaiDonHang.Shipper_KhongNhanGiaoHang||trangThaiDonHang == TrangThaiDonHang.Bep_DaHuyDonHang)
        {
            view.setBackgroundColor(Color.parseColor("#31BAF0"));
            res ="Đã giao đơn hàng cho bếp";
        }
        if (trangThaiDonHang == TrangThaiDonHang.Shipper_DaChuyenTien||trangThaiDonHang == TrangThaiDonHang.Shipper_GiaoThanhCong)
        {
            res ="Chờ xác nhận chuyển tiền cọc";
            view.setBackgroundColor(Color.parseColor("#05E6C6"));
        }
    }
    public void SetColor(TrangThaiDonHang trangThaiDonHang,View lnView, TextView textView){
        textView.setTextColor(Color.WHITE);
        if (trangThaiDonHang == TrangThaiDonHang.SHOP_HuyDonHang||trangThaiDonHang == TrangThaiDonHang.Shipper_DaTraHang||
                trangThaiDonHang == TrangThaiDonHang.Shipper_GiaoKhongThanhCong)
        {
            lnView.setBackgroundColor(Color.RED);
        }


        if (trangThaiDonHang == TrangThaiDonHang.SHOP_DangChuanBihang||trangThaiDonHang == TrangThaiDonHang.SHOP_DaChuanBiXong
        ||trangThaiDonHang == TrangThaiDonHang.SHOP_DangGiaoShipper||trangThaiDonHang == TrangThaiDonHang.SHOP_ChoShipperLayHang
        ||trangThaiDonHang == TrangThaiDonHang.SHOP_ChoXacNhanGiaoHangChoShipper||trangThaiDonHang == TrangThaiDonHang.ChoShopXacNhan_Tien
        ||trangThaiDonHang == TrangThaiDonHang.ChoShopXacNhan_TraHang||trangThaiDonHang == TrangThaiDonHang.Shipper_DaLayHang
        ||trangThaiDonHang == TrangThaiDonHang.Shipper_KhongNhanGiaoHang||trangThaiDonHang == TrangThaiDonHang.Shipper_DangGiaoHang||trangThaiDonHang == TrangThaiDonHang.Shipper_KhongNhanGiaoHang)
        {
            lnView.setBackgroundColor(Color.parseColor("#FFFFC107"));

        }
        if (trangThaiDonHang == TrangThaiDonHang.Shipper_GiaoThanhCong||trangThaiDonHang == TrangThaiDonHang.ChoShopXacNhan_Tien||trangThaiDonHang == TrangThaiDonHang.Shipper_DaChuyenTien)
        {
            lnView.setBackgroundColor(Color.BLUE);

        }




    }
}
