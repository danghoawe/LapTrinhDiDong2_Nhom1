package com.example.cuahangarea_realfood.model;

import java.util.Date;

public class Voucher {
    String idMaGiamGia,idCuaHang,maGiamGia;
    SanPham sanPham;
    int giaGiam,phanTramGiam;
    Date ngayTao,hanSuDung;

    public Voucher(String idMaGiamGia, String idCuaHang, String maGiamGia, SanPham sanPham, int giaGiam, int phanTramGiam, Date ngayTao, Date hanSuDung) {
        this.idMaGiamGia = idMaGiamGia;
        this.idCuaHang = idCuaHang;
        this.maGiamGia = maGiamGia;
        this.sanPham = sanPham;
        this.giaGiam = giaGiam;
        this.phanTramGiam = phanTramGiam;
        this.ngayTao = ngayTao;
        this.hanSuDung = hanSuDung;
    }

    public Voucher() {
    }

    public String getIdMaGiamGia() {
        return idMaGiamGia;
    }

    public void setIdMaGiamGia(String idMaGiamGia) {
        this.idMaGiamGia = idMaGiamGia;
    }

    public String getIdCuaHang() {
        return idCuaHang;
    }

    public void setIdCuaHang(String idCuaHang) {
        this.idCuaHang = idCuaHang;
    }

    public String getMaGiamGia() {
        return maGiamGia;
    }

    public void setMaGiamGia(String maGiamGia) {
        this.maGiamGia = maGiamGia;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }

    public int getGiaGiam() {
        return giaGiam;
    }

    public void setGiaGiam(int giaGiam) {
        this.giaGiam = giaGiam;
    }

    public int getPhanTramGiam() {
        return phanTramGiam;
    }

    public void setPhanTramGiam(int phanTramGiam) {
        this.phanTramGiam = phanTramGiam;
    }

    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }

    public Date getHanSuDung() {
        return hanSuDung;
    }

    public void setHanSuDung(Date hanSuDung) {
        this.hanSuDung = hanSuDung;
    }
}
