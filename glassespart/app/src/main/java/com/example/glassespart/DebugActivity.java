package com.example.glassespart;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.glassespart.network.ConnectionCtx;
import com.example.glassespart.network.WiFi;
import com.example.glassespart.network.WifiTCPSocket;
import com.example.glassespart.network.WifiUDPSocket;

public class DebugActivity extends AppCompatActivity {
    private ConnectionCtx TCPContext;
    private ConnectionCtx UDPContext;

    private String ipAddr = "192.168.43.154";
    private int port = 3333;

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
            TCPContext.setIpAddress(ipAddr);
            TCPContext.setPortNumber(port);
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
            UDPContext.setIpAddress(ipAddr);
            UDPContext.setPortNumber(port);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        debugWiFiTCPConnection();
        debugWiFiUDPConnection();

//        checkGyroscopeWorking();
//        checkWiFiWotking();
    }

    private void checkGyroscopeWorking() {
        Gyroscope gyroscope = new Gyroscope(this);
        gyroscope.startGyroscope();
    }

    private void checkWiFiWotking() {
        WiFi wiFi = new WiFi();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(wiFi, intentFilter);

    }
}
