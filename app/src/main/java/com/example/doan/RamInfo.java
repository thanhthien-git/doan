package com.example.doan;

import android.app.ActivityManager;
import android.content.Context;

public class RamInfo {
    private long totalRam;
    private long usedRam;
    private float usagePercent;

    public RamInfo(long totalRam, long usedRam, float usagePercent) {
        this.totalRam = totalRam;
        this.usedRam = usedRam;
        this.usagePercent = usagePercent;
    }

    public long getTotalRam() {
        return totalRam;
    }

    public long getUsedRam() {
        return usedRam;
    }

    public float getUsagePercent() {
        return usagePercent;
    }

    public static RamInfo getCurrentRamInfo(Context context) {
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(memInfo);

        long totalMem = memInfo.totalMem / (1024 * 1024);
        long availMem = memInfo.availMem / (1024 * 1024);
        long usedMem = totalMem - availMem;

        float percent = (usedMem * 100f) / totalMem;

        return new RamInfo(totalMem, usedMem, percent);
    }
}
