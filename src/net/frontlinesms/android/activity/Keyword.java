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

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

    /** Menu item to save / cancel operation. */
    private static final int MENU_OPTION_SAVE = Menu.FIRST;
    private static final int MENU_OPTION_CANCEL = 2;

    // Views
    private CheckBox mChkReply;
    private CheckBox mChkForward;
    private CheckBox mChkAddToGroup;
    private CheckBox mChkRemoveFromGroup;
    private Spinner mSpinnerAddToGroup;
    private Spinner mSpinnerRemoveFromGroup;
    private Spinner mSpinnerForwardToGroup;
    private EditText mEdtText;
    private TextView mTxtText;

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

    /**
     * Initialize views
     */
    private void initView() {
        mChkForward = ((CheckBox)findViewById(R.id.chk_forward));
        mChkReply = ((CheckBox)findViewById(R.id.chk_autoreply));
        mChkAddToGroup = ((CheckBox)findViewById(R.id.chk_add_to_group));
        mChkRemoveFromGroup = ((CheckBox)findViewById(R.id.chk_remove_from_group));
        mSpinnerAddToGroup = ((Spinner)findViewById(R.id.spn_add_to_group));
        mSpinnerRemoveFromGroup = ((Spinner)findViewById(R.id.spn_remove_from_group));
        mSpinnerForwardToGroup = ((Spinner)findViewById(R.id.spn_forward_to_group));
        mEdtText = ((EditText)findViewById(R.id.edt_text));
        mTxtText = ((TextView)findViewById(R.id.txt_text));

        // group list
        if (PIMService.groupNameCache.isEmpty()) {
            PIMService.getGroupsCursor(getApplicationContext()).close();
        }
        ArrayList<String> groups = new ArrayList<String>();
        groups.add(getResources().getString(R.string.choose_group));
        int ix = 1;
        int groupListIndex = -1;

        Log.d("Group", "Group cache size: " + PIMService.groupNameCache.keySet().size());

        for (Iterator iter=PIMService.groupNameCache.keySet().iterator();iter.hasNext();) {
            String g = PIMService.groupNameCache.get(iter.next());

            Log.d("Group", "Group cache g: " + g + " = " + mKeywordAction.getGroup() + "?");

            if (g.equals(mKeywordAction.getGroup())) {
                groupListIndex = ix;
            }
            groups.add(g);
            ix++;
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, groups);
        mSpinnerAddToGroup.setAdapter(spinnerArrayAdapter);
        mSpinnerRemoveFromGroup.setAdapter(spinnerArrayAdapter);
        mSpinnerForwardToGroup.setAdapter(spinnerArrayAdapter);

        Log.d("Group", "Group listIndex: " + groupListIndex);

        // group
        if (mKeywordAction.getType()==KeywordAction.Type.JOIN) {
            mSpinnerAddToGroup.setSelection(groupListIndex);
        } else {
            mSpinnerAddToGroup.setSelection(0);
        }
        if (mKeywordAction.getType()==KeywordAction.Type.LEAVE) {
            mSpinnerRemoveFromGroup.setSelection(groupListIndex);
        } else {
            mSpinnerRemoveFromGroup.setSelection(0);
        }
        if (mKeywordAction.getType()==KeywordAction.Type.FORWARD) {
            mSpinnerForwardToGroup.setSelection(groupListIndex);
        } else {
            mSpinnerRemoveFromGroup.setSelection(0);
        }

        // set title
        if (mKeywordAction==null || mKeywordAction.getKeyword()==null) {
            ((TextView)findViewById(R.id.txt_header)).setText("New Keyword");
        } else {
            ((TextView)findViewById(R.id.txt_header)).setText("Keyword: " + mKeywordAction.getKeyword());
            ((TextView)findViewById(R.id.edt_keyword)).setText(mKeywordAction.getKeyword());
            ((TextView)findViewById(R.id.edt_description)).setText(mKeywordAction.getDescription());
            mEdtText.setText(mKeywordAction.getText());

            mChkForward.setChecked(mKeywordAction.getType()==KeywordAction.Type.FORWARD);
            mChkReply.setChecked(mKeywordAction.getType()==KeywordAction.Type.REPLY);
            mChkAddToGroup.setChecked(mKeywordAction.getType()==KeywordAction.Type.JOIN);
            mChkRemoveFromGroup.setChecked(mKeywordAction.getType()==KeywordAction.Type.LEAVE);
        }

        checkCheckBoxLogic(null);
    }

    /**
     * Prepare the keyword action object with form values for storing in database.
     */
    private void assignFormValuesToDao() {
        mKeywordAction.setKeyword(((EditText) findViewById(R.id.edt_keyword)).getText().toString());
        mKeywordAction.setDescription( ((EditText)findViewById(R.id.edt_description)).getText().toString() );
        mKeywordAction.setGroup(null);
        mKeywordAction.setText(null);
        String spinnerValue;

        if (mChkAddToGroup.isChecked()) {
            mKeywordAction.setType(KeywordAction.Type.JOIN);
            spinnerValue = ((Spinner) findViewById(R.id.spn_add_to_group)).getSelectedItem().toString();
            if (getResources().getString(R.string.choose_group).equals(spinnerValue)) {
                 spinnerValue = null;
            }
            mKeywordAction.setGroup(spinnerValue);
        }
        else if (mChkRemoveFromGroup.isChecked()) {
            mKeywordAction.setType(KeywordAction.Type.LEAVE);
            spinnerValue = ((Spinner) findViewById(R.id.spn_remove_from_group)).getSelectedItem().toString();
            if (getResources().getString(R.string.choose_group).equals(spinnerValue)) {
                 spinnerValue = null;
            }
            mKeywordAction.setGroup(spinnerValue);
        }
        else if (mChkReply.isChecked()) {
            mKeywordAction.setType(KeywordAction.Type.REPLY);
        }
        else if (mChkForward.isChecked()) {
            mKeywordAction.setType(KeywordAction.Type.FORWARD);
            spinnerValue = ((Spinner) findViewById(R.id.spn_forward_to_group)).getSelectedItem().toString();
            if (getResources().getString(R.string.choose_group).equals(spinnerValue)) {
                 spinnerValue = null;
            }
            mKeywordAction.setGroup(spinnerValue);
        }

        mKeywordAction.setText(mEdtText.getText().toString());
    }

    /**
     * Checks that only auto-reply or forward is checked at the same time.
     * @param v View (Checkbox)
     */
    public void checkCheckBoxLogic(View v) {
        if ((v==null || v.getId()==R.id.chk_forward) && mChkForward.isChecked()) {
            mChkReply.setChecked(false);
            mChkAddToGroup.setChecked(false);
            mChkRemoveFromGroup.setChecked(false);
            mEdtText.setVisibility(View.VISIBLE);
            mTxtText.setVisibility(View.VISIBLE);
            mSpinnerAddToGroup.setVisibility(View.GONE);
            mSpinnerRemoveFromGroup.setVisibility(View.GONE);
            mSpinnerForwardToGroup.setVisibility(View.VISIBLE);
        } else if ((v==null || v.getId()==R.id.chk_autoreply) && mChkReply.isChecked()) {
            mChkForward.setChecked(false);
            mChkAddToGroup.setChecked(false);
            mChkRemoveFromGroup.setChecked(false);
            mEdtText.setVisibility(View.VISIBLE);
            mTxtText.setVisibility(View.VISIBLE);
            mSpinnerAddToGroup.setVisibility(View.GONE);
            mSpinnerRemoveFromGroup.setVisibility(View.GONE);
            mSpinnerForwardToGroup.setVisibility(View.GONE);
        } else if ((v==null || v.getId()==R.id.chk_add_to_group) && mChkAddToGroup.isChecked()) {
            mChkForward.setChecked(false);
            mChkReply.setChecked(false);
            mChkRemoveFromGroup.setChecked(false);
            mEdtText.setVisibility(View.GONE);
            mTxtText.setVisibility(View.GONE);
            mSpinnerAddToGroup.setVisibility(View.VISIBLE);
            mSpinnerRemoveFromGroup.setVisibility(View.GONE);
            mSpinnerForwardToGroup.setVisibility(View.GONE);
        } else if ((v==null || v.getId()==R.id.chk_remove_from_group) && mChkRemoveFromGroup.isChecked()) {
            mChkForward.setChecked(false);
            mChkReply.setChecked(false);
            mChkAddToGroup.setChecked(false);
            mEdtText.setVisibility(View.GONE);
            mTxtText.setVisibility(View.GONE);
            mSpinnerAddToGroup.setVisibility(View.GONE);
            mSpinnerRemoveFromGroup.setVisibility(View.VISIBLE);
            mSpinnerForwardToGroup.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
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
                    Log.d("Keyword", "Keyword.group to store: " + mKeywordAction.getGroup());
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
