package com.cs495.gesconnect;

import java.util.HashMap;


public class GestureList {
    private HashMap<ContactTarget, Gesture> gestures;

    public HashMap<ContactTarget, Gesture> getGestures() {
        return gestures;
    }

    public void setGestures(HashMap<ContactTarget, Gesture> gestures) {
        this.gestures = gestures;
    }

    public void removeGesture(ContactTarget contactID){

    }

    public String save(){ return "";}

    public void load(String data){

    }

    //public HashMap<ContactTarget, Gesture> getGestures(){
    //}
}
