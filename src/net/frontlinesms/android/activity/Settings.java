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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import net.frontlinesms.android.FrontlineSMS;
import net.frontlinesms.android.R;

/**
 * @author Mathias Lin <mathias.lin@metahealthcare.com>
 */
public class Settings extends BaseActivity {

    private SharedPreferences mySharedPreferences;
    private Spinner mSpnLanguage;
    private EditText mEdtDelay;
    private EditText mEdtEmailServer;
    private EditText mEdtEmailPort;
    private EditText mEdtEmailUsername;
    private EditText mEdtEmailPassword;
    private EditText mEdtEmailSender;
    private CheckBox mChkEmailSSL;
    private CheckBox mChkAnalytics;
    private CheckBox mChkKeywordAnywhere;

    /** Menu item to save / cancel operation. */
    private static final int MENU_OPTION_SAVE = Menu.FIRST;
    private static final int MENU_OPTION_CANCEL = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        mySharedPreferences = getSharedPreferences(FrontlineSMS.SHARED_PREFS_ID, Activity.MODE_PRIVATE);
        mSpnLanguage = (Spinner) findViewById(R.id.spn_language);
        mEdtDelay = (EditText) findViewById(R.id.edt_delay);
        mEdtEmailServer = (EditText) findViewById(R.id.edt_email_server);
        mEdtEmailPort = (EditText) findViewById(R.id.edt_email_port);
        mEdtEmailSender = (EditText) findViewById(R.id.edt_email_sender);
        mEdtEmailUsername = (EditText) findViewById(R.id.edt_email_username);
        mEdtEmailPassword = (EditText) findViewById(R.id.edt_email_password);
        mChkEmailSSL = (CheckBox) findViewById(R.id.chk_email_ssl);
        mChkAnalytics = (CheckBox) findViewById(R.id.chk_analytics);
        mChkKeywordAnywhere = (CheckBox) findViewById(R.id.chk_keywords_anywhere);
        loadPreferences();
    }

    private void loadPreferences() {
        // language
        mSpnLanguage.setSelection(mySharedPreferences.getInt(FrontlineSMS.PREF_SETTINGS_LANGUAGE, 0));
        // email settings
        mEdtEmailServer.setText(mySharedPreferences.getString(FrontlineSMS.PREF_SETTINGS_EMAIL_SERVER, ""));
        mEdtEmailUsername.setText(mySharedPreferences.getString(FrontlineSMS.PREF_SETTINGS_EMAIL_USERNAME,""));
        mEdtEmailSender.setText(mySharedPreferences.getString(FrontlineSMS.PREF_SETTINGS_EMAIL_SENDER,""));
        mEdtEmailPassword.setText(mySharedPreferences.getString(FrontlineSMS.PREF_SETTINGS_EMAIL_PASSWORD,""));
        mEdtEmailPort.setText(mySharedPreferences.getString(FrontlineSMS.PREF_SETTINGS_EMAIL_PORT,""));
        mChkEmailSSL.setChecked(mySharedPreferences.getBoolean(FrontlineSMS.PREF_SETTINGS_EMAIL_SSL, false));
        mChkAnalytics.setChecked(mySharedPreferences.getBoolean(FrontlineSMS.PREF_SETTINGS_ALLOW_ANALYTICS, false));
        mChkKeywordAnywhere.setChecked(mySharedPreferences.getBoolean(FrontlineSMS.PREF_SETTINGS_ALLOW_KEYWORD_ANYWHERE, false));
        mEdtDelay.setText(new Integer(mySharedPreferences.getInt(FrontlineSMS.PREF_SETTINGS_DELAY,0)).toString());
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        // language
        editor.putInt(FrontlineSMS.PREF_SETTINGS_LANGUAGE, mSpnLanguage.getSelectedItemPosition());
        // email settings
        editor.putString(FrontlineSMS.PREF_SETTINGS_EMAIL_SERVER, mEdtEmailServer.getText().toString());
        editor.putString(FrontlineSMS.PREF_SETTINGS_EMAIL_USERNAME, mEdtEmailUsername.getText().toString());
        editor.putString(FrontlineSMS.PREF_SETTINGS_EMAIL_SENDER, mEdtEmailSender.getText().toString());
        editor.putString(FrontlineSMS.PREF_SETTINGS_EMAIL_PASSWORD, mEdtEmailPassword.getText().toString());
        editor.putString(FrontlineSMS.PREF_SETTINGS_EMAIL_PORT, mEdtEmailPort.getText().toString());
        editor.putBoolean(FrontlineSMS.PREF_SETTINGS_EMAIL_SSL, mChkEmailSSL.isChecked());
        editor.putBoolean(FrontlineSMS.PREF_SETTINGS_ALLOW_ANALYTICS, mChkAnalytics.isChecked());
        editor.putBoolean(FrontlineSMS.PREF_SETTINGS_ALLOW_KEYWORD_ANYWHERE, mChkKeywordAnywhere.isChecked());
        editor.putInt(FrontlineSMS.PREF_SETTINGS_DELAY, Integer.valueOf(mEdtDelay.getText().toString()));
        editor.commit();
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
                    savePreferences();
                    Toast.makeText(this, "Settings saved.", Toast.LENGTH_SHORT).show();
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
