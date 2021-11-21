package com.example.cuahangarea_realfood.model;

public class LoaiSanPham extends CuaHang {
    String iDLoai, sTT, tenLoai;

    public LoaiSanPham() {
    }

    public LoaiSanPham(String iDLoai, String sTT, String tenLoai) {
        this.iDLoai = iDLoai;
        this.sTT = sTT;
        this.tenLoai = tenLoai;
    }

    public String getiDLoai() {
        return iDLoai;
    }

    public void setiDLoai(String iDLoai) {
        this.iDLoai = iDLoai;
    }

    public String getsTT() {
        return sTT;
    }

    public void setsTT(String sTT) {
        this.sTT = sTT;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }
}