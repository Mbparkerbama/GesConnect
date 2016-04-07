package com.cs495.gesconnect;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by spencer on 4/6/16.
 */
public class ContactsManager {

    private Context ctx;

    public ContactsManager(Context ctx) {
        this.ctx = ctx;
    }

    public int countMatches(String name) {
        int matches = 0;

        Cursor cursor = ctx.getContentResolver()
                .query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[] {
                                ContactsContract.CommonDataKinds.Phone.NUMBER,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME },
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                                + " LIKE ?",
                        new String[] { name + "%" }, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                matches++;
            } while(cursor.moveToNext());
            cursor.close();
        }

        return matches;
    }

    public String findPhoneNumber(String name) {
        String number = "";

        Cursor cursor = ctx.getContentResolver()
                .query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[] {
                                ContactsContract.CommonDataKinds.Phone.NUMBER,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME },
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                                + " LIKE ?",
                        new String[] { name + "%" }, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            number = cursor.getString(0);
            cursor.close();
        }

        return number;
    }

    public ArrayList<CandidateContact> findCandidateContacts() {
        ArrayList<CandidateContact> results = new ArrayList<CandidateContact>();

        Cursor cursor = ctx.getContentResolver()
                .query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String lookupKey = cursor.getString(cursor.getColumnIndex(
                        android.provider.ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY));
                String contactName = cursor.getString(cursor.getColumnIndex(
                        android.provider.ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                results.add(new CandidateContact(lookupKey, contactName));
            } while(cursor.moveToNext());
            cursor.close();
        }

        return results;
    }
}
