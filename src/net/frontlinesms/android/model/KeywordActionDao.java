/**
 * 
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
