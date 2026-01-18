package com.bytespacegames.chattingenthusiast.gui;

import it.unimi.dsi.fastutil.floats.FloatUnaryOperator;

public class Animator {
    private float targetValue;
    private float animationTime;
    private float interpolateFromValue;
    private long interpolateStartTime;
    private FloatUnaryOperator easingFunction = t -> t;
    public Animator(float value) {
        this.targetValue = value;
        this.interpolateFromValue = value;
        this.interpolateStartTime = 0;
        this.animationTime = 0;
    }
    public void setTarget(float target) {
        this.interpolateFromValue = getValue();
        this.targetValue = target;
        this.interpolateStartTime = System.currentTimeMillis();
    }
    private float easing(float value) {
        return easingFunction.apply(value);
    }
    public float getValue() {
        if (animationTime <= 0) return targetValue;
        float animationProgress = Math.min(1,(System.currentTimeMillis() - interpolateStartTime) / 1000f);
        return interpolateFromValue + (targetValue - interpolateFromValue) * (easing(animationProgress / animationTime));
    }
    public void setAnimationTime(float animationTime) {
        this.animationTime = animationTime;
    }
    public float getAnimationTime() {
        return animationTime;
    }
    public void setEasingFunction(FloatUnaryOperator easing) {
        this.easingFunction = easing;
    }
}
