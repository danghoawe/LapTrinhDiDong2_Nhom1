package com.example.cuahangarea_realfood.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.developer.kalert.KAlertDialog;
import com.example.cuahangarea_realfood.Firebase_Manager;
import com.example.cuahangarea_realfood.R;
import com.example.cuahangarea_realfood.TrangThai.TrangThaiCuaHang;
import com.example.cuahangarea_realfood.Validate;
import com.example.cuahangarea_realfood.databinding.ActivityThongTinCuaHangBinding;
import com.example.cuahangarea_realfood.model.CuaHang;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.tapadoo.alerter.Alerter;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.Date;

public class ThongTinCuaHangActivity extends AppCompatActivity {
    ActivityThongTinCuaHangBinding binding;
    Firebase_Manager firebase_manager= new Firebase_Manager();
    CuaHang cuaHang;
    Uri uriAvatar,uriWallpaper;
    Validate validate = new Validate();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityThongTinCuaHangBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.lnLayout.setVisibility(View.GONE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LoadData();
        setEvent();
    }

    private void setEvent() {
        binding.btnLuuThongTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate.isBlank(binding.edtEmail)&&validate.isEmail(binding.edtEmail)
                        &&!validate.isBlank(binding.edtDiaChi)&&!validate.isBlank(binding.edtHotenChu)
                        &&!validate.isBlank(binding.edtSoCMND)&&!validate.isBlank(binding.edtSoDT)
                        &&!validate.isBlank(binding.edtThongTinChiTiet)&&!validate.isBlank(binding.edtTenCuaHang)
                )
                {
                    CuaHang temp = new CuaHang(firebase_manager.auth.getUid(), binding.edtTenCuaHang.getText().toString()
                            , binding.edtHotenChu.getText().toString(), binding.edtThongTinChiTiet.getText().toString()
                            , binding.edtSoCMND.getText().toString(), binding.edtSoDT.getText().toString()
                            , "", "", cuaHang.getRating(), binding.edtEmail.getText().toString()
                            , cuaHang.getTrangThaiCuaHang(),binding.edtDiaChi.getText().toString(),binding.dtpGioBatDau.getDate(),binding.dtpGioKetThuc.getDate(),cuaHang.getCreatedDate());
                    cuaHang = temp;
                    CapNhatCuaHang(cuaHang);
                    if (uriAvatar!=null)
                    {
                        firebase_manager.UpAvatar(uriAvatar).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                LoadData();
                            }
                        });
                    }
                    if (uriWallpaper!=null)
                    {
                        firebase_manager.UpWallPaper(uriWallpaper).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                LoadData();
                            }
                        });
                    }

                }
            }
        });


        binding.btnEditAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.build(new PickSetup())
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {
                                binding.profileImage.setImageBitmap(r.getBitmap());
                                uriAvatar = r.getUri();
                            }
                        })
                        .setOnPickCancel(new IPickCancel() {
                            @Override
                            public void onCancelClick() {
                                //TODO: do what you have to if user clicked cancel
                            }
                        }).show(ThongTinCuaHangActivity.this);
            }
        });
        binding.dtpGioKetThuc.addOnDateChangedListener(new SingleDateAndTimePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(String displayed, Date date) {
                if (date.compareTo(binding.dtpGioBatDau.getDate())<0){
                    Alerter.create(ThongTinCuaHangActivity.this)
                            .setTitle("Lỗi")
                            .setText("Bạn vui lòng chọn thời gian bắt đầu bé hơn thời gian kết thúc")
                            .setBackgroundColorRes(R.color.error_stroke_color) // or setBackgroundColorInt(Color.CYAN)
                            .show();
                    Date temp = binding.dtpGioBatDau.getDate();
                    temp.setHours(temp.getHours()+1);

                    binding.dtpGioKetThuc.setDefaultDate(temp);
                }

            }
        });
        binding.dtpGioBatDau.addOnDateChangedListener(new SingleDateAndTimePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(String displayed, Date date) {
                if (date.compareTo(binding.dtpGioKetThuc.getDate())>0){
                   Alerter.create(ThongTinCuaHangActivity.this)
                           .setTitle("Lỗi")
                           .setText("Bạn vui lòng chọn thời gian bắt đầu bé hơn thời gian kết thúc")
                           .setBackgroundColorRes(R.color.error_stroke_color) // or setBackgroundColorInt(Color.CYAN)
                           .show();
                    Date temp = binding.dtpGioKetThuc.getDate();
                    temp.setHours(temp.getHours()-1);

                    binding.dtpGioBatDau.setDefaultDate(temp);
                }
            }


        });
        binding.btnEditWallPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.build(new PickSetup())
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {
                                binding.imgWallPaper.setImageBitmap(r.getBitmap());
                                uriWallpaper = r.getUri();
                            }
                        })
                        .setOnPickCancel(new IPickCancel() {
                            @Override
                            public void onCancelClick() {
                                //TODO: do what you have to if user clicked cancel
                            }
                        }).show(ThongTinCuaHangActivity.this);
            }
        });
    }
    public void CapNhatCuaHang (CuaHang cuaHang){
        KAlertDialog kAlertDialog = new KAlertDialog(ThongTinCuaHangActivity.this,KAlertDialog.PROGRESS_TYPE).setContentText("Loading");
        kAlertDialog.show();
        kAlertDialog.showConfirmButton(false);
        firebase_manager.Ghi_CuaHang(cuaHang).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                kAlertDialog.changeAlertType(KAlertDialog.SUCCESS_TYPE);
                kAlertDialog.setContentText("Trạng thái đã cửa hàng đã được cập nhật!");
                kAlertDialog.showConfirmButton(false);
                LoadData();
            }
        });
    }

    private void LoadData() {
       firebase_manager. mDatabase.child("CuaHang").child(firebase_manager.auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CuaHang temp = dataSnapshot.getValue(CuaHang.class);
                cuaHang = temp;
                binding.edtDiaChi.setText( temp.getDiaChi());
                binding.edtTenCuaHang.setText(temp.getTenCuaHang());
                binding.edtEmail.setText(temp.getEmail());
                binding.edtSoDT.setText(temp.getSoDienThoai());
                binding.edtSoCMND.setText(temp.getSoCMND());
                binding.edtHotenChu.setText(temp.getChuSoHuu());
                binding.edtThongTinChiTiet.setText(temp.getThongTinChiTiet());
                binding.progessbar.setVisibility(View.GONE);
                binding.lnLayout.setVisibility(View.VISIBLE);
                binding.dtpGioBatDau.setDefaultDate(temp.getTimeStart());
                binding.dtpGioKetThuc.setDefaultDate(temp.getTimeEnd());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
       firebase_manager.storageRef.child("CuaHang").child(firebase_manager.auth.getUid()).child("Avatar").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
           @Override
           public void onSuccess(Uri uri) {
               Glide.with(ThongTinCuaHangActivity.this)
                       .load(uri.toString())
                       .into(binding.profileImage);

           }
       });
        firebase_manager.storageRef.child("CuaHang").child(firebase_manager.auth.getUid()).child("WallPaper").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ThongTinCuaHangActivity.this)
                        .load(uri.toString())
                        .into(binding.imgWallPaper);
            }
        });

    }
}