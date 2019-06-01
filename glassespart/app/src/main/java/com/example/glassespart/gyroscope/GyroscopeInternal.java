package com.example.glassespart.gyroscope;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

@SuppressLint("Registered")
public class GyroscopeInternal extends Activity {

    private SensorManager sensorManager;
    private Sensor sensorGyroscope;

    private SensorEventListener listenerGyroscope;

    private int changeX = 0;
    private int changeY = 0;
    private int changeZ = 0;

    private int X = 0;
    private int Y = 1;
    private int Z = 2;

    GyroscopeInternal(Activity activity) {
        sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        sensorGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        listenerGyroscope = new SensorEventListener() {
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }

            @Override
            public void onSensorChanged(SensorEvent event) {
//                Log.d("DEBUG", "onSensorChanged");
                changeX = (int) (event.values[X] * 100);
                changeY = (int) (event.values[Y] * 100);
                changeZ = (int) (event.values[Z] * 100);

//                Log.d("DEBUG", "Gyroscope: " +
//                        "X = " + changeX + ", Y = " + changeY + ", Z = " + changeZ);
            }
        };
    }

    void startGyroscope() {
        sensorManager.registerListener(listenerGyroscope, sensorGyroscope,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    void stopGyroscope() {
        sensorManager.unregisterListener(listenerGyroscope, sensorGyroscope);
        changeX = 0;
        changeY = 0;
        changeZ = 0;
    }

    String getData(){
        return Integer.toString(changeX) + ':' +
                Integer.toString(changeY) + ':' +
                Integer.toString(changeZ) + '\n';
    }
}

