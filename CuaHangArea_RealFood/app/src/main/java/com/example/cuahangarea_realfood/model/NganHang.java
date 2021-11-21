package com.example.cuahangarea_realfood.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NganHang {
        public NganHang() {
        }
        @SerializedName("id")
        String id;
        @SerializedName("name")
        String name;
        @SerializedName("code")
        String code;
        @SerializedName("bin")
        String bin;
        @SerializedName("short_name")
        String short_name;
        @SerializedName("logo")
        String logo;
        @SerializedName("vietqr")
        String vietqr;
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getBin() {
            return bin;
        }

        public void setBin(String bin) {
            this.bin = bin;
        }

        public String getShort_name() {
            return short_name;
        }

        public void setShort_name(String short_name) {
            this.short_name = short_name;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getVietqr() {
            return vietqr;
        }

        public void setVietqr(String vietqr) {
            this.vietqr = vietqr;
        }

        public NganHang(String id, String name, String code, String bin, String short_name, String logo, String vietqr) {
            this.id = id;
            this.name = name;
            this.code = code;
            this.bin = bin;
            this.short_name = short_name;
            this.logo = logo;
            this.vietqr = vietqr;
        }

}
