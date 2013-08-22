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

import net.frontlinesms.android.FrontlineSMS;
import net.frontlinesms.android.model.AnalyticsService;
import net.frontlinesms.android.search.SuggestionProvider;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * @author Mathias Lin <mathias.lin@metahealthcare.com>
 */
public abstract class BaseActivity extends Activity {

	private static Bundle appDataBundle;

	protected String TAG = getClass().getSimpleName();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		trackAnalytics();

        if (getIntent()!=null) Log.d(TAG, "getIntent.getAction(): " + getIntent().getAction());

        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
			String query = getIntent().getStringExtra(SearchManager.QUERY);
			SearchRecentSuggestions suggestions = new SearchRecentSuggestions(
					this, SuggestionProvider.AUTHORITY,
					SuggestionProvider.MODE);
			suggestions.saveRecentQuery(query, null);

            // if user selected suggestion from suggestion list, rather than pressing the
            // search icon
            if (query==null && getIntent().getDataString()!=null) {
                query = getIntent().getDataString();
            }

			if (query!=null) {
				final Intent i = new Intent(this, MessageList.class);
				i.putExtra(FrontlineSMS.EXTRA_SEARCH_QUERY, query);
				startActivity(i);
                finish();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (getIntent()!=null) Log.d(TAG, "getIntent.getAction(): " + getIntent().getAction());

        // if (!this.getClass().equals(Dashboard.class) || getIntent()==null)

		/*if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
			String query = getIntent().getStringExtra(SearchManager.QUERY);
			SearchRecentSuggestions suggestions = new SearchRecentSuggestions(
					this, SuggestionProvider.AUTHORITY,
					SuggestionProvider.MODE);
			suggestions.saveRecentQuery(query, null);
			Toast.makeText(this, "saving query: " + query, Toast.LENGTH_LONG).show();
			//            query = intent.getDataString();

			if (query!=null) {
				final Intent i = new Intent(this, MessageList.class);
				i.putExtra(FrontlineSMS.EXTRA_SEARCH_QUERY, query);
				startActivity(i);
			}
		}*/

	}

    @Override
    protected void onNewIntent(Intent intent) {
        // super.onNewIntent(intent);
        if (getIntent()!=null) Log.d(TAG, "onNewIntent.getIntent.getAction(): " + getIntent().getAction());
        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
			String query = getIntent().getStringExtra(SearchManager.QUERY);
			SearchRecentSuggestions suggestions = new SearchRecentSuggestions(
					this, SuggestionProvider.AUTHORITY,
					SuggestionProvider.MODE);
			suggestions.saveRecentQuery(query, null);
			Toast.makeText(this, "saving query: " + query, Toast.LENGTH_LONG).show();
			//            query = intent.getDataString();

			if (query!=null) {
				final Intent i = new Intent(this, MessageList.class);
				i.putExtra(FrontlineSMS.EXTRA_SEARCH_QUERY, query);
				startActivity(i);
			}
		}

    }

    /**
	 * Returns to dashboard, after user clicked the action bar anywhere.
	 * @param v Clicked view
	 */
	public void goHome(View v) {
		startActivity(new Intent(this, Dashboard.class));
	}

	/**
	 * Based on the user preferences, the application tracks the
	 * visited activity.
	 */
	private void trackAnalytics() {
		SharedPreferences mySharedPreferences = getSharedPreferences(FrontlineSMS.SHARED_PREFS_ID, Activity.MODE_PRIVATE);
		if (mySharedPreferences.getBoolean(FrontlineSMS.PREF_SETTINGS_ALLOW_ANALYTICS, false)) {
			AnalyticsService.startAnalyticsManager(this);
			AnalyticsService.trackPageView(TAG, this);
			AnalyticsService.dispatchAnalytics();
		}
	}

}
