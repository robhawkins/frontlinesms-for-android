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
 * (c)2010-2011 Meta Healthcare Systems Ltd. All rights reserved.
 */
package net.frontlinesms.android.model;

import android.content.ContentResolver;
import android.database.Cursor;
import net.frontlinesms.android.db.BaseDbAccessObject;
import net.frontlinesms.android.db.DbEntity;
import net.frontlinesms.android.db.FrontlineSmsSqliteHelper;

import java.util.List;

/**
 * @author aga
 */
public class KeywordActionDao extends BaseDbAccessObject implements IKeywordActionDao {

    ContentResolver mContentResolver;

    public KeywordActionDao(ContentResolver contentResolver) {
		super(contentResolver);
        this.mContentResolver = contentResolver;
	}

	@Override
	public void saveOrUpdateAction(KeywordAction action) {
		super.save(action);
	}

	@Override
	public KeywordAction[] getActions(String messageContent, boolean allowAnywhere) {
		KeywordAction example = new KeywordAction();
		example.setKeyword(KeywordAction.getKeyword(mContentResolver, messageContent, allowAnywhere));
        List<KeywordAction> list = super.getAll(example);
		return list.toArray(new KeywordAction[list.size()]);
	}

    @Override
    public KeywordAction getKeywordById(long id) {
        return super.get(KeywordAction.class, id);
    }

    @Override
    public void deleteKeyword(DbEntity entity) {
        super.delete(entity);
    }

    @Override
	public List<KeywordAction> getAllKeywords() {
		List<KeywordAction> keywordList = super.getAll(KeywordAction.class);
        return keywordList;

        /*
		TreeSet<String> keywords = new TreeSet<String>();
		for(KeywordAction action : keywordList) {
			keywords.add(action.getKeyword());
		}
		return keywords.toArray(new String[0]);
		*/
	}

    @Override
    public Cursor getAllKeywordsCursor() {
        return super.getAllCursor(KeywordAction.class);
    }

    @Override
	protected String getBaseUri() {
		return FrontlineSmsSqliteHelper.CONTENT_URI;
	}
}
