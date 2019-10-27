package com.example.glassespart.controller;

import android.os.AsyncTask;
import android.util.Log;

import com.example.glassespart.config.NetworkConfig;
import com.example.glassespart.config.TotalConfig;
import com.example.glassespart.network.TCPNetwork;

import java.util.PriorityQueue;

import static android.os.AsyncTask.Status.RUNNING;

public class Controller {
    private AsyncTask controllerTCPInternal;
    private int cameraState = 0;
    private int cameraTransmitterState = 0;
    private int gyroscopeReceoverState = 0;
    private int motorState = 0;

    private PriorityQueue<ControllerCtx> controllerCtxQueue = new PriorityQueue<>(10);

    private String createMessage(int module, int state) {
        return module + ":" + state;
    }

    private boolean isConnectionExist() {
        return controllerTCPInternal.getStatus() == RUNNING;
    }

    // return false if connecion is not exit(bad behavior/error), true if everything good
    public boolean setCameraState(int cameraState) {
        if (!isConnectionExist()) return false;
        if (cameraState == this.cameraState) return true;
        this.cameraState = cameraState;
        controllerCtxQueue.add(new ControllerCtx(createMessage(TotalConfig.CONTROLLER_CAMERA, cameraState)));

        return true;
    }

    // return false if connecion is not exit(bad behavior/error), true if everything good
    public boolean setCameraTransmitterState(int cameraTransmitterState) {
        if (!isConnectionExist()) return false;
        if (cameraTransmitterState == this.cameraTransmitterState) return true;
        this.cameraTransmitterState = cameraTransmitterState;
        try {
            controllerCtxQueue.add(new ControllerCtx(createMessage(TotalConfig.CONTROLLER_CAMERA_TRANSMITTER, cameraTransmitterState)));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    // return false if connecion is not exit(bad behavior/error), true if everything good
    public boolean setGyroscopeReceoverState(int gyroscopeReceoverState) {
        if (!isConnectionExist()) return false;
        if (gyroscopeReceoverState == this.gyroscopeReceoverState) return true;
        this.gyroscopeReceoverState = gyroscopeReceoverState;
        try {
            controllerCtxQueue.add(new ControllerCtx(createMessage(TotalConfig.CONTROLLER_GYROSCOPE_RECEIVER, gyroscopeReceoverState)));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    // return false if connecion is not exit(bad behavior/error), true if everything good
    public boolean setMotorState(int motorState) {
        if (!isConnectionExist()) return false;
        if (motorState == this.motorState) return true;
        this.motorState = motorState;
        try {
            controllerCtxQueue.add(new ControllerCtx(createMessage(TotalConfig.CONTROLLER_MOTOR, motorState)));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean startController() {
        controllerTCPInternal = new ControllerTCPInternal().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                controllerCtxQueue);
        return true;
    }
}

class ControllerTCPInternal extends AsyncTask<PriorityQueue<ControllerCtx>, Void, Void> {
    private boolean isFinish = false;

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    @SafeVarargs
    @Override
    protected final Void doInBackground(PriorityQueue<ControllerCtx>... controllerCtx) {
        TCPNetwork tcpSocket = new TCPNetwork();

        int retry = 5;
        while (retry-- > 0 && !tcpSocket.isConnected()) {
            tcpSocket.createConnection(NetworkConfig.IP_TARGET_ADDRESS, NetworkConfig.CONTROLLER_TARGET_PORT);
        }

        if (tcpSocket == null || !tcpSocket.isConnected()) {
            Log.d("ERROR", "Cannot create connection wit " + NetworkConfig.IP_TARGET_ADDRESS +
                    ":" + NetworkConfig.CONTROLLER_TARGET_PORT + ", retryes: " + retry);
            return null;
        }

        while (!isFinish) {
            if (controllerCtx[0].isEmpty()) continue;
            ControllerCtx ctx;
            try {
                ctx = controllerCtx[0].remove();
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            if (!tcpSocket.isConnected()) {
                Log.d("ERROR", "Cannot create connection wit " + NetworkConfig.IP_TARGET_ADDRESS +
                        ":" + NetworkConfig.CONTROLLER_TARGET_PORT);
                break;
            }
            Log.d("DEBUG", "Controller socket was created");

            if (ctx.getMessage().length() > 0) {
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
