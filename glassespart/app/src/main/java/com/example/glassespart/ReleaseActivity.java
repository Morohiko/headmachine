package com.example.glassespart;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.glassespart.controller.Controller;
import com.example.glassespart.gyroscope.Gyroscope;
import com.example.glassespart.opencv.VideoReceiver;
import com.example.glassespart.opencv.VideoRecorderActivity;

import static java.lang.Thread.sleep;

public class ReleaseActivity extends AppCompatActivity {
    private Gyroscope gyroscope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);

        try {
            startMPController();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startVideoReceive();
        startVideoRecording();
        startSendGyroscopeData();
    }

    void startMPController() throws InterruptedException {
        Controller controller = new Controller();
        controller.startController();

        controller.setCameraState(1);
        sleep(2000);
        controller.setCameraTransmitterState(1);
        sleep(2000);
        controller.setGyroscopeReceoverState(1);
        sleep(2000);
        controller.setMotorState(1);
    }

    void startSendGyroscopeData() {
        gyroscope = new Gyroscope(this);
        gyroscope.startGyroscopeOverUDP();
    }

    void startVideoRecording() {
        startActivity(new Intent(ReleaseActivity.this, VideoRecorderActivity.class));
    }

    void startVideoReceive() {
        AsyncTask aa = new VideoReceiver().execute();
    }
}
