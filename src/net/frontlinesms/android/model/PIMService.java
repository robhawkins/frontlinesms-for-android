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
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.util.Log;
import net.frontlinesms.android.model.model.Contact;
import net.frontlinesms.android.util.sms.WholeSmsMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
                ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.HAS_PHONE_NUMBER},
                ContactsContract.Contacts._ID + " IN (" + idString + ")" +
                " AND " + ContactsContract.Contacts.HAS_PHONE_NUMBER + " > 0",
                // " AND " + ContactsContract.Contacts.HAS_PHONE_NUMBER + " <> 1110",
                null, sortOrder);
        Log.d(TAG, "getContactsById - c.results: " + c.getCount());
        return c;
    }


    public static String getPhoneNumberByContactId(Context context, Integer contactId) {
        Cursor pCur = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[]{contactId.toString()}, null);

        String phone = null;
        while (pCur.moveToNext()) {
            phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        pCur.close();
        return phone;
    }

    private static Contact cursorToContact(Context context, Cursor c) {
        if (c.moveToFirst()) {
            Contact contact = new Contact();
            contact.setId(c.getInt(0));
            contact.setDisplayName(c.getString(1));
            contact.setMobile(getPhoneNumberByContactId(context, contact.getId()));
            return contact;
        } else {
            return null;
        }
    }

    /**
     * Return contact name by searching address book for a given phone number.
     * @param context Context
     * @param number Phone number
     * @return Contact name that belongs to the given phone number
     */
    public static String getContactNameByPhoneNumber(final Context context, String number) {
        return (String)returnContactProperty(context, number, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, true);
    }


    /**
     * Return contact id by searching address book for a given phone number.
     * @param context Context
     * @param number Phone number
     * @return Contact id that belongs to the given phone number
     */
    public static Integer getContactIdByPhoneNumber(final Context context, final String number) {
        return (Integer)returnContactProperty(context, number, ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID, true);
    }

    public static Contact getContactByPhoneNumber(final Context context, final String number) {
        Integer contactId = (Integer)returnContactProperty(context,
                number, ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID, true);
        Log.d("MsgActions", "getContactByPhoneNumber --> : " + contactId);
        if (contactId!=null) {
            Contact c = cursorToContact(context, getContactsById(context, new Integer[]{contactId}));
            Log.d("MsgActions", "Contact --> : " + c);
            return c;
        } else {
            return null;
        }
    }


    private static Object returnContactProperty(Context context, final String number, String property, boolean createIfNotFound) {
        String[] whereArgs = new String[] {
                String.valueOf(number),  // "+86" +
                String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) };

        Cursor c = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{property},
                ContactsContract.CommonDataKinds.Phone.NUMBER + " = ? and "
                        + ContactsContract.CommonDataKinds.Phone.TYPE + " = ?", whereArgs, null);

        Object result = null;

        while (c.moveToNext()) {
            for (int i=0;i<c.getColumnCount();i++) {
                Log.d("C2.result: ", c.getColumnName(i) + ": " + c.getString(i));
                result = c.getInt(i);
            }
        }
         // TODO fix exception here!!!
        if (c.moveToFirst()) {
            int colIndex = c.getColumnIndexOrThrow(property);
            if (property.equals(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID)) {
                result = c.getInt(colIndex);
            } else {
                result = c.getString(colIndex);
            }
            Log.d("MsgActions", "Matched contactId: " + result);
            c.close();
            return result;
        } else if (createIfNotFound) {
            // if contact doesn't exist yet, create first
            c.close();
            createContactFromMessage(context, number);
            return returnContactProperty(context, number, property, false);
            /*c = context.getContentResolver().
                    query(uri, new String[]{property}, null, null, null);
            if (c.moveToFirst()) {
                int colIndex = c.getColumnIndexOrThrow(property);
                if (property.equals(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID)) {
                    result = c.getInt(colIndex);
                } else {
                    result = c.getString(colIndex);
                }
            }*/
        } else {
            c.close();
            return result;
        }

    }


    public static Integer getGroupIdByName(final Context context, String groupName) {
        // group list
        if (PIMService.groupNameCache.isEmpty()) {
            PIMService.getGroups(context.getApplicationContext()).close();
        }
        for (Iterator iter=groupNameCache.keySet().iterator(); iter.hasNext();) {
            Integer key = (Integer)iter.next();
            if (groupName.equals(groupNameCache.get(key))) {
                return key;
            }
        }
        return null;
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
                new String[]{values.getAsInteger(ContactsContract.CommonDataKinds.GroupMembership.RAW_CONTACT_ID).toString()});
    }

    public static Uri createContactFromMessage(Context context, String number) {
        ContentValues values = new ContentValues();

        // TODO this api is deprecated, should be replaced with 2.x api
        // Add contact base record
        values.put(Contacts.People.NAME, number);
        Uri uri = context.getContentResolver().insert(Contacts.People.CONTENT_URI, values);

        // Add a phone number for Abraham Lincoln.  Begin with the URI for
        // the new record just returned by insert(); it ends with the _ID
        // of the new record, so we don't have to add the ID ourselves.
        // Then append the designation for the phone table to this URI,
        // and use the resulting URI to insert the phone number.
        Uri phoneUri = Uri.withAppendedPath(uri, Contacts.People.Phones.CONTENT_DIRECTORY);
        values.clear();
        values.put(Contacts.People.Phones.TYPE, Contacts.People.Phones.TYPE_MOBILE);
        values.put(Contacts.People.Phones.NUMBER, number);
        uri = context.getContentResolver().insert(phoneUri, values);

        context.getContentResolver().notifyChange(Contacts.People.CONTENT_URI, null);
        context.getContentResolver().notifyChange(ContactsContract.Contacts.CONTENT_URI, null);

        return uri;
    }

}
