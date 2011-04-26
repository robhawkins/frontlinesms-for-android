package net.frontlinesms.android.db;

import net.frontlinesms.android.model.KeywordAction;

import android.content.Context;

public class FrontlineSmsSqliteHelper extends DbSqliteHelper {

	public static final String CONTENT_URI = "content://" + FrontlineSmsContentProvider.class.getName().toLowerCase();
	private static final String DATABASE_NAME = "frontlinesms";
	private static final int DATABASE_VERSION = 7;

	public FrontlineSmsSqliteHelper(Context context) {
		super(context, DATABASE_NAME, DATABASE_VERSION);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Class<? extends DbEntity>[] getEntityClasses() {
		return new Class[]{
				KeywordAction.class};
	}
}
