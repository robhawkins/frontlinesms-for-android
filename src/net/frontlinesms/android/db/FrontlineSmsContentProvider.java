/**
 * 
 */
package net.frontlinesms.android.db;

import android.content.Context;

/**
 * @author aga
 */
public class FrontlineSmsContentProvider extends DbContentProvider {

    /** DbContentProvider#getDatabaseHelper() */
	@Override
	protected DbSqliteHelper getDatabaseHelper(Context context) {
		return new FrontlineSmsSqliteHelper(context);
	}

}
