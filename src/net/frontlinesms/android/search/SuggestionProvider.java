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
package net.frontlinesms.android.search;

import android.app.SearchManager;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * This suggestion provider provides suggestions for the user search in the
 * native search field.
 *
 * @author Mathias Lin <mathias.lin@metahealthcare.com>
 */
public final class SuggestionProvider extends SearchRecentSuggestionsProvider {

    /**
     * Android search authority.
     */
    public static final String AUTHORITY = "net.frontlinesms.android.search.SuggestionProvider";

    /**
     * Search mode.
     */
    public static final int MODE = DATABASE_MODE_QUERIES;

    /**
     * Logging.
     */
    private static final String TAG = SuggestionProvider.class.getSimpleName();


    /**
     * Default constructor.
     */
    public SuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

    @Override
    public Cursor query(final Uri uri, final String[] projection, final String selection,
                        final String[] selectionArgs, final String sortOrder) {

        final StringBuilder queryStringBuilder = new StringBuilder();
        for (final String selectionArg : selectionArgs) {
            queryStringBuilder.append(selectionArg);
        }
        String query = queryStringBuilder.toString().toLowerCase();

        Log.d(TAG, "selection    : " + selection);
        Log.d(TAG, "selectionArgs: " + Arrays.toString(selectionArgs));
        Log.d(TAG, "query        : " + query);

        // if no query entered, show history
        // edit: also, according to the "meta logic", when the query ends with a space,
        // there can never be a suggestion returned
        if (query.length() == 0 || query.endsWith(" ")) {
            /*Cursor c = super.query(uri, projection, selection, selectionArgs, sortOrder);;
            for (int i=0;i<c.getColumnCount();i++) {
                Log.d("CURSOR", c.getColumnName(i));
            }*/
            return super.query(uri, projection, selection, selectionArgs, sortOrder);
        } else {
            final MatrixCursor matrixCursor = new MatrixCursor(new String[]{
                    SearchManager.SUGGEST_COLUMN_FORMAT, SearchManager.SUGGEST_COLUMN_TEXT_1,
                    SearchManager.SUGGEST_COLUMN_INTENT_DATA, BaseColumns._ID});

//            if (ShopPrototype.isOnlineSearchSuggestionsEnabled()) {
//                final List<String> results = VodafoneApi.search(query);
//                for (final String res : results) {
//                    matrixCursor.addRow(new Object[]{
//                            res, res, res, new Random().nextInt(MAX_RANDOM_ID)
//                    });
//                }
//            } else {
//                final ContentList contentList = ContentList.getSample(Settings
//                        .readCategory(getContext()));
//                contentList.sort();
//
//                // show regular suggestions
//                for (int i = 0; i < contentList.getLength(); i++) {
//                    final Content tmpContent = contentList.getContent(i);
//                    final String title = tmpContent.getTitle();
//                    if (title.toLowerCase().startsWith(query)) {
//                        matrixCursor.addRow(new Object[]{
//                                title, title, title, (long) tmpContent.getId()
//                        });
//                    }
//
//                }
//            }

            // merging history and suggestions cursor to show both in the search bar
            return new MergeCursor(
                    new Cursor[]{
                            super.query(uri, projection, selection, selectionArgs, sortOrder),
                            matrixCursor
                    }
            );
        }
    }
}
