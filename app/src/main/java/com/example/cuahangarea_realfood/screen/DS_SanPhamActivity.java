package com.example.cuahangarea_realfood.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.cuahangarea_realfood.Fragment.DanhMuc_DialogFragment;
import com.example.cuahangarea_realfood.Firebase_Manager;
import com.example.cuahangarea_realfood.R;
import com.example.cuahangarea_realfood.SetOnClick;
import com.example.cuahangarea_realfood.SetOnLongClick;
import com.example.cuahangarea_realfood.adapter.DanhMucAdapter;
import com.example.cuahangarea_realfood.adapter.SanPhamAdapter;
import com.example.cuahangarea_realfood.model.DanhMuc;
import com.example.cuahangarea_realfood.model.SanPham;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;


import java.util.ArrayList;

public class DS_SanPhamActivity extends AppCompatActivity {
    DanhMucAdapter danhMucAdapter;
    SanPhamAdapter sanPhamAdapter;
    ArrayList<DanhMuc> danhMucs;
    ArrayList<SanPham> sanPhams;
    LinearLayoutManager linearLayoutManager,linearLayoutManager2;
    GridLayoutManager gridLayoutManager ;
    Button btnThemSanPham;
    Firebase_Manager firebase_manager = new Firebase_Manager();
    RecyclerView rcDanhMuc,rcSanPham;
    SetOnLongClick setOnLongClick;
    SearchView search;

    SetOnClick setOnClick ;
    @Override
    protected void onResume() {
        //GetSanPham();
        //GetDanhSachDanhMuc();
        sanPhamAdapter.notifyDataSetChanged();
        danhMucAdapter.notifyDataSetChanged();
        super.onResume();
        Log.d("a","oke");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        WorkRequest uploadWorkRequest =
//                new OneTimeWorkRequest.Builder(LoadWorker.class)
//                        .build();
//
//        WorkManager
//                .getInstance(this)
//                .enqueue(uploadWorkRequest);

        danhMucs = new ArrayList<DanhMuc>();
        sanPhams = new ArrayList<SanPham>();
        setContentView(R.layout.activity_ds_san_pham);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setControl();
        ;
        danhMucAdapter = new DanhMucAdapter(this, R.layout.danhmuc_item, danhMucs);

        sanPhamAdapter = new SanPhamAdapter(this, R.layout.sanpham_item, sanPhams);

        setOnLongClick = new SetOnLongClick() {
            @Override
            public void onLongClick(int positon) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                DanhMuc_DialogFragment danhMuc_dialogFragment = new DanhMuc_DialogFragment(danhMucs.get(positon));
                danhMuc_dialogFragment.onActivityResult(1, 1, null);
                danhMuc_dialogFragment.show(fragmentManager, "DS_SanPhamActivity");
            }
        };
        setOnClick = new SetOnClick() {
            @Override
            public void onClick(int positon) {
                sanPhamAdapter.getFilter().filter(danhMucs.get(positon).getIDDanhMuc());
            }
        };
        danhMucAdapter.setSetOnClick(setOnClick);
        danhMucAdapter.setSetOnLongClick(setOnLongClick);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rcDanhMuc.setLayoutManager(linearLayoutManager);
        rcDanhMuc.setAdapter(danhMucAdapter);
        gridLayoutManager = new GridLayoutManager(this,2);
        rcSanPham.setLayoutManager(gridLayoutManager);
        rcSanPham.setAdapter(sanPhamAdapter);
        firebase_manager.GetSanPham(sanPhams,sanPhamAdapter);
        firebase_manager.GetDanhSachDanhMuc(danhMucs,danhMucAdapter);
//          GetDanhSachDanhMuc();
//        GetSanPham();
        setEvent();
    }

    private void setEvent() {
        btnThemSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DS_SanPhamActivity.this, ThongTinSanPhamActivity.class);
                startActivity(intent);
            }
        });
    }



    private void setControl() {
        rcDanhMuc = findViewById(R.id.rcDanhMuc);
        rcSanPham = findViewById(R.id.rcSanPham);
        btnThemSanPham = findViewById(R.id.btnThemSanPham);

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
                sanPhamAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                sanPhamAdapter.getFilter().filter(query);
                return true;

            }

        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Context context = this;
        FragmentManager fragmentManager = getSupportFragmentManager();
        DanhMuc_DialogFragment danhMuc_dialogFragment = new DanhMuc_DialogFragment(null);
        switch (item.getItemId()) {
            case R.id.mnThemDanhMuc:

                if (getFragmentManager() != null) {
                    danhMuc_dialogFragment.onActivityResult(1, 1, null);
                    danhMuc_dialogFragment.show(fragmentManager, "DS_SanPhamActivity");
                }
                break;
            case R.id.mnThemSanPham:
                Intent intent = new Intent(DS_SanPhamActivity.this, ThongTinSanPhamActivity.class);
                startActivity(intent);

                break;
            case R.id.mnRefesh:
                firebase_manager.GetDanhSachDanhMuc(danhMucs,danhMucAdapter);

                break;

        }
        return super.onOptionsItemSelected(item);
    }

}