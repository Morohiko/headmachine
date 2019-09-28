package com.example.glassespart;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.glassespart.config.NetworkConfig;

public class ConfigActivity extends AppCompatActivity {
    private TextView ipLocalAddressView;
    private TextView ipTargetAddressView;
    private TextView gyroscopeLocalPortView;
    private TextView gyroscopeTargetPortView;
    private TextView cameraLocalPortView;
    private TextView cameraTargetPortView;
    private TextView testLocalPortView;
    private TextView testTargetPortView;

    @SuppressLint("SetTextI18n")
    private void showConfig(){
        ipLocalAddressView.setText(NetworkConfig.IP_LOCAL_ADDRESS);
        ipTargetAddressView.setText(NetworkConfig.IP_TARGET_ADDRESS);
        gyroscopeLocalPortView.setText(Integer.toString(NetworkConfig.GYROSCOPE_LOCAL_PORT));
        gyroscopeTargetPortView.setText(Integer.toString(NetworkConfig.GYROSCOPE_TARGET_PORT));
        cameraLocalPortView.setText(Integer.toString(NetworkConfig.CAMERA_FRAME_LOCAL_PORT));
        cameraTargetPortView.setText(Integer.toString(NetworkConfig.CAMERA_FRAME_TARGET_PORT));
        testLocalPortView.setText(Integer.toString(NetworkConfig.TEST_LOCAL_PORT));
        testTargetPortView.setText(Integer.toString(NetworkConfig.TEST_TARGET_PORT));
    }

    private void initResources(){
        ipLocalAddressView = findViewById(R.id.configLocalIPText);
        ipTargetAddressView = findViewById(R.id.configTargetIPText);
        gyroscopeLocalPortView = findViewById(R.id.configGyroscopeLocalPortText);
        gyroscopeTargetPortView = findViewById(R.id.configGyroscopeTargetPortText);
        cameraLocalPortView = findViewById(R.id.configCameraLocalPortText);
        cameraTargetPortView = findViewById(R.id.configCameraTargetPortText);
        testLocalPortView = findViewById(R.id.configTestLocalPortText);
        testTargetPortView = findViewById(R.id.configTestTargetPortText);
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
                NetworkConfig.IP_LOCAL_ADDRESS = ipLocalAddressView.getText().toString();
                NetworkConfig.IP_TARGET_ADDRESS = ipTargetAddressView.getText().toString();
                
                NetworkConfig.GYROSCOPE_LOCAL_PORT = Integer.parseInt(gyroscopeLocalPortView.getText().toString());
                NetworkConfig.GYROSCOPE_TARGET_PORT = Integer.parseInt(gyroscopeTargetPortView.getText().toString());
                NetworkConfig.CAMERA_FRAME_LOCAL_PORT = Integer.parseInt(cameraLocalPortView.getText().toString());
                NetworkConfig.CAMERA_FRAME_TARGET_PORT = Integer.parseInt(cameraTargetPortView.getText().toString());
                NetworkConfig.TEST_LOCAL_PORT = Integer.parseInt(testLocalPortView.getText().toString());
                NetworkConfig.TEST_TARGET_PORT = Integer.parseInt(testTargetPortView.getText().toString());
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
