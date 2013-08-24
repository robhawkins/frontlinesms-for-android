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
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.frontlinesms.android.FrontlineSMS;
import net.frontlinesms.android.R;
import net.frontlinesms.android.model.KeywordAction;
import net.frontlinesms.android.model.KeywordActionDao;
import net.frontlinesms.android.model.PIMService;
import net.frontlinesms.android.model.Poll;
import net.frontlinesms.android.model.PollDao;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Mathias Lin <mathias.lin@metahealthcare.com>
 */
public class Keyword extends BaseActivity {

    private KeywordActionDao mKeywordDao;
    private KeywordAction mKeywordAction;

    private PollDao mPollDao;
    private Poll mPoll;

    /** Menu item to save / cancel operation. */
    private static final int MENU_OPTION_SAVE = Menu.FIRST;
    private static final int MENU_OPTION_CANCEL = 2;

    // Views
    private CheckBox mChkReply;
    private CheckBox mChkForward;
    private CheckBox mChkAddToGroup;
    private CheckBox mChkEmail;
    private CheckBox mChkHttpRequest;
    private CheckBox mChkRemoveFromGroup;
    private CheckBox mChkPoll;
    private Spinner mSpinnerAddToGroup;
    private Spinner mSpinnerRemoveFromGroup;
    private Spinner mSpinnerForwardToGroup;
    private Spinner mSpinnerPollToGroup;
    private EditText mEdtTextReply;
    private TextView mTxtTextReply;
    private EditText mEdtTextForward;
    private TextView mTxtTextForward;
    private EditText mEdtTextPoll;
    private TextView mTxtTextPoll;
    private EditText mEdtTextPollResponse1;
    private EditText mEdtTextPollResponse2;
    private TextView mTxtTextPollResponse;
    private EditText mEdtSubjectEmail;
    private EditText mEdtRecipientEmail;
    private EditText mEdtTextEmail;
    private TextView mTxtTextEmail;
    private TextView mTxtSubjectEmail;
    private TextView mTxtRecipientEmail;
    private EditText mEdtHttpUrl;
    private TextView mTxtTextHttpRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keyword);
        mKeywordDao = new KeywordActionDao(getContentResolver());
        mPollDao = new PollDao(getContentResolver());

        if (getIntent()!=null) {
            int id = getIntent().getIntExtra(FrontlineSMS.EXTRA_KEYWORD_ID, -1);

            Log.d("onClick", "onCreate.id: " + id);

            if (id!=-1) {
                mKeywordAction = mKeywordDao.getKeywordById(id);
            } else {
                mKeywordAction = new KeywordAction();
            }
        } else {
            mKeywordAction = new KeywordAction();
        }
        if (mKeywordAction.getType() != null &&
            mKeywordAction.getType()==KeywordAction.Type.POLL) {
            //int idPoll = getIntent().getIntExtra(FrontlineSMS.EXTRA_POLL_ID, -1);
            //Log.d("onClick", "onCreate.idPoll: " + idPoll);

            mPoll = mPollDao.getPollByKeywordActionId(mKeywordAction.getDbId());
//            if (idPoll!=-1) {
//                mPoll = mPollDao.getPollById(idPoll);
//            } else {
//                mPoll = new Poll();
//            }
        } else {
            mPoll = new Poll();
        }
        initView();
    }

    /**
     * Initialize views
     */
    private void initView() {
        mChkForward = ((CheckBox)findViewById(R.id.chk_forward));
        mChkPoll = ((CheckBox)findViewById(R.id.chk_poll));
        mChkReply = ((CheckBox)findViewById(R.id.chk_autoreply));
        mChkEmail = ((CheckBox)findViewById(R.id.chk_email));
        mChkHttpRequest = ((CheckBox)findViewById(R.id.chk_http_request));
        mChkAddToGroup = ((CheckBox)findViewById(R.id.chk_add_to_group));
        mChkRemoveFromGroup = ((CheckBox)findViewById(R.id.chk_remove_from_group));
        mSpinnerAddToGroup = ((Spinner)findViewById(R.id.spn_add_to_group));
        mSpinnerRemoveFromGroup = ((Spinner)findViewById(R.id.spn_remove_from_group));
        mSpinnerForwardToGroup = ((Spinner)findViewById(R.id.spn_forward_to_group));
        mSpinnerPollToGroup = ((Spinner)findViewById(R.id.spn_poll_to_group));
        mEdtTextReply = ((EditText)findViewById(R.id.edt_text_reply));
        mTxtTextReply = ((TextView)findViewById(R.id.txt_text_reply));
        mEdtTextForward = ((EditText) findViewById(R.id.edt_text_forward));
        mTxtTextForward = ((TextView)findViewById(R.id.txt_text_forward));
        mEdtTextPoll = ((EditText) findViewById(R.id.edt_text_poll));
        mTxtTextPoll = ((TextView)findViewById(R.id.txt_text_poll));
        mTxtTextPollResponse = ((TextView)findViewById(R.id.txt_text_poll_response));
        mEdtTextPollResponse1 = ((EditText) findViewById(R.id.edt_text_poll_response1));
        mEdtTextPollResponse2 = ((EditText) findViewById(R.id.edt_text_poll_response2));
        mEdtTextEmail = ((EditText)findViewById(R.id.edt_text_email));
        mTxtTextEmail = ((TextView)findViewById(R.id.txt_text_email));
        mEdtSubjectEmail = ((EditText)findViewById(R.id.edt_subject_email));
        mTxtSubjectEmail = ((TextView)findViewById(R.id.txt_subject_email));
        mEdtRecipientEmail = ((EditText)findViewById(R.id.edt_recipient_email));
        mTxtRecipientEmail = ((TextView)findViewById(R.id.txt_recipient_email));
        mEdtHttpUrl = ((EditText)findViewById(R.id.edt_http_url));
        mTxtTextHttpRequest = ((TextView)findViewById(R.id.txt_text_http_request));
        
        // group list
        if (PIMService.groupNameCache.isEmpty()) {
            PIMService.getGroupsCursor(getApplicationContext()).close();
        }
        ArrayList<String> groups = new ArrayList<String>();
        groups.add(getResources().getString(R.string.choose_group));
        int ix = 1;
        int groupListIndex = -1;

        for (Iterator iter=PIMService.groupNameCache.keySet().iterator();iter.hasNext();) {
            String g = PIMService.groupNameCache.get(iter.next());
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
        mSpinnerPollToGroup.setAdapter(spinnerArrayAdapter);

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
            mEdtTextForward.setText(mKeywordAction.getText());
        } else {
            mSpinnerRemoveFromGroup.setSelection(0);
        }
        if (mKeywordAction.getType()==KeywordAction.Type.POLL) {
            mSpinnerPollToGroup.setSelection(groupListIndex);
            mEdtTextPoll.setText(mKeywordAction.getText());
        } else {
            mSpinnerRemoveFromGroup.setSelection(0);
        }
        if (mKeywordAction.getType()==KeywordAction.Type.REPLY) {
            mEdtTextReply.setText(mKeywordAction.getText());
        }
        if (mKeywordAction.getType()==KeywordAction.Type.EMAIL) {
            mEdtTextEmail.setText(mKeywordAction.getText());
            mEdtSubjectEmail.setText(mKeywordAction.getSubject());
            mEdtRecipientEmail.setText(mKeywordAction.getRecipient());
        }
        if (mKeywordAction.getType()==KeywordAction.Type.HTTP_REQUEST) {
            mEdtHttpUrl.setText(mKeywordAction.getRecipient());
        }

        // set title
        if (mKeywordAction==null || mKeywordAction.getKeyword()==null) {
            ((TextView)findViewById(R.id.txt_header)).setText("New Keyword");
        } else {
            ((TextView)findViewById(R.id.txt_header)).setText("Keyword: " + mKeywordAction.getKeyword());
            ((TextView)findViewById(R.id.edt_keyword)).setText(mKeywordAction.getKeyword());
            ((TextView)findViewById(R.id.edt_description)).setText(mKeywordAction.getDescription());

            mChkForward.setChecked(mKeywordAction.getType()==KeywordAction.Type.FORWARD);
            mChkPoll.setChecked(mKeywordAction.getType()==KeywordAction.Type.POLL);
            mChkReply.setChecked(mKeywordAction.getType()==KeywordAction.Type.REPLY);
            mChkEmail.setChecked(mKeywordAction.getType()==KeywordAction.Type.EMAIL);
            mChkHttpRequest.setChecked(mKeywordAction.getType()==KeywordAction.Type.HTTP_REQUEST);
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
        mKeywordAction.setRecipient(null);
        mKeywordAction.setSubject(null);
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
            mKeywordAction.setText(mEdtTextReply.getText().toString());
        }
        else if (mChkForward.isChecked()) {
            mKeywordAction.setType(KeywordAction.Type.FORWARD);
            spinnerValue = ((Spinner) findViewById(R.id.spn_forward_to_group)).getSelectedItem().toString();
            if (getResources().getString(R.string.choose_group).equals(spinnerValue)) {
                 spinnerValue = null;
            }
            mKeywordAction.setGroup(spinnerValue);
            mKeywordAction.setText(mEdtTextForward.getText().toString());
        }
        else if (mChkEmail.isChecked()) {
            mKeywordAction.setType(KeywordAction.Type.EMAIL);
            mKeywordAction.setText(mEdtTextEmail.getText().toString());
            mKeywordAction.setSubject(mEdtSubjectEmail.getText().toString());
            mKeywordAction.setRecipient(mEdtRecipientEmail.getText().toString());
        }
        else if (mChkHttpRequest.isChecked()) {
            mKeywordAction.setType(KeywordAction.Type.HTTP_REQUEST);
            mKeywordAction.setRecipient(mEdtHttpUrl.getText().toString());
        }
        else if (mChkPoll.isChecked()) {
            //TODO: assign these values to mPoll also... decide if a poll should get a row in KeywordAction table ..
            //  give it a row for now, link on the keyword?
            mKeywordAction.setType(KeywordAction.Type.POLL);
            spinnerValue = ((Spinner) findViewById(R.id.spn_poll_to_group)).getSelectedItem().toString();
            if (getResources().getString(R.string.choose_group).equals(spinnerValue)) {
                spinnerValue = null;
            }
            mKeywordAction.setGroup(spinnerValue);
            mKeywordAction.setText(mEdtTextPoll.getText().toString());

            mPoll.setQuestion(mEdtTextPoll.getText().toString());
            mPoll.setKeyword(((EditText) findViewById(R.id.edt_keyword)).getText().toString());
            mPoll.setDaysToExpire(5);
        }
    }

    /**
     * Checks that only auto-reply or forward is checked at the same time.
     * @param v View (Checkbox)
     */
    public void checkCheckBoxLogic(View v) {
        if ((v==null || v.getId()==R.id.chk_forward) && mChkForward.isChecked()) {
            mChkEmail.setChecked(false);
            mChkHttpRequest.setChecked(false);
            mChkReply.setChecked(false);
            mChkAddToGroup.setChecked(false);
            mChkRemoveFromGroup.setChecked(false);
            mChkPoll.setChecked(false);

            mTxtTextEmail.setVisibility(View.GONE);
            mTxtSubjectEmail.setVisibility(View.GONE);
            mTxtTextHttpRequest.setVisibility(View.GONE);
            mTxtTextReply.setVisibility(View.GONE);
            mTxtTextEmail.setVisibility(View.GONE);
            mTxtTextForward.setVisibility(View.VISIBLE);
            mTxtTextPoll.setVisibility(View.GONE);
            mTxtRecipientEmail.setVisibility(View.GONE);

            mEdtTextReply.setVisibility(View.GONE);
            mEdtTextEmail.setVisibility(View.GONE);
            mEdtTextForward.setVisibility(View.VISIBLE);
            mEdtTextPoll.setVisibility(View.GONE);
            mEdtHttpUrl.setVisibility(View.GONE);
            mEdtSubjectEmail.setVisibility(View.GONE);
            mEdtRecipientEmail.setVisibility(View.GONE);

            mSpinnerAddToGroup.setVisibility(View.GONE);
            mSpinnerRemoveFromGroup.setVisibility(View.GONE);
            mSpinnerForwardToGroup.setVisibility(View.VISIBLE);
            mSpinnerPollToGroup.setVisibility(View.GONE);

            mTxtTextPollResponse.setVisibility(View.GONE);
            mEdtTextPollResponse1.setVisibility(View.GONE);
            mEdtTextPollResponse2.setVisibility(View.GONE);
        } else if ((v==null || v.getId()==R.id.chk_poll) && mChkPoll.isChecked()) {
            mChkEmail.setChecked(false);
            mChkHttpRequest.setChecked(false);
            mChkReply.setChecked(false);
            mChkAddToGroup.setChecked(false);
            mChkRemoveFromGroup.setChecked(false);
            mChkForward.setChecked(false);

            mTxtTextEmail.setVisibility(View.GONE);
            mTxtSubjectEmail.setVisibility(View.GONE);
            mTxtTextHttpRequest.setVisibility(View.GONE);
            mTxtTextReply.setVisibility(View.GONE);
            mTxtTextEmail.setVisibility(View.GONE);
            mTxtTextForward.setVisibility(View.GONE);
            mTxtTextPoll.setVisibility(View.VISIBLE);
            mTxtRecipientEmail.setVisibility(View.GONE);

            mEdtTextReply.setVisibility(View.GONE);
            mEdtTextEmail.setVisibility(View.GONE);
            mEdtTextForward.setVisibility(View.GONE);
            mEdtTextPoll.setVisibility(View.VISIBLE);
            mEdtHttpUrl.setVisibility(View.GONE);
            mEdtSubjectEmail.setVisibility(View.GONE);
            mEdtRecipientEmail.setVisibility(View.GONE);

            mSpinnerAddToGroup.setVisibility(View.GONE);
            mSpinnerRemoveFromGroup.setVisibility(View.GONE);
            mSpinnerForwardToGroup.setVisibility(View.GONE);
            mSpinnerPollToGroup.setVisibility(View.VISIBLE);

            mTxtTextPollResponse.setVisibility(View.VISIBLE);
            mEdtTextPollResponse1.setVisibility(View.VISIBLE);
            mEdtTextPollResponse2.setVisibility(View.VISIBLE);
        } else if ((v==null || v.getId()==R.id.chk_autoreply) && mChkReply.isChecked()) {
            mChkEmail.setChecked(false);
            mChkHttpRequest.setChecked(false);
            mChkForward.setChecked(false);
            mChkAddToGroup.setChecked(false);
            mChkRemoveFromGroup.setChecked(false);
            mChkPoll.setChecked(false);

            mTxtTextEmail.setVisibility(View.GONE);
            mTxtSubjectEmail.setVisibility(View.GONE);
            mTxtTextHttpRequest.setVisibility(View.GONE);
            mTxtTextReply.setVisibility(View.VISIBLE);
            mTxtTextForward.setVisibility(View.GONE);
            mTxtTextPoll.setVisibility(View.GONE);
            mTxtRecipientEmail.setVisibility(View.GONE);

            mEdtTextReply.setVisibility(View.VISIBLE);
            mEdtTextForward.setVisibility(View.GONE);
            mEdtTextPoll.setVisibility(View.GONE);
            mEdtTextEmail.setVisibility(View.GONE);
            mEdtHttpUrl.setVisibility(View.GONE);
            mEdtSubjectEmail.setVisibility(View.GONE);
            mEdtRecipientEmail.setVisibility(View.GONE);

            mSpinnerAddToGroup.setVisibility(View.GONE);
            mSpinnerRemoveFromGroup.setVisibility(View.GONE);
            mSpinnerForwardToGroup.setVisibility(View.GONE);
            mSpinnerPollToGroup.setVisibility(View.GONE);

            mTxtTextPollResponse.setVisibility(View.GONE);
            mEdtTextPollResponse1.setVisibility(View.GONE);
            mEdtTextPollResponse2.setVisibility(View.GONE);
        } else if ((v==null || v.getId()==R.id.chk_add_to_group) && mChkAddToGroup.isChecked()) {
            mChkEmail.setChecked(false);
            mChkHttpRequest.setChecked(false);
            mChkForward.setChecked(false);
            mChkReply.setChecked(false);
            mChkRemoveFromGroup.setChecked(false);
            mChkPoll.setChecked(false);

            mTxtTextEmail.setVisibility(View.GONE);
            mTxtSubjectEmail.setVisibility(View.GONE);
            mTxtTextHttpRequest.setVisibility(View.GONE);
            mTxtTextForward.setVisibility(View.GONE);
            mTxtTextPoll.setVisibility(View.GONE);
            mTxtTextReply.setVisibility(View.GONE);
            mTxtRecipientEmail.setVisibility(View.GONE);

            mEdtTextReply.setVisibility(View.GONE);
            mEdtTextForward.setVisibility(View.GONE);
            mEdtTextPoll.setVisibility(View.GONE);
            mEdtTextEmail.setVisibility(View.GONE);
            mEdtHttpUrl.setVisibility(View.GONE);
            mEdtSubjectEmail.setVisibility(View.GONE);
            mEdtRecipientEmail.setVisibility(View.GONE);

            mSpinnerAddToGroup.setVisibility(View.VISIBLE);
            mSpinnerRemoveFromGroup.setVisibility(View.GONE);
            mSpinnerForwardToGroup.setVisibility(View.GONE);
            mSpinnerPollToGroup.setVisibility(View.GONE);

            mTxtTextPollResponse.setVisibility(View.GONE);
            mEdtTextPollResponse1.setVisibility(View.GONE);
            mEdtTextPollResponse2.setVisibility(View.GONE);
        } else if ((v==null || v.getId()==R.id.chk_remove_from_group) && mChkRemoveFromGroup.isChecked()) {
            mChkEmail.setChecked(false);
            mChkHttpRequest.setChecked(false);
            mChkForward.setChecked(false);
            mChkReply.setChecked(false);
            mChkAddToGroup.setChecked(false);
            mChkPoll.setChecked(false);

            mTxtTextEmail.setVisibility(View.GONE);
            mTxtSubjectEmail.setVisibility(View.GONE);
            mTxtTextHttpRequest.setVisibility(View.GONE);
            mTxtTextReply.setVisibility(View.GONE);
            mTxtTextForward.setVisibility(View.GONE);
            mTxtTextPoll.setVisibility(View.GONE);
            mTxtRecipientEmail.setVisibility(View.GONE);

            mEdtTextReply.setVisibility(View.GONE);
            mEdtTextForward.setVisibility(View.GONE);
            mEdtTextPoll.setVisibility(View.GONE);
            mEdtHttpUrl.setVisibility(View.GONE);
            mEdtTextEmail.setVisibility(View.GONE);
            mEdtSubjectEmail.setVisibility(View.GONE);
            mEdtRecipientEmail.setVisibility(View.GONE);

            mSpinnerAddToGroup.setVisibility(View.GONE);
            mSpinnerRemoveFromGroup.setVisibility(View.VISIBLE);
            mSpinnerForwardToGroup.setVisibility(View.GONE);
            mSpinnerPollToGroup.setVisibility(View.GONE);

            mTxtTextPollResponse.setVisibility(View.GONE);
            mEdtTextPollResponse1.setVisibility(View.GONE);
            mEdtTextPollResponse2.setVisibility(View.GONE);
        } else if ((v==null || v.getId()==R.id.chk_email) && mChkEmail.isChecked()) {
            mChkEmail.setChecked(true);
            mChkHttpRequest.setChecked(false);
            mChkForward.setChecked(false);
            mChkReply.setChecked(false);
            mChkAddToGroup.setChecked(false);
            mChkPoll.setChecked(false);

            mTxtTextEmail.setVisibility(View.VISIBLE);
            mTxtSubjectEmail.setVisibility(View.VISIBLE);
            mTxtTextHttpRequest.setVisibility(View.GONE);
            mTxtTextReply.setVisibility(View.GONE);
            mTxtTextForward.setVisibility(View.GONE);
            mTxtTextPoll.setVisibility(View.GONE);
            mTxtRecipientEmail.setVisibility(View.VISIBLE);

            mEdtTextReply.setVisibility(View.GONE);
            mEdtTextForward.setVisibility(View.GONE);
            mEdtTextPoll.setVisibility(View.GONE);
            mEdtHttpUrl.setVisibility(View.GONE);
            mEdtTextEmail.setVisibility(View.VISIBLE);
            mEdtSubjectEmail.setVisibility(View.VISIBLE);
            mEdtRecipientEmail.setVisibility(View.VISIBLE);

            mSpinnerAddToGroup.setVisibility(View.GONE);
            mSpinnerRemoveFromGroup.setVisibility(View.GONE);
            mSpinnerForwardToGroup.setVisibility(View.GONE);
            mSpinnerPollToGroup.setVisibility(View.GONE);

            mTxtTextPollResponse.setVisibility(View.GONE);
            mEdtTextPollResponse1.setVisibility(View.GONE);
            mEdtTextPollResponse2.setVisibility(View.GONE);
        } else if ((v==null || v.getId()==R.id.chk_http_request) && mChkHttpRequest.isChecked()) {
            mChkEmail.setChecked(false);
            mChkHttpRequest.setChecked(true);
            mChkForward.setChecked(false);
            mChkReply.setChecked(false);
            mChkAddToGroup.setChecked(false);
            mChkPoll.setChecked(false);

            mTxtTextEmail.setVisibility(View.GONE);
            mTxtSubjectEmail.setVisibility(View.GONE);
            mTxtTextHttpRequest.setVisibility(View.VISIBLE);
            mTxtTextReply.setVisibility(View.GONE);
            mTxtTextForward.setVisibility(View.GONE);
            mTxtTextPoll.setVisibility(View.GONE);
            mTxtRecipientEmail.setVisibility(View.GONE);

            mEdtHttpUrl.setVisibility(View.VISIBLE);
            mEdtTextReply.setVisibility(View.GONE);
            mEdtTextForward.setVisibility(View.GONE);
            mEdtTextPoll.setVisibility(View.GONE);
            mEdtTextEmail.setVisibility(View.GONE);
            mEdtSubjectEmail.setVisibility(View.GONE);
            mEdtRecipientEmail.setVisibility(View.GONE);

            mSpinnerAddToGroup.setVisibility(View.GONE);
            mSpinnerRemoveFromGroup.setVisibility(View.GONE);
            mSpinnerForwardToGroup.setVisibility(View.GONE);
            mSpinnerPollToGroup.setVisibility(View.GONE);

            mTxtTextPollResponse.setVisibility(View.GONE);
            mEdtTextPollResponse1.setVisibility(View.GONE);
            mEdtTextPollResponse2.setVisibility(View.GONE);
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
                    if (mKeywordAction.getType() == KeywordAction.Type.POLL) {
                        Log.d("onClick", "mPoll.id before: " + mPoll.getDbId());
                        if (mPoll.getKeywordActionId() == null) {
                            mPoll.setKeywordActionId(mKeywordAction.getDbId());
                        }
                        mPollDao.saveOrUpdatePoll(mPoll);
                        Log.d("onClick", "mPoll.id after: " + mPoll.getDbId());
                    }
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
