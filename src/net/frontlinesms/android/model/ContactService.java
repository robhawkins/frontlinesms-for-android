/**
 * FrontlineSMS <http://www.frontlinesms.com>
 * Copyright 2010, Meta Healthcare Systems Ltd.
 *
 * This file is part of FrontlineSMS for Android.
 *
 * FrontlineSMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * FrontlineSMS is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FrontlineSMS. If not, see <http://www.gnu.org/licenses/>.
 */
package net.frontlinesms.android.model;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import net.frontlinesms.android.model.model.Contact;

import java.util.ArrayList;
import java.util.Vector;

public class ContactService {

    private final static String TAG = ContactService.class.getSimpleName();

    /**
     * Obtains the contact list for the currently selected account.
     *
     * @return A cursor for for accessing the contact list.
     */
    public static Cursor getContactsByGroup(final Context context, final Integer[] ids)
    {
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        String idString = "";
        for (Integer id:ids) {
            idString += (!"".equals(idString)?",":"") + id;
        }
        Cursor cTmp = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[]{
                ContactsContract.Data._ID, ContactsContract.Data.CONTACT_ID, ContactsContract.Data.DISPLAY_NAME},
                ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + " IN (" + idString + ")",
                null, sortOrder);

        // filter only those contacts who have a phone number
        ArrayList<Integer> idList = new ArrayList<Integer>();
        while (cTmp.moveToNext()) {
            idList.add(cTmp.getInt(cTmp.getColumnIndex(ContactsContract.Data.CONTACT_ID)));
        }
        Integer[] contactIds = idList.toArray(new Integer[idList.size()]);
        return getContactsById(context, contactIds);
    }

    public static Cursor getContactsById(final Context context, final Integer[] ids)
    {
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        String idString = "";
        for (Integer id:ids) {
            idString += (!"".equals(idString)?",":"") + id;
        }
        Log.d(TAG, "getContactsById: " + idString);
        Cursor c = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, new String[] {
                ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME,ContactsContract.Contacts.HAS_PHONE_NUMBER},
                ContactsContract.Contacts._ID + " IN (" + idString + ")" +
                " AND " + ContactsContract.Contacts.HAS_PHONE_NUMBER + " > 0",
                null, sortOrder);
        Log.d(TAG, "getContactsById - c.results: " + c.getCount());
        return c;
    }


    public static String getContactNameByPhoneNumber(String number) {
        return "Mathias Lin";
    }


}
