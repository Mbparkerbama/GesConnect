package com.cs495.gesconnect;

import java.util.ArrayList;
import java.util.List;
import com.cs495.gesconnect.Point;

/**
 * Created by spencer on 4/6/16.
 */
public class PointSet {
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

    // TODO: write compare method
}
