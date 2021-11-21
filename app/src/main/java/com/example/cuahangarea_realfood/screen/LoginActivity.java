package com.example.cuahangarea_realfood.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.developer.kalert.KAlertDialog;
import com.example.cuahangarea_realfood.Firebase_Manager;
import com.example.cuahangarea_realfood.R;
import com.example.cuahangarea_realfood.TrangThai.TrangThaiCuaHang;
import com.example.cuahangarea_realfood.Validate;
import com.example.cuahangarea_realfood.model.CuaHang;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText edtEmail, edtMatKhau;
    TextView txtDangKy,txtQuenMatKhau;
    Button btnDangNhap;
    FirebaseAuth auth;
    Firebase_Manager firebase_manager = new Firebase_Manager();
    Validate validate = new Validate();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser()!=null)
        {
            Intent intent = new Intent(LoginActivity.this, Home.class);
            startActivity(intent);
        }
        this.getSupportActionBar().hide();
        setControl();
        setEvent();
    }

    private void setEvent() {
        Context context = this;
        txtDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DangKyActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_right);
            }
        });
        txtQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, QuenMatKhauActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_right);
            }
        });
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                KAlertDialog kAlertDialog = new KAlertDialog(context);

                if (Validated_Form()) {
                    kAlertDialog.setTitleText("Loading... ");
                    kAlertDialog.changeAlertType(KAlertDialog.PROGRESS_TYPE);
                    kAlertDialog.show();

                    auth.signInWithEmailAndPassword(edtEmail.getText().toString(), edtMatKhau.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull  Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                firebase_manager. mDatabase.child("CuaHang").child(firebase_manager.auth.getUid()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists())
                                        {
                                            CuaHang temp = dataSnapshot.getValue(CuaHang.class);

                                            if (temp.getTrangThaiCuaHang()== TrangThaiCuaHang.ChuaKichHoat)
                                            {
                                                kAlertDialog.setTitleText("Thông báo");
                                                kAlertDialog.setContentText("Vui lòng đợi Admin xét duyệt và kích hoạt tài khoản trong vòng 24h!");
                                                kAlertDialog.changeAlertType(KAlertDialog.WARNING_TYPE);
                                                firebase_manager.auth.signOut();
                                            }
                                            else {
                                                Intent intent = new Intent(LoginActivity.this, Home.class);
                                                kAlertDialog.dismiss();
                                                startActivity(intent);
                                                edtMatKhau.setText("");
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });

                            }
                            else {
                                kAlertDialog.setTitleText("Sai tài khoản hoặc mật khẩu");
                                kAlertDialog.changeAlertType(KAlertDialog.WARNING_TYPE);
                            }
                        }
                    });

                }
            }
        });
    }

    private boolean Validated_Form() {
        boolean result = false;
        if (!validate.isBlank(edtEmail) && validate.isEmail(edtEmail)
                &&!validate.isBlank(edtMatKhau)
                && !validate.lessThan6Char(edtMatKhau)) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    private void setControl() {
        txtDangKy = findViewById(R.id.txtDangKy);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        edtEmail = findViewById(R.id.edtEmail);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        txtQuenMatKhau = findViewById(R.id.txtQuenMatKhau);
    }
}