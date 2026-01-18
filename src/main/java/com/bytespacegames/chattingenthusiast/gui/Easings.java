package com.bytespacegames.chattingenthusiast.gui;

import it.unimi.dsi.fastutil.floats.FloatUnaryOperator;

public class Easings {
    public static final FloatUnaryOperator LINEAR = t -> t;

    public static final FloatUnaryOperator EASE_IN = t -> t * t;

    public static final FloatUnaryOperator EASE_OUT =
            t -> 1 - (1 - t) * (1 - t);

    public static final FloatUnaryOperator EASE_IN_OUT =
            t -> t < 0.5f
                    ? 2 * t * t
                    : 1 - (float)Math.pow(-2 * t + 2, 2) / 2;
    public static final FloatUnaryOperator QUINT = t ->
            (float) (1 - Math.pow(1 - t, 5));
}
