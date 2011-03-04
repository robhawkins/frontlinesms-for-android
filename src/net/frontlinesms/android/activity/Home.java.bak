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
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import net.frontlinesms.android.R;
import net.frontlinesms.android.data.model.Event;
import net.frontlinesms.android.ui.view.ActionBar;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * @author Mathias Lin <mathias.lin@metahealthcare.com>
 */
public class Home extends BaseActivity implements
        AdapterView.OnItemClickListener {

    static final private int MENU_OPTION_CLEAR = Menu.FIRST;
    static final private int MENU_OPTION_SETTINGS = 2;

    /** list view holding the latest events */
    private ListView mLstEvents;

    /** menu option to clear the event list */
    private MenuItem mMenuItemClear;

    /** menu option to enter settings screen */
    private MenuItem mMenuItemSettings;

    /**
     * Called when the activity is created.
     * @param savedInstanceState Saved activity state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        // set action bar handler
        ((ActionBar)findViewById(R.id.actionbar)).
                setOnClickHandler(new HomeActionBarHandler());

        // initialize event list
        mLstEvents = (ListView) findViewById(R.id.lst_events);
        mLstEvents.setOnItemClickListener(this);
        initEventList();
    }

    /**
     * Loads the latest events into the list view.
     */
    private void initEventList() {
        ArrayList<Event> events = new ArrayList<Event>(1);

        // List header
        TextView txtHeader = new TextView(this);
        txtHeader.setLayoutParams(
                new ListView.LayoutParams(
                        ListView.LayoutParams.FILL_PARENT,
                        ListView.LayoutParams.WRAP_CONTENT));
        txtHeader.setText("Latest Events");
        txtHeader.setPadding(10, 10, 10, 10);
        txtHeader.setBackgroundColor(Color.parseColor("#8E0052"));
        txtHeader.setTextColor(Color.WHITE);
        mLstEvents.addHeaderView(txtHeader);

        // add albums to list adapter
        for (int i=1;i<30;i++) {
            Event event = new Event();
            event.setId(i);
            event.setDescription("Sample Event Description");
            event.setTimestamp(new Timestamp(Calendar.getInstance().getTimeInMillis()));
            event.setEventType(Event.EventType.SMS_INCOMING);
            events.add(event);
        }
        ArrayAdapter<Event> adapter = new EventListAdapter(this, events);
        mLstEvents.setAdapter(adapter);

    }

    /**
     * Handles click on a list item, an event.
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // TODO do something
    }

    /**
     * Handle the main menu click for contacts, keywords and messages.
     * @param v Button clicked
     */
    public void onClickHandler(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_contacts:
                intent = new Intent(this, ContactList.class);
                break;
            case R.id.btn_keywords:
                intent = new Intent(this, Keywords.class);
                break;
            case R.id.btn_messages:
                intent = new Intent(this, Messages.class);
                break;
            default:
                intent = null;
                break;
        }
        if (intent!=null) {
            startActivity(intent);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        int groupId = 0;
        int menuItemOrder = Menu.NONE;
        mMenuItemClear = menu.add(groupId, MENU_OPTION_CLEAR, menuItemOrder, R.string.menu_option_clear);
        mMenuItemSettings = menu.add(groupId, MENU_OPTION_SETTINGS, menuItemOrder, R.string.menu_option_settings);
        return true;
    }

    /**
     * Handle the options menu actions, clearing the event list and
     * entering the settings menu.
     * @param item Selected menu option
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == MENU_OPTION_SETTINGS) {
            startActivity(new Intent(this, Settings.class));
        } else if (itemId == MENU_OPTION_CLEAR) {
            // mLstEvents.getAdapter();
        }
        return super.onMenuItemSelected(itemId, item);
    }


    /**
     * ActionBarHandler
     */
    public final class HomeActionBarHandler extends Handler {

        /**
         * Handles the message
         * @param msg msg.what contains the view id of the clicked view (button or logo)
         */
        @Override
        public void handleMessage(final Message msg) {
            String s = "";
            switch (msg.what) {
                case R.id.actionbar_button_1:
                    Home.this.onSearchRequested();
                    break;
            }
        }
    }

}
