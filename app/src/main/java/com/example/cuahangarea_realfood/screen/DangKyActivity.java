package com.example.cuahangarea_realfood.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.kalert.KAlertDialog;
import com.example.cuahangarea_realfood.Firebase_Manager;
import com.example.cuahangarea_realfood.R;
import com.example.cuahangarea_realfood.TrangThai.TrangThaiCuaHang;
import com.example.cuahangarea_realfood.Validate;
import com.example.cuahangarea_realfood.model.CuaHang;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.tapadoo.alerter.Alerter;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.Date;

public class DangKyActivity extends AppCompatActivity {
    EditText edtEmail, edtMatKhau, edtHoTen, edtTenCuaHang, edtDiaChi, edtSoDienThoai, edtReMatKhau, edtIDcard;
    Button btnDangKy;
    ImageView imgCMND_Truoc, imgCMND_Sau;
    FirebaseAuth auth;
    TextView txtDangNhap;
    KAlertDialog kAlertDialog;
    ViewGroup viewGroup;
    Validate validate = new Validate();
    Uri CMND_truoc;
    Uri CMND_sau;
    Firebase_Manager firebase_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        auth = FirebaseAuth.getInstance();
        firebase_manager = new Firebase_Manager();
        setControl();
        setEvent();
    }

    private void setEvent() {
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Validated_Form()) {
                    KAlertDialog kAlertDialog = new KAlertDialog(DangKyActivity.this, KAlertDialog.PROGRESS_TYPE).setContentText("Loading");
                    kAlertDialog.show();
                    auth.createUserWithEmailAndPassword(edtEmail.getText().toString(), edtMatKhau.getText().toString()).
                            addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    String uuid = authResult.getUser().getUid();
                                    CuaHang cuaHang = new CuaHang(uuid, edtTenCuaHang.getText().toString()
                                            , edtHoTen.getText().toString(), "", edtIDcard.getText().toString(), edtSoDienThoai.getText().toString(), "", "", (float) 0.0, edtEmail.getText().toString(), TrangThaiCuaHang.DaKichHoat, edtDiaChi.getText().toString(), null, null, new Date());
                                    firebase_manager.Ghi_CuaHang(cuaHang);
                                    firebase_manager.Up2MatCMND(CMND_truoc, CMND_sau, uuid);
                                    kAlertDialog.changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                    kAlertDialog.setTitleText("Đăng ký tài khỏan thành công ");
                                    kAlertDialog.setContentText("Vui lòng đợi Admin xét duyệt và kích hoạt tài khoản trong vòng 24h!");
//                                    Dialog a = new NordanAlertDialog.Builder(DangKyActivity.this)
//                                            .setAnimation(Animation.SIDE)
//                                            .isCancellable(false)
//                                            .setTitle("Thông báo")
//                                            .setMessage("Thêm danh mục thành công")
//                                            .setPositiveBtnText("Ok")
//                                            .setDialogType(DialogType.SUCCESS)
//                                            .onPositiveClicked(() -> {/* Do something here */})
//                                            .build();
//                                    a.show();

                                    clearForm(viewGroup);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            kAlertDialog.changeAlertType(KAlertDialog.WARNING_TYPE);
                            kAlertDialog.setContentText(e.getMessage());
                        }
                    });
                }
            }
        });
        txtDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgCMND_Truoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.build(new PickSetup())
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {
                                imgCMND_Truoc.setImageBitmap(r.getBitmap());
                                CMND_truoc = r.getUri();
                            }
                        })
                        .setOnPickCancel(new IPickCancel() {
                            @Override
                            public void onCancelClick() {
                                //TODO: do what you have to if user clicked cancel
                            }
                        }).show(DangKyActivity.this);
            }
        });
        imgCMND_Sau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.build(new PickSetup())
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {
                                imgCMND_Sau.setImageBitmap(r.getBitmap());
                                CMND_sau = r.getUri();
                            }
                        })
                        .setOnPickCancel(new IPickCancel() {
                            @Override
                            public void onCancelClick() {
                                //TODO: do what you have to if user clicked cancel
                            }
                        }).show(DangKyActivity.this);
            }
        });
    }

    private boolean Validated_Form() {
        boolean result = false;
        if (!validate.isBlank(edtEmail) && validate.isEmail(edtEmail)
                && !validate.isBlank(edtHoTen) && !validate.isBlank(edtIDcard) && validate.isCMND(edtIDcard) && !validate.isBlank(edtTenCuaHang) && !validate.moreThan50Char(edtTenCuaHang)
                && !validate.isBlank(edtDiaChi) && validate.isPhone(edtSoDienThoai)
                && !validate.isBlank(edtMatKhau) && !validate.lessThan6Char(edtMatKhau)
                && !validate.isBlank(edtReMatKhau) && !validate.lessThan6Char(edtReMatKhau)
        ) {
            edtReMatKhau.setError(null);
            if (edtReMatKhau.getText().toString().equals(edtMatKhau.getText().toString())) {
                result = true;

                if (CMND_sau == null || CMND_truoc == null) {
                    Alerter.create(DangKyActivity.this)
                            .setTitle("Lỗi")
                            .setText("Vui lòng tải lên 2 mặt CMND/CCCD để xác thưc")
                            .setBackgroundColorRes(R.color.error_stroke_color) // or setBackgroundColorInt(Color.CYAN)
                            .show();
                    result = false;
                }
            } else {
                result = false;
                edtReMatKhau.setError("Mật khẩu không trùng khớp");
            }
        }

        return result;
    }

    private void setControl() {
        edtEmail = findViewById(R.id.edtEmail);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        edtHoTen = findViewById(R.id.edtHoten);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        edtTenCuaHang = findViewById(R.id.edtTenCuaHang);
        edtSoDienThoai = findViewById(R.id.edtSoDT);
        edtReMatKhau = findViewById(R.id.edtNhapLaiMatKhau);

        imgCMND_Sau = findViewById(R.id.imgCMND_Sau);
        imgCMND_Truoc = findViewById(R.id.imgCMND_Truoc);

        btnDangKy = findViewById(R.id.btnDangKy);
        txtDangNhap = findViewById(R.id.txtDangNhap);
        edtIDcard = findViewById(R.id.edtIDCard);
        viewGroup = findViewById(R.id.linear1);
    }

    private void clearForm(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).setText("");
            }
            if (view instanceof ImageView) {
                if (view.getId() == R.id.imgCMND_Truoc)
                {
                    ((ImageView) view).setImageResource(R.drawable.idtruoc);
                }
                else {
                    ((ImageView) view).setImageResource(R.drawable.idsau);
                }
            }
            if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                clearForm((ViewGroup) view);
        }
    }
}