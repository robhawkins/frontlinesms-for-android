/**
 *
 */
package net.frontlinesms.android.model;

import android.database.Cursor;
import net.frontlinesms.android.db.DbEntity;

import java.util.List;

/**
 * @author Rob Hawkins
 */
public interface IPollDao {

    Cursor getAllPollsCursor();
    List<Poll> getAllPolls();
    Poll getPollById(long id);
    void deletePoll(DbEntity entity);
    //KeywordAction[] getActions(String messageContent, boolean allowAnywhere);
    Poll[] getActivePolls();
    void saveOrUpdatePoll(Poll poll);

}
