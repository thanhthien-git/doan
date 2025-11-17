package com.example.doan.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.doan.R;
import com.example.doan.CpuReader;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import java.util.ArrayList;


public class FragmentCpu extends Fragment {
    private TextView tvCpuUsage;
    private LineChart cpuChart;
    private Handler handler = new Handler();
    private CpuReader cpuReader;
    private ArrayList<Entry> entries = new ArrayList<>();
    private int entryCount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cpu, container, false);

        tvCpuUsage = view.findViewById(R.id.tv_cpu_usage);
        cpuChart = view.findViewById(R.id.cpu_chart);
        cpuReader = new CpuReader();

        setupChart();
        startMonitoring();

        return view;
    }

    private void setupChart() {
        cpuChart.getDescription().setEnabled(false);
        cpuChart.setTouchEnabled(true);
        cpuChart.setDragEnabled(true);
        cpuChart.setScaleEnabled(true);
        cpuChart.setPinchZoom(true);

        XAxis xAxis = cpuChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(getResources().getColor(R.color.text_gray));

        cpuChart.getAxisLeft().setTextColor(getResources().getColor(R.color.text_gray));
        cpuChart.getAxisRight().setEnabled(false);
        cpuChart.getLegend().setEnabled(false);
    }

    private void startMonitoring() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                float cpuUsage = cpuReader.getCpuUsage();
                tvCpuUsage.setText(String.format("%.1f%%", cpuUsage));

                entries.add(new Entry(entryCount++, cpuUsage));
                if (entries.size() > 30) {
                    entries.remove(0);
                }

                updateChart();
                handler.postDelayed(this, 1000);
            }
        }, 0);
    }

    private void updateChart() {
        LineDataSet dataSet = new LineDataSet(entries, "CPU Usage");
        dataSet.setColor(getResources().getColor(R.color.green_primary));
        dataSet.setValueTextColor(getResources().getColor(R.color.text_dark));
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(3f);
        dataSet.setCircleColor(getResources().getColor(R.color.green_primary));
        dataSet.setDrawValues(false);

        LineData lineData = new LineData(dataSet);
        cpuChart.setData(lineData);
        cpuChart.notifyDataSetChanged();
        cpuChart.invalidate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
    }
}