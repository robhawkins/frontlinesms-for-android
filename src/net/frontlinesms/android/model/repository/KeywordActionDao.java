/**
 * 
 */
package net.frontlinesms.android.model.repository;

import net.frontlinesms.android.model.model.KeywordAction;

/**
 * @author aga
 */
public interface KeywordActionDao {
	String[] getKeywords();
	KeywordAction[] getActions(String messageContent);
	void addAction(KeywordAction action);
}
