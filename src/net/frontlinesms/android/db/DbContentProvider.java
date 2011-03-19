/**
 * 
 */
package net.frontlinesms.android.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.util.Log;

/**
 * @author aga
 */
public abstract class DbContentProvider extends ContentProvider {

	private DbSqliteHelper helper;
	
	/** @see ContentProvider#delete(Uri, String, String[]) */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		String whereClause = DbUtils.getWhereClause(uri, selection);
        int i = helper.getWritableDatabase().delete(DbUtils.getTableName(uri), whereClause, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        Log.d("DbContentProvider", "notifyChange!!");
		return i;
	}

	/** @see ContentProvider#getType(Uri) */
	@Override
	public String getType(Uri uri) {
		// TODO Check if URI is plain and return mixed
		// TODO check if URI matches entity class and return appropriate MIME
		// If no type is matched, return null
		return null;
	}

	/** @see ContentProvider#insert(Uri, ContentValues) */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
        long insertId;
        Integer objId = values.getAsInteger("_id");
        if (objId!=null && objId>=0) {
            insertId = this.helper.getWritableDatabase().update(DbUtils.getTableName(uri), values, "_id=?",
                    new String[]{values.getAsInteger("_id").toString()});
        } else {
            insertId = this.helper.getWritableDatabase().insert(DbUtils.getTableName(uri), "_id", values);
        }

        Uri u = ContentUris.withAppendedId(uri, insertId);
        getContext().getContentResolver().notifyChange(uri, null);
		return u;
	}

	/** @see ContentProvider#onCreate() */
	@Override
	public boolean onCreate() {
		this.helper = getDatabaseHelper(getContext());
		return true;
	}

	protected abstract DbSqliteHelper getDatabaseHelper(Context context);

	/** @see ContentProvider#query(Uri, String[], String, String[], String) */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		return this.helper.getReadableDatabase().query(DbUtils.getTableName(uri),
				projection, selection, selectionArgs, null, null, null);
	}

	/** @see ContentProvider#update(Uri, ContentValues, String, String[]) */
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
        int i =  this.helper.getReadableDatabase().update(DbUtils.getTableName(uri),
                values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
		return i;
	}

}
