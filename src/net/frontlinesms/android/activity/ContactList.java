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

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.*;
import net.frontlinesms.android.R;

/**
 * @author Mathias Lin <mathias.lin@metahealthcare.com>
 */
public final class ContactList extends BaseActivity {

    public static final String TAG = "ContactManager";

    // private Button mAddAccountButton;
    private ListView mContactList;
    private Integer mGroupId;
    private String mGroupName;
//    private boolean mShowInvisible;
//    private CheckBox mShowInvisibleControl;

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

        // Obtain handles to UI objects
        mContactList = (ListView) findViewById(R.id.lst_contacts);
//        mAddAccountButton = (Button) findViewById(R.id.btn_add_contact);
//        mShowInvisibleControl = (CheckBox) findViewById(R.id.chk_show_invisible);

        // Initialize class properties
//        mShowInvisible = false;
//        mShowInvisibleControl.setChecked(mShowInvisible);

        // Register handler for UI elements
//        mAddAccountButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Log.d(TAG, "mAddAccountButton clicked");
//                launchContactAdder();
//            }
//        });
//        mShowInvisibleControl.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Log.d(TAG, "mShowInvisibleControl changed: " + isChecked);
//                mShowInvisible = isChecked;
//                populateContactList();
//            }
//        });

        // Populate the contact list
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
        txtHeader.setText("Contacts of " + mGroupName);
        txtHeader.setPadding(10, 10, 10, 10);
        txtHeader.setBackgroundColor(Color.parseColor("#8E0052"));
        txtHeader.setTextColor(Color.WHITE);
        mContactList.addHeaderView(txtHeader);

        // Build adapter with contact entries
        Cursor cursor = getContacts();
        String[] fields = new String[] {
                ContactsContract.Data.DISPLAY_NAME
        };
//        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.contact_list_item, cursor,
//                fields, new int[] {R.id.txt_display_name});
        ContactListAdapter adapter = new ContactListAdapter(this, cursor);
        mContactList.setAdapter(adapter);
    }

    /**
     * Obtains the contact list for the currently selected account.
     *
     * @return A cursor for for accessing the contact list.
     */
    private Cursor getContacts()
    {
        // Run query
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };
//        String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '" +
//                (mShowInvisible ? "0" : "1") + "'";
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        Cursor c = managedQuery(ContactsContract.Data.CONTENT_URI, new String[] {
                    ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID },
                    ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + " = ?",
                    new String[] { mGroupId.toString() }, sortOrder);
        return c;


        // return managedQuery(uri, projection, selection, selectionArgs, sortOrder);
    }

    /**
     * Launches the ContactAdder activity to add a new contact to the selected accont.
     */
    protected void launchContactAdder() {
        Intent i = new Intent(this, ContactAdder.class);
        startActivity(i);
    }
}