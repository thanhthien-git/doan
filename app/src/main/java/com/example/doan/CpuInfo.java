package com.example.doan;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CpuInfo {
    private long prevTotal = 0;
    private long prevIdle = 0;

    public float getCpuUsage() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("/proc/stat"));
            String line = reader.readLine();
            reader.close();

            if (line != null && line.startsWith("cpu ")) {
                String[] tokens = line.split("\\s+");

                long idle = Long.parseLong(tokens[4]);
                long total = 0;

                // Tính tổng các giá trị CPU
                for (int i = 1; i < tokens.length; i++) {
                    total += Long.parseLong(tokens[i]);
                }

                long diffTotal = total - prevTotal;
                long diffIdle = idle - prevIdle;

                prevTotal = total;
                prevIdle = idle;

                if (diffTotal == 0) {
                    return 0;
                }

                float usage = (float) (diffTotal - diffIdle) / diffTotal * 100;
                return Math.max(0, Math.min(100, usage));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
