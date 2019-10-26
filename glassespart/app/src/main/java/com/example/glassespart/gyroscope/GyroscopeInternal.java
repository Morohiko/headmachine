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

    private int currentX = 0;
    private int currentY = 0;

    private int X = 0;
    private int Y = 1;

    private long lastTimeInMillis;
    private int clarifyGyroscopeValue(int value) {
//        if (value < 0) {
//            value *= -1;
//        }
        return value;
    }

    private void setGyroscopeData(int x, int y) {
        currentX = resizeGyroscopeDataToNormal(currentX + clarifyGyroscopeValue(x));
        currentY = resizeGyroscopeDataToNormal(currentY + clarifyGyroscopeValue(y));
    }

    private int resizeGyroscopeDataToNormal(int value) {
//        value = value / 2;

        // TODO: do we need check < 0 value?
        if (value < 0) value = 0;
        if (value > 180) value = 180;
        return value;
    }

    GyroscopeInternal(Activity activity) {
        sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        sensorGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        lastTimeInMillis = System.currentTimeMillis();
        listenerGyroscope = new SensorEventListener() {
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }

            @Override
            public void onSensorChanged(SensorEvent event) {
//                Log.d("DEBUG", "onSensorChanged");
                long currentTimeMillis = System.currentTimeMillis();

                long diffsTimeMillis = currentTimeMillis - lastTimeInMillis;
                //Log.d("DEBUG", "diffsTimeMillis = " + diffsTimeMillis);
                //if (diffsTimeMillis < 1000) return;
                // setGyroscopeData((int) (event.values[X]), (int) (event.values[Y]));
                Log.d("DEBUG",  "diffsTimeMillis = " + (diffsTimeMillis / 1000.0));
                currentX += (int) ((event.values[X] * ((float) diffsTimeMillis / 1000.0)) * 57.296);
                currentY += (int) ((event.values[Y] * ((float) diffsTimeMillis / 1000.0)) * 57.296);
                lastTimeInMillis = currentTimeMillis;
                Log.d("DEBUG", "Gyroscope current: " +
                        "X = " + currentX + ", Y = " + currentY);
            }
        };
    }

    void startGyroscope() {
        currentX = 90;
        currentY = 90;
        sensorManager.registerListener(listenerGyroscope, sensorGyroscope,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    void stopGyroscope() {
        sensorManager.unregisterListener(listenerGyroscope, sensorGyroscope);
        currentX = 0;
        currentY = 0;
    }

    String getData(){
        return Integer.toString(currentX) + ':' +
                currentY + '\n';
    }
}

