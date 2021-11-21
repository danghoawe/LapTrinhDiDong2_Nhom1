package com.example.cuahangarea_realfood.model;

public class TaiKhoanNganHang {
    String id,tenNganHang,tenChiNhanh,soTaiKhoan,tenChuTaiKhoan,soCMND,idTaiKhoan;

    public TaiKhoanNganHang(String id, String tenNganHang, String tenChiNhanh, String soTaiKhoan, String tenChuTaiKhoan, String soCMND, String idTaiKhoan) {
        this.id = id;
        this.tenNganHang = tenNganHang;
        this.tenChiNhanh = tenChiNhanh;
        this.soTaiKhoan = soTaiKhoan;
        this.tenChuTaiKhoan = tenChuTaiKhoan;
        this.soCMND = soCMND;
        this.idTaiKhoan = idTaiKhoan;
    }

    public String getIdTaiKhoan() {
        return idTaiKhoan;
    }

    public void setIdTaiKhoan(String idTaiKhoan) {
        this.idTaiKhoan = idTaiKhoan;
    }

    public TaiKhoanNganHang() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenNganHang() {
        return tenNganHang;
    }

    public void setTenNganHang(String tenNganHang) {
        this.tenNganHang = tenNganHang;
    }

    public String getTenChiNhanh() {
        return tenChiNhanh;
    }

    public void setTenChiNhanh(String tenChiNhanh) {
        this.tenChiNhanh = tenChiNhanh;
    }

    public String getSoTaiKhoan() {
        return soTaiKhoan;
    }

    public void setSoTaiKhoan(String soTaiKhoan) {
        this.soTaiKhoan = soTaiKhoan;
    }

    public String getTenChuTaiKhoan() {
        return tenChuTaiKhoan;
    }

    public void setTenChuTaiKhoan(String tenChuTaiKhoan) {
        this.tenChuTaiKhoan = tenChuTaiKhoan;
    }

    public String getSoCMND() {
        return soCMND;
    }

    public void setSoCMND(String soCMND) {
        this.soCMND = soCMND;
    }
}
