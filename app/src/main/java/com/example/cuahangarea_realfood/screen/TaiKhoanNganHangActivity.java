package com.example.cuahangarea_realfood.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.developer.kalert.KAlertDialog;
import com.example.cuahangarea_realfood.Firebase_Manager;
import com.example.cuahangarea_realfood.R;
import com.example.cuahangarea_realfood.Validate;
import com.example.cuahangarea_realfood.adapter.NganHangAdapter;
import com.example.cuahangarea_realfood.databinding.ActivityTaiKhoanNganHangBinding;
import com.example.cuahangarea_realfood.model.DanhMuc;
import com.example.cuahangarea_realfood.model.NganHang;
import com.example.cuahangarea_realfood.model.NganHangAPI;
import com.example.cuahangarea_realfood.model.NganHangWrapper;
import com.example.cuahangarea_realfood.model.TaiKhoanNganHang;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TaiKhoanNganHangActivity extends AppCompatActivity {
    EditText edtTenChiNhanh,edtSoTaiKhoan,edtTenChuTaiKhoan,edtSoCMND;
    SearchableSpinner spDSNganHang;
    NganHangAdapter nganHangAdapter ;
    Button btnLuuThongTin;
    Validate validate = new Validate();
    Firebase_Manager firebase_manager = new Firebase_Manager();
    ArrayList<NganHang>arrayList = new ArrayList<>();
    NganHangAPI nganHangAPI;
    TaiKhoanNganHang nganHang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tai_khoan_ngan_hang);
        getData();
        setControl();
        setEvent();
        LoadData();
    }

    private void setControl() {
        edtSoCMND = findViewById(R.id.edtSOCMND);
        edtSoTaiKhoan = findViewById(R.id.edtSoTaiKhoan);
        edtTenChiNhanh = findViewById(R.id.edtTenChiNhanh);
        edtTenChuTaiKhoan = findViewById(R.id.edtTenChuTaiKhoan);
        spDSNganHang = findViewById(R.id.spTenNganHang);
        btnLuuThongTin = findViewById(R.id.btnLuuThongTin);
    }

    private void LoadData() {
        firebase_manager.mDatabase.child("TaiKhoanNganHang").orderByChild("idTaiKhoan").equalTo(firebase_manager.auth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull  DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            TaiKhoanNganHang taiKhoanNganHang = postSnapshot.getValue(TaiKhoanNganHang.class);
                            nganHang = taiKhoanNganHang;
                            SetValueToAllField();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void SetValueToAllField() {
        edtSoCMND.setText(nganHang.getSoCMND());
        edtTenChuTaiKhoan.setText(nganHang.getTenChuTaiKhoan());
        edtSoTaiKhoan.setText(nganHang.getSoTaiKhoan());
        edtTenChiNhanh.setText(nganHang.getTenChiNhanh());
        edtSoCMND.setText(nganHang.getSoCMND());
        AtomicInteger positon = new AtomicInteger();
        arrayList.forEach(temp -> {
            if (temp.getName().equals(nganHang.getTenNganHang()))
            {
                spDSNganHang.setSelected(true);
                spDSNganHang.setActivated(true);
                spDSNganHang.dispatchSetSelected(true);
                spDSNganHang.setSelectedItem(positon.get());
            }
            positon.getAndIncrement();
        });

    }

    private void setEvent() {
       btnLuuThongTin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                if (!validate.isBlank(edtTenChiNhanh)
                        &&spDSNganHang.getSelectedItem()!=null
                        &&!validate.isBlank(edtSoTaiKhoan)&&!validate.isBlank(edtTenChuTaiKhoan)
                         &&!validate.isBlank(edtSoCMND)&&validate.isCMND(edtSoCMND)){

                    if (nganHang==null)
                    {
                        KAlertDialog kAlertDialog = new KAlertDialog(TaiKhoanNganHangActivity.this,KAlertDialog.PROGRESS_TYPE)
                                .setContentText("Loading");
                        kAlertDialog.show();
                        String uuid = UUID.randomUUID().toString().replace("-", "");
                        String tenNganHang =nganHangAdapter.getItem(spDSNganHang.getSelectedPosition()).getName() ;
                        TaiKhoanNganHang temp =
                                new TaiKhoanNganHang(uuid,tenNganHang,edtTenChiNhanh.getText().toString()
                                        ,edtSoTaiKhoan.getText().toString(),edtTenChuTaiKhoan.getText().toString(),edtSoCMND.getText().toString(),firebase_manager.auth.getUid());
                        nganHang = temp;
                        firebase_manager.Ghi_NganHang(nganHang).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                kAlertDialog.setContentText("Đã lưu thông tin thành công!");
                                kAlertDialog.changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                kAlertDialog.showConfirmButton(false);
                            }
                        });
                    }
                    else {
                        KAlertDialog kAlertDialog = new KAlertDialog(TaiKhoanNganHangActivity.this,KAlertDialog.PROGRESS_TYPE)
                                .setContentText("Loading");
                        kAlertDialog.show();
                        String uuid = nganHang.getId();
                        String tenNganHang =nganHangAdapter.getItem(spDSNganHang.getSelectedPosition()).getName() ;
                        TaiKhoanNganHang temp =
                                new TaiKhoanNganHang(uuid,tenNganHang,edtTenChiNhanh.getText().toString()
                                        ,edtSoTaiKhoan.getText().toString(),edtTenChuTaiKhoan.getText().toString(),edtSoCMND.getText().toString(),firebase_manager.auth.getUid());
                        nganHang = temp;
                        firebase_manager.Ghi_NganHang(nganHang).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                kAlertDialog.setContentText("Đã lưu thông tin thành công!");
                                kAlertDialog.changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                kAlertDialog.showConfirmButton(false);
                            }
                        });
                    }
                }
           }
       });
    }

    private void getData() {
        arrayList.clear();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(NganHangAPI.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        nganHangAPI = retrofit.create(NganHangAPI.class);
        Call<NganHangWrapper<NganHang>> call = nganHangAPI.getNganHang();
        call.enqueue(new Callback<NganHangWrapper<NganHang>>() {
            @Override
            public void onResponse(Call<NganHangWrapper<NganHang>> call, Response<NganHangWrapper<NganHang>> response) {
                if(response.isSuccessful()){
                    NganHangWrapper<NganHang> wrapper = response.body();
                    assert wrapper !=null;
                    arrayList.addAll(wrapper.items);
                    arrayList.forEach(nganHang -> Log.d("NganHang",nganHang.getName()));
                    nganHangAdapter = new NganHangAdapter(TaiKhoanNganHangActivity.this,R.layout.sp_nganhang_item,R.id.textView,arrayList);
                    spDSNganHang.setAdapter(nganHangAdapter);
                   if (nganHang!= null)
                   {
                       LoadData();
                   }
                }
            }
            @Override
            public void onFailure(Call<NganHangWrapper<NganHang>> call, Throwable t) {
            }
        });

    }
}
