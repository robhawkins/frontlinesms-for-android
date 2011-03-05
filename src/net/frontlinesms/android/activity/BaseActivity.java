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
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.widget.Toast;
import net.frontlinesms.android.FrontlineSMS;
import net.frontlinesms.android.R;
import net.frontlinesms.android.search.SuggestionProvider;

/**
 * @author Mathias Lin <mathias.lin@metahealthcare.com>
 */
public abstract class BaseActivity extends Activity {

    private static Bundle appDataBundle;

    private String TAG = getClass().getSimpleName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getIntent()!=null) Log.d(TAG, "getIntent.getAction(): " + getIntent().getAction());

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


}
