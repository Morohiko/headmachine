package com.example.glassespart.opencv;

import android.graphics.Bitmap;

public class FramesQueue {
    private boolean isBusy = false;
    private Bitmap currentFrame = null;

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

    int setBitmapFrame(Bitmap fr) {
        if (isBusy) {
            return -1;
        }
        isBusy = true;
        currentFrame = fr;
        isBusy = false;
        return 0;
    }
}
