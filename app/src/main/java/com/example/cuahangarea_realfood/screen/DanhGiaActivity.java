package com.example.cuahangarea_realfood.screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.cuahangarea_realfood.Firebase_Manager;
import com.example.cuahangarea_realfood.R;
import com.example.cuahangarea_realfood.adapter.DanhGiaSanPhamAdapter;
import com.example.cuahangarea_realfood.databinding.ActivityDanhGiaBinding;
import com.example.cuahangarea_realfood.model.DanhGia;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.stream.Collectors;

public class DanhGiaActivity extends AppCompatActivity {
    ActivityDanhGiaBinding binding ;
    Firebase_Manager firebase_manager = new Firebase_Manager();
    ArrayList<DanhGia>danhGias = new ArrayList<>();
    ArrayList<DanhGia>temps = new ArrayList<>();
    DanhGiaSanPhamAdapter danhGiaSanPhamAdapter ;
    LinearLayoutManager linearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDanhGiaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        danhGiaSanPhamAdapter = new DanhGiaSanPhamAdapter(this,R.layout.danhgiasanpham,danhGias);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rcDanhGia.setLayoutManager(linearLayoutManager);
        binding.rcDanhGia.setAdapter(danhGiaSanPhamAdapter);
        firebase_manager.GetPhanHoi(temps,danhGiaSanPhamAdapter);
        LoadData();

        binding.dtpGioKetThuc.addOnDateChangedListener(new SingleDateAndTimePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(String displayed, Date date) {
                if (date.compareTo(binding.dtpGioBatDau.getDate())<0){
                    Alerter.create(DanhGiaActivity.this)
                            .setTitle("Lỗi")
                            .setText("Bạn vui lòng chọn thời gian bắt đầu bé hơn thời gian kết thúc")
                            .setBackgroundColorRes(R.color.error_stroke_color) // or setBackgroundColorInt(Color.CYAN)
                            .show();

                    Date temp = binding.dtpGioBatDau.getDate();
                    temp.setDate(temp.getDate()+1);

                    binding.dtpGioKetThuc.setDefaultDate(temp);
                }

            }
        });
        binding.dtpGioBatDau.addOnDateChangedListener(new SingleDateAndTimePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(String displayed, Date date) {
                if (date.compareTo(binding.dtpGioKetThuc.getDate())>0){
                    Alerter.create(DanhGiaActivity.this)
                            .setTitle("Lỗi")
                            .setText("Bạn vui lòng chọn thời gian bắt đầu bé hơn thời gian kết thúc")
                            .setBackgroundColorRes(R.color.error_stroke_color) // or setBackgroundColorInt(Color.CYAN)
                            .show();
                    Date temp = binding.dtpGioKetThuc.getDate();
                    temp.setDate(temp.getDate()-1);
                    binding.dtpGioBatDau.setDefaultDate(temp);
                }
            }
        });
        binding.btnRefesh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadData();
            }
        });
        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<DanhGia>temps = new ArrayList<>();
                Date ngayBatDau = binding.dtpGioBatDau.getDate();
                Date ngayKetThuc = binding.dtpGioKetThuc.getDate();
                for (DanhGia danhGia:danhGias) {


                    if (danhGia.getNgayDanhGia().compareTo(ngayBatDau)>0&&danhGia.getNgayDanhGia().compareTo(ngayKetThuc)<0)
                    {
                        temps.add(danhGia);

                    }

                }
                danhGiaSanPhamAdapter.setDanhGias(temps);
                danhGiaSanPhamAdapter.notifyDataSetChanged();
            }
        });
    }

    private void LoadData() {
        firebase_manager. mDatabase.child("DanhGia").orderByChild("idcuaHang").equalTo(firebase_manager.auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                danhGias.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    DanhGia danhGia = postSnapshot.getValue(DanhGia.class);
                    danhGias.add(danhGia);
                }
                Collections.sort(danhGias, new Comparator<DanhGia>() {
                    @Override
                    public int compare(DanhGia o1, DanhGia o2) {
                        return o2.getNgayDanhGia().compareTo(o1.getNgayDanhGia());
                    }
                });
                danhGiaSanPhamAdapter.setDanhGias(danhGias);
                danhGiaSanPhamAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


}