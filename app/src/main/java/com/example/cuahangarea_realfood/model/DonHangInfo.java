package com.example.cuahangarea_realfood.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DonHangInfo implements Parcelable {
    String IDInfo, IDDonHang,IDKhachHang, soLuong, donGia, maGiamGia;
    SanPham sanPham;
    boolean selected;

    protected DonHangInfo(Parcel in) {
        IDInfo = in.readString();
        IDDonHang = in.readString();
        IDKhachHang = in.readString();
        soLuong = in.readString();
        donGia = in.readString();
        maGiamGia = in.readString();
        selected = in.readByte() != 0;
    }

    public static final Creator<DonHangInfo> CREATOR = new Creator<DonHangInfo>() {
        @Override
        public DonHangInfo createFromParcel(Parcel in) {
            return new DonHangInfo(in);
        }

        @Override
        public DonHangInfo[] newArray(int size) {
            return new DonHangInfo[size];
        }
    };

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public DonHangInfo(String IDInfo, String IDDonHang, String IDKhachHang, String soLuong, String donGia, String maGiamGia, SanPham sanPham) {
        this.IDInfo = IDInfo;
        this.IDDonHang = IDDonHang;
        this.IDKhachHang = IDKhachHang;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.maGiamGia = maGiamGia;
        this.sanPham = sanPham;
        selected = false;
    }

    public DonHangInfo() {
    }

    public String getIDInfo() {
        return IDInfo;
    }

    public void setIDInfo(String IDInfo) {
        this.IDInfo = IDInfo;
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

    public String getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(String soLuong) {
        this.soLuong = soLuong;
    }

    public String getDonGia() {
        return donGia;
    }

    public void setDonGia(String donGia) {
        this.donGia = donGia;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(IDInfo);
        dest.writeString(IDDonHang);
        dest.writeString(IDKhachHang);
        dest.writeString(soLuong);
        dest.writeString(donGia);
        dest.writeString(maGiamGia);
        dest.writeByte((byte) (selected ? 1 : 0));
    }
}
