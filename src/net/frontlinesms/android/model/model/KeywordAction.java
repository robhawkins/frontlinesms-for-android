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
package net.frontlinesms.android.model.model;

import net.frontlinesms.android.db.DbEntity;

/**
 * @author Alex Anderson
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

    /** Auto reply text. */
	private String text;

    /** Group to leave or join. */
	private String group;

    /** Constructor/ */
	public KeywordAction() {}
	
	private KeywordAction(Type type, String keyword, String description, String text, String group) {

		this.type = type;
		// convert all keywords to lower-case so they take up less space
		this.keyword = keyword.toLowerCase();
        this.description = description;
		
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
		if(!type.hasText() && text!=null && !"".equals(text))
            throw new IllegalStateException("Cannot set text on an action of type: " + type);
		if ("".equals(text)) text = null;
        this.text = text;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		if(!type.hasGroup() && group!=null && !"".equals(group))
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

    //> STATIC FACTORY METHODS
	public static KeywordAction createReplyAction(String keyword, String description, String replyText) {
		return new KeywordAction(Type.REPLY, keyword, description, replyText, null);
	}
	public static KeywordAction createForwardAction(String keyword, String description, String forwardText, String group) {
		return new KeywordAction(Type.FORWARD, keyword, description, forwardText, group);
	}
	public static KeywordAction createJoinAction(String keyword, String description, String group) {
		return new KeywordAction(Type.JOIN, keyword, description, null, group);
	}
	public static KeywordAction createLeaveAction(String keyword, String description, String group) {
		return new KeywordAction(Type.LEAVE, keyword, description, null, group);
	}
	
//> ENUMS
	public enum Type {
		REPLY(true, false),
		FORWARD(true, true),
		JOIN(false, true),
		LEAVE(false, true);

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
	
	/** @return the first word from the message, or <code>null</code> if there
	 * don't appear to be any */
	public static String getKeyword(String messageContent) {
		// convert to lower case to save space
		messageContent = messageContent.toLowerCase().trim();
		if(messageContent.length() == 0) {
			return null;
		} else {
			return messageContent.split("\\s", 2)[0];
		}
	}
}