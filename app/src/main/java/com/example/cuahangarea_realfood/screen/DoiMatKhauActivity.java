package com.example.cuahangarea_realfood.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.developer.kalert.KAlertDialog;
import com.example.cuahangarea_realfood.Firebase_Manager;
import com.example.cuahangarea_realfood.Validate;
import com.example.cuahangarea_realfood.databinding.ActivityDoiMatKhauBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;

public class DoiMatKhauActivity extends AppCompatActivity {
    Validate validate = new Validate();

    Firebase_Manager firebase_manager = new Firebase_Manager();
    ActivityDoiMatKhauBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoiMatKhauBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setEvent();
    }

    private void setEvent() {
        binding.btnDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validate.isBlank(binding.edtPass)&&!validate.isBlank(binding.edtNewPass)&&!validate.isBlank(binding.edtRePass)) {
                    KAlertDialog kAlertDialog = new KAlertDialog(DoiMatKhauActivity.this, KAlertDialog.PROGRESS_TYPE);
                    kAlertDialog.setContentText("Loading");
                    kAlertDialog.show();
                    FirebaseUser user = firebase_manager.auth.getCurrentUser();
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(firebase_manager.auth.getCurrentUser().getEmail(), binding.edtPass.getText().toString());
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                if (binding.edtNewPass.getText().toString().equals(binding.edtRePass.getText().toString())) {
                                    user.updatePassword(binding.edtNewPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            kAlertDialog.setContentText("Đã đổi mật khẩu thành công");
                                            kAlertDialog.changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                            kAlertDialog.showConfirmButton(false);
                                        }
                                    });
                                } else {
                                    binding.edtRePass.setError("Mật khẩu không trùng khớp");
                                    kAlertDialog.dismiss();
                                }

                            } else {
                                kAlertDialog.setContentText("Mật khẩu sai");
                                kAlertDialog.changeAlertType(KAlertDialog.WARNING_TYPE);
                                kAlertDialog.showConfirmButton(false);
                            }
                        }
                    });
                }
            }
        });
        }
    }
