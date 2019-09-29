package com.example.glassespart.opencv;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.glassespart.config.TotalConfig;

import java.util.Arrays;

class FramesQueue {
    private boolean isBusy = false;
    private Bitmap currentFrame = null;
//    int MTU_MESSAGE_SIZE = 131072;
    private byte[] bytesFrame = new byte[TotalConfig.IMAGE_SIZE];

    private FramesQueue(){}

    // singleton
    private static FramesQueue framesQueue = null;
    static FramesQueue getInstance() {
        if (FramesQueue.framesQueue == null) {
            framesQueue = new FramesQueue();
        }
        return framesQueue;
    }

    Bitmap getBitmapFrame(){
        if (isBusy) {
            return null;
        }

        isBusy = true;
        Bitmap buff = currentFrame;
        isBusy = false;
        return buff;
    }

    Bitmap getBitmapFrameFromBytes(){
        if (isBusy) {
            return null;
        }

        isBusy = true;
        Bitmap buff = BitmapFactory.decodeByteArray(bytesFrame, 0, bytesFrame.length);
        if (buff == null) {
            Log.d("ERROR", "getted bitmap from byte is null");
        }
        isBusy = false;
        return buff;
    }

    int setBitmapFrame(Bitmap fr) {
        if (isBusy) {
            return -1;
        }
        isBusy = true;
        currentFrame = fr;
        isBusy = false;
        return 0;
    }

    void pushMessageChumk(byte[] message) {
        Log.d("DEBUG", "push message chunk, size = " + message.length);

        if (message.length < 1) {
            Log.d("DEBUG", "message is empy, return");
            return;
        }
        try {
            System.arraycopy(message, 0, bytesFrame, 0, TotalConfig.IMAGE_SIZE);
        } catch (Exception e) {
            Log.d("ERROR", Arrays.toString(e.getStackTrace()));
        }
    }
}
