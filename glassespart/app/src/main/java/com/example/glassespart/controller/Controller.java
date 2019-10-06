package com.example.glassespart.controller;

import android.os.AsyncTask;
import android.util.Log;

import com.example.glassespart.config.NetworkConfig;
import com.example.glassespart.config.TotalConfig;
import com.example.glassespart.network.TCPNetwork;

import java.util.PriorityQueue;

import static java.lang.Thread.sleep;

public class Controller {
    private int cameraState = 0;
    private int cameraTransmitterState = 0;
    private int gyroscopeReceoverState = 0;
    private int motorState = 0;

    private PriorityQueue<ControllerCtx> controllerCtxQueue = new PriorityQueue<>(10);

    private String createMessage(int module, int state) {
        return module + ":" + state;
    }

    public void setCameraState(int cameraState) {
        if (cameraState == this.cameraState) return;
        this.cameraState = cameraState;
        controllerCtxQueue.add(new ControllerCtx(createMessage(TotalConfig.CONTROLLER_CAMERA, cameraState)));
    }

    public void setCameraTransmitterState(int cameraTransmitterState) {
        if (cameraTransmitterState == this.cameraTransmitterState) return;
        this.cameraTransmitterState = cameraTransmitterState;
        controllerCtxQueue.add(new ControllerCtx(createMessage(TotalConfig.CONTROLLER_CAMERA_TRANSMITTER, cameraTransmitterState)));
    }

    public void setGyroscopeReceoverState(int gyroscopeReceoverState) {
        if (gyroscopeReceoverState == this.gyroscopeReceoverState) return;
        this.gyroscopeReceoverState = gyroscopeReceoverState;
        controllerCtxQueue.add(new ControllerCtx(createMessage(TotalConfig.CONTROLLER_GYROSCOPE_RECEIVER, gyroscopeReceoverState)));
    }

    public void setMotorState(int motorState) {
        if (motorState == this.motorState) return;
        this.motorState = motorState;
        controllerCtxQueue.add(new ControllerCtx(createMessage(TotalConfig.CONTROLLER_MOTOR, motorState)));
    }

    public void startController() {
        AsyncTask controllerTCPInternal = new ControllerTCPInternal().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                controllerCtxQueue);
    }
}

class ControllerTCPInternal extends AsyncTask<PriorityQueue<ControllerCtx>, Void, Void> {
    private boolean isFinish = false;

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    @Override
    protected Void doInBackground(PriorityQueue<ControllerCtx>... controllerCtx) {
        TCPNetwork tcpSocket = new TCPNetwork();
        tcpSocket.createConnection(NetworkConfig.IP_TARGET_ADDRESS, NetworkConfig.CONTROLLER_TARGET_PORT);
        Log.d("DEBUG", "Controller socket was created");

        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (!isFinish) {
            if (controllerCtx[0].isEmpty()) continue;
            ControllerCtx ctx = controllerCtx[0].remove();
            if (ctx.getMessage().length()> 0) {
                Log.d("DEBUG", "send controller message: " + ctx.getMessage());
                tcpSocket.sendMessage(ctx.getMessage());
            }
        }
        return null;
    }
}

class ControllerCtx {
    private String message = "";

    ControllerCtx(String message) {
        this.message = message;
    }

    String getMessage() {
        return message;
    }
}
