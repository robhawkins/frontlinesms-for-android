/**
 * 
 */
package net.frontlinesms.android.model;

import android.database.Cursor;
import net.frontlinesms.android.db.DbEntity;

import java.util.List;

/**
 * @author Alex Anderson
 */
public interface IKeywordActionDao {

    Cursor getAllKeywordsCursor();
	List<KeywordAction> getAllKeywords();
    KeywordAction getKeywordById(long id);
    void deleteKeyword(DbEntity entity);
	KeywordAction[] getActions(String messageContent, boolean allowAnywhere);
	void saveOrUpdateAction(KeywordAction action);

}
