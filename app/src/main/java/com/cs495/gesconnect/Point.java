package com.cs495.gesconnect;

public class Point {
    private float x;
    private float y;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String toString() {
        return "{ x: " + Float.toString(x) + ", y: " + Float.toString(y) + " }";
    }
}
