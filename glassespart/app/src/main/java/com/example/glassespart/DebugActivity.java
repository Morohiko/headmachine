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
import android.widget.TextView;

import com.example.glassespart.gyroscope.Gyroscope;
import com.example.glassespart.network.ConnectionCtx;
import com.example.glassespart.network.WiFi;
import com.example.glassespart.network.WifiTCPSocket;
import com.example.glassespart.network.WifiUDPSocket;

import com.example.glassespart.config.NetworkConfig;
import com.example.glassespart.opencv.VideoRecorderActivity;

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
            AsyncTask aa = new WifiTCPSocket().execute(TCPContext);
            }
        });

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Log.d("DEBUG","connect Button is clicked");
            TCPContext.setTargetIpAddress(NetworkConfig.TARGETIPADDRESS);
            TCPContext.setTargetPort(NetworkConfig.TARGETPORT);
            TCPContext.pushMessageCtx(ConnectionCtx.operations.CREATE_CONNECTION, null);
            }
        });

        disconnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            TCPContext.pushMessageCtx(ConnectionCtx.operations.CLOSE_CONNECTION, null);
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
            TCPContext.pushMessageCtx(ConnectionCtx.operations.RECEIVE_MESSAGE, null);
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
            UDPContext.setTargetIpAddress(NetworkConfig.TARGETIPADDRESS);
            UDPContext.setTargetPort(NetworkConfig.TARGETPORT);
            UDPContext.setLocatPort(NetworkConfig.LOCALPORT);
            UDPContext.pushMessageCtx(ConnectionCtx.operations.CREATE_CONNECTION, null);
            }
        });

        disconnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            UDPContext.pushMessageCtx(ConnectionCtx.operations.CLOSE_CONNECTION, null);
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
            UDPContext.pushMessageCtx(ConnectionCtx.operations.RECEIVE_MESSAGE, null);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
//        debugWiFiTCPConnection();
//        debugWiFiUDPConnection();

//        debugGyroscope();
//        checkWiFiWotking();
        debugVideoRecorder();
    }

    private void checkWiFiWotking() {
        WiFi wiFi = new WiFi();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(wiFi, intentFilter);
    }
}
