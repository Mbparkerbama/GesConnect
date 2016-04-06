package com.cs495.gesconnect;

import java.util.List;
import com.cs495.gesconnect.Point;

/**
 * Created by spencer on 4/6/16.
 */
public class PointSet {
    private List<Point> points;

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

    // TODO: write compare method
}
