package com.example.cuahangarea_realfood.model;

import java.util.Date;

public class DanhGia {
    String IDDanhGia, IDSanPham,IDCuaHang, IDKhachHang, noiDung,noiDungShopTraLoi,IDInfo;
    float rating;
    Date ngayDanhGia,ngayShopTraLoi;


    public DanhGia(String IDDanhGia, String IDSanPham, String IDCuaHang, String IDKhachHang, String noiDung, String noiDungShopTraLoi, String IDInfo, float rating, Date ngayDanhGia, Date ngayShopTraLoi) {
        this.IDDanhGia = IDDanhGia;
        this.IDSanPham = IDSanPham;
        this.IDCuaHang = IDCuaHang;
        this.IDKhachHang = IDKhachHang;
        this.noiDung = noiDung;
        this.noiDungShopTraLoi = noiDungShopTraLoi;
        this.IDInfo = IDInfo;
        this.rating = rating;
        this.ngayDanhGia = ngayDanhGia;
        this.ngayShopTraLoi = ngayShopTraLoi;
    }

    public DanhGia() {
    }

    public Date getNgayShopTraLoi() {
        return ngayShopTraLoi;
    }

    public void setNgayShopTraLoi(Date ngayShopTraLoi) {
        this.ngayShopTraLoi = ngayShopTraLoi;
    }

    public String getIDDanhGia() {
        return IDDanhGia;
    }

    public void setIDDanhGia(String IDDanhGia) {
        this.IDDanhGia = IDDanhGia;
    }

    public String getIDSanPham() {
        return IDSanPham;
    }

    public void setIDSanPham(String IDSanPham) {
        this.IDSanPham = IDSanPham;
    }

    public String getIDCuaHang() {
        return IDCuaHang;
    }

    public void setIDCuaHang(String IDCuaHang) {
        this.IDCuaHang = IDCuaHang;
    }

    public String getIDKhachHang() {
        return IDKhachHang;
    }

    public void setIDKhachHang(String IDKhachHang) {
        this.IDKhachHang = IDKhachHang;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getNoiDungShopTraLoi() {
        return noiDungShopTraLoi;
    }

    public void setNoiDungShopTraLoi(String noiDungShopTraLoi) {
        this.noiDungShopTraLoi = noiDungShopTraLoi;
    }

    public String getIDInfo() {
        return IDInfo;
    }

    public void setIDInfo(String IDInfo) {
        this.IDInfo = IDInfo;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Date getNgayDanhGia() {
        return ngayDanhGia;
    }

    public void setNgayDanhGia(Date ngayDanhGia) {
        this.ngayDanhGia = ngayDanhGia;
    }
}