package com.example.doan;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

public class BatteryInfo {

    private float batteryLevel;
    private float temperature;
    private boolean isCharging;

    public BatteryInfo(float batteryLevel, float temperature, boolean isCharging) {
        this.batteryLevel = batteryLevel;
        this.temperature = temperature;
        this.isCharging = isCharging;
    }

    public float getBatteryLevel() {
        return batteryLevel;
    }

    public float getTemperature() {
        return temperature;
    }

    public boolean isCharging() {
        return isCharging;
    }

    public static BatteryInfo getCurrentBatteryInfo(Context context) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        if (batteryStatus == null) {
            return new BatteryInfo(0f, 0f, false);
        }

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = (level / (float) scale) * 100;

        int temp = batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
        float temperature = temp / 10.0f;

        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        return new BatteryInfo(batteryPct, temperature, isCharging);
    }
}

