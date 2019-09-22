package com.example.glassespart.opencv;

import android.os.AsyncTask;
import android.util.Log;

import com.example.glassespart.config.NetworkConfig;
import com.example.glassespart.network.ConnectionCtx;
import com.example.glassespart.network.WifiTCPSocket;

import static java.lang.Thread.sleep;

public class VideoReceiver extends AsyncTask  {
    private boolean isFinish = false;
    private ConnectionCtx TCPContext;

    void setFinish(boolean isFinish) {
        this.isFinish = isFinish;
    }

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
    protected Void doInBackground(Object[] objects) {

//        TCPContext = new ConnectionCtx();
//        WifiTCPSocket wifiTCPSocket = new WifiTCPSocket();
//        wifiTCPSocket.execute(TCPContext);
        Log.d("DEBUG", "video receiver started");
        TCPContext.setTargetIpAddress(NetworkConfig.IP_TARGET_ADDRESS);
        TCPContext.setTargetPort(NetworkConfig.CAMERA_TARGET_PORT);
        TCPContext.pushMessageCtx(ConnectionCtx.operations.CREATE_CONNECTION);

        byte[] byteFrame = new byte[FramesQueue.getInstance().MTU_MESSAGE_SIZE];
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("DEBUG", "connection created");

        while (!isFinish) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("DEBUG", "receive message");

            TCPContext.pushMessageCtx(ConnectionCtx.operations.RECEIVE_MESSAGE, byteFrame);
            FramesQueue.getInstance().pushMessageChumk(byteFrame);
            Log.d("DEBUG", "message received");
        }

        return null;
    }
}
