package com.example.doan;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.doan.fragments.FragmentBattery;
import com.example.doan.fragments.FragmentCpu;
import com.example.doan.fragments.FragmentRam;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView cardCpu = findViewById(R.id.card_cpu);
        CardView cardRam = findViewById(R.id.card_ram);
        CardView cardBattery = findViewById(R.id.card_battery);

        cardCpu.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new FragmentCpu())
                    .addToBackStack(null)
                    .commit();
        });

        cardRam.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new FragmentRam())
                    .addToBackStack(null)
                    .commit();
        });

        cardBattery.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new FragmentBattery())
                    .addToBackStack(null)
                    .commit();
        });
    }
}