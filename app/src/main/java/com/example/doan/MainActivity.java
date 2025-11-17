package com.example.doan;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.doan.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.doan.fragments.FragmentBattery;
import com.example.doan.fragments.FragmentCpu;
import com.example.doan.fragments.FragmentRam;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // Load fragment mặc định
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new FragmentCpu())
                    .commit();
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_cpu) {
                selectedFragment = new FragmentCpu();
            } else if (itemId == R.id.nav_ram) {
                selectedFragment = new FragmentRam();
            } else if (itemId == R.id.nav_battery) {
                selectedFragment = new FragmentBattery();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });
    }
}