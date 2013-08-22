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
 * @author Mathias Lin <mathias.lin@metahealthcare.com>
 */
public class JobDao extends BaseDbAccessObject implements IJobDao {

    ContentResolver mContentResolver;

    public JobDao(ContentResolver contentResolver) {
		super(contentResolver);
        this.mContentResolver = contentResolver;
	}

	@Override
	public void saveOrUpdateJob(Job job) {
		super.save(job);
	}

    @Override
    public Job getJobById(long id) {
        return super.get(Job.class, id);
    }

    @Override
    public void deleteJob(DbEntity entity) {
        super.delete(entity);
    }

    @Override
	public List<Job> getAllJobs(boolean dueJobsOnly) {
		List<Job> jobList = super.getAll(Job.class);
        return jobList;

        /*
		TreeSet<String> keywords = new TreeSet<String>();
		for(KeywordAction action : keywordList) {
			keywords.add(action.getKeyword());
		}
		return keywords.toArray(new String[0]);
		*/
	}

    @Override
    public Cursor getAllJobsCursor(boolean dueJobsOnly) {
        return super.getAllCursor(Job.class);
    }

    @Override
	protected String getBaseUri() {
		return FrontlineSmsSqliteHelper.CONTENT_URI;
	}
}
