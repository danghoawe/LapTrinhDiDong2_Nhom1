package com.example.cuahangarea_realfood.model;



import com.example.cuahangarea_realfood.TrangThai.TrangThaiDonHang;

import java.util.Date;

public class DonHang {
    String IDDonHang;
    String IDCuaHang;
    String IDKhachHang;
    String IDShipper;
    String diaChi;
    String soDienThoai;
    String ghiChu_KhachHang;
    String ghiChu_Shipper;

    public String getGhiChuCuaHang() {
        return ghiChuCuaHang;
    }

    public void setGhiChuCuaHang(String ghiChuCuaHang) {
        this.ghiChuCuaHang = ghiChuCuaHang;
    }

    String ghiChuCuaHang;
    double tongTien;
    Date ngayTao;
    TrangThaiDonHang trangThai;

    public DonHang() {
    }

    public DonHang(String IDDonHang, String IDCuaHang, String IDKhachHang, String IDShipper, String diaChi, String soDienThoai, String ghiChu_KhachHang, String ghiChu_Shipper, double tongTien, Date ngayTao, TrangThaiDonHang trangThai) {
        this.IDDonHang = IDDonHang;
        this.IDCuaHang = IDCuaHang;
        this.IDKhachHang = IDKhachHang;
        this.IDShipper = IDShipper;
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
        this.ghiChu_KhachHang = ghiChu_KhachHang;
        this.ghiChu_Shipper = ghiChu_Shipper;
        this.tongTien = tongTien;
        this.ngayTao = ngayTao;
        this.trangThai = trangThai;
    }

    public String getIDCuaHang() {
        return IDCuaHang;
    }

    public void setIDCuaHang(String IDCuaHang) {
        this.IDCuaHang = IDCuaHang;
    }

    public String getIDDonHang() {
        return IDDonHang;
    }

    public void setIDDonHang(String IDDonHang) {
        this.IDDonHang = IDDonHang;
    }

    public String getIDKhachHang() {
        return IDKhachHang;
    }

    public void setIDKhachHang(String IDKhachHang) {
        this.IDKhachHang = IDKhachHang;
    }

    public String getIDShipper() {
        return IDShipper;
    }

    public void setIDShipper(String IDShipper) {
        this.IDShipper = IDShipper;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getGhiChu_KhachHang() {
        return ghiChu_KhachHang;
    }

    public void setGhiChu_KhachHang(String ghiChu_KhachHang) {
        this.ghiChu_KhachHang = ghiChu_KhachHang;
    }

    public String getGhiChu_Shipper() {
        return ghiChu_Shipper;
    }

    public void setGhiChu_Shipper(String ghiChu_Shipper) {
        this.ghiChu_Shipper = ghiChu_Shipper;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }

    public TrangThaiDonHang getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiDonHang trangThai) {
        this.trangThai = trangThai;
    }
}
