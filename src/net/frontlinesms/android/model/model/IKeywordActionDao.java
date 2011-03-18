/**
 * 
 */
package net.frontlinesms.android.model.model;

import android.database.Cursor;
import net.frontlinesms.android.model.model.KeywordAction;

import java.util.List;

/**
 * @author Alex Anderson
 */
public interface IKeywordActionDao {

    Cursor getKeywordsCursor();
	List<KeywordAction> getKeywords();
	KeywordAction[] getActions(String messageContent);
	void addAction(KeywordAction action);

}
