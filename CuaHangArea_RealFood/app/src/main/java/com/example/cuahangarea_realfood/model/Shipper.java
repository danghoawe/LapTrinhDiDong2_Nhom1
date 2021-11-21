package com.example.cuahangarea_realfood.model;

public class Shipper {
    String iDShipper, eMail, matKhau, hoVaTen, diaChi, khuVucHoatDong, ngaySinh, maSoXe, trangThaiHoatDong, soDienThoai;

    public Shipper() {
    }

    public Shipper(String iDShipper, String eMail, String matKhau, String hoVaTen, String diaChi, String khuVucHoatDong, String ngaySinh, String maSoXe, String trangThaiHoatDong, String soDienThoai) {
        this.iDShipper = iDShipper;
        this.eMail = eMail;
        this.matKhau = matKhau;
        this.hoVaTen = hoVaTen;
        this.diaChi = diaChi;
        this.khuVucHoatDong = khuVucHoatDong;
        this.ngaySinh = ngaySinh;
        this.maSoXe = maSoXe;
        this.trangThaiHoatDong = trangThaiHoatDong;
        this.soDienThoai = soDienThoai;
    }

    public String getiDShipper() {
        return iDShipper;
    }

    public void setiDShipper(String iDShipper) {
        this.iDShipper = iDShipper;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getHoVaTen() {
        return hoVaTen;
    }

    public void setHoVaTen(String hoVaTen) {
        this.hoVaTen = hoVaTen;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getKhuVucHoatDong() {
        return khuVucHoatDong;
    }

    public void setKhuVucHoatDong(String khuVucHoatDong) {
        this.khuVucHoatDong = khuVucHoatDong;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getMaSoXe() {
        return maSoXe;
    }

    public void setMaSoXe(String maSoXe) {
        this.maSoXe = maSoXe;
    }

    public String getTrangThaiHoatDong() {
        return trangThaiHoatDong;
    }

    public void setTrangThaiHoatDong(String trangThaiHoatDong) {
        this.trangThaiHoatDong = trangThaiHoatDong;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }
}