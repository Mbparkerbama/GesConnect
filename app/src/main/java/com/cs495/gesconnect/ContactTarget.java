package com.cs495.gesconnect;

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
            OutputStream stream = new ByteArrayOutputStream();
            ObjectOutput output = new ObjectOutputStream(stream);
            output.writeObject(lookupKey);
            output.writeObject(phoneType);
            return output.toString();
        }
        catch (Exception e) {

        }

        return "";
    }

    public void load(String data) {
        try {
            InputStream stream = new ByteArrayInputStream(
                    data.getBytes());
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
