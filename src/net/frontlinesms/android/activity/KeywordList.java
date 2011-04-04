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
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import net.frontlinesms.android.FrontlineSMS;
import net.frontlinesms.android.R;
import net.frontlinesms.android.model.KeywordActionDao;
import net.frontlinesms.android.model.IKeywordActionDao;

import java.util.HashMap;

/**
 * @author Mathias Lin <mathias.lin@metahealthcare.com>
 */
public final class KeywordList extends BaseActivity {

    public static final String TAG = KeywordList.class.getSimpleName();

    /** Cached group id to rule name mappings, for faster rules name lookup. */
    public static HashMap<Integer, String> ruleNameCache = new HashMap<Integer, String>();

    /** List view that displays the rules. */
    private ListView mKeywordList;

    /** List adapter. */
    private KeywordListAdapter mAdapter;

    /** Keyword db access object. */
    private IKeywordActionDao keywordDao;

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
        mKeywordList = (ListView) findViewById(R.id.lst_rules);

        // Populate the rule list
        initKeywordList();
    }

    // TODO


    @Override
    protected void onResume() {
        super.onResume();
        // update the list if user came from editing a keyword
        mAdapter.changeCursor(keywordDao.getAllKeywordsCursor());
    }

    /**
     * Populate the rule list based on account currently selected in the account spinner.
     */
    private void initKeywordList() {

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
        mKeywordList.addHeaderView(txtHeader);

        // Build adapter with contact entries
        Cursor cursor = getKeywords();
        mAdapter = new KeywordListAdapter(this, cursor);
        mKeywordList.setAdapter(mAdapter);
    }

    /**
     * Obtains the rule list for the currently selected account.
     *
     * @return A cursor for for accessing the contact list.
     */
    private Cursor getKeywords()
    {
        keywordDao = new KeywordActionDao(getContentResolver());
        /*if(keywordDao.getAllKeywords().size() == 0) {
        	createDemoKeywords();
        }*/
        return keywordDao.getAllKeywordsCursor();
    }

    /*private void createDemoKeywords() {
		// Populate the map with some keyword actions
		keywordDao.saveOrUpdateAction(KeywordAction.createReplyAction("hi", "Test", "Hello there, PropertySubstituter.KEY_SENDER_NAME"
                + ", i do like your phone number (PropertySubstituter.KEY_SENDER_PHONENUMBER)"));
		keywordDao.saveOrUpdateAction(KeywordAction.createJoinAction("join", "test", "Guangzhou"));
		keywordDao.saveOrUpdateAction(KeywordAction.createLeaveAction("stop", "test", "Vodafone"));
		keywordDao.saveOrUpdateAction(KeywordAction.createForwardAction("say", "test",
                "PropertySubstituter.KEY_SENDER_NAME says PropertySubstituter.KEY_ORIGINAL_MESSAGE",
                null));
	}*/

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
        switch (itemId) {
            case MENU_OPTION_NEW:
                Intent intent = new Intent(this, Keyword.class);
                startActivity(intent);
                break;
            case MENU_OPTION_EDIT:
                if (mAdapter.getSelectedItems().size()==1) {
                    intent = new Intent(this, Keyword.class);
                    intent.putExtra(FrontlineSMS.EXTRA_KEYWORD_ID, mAdapter.getSelectedItems().get(0));
                    startActivity(intent);
                } else {
                    String msg = "No keyword selected.";
                    if (mAdapter.getSelectedItems().size()>1) {
                        msg = "To edit, only select 1 keyword.";
                    }
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                }
                break;
            case MENU_OPTION_DELETE:
                if (mAdapter.getSelectedItems().size()>0) {
                    for (int id:mAdapter.getSelectedItems()) {
                        keywordDao = new KeywordActionDao(getContentResolver());
                        keywordDao.deleteKeyword(keywordDao.getKeywordById(id));
                    }
                    mAdapter.getSelectedItems().clear();
                    mAdapter.notifyDataSetChanged();
                    mAdapter.changeCursor(keywordDao.getAllKeywordsCursor());
                    Toast.makeText(this, "Keyword(s) deleted.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "No keyword(s) selected.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}