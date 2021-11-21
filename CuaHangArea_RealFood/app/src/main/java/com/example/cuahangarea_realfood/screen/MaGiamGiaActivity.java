package com.example.cuahangarea_realfood.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.cuahangarea_realfood.Firebase_Manager;
import com.example.cuahangarea_realfood.Fragment.DanhMuc_DialogFragment;
import com.example.cuahangarea_realfood.R;
import com.example.cuahangarea_realfood.adapter.MaGiamGiaAdapter;
import com.example.cuahangarea_realfood.databinding.ActivityMaGiamGiaBinding;
import com.example.cuahangarea_realfood.model.Voucher;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MaGiamGiaActivity extends AppCompatActivity {
    ActivityMaGiamGiaBinding binding;
    MaGiamGiaAdapter maGiamGiaAdapter ;
    LinearLayoutManager linearLayoutManager;
    ArrayList<Voucher> vouchers = new ArrayList<>();
    Firebase_Manager firebase_manager = new Firebase_Manager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMaGiamGiaBinding.inflate(getLayoutInflater());
        maGiamGiaAdapter = new MaGiamGiaAdapter(this,R.layout.magiamgia_item,vouchers);
        setContentView(binding.getRoot());
        setEvent();
        LoadData();


    }

    private void LoadData() {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.rcMaGiamGia.setLayoutManager(linearLayoutManager);
        binding.rcMaGiamGia.setAdapter(maGiamGiaAdapter);
        firebase_manager.GetVoucher(vouchers,maGiamGiaAdapter);
    }


    private void setEvent() {
        binding.btnThemMoiVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MaGiamGiaActivity.this, ThongTinMaGiamGiaActivity.class);
                startActivity(intent);
            }
        });
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
                maGiamGiaAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                maGiamGiaAdapter.getFilter().filter(query);
                return true;

            }

        });
        return true;
    }
}