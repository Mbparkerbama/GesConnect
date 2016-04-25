package com.cs495.gesconnect;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by spencer on 4/6/16.
 */
public class PointSet implements Serializable {
    public static final String TAG = "PointSet";
    public static final float MATCH_VAL = 100.0f;

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

    /**
     * Draw this set of points, scaled to maximally fit the area of the
     * passed Canvas.
     * @param canvas Canvas to draw on.
     */
    public void drawScaled(Canvas canvas) {
        // Scale the coordinates to an area 3/4ths the size of the actual
        // Canvas (otherwise, the gesture may overlap the UI)
        int w = (int)((float)(canvas.getWidth()) * canvasScaleFactor);
        int h = (int)((float)(canvas.getHeight()) * canvasScaleFactor);

        List<Point> scaledPoints
                = scaleToFit(points, w, h);
        // Center the points around the "margins" created
        // by the previous step
        int offsetX = (canvas.getWidth() - w) / 2;
        int offsetY = (canvas.getHeight() - h) / 2;

        // Draw points
        Paint paint = new Paint();
        paint.setARGB(0xFF, 0, 0x88, 0xFF);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float)8.0);
        for (int i = 0; i < scaledPoints.size() - 1; i++) {
//            Log.d(TAG, "point: " + Float.toString(
//                    scaledPoints.get(i).getX())
//                    + ", "
//                    + Float.toString(
//                            scaledPoints.get(i).getY()));
            Point point1 = scaledPoints.get(i);
            Point point2 = scaledPoints.get(i + 1);

            float startX = ((point1.getX())) + offsetX;
            float startY = ((point1.getY())) + offsetY;
            float endX = ((point2.getX())) + offsetX;
            float endY = ((point2.getY())) + offsetY;
//            Log.d(TAG, "start: " + Float.toString(
//                    startX)
//                    + ", "
//                    + Float.toString(
//                            startY));
//            Log.d(TAG, "end: " + Float.toString(
//                    endX)
//                    + ", "
//                    + Float.toString(
//                    endY));

            canvas.drawLine(
                    startX,
                    startY,
                    endX,
                    endY,
                    paint);
        }
//        Log.d(TAG, "w: " + Integer.toString(w));
//        Log.d(TAG, "h: " + Integer.toString(h));
    }

    private float min(List<Float> dists) {
        if (dists.size() <= 0) {
            return 0.0f;
        }

        float min = dists.get(0);

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

    // Scale the points in ps to fit the specified dimensions.
    // Result is in Cartesian coordinates and bounded by the specified
    // width and height, i.e. the x-range is (-(w/2), (w/2)).
    private List<Point> scaleToFit(List<Point> ps, int w, int h) {
        List<Point> scaled = new ArrayList<>();
        if (ps.size() <= 0) {
            return scaled;
        }

        // Find the farthest x/y extent of the set
        float maxX = ps.get(0).getX();
        float maxY = ps.get(0).getY();
        for (Point p: ps) {
            if (p.getX() > maxX) {
                maxX = p.getX();
            }

            if (p.getY() > maxY) {
                maxY = p.getY();
            }
        }

        // Compute the amount by which to scale each point
        float factorX = maxX != 0 ? ((float)w / maxX) : 1;
        float factorY = maxY != 0 ? ((float)h / maxY) : 1;

        // Scale the points
        for (Point p : ps) {
            Point t = new Point();
            t.setX(factorX * p.getX());
            t.setY(factorY * p.getY());
            scaled.add(t);
        }

        return scaled;
    }

    // Scale the points in ps to a standard reference size
    private List<Point> scale(List<Point> ps) {
        return scaleToFit(ps, standardScaleFactor, standardScaleFactor);
    }

    // Shift the points in ps so that the origin is at the (approximate)
    // bottom-left point
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

    // Find the (approximate) bottom-left point in ps
    private Point getRelOrigin(List<Point> ps) {
        if (ps.size() <= 0) {
            Point result = new Point();
            result.setX(0.0f);
            result.setY(0.0f);
            return result;
        }

        float curY = ps.get(0).getY();
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

        float curX = ps.get(0).getX();
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

    private static final int standardScaleFactor = 1000;

    private static final float canvasScaleFactor
            = 3.0f / 4.0f;
}
