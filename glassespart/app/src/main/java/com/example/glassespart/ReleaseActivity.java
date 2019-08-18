package com.example.glassespart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.glassespart.gyroscope.Gyroscope;

public class ReleaseActivity extends AppCompatActivity {
    private Gyroscope gyroscope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
        initSwitches();
    }

    private void initSwitches() {
        gyroscope = new Gyroscope(this);

        Switch gyroscopeSwitch = findViewById(R.id.gyroscopeProcessingSwitch);
        gyroscopeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    gyroscope.startGyroscopeOverUDP();
                    Log.d("DEBUG", "gyroscope processing started");
                } else {
                    gyroscope.stopGyroscopeOverUDP();
                    Log.d("DEBUG", "gyroscope processing stoped");
                }
            }
        });
    }
}
