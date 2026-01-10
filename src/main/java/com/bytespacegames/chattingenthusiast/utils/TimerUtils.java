package com.bytespacegames.chattingenthusiast.utils;

public class TimerUtils {
    private long lastMilli = 0L;

    public boolean hasTimeElapsed(long time, boolean reset) {
        if (System.currentTimeMillis() - this.lastMilli >= time) {
            if (reset) {
                this.reset();
            }

            return true;
        } else {
            return false;
        }
    }

    public void reset() {
        this.lastMilli = System.currentTimeMillis();
    }
}