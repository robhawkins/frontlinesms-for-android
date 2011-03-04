/**
 * FrontlineSMS <http://www.frontlinesms.com>
 * Copyright 2011, Meta Healthcare Systems Ltd.
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

import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import net.frontlinesms.android.R;

/**
 * @author Mathias Lin <mathias.lin@metahealthcare.com>
 */
public final class GroupList extends BaseActivity {

    public static final String TAG = GroupList.class.getSimpleName();
    private ListView mGroupList;

    /**
     * Called when the activity is first created. Responsible for initializing the UI.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_list);

        // Obtain handles to UI objects
        mGroupList = (ListView) findViewById(R.id.lst_groups);

        // Populate the contact list
        populateContactList();
    }

    /**
     * Populate the group list based on account currently selected in the account spinner.
     */
    private void populateContactList() {

        // List header
        TextView txtHeader = new TextView(this);
        txtHeader.setLayoutParams(
                new ListView.LayoutParams(
                        ListView.LayoutParams.FILL_PARENT,
                        ListView.LayoutParams.WRAP_CONTENT));
        txtHeader.setText("Groups");
        txtHeader.setPadding(10, 10, 10, 10);
        txtHeader.setBackgroundColor(Color.parseColor("#8E0052"));
        txtHeader.setTextColor(Color.WHITE);
        mGroupList.addHeaderView(txtHeader);

        // Build adapter with contact entries
        Cursor cursor = getGroups();
        String[] fields = new String[] {
                ContactsContract.Groups.TITLE,
                ContactsContract.Groups.ACCOUNT_NAME,
        };
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.group_list_item, cursor,
                fields, new int[] {R.id.txt_title, R.id.txt_account});
        mGroupList.setAdapter(adapter);
    }

    /**
     * Obtains the group list for the currently selected account.
     *
     * @return A cursor for for accessing the contact list.
     */
    private Cursor getGroups()
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
        return managedQuery(uri, projection, selection, null, sortOrder);
    }


}