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

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import net.frontlinesms.android.FrontlineSMS;
import net.frontlinesms.android.R;

import java.util.HashMap;

/**
 * @author Mathias Lin <mathias.lin@metahealthcare.com>
 */
public final class RuleList extends BaseActivity {

    public static final String TAG = RuleList.class.getSimpleName();

    /** Cached group id to rule name mappings, for faster rules name lookup. */
    public static HashMap<Integer, String> ruleNameCache = new HashMap<Integer, String>();

    /** List view that displays the rules. */
    private ListView mRuleList;

    /** List adapter. */
    private RuleListAdapter mAdapter;

    /** Menu item to send message to selected rules. */
    private static final int MENU_OPTION_NEW = Menu.FIRST;
    private static final int MENU_OPTION_EDIT = 2;
    private static final int MENU_OPTION_DELETE = 3;


    /**
     * Called when the activity is first created. Responsible for initializing the UI.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rule_list);

        // Obtain handles to UI objects
        mRuleList = (ListView) findViewById(R.id.lst_rules);

        // Populate the rule list
        initRuleList();
    }

    /**
     * Populate the rule list based on account currently selected in the account spinner.
     */
    private void initRuleList() {

        // List header
        TextView txtHeader = new TextView(this);
        txtHeader.setLayoutParams(
                new ListView.LayoutParams(
                        ListView.LayoutParams.FILL_PARENT,
                        ListView.LayoutParams.WRAP_CONTENT));
        txtHeader.setText(getResources().getString(R.string.dashboard_rules));
        txtHeader.setPadding(10, 10, 10, 10);
        txtHeader.setBackgroundColor(Color.parseColor("#8E0052"));
        txtHeader.setTextColor(Color.WHITE);
        mRuleList.addHeaderView(txtHeader);

        // Build adapter with contact entries
        Cursor cursor = getRules();
        mAdapter = new RuleListAdapter(this, cursor);
        mRuleList.setAdapter(mAdapter);
    }

    /**
     * Obtains the rule list for the currently selected account.
     *
     * @return A cursor for for accessing the contact list.
     */
    private Cursor getRules()
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

        Cursor cursor = managedQuery(uri, projection, selection, null, sortOrder);
        if (ruleNameCache.isEmpty() && cursor.moveToFirst()) {
            do {
                ruleNameCache.put(cursor.getInt(0), cursor.getString(1));
            } while (cursor.moveToNext());
            cursor.moveToFirst();
        }
        return cursor;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        int groupId = 0;
        MenuItem menuItem = menu.add(MENU_OPTION_NEW, MENU_OPTION_NEW, Menu.NONE, R.string.menu_option_new);
        menuItem.setIcon(android.R.drawable.ic_menu_add);
        menuItem = menu.add(MENU_OPTION_EDIT, MENU_OPTION_EDIT, Menu.NONE, R.string.menu_option_edit);
        menuItem.setIcon(android.R.drawable.ic_menu_edit);
        menuItem = menu.add(MENU_OPTION_DELETE, MENU_OPTION_DELETE, Menu.NONE, R.string.menu_option_delete);
        menuItem.setIcon(android.R.drawable.ic_menu_delete);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}