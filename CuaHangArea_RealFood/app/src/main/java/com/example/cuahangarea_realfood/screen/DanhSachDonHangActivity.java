package com.example.cuahangarea_realfood.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cuahangarea_realfood.Firebase_Manager;
import com.example.cuahangarea_realfood.Fragment.DanhMuc_DialogFragment;
import com.example.cuahangarea_realfood.R;
import com.example.cuahangarea_realfood.TrangThai.TrangThaiDonHang;
import com.example.cuahangarea_realfood.adapter.DanhMucAdapter;
import com.example.cuahangarea_realfood.adapter.DonHangAdapter;
import com.example.cuahangarea_realfood.adapter.SanPhamAdapter;
import com.example.cuahangarea_realfood.model.DanhMuc;
import com.example.cuahangarea_realfood.model.DonHang;
import com.example.cuahangarea_realfood.model.SanPham;
import com.example.cuahangarea_realfood.screen.DS_SanPhamActivity;
import com.example.cuahangarea_realfood.screen.ThongTinSanPhamActivity;

import java.util.ArrayList;

public class DanhSachDonHangActivity extends AppCompatActivity {
    DonHangAdapter donHangAdapter;
    ArrayList<DonHang> donHangs;
    LinearLayoutManager linearLayoutManager, linearLayoutManager2;
    GridLayoutManager gridLayoutManager;
    Firebase_Manager firebase_manager = new Firebase_Manager();
    RecyclerView rcDonHang;
    Spinner spTrangThaiDonHang;
    ProgressBar progressBar;
    @Override
    protected void onResume() {
        //GetSanPham();
        //GetDanhSachDanhMuc();
        donHangAdapter.notifyDataSetChanged();
        super.onResume();
        Log.d("a", "oke");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        donHangs = new ArrayList<DonHang>();
        setContentView(R.layout.activity_danh_sach_don_hang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setControl();
        donHangAdapter = new DonHangAdapter(this, R.layout.donhang_item, donHangs);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        gridLayoutManager = new GridLayoutManager(this, 2);
        rcDonHang.setLayoutManager(linearLayoutManager);
        rcDonHang.setAdapter(donHangAdapter);
        firebase_manager.GetDonHang(donHangs, donHangAdapter,progressBar);
//          GetDanhSachDanhMuc();
//        GetSanPham();
        setEvent();
    }

    private void setEvent() {
        spTrangThaiDonHang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        donHangAdapter.getFilter().filter("");
                        break;
                    case 1:
                        donHangAdapter.getFilter().filter(TrangThaiDonHang.SHOP_ChoXacNhanChuyenTien.toString());
                        break;
                    case 2:
                        donHangAdapter.getFilter().filter(TrangThaiDonHang.SHOP_HuyDonHang.toString());
                        break;
                    case 3:
                        donHangAdapter.getFilter().filter(TrangThaiDonHang.SHOP_DaGiaoChoBep.toString());
                        break;
                    case 4:
                        donHangAdapter.getFilter().filter(TrangThaiDonHang.SHOP_DangChuanBihang.toString());
                        break;
                    case 5:
                        donHangAdapter.getFilter().filter(TrangThaiDonHang.Bep_DaHuyDonHang.toString());
                        break;
                    case 6:
                        donHangAdapter.getFilter().filter(TrangThaiDonHang.SHOP_DaChuanBiXong.toString());
                        break;
                    case 7:
                        donHangAdapter.getFilter().filter(TrangThaiDonHang.Shipper_DangGiaoHang.toString());
                        break;

                    case 8:
                        donHangAdapter.getFilter().filter(TrangThaiDonHang.Shipper_DaChuyenTien.toString());
                        break;
                    case 9:
                        donHangAdapter.getFilter().filter(TrangThaiDonHang.Shipper_DaTraHang.toString());
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void setControl() {
        rcDonHang = findViewById(R.id.rcDonHang);
        spTrangThaiDonHang = findViewById(R.id.spTrangThaiDonHang);
        progressBar = findViewById(R.id.progessbar);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.menuItem_search);
        SearchView searchView =
                (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                donHangAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                donHangAdapter.getFilter().filter(query);
                return true;

            }

        });
        return true;
    }

}