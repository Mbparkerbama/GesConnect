package com.cs495.gesconnect;

public class ContactTarget {
    public String getLookupKey() {
        return lookupKey;
    }

    public void setLookupKey(String lookupKey) {
        this.lookupKey = lookupKey;
    }

    public int getPhonteType() {
        return phonteType;
    }

    public void setPhonteType(int phonteType) {
        this.phonteType = phonteType;
    }

    private String lookupKey;
    private int phonteType;

}
