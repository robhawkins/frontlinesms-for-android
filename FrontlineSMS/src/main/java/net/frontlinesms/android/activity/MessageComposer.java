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

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import net.frontlinesms.android.FrontlineSMS;
import net.frontlinesms.android.R;
import net.frontlinesms.android.model.Contact;
import net.frontlinesms.android.model.PIMService;
import net.frontlinesms.android.model.SmsService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mathias Lin <mathias.lin@metahealthcare.com>
 */
public class MessageComposer extends BaseActivity {

    public final static int RECIPIENT_TYPE_GROUPS = 1;
    public final static int RECIPIENT_TYPE_CONTACTS = 2;

    private List<Contact> mContacts = new ArrayList<Contact>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_composer);

        // get recipient groups
        ArrayList<Integer> recipientIds = (ArrayList<Integer>) getIntent().
                getSerializableExtra(FrontlineSMS.EXTRA_SELECTED_ITEMS);

        // if null, it's after sms message sending is done
        Log.d(TAG, "recipientIds: " + recipientIds);
        if (recipientIds==null) {
            Toast.makeText(this, "Message has been sent.", Toast.LENGTH_SHORT).show();
            finish();
        }

        String recipients = "";
        if (getIntent().getIntExtra(FrontlineSMS.EXTRA_RECIPIENT_TYPE, RECIPIENT_TYPE_GROUPS)
                == RECIPIENT_TYPE_GROUPS) {

            if (recipientIds!=null) {
                for (Integer id:recipientIds) {
                    recipients += (!"".equals(recipients)?", ":"") + PIMService.groupNameCache.get(id);
                    mContacts.addAll(PIMService.getContactsByGroup(this, id));
                }
            }
            Log.d(TAG, "recipientIds for groups: " + recipients);
        } else {
            Cursor c = PIMService.getContactsCursorById(this, recipientIds.toArray(new Integer[recipientIds.size()]));
            if (c.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    contact.setId(c.getInt(0));
                    contact.setDisplayName(c.getString(1));
                    mContacts.add(contact);
                    recipients += (!"".equals(recipients)?", ":"") + contact.getDisplayName();
                } while (c.moveToNext());
            }
            c.close();
            Log.d(TAG, "recipientIds for contacts: " + recipients);
        }
        ((TextView) findViewById(R.id.txt_recipients)).setText(recipients);
        findViewById(R.id.btn_send_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object... objects) {
                        SmsService.individualizeAndSendMessage(getApplicationContext(),
                                mContacts, ((EditText) findViewById(R.id.edt_message)).getText().toString(),
                                null, null);
                        return null;
                    }

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        Toast.makeText(MessageComposer.this, "Message(s) sent.", Toast.LENGTH_SHORT);
                        finish();
                    }
                }.execute();

            }
        });
    }


}
