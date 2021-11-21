package com.example.cuahangarea_realfood.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.developer.kalert.KAlertDialog;
import com.example.cuahangarea_realfood.Firebase_Manager;
import com.example.cuahangarea_realfood.Validate;
import com.example.cuahangarea_realfood.databinding.ActivityQuenMatKhauBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class  QuenMatKhauActivity extends AppCompatActivity {
    ActivityQuenMatKhauBinding binding;
    Validate validate = new Validate();
    Firebase_Manager firebase_manager = new Firebase_Manager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuenMatKhauBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.txtDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.btnGuiMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate.isEmail(binding.edtEmail))
                {
                    KAlertDialog kAlertDialog = new KAlertDialog(QuenMatKhauActivity.this,KAlertDialog.PROGRESS_TYPE).setContentText("Loading");
                    kAlertDialog.show();
                    kAlertDialog.setCancelable(false);
                    firebase_manager.auth.getInstance().sendPasswordResetEmail(binding.edtEmail.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                        kAlertDialog.changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                        kAlertDialog.setContentText("Chúng tôi đã gửi thư xác thực đến Email của bạn!\nVui lòng kiểm tra email của bạn!");
                                        kAlertDialog.showConfirmButton(false);
                                        kAlertDialog.setCancelable(true);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull  Exception e) {
                            kAlertDialog.changeAlertType(KAlertDialog.ERROR_TYPE);
                            kAlertDialog.setContentText("Email không tồn tại !");
                            kAlertDialog.showConfirmButton(false);
                            kAlertDialog.setCancelable(true);
                        }
                    });
                }
            }
        });

    }
}