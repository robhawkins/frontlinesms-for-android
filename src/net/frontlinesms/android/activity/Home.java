/**
 * This software is written by Meta Healthcare Systems Ltd. and subject
 * to a contract between Meta Healthcare Systems and its customer.
 * <p/>
 * This software stays property of Meta Healthcare Systems unless differing
 * arrangements between Meta Healthcare Systems and its customer apply.
 * <p/>
 * Meta Healthcare Systems Ltd.
 * 20/F Central Tower
 * 28 Queen's Road Central
 * Hong Kong
 * <p/>
 * Tel: +852 8199 9605
 * http://www.metahealthcare.com
 * mailto:info@metahealthcare.com
 * <p/>
 * (c)2010 Meta Healthcare Systems Ltd. All rights reserved.
 */
package net.frontlinesms.android.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import net.frontlinesms.android.R;
import net.frontlinesms.android.data.model.Event;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

public class Home extends BaseActivity implements
        AdapterView.OnItemClickListener {

    ListView mLstEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        mLstEvents = (ListView) findViewById(R.id.lst_events);
        mLstEvents.setOnItemClickListener(this);
        initEventList();
    }

    private void initEventList() {
        ArrayList<Event> events = new ArrayList<Event>(1);

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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // TODO do somethin
    }
}
