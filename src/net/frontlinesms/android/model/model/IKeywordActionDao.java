/**
 * 
 */
package net.frontlinesms.android.model.model;

import android.database.Cursor;
import net.frontlinesms.android.db.DbEntity;
import net.frontlinesms.android.model.model.KeywordAction;

import java.util.List;

/**
 * @author Alex Anderson
 */
public interface IKeywordActionDao {

    Cursor getAllKeywordsCursor();
	List<KeywordAction> getAllKeywords();
    KeywordAction getKeywordById(long id);
    void deleteKeyword(DbEntity entity);
	KeywordAction[] getActions(String messageContent);
	void saveOrUpdateAction(KeywordAction action);

}
