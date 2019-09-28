package com.example.glassespart.opencv;

import android.os.AsyncTask;
import android.util.Log;

import com.example.glassespart.config.NetworkConfig;
import com.example.glassespart.network.ConnectionCtx;
import com.example.glassespart.network.TCPNetwork;
import com.example.glassespart.network.WifiTCPSocket;

import java.util.Arrays;

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
    }

    @Override
    protected Void doInBackground(Object[] objects) {
        TCPNetwork tcpFrameSocket = new TCPNetwork();
        tcpFrameSocket.createConnection(NetworkConfig.IP_TARGET_ADDRESS, NetworkConfig.CAMERA_FRAME_TARGET_PORT);
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TCPNetwork tcpAckSocket = new TCPNetwork();
        tcpAckSocket.createConnection(NetworkConfig.IP_TARGET_ADDRESS, NetworkConfig.CAMERA_ACK_TARGET_PORT);

        byte[] emptyArray = new byte[614400]; // workround caused by memset is not exist in java!!
        for (int i = 0; i < 614400; i++) {
            emptyArray[i] = '\0';
        }
        byte[] byteFrame = new byte[614400];

        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (!isFinish) {
            Log.d("DEBUG", "wait for receive message");
            System.arraycopy(emptyArray, 0, byteFrame, 0, 614400);
            tcpFrameSocket.receiveMessage(byteFrame);
            if (byteFrame[0] == '\0') {
                Log.d("DEBUG", "not received any messages, continue");
                continue;
            }
            Log.d("DEBUG", "received message");
            FramesQueue.getInstance().pushMessageChumk(byteFrame);

            String ackMessage = "msg";
            tcpAckSocket.sendMessage(ackMessage);
        }
        return null;
    }
}
