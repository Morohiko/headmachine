package com.example.glassespart;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.glassespart.network.ConnectionCtx;
import com.example.glassespart.network.WiFi;
import com.example.glassespart.network.WifiTCPSocket;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Gyroscope gyroscope;

    ConnectionCtx message;
    private void configureWindowResources() {
        Button startBtn = findViewById(R.id.startBtn);
        Button dbgBtn = findViewById(R.id.dbgBtn);
        Button connectBtn = findViewById(R.id.connectBtn);
        Button disconnectBtn = findViewById(R.id.disconnectBtn);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = new ConnectionCtx();
                AsyncTask aa = new WifiTCPSocket().execute(message);
            }
        });

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG","connect Button is clicked");
                String ipAddr = "192.168.43.151";
                int port = 3333;
                message.setIpAddress(ipAddr);
                message.setPortNumber(port);
                message.pushMessageCtx(ConnectionCtx.operations.CREATE_CONNECTION, null);
            }
        });

        disconnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message.pushMessageCtx(ConnectionCtx.operations.CLOSE_CONNECTION, null);
            }
        });

        dbgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG","debug Button is clicked");
                message.pushMessageCtx(ConnectionCtx.operations.SEND_MESSAGE, "some msg");
                //wifiSocket.sendMessage();

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureWindowResources();

//        checkGyroscopeWorking();
        checkWiFiWotking();
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
