package com.example.glassespart;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.glassespart.config.NetworkConfig;

import org.w3c.dom.Text;

public class ConfigActivity extends AppCompatActivity {
    private TextView localIp;
    private TextView localPort;
    private TextView targetIp;
    private TextView targetPort;

    @SuppressLint("SetTextI18n")
    private void showConfig(){
        localIp.setText(NetworkConfig.LOCALIPADDRESS);
        localPort.setText(Integer.toString(NetworkConfig.LOCALPORT));
        targetIp.setText(NetworkConfig.TARGETIPADDRESS);
        targetPort.setText(Integer.toString(NetworkConfig.TARGETPORT));
    }

    private void initResources(){
        localIp = findViewById(R.id.configLocalIPText);
        localPort = findViewById(R.id.configLocalPortText);
        targetIp = findViewById(R.id.configTargetIPText);
        targetPort = findViewById(R.id.configTargetPortText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        Button confirmButton = findViewById(R.id.configConfirmBtn);
        Button toDefaultButton = findViewById(R.id.configToDefaultBtn);

        initResources();
        showConfig();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkConfig.LOCALIPADDRESS = localIp.getText().toString();
                NetworkConfig.LOCALPORT = Integer.parseInt(localPort.getText().toString());
                NetworkConfig.TARGETIPADDRESS = targetIp.getText().toString();
                NetworkConfig.TARGETPORT = Integer.parseInt(targetPort.getText().toString());

                showConfig();
            }
        });

        toDefaultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkConfig.toDefaultConfig();
                showConfig();
            }
        });
    }
}
