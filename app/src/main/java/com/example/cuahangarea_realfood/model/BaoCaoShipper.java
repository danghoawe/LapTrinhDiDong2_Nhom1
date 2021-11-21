package com.example.cuahangarea_realfood.model;

import java.util.Date;

public class BaoCaoShipper {
    String idBaoCao,idCuaHang,idShipper,noiDung,tieuDe;
    Date ngayBaoCao;

    public BaoCaoShipper(String idBaoCao, String idCuaHang, String idShipper, String noiDung, String tieuDe, Date ngayBaoCao) {
        this.idBaoCao = idBaoCao;
        this.idCuaHang = idCuaHang;
        this.idShipper = idShipper;
        this.noiDung = noiDung;
        this.tieuDe = tieuDe;
        this.ngayBaoCao = ngayBaoCao;
    }

    public BaoCaoShipper() {
    }

    public String getIdBaoCao() {
        return idBaoCao;
    }

    public void setIdBaoCao(String idBaoCao) {
        this.idBaoCao = idBaoCao;
    }

    public String getIdCuaHang() {
        return idCuaHang;
    }

    public void setIdCuaHang(String idCuaHang) {
        this.idCuaHang = idCuaHang;
    }

    public String getIdShipper() {
        return idShipper;
    }

    public void setIdShipper(String idShipper) {
        this.idShipper = idShipper;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public Date getNgayBaoCao() {
        return ngayBaoCao;
    }

    public void setNgayBaoCao(Date ngayBaoCao) {
        this.ngayBaoCao = ngayBaoCao;
    }
}
