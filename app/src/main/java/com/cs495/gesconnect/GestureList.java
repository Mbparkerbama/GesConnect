package com.cs495.gesconnect;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    public String save() {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ObjectOutputStream output = new ObjectOutputStream(stream);
            output.writeObject(gestures);
            output.close();
            return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
        }
        catch (Exception e) {

        }

        return "";
    }

    public void load(String data) {
        if (data.equals("")) {
            return;
        }

        try {
            byte[] bytes = Base64.decode(data, Base64.DEFAULT);
            InputStream stream = new ByteArrayInputStream(
                    bytes);
            ObjectInput input = new ObjectInputStream(stream);
            gestures = (HashMap<ContactTarget, Gesture>) input.readObject();
        }
        catch (Exception e) {

        }
    }

    //public HashMap<ContactTarget, Gesture> getGestures(){
    //}
}
