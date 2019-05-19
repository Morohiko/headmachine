package com.example.glassespart.network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class WifiTCPSocket extends AsyncTask<ConnectionCtx, Void, Integer> {
    private Socket socket = null;
    private int timeThreadSleep = 2000;

    private OutputStreamWriter sendStream = null;
    private DataInputStream recvStream = null;

    private void createConnection(String ipAddress, int portNumber) {
        try {
            socket = new Socket(ipAddress, portNumber);
            Log.d("DEBUG", "Socket created with ip: " + ipAddress + ", port: " + portNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        try {
            if (sendStream != null) {
                sendStream.close();
                sendStream = null;
            }

            if (recvStream != null) {
                recvStream.close();
                recvStream = null;
            }

            if (socket != null) {
                socket.close();
                socket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("DEBUG", "Socket closed");
    }


    private void receiveMessage() {
        try {
            Log.d("DEBUG","started recv message");

            if (recvStream == null) {
                recvStream = new DataInputStream(socket.getInputStream());
            }

            int buffSize = 8;
            int size = 0;

            while (size >= 0) {
                byte[] message = new byte[buffSize];
                size = recvStream.read(message, 0, buffSize);
                String text = new String(message);
                Log.d("DEBUG", "received message = " + text + ", size = " + size);
            }
            recvStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) {
        try {
            Log.d("DEBUG","started send message");
            if (sendStream == null) {
                sendStream = new OutputStreamWriter(socket.getOutputStream());
            }
            sendStream.write(message);
            sendStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    createConnection(parameter[0].getIpAddress(), parameter[0].getPortNumber());
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
