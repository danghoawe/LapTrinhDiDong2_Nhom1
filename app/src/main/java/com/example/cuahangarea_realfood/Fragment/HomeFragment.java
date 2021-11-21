package com.example.cuahangarea_realfood.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.developer.kalert.KAlertDialog;
import com.example.cuahangarea_realfood.R;
import com.example.cuahangarea_realfood.model.DanhGia;
import com.example.cuahangarea_realfood.screen.DanhGiaActivity;
import com.example.cuahangarea_realfood.screen.ThongKeActivity;
import com.example.cuahangarea_realfood.screen.BepActivity;
import com.example.cuahangarea_realfood.screen.DanhSachDonHangActivity;
import com.example.cuahangarea_realfood.Firebase_Manager;
import com.example.cuahangarea_realfood.screen.MaGiamGiaActivity;
import com.example.cuahangarea_realfood.screen.DS_SanPhamActivity;
import com.example.cuahangarea_realfood.screen.ThongTinCuaHangActivity;
import com.example.cuahangarea_realfood.databinding.FragmentHomeBinding;
import com.example.cuahangarea_realfood.model.CuaHang;
import com.example.cuahangarea_realfood.screen.ThongTinDonHangActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tapadoo.alerter.Alerter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    CardView danhSachSanPham,maGiamGia,danhSachDonHang,phanHoiKhachHang,khuVucBep,DoanhThu;
    FragmentHomeBinding binding;
    String txtTenCuaHang ;
    Uri avatar,wallPaper;
    Firebase_Manager firebase_manager = new Firebase_Manager();
    public HomeFragment() {
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setControl();
    }

    private void setControl() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());

        Loaddata();


        binding.cardViewDanhSachSamPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebase_manager.mDatabase.child("TaiKhoanNganHang").orderByChild("idTaiKhoan").equalTo(firebase_manager.auth.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull  DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            Intent intent = new Intent(getActivity(), DS_SanPhamActivity.class);
                            startActivity(intent);
                        }
                        else {
                            new KAlertDialog(getContext(),KAlertDialog.WARNING_TYPE).setContentText("Bạn vui lòng thêm thông tin tài khoản ngân hàng trước khi đăng bán sản phẩm !").show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull  DatabaseError error) {

                    }
                });

            }
        });

        binding.cardViewMaGiamGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MaGiamGiaActivity.class);
                startActivity(intent);
            }
        });

        binding.cardViewDanhSachDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DanhSachDonHangActivity.class);
                startActivity(intent);
            }
        });
        binding.cardViewKhuVucBep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BepActivity.class);
                startActivity(intent);
            }
        });
        binding.cardViewDoanhThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ThongKeActivity.class);
                startActivity(intent);
            }
        });
        binding.cardViewPhanHoiKhachHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DanhGiaActivity.class);
                startActivity(intent);
            }
        });
        return  binding.getRoot();
    }



    private void Loaddata() {
        if (txtTenCuaHang!=null)
        {
            binding.txtTenCuaHang.setText(txtTenCuaHang);
        }
        else {
            firebase_manager. mDatabase.child("CuaHang").child(firebase_manager.auth.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    CuaHang temp = dataSnapshot.getValue(CuaHang.class);
                    binding.txtTenCuaHang.setText(temp.getTenCuaHang());
                    txtTenCuaHang =  temp.getTenCuaHang();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        firebase_manager.mDatabase.child("DanhGia").orderByChild("idcuaHang").equalTo(firebase_manager.auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int tong = 0;
                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    DanhGia danhGia = snapshot.getValue(DanhGia.class);
                    tong+=danhGia.getRating();
                }
                if (tong!=0)
                binding.tvStar.setText(tong/dataSnapshot.getChildrenCount()+"");
                binding.tvLuotDanhGia.setText(dataSnapshot.getChildrenCount()+" lượt đánh giá");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        firebase_manager.storageRef.child("CuaHang").child(firebase_manager.auth.getUid()).child("Avatar").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext())
                        .load(uri.toString())
                        .into(binding.profileImage);
                binding.progessbarAvatar.setVisibility(View.GONE);
                avatar =  uri;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                binding.progessbarAvatar.setVisibility(View.GONE);
                Alerter.create(getActivity())
                        .setTitle("Thông báo")
                        .setText("Vui lòng cập nhật ảnh đại diện và ảnh Ảnh bìa")
                        .setBackgroundColorRes(R.color.success_stroke_color) // or setBackgroundColorInt(Color.CYAN)
                        .show();
            }
        });

        firebase_manager.storageRef.child("CuaHang").child(firebase_manager.auth.getUid()).child("WallPaper").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext())
                        .load(uri.toString())
                        .into(binding.imagebackground);
                binding.progessbarWallPaper.setVisibility(View.GONE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                binding.progessbarWallPaper.setVisibility(View.GONE);
                Alerter.create(getActivity())
                        .setTitle("Thông báo")
                        .setText("Vui lòng cập nhật Ảnh bìa và ảnh đại diện")
                        .setBackgroundColorRes(R.color.success_stroke_color) // or setBackgroundColorInt(Color.CYAN)
                        .show();
            }
        });
    }
}