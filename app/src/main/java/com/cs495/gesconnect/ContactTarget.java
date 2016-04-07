package com.cs495.gesconnect;

import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ContactTarget {
    ContactTarget(String lookupKey, int phoneType) {
        this.lookupKey = lookupKey;
        this.phoneType = phoneType;
    }

    public String getLookupKey() {
        return lookupKey;
    }

    public void setLookupKey(String lookupKey) {
        this.lookupKey = lookupKey;
    }

    public int getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(int phoneType) {
        this.phoneType = phoneType;
    }

    public String save() {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ObjectOutputStream output = new ObjectOutputStream(stream);
            output.writeObject(lookupKey);
            output.writeObject(phoneType);
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
            lookupKey = (String) input.readObject();
            phoneType = (int) input.readObject();
        }
        catch (Exception e) {

        }
    }

    private String lookupKey;
    private int phoneType;

}
