package com.example.glassespart.network;

import android.util.Log;

import com.example.glassespart.config.TotalConfig;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPNetwork {
    private Socket socket = null;

    private OutputStreamWriter sendStream = null;
    private DataInputStream recvStream = null;

    public int createConnection(String ipAddress, int portNumber) {
        try {
            Log.d("DEBUG", "before create socked ip = " + ipAddress + " port = " + portNumber);
            socket = new Socket(ipAddress, portNumber);
            Log.d("DEBUG", "Socket created with ip: " + ipAddress + ", port: " + portNumber);
        } catch (ConnectException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (socket == null) {
            return -1;
        } else {
            Log.d("DEBUG", "wtf, socket is not null");
            return 0;
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

            int buffSize = TotalConfig.IMAGE_SIZE;
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
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private PrintWriter mBufferOut;
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
                    Log.d("DEBUG", "buffer is null");
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
