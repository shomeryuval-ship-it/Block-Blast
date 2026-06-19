package com.example.block_blast;

import android.os.Handler;
import android.os.Message;

public class GameTimer extends Thread {
    private int counter = 60;
    private Handler handler;
    private boolean pause = false;
    private boolean isRunning = true;

    public GameTimer(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        super.run();

        while (counter >= 0 && isRunning) {
            if (!pause) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Message msg = new Message();
                msg.arg1 = counter;
                handler.sendMessage(msg);

                counter--;
            }
        }
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public void stopTimer() {
        this.isRunning = false;
    }
}
