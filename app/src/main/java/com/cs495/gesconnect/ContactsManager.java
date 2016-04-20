package com.cs495.gesconnect;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by spencer on 4/6/16.
 */
public class ContactsManager {

    private Context ctx;

    public ContactsManager(Context ctx) {
        this.ctx = ctx;
    }

    private static final String TAG = "ContactsManager";

    public int countMatches(String name) {
        /**
         * Note: Assume contacts "Dad" and "Dad Work". Input gesture "Dad" can _never_
         *       match because it is a partial for the other contact.
         */
        Set<Integer> matches = new HashSet<>();

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
                matches.add(cursor.getInt(1));
                Log.d(TAG, cursor.getString(1));
            } while(cursor.moveToNext());
            cursor.close();
        }

        Log.d(TAG, Integer.toString(matches.size()));

        return matches.size();
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

    public String findPhoneByType(String lookupKey, Integer phoneType) {
        String number = "";

        Cursor cursor = ctx.getContentResolver()
                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[] {
                                ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY,
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                                ContactsContract.CommonDataKinds.Phone.TYPE,
                                ContactsContract.CommonDataKinds.Phone.NUMBER },

                                ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY
                                    + " = ? AND " +
                                ContactsContract.CommonDataKinds.Phone.TYPE
                                    + " = ?",

                        new String[] {lookupKey, Integer.toString(phoneType)}, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            number = cursor.getString(3);
            Log.d(TAG, "PHONE TYPE: " + cursor.getString(2));
            cursor.close();
        }

        return number;
    }

    public String findName(String lookupKey) {
        String name = null;

        Cursor cursor = ctx.getContentResolver()
                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[] {
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                        },
                        ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY
                            + " = ? ",
                        new String[] {lookupKey}, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            name = cursor.getString(0);
            Log.d(TAG, "DISPLAY NAME: " + name);
            cursor.close();
        }

        return name;
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

                // Query ContactsContract.Data for all rows containing phone numbers
                // associated with this contact
                Cursor phoneCursor = ctx.getContentResolver().query(
                        android.provider.ContactsContract.Data.CONTENT_URI,
                        new String[] {
                                android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER,
                                android.provider.ContactsContract.CommonDataKinds.Phone.TYPE,
                                },
                        android.provider.ContactsContract.Data.LOOKUP_KEY
                                + "=?" + " AND "
                                + android.provider.ContactsContract.Data.MIMETYPE
                                + "='"
                                + android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                                + "'",
                        new String[] {lookupKey}, null);

                // If no results found, add no entries
                if ((phoneCursor == null)
                        || (phoneCursor.getCount() <= 0)) {
                    continue;
                }

                phoneCursor.moveToFirst();

                do {
                    // Add an entry for each phone number associated with the contact
                    String contactNumber =
                            cursor.getString(cursor.getColumnIndex(
                            android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER));
                    int phoneType =
                            Integer.parseInt(
                                    cursor.getString(cursor.getColumnIndex(
                                    android.provider.ContactsContract.CommonDataKinds.Phone.TYPE)));
                    results.add(new CandidateContact(lookupKey,
                            phoneType,
                            contactName + " " + contactNumber));
                } while (phoneCursor.moveToNext());


            } while (cursor.moveToNext());
            cursor.close();
        }

        return results;
    }
}
