package com.example.cuahangarea_realfood.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.developer.kalert.KAlertDialog;
import com.example.cuahangarea_realfood.Firebase_Manager;
import com.example.cuahangarea_realfood.R;
import com.example.cuahangarea_realfood.TrangThai.TrangThaiThongBao;
import com.example.cuahangarea_realfood.Validate;
import com.example.cuahangarea_realfood.model.DanhMuc;
import com.example.cuahangarea_realfood.model.LoaiSanPham;
import com.example.cuahangarea_realfood.model.SanPham;
import com.example.cuahangarea_realfood.model.ThongBao;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.nordan.dialog.Animation;
import com.nordan.dialog.DialogType;
import com.nordan.dialog.NordanAlertDialog;
import com.tapadoo.alerter.Alerter;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import dev.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;
import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

public class ThongTinSanPhamActivity extends AppCompatActivity {

    SanPham sanPham;

    List<CarouselItem> list = new ArrayList<>();
    List<DanhMuc> listDanhMuc = new ArrayList<>();
    List<LoaiSanPham> listLoaiSanPham = new ArrayList<>();
    List<String> namesDanhMuc = new ArrayList<>();
    List<String> namesLoaiSanPham = new ArrayList<>();
    ArrayAdapter adapter ;

    ImageCarousel carousel;
    Button btnThemAnh, btnThemSanPham,btnXoaSanPham;
    private String image;
    EditText edtTenSanPham, edtSize, edtDonGia, edtThongTinChiTiet;
    Spinner spDanhMuc, spLoaiSanPham;
    Validate validate = new Validate();
    ImageView imageView,imageDelete;
    Firebase_Manager firebase_manager = new Firebase_Manager();

    //Lưu trữ ảnh
    ArrayList<String> images = new ArrayList<>();
    ArrayList<Uri> uriImages = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_san_pham);
        setControl();
        //Lấy sản phẩm truyền vào nếu có
        if (getIntent() != null && getIntent().getExtras() != null) {
            Intent intent = getIntent();
            String dataDonHang = intent.getStringExtra("sanPham");
            Gson gson = new Gson();
            sanPham = gson.fromJson(dataDonHang, SanPham.class);
        }
        getDanhMuc();
        getLoaiSanPham();
        LoadData();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SetEvent();

    }

    private void LoadInfoSanPham() {
        //Nếu có sản phẩm truyền vào
        if(sanPham!= null){
            btnXoaSanPham.setVisibility(View.VISIBLE);
            edtTenSanPham.setText(sanPham.getTenSanPham());
            edtThongTinChiTiet.setText(sanPham.getChiTietSanPham());
            edtSize.setText(sanPham.getSize());
            edtTenSanPham.setText(sanPham.getTenSanPham());
            edtDonGia.setText(sanPham.getGia());
            AtomicInteger positon = new AtomicInteger();
            listDanhMuc.forEach(danhMuc -> {
                if (danhMuc.getIDDanhMuc().equals(sanPham.getIDDanhMuc()))
                {
                    spDanhMuc.setSelection(positon.get());

                }
                positon.getAndIncrement();
            });
            AtomicInteger positon1 = new AtomicInteger();
            listLoaiSanPham.forEach(loaiSanPham -> {
                if (loaiSanPham.getiDLoai().equals(sanPham.getIDLoai()))
                {
                    spLoaiSanPham.setSelection(positon1.get());
                }
                positon1.getAndIncrement();
            });
            images = sanPham.getImages();
            //Load hình ảnh và truyền vào carousel dựa vào properties Images của sản phẩm
            for (int i =0 ; i <sanPham.getImages().size();i++)
            {

                firebase_manager.storageRef.child("SanPham").child(sanPham.getIDCuaHang()).child(sanPham.getIDSanPham()).child(sanPham.getImages().get(i)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        list.add(new CarouselItem(uri.toString()));
                        uriImages.add(uri);
                        carousel.setData(list);
                        LoadData();

                    }
                });


            }
        }

    }

    private void setControl() {
        carousel = findViewById(R.id.carousel);
        btnThemAnh = findViewById(R.id.btnThemAnh);
        edtDonGia = findViewById(R.id.edtDonGia);
        edtSize = findViewById(R.id.edtSize);
        edtTenSanPham = findViewById(R.id.edtTenSanPham);
        edtThongTinChiTiet = findViewById(R.id.edtThongTinChiTiet);
        spDanhMuc = findViewById(R.id.spDanhMucSanPham);
        spLoaiSanPham = findViewById(R.id.spLoaiSanPham);
        btnThemSanPham = findViewById(R.id.btnThemSanPham);
        imageView = findViewById(R.id.imagebackground);
        imageDelete = findViewById(R.id.imageDelete);
        btnXoaSanPham = findViewById(R.id.btnXoaSanPham);
    }

    private void LoadData() {

        if (list.size() == 0) {
            imageView.setVisibility(View.VISIBLE);
            imageDelete.setVisibility(View.GONE);
        } else {
            imageView.setVisibility(View.GONE);
            imageDelete.setVisibility(View.VISIBLE);
        }

    }



    private void SetEvent() {

        btnXoaSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KAlertDialog kAlertDialog = new KAlertDialog(ThongTinSanPhamActivity.this,KAlertDialog.WARNING_TYPE);
                kAlertDialog.setContentText("Bạn có chắc chắn xóa sản phẩm này?");
                kAlertDialog.setConfirmText("Có").setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                    @Override
                    public void onClick(KAlertDialog kAlertDialog) {
                        kAlertDialog.changeAlertType(KAlertDialog.PROGRESS_TYPE);
                        firebase_manager.mDatabase.child("SanPham").child(sanPham.getIDSanPham()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                kAlertDialog.dismiss();

                                //Xóa thư mục hình ảnh
                                firebase_manager.storageRef.child("SanPham").child(sanPham.getIDCuaHang()).child(sanPham.getIDSanPham()).listAll()
                                        .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                                                                  @Override
                                                                  public void onSuccess(ListResult listResult) {
                                                                      List<StorageReference> items = listResult.getItems();
                                                                      items.forEach(storageReference -> storageReference.delete());
                                                                  }
                                                              }
                                        );
                                finish();
                            }
                        });
                    }
                })
                .setTitleText("Thông báo").setCancelText("Không");
                kAlertDialog.show();


            }
        });

        btnThemAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.build(new PickSetup()).setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {
                        image = r.getUri().toString();
                        list.add(new CarouselItem(image));
                        carousel.setData(list);
                        uriImages.add(r.getUri());
                        String uuid = UUID.randomUUID().toString().replace("-", "");
                        images.add(uuid);
                        LoadData();
                    }
                }).show(ThongTinSanPhamActivity.this);
            }
        });
        btnThemSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validated_Form()) {
                    KAlertDialog kAlertDialog = new KAlertDialog(ThongTinSanPhamActivity.this, KAlertDialog.PROGRESS_TYPE);
                    kAlertDialog.show();
                    if (!images.isEmpty())
                    {
                        if (sanPham==null)
                        {
                            String uuid = UUID.randomUUID().toString().replace("-", "");
                            SanPham temp;
                            if (listDanhMuc.size()==0)
                            {
                                temp = new SanPham(uuid, edtTenSanPham.getText().toString(), listLoaiSanPham.get(spLoaiSanPham.getSelectedItemPosition()).getiDLoai(), "", edtDonGia.getText().toString(), edtThongTinChiTiet.getText().toString(), firebase_manager.auth.getUid(),edtSize.getText().toString(), (float) 0.0, images   );

                            }
                            else {
                                temp = new SanPham(uuid, edtTenSanPham.getText().toString(), listLoaiSanPham.get(spLoaiSanPham.getSelectedItemPosition()).getiDLoai(), listDanhMuc.get(spDanhMuc.getSelectedItemPosition()).getIDDanhMuc(), edtDonGia.getText().toString(), edtThongTinChiTiet.getText().toString(), firebase_manager.auth.getUid(),edtSize.getText().toString(), (float) 0.0, images   );
                            }
                            firebase_manager.UpImageSanPham(uriImages, uuid, images);
                            firebase_manager.Ghi_SanPham(temp).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    kAlertDialog.changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                    kAlertDialog.setContentText("Đã Lưu phẩm thành công");
                                }
                            });
                            sanPham = temp;
                            String uuid_ThongBao = UUID.randomUUID().toString().replace("-", "");
                            ThongBao thongBao = new ThongBao(uuid_ThongBao, "Sản phẩm đã được thêm mới: "+sanPham.getTenSanPham(), "Thông báo", "", firebase_manager.auth.getUid(),"",TrangThaiThongBao.ChuaXem,new Date());
                            firebase_manager.Ghi_ThongBao(thongBao);


                        }
                        else {
                            String uuid = sanPham.getIDSanPham();
                            SanPham temp = new SanPham(uuid, edtTenSanPham.getText().toString(), listLoaiSanPham.get(spLoaiSanPham.getSelectedItemPosition()).getiDLoai(), listDanhMuc.get(spDanhMuc.getSelectedItemPosition()).getIDDanhMuc(), edtDonGia.getText().toString(), edtThongTinChiTiet.getText().toString(), firebase_manager.auth.getUid(),edtSize.getText().toString(), (float) 0.0, images   );

                            firebase_manager.Ghi_SanPham(temp).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    kAlertDialog.changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                    kAlertDialog.setContentText("Đã Lưu sản phẩm thành công");
                                    try {
                                        firebase_manager.UpImageSanPham(uriImages, uuid, images);
                                    }catch (Exception e)
                                    {
                                        Log.d("Firebase UPLOAD",e.getMessage());
                                    }
                                }
                            });
                            sanPham = temp;


                        }
                    }
                    else {
                        kAlertDialog.changeAlertType(KAlertDialog.ERROR_TYPE);
                        kAlertDialog.setContentText("Vui lòng chọn ảnh" );
                    }
                } else {
                    Alerter.create(ThongTinSanPhamActivity.this)
                            .setTitle("Lỗi")
                            .setText("Vui lòng nhập đúng định dạng")
                            .setBackgroundColorRes(R.color.error_stroke_color) // or setBackgroundColorInt(Color.CYAN)
                            .show();
                }
            }
        });
        imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    if(sanPham== null)
                    {
                        list.remove( carousel.getCurrentPosition());
                        images.remove( carousel.getCurrentPosition());
                        carousel.setData(list);
                        LoadData();
                    }
                    else {
                        images.remove( carousel.getCurrentPosition());
                        list.remove( carousel.getCurrentPosition());
                        carousel.setData(list);
                        LoadData();
                    }
            }
        });
    }

    private boolean Validated_Form() {
        boolean result = false;
        if (!validate.isBlank(edtTenSanPham) && !validate.isBlank(edtThongTinChiTiet)
                && !validate.isBlank(edtSize) && !validate.isBlank(edtDonGia)
                && validate.isNumber(edtDonGia)
        ) {

            result = true;
        }
        return result;
    }
    private void getDanhMuc(){
        firebase_manager.mDatabase.child("DanhMuc").child(firebase_manager.auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listDanhMuc.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    DanhMuc danhMuc = postSnapshot.getValue(DanhMuc.class);
                    listDanhMuc.add(danhMuc);
                    namesDanhMuc.add(danhMuc.getTenDanhMuc());
                }
                adapter = new ArrayAdapter(ThongTinSanPhamActivity.this,
                        android.R.layout.simple_expandable_list_item_1 ,
                        namesDanhMuc);
                spDanhMuc.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    private void getLoaiSanPham(){

        firebase_manager.mDatabase.child("LoaiSanPham").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listLoaiSanPham.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    LoaiSanPham danhMuc = postSnapshot.getValue(LoaiSanPham.class);
                    listLoaiSanPham.add(danhMuc);
                    namesLoaiSanPham.add(danhMuc.getTenLoai());
                }
                adapter = new ArrayAdapter(ThongTinSanPhamActivity.this,
                        android.R.layout.simple_expandable_list_item_1 ,
                        namesLoaiSanPham);
                spLoaiSanPham.setAdapter(adapter);
                LoadInfoSanPham();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        Log.d("A",namesDanhMuc.size()+"");
    }

}