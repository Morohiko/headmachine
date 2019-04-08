package com.example.glassespart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private Gyroscope gyroscope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkGyroscopeWorking();
    }

    private void checkGyroscopeWorking() {
        Gyroscope gyroscope = new Gyroscope(this);

        gyroscope.startGyroscope();
    }
}
