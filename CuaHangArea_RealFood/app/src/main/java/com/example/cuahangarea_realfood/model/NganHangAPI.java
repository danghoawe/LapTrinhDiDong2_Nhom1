package com.example.cuahangarea_realfood.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NganHangAPI {
    String BASE_URL = "https://api.vietqr.io/";
    @GET("v1/banks/")
    Call<NganHangWrapper<NganHang>> getNganHang();
}
