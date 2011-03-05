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
import android.provider.BaseColumns;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import net.frontlinesms.android.R;

/**
 * @author Mathias Lin <mathias.lin@metahealthcare.com>
 */
public final class MessageList extends BaseActivity {

    public static final String TAG = GroupList.class.getSimpleName();
    private ListView mMessageList;

    /**
     * The column of the phone data retriever that contains the body of the
     * message.
     */
    public static final String BODY_COLUMN = "body";

    /**
     * The column of the phone data retriever that contains the date the message
     * that was received.
     */
    public static final String DATE_COLUMN = "date";

    /**
     * The column of the phone data retriever that contains the id of the
     * conversation the message belongs to.
     */
    public static final String THREAD_ID_COLUMN = "thread_id";

    public static final String TYPE_COLUMN = "type";

    /**
     * The column in the phone data retriever that contains the number the
     * message was received from.
     */
    public static final String NUMBER_COLUMN = "address";

    /** URI for all inbound and outbound sms */
    private static final Uri SMS_URI = Uri.parse("content://sms/");
    private static final String[] PROJECTION = new String[] { DATE_COLUMN, BODY_COLUMN,
            NUMBER_COLUMN, THREAD_ID_COLUMN, BaseColumns._ID, TYPE_COLUMN};

    // see all fields: http://stackoverflow.com/questions/4022088/how-many-database-columns-associated-with-a-sms-in-android


    /**
     * Called when the activity is first created. Responsible for initializing the UI.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_list);

        // Obtain handles to UI objects
        mMessageList = (ListView) findViewById(R.id.lst_messages);

        // Populate the contact list
        initGroupList();
    }

    /**
     * Populates the message list.
     */
    private void initGroupList() {

        // List header
        TextView txtHeader = new TextView(this);
        txtHeader.setLayoutParams(
                new ListView.LayoutParams(
                        ListView.LayoutParams.FILL_PARENT,
                        ListView.LayoutParams.WRAP_CONTENT));
        txtHeader.setText("Messages");
        txtHeader.setPadding(10, 10, 10, 10);
        txtHeader.setBackgroundColor(Color.parseColor("#8E0052"));
        txtHeader.setTextColor(Color.WHITE);
        mMessageList.addHeaderView(txtHeader);

        // Build adapter with contact entries
        Cursor cursor = getMessages();
        String[] fields = new String[] {
                BODY_COLUMN,
                NUMBER_COLUMN,
                // TYPE_COLUMN,   // 1 = inbound, 2 = outbound
        };
//        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.message_list_item, cursor,
//                fields, new int[] {R.id.txt_title, R.id.txt_mobile});
        MessageListAdapter adapter = new MessageListAdapter(this, cursor);
        mMessageList.setAdapter(adapter);
    }

    /**
     * Obtains the message list for the currently selected account.
     *
     * @return A cursor for for accessing the sms list.
     */
    private Cursor getMessages()
    {
        Cursor cursor = getContentResolver().query(SMS_URI, PROJECTION, null, null,
                null);
        return cursor;
    }


}