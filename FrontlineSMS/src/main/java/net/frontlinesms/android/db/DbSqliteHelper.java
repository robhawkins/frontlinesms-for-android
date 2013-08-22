/**
 * 
 */
package net.frontlinesms.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author aga
 */
public abstract class DbSqliteHelper extends SQLiteOpenHelper {

    private static final String TAG = DbSqliteHelper.class.getSimpleName();

	protected DbSqliteHelper(Context context, String databaseName, int databaseVersion) {
		super(context, databaseName, null, databaseVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for(Class<? extends DbEntity> entityClass : getEntityClasses()) {
			Log.i(TAG, "Creating database table for " + entityClass);
			String createStatement = DbUtils.getCreateStatement(entityClass);
			try {
				db.execSQL(createStatement);
			} catch(SQLiteException ex) {
				String tableName = DbUtils.getTableName(entityClass);
				if(ex.getMessage().startsWith("table " + tableName + " already exists: ")) {
					Log.i(TAG, "Table already exists: " + tableName);
				} else {
					Log.e(TAG, "Error executing SQL: " + createStatement, ex);
					throw ex;
				}
			}
		}
	}

	protected abstract Class<? extends DbEntity>[] getEntityClasses();

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(TAG, "Database version changed from " + oldVersion + " to " + newVersion);
		
		// drop old tables
		for (Class<? extends DbEntity> entityClass : getEntityClasses()) {
			db.execSQL("DROP TABLE IF EXISTS " + DbUtils.getTableName(entityClass));
		}
		
		onCreate(db);
	}
}
