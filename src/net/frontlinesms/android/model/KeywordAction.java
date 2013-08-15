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

import android.content.ContentResolver;
import net.frontlinesms.android.db.DbEntity;

import java.util.List;

/**
 * @author Alex Anderson
 * @author Mathias Lin
 *
 */
public final class KeywordAction implements DbEntity {

    /** Unique id. */
	private Long _id;

    /** Action type. */
	private Type type;

    /** Keyword to look for. */
	private String keyword;

    /** Description. */
    private String description;

    /** Auto reply/forward/email text. */
	private String text;

    /** Group to leave or join. */
	private String group;

    /** Subject (used for e-mail action). */
	private String subject;

    /** Recipient. */
	private String recipient;

    /** Constructor/ */
	public KeywordAction() {}
	
	private KeywordAction(Type type, String keyword, String description, String subject, String text, String group,
                          String recipient) {

		this.type = type;
		// convert all keywords to lower-case so they take up less space
		this.keyword = keyword.toLowerCase();
        this.description = description;
        this.subject = subject;
        this.recipient = recipient;
		
		if(!type.hasText() && text!=null) {
			throw new IllegalStateException("Cannot set text on an action of type: " + type);
		}
		this.text = text;
		
		if(!type.hasGroup() && group !=null) {
			throw new IllegalStateException("Cannot set list on an action of type: " + type);
		}
		this.group = group;
	}
	
//> ACCESSORS
	@Override
	public Long getDbId() {
		return _id;
	}

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Type getType() {
		return type;
	}
    public void setType(Type type) {
        this.type = type;
    }

	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		if(text!=null && !type.hasText() && !"".equals(text))
            throw new IllegalStateException("Cannot set text on an action of type: " + type);
		if ("".equals(text)) text = null;
        this.text = text;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		if(group!=null && !type.hasGroup() && !"".equals(group))
            throw new IllegalStateException("Cannot set list on an action of type: " + type);
        if ("".equals(group)) group = null;
		this.group = group;
	}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

	
//> ENUMS
	public enum Type {
		REPLY(true, false),
		FORWARD(true, true),
		JOIN(false, true),
		LEAVE(false, true),
        EMAIL(true,false),
        HTTP_REQUEST(true,false),
        POLL(true,true);

		private final boolean hasText;
		private final boolean hasGroup;
		
		private Type(boolean hasText, boolean hasGroup) {
			this.hasText = hasText;
			this.hasGroup = hasGroup;
		}

		boolean hasText() {
			return this.hasText;
		}
		boolean hasGroup() {
			return this.hasGroup;
		}
	}

    /**
     * Checks the message for a keyword, by default checks for first word.
     * @param contentResolver Content resolver
     * @param messageContent SMS content
     * @param allowAnywhere Keyword can be anywhere in message, otherwise only check first word
     * @return
     */
    // TODO if we allow keywordAnywhere, then we should allow multiple keyword executions as well
	public static String getKeyword(ContentResolver contentResolver, String messageContent, boolean allowAnywhere) {
        messageContent = messageContent.toLowerCase().trim();
        String k = null;
        if (allowAnywhere) {
            List<KeywordAction> keywords = new KeywordActionDao(contentResolver).getAllKeywords();
            for (KeywordAction keyword:keywords) {
                if (messageContent.indexOf(keyword.getKeyword().toLowerCase())>=0) {
                    k = keyword.getKeyword();
                }
            }
        }
        if  (k==null && messageContent.length()>0) {
            k = messageContent.split("\\s", 2)[0];
        }
        return k;
	}
}