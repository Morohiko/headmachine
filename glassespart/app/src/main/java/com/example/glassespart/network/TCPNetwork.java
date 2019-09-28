package com.example.glassespart.network;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPNetwork {
    private Socket socket = null;

    private OutputStreamWriter sendStream = null;
    private DataInputStream recvStream = null;

    public void createConnection(String ipAddress, int portNumber) {
        while (socket == null) {
            try {
                Log.d("DEBUG", "before create socked ip = " + ipAddress + " port = " + portNumber);
                socket = new Socket(ipAddress, portNumber);

                Log.d("DEBUG", "after create socked");
                Log.d("DEBUG", "Socket created with ip: " + ipAddress + ", port: " + portNumber);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.d("DEBUG", "AAAAAA" + portNumber);
        if (socket == null) {
            Log.d("DEBUG", "AAAAAA socket is null" + portNumber);
        }
    }

    public void closeConnection() {
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

    public void receiveMessage(byte[] message) {
        if (socket == null || !socket.isConnected()) {
            Log.d("ERROR", "tcp socket is null, cannot establish connection, return");
            return;
        }
        Log.d("DEBUG","started recv message");

        try {
            if (recvStream == null) {
                recvStream = new DataInputStream(socket.getInputStream());
            }

            int buffSize = 614400;
            int size = 0;

            int receivedMsgSize;

            while (size < buffSize) {
                byte[] buff = new byte[buffSize];

                receivedMsgSize = recvStream.read(buff, 0, buffSize);
                if (receivedMsgSize <= 0) {
                    break;
                }
                System.arraycopy(buff, 0, message, size, receivedMsgSize);

                size += receivedMsgSize;

                if (size < 0) {
                    Log.d("ERROR", "size != buffsize");
                    break;
                }
//                Log.d("DEBUG", "received message, size = " + size);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private PrintWriter mBufferOut;
    static BufferedWriter out;
    public void sendMessage(final String message) {
        if (socket == null || !socket.isConnected()) {
            Log.d("ERROR", "tcp socket is null, cannot establish connection, return");
            return;
        }

        try {
            mBufferOut = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mBufferOut != null) {
                    Log.d("DEBUG", "Sending: " + message);
                    mBufferOut.println(message);
                    mBufferOut.flush();
                } else {
                    Log.d("DEBUG", "fucking null");

                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
