package com.example.cuahangarea_realfood.screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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

import com.example.cuahangarea_realfood.Firebase_Manager;
import com.example.cuahangarea_realfood.R;
import com.example.cuahangarea_realfood.TrangThai.TrangThaiDonHang;
import com.example.cuahangarea_realfood.adapter.DonHangAdapter;
import com.example.cuahangarea_realfood.adapter.DonHang_BepAdapter;
import com.example.cuahangarea_realfood.model.DonHang;

import java.util.ArrayList;

public class BepActivity extends AppCompatActivity {
    DonHang_BepAdapter donHang_bepAdapter;
    ArrayList<DonHang> donHangs;
    LinearLayoutManager linearLayoutManager, linearLayoutManager2;
    GridLayoutManager gridLayoutManager;
    Button btnThemSanPham;
    Firebase_Manager firebase_manager = new Firebase_Manager();
    RecyclerView rcDonHang;
    Spinner spLoaiDonHang;
    ProgressBar progressBar;
    @Override
    protected void onResume() {
        //GetSanPham();
        //GetDanhSachDanhMuc();
        donHang_bepAdapter.notifyDataSetChanged();
        super.onResume();
        Log.d("a", "oke");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        donHangs = new ArrayList<DonHang>();
        setContentView(R.layout.activity_bep);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setControl();
        donHang_bepAdapter = new DonHang_BepAdapter(this, R.layout.donhang_bep_item, donHangs);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        gridLayoutManager = new GridLayoutManager(this, 2);
        rcDonHang.setLayoutManager(linearLayoutManager);
        rcDonHang.setAdapter(donHang_bepAdapter);
        firebase_manager.GetDonHang_Bep(donHangs, donHang_bepAdapter,progressBar);
        setEvent();
    }

    private void setEvent() {
        spLoaiDonHang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position)
                    {
                        case 0 :
                            donHang_bepAdapter.getFilter().filter("");
                            break;
                        case 1 :
                            donHang_bepAdapter.getFilter().filter(TrangThaiDonHang.SHOP_DaGiaoChoBep.toString());
                            break;
                        case 2 :
                            donHang_bepAdapter.getFilter().filter(TrangThaiDonHang.SHOP_DangChuanBihang.toString());
                            break;
                        case 3 :
                            donHang_bepAdapter.getFilter().filter(TrangThaiDonHang.SHOP_DaChuanBiXong.toString());
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
        spLoaiDonHang = findViewById(R.id.spLoaiDonHang);
        progressBar = findViewById(R.id.progessbar);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_magiamgia, menu);
        MenuItem searchItem = menu.findItem(R.id.menuItem_search);
        SearchView searchView =
                (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                donHang_bepAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                donHang_bepAdapter.getFilter().filter(query);
                return true;
            }

        });
        return true;
    }
}