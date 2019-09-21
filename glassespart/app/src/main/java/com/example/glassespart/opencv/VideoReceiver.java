package com.example.glassespart.opencv;

import android.os.AsyncTask;
import android.util.Log;

import com.example.glassespart.config.NetworkConfig;
import com.example.glassespart.network.ConnectionCtx;
import com.example.glassespart.network.WifiTCPSocket;

import static java.lang.Thread.sleep;

public class VideoReceiver extends AsyncTask {

    private ConnectionCtx TCPContext;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        new Thread(new Runnable() {
            public void run() {

                TCPContext = new ConnectionCtx();
                WifiTCPSocket wifiTCPSocket = new WifiTCPSocket();
                wifiTCPSocket.doInBackground(TCPContext);
                try {
                    sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected Object doInBackground(Object[] objects) {

//        TCPContext = new ConnectionCtx();
//        WifiTCPSocket wifiTCPSocket = new WifiTCPSocket();
//        wifiTCPSocket.execute(TCPContext);
        Log.d("DEBUG", "video receiver started");
        TCPContext.setTargetIpAddress(NetworkConfig.IP_TARGET_ADDRESS);
        TCPContext.setTargetPort(NetworkConfig.CAMERA_TARGET_PORT);
        TCPContext.pushMessageCtx(ConnectionCtx.operations.CREATE_CONNECTION, null);
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("DEBUG", "connection created");
        int count = 5;
        while (true) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("DEBUG", "receive message");


            TCPContext.pushMessageCtx(ConnectionCtx.operations.RECEIVE_MESSAGE, null);
            Log.d("DEBUG", "message received");

            if (count-- == 0) break;
        }

        Log.d("DEBUG", "AAAAAAAAAAAAAAAA");
        return null;
    }
}
