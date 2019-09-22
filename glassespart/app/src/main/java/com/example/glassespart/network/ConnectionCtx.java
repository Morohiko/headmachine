package com.example.glassespart.network;

import java.util.LinkedList;

public class ConnectionCtx {
    public ConnectionCtx() {}

    private String localIpAddress;
    private int locatPort;
    private String targetIpAddress;
    private int targetPort;


    private LinkedList<MessageCtx> messages = new LinkedList<>();

    int getTargetPort() {
        return targetPort;
    }

    public void setTargetPort(int targetPort) {
        this.targetPort = targetPort;
    }

    String getTargetIpAddress() {
        return targetIpAddress;
    }

    public void setTargetIpAddress(String targetIpAddress) {
        this.targetIpAddress = targetIpAddress;
    }

    int getLocatPort() {
        return locatPort;
    }

    public void setLocatPort(int locatPort) {
        this.locatPort = locatPort;
    }

    public String getLocalIpAddress() {
        return localIpAddress;
    }

    public void setLocalIpAddress(String localIpAddress) {
        this.localIpAddress = localIpAddress;
    }

    public enum operations{
        CREATE_CONNECTION,
        CLOSE_CONNECTION,
        SEND_MESSAGE,
        RECEIVE_MESSAGE
    }

    public void pushMessageCtx(operations op, String messageCtx) {
        messages.addFirst(new MessageCtx(op, messageCtx));
    }

    public void pushMessageCtx(operations op, byte[] messageCtx) {
        messages.addFirst(new MessageCtx(op, messageCtx));
    }

    public void pushMessageCtx(operations op) {
        messages.addFirst(new MessageCtx(op));
    }

    MessageCtx pullMessageCtx() {
        if (messages.size() < 1) {
            return null;
        }

        MessageCtx msg = messages.getFirst();
        messages.removeFirst();

        return msg;
    }
}

class MessageCtx {
    String message;
    byte[] messageByte;
    ConnectionCtx.operations operation;

    MessageCtx(ConnectionCtx.operations op, String messageCtx) {
        this.message = messageCtx;
        this.operation = op;
    }
    MessageCtx(ConnectionCtx.operations op, byte[] messageCtx) {
        this.messageByte = messageCtx;
        this.operation = op;
    }

    MessageCtx(ConnectionCtx.operations op) {
        this.operation = op;
    }
}
