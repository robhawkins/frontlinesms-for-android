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
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import net.frontlinesms.android.FrontlineSMS;
import net.frontlinesms.android.R;
import net.frontlinesms.android.model.PIMService;
import net.frontlinesms.android.model.model.KeywordAction;
import net.frontlinesms.android.model.model.KeywordActionDao;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Mathias Lin <mathias.lin@metahealthcare.com>
 */
public class Keyword extends BaseActivity {

    private KeywordActionDao mKeywordDao;
    private KeywordAction mKeywordAction;

    /** Menu item to send message to selected rules. */
    private static final int MENU_OPTION_SAVE = Menu.FIRST;
    private static final int MENU_OPTION_CANCEL = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keyword);
        mKeywordDao = new KeywordActionDao(getContentResolver());

        if (getIntent()!=null) {
            int id = getIntent().getIntExtra(FrontlineSMS.EXTRA_KEYWORD_ID, -1);

            Log.d("onClick", "onCreate.id: " + id);

            if (id!=-1) {
                mKeywordAction = mKeywordDao.getKeywordById(id);
            } else mKeywordAction = new KeywordAction();
        } else mKeywordAction = new KeywordAction();
        initView();
    }

    private void initView() {

        // group list
        if (PIMService.groupNameCache.isEmpty()) {
            PIMService.getGroups(getApplicationContext()).close();
        }
        ArrayList<String> groups = new ArrayList<String>();
        groups.add("");
        int ix = 1;
        int groupListIndex = -1;
        for (Iterator iter=PIMService.groupNameCache.keySet().iterator();iter.hasNext();) {
            String g = (String)PIMService.groupNameCache.get(iter.next());
            if (g.equals(mKeywordAction.getGroup())) {
                groupListIndex = ix;
            }
            groups.add(g);
            ix++;
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, groups);
        Spinner spinnerAddToGroup = ((Spinner)findViewById(R.id.spn_add_to_group));
        spinnerAddToGroup.setAdapter(spinnerArrayAdapter);
        Spinner spinnerRemoveFromGroup = ((Spinner)findViewById(R.id.spn_remove_from_group));
        spinnerRemoveFromGroup.setAdapter(spinnerArrayAdapter);

        // group
        if (mKeywordAction.getType()==KeywordAction.Type.JOIN) {
            spinnerAddToGroup.setSelection(groupListIndex);
        } else spinnerAddToGroup.setSelection(0);
        if (mKeywordAction.getType()==KeywordAction.Type.LEAVE) {
            spinnerRemoveFromGroup.setSelection(groupListIndex);
        } else spinnerRemoveFromGroup.setSelection(0);

        // set title
        if (mKeywordAction==null) {
            ((TextView)findViewById(R.id.txt_header)).setText("New Keyword");
        } else {
            ((TextView)findViewById(R.id.txt_header)).setText("Keyword: " + mKeywordAction.getKeyword());
            ((TextView)findViewById(R.id.edt_keyword)).setText(mKeywordAction.getKeyword());
            ((TextView)findViewById(R.id.edt_description)).setText(mKeywordAction.getDescription());
            ((TextView)findViewById(R.id.edt_autoreply)).setText(mKeywordAction.getText());

            ((CheckBox)findViewById(R.id.chk_autoreply)).setChecked(mKeywordAction.getType()==KeywordAction.Type.REPLY);
            ((CheckBox)findViewById(R.id.chk_add_to_group)).setChecked(mKeywordAction.getType()==KeywordAction.Type.JOIN);
            ((CheckBox)findViewById(R.id.chk_remove_from_group)).setChecked(mKeywordAction.getType()==KeywordAction.Type.LEAVE);
        }

    }

    private void assignFormValuesToDao() {
        mKeywordAction.setKeyword(((EditText) findViewById(R.id.edt_keyword)).getText().toString());
        mKeywordAction.setDescription( ((EditText)findViewById(R.id.edt_description)).getText().toString() );

        // TODO values not reset properly when saving
        if ( ((CheckBox)findViewById(R.id.chk_add_to_group)).isChecked()) {
            mKeywordAction.setType(KeywordAction.Type.JOIN);
        }
        else if ( ((CheckBox)findViewById(R.id.chk_remove_from_group)).isChecked()) {
            mKeywordAction.setType(KeywordAction.Type.LEAVE);
        }
        else if ( ((CheckBox)findViewById(R.id.chk_autoreply)).isChecked()) {
            mKeywordAction.setType(KeywordAction.Type.REPLY);
        }
        mKeywordAction.setGroup(((Spinner) findViewById(R.id.spn_add_to_group)).getSelectedItem().toString());
        mKeywordAction.setText(((EditText)findViewById(R.id.edt_autoreply)).getText().toString());
        mKeywordAction.setGroup(((Spinner)findViewById(R.id.spn_remove_from_group)).getSelectedItem().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        int groupId = 0;
        MenuItem menuItem = menu.add(MENU_OPTION_SAVE, MENU_OPTION_SAVE, Menu.NONE, R.string.menu_option_save);
        menuItem.setIcon(android.R.drawable.ic_menu_save);
        menuItem = menu.add(MENU_OPTION_CANCEL, MENU_OPTION_CANCEL, Menu.NONE, R.string.menu_option_cancel);
        menuItem.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case MENU_OPTION_SAVE:
                try {
                    assignFormValuesToDao();
                    Log.d("Keyword", "Keyword to store: " + mKeywordAction);
                    mKeywordDao.saveOrUpdateAction(mKeywordAction);
                    Toast.makeText(this, "Keyword saved.", Toast.LENGTH_SHORT).show();
                    finish();
                } catch (Exception e) {
                    Log.e("Keyword", "Saving failed.", e);
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            case MENU_OPTION_CANCEL:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
