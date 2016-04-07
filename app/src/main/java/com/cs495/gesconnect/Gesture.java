package com.cs495.gesconnect;

public class Gesture {
    Gesture(PointSet points) {
        this.points = points;
    }

    private PointSet points;

    public PointSet getPoints(){return points;}

    public void setPoints(PointSet points) {this.points = points;}

    //public MatchValue compare(Gesture gesture){}

    public String save(){return "";}

    public void load(String data){}

}
