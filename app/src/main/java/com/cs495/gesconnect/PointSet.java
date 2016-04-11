package com.cs495.gesconnect;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by spencer on 4/6/16.
 */
public class PointSet implements Serializable {
    public static final String TAG = "PointSet";
    public static final float MATCH_VAL = 75.0f;

    private List<Point> points;

    public PointSet() {
        points = new ArrayList<Point>();
    }

    public void clear() {
        points.clear();
    }

    public void appendPoint(Point p) {
        points.add(p);
    }

    public void setPoints(List<Point> ps) {
        points = ps;
    }

    public List<Point> getPoints() {
        return points;
    }

    public String toString() { return points.toString(); }

    public int getSize() { return points.size(); }

    @Override
    public boolean equals(Object other) {

        if (! (other instanceof PointSet)) {
            return false;
        }

        Log.d(TAG, "ORIGINAL:" + points.size() + points.toString());
        Log.d(TAG, "ORIGINAL (other):" + points.size() + ((PointSet) other).toString());

        List<Point> myPoints = normalize(points);
        List<Point> otherPoints = normalize(((PointSet) other).getPoints());

        Log.d(TAG, "NORMALIZED: " + myPoints.size() + myPoints.toString());

        myPoints = scale(myPoints);
        otherPoints = scale(otherPoints);

        Log.d(TAG, "SCALED: " + myPoints.size() + myPoints.toString());

        float diff = 0.0f;
        int magnitude = myPoints.size() + otherPoints.size();

        for (Point p : myPoints) {

            float dist = min(computeDistances(p, otherPoints));
            diff += dist / magnitude;
        }

        for (Point p : otherPoints) {

            float dist = min(computeDistances(p, myPoints));
            diff += dist / magnitude;
        }

        Log.d(TAG, "DIFF: " + Float.toString(diff));

        return (diff < MATCH_VAL);
    }

    private float min(List<Float> dists) {
        float min = 5000.0f;

        for (float f : dists) {
            if (f < min) {
                min = f;
            }
        }

        return min;
    }

    private List<Float> computeDistances(Point p, List<Point> ps) {
        List<Float> dists = new ArrayList<>();
        for (Point t : ps) {
            float dist = (float) Math.sqrt(Math.pow(p.getX() - t.getX(), 2) + Math.pow(p.getY() - t.getY(), 2));
            dists.add(dist);
        }
        return dists;
    }

    private List<Point> scale(List<Point> ps) {
        List<Point> scaled = new ArrayList<>();
        float maxX = -1000;
        float maxY = -1000;

        for (Point p: ps) {
            if (p.getX() > maxX) {
                maxX = p.getX();
            }

            if (p.getY() > maxY) {
                maxY = p.getY();
            }
        }

        float factorX = (maxX != 0) ? 1000 / maxX : 1;
        float factorY = (maxY != 0) ? 1000 / maxY : 1;

        for (Point p : ps) {
            Point t = new Point();
            t.setX(factorX * p.getX());
            t.setY(factorY * p.getY());
            scaled.add(t);
        }

        return scaled;
    }

    private List<Point> normalize(List<Point> ps) {
        List<Point> normalized = new ArrayList<>();

        Point min = getRelOrigin(ps);

        for (Point p : ps) {
            Point t = new Point();
            t.setX(p.getX() - min.getX());
            t.setY(p.getY() - min.getY());
            normalized.add(t);
        }

        return normalized;
    }

    private Point getRelOrigin(List<Point> ps) {
        float curY = 5000.0f;
        List<Integer> idxsY = new ArrayList<>();

        for (int i = 0; i < ps.size(); i++) {
            float val = ps.get(i).getY();

            if (val < curY) {
                curY = val;
                idxsY.clear();
                idxsY.add(i);
            } else if (val == curY) {
                idxsY.add(i);
            }
        }

        float curX = 5000.0f;
        Point min = new Point();

        for (int i = 0; i < idxsY.size(); i++) {
            int idx = idxsY.get(i);
            float val = ps.get(idx).getX();

            if (val < curX) {
                min = ps.get(idx);
            }

        }

        return min;
    }
}
