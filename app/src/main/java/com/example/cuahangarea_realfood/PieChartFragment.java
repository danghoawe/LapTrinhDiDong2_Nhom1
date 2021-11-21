package com.example.cuahangarea_realfood;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;

import java.util.ArrayList;
import java.util.List;


public class PieChartFragment extends Fragment {

    int choXacNhanCoc = 0, dangXuLy = 0, dangGiaoHang = 0, giaoHangThanhCong = 0, giaoHangThatBai = 0;
    AnyChartView anyChartView;

    public PieChartFragment(int choXacNhanCoc, int dangXuLy, int dangGiaoHang, int giaoHangThanhCong, int giaoHangThatBai) {
        this.choXacNhanCoc = choXacNhanCoc;
        this.dangXuLy = dangXuLy;
        this.dangGiaoHang = dangGiaoHang;
        this.giaoHangThanhCong = giaoHangThanhCong;
        this.giaoHangThatBai = giaoHangThatBai;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view = inflater.inflate(R.layout.fragment_pie_chart, container, false);
        anyChartView = view.findViewById(R.id.piechartThongKe);
        Pie pie = AnyChart.pie();
        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Chờ xác nhận cọc", choXacNhanCoc));
        data.add(new ValueDataEntry("Đang xử lí", dangXuLy));
        data.add(new ValueDataEntry("Đang giao hàng ", dangGiaoHang));
        data.add(new ValueDataEntry("Giao hàng thành công", giaoHangThanhCong));
        data.add(new ValueDataEntry("Giao hàng thất bại", giaoHangThatBai));

        pie.data(data);
        pie.labels().position("outside");
        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("Trạng thái đơn hàng")
                .padding(0d, 0d, 10d, 0d);
        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);
        anyChartView.setChart(pie);
        return view;
    }
}