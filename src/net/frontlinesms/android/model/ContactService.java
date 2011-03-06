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

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

public class ContactService {

    private final static String TAG = ContactService.class.getSimpleName();

    /**
     * Obtains the contact list for the currently selected account.
     *
     * @return A cursor for for accessing the contact list.
     */
    public static Cursor getContactsByGroup(final Context context, final Integer[] ids)
    {
        // Run query
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };
//        String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '" +
//                (mShowInvisible ? "0" : "1") + "'";
//        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        String idString = "";
        for (Integer id:ids) {
            idString += (!"".equals(idString)?",":"") + id;
        }

        Cursor c = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[]{
                ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME}, //ContactsContract.Data.HAS_PHONE_NUMBER
                ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + " IN (" + idString + ")",
//                " AND " + ContactsContract.Data.HAS_PHONE_NUMBER + " > 0",
                null, sortOrder);
        return c;

        // return managedQuery(uri, projection, selection, selectionArgs, sortOrder);
    }

    public static Cursor getContactsById(final Context context, final Integer[] ids)
    {
        // Run query
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };
//        String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '" +
//                (mShowInvisible ? "0" : "1") + "'";
//        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        String idString = "";
        for (Integer id:ids) {
            idString += (!"".equals(idString)?",":"") + id;
        }

        Log.d(TAG, "getContactsById: " + idString);

        Cursor c = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[] {
                ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME}, //ContactsContract.Data.HAS_PHONE_NUMBER
                ContactsContract.Contacts._ID + " IN (" + idString + ")",
                // " AND " + ContactsContract.Data.HAS_PHONE_NUMBER + " > 0",
                null, sortOrder);

        Log.d(TAG, "getContactsById - c.results: " + c.getCount());

        return c;

        // return managedQuery(uri, projection, selection, selectionArgs, sortOrder);
    }

}
