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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class PIMService {

    private final static String TAG = PIMService.class.getSimpleName();

    /** Cached group id to group name mappings, for faster group name lookup, i.e. in message composer. */
    public static HashMap<Integer, String> groupNameCache = new HashMap<Integer, String>();


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


    /**
     * Obtains the group list for the currently selected account.
     *
     * @return A cursor for for accessing the contact list.
     */
    public static Cursor getGroups(Context context)
    {
        // Run query
        Uri uri = ContactsContract.Groups.CONTENT_URI;
        String[] projection = new String[] {
                ContactsContract.Groups._ID,
                ContactsContract.Groups.TITLE,
                ContactsContract.Groups.ACCOUNT_NAME
        };
        String selection = ContactsContract.Groups.TITLE + " not like 'System Group:%'";
        String sortOrder = ContactsContract.Groups.TITLE + " COLLATE LOCALIZED ASC";

        Cursor cursor = context.getContentResolver().query(uri, projection, selection, null, sortOrder);
        if (groupNameCache.isEmpty() && cursor.moveToFirst()) {
            do {
                groupNameCache.put(cursor.getInt(0), cursor.getString(1));
            } while (cursor.moveToNext());
            cursor.moveToFirst();
        }
        return cursor;
    }


    /**
     * Adds a contact to a group.
     * @param context Context
     * @param personId Raw contact id
     * @param groupId Group id
     * @return Uri to the contact data entry
     */
    public static Uri addContactToGroup(Context context, long personId, long groupId) {
        //remove if exists
        removeContactFromGroup(context, personId, groupId);
        ContentValues values = new ContentValues();
        values.put(ContactsContract.CommonDataKinds.GroupMembership.RAW_CONTACT_ID,
                personId);
        values.put(
                ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID,
                groupId);
        values.put(
                    ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE,
                    ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE);
        return context.getContentResolver().insert(
                ContactsContract.Data.CONTENT_URI, values);
    }

    /**
     * Removes a contact from a group.
     * @param context Context
     * @param personId Raw contact id
     * @param groupId Group id
     * @return
     */
    public static int removeContactFromGroup(Context context, long personId, long groupId) {
        ContentValues values = new ContentValues();
        values.put(ContactsContract.CommonDataKinds.GroupMembership.RAW_CONTACT_ID,
                personId);
        values.put(
                ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID,
                groupId);
        values.put(
                    ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE,
                    ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE);

        return context.getContentResolver().delete(
                ContactsContract.Data.CONTENT_URI, "_id=?",
                new String[]{values.getAsInteger("_id").toString()});
    }

}
