package com.example.cuahangarea_realfood.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.developer.kalert.KAlertDialog;
import com.example.cuahangarea_realfood.Firebase_Manager;
import com.example.cuahangarea_realfood.R;
import com.example.cuahangarea_realfood.Validate;
import com.example.cuahangarea_realfood.model.DanhMuc;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nordan.dialog.Animation;
import com.nordan.dialog.DialogType;
import com.nordan.dialog.NordanAlertDialog;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.UUID;


public class DanhMuc_DialogFragment extends DialogFragment {
    Firebase_Manager firebase_manager = new Firebase_Manager();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    Validate validate = new Validate();
    public OnInputSelected onInputSelected;
    static final String TAG = "DanhMuc_DialogFragment";
    static final int code = 1;
    Uri image,uriSua;
    Button btnThemDanhMuc,btnXoa;
    EditText edtTenDanhMuc;
    ImageView imgAnh;
    DanhMuc danhMuc;
    StorageReference storageRef  = FirebaseStorage.getInstance().getReference();
    public interface OnInputSelected {
        void setInputUpdate(DanhMuc danhMuc);

        void setInput(DanhMuc danhMuc);
    }
    public  DanhMuc_DialogFragment(DanhMuc danhMuc)
    {
        this.danhMuc = danhMuc;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_danh_muc__dialog, container, false);
        btnThemDanhMuc = view.findViewById(R.id.btnThem);
        btnXoa = view.findViewById(R.id.btnXoa);
        imgAnh = view.findViewById(R.id.imgAnh);
        edtTenDanhMuc = view.findViewById(R.id.edtTenDanhMuc);


        if (danhMuc!=null)
        {
            edtTenDanhMuc.setText(danhMuc.getTenDanhMuc());
            storageRef.child("DanhMuc").child(danhMuc.getIDDanhMuc()).child("image").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Glide.with(getActivity())
                            .load(task.getResult().toString())
                            .into(imgAnh);
                    Log.d("link",task.getResult().toString());
                    image = task.getResult();
                    uriSua = task.getResult();
                }
            });
            btnXoa.setVisibility(View.VISIBLE);
        }

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnThemDanhMuc.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if (!validate.isBlank(edtTenDanhMuc)) {
                    if (image != null) {
                        if (danhMuc==null)
                        {
                            KAlertDialog kAlertDialog = new KAlertDialog(getActivity(),KAlertDialog.PROGRESS_TYPE).setContentText("Loading");
                            kAlertDialog.show();
                       
                            String uuid = UUID.randomUUID().toString().replace("-", "");
                            DanhMuc danhMuc = new DanhMuc(auth.getUid(), uuid, edtTenDanhMuc.getText().toString());
                            firebase_manager.UpImageDanhMuc(image, uuid).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    firebase_manager.Ghi_DanhMuc(danhMuc);
                                    kAlertDialog.setContentText("Lưu Danh mục thành công!");
                                    kAlertDialog.changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                }
                            });
                        }
                        else {
                            KAlertDialog kAlertDialog = new KAlertDialog(getActivity(),KAlertDialog.PROGRESS_TYPE).setContentText("Loading");
                            kAlertDialog.show();
                            DanhMuc temp = new DanhMuc(auth.getUid(),danhMuc.getIDDanhMuc(), edtTenDanhMuc.getText().toString());
                            if (uriSua!=image)
                            {
                                firebase_manager.UpImageDanhMuc(image, danhMuc.getIDDanhMuc()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            firebase_manager.Ghi_DanhMuc(temp);
                                        kAlertDialog.setContentText("Lưu Danh mục thành công!");
                                        kAlertDialog.changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                    }
                                });
                            }
                            else {
                                firebase_manager.Ghi_DanhMuc(temp);
                                kAlertDialog.setContentText("Lưu Danh mục thành công!");
                                kAlertDialog.changeAlertType(KAlertDialog.SUCCESS_TYPE);
                            }
                        }


                    }
                    else {
                        KAlertDialog kAlertDialog = new KAlertDialog(getActivity(),KAlertDialog.ERROR_TYPE).setContentText("Vui lòng chọn ảnh");
                        kAlertDialog.show();
                    }

                }
            }
        });
        imgAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.build(new PickSetup())
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {
                                imgAnh.setImageBitmap(r.getBitmap());
                                image = r.getUri();
                            }
                        })
                        .setOnPickCancel(new IPickCancel() {
                            @Override
                            public void onCancelClick() {
                                //TODO: do what you have to if user clicked cancel
                            }
                        }).show(getActivity());
            }
        });
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new NordanAlertDialog.Builder(getActivity())
                        .setDialogType(DialogType.WARNING)
                        .setAnimation(Animation.SLIDE)
                        .isCancellable(true)
                        .setTitle("Thông báo!")
                        .setMessage("Bạn có muốn xóa? Sẽ xóa toàn bộ sản phẩm có danh mục "+danhMuc.getTenDanhMuc() + ".Và không thể khôi phục .    Vui lòng cân nhắc ")
                        .setPositiveBtnText("Không")
                        .setNegativeBtnText("Có")
                        .onPositiveClicked(() -> {})
                        .onNegativeClicked(() -> {
                            //Xóa sản phẩm trong danh mục
                            firebase_manager.mDatabase.child("DanhMuc").child(danhMuc.getIDCuaHang()).child(danhMuc.getIDDanhMuc()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    getDialog().dismiss();
                                }
                            });
                            DatabaseReference ref = firebase_manager.mDatabase;
                            //Xóa các sản phẩm theo danh mục
                            Query applesQuery = ref.child("SanPham").orderByChild("iddanhMuc").equalTo(danhMuc.getIDDanhMuc());

                            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                        appleSnapshot.getRef().removeValue();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.e(TAG, "onCancelled", databaseError.toException());
                                }
                            });
                        })
                        .build().show();


            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onInputSelected = (OnInputSelected) getTargetFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DanhMuc_DialogFragment.code) {
        }
    }
}