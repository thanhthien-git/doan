package com.example.doan.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.doan.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class FragmentBattery extends Fragment {

    private TextView tvBatteryLevel, tvBatteryTemp, tvBatteryStatus;
    private LineChart batteryChart;
    private ArrayList<Entry> entries = new ArrayList<>();
    private int entryCount = 0;
    private Handler handler = new Handler();

    private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float batteryPct = (level / (float) scale) * 100;

            int temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
            float temperature = temp / 10.0f;

            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;

            tvBatteryLevel.setText(String.format("%.0f%%", batteryPct));
            tvBatteryTemp.setText(String.format("Temperature: %.1f°C", temperature));
            tvBatteryStatus.setText("Status: " + (isCharging ? "Charging" : "Not Charging"));

            entries.add(new Entry(entryCount++, batteryPct));
            if (entries.size() > 30) {
                entries.remove(0);
            }
            updateChart();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_battery, container, false);

        tvBatteryLevel = view.findViewById(R.id.tv_battery_level);
        tvBatteryTemp = view.findViewById(R.id.tv_battery_temp);
        tvBatteryStatus = view.findViewById(R.id.tv_battery_status);
        batteryChart = view.findViewById(R.id.battery_chart);

        setupChart();

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        requireContext().registerReceiver(batteryReceiver, filter);

        // Setup Bottom Navigation
        BottomNavigationView bottomNav = view.findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_battery); // Đặt item hiện tại

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_battery) {
                // Đã ở Battery rồi, không làm gì
                return true;
            } else if (itemId == R.id.nav_cpu) {
                selectedFragment = new FragmentCpu();
            } else if (itemId == R.id.nav_ram) {
                selectedFragment = new FragmentRam();
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
        batteryChart.getDescription().setEnabled(false);
        batteryChart.setTouchEnabled(true);
        batteryChart.setDragEnabled(true);
        batteryChart.setScaleEnabled(true);

        XAxis xAxis = batteryChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(getResources().getColor(R.color.text_gray));

        batteryChart.getAxisLeft().setTextColor(getResources().getColor(R.color.text_gray));
        batteryChart.getAxisRight().setEnabled(false);
        batteryChart.getLegend().setEnabled(false);
    }

    private void updateChart() {
        LineDataSet dataSet = new LineDataSet(entries, "Battery Level");
        dataSet.setColor(getResources().getColor(R.color.green_primary));
        dataSet.setValueTextColor(getResources().getColor(R.color.text_dark));
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(3f);
        dataSet.setCircleColor(getResources().getColor(R.color.green_primary));
        dataSet.setDrawValues(false);

        LineData lineData = new LineData(dataSet);
        batteryChart.setData(lineData);
        batteryChart.notifyDataSetChanged();
        batteryChart.invalidate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        requireContext().unregisterReceiver(batteryReceiver);
        handler.removeCallbacksAndMessages(null);
    }
}