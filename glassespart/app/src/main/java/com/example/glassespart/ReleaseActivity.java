package com.example.glassespart;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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
        boolean success = true;
        try {
            success = startMPController();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (success) {
            startVideoReceive();
            startVideoRecording();
            startSendGyroscopeData();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(ReleaseActivity.this);
            builder.setTitle("ERROR")
                    .setMessage("Cannot establisg connection")
                    .setCancelable(false)
                    .setNegativeButton("exit",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    finish();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    boolean startMPController() throws InterruptedException {
        Controller controller = new Controller();
        boolean success = controller.startController();
        if (!success) {
            return success;
        }

        success = controller.setCameraState(1);
        if (!success) {
            return success;
        }

        sleep(2000);
        success = controller.setCameraTransmitterState(1);
        if (!success) {
            return success;
        }

        sleep(2000);
        success = controller.setGyroscopeReceoverState(1);
        if (!success) {
            return success;
        }

        sleep(2000);
        success = controller.setMotorState(1);
        if (!success) {
            return success;
        }

        return success;
    }

    void startSendGyroscopeData() {
        gyroscope = new Gyroscope(this);
        gyroscope.startGyroscopeOverUDP();
    }

    void startVideoRecording() {
        startActivity(new Intent(ReleaseActivity.this, VideoRecorderActivity.class));
        Log.d("DEBUG", "after start activity");
    }

    void startVideoReceive() {
        AsyncTask aa = new VideoReceiver().execute();
    }
}
