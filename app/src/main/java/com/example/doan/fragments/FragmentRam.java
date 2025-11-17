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
import androidx.fragment.app.Fragment;
import com.example.doan.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import java.util.ArrayList;

import com.example.doan.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentRam#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentRam extends Fragment {
    private TextView tvRamUsed;
    private ProgressBar progressRam;
    private LineChart ramChart;
    private Handler handler = new Handler();
    private ArrayList<Entry> entries = new ArrayList<>();
    private int entryCount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ram, container, false);

        tvRamUsed = view.findViewById(R.id.tv_ram_used);
        progressRam = view.findViewById(R.id.progress_ram);
        ramChart = view.findViewById(R.id.ram_chart);

        setupChart();
        startMonitoring();

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
                ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
                ActivityManager activityManager = (ActivityManager)
                        requireContext().getSystemService(Context.ACTIVITY_SERVICE);
                activityManager.getMemoryInfo(memInfo);

                long totalMem = memInfo.totalMem / (1024 * 1024);
                long availMem = memInfo.availMem / (1024 * 1024);
                long usedMem = totalMem - availMem;

                float usagePercent = (usedMem * 100f) / totalMem;

                tvRamUsed.setText(String.format("%d MB / %d MB", usedMem, totalMem));
                progressRam.setMax(100);
                progressRam.setProgress((int) usagePercent);

                entries.add(new Entry(entryCount++, usagePercent));
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