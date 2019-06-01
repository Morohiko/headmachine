package com.example.glassespart.gyroscope;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import com.example.glassespart.config.NetworkConfig;
import com.example.glassespart.network.ConnectionCtx;
import com.example.glassespart.network.WifiUDPSocket;

public class Gyroscope {
    private Activity activity;
    private GyroscopeInternal gyroscopeInternal;


    @SuppressLint("Assert")
    public Gyroscope(Activity activity) {
        this.activity = activity;
    }


    public void startGyroscopeOverUDP(){
        gyroscopeInternal = new GyroscopeInternal(activity);

        gyroscopeInternal.startGyroscope();

        GyroscopeUDPThread gyroscopeUDPThread = new GyroscopeUDPThread();
        AsyncTask at = new GyroscopeUDPThread().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, gyroscopeInternal);
    }
}

class GyroscopeUDPThread extends AsyncTask<GyroscopeInternal, Void, Integer> {
    private ConnectionCtx UDPContext = null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        UDPContext = new ConnectionCtx();

        AsyncTask aa = new WifiUDPSocket().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, UDPContext);

        UDPContext.setTargetIpAddress(NetworkConfig.TARGETIPADDRESS);
        UDPContext.setTargetPort(NetworkConfig.TARGETPORT);
        UDPContext.setLocatPort(NetworkConfig.LOCALPORT);

        UDPContext.pushMessageCtx(ConnectionCtx.operations.CREATE_CONNECTION, null);
    }

    @Override
    protected Integer doInBackground(GyroscopeInternal... gyroscopeInternal) {
        Log.d("DEBUG", "do in background");

        SystemClock.sleep(500);
        while (true) {
            if (UDPContext == null) return -1;
            String sendedText = gyroscopeInternal[0].getData();
            Log.d("DEBUG", "Sended text: " + sendedText);
            UDPContext.pushMessageCtx(ConnectionCtx.operations.SEND_MESSAGE, sendedText);
        }
    }
}

