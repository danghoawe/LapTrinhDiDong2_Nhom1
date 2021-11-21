package com.example.cuahangarea_realfood.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cuahangarea_realfood.Firebase_Manager;
import com.example.cuahangarea_realfood.R;
import com.example.cuahangarea_realfood.TrangThai.TrangThaiThongBao;
import com.example.cuahangarea_realfood.adapter.ThongBaoAdapter;
import com.example.cuahangarea_realfood.databinding.FragmentNotificationBinding;
import com.example.cuahangarea_realfood.model.DanhMuc;
import com.example.cuahangarea_realfood.model.ThongBao;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;


public class NotificationFragment extends Fragment {

    ArrayList<ThongBao> thongBaos = new ArrayList<>();
    Firebase_Manager firebase_manager = new Firebase_Manager();
    FragmentNotificationBinding binding;
    LinearLayoutManager linearLayoutManager;
    ThongBaoAdapter thongBaoAdapter ;
    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(getLayoutInflater());
        thongBaoAdapter = new ThongBaoAdapter(getActivity(),R.layout.thongbao_item,thongBaos);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        LoadData();

        return binding.getRoot();
    }
    private void LoadAlert() {
        if (thongBaos.isEmpty())
        {
            binding.txtAlert.setVisibility(View.VISIBLE);
            binding.lnNoti.setVisibility(View.GONE);
        }
    }
    private void LoadData() {
        firebase_manager.mDatabase.child("ThongBao").child(firebase_manager.auth.getUid()).orderByChild("trangThaiThongBao").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    thongBaos.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        ThongBao thongBao = postSnapshot.getValue(ThongBao.class);
                        thongBaos.add(thongBao);
                        thongBaoAdapter.notifyDataSetChanged();
                    }
                    binding.rcNotification.setLayoutManager(linearLayoutManager);
                    binding.rcNotification.setAdapter(thongBaoAdapter);
                    binding.pdLoad.setVisibility(View.GONE);
                    LoadAlert();
                }
                else {
                    binding.pdLoad.setVisibility(View.GONE);
                    Alerter.create(getActivity())
                            .setTitle("Thông báo")
                            .setText("Bạn chưa có thông báo nào :((")
                            .setBackgroundColorRes(R.color.success_stroke_color) // or setBackgroundColorInt(Color.CYAN)
                            .show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        binding.btnDanhDauLaDaDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.pdLoad.setVisibility(View.VISIBLE);
                thongBaos.forEach(thongBao -> {
                    if (thongBao.getTrangThaiThongBao()== TrangThaiThongBao.ChuaXem)
                    {
                        thongBao.setTrangThaiThongBao(TrangThaiThongBao.DaXem);
                        firebase_manager.Ghi_ThongBao(thongBao);
                        binding.pdLoad.setVisibility(View.GONE);
                    }
                    thongBaoAdapter.notifyDataSetChanged();
                });
                binding.pdLoad.setVisibility(View.GONE);
            }
        });
    }
}