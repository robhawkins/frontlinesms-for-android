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
 * (c)2010 Meta Healthcare Systems Ltd. All rights reserved.
 */
package net.frontlinesms.android.model;

import net.frontlinesms.android.db.DbEntity;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * The job represents a delivery task (email, sms, http request) that can be
 * queued and executed. If execution is successful, a job will be deleted.
 * Jobs are persistent in the database (until deleted).
 *
 * Jobs serve the purpose to make message delivery more stable, i.e. if sending
 * a message, email or http request had failed, it can be re-executed at another
 * time.
 *
 * @author Mathias Lin
 *
 */
public final class Job implements DbEntity {

    /** Unique id. */
	private Long _id;

    private Job.Type type;

    /** Recipient (can be email address, phone number, or http url. */
	private String recipient;

    /** Message subject (for emails only) */
    private String subject;

    /** Message. */
	private String text;

    /** Timestamp when this job was created. */
    private Timestamp creationTime;

    /** Timestamp when this job shall be executed, optional. */
    private Timestamp scheduledTime;

    /** Logs the time of last execution attempt. */
    private Timestamp lastAttemptTime;

    /** Constructor/ */
	public Job() {}

	public Job(Job.Type type, String recipient, String subject, String text) {
		this.type = type;
        this.subject = subject;
        this.recipient = recipient;
		this.text = text;
        this.creationTime = new Timestamp(Calendar.getInstance().getTimeInMillis());
	}

//> ENUMS
	public enum Type {
		SMS_JOB,
		EMAIL_JOB,
		HTTP_REQUEST_JOB;
	}

//> ACCESSORS
	@Override
	public Long getDbId() {
		return _id;
	}

    public void setDbId(long id) {
        this._id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    /*public Long getId() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }*/

    public Job.Type getType() {
        return type;
    }

    public void setType(Job.Type type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }

    public Timestamp getLastAttemptTime() {
        return lastAttemptTime;
    }

    public void setLastAttemptTime(Timestamp lastAttemptTime) {
        this.lastAttemptTime = lastAttemptTime;
    }

    public Timestamp getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(Timestamp scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
}