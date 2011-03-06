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
package net.frontlinesms.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import net.frontlinesms.android.FrontlineSMS;
import net.frontlinesms.android.R;
import net.frontlinesms.android.model.ContactService;

/**
 * @author Mathias Lin <mathias.lin@metahealthcare.com>
 */
public final class ContactList extends BaseActivity {

    public static final String TAG = "ContactManager";

    // private Button mAddAccountButton;
    private ListView mContactList;
    private ContactListAdapter mAdapter;
    private Integer mGroupId;
    private String mGroupName;

    /** Menu item to send message to selected groups. */
    private static final int MENU_OPTION_SEND_MESSAGE = Menu.FIRST;


    /**
     * Called when the activity is first created. Responsible for initializing the UI.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.v(TAG, "Activity State: onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list);

        mGroupId = getIntent().getIntExtra("EXTRA_GROUP_ID", -1);
        mGroupName = getIntent().getStringExtra("EXTRA_GROUP_NAME");

        mContactList = (ListView) findViewById(R.id.lst_contacts);
        initContactList();
    }

    /**
     * Populate the contact list based on account currently selected in the account spinner.
     */
    private void initContactList() {

        // List header
        TextView txtHeader = new TextView(this);
        txtHeader.setLayoutParams(
                new ListView.LayoutParams(
                        ListView.LayoutParams.FILL_PARENT,
                        ListView.LayoutParams.WRAP_CONTENT));
        txtHeader.setText(getResources().getString(R.string.dashboard_contacts) + ": " + mGroupName);
        txtHeader.setPadding(10, 10, 10, 10);
        txtHeader.setBackgroundColor(Color.parseColor("#8E0052"));
        txtHeader.setTextColor(Color.WHITE);
        mContactList.addHeaderView(txtHeader);

        // Build adapter with contact entries
//        Cursor cursor = getContacts(mGroupId);
        Cursor cursor = ContactService.getContactsByGroup(this, new Integer[]{mGroupId});
        mAdapter = new ContactListAdapter(this, cursor);
        mContactList.setAdapter(mAdapter);
    }

    /**
     * Obtains the contact list for the currently selected account.
     *
     * @return A cursor for for accessing the contact list.
     */
    private Cursor getContacts(final Integer groupId)
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

        Cursor c = managedQuery(ContactsContract.Data.CONTENT_URI, new String[]{
                ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID},
                ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + " = ?",
                new String[]{groupId.toString()}, sortOrder);
        return c;

        // return managedQuery(uri, projection, selection, selectionArgs, sortOrder);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        int groupId = 0;
        MenuItem menuItem = menu.add(groupId, Menu.FIRST, Menu.NONE, R.string.menu_option_send_message);
        menuItem.setIcon(android.R.drawable.ic_menu_send);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == MENU_OPTION_SEND_MESSAGE) {
            if (mAdapter.getSelectedItems().size()>0) {
                Intent intent = new Intent(this, MessageComposer.class);
                intent.putExtra(FrontlineSMS.EXTRA_RECIPIENT_TYPE, MessageComposer.RECIPIENT_TYPE_CONTACTS);
                intent.putExtra(FrontlineSMS.EXTRA_SELECTED_ITEMS, mAdapter.getSelectedItems());
                startActivity(intent);
            } else {
                Toast.makeText(this, "No contacts selected.", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

}