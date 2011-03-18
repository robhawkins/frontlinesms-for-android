package net.frontlinesms.android.db;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * This software is written by Meta Healthcare Systems Ltd. and subject
 * to a contract between Meta Healthcare Systems and its customer.
 * <p/>
 * This software stays property of Meta Healthcare Systems unless differing
 * arrangements between Meta Healthcare Systems and its customer apply.
 * <p/>
 * Meta Healthcare Systems Ltd.
 * 20/F Central Tower
 * 28 Queen's Road Central
 * Hong Kong
 * <p/>
 * Tel: +852 8199 9605
 * http://www.metahealthcare.com
 * mailto:info@metahealthcare.com
 * <p/>
 * (c)2010 Meta Healthcare Systems Ltd. All rights reserved.
 */
public abstract class BaseDbAccessObject implements DbAccessObject {
	private final ContentResolver contentResolver;

	protected BaseDbAccessObject(ContentResolver contentResolver) {
		this.contentResolver = contentResolver;
	}

	@Override
	public void delete(DbEntity entity) {
		contentResolver.delete(getUri(entity), null, null);
	}

	@Override
	public <T extends DbEntity> T get(Class<T> entityClass, long databaseId) {
		Uri entityUri = getUri(entityClass, databaseId);
		Cursor cursor = this.contentResolver.query(entityUri, null, null, null, null);
		return DbUtils.asList(entityClass, cursor).get(0);
	}

	@Override
	public void deleteByExample(DbEntity example) {
		WhereClause whereClause = getWhereClause(example);
		this.contentResolver.delete(whereClause.getUri(), whereClause.getWhere(), whereClause.getSelectionArgs());
	}

	@Override
	public <T extends DbEntity> Cursor getCursor(Class<T> entityClass) {
		Uri uri = Uri.parse(getUri(entityClass));
		Cursor cursor = this.contentResolver.query(uri, null, null, null, null);
		return cursor;
	}

    @SuppressWarnings("unchecked")
	@Override
    public <T extends DbEntity> List<T> get(T example) {
		WhereClause whereClause = getWhereClause(example);
		Cursor cursor = this.contentResolver.query(whereClause.getUri(), null,
				whereClause.getWhere(), whereClause.getSelectionArgs(), null);
		return (List<T>) DbUtils.asList(example.getClass(), cursor);
	}

	@Override
	public <T extends DbEntity> List<T> get(Class<T> entityClass) {
		Uri uri = Uri.parse(getUri(entityClass));
		Cursor cursor = this.contentResolver.query(uri, null, null, null, null);
		return DbUtils.asList(entityClass, cursor);
	}

	@Override
	public void save(DbEntity entity) {
		contentResolver.insert(
				Uri.parse(getUri(entity.getClass())),
				DbUtils.getNonNullValues(entity));
	}

	public void saveOrUpdate(DbEntity entity) {
		if(entity.getDbId() == null) {
			save(entity);
		} else {
			update(entity);
		}
	}

	@Override
	public void update(DbEntity entity) {
		contentResolver.update(getUri(entity), DbUtils.getValues(entity), null, null);
	}

	private Uri getUri(DbEntity entity) {
		return getUri(entity.getClass(), entity.getDbId());
	}

	private Uri getUri(Class<? extends DbEntity> entityClass, long databaseId) {
		return Uri.parse(getUri(entityClass) + '/' + databaseId);
	}
	private String getUri(Class<? extends DbEntity> entityClass) {
		return DbUtils.getUri(getBaseUri(), entityClass);
	}

	protected abstract String getBaseUri();

	private WhereClause getWhereClause(DbEntity example) {
		Uri uri = Uri.parse(getUri(example.getClass()));
		StringBuilder selection = new StringBuilder();
		ArrayList<String> selectionArgs = new ArrayList<String>();
		for(Field field : DbUtils.getFields(example.getClass())) {
			Object value = DbUtils.getContentValue(example, field);
			if(value != null) {
				selection.append(" AND ");
				selection.append(DbUtils.getColumnName(field));
				selection.append("=?");
				selectionArgs.add(DbUtils.getValueAsSqlString(value));
			}
		}
		return new WhereClause(uri,
				selection.length() > 0 ? selection.substring(5) : null,
				selectionArgs.size() > 0 ? selectionArgs.toArray(new String[0]) : null);
	}
}

class WhereClause {
	private final Uri uri;
	private final String where;
	private final String[] selectionArgs;

	public WhereClause(Uri uri, String where, String[] selectionArgs) {
		super();
		this.uri = uri;
		this.where = where;
		this.selectionArgs = selectionArgs;
	}

	public String getWhere() {
		return where;
	}
	public String[] getSelectionArgs() {
		return selectionArgs;
	}
	public Uri getUri() {
		return uri;
	}
}