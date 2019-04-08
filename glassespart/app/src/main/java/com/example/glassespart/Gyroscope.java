package com.example.glassespart;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

class Gyroscope extends Activity {
    private SensorManager sensorManager;
    private Sensor sensorGyroscope;

    private SensorEventListener listenerGyroscope;

    private float changeX = 0;
    private float changeY = 0;
    private float changeZ = 0;

    private int X = 0;
    private int Y = 1;
    private int Z = 2;

    Gyroscope(Activity activity) {
        sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        sensorGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        listenerGyroscope = new SensorEventListener() {
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }

            @Override
            public void onSensorChanged(SensorEvent event) {
                System.out.println("onSensorChanged");
                changeX = event.values[X];
                changeY = event.values[Y];
                changeZ = event.values[Z];

                System.out.println("Gyroscope: \n" +
                        "X = " + changeX + ", Y = " + changeY + ", Z = " + changeZ);

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

    float getChangeX() {
        return changeX;
    }

    float getChangeY() {
        return changeY;
    }

    float getChangeZ() {
        return changeZ;
    }
}
