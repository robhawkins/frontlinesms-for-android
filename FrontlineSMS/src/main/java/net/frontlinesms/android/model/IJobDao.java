package net.frontlinesms.android.model;

import android.database.Cursor;
import net.frontlinesms.android.db.DbEntity;

import java.util.List;

public interface IJobDao {

    Cursor getAllJobsCursor(boolean dueJobsOnly);
	List<Job> getAllJobs(boolean dueJobsOnly);
    Job getJobById(long id);
    void deleteJob(DbEntity entity);
	void saveOrUpdateJob(Job job);

}
