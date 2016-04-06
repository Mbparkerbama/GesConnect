package com.cs495.gesconnect;

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

    public void setPhoneType(int phonteType) {
        this.phoneType = phoneType;
    }

    private String lookupKey;
    private int phoneType;

}
