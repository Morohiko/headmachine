package com.example.glassespart.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WiFi extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d("DEBUG","action = " + action);

        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork == null) {
            Log.d("ERROR", "active network is null");
            return;
        }

        boolean isConnected = activeNetwork.isConnectedOrConnecting();
        Log.d("DEBUG","isConnected: " + isConnected);
        if (!isConnected) {
            return;
        }
        boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
        Log.d("DEBUG","isWiFi: " + isWiFi);
        if (!isWiFi) {
            return;
        }

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        Log.d("DEBUG","connectionInfo.getSSID = " + connectionInfo.getSSID());
    }
}
