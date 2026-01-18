package com.bytespacegames.chattingenthusiast.gui;

public class Interpolator {
    public static int interpolateColor(int from, int to, float progress) {
        progress = Math.max(0,Math.min(1,progress));
        int fromA = from >>> 24 & 0x000000FF;
        int toA = to >>> 24 & 0x000000FF;
        int fromR = from >>> 16 & 0x000000FF;
        int toR = to >>> 16 & 0x000000FF;
        int fromG = from >>> 8 & 0x000000FF;
        int toG = to >>> 8 & 0x000000FF;
        int fromB = from & 0x000000FF;
        int toB = to & 0x000000FF;

        int finalA = (int) (fromA + (toA-fromA) * progress + 0.5f);
        int finalR = (int) (fromR + (toR-fromR) * progress + 0.5f);
        int finalG = (int) (fromG + (toG-fromG) * progress + 0.5f);
        int finalB = (int) (fromB + (toB-fromB) * progress + 0.5f);

        return finalA << 24 | finalR << 16 | finalG << 8 | finalB;
    }
}
