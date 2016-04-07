package com.cs495.gesconnect;

/**
 * Created by Patrick on 4/6/2016.
 */
public class CandidateContact {
    CandidateContact(String lookupKey, String displayString) {
        this.lookupKey = lookupKey;
        this.displayString = displayString;
    }

    public String getLookupKey() {
        return lookupKey;
    }

    public void setLookupKey(String lookupKey) {
        this.lookupKey = lookupKey;
    }

    public String getDisplayString() {
        return displayString;
    }

    public void setDisplayString(String displayString) {
        this.displayString = displayString;
    }

    private String lookupKey;

    private String displayString;
}
