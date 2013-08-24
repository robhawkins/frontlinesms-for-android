package net.frontlinesms.android.model;

import net.frontlinesms.android.db.DbEntity;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * @author Rob Hawkins
 *
 */

public final class Poll implements DbEntity {

    /** Unique id. */
    private Long _id;

    /** Timestamp when this poll was created. */
    private Timestamp creationTime;

    /** Timestamp when this poll expires. */
    private Timestamp expirationTime;

    /** The poll question. */
    private String question;

    /** Keyword for the poll, optional. */
    private String keyword;

    /** Keyword action id, if there is a keyword set for this poll. */
    private Long keywordActionId;

    /** Constructor/ */
    public Poll() {}

    private Poll(String question, String keyword, int daysToExpire) {

        this.question = question;
        this.keyword = keyword;

        Calendar cal = Calendar.getInstance();
        this.creationTime = new Timestamp(cal.getTimeInMillis());
        cal.add(Calendar.DATE, daysToExpire);
        this.expirationTime = new Timestamp(cal.getTimeInMillis());

    }

    //> ACCESSORS
    @Override
    public Long getDbId() {
        return _id;
    }

    public void setDbId(long id) {
        this._id = id;
    }

    public Timestamp creationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }
    public Timestamp expirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Timestamp expirationTime) {
        this.expirationTime = expirationTime;
    }

    public void setDaysToExpire(int daysToExpire) {
        Calendar cal = Calendar.getInstance();
        this.creationTime = new Timestamp(cal.getTimeInMillis());
        cal.add(Calendar.DATE, daysToExpire);
        this.expirationTime = new Timestamp(cal.getTimeInMillis());
    }

    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }

    public String getKeyword() {
        return keyword;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getKeywordActionId() {
        return keywordActionId;
    }

    public void setKeywordActionId(Long keywordActionId) {
        this.keywordActionId = keywordActionId;
    }
}
