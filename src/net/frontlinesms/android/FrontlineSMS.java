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
package net.frontlinesms.android;

import android.app.Application;
import android.content.Intent;
import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

// https://spreadsheets0.google.com/viewform?formkey=dHFwTUN4MFdkZ3lnZjZIcWktZjVwcXc6MQ

@ReportsCrashes(formKey = "dHFwTUN4MFdkZ3lnZjZIcWktZjVwcXc6MQ")
public class FrontlineSMS extends Application {

    // intent extras
    public static final String EXTRA_SEARCH_QUERY = "EXTRA_SEARCH_QUERY";
    public static final String EXTRA_GROUP_NAME = "EXTRA_GROUP_NAME";
    public static final String EXTRA_GROUP_ID = "EXTRA_GROUP_ID";
    public static final String EXTRA_SELECTED_ITEMS = "EXTRA_SELECTED_ITEMS";
    public static final String EXTRA_RECIPIENT_TYPE = "EXTRA_RECIPIENT_TYPE";
    public static final String EXTRA_KEYWORD_ID = "EXTRA_KEYWORD_ID";
    public static final String EXTRA_KEYWORD_KEYWORD = "EXTRA_KEYWORD_KEYWORD";

    // shared prefs
    public static final String SHARED_PREFS_ID = "FRONTLINESMS_SETTINGS";
    public static final String PREF_SETTINGS_LANGUAGE = "Settings.Language";
    public static final String PREF_SETTINGS_EMAIL_SERVER = "Settings.Email.Server";
    public static final String PREF_SETTINGS_EMAIL_PORT = "Settings.Email.Port";
    public static final String PREF_SETTINGS_EMAIL_USERNAME = "Settings.Email.Username";
    public static final String PREF_SETTINGS_EMAIL_PASSWORD = "Settings.Email.Password";
    public static final String PREF_SETTINGS_EMAIL_SSL = "Settings.Email.SSL";
    public static final String PREF_SETTINGS_EMAIL_SENDER = "Settings.Email.Sender";
    public static final String PREF_SETTINGS_ALLOW_ANALYTICS = "Settings.Analytics.Permission";
    public static final String PREF_SETTINGS_ALLOW_KEYWORD_ANYWHERE = "Settings.Keywords.Anywhere";
    public static final String PREF_SETTINGS_DELAY = "Settings.Delay";

    @Override
    public void onCreate() {
        // The following line triggers the initialization of ACRA
        ACRA.init(this);
        startService(new Intent(this, FrontlineSMSService.class));
        super.onCreate();
    }

}
