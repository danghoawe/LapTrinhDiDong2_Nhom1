package com.example.cuahangarea_realfood.model;

import com.example.cuahangarea_realfood.TrangThai.TrangThaiThongBao;

import java.util.Date;

public class ThongBao {
    String IDThongBao;
    String noiDung;
    String tieuDe;
    String theme;
    String IDUSer;
    String Image;
    TrangThaiThongBao trangThaiThongBao;
    Date date;

    public ThongBao(String IDThongBao, String noiDung, String tieuDe, String theme, String IDUSer, String image, TrangThaiThongBao trangThaiThongBao, Date date) {
        this.IDThongBao = IDThongBao;
        this.noiDung = noiDung;
        this.tieuDe = tieuDe;
        this.theme = theme;
        this.IDUSer = IDUSer;
        Image = image;
        this.trangThaiThongBao = trangThaiThongBao;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }


    public String getIDThongBao() {
        return IDThongBao;
    }

    public void setIDThongBao(String IDThongBao) {
        this.IDThongBao = IDThongBao;
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

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getIDUSer() {
        return IDUSer;
    }

    public void setIDUSer(String IDUSer) {
        this.IDUSer = IDUSer;
    }

    public TrangThaiThongBao getTrangThaiThongBao() {
        return trangThaiThongBao;
    }

    public void setTrangThaiThongBao(TrangThaiThongBao trangThaiThongBao) {
        this.trangThaiThongBao = trangThaiThongBao;
    }
    public ThongBao() {
    }
}
