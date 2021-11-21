package com.example.cuahangarea_realfood.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.cuahangarea_realfood.Firebase_Manager;
import com.example.cuahangarea_realfood.R;
import com.example.cuahangarea_realfood.screen.ThongTinCuaHangActivity;
import com.example.cuahangarea_realfood.databinding.FragmentStoreBinding;
import com.example.cuahangarea_realfood.model.CuaHang;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tapadoo.alerter.Alerter;

import java.text.SimpleDateFormat;


public class StoreFragment extends Fragment {

    FragmentStoreBinding binding;
    Firebase_Manager firebase_manager = new Firebase_Manager();
    public StoreFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStoreBinding.inflate(getLayoutInflater());
        LoadData();
        binding.btnLuuThongTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ThongTinCuaHangActivity.class);
                getActivity().startActivity(intent);
            }
        });
        return binding.getRoot();

    }
    private void LoadData() {
        binding.lnLayout.setVisibility(View.GONE);
        firebase_manager. mDatabase.child("CuaHang").child(firebase_manager.auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                CuaHang temp = dataSnapshot.getValue(CuaHang.class);
                SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");
                if (temp.getTimeStart()!=null&&temp.getTimeEnd()!=null){
                    String strTimeStart= formatter.format(temp.getTimeStart());
                    String strTimeEnd= formatter.format(temp.getTimeEnd());
                    binding.txtThoiGianHoatDong.setText(strTimeStart + " đến " +strTimeEnd );
                }


                binding.txtDiaChi.setText( temp.getDiaChi());
                binding.txtTenCuaHang.setText(temp.getTenCuaHang());
                binding.txtEmail.setText(temp.getEmail());
                binding.txtSoDT.setText(temp.getSoDienThoai());
                binding.txtSoCMND.setText(temp.getSoCMND());
                binding.txtChuSoHu.setText(temp.getChuSoHuu());
                binding.txtThongTinChiTiet.setText(temp.getThongTinChiTiet());
                binding.progessbar.setVisibility(View.GONE);
                binding.lnLayout.setVisibility(View.VISIBLE);

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

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                binding.progessbarAvatar.setVisibility(View.GONE);
                Alerter.create(getActivity())
                        .setTitle("Thông báo")
                        .setText("Vui lòng cập nhật Ảnh bìa và ảnh đại diện")
                        .setBackgroundColorRes(R.color.success_stroke_color) // or setBackgroundColorInt(Color.CYAN)
                        .show();
            }
        });
        firebase_manager.storageRef.child("CuaHang").child(firebase_manager.auth.getUid()).child("WallPaper").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext())
                        .load(uri.toString())
                        .into(binding.imgWallPaper);
                binding.progessbarWallPaper.setVisibility(View.GONE);

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        binding.progessbarWallPaper.setVisibility(View.GONE);
                        Alerter.create(getActivity())
                                .setTitle("Thông báo")
                                .setText("Vui lòng cập nhật Ảnh bìa và ảnh đại diện")
                                .setBackgroundColorRes(R.color.success_stroke_color) // or setBackgroundColorInt(Color.CYAN)
                                .show();
                    }
                });;

    }
}