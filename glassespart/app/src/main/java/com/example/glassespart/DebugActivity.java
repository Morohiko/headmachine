package com.example.glassespart;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.glassespart.controller.Controller;
import com.example.glassespart.gyroscope.Gyroscope;
import com.example.glassespart.network.ConnectionCtx;
import com.example.glassespart.network.WiFi;
import com.example.glassespart.network.WifiTCPSocket;
import com.example.glassespart.network.WifiUDPSocket;

import com.example.glassespart.config.NetworkConfig;
import com.example.glassespart.opencv.VideoReceiver;
import com.example.glassespart.opencv.VideoRecorderActivity;

import static java.lang.Thread.sleep;

public class DebugActivity extends AppCompatActivity {
    private ConnectionCtx TCPContext;
    private ConnectionCtx UDPContext;
    private Gyroscope gyroscope;

    private void debugWiFiTCPConnection() {
        Button startBtn = findViewById(R.id.startTCPThreadBtnDbg);
        Button recvMsgBtb = findViewById(R.id.recvMsgTCPBtnDbg);
        Button sendMsgBtb = findViewById(R.id.sendMsgTCPBtnDbg);
        Button connectBtn = findViewById(R.id.connectTCPBtnDbg);
        Button disconnectBtn = findViewById(R.id.disconnectTCPBtnDbg);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            TCPContext = new ConnectionCtx();
//            AsyncTask aa = new WifiTCPSocket().execute(TCPContext);
            }
        });

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Log.d("DEBUG","connect Button is clicked");
            TCPContext.setTargetIpAddress(NetworkConfig.IP_TARGET_ADDRESS);
            TCPContext.setTargetPort(NetworkConfig.TEST_TARGET_PORT);
            TCPContext.pushMessageCtx(ConnectionCtx.operations.CREATE_CONNECTION);
            }
        });

        disconnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            TCPContext.pushMessageCtx(ConnectionCtx.operations.CLOSE_CONNECTION);
            }
        });

        sendMsgBtb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Log.d("DEBUG","send Msg Button is clicked");
            TCPContext.pushMessageCtx(ConnectionCtx.operations.SEND_MESSAGE, "some msg");
            }
        });

        recvMsgBtb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Log.d("DEBUG","recv Msg Button is clicked");
            byte[] message = new byte[100000];
            TCPContext.pushMessageCtx(ConnectionCtx.operations.RECEIVE_MESSAGE, message);
            }
        });
    }

    private void debugWiFiUDPConnection() {
        Button startBtn = findViewById(R.id.startUDPThreadBtnDbg);
        Button recvMsgBtb = findViewById(R.id.recvMsgUDPBtnDbg);
        Button sendMsgBtb = findViewById(R.id.sendMsgUDPBtnDbg);
        Button connectBtn = findViewById(R.id.connectUDPBtnDbg);
        Button disconnectBtn = findViewById(R.id.disconnectUDPBtnDbg);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            UDPContext = new ConnectionCtx();
            AsyncTask aa = new WifiUDPSocket().execute(UDPContext);
            }
        });

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Log.d("DEBUG","connect Button is clicked");
            UDPContext.setTargetIpAddress(NetworkConfig.IP_TARGET_ADDRESS);
            UDPContext.setTargetPort(NetworkConfig.TEST_TARGET_PORT);
            UDPContext.setLocatPort(NetworkConfig.TEST_LOCAL_PORT);
            UDPContext.pushMessageCtx(ConnectionCtx.operations.CREATE_CONNECTION);
            }
        });

        disconnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            UDPContext.pushMessageCtx(ConnectionCtx.operations.CLOSE_CONNECTION);
            }
        });

        sendMsgBtb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Log.d("DEBUG","send Msg Button is clicked");
            UDPContext.pushMessageCtx(ConnectionCtx.operations.SEND_MESSAGE, "some msg");
            }
        });

        recvMsgBtb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Log.d("DEBUG","recv Msg Button is clicked");
            byte[] message = new byte[100000];
            UDPContext.pushMessageCtx(ConnectionCtx.operations.RECEIVE_MESSAGE, message);
            }
        });
    }

    private TextView debugGyroscopeText;
    @SuppressLint("SetTextI18n")
    private void debugGyroscope() {
        Button debugGyroscopeBtn = findViewById(R.id.debugRunGyroscopeBtn);
        debugGyroscopeText = findViewById(R.id.debugGyroscopeText);

        gyroscope = new Gyroscope(this);

        debugGyroscopeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            gyroscope.startGyroscopeOverUDP();
            }
        });
    }

    private void debugVideoRecorder() {
        Button videoRecorderBtn = findViewById(R.id.videoRecorder);
        videoRecorderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DebugActivity.this, VideoRecorderActivity.class));
            }
        });
    }

    private void debugVideoReceiver() {
        Button videoReceiverBtn = findViewById(R.id.debugVideoReceiverBtn);
        videoReceiverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask aa = new VideoReceiver().execute();
            }
        });

    }

    private void debugController() {
        Button controllerBtn = findViewById(R.id.debugControllerBtn);
        controllerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Controller controller = new Controller();
                controller.startController();
                //TODO: remove it, test only
                controller.setCameraState(1);
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                controller.setCameraTransmitterState(1);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
//        debugWiFiTCPConnection();
//        debugWiFiUDPConnection();

//        debugGyroscope();
//        checkWiFiWotking();

        debugVideoRecorder();
        debugVideoReceiver();

        debugController();
    }

    private void checkWiFiWotking() {
        WiFi wiFi = new WiFi();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(wiFi, intentFilter);
    }
}
