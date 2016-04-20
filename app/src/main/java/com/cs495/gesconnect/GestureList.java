package com.cs495.gesconnect;

import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;


public class GestureList {
    private static final String TAG = "GestureList";
    private HashMap<ContactTarget, Gesture> gestures = new HashMap<>();

    public HashMap<ContactTarget, Gesture> getGestures() {
        return gestures;
    }

    public void setGestures(HashMap<ContactTarget, Gesture> gestures) {
        this.gestures = gestures;
    }

    public void removeGesture(ContactTarget contactID){
        gestures.remove(contactID);
    }

    public String save() {
        Log.d(TAG, "Trying to save GestureList.");
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ObjectOutputStream output = new ObjectOutputStream(stream);
            output.writeObject(gestures);
            output.close();
            return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
        } catch (IOException e) {
            Log.d(TAG, e.getMessage(), e);
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
            Log.d(TAG, e.getMessage() + e.getStackTrace());
        }
    }

    //public HashMap<ContactTarget, Gesture> getGestures(){
    //}
}
