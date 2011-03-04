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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import net.frontlinesms.android.R;

public class Dashboard extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_view);
    }

    /**
     * Handles click on dashboard icon.
     * @param v Clicked icon.
     */
    public void onClickHandler(final View v) {
        switch (v.getId()) {
            case R.id.img_groups:
                startActivity(new Intent(this, GroupList.class));
                break;
            case R.id.img_messages:
                startActivity(new Intent(this, MessageList.class));
                break;
            // ...
            default:
                Toast.makeText(this, "Clicked!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
