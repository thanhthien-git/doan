package com.example.doan.fragments;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.doan.R;
import com.example.doan.RamInfo;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class FragmentRam extends Fragment {
    private TextView tvRamUsed;
    private ProgressBar progressRam;
    private LineChart ramChart;
    private Handler handler = new Handler();
    private ArrayList<Entry> entries = new ArrayList<>();
    private int entryCount = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ram, container, false);

        tvRamUsed = view.findViewById(R.id.tv_ram_used);
        progressRam = view.findViewById(R.id.progress_ram);
        ramChart = view.findViewById(R.id.ram_chart);

        setupChart();
        startMonitoring();

        // Setup Bottom Navigation
        BottomNavigationView bottomNav = view.findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_ram); // Đặt item hiện tại

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_ram) {
                // Đã ở RAM rồi, không làm gì
                return true;
            } else if (itemId == R.id.nav_cpu) {
                selectedFragment = new FragmentCpu();
            } else if (itemId == R.id.nav_battery) {
                selectedFragment = new FragmentBattery();
            }

            if (selectedFragment != null) {
                getParentFragmentManager().beginTransaction()
                        .replace(android.R.id.content, selectedFragment)
                        .addToBackStack(null)
                        .commit();
            }
            return true;
        });

        return view;
    }

    private void setupChart() {
        ramChart.getDescription().setEnabled(false);
        ramChart.setTouchEnabled(true);
        ramChart.setDragEnabled(true);
        ramChart.setScaleEnabled(true);

        XAxis xAxis = ramChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(getResources().getColor(R.color.text_gray));

        ramChart.getAxisLeft().setTextColor(getResources().getColor(R.color.text_gray));
        ramChart.getAxisRight().setEnabled(false);
        ramChart.getLegend().setEnabled(false);
    }

    private void startMonitoring() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RamInfo ramInfo = RamInfo.getCurrentRamInfo(requireContext());

                tvRamUsed.setText(String.format("%d MB / %d MB", ramInfo.getUsedRam(), ramInfo.getTotalRam()));
                progressRam.setMax(100);
                progressRam.setProgress((int) ramInfo.getUsagePercent());

                entries.add(new Entry(entryCount++, ramInfo.getUsagePercent()));
                if (entries.size() > 30) {
                    entries.remove(0);
                }

                updateChart();
                handler.postDelayed(this, 1000);
            }
        }, 0);
    }


    private void updateChart() {
        LineDataSet dataSet = new LineDataSet(entries, "RAM Usage");
        dataSet.setColor(getResources().getColor(R.color.green_primary));
        dataSet.setValueTextColor(getResources().getColor(R.color.text_dark));
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(3f);
        dataSet.setCircleColor(getResources().getColor(R.color.green_primary));
        dataSet.setDrawValues(false);

        LineData lineData = new LineData(dataSet);
        ramChart.setData(lineData);
        ramChart.notifyDataSetChanged();
        ramChart.invalidate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
    }
}