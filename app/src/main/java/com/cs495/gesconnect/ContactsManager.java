package com.cs495.gesconnect;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

/**
 * Created by spencer on 4/6/16.
 */
public class ContactsManager {

    private Context ctx;

    public ContactsManager(Context ctx) {
        this.ctx = ctx;
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
                                + " = ?",
                        new String[] { name }, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            number = cursor.getString(0);
            cursor.close();
        }

        return number;
    }
}
