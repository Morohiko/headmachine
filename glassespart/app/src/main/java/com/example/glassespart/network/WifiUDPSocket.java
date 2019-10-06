package com.example.glassespart.network;

import android.os.AsyncTask;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class WifiUDPSocket extends AsyncTask<ConnectionCtx, Void, Integer> {
    private int timeThreadSleep = 1;
    private DatagramSocket socket;

    private DatagramPacket packetIn;
    private int BUFFER_SIZE = 1000;


    private void createConnection(String targetIpAddress, int targetPort, int localPort) {
        try {
            socket = new DatagramSocket(localPort);
            socket.connect(InetAddress.getByName(targetIpAddress), targetPort);

            Log.d("DEBUG", "Socket created targetIp ip: " + targetIpAddress + ", target port: " + targetPort + ", local port: " + localPort);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String msg) {
        try {
            if (!socket.isConnected()) {
                Log.d("ERROR", "socket is not connected");
                return;
            }
            if (socket.isClosed()) {
                Log.d("DEBUG", "udp socket is closed");
                return;
            }
            Log.d("DEBUG","started send UDP message");
            byte[] DataOut = msg.getBytes();
            packetIn = new DatagramPacket(DataOut, DataOut.length);

            socket.send(packetIn);

            Log.d("DEBUG", "sended msg: " + msg);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void receiveMessage() {
        try {
            Log.d("DEBUG","started recv UDP message");
            byte[] DataOut = new byte[BUFFER_SIZE];

            packetIn = new DatagramPacket(DataOut, DataOut.length);

            socket.receive(packetIn);

            String msg = new String(packetIn.getData(), packetIn.getOffset(), packetIn.getLength());
            Log.d("DEBUG", "msg: " + msg);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        if (socket != null) {
            Log.d("DEBUG", "socket realy close");
            socket.disconnect();
            socket.close();
            socket = null;
        }

        Log.d("DEBUG", "Socket closed");
    }

    @Override
    protected Integer doInBackground(ConnectionCtx[] parameter) {
        while (true) {
            try {
                Thread.sleep(timeThreadSleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            MessageCtx msgCtx = parameter[0].pullMessageCtx();

            if (msgCtx == null) {
//                Log.d("DEBUG: ", "parameter[0].pullMessageCtx(op, msg) == -1");
                continue;
            } else {
//                Log.d("DEBUG", "Something here");
            }

            switch (msgCtx.operation) {
                case CREATE_CONNECTION:
                    createConnection(parameter[0].getTargetIpAddress(), parameter[0].getTargetPort(), parameter[0].getLocatPort());
                    break;
                case CLOSE_CONNECTION:
                    closeConnection();
                    break;
                case SEND_MESSAGE:
                    sendMessage(msgCtx.message);
                    break;
                case RECEIVE_MESSAGE:
                    receiveMessage();
                    break;
                default:
                    Log.d("DEBUG", "dont know operation");
            }
            Log.d("DEBUG", String.valueOf(msgCtx.message));
        }
    }
}
