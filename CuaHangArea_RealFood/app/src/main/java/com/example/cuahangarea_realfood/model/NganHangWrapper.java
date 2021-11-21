package com.example.cuahangarea_realfood.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NganHangWrapper <T>{
        @SerializedName("data")
        public ArrayList<T> items;
}
