package com.example.cuahangarea_realfood.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.developer.kalert.KAlertDialog;
import com.example.cuahangarea_realfood.Firebase_Manager;
import com.example.cuahangarea_realfood.R;
import com.example.cuahangarea_realfood.Validate;
import com.example.cuahangarea_realfood.model.SanPham;
import com.example.cuahangarea_realfood.model.Voucher;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.tapadoo.alerter.Alerter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;

public class ThongTinMaGiamGiaActivity extends AppCompatActivity {
    EditText edtMaGiamGia, edtSoTienGiam, edtGiamTheoPhanTram;
    TextInputLayout tiplGiamTheoPhanTram, tiplGiamTheoGia,tiplHanSuaDung;
    Spinner spSanPham;
    SingleDateAndTimePicker dateAndTimePicker;
    ArrayAdapter adapter;
    List<String> namesSanPham = new ArrayList<>();
    ArrayList<SanPham> dSSanPham = new ArrayList<>();
    Firebase_Manager firebase_manager;
    Button btnLuuMaGiamGia,btnHanSuDung,btnXoa;
    RadioButton radGiamTheoGia, radGiamTheoPhanTram;
    Voucher voucher;
    Validate validate = new Validate();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_ma_giam_gia);
        firebase_manager = new Firebase_Manager();
        setControl();
        setEvent();
        if (getIntent() != null && getIntent().getExtras() != null) {
            Intent intent = getIntent();
            String dataDonHang = intent.getStringExtra("voucher");
            Gson gson = new Gson();
            voucher = gson.fromJson(dataDonHang, Voucher.class);
            LoadData();
        }
        loadLoaiGiamGia();
    }
    private void LoadData() {
        if (voucher != null) {
            if (voucher.getPhanTramGiam() == 0.0) {
                radGiamTheoGia.setChecked(true);
                loadLoaiGiamGia();
                edtSoTienGiam.setText(voucher.getGiaGiam() + "");
                LoadButton();
            } else {
                radGiamTheoPhanTram.setChecked(true);
                edtGiamTheoPhanTram.setText(voucher.getPhanTramGiam() + "");
                loadLoaiGiamGia();
                LoadButton();
            }
            AtomicInteger positon = new AtomicInteger();
            dSSanPham.forEach(temp -> {
                if (temp.getIDSanPham().equals(voucher.getSanPham().getIDSanPham())) {
                    spSanPham.setSelected(true);
                    spSanPham.setActivated(true);
                    spSanPham.dispatchSetSelected(true);
                    spSanPham.setSelection(positon.get());
                }
                positon.getAndIncrement();
            });
            dateAndTimePicker.setDefaultDate(voucher.getHanSuDung());
            edtMaGiamGia.setText(voucher.getMaGiamGia());

        }


    }

    private void loadLoaiGiamGia() {


        if (radGiamTheoGia.isChecked()) {
            edtSoTienGiam.setVisibility(View.VISIBLE);
            tiplGiamTheoGia.setVisibility(View.VISIBLE);
            edtGiamTheoPhanTram.setVisibility(View.GONE);
            tiplGiamTheoPhanTram.setVisibility(View.GONE);
        } else {
            edtSoTienGiam.setVisibility(View.GONE);
            tiplGiamTheoGia.setVisibility(View.GONE);
            edtGiamTheoPhanTram.setVisibility(View.VISIBLE);
            tiplGiamTheoPhanTram.setVisibility(View.VISIBLE);

        }
    }

    public void GetSanPham_V2(ArrayList arrayList, ArrayAdapter sanPhamAdapter) {
        firebase_manager.mDatabase.child("SanPham").orderByChild("idcuaHang").equalTo(firebase_manager.auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    SanPham sanPham = postSnapshot.getValue(SanPham.class);
                    arrayList.add(sanPham.getTenSanPham());
                    dSSanPham.add(sanPham);
                }
                sanPhamAdapter.notifyDataSetChanged();
                LoadData();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void setEvent() {

        adapter = new ArrayAdapter(ThongTinMaGiamGiaActivity.this, android.R.layout.simple_expandable_list_item_1, namesSanPham);
        Date date = dateAndTimePicker.getDate();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formatter.format(date);
        GetSanPham_V2((ArrayList) namesSanPham, adapter);
        spSanPham.setAdapter(adapter);
        btnLuuMaGiamGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate.isBlank(edtMaGiamGia) && spSanPham.getSelectedItem() != null) {

                    firebase_manager.mDatabase.child("Voucher").orderByChild("idCuaHang").equalTo(dSSanPham.get(spSanPham.getSelectedItemPosition()).getIDCuaHang()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            boolean checkExistVoucher = true;
                            ArrayList<Voucher> vouchers = new ArrayList<>();
                            for (DataSnapshot snapshot : task.getResult().getChildren()) {
                                Voucher temp = snapshot.getValue(Voucher.class);
                                vouchers.add(temp);
                                if (temp.getSanPham().getIDSanPham().equals(dSSanPham.get(spSanPham.getSelectedItemPosition()).getIDSanPham()))
                                {
                                    checkExistVoucher = false;
                                    Toast.makeText(ThongTinMaGiamGiaActivity.this, "Sản phẩm đã tồn tại mã giảm giá", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                                if (temp.getMaGiamGia().equals(edtMaGiamGia.getText().toString()))
                                {
                                    checkExistVoucher = false;
                                    Toast.makeText(ThongTinMaGiamGiaActivity.this, "Mã giảm giá đã tồn tại", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                            if (checkExistVoucher == true)
                            {
                                Voucher temp = new Voucher();
                                if (radGiamTheoGia.isChecked()) {
                                    if (!validate.isBlank(edtSoTienGiam) && validate.isNumber(edtSoTienGiam) && spSanPham.getSelectedItem() != null) {

                                        double tien = Double.parseDouble(edtSoTienGiam.getText().toString() + "");
                                        if (tien > 0 && tien < Double.parseDouble(dSSanPham.get(spSanPham.getSelectedItemPosition()).getGia())) {
                                            KAlertDialog kAlertDialog = new KAlertDialog(ThongTinMaGiamGiaActivity.this, KAlertDialog.PROGRESS_TYPE);
                                            kAlertDialog.setContentText("Loading");
                                            kAlertDialog.show();
                                            String uuid;
                                            if (voucher == null) {
                                                uuid = UUID.randomUUID().toString().replace("-", "");
                                            } else {
                                                uuid = voucher.getIdMaGiamGia();
                                            }
                                            temp = new Voucher(uuid
                                                    , dSSanPham.get(spSanPham.getSelectedItemPosition()).getIDCuaHang(), edtMaGiamGia.getText().toString(), dSSanPham.get(spSanPham.getSelectedItemPosition()), Integer.parseInt(edtSoTienGiam.getText().toString() + "")
                                                    , 0, new Date(), dateAndTimePicker.getDate());

                                            firebase_manager.Ghi_Voucher(temp).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    kAlertDialog.changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                                    kAlertDialog.setContentText("Lưu mã giảm giá thành công!");
                                                    kAlertDialog.showConfirmButton(false);
                                                }
                                            });
                                            LoadButton();
                                            voucher = temp;
                                        } else {
                                            edtSoTienGiam.setError("Số tiền không hợp lệ");
                                        }
                                    }
                                } else {
                                    if (!validate.isBlank(edtGiamTheoPhanTram) && validate.isNumber(edtGiamTheoPhanTram)) {
                                        double phanTram = Double.parseDouble(edtGiamTheoPhanTram.getText().toString() + "");
                                        if (phanTram < 100.0 && phanTram > 0) {
                                            KAlertDialog kAlertDialog = new KAlertDialog(ThongTinMaGiamGiaActivity.this, KAlertDialog.PROGRESS_TYPE);
                                            kAlertDialog.setContentText("Loading");
                                            kAlertDialog.show();
                                            String uuid;
                                            if (voucher == null) {
                                                uuid = UUID.randomUUID().toString().replace("-", "");
                                            } else {
                                                uuid = voucher.getIdMaGiamGia();
                                            }

                                            temp = new Voucher(uuid
                                                    , dSSanPham.get(spSanPham.getSelectedItemPosition()).getIDCuaHang(), edtMaGiamGia.getText().toString(), dSSanPham.get(spSanPham.getSelectedItemPosition()), 0
                                                    , Integer.parseInt(edtGiamTheoPhanTram.getText().toString() + ""), new Date(), dateAndTimePicker.getDate());

                                            firebase_manager.Ghi_Voucher(temp).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    kAlertDialog.changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                                    kAlertDialog.setContentText("Lưu mã giảm giá thành công!");
                                                    kAlertDialog.showConfirmButton(false);
                                                }
                                            });
                                            voucher = temp;
                                            LoadButton();
                                        } else {
                                            edtGiamTheoPhanTram.setError("Số % không hợp lệ");
                                        }
                                    }
                                }
                            }

                        }
                    });


                }

            }
        });
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebase_manager.mDatabase.child("Voucher").child(voucher.getIdMaGiamGia()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull  Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            KAlertDialog kAlertDialog = new KAlertDialog(ThongTinMaGiamGiaActivity.this,KAlertDialog.SUCCESS_TYPE).setContentText("Đã xóa thành công mã giảm giá");
                            kAlertDialog.show();
                            kAlertDialog.setConfirmText("Oke");
                            kAlertDialog.setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog kAlertDialog) {
                                    finish();
                                }
                            });
                            LoadButton();
                        }
                    }
                });
            }
        });
        radGiamTheoPhanTram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLoaiGiamGia();
            }
        });
        radGiamTheoGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLoaiGiamGia();
            }
        });

    }

    private void LoadButton() {
        if (voucher!=null)
        {
            btnXoa.setVisibility(View.VISIBLE);
        }
        else {
            btnXoa.setVisibility(View.GONE);

        }
    }

    private void setControl() {
        edtMaGiamGia = findViewById(R.id.edtMaGiamGia);
        edtGiamTheoPhanTram = findViewById(R.id.edtGiamPhanTram);
        edtSoTienGiam = findViewById(R.id.edtSoTienGiam);
        spSanPham = findViewById(R.id.spDSsanPham);
        dateAndTimePicker = findViewById(R.id.single_day_picker);
        btnLuuMaGiamGia = findViewById(R.id.btnLuuMaGiamGia);
        radGiamTheoGia = findViewById(R.id.radGiamTheoGia);
        radGiamTheoPhanTram = findViewById(R.id.radGiamTheoPhanTram);
        tiplGiamTheoGia = findViewById(R.id.tiplGiamTheoGia);
        tiplGiamTheoPhanTram = findViewById(R.id.tiplGiamTheoPhanTram);
        btnXoa = findViewById(R.id.btnXoa);

    }
}