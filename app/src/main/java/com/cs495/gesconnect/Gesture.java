package com.cs495.gesconnect;

import java.io.Serializable;

public class Gesture implements Serializable {
    Gesture(PointSet points) {
        this.points = points;
    }

    private PointSet points;

    public PointSet getPoints(){return points;}

    public void setPoints(PointSet points) {this.points = points;}

    //public MatchValue compare(Gesture gesture){}

    public String save(){return "";}

    public void load(String data){}

    public boolean equals(Object other) {
        if (other instanceof Gesture) {
            return points.equals(((Gesture) other).getPoints());
        }

        return other instanceof PointSet && points.equals((PointSet) other);
    }

}
