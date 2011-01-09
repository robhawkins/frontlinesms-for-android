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
package net.frontlinesms.android.data.model;

import java.sql.Timestamp;

public class Event {

    static public enum EventType {
        SMS_INCOMING,
        SMS_OUTGOING,
        EMAIL_INCOMING,
        EMAIL_OUTGOING
    }

    private int id;
    private EventType eventType;
    private String description;
    private Timestamp timestamp;

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
