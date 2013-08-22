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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import net.frontlinesms.android.R;

public class Dashboard extends BaseActivity {

    private static final int MENU_OPTION_INFO = Menu.FIRST;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
    }

    /**
     * Handles click on dashboard icon.
     * @param v Clicked icon.
     */
    public void onClickHandler(final View v) {
        switch (v.getId()) {
            case R.id.ll_groups:
            case R.id.img_groups:
                startActivity(new Intent(this, GroupList.class));
                break;
            case R.id.ll_messages:
            case R.id.img_messages:
                startActivity(new Intent(this, MessageList.class));
                break;
            case R.id.img_search:
                onSearchRequested();
                break;
            case R.id.ll_logs:
            case R.id.img_log:
                startActivity(new Intent(this, JobList.class));
                break;
            case R.id.img_settings:
                startActivity(new Intent(this, Settings.class));
                break;
            case R.id.img_rules:
                startActivity(new Intent(this, KeywordList.class));
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem menuItem = menu.add(MENU_OPTION_INFO, MENU_OPTION_INFO, Menu.NONE, R.string.menu_option_info);
        menuItem.setIcon(android.R.drawable.ic_menu_info_details);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case MENU_OPTION_INFO:
                showInfoDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showInfoDialog() {
        new AlertDialog.Builder(this)
            .setIcon(R.drawable.icon)
            .setTitle("About FrontlineSMS for Android")
            .setMessage("This is an early alpha release of FrontlineSMS for Android, not intended to be used in production.\n" +
                "If you experience any problems, please report them at https://github.com/mathiaslin/frontlinesms-for-android/issues\n\n" +
                "Mathias Lin\nmathias.lin@metahealthcare.com")
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            })
            .create()
            .show();
    }
}
