package com.example.cuahangarea_realfood.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cuahangarea_realfood.Firebase_Manager;
import com.example.cuahangarea_realfood.screen.DoiMatKhauActivity;
import com.example.cuahangarea_realfood.screen.TaiKhoanNganHangActivity;
import com.example.cuahangarea_realfood.databinding.FragmentSettingBinding;

public class SettingFragment extends Fragment {

    Firebase_Manager  firebase_manager = new Firebase_Manager();
    FragmentSettingBinding binding;
    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(getLayoutInflater());

        binding.txtDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DoiMatKhauActivity.class);
                getActivity().startActivity(intent);
            }
        });
        binding.txtTaiKhoanNganHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TaiKhoanNganHangActivity.class);
                getActivity().startActivity(intent);
            }
        });
        binding.btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebase_manager.auth.signOut();
                getActivity().finish();
            }
        });
        binding.txtDichVuVaChinhSach.setOnClickListener(onClickListener);
        binding.txtRiengTu.setOnClickListener(onClickListener);
        binding.txtTroGiup.setOnClickListener(onClickListener);
        return binding.getRoot();
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openWebURL("https://mason.gmu.edu/~rhanson/policymarkets.html");
        }
    };
    public void openWebURL( String inURL ) {
        Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse( inURL ) );

        startActivity( browse );
    }
}