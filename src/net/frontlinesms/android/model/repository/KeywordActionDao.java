/**
 * 
 */
package net.frontlinesms.android.model.repository;

import android.database.Cursor;
import net.frontlinesms.android.model.model.KeywordAction;

import java.util.List;

/**
 * @author Alex Anderson
 */
public interface KeywordActionDao {

    Cursor getKeywordsCursor();
	List<KeywordAction> getKeywords();
	KeywordAction[] getActions(String messageContent);
	void addAction(KeywordAction action);

}
