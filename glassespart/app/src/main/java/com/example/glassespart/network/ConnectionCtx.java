package com.example.glassespart.network;

import java.util.LinkedList;

public class ConnectionCtx {
    public ConnectionCtx(){

    }
    public ConnectionCtx(String ipAddress, int portNumber) {
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
    }

    private String ipAddress;
    private int portNumber;


    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }


    private LinkedList<MessageCtx> messages = new LinkedList<>();

    public enum operations{
        CREATE_CONNECTION,
        CLOSE_CONNECTION,
        SEND_MESSAGE,
        RECEIVE_MESSAGE
    }

    public void pushMessageCtx(operations op, String messageCtx) {
        messages.addFirst(new MessageCtx(op, messageCtx));
    }

    public MessageCtx pullMessageCtx() {
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
    ConnectionCtx.operations operation;

    MessageCtx(ConnectionCtx.operations op, String messageCtx) {
        this.message = messageCtx;
        this.operation = op;
    }

}
