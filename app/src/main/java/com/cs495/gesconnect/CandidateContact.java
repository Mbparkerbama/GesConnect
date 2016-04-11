package com.cs495.gesconnect;

/**
 * Created by Patrick on 4/6/2016.
 */
public class CandidateContact {
    CandidateContact(String lookupKey, int phoneType, String displayString) {
        this.lookupKey = lookupKey;
        this.phoneType = phoneType;
        this.displayString = displayString;
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

    public String getDisplayString() {
        return displayString;
    }

    public void setDisplayString(String displayString) {
        this.displayString = displayString;
    }

    private String lookupKey;

    private int phoneType;

    private String displayString;
}
