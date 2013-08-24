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
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;

import net.frontlinesms.android.db.BaseDbAccessObject;
import net.frontlinesms.android.db.DbEntity;
import net.frontlinesms.android.db.FrontlineSmsSqliteHelper;

import java.util.List;

/**
 * @author Rob Hawkins
 */
public class PollDao extends BaseDbAccessObject implements IPollDao {

    ContentResolver mContentResolver;

    public PollDao(ContentResolver contentResolver) {
        super(contentResolver);
        this.mContentResolver = contentResolver;
    }

    @Override
    public void saveOrUpdatePoll(Poll poll) {
        Uri row = super.save(poll);
        long id = ContentUris.parseId(row);
        poll.setDbId(id);
    }

    @Override
    public Poll[] getActivePolls() {
//        KeywordAction example = new KeywordAction();
//        example.setKeyword(KeywordAction.getKeyword(mContentResolver, messageContent, allowAnywhere));
//        List<KeywordAction> list = super.getAll(example);
//        return list.toArray(new KeywordAction[list.size()]);
        Poll example = new Poll();
        List<Poll> list = super.getAll(example);
        return list.toArray(new Poll[0]);
    }

    @Override
    public Poll getPollById(long id) {
        return super.get(Poll.class, id);
    }

    public Poll getPollByKeywordActionId(long keywordActionId) {
        return super.getByForeignId(Poll.class, "keywordActionId", keywordActionId);
    }



    @Override
    public void deletePoll(DbEntity entity) {
        super.delete(entity);
    }

    @Override
    public List<Poll> getAllPolls() {
        List<Poll> PollList = super.getAll(Poll.class);
        return PollList;

        /*
		TreeSet<String> keywords = new TreeSet<String>();
		for(KeywordAction action : keywordList) {
			keywords.add(action.getKeyword());
		}
		return keywords.toArray(new String[0]);
		*/
    }

    @Override
    public Cursor getAllPollsCursor() {
        return super.getAllCursor(Poll.class);
    }

    @Override
    protected String getBaseUri() {
        return FrontlineSmsSqliteHelper.CONTENT_URI;
    }
}
