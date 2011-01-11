/**
 * 
 */
package net.frontlinesms.android.data.repository;

import android.content.Context;

import net.frontlinesms.android.db.DbContentProvider;
import net.frontlinesms.android.db.DbSqliteHelper;

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
