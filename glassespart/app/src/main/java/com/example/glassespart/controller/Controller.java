package com.example.glassespart.controller;

import android.os.AsyncTask;
import android.util.Log;

import com.example.glassespart.config.NetworkConfig;
import com.example.glassespart.config.TotalConfig;
import com.example.glassespart.network.TCPNetwork;

import java.util.PriorityQueue;

import static java.lang.Thread.sleep;

public class Controller {
    boolean connetioncExist = false;
    TCPNetwork tcpSocket;

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
        controllerCtxQueue.add(new ControllerCtx(createMessage(TotalConfig.CONTROLLER_CAMERA, cameraState), tcpSocket));
    }

    public void setCameraTransmitterState(int cameraTransmitterState) {
        if (cameraTransmitterState == this.cameraTransmitterState) return;
        this.cameraTransmitterState = cameraTransmitterState;
        controllerCtxQueue.add(new ControllerCtx(createMessage(TotalConfig.CONTROLLER_CAMERA_TRANSMITTER, cameraTransmitterState), tcpSocket));
    }

    public void setGyroscopeReceoverState(int gyroscopeReceoverState) {
        if (gyroscopeReceoverState == this.gyroscopeReceoverState) return;
        this.gyroscopeReceoverState = gyroscopeReceoverState;
        controllerCtxQueue.add(new ControllerCtx(createMessage(TotalConfig.CONTROLLER_GYROSCOPE_RECEIVER, gyroscopeReceoverState), tcpSocket));
    }

    public void setMotorState(int motorState) {
        if (motorState == this.motorState) return;
        this.motorState = motorState;
        controllerCtxQueue.add(new ControllerCtx(createMessage(TotalConfig.CONTROLLER_MOTOR, motorState), tcpSocket));
    }

    public boolean startController() {
        tcpSocket = new TCPNetwork();
        int retval = tcpSocket.createConnection(NetworkConfig.IP_TARGET_ADDRESS, NetworkConfig.CONTROLLER_TARGET_PORT);
        if (retval == -1) {
            Log.d("ERROR", "Cannot create connection");

            return  false;
        }
        Log.d("DEBUG", "Controller socket was created");

        AsyncTask controllerTCPInternal = new ControllerTCPInternal().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                controllerCtxQueue);

        return connetioncExist;
    }
}

class ControllerTCPInternal extends AsyncTask<PriorityQueue<ControllerCtx>, Void, Void> {
//class ControllerTCPInternal {
    private boolean isFinish = false;

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    @Override
    protected Void doInBackground(PriorityQueue<ControllerCtx>... controllerCtx) {
        while (!isFinish) {
            if (controllerCtx[0].isEmpty()) continue;
            ControllerCtx ctx = controllerCtx[0].remove();

            if (ctx.getMessage().length()> 0) {
                Log.d("DEBUG", "send controller message: " + ctx.getMessage());
                ctx.tcpSocket.sendMessage(ctx.getMessage());
            }
        }
        return null;
    }
}

class ControllerCtx {
    TCPNetwork tcpSocket;

    private String message = "";

    ControllerCtx(String message, TCPNetwork tcpSock) {
        this.message = message;
        this.tcpSocket = tcpSock;
    }

    String getMessage() {
        return message;
    }
}
