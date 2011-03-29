package net.frontlinesms.android.util.sms;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import net.frontlinesms.android.KeywordActionProcessor;
import net.frontlinesms.android.model.model.KeywordAction;
import net.frontlinesms.android.model.model.KeywordActionDao;

public class MessageProcessor {
	//private final Logger log = Logger.getLogger(this);
	private final KeywordActionProcessor mKeywordActionProcessor;
	private final KeywordActionDao mKeywordActionDao;
//    private final ContentResolver mResolver;
	
	public MessageProcessor(Context context) {
		this.mKeywordActionDao = new KeywordActionDao(context.getContentResolver());
		this.mKeywordActionProcessor = new KeywordActionProcessor(context);
//        this.mResolver = resolver;
	}

    // process keyword actions
	public void process(WholeSmsMessage message) {
		KeywordAction[] actions = this.mKeywordActionDao.getActions(message.getMessageBody());
		for(KeywordAction action : actions) {
			this.mKeywordActionProcessor.process(action, message);
		}
	}
}
