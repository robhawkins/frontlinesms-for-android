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
import net.frontlinesms.android.activity.MessageList;

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
        if (query.length() == 0) {
            return super.query(uri, projection, selection, selectionArgs, sortOrder);
        } else {

            Log.d(TAG, "put matrix cursor together....");

            final MatrixCursor matrixCursor = new MatrixCursor(new String[]{
                    SearchManager.SUGGEST_COLUMN_FORMAT, SearchManager.SUGGEST_COLUMN_TEXT_1,
                    SearchManager.SUGGEST_COLUMN_INTENT_DATA, BaseColumns._ID});

            // TODO: this should be cached and updated on SMS retrieval/sending
            Cursor cursor = this.getContext().getContentResolver().
                    query(MessageList.SMS_URI, MessageList.PROJECTION, null, null,
                null);
            cursor.moveToFirst();
            do {
                int columnIndex = cursor.getColumnIndex(MessageList.BODY_COLUMN);
                String msg = cursor.getString(columnIndex);

                Log.d(TAG, "add to matrix cursor together...." + msg);
                if (msg.toLowerCase().indexOf(query)>=0) {
                    columnIndex = cursor.getColumnIndex(MessageList.THREAD_ID_COLUMN);
                    int id = cursor.getInt(columnIndex);
                    matrixCursor.addRow(new Object[]{msg, msg, msg, id});
                }
            } while (cursor.moveToNext());

            return matrixCursor;
        }
    }
}
