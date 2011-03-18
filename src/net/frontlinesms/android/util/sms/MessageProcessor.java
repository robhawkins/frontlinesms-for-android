package net.frontlinesms.android.util.sms;

import android.content.ContentResolver;
import net.frontlinesms.android.KeywordActionProcessor;
import net.frontlinesms.android.model.model.KeywordAction;
import net.frontlinesms.android.model.model.KeywordActionDao;
import net.frontlinesms.android.util.sms.WholeSmsMessage;

public class MessageProcessor {
	//private final Logger log = Logger.getLogger(this);
	private final KeywordActionProcessor keywordActionProcessor;
	private final KeywordActionDao keywordActionDao;
	
	public MessageProcessor(ContentResolver resolver) {
		this.keywordActionDao = new KeywordActionDao(resolver);
		this.keywordActionProcessor = new KeywordActionProcessor(resolver);
	}
	
	public void process(WholeSmsMessage message) {
		// match keyword actions
//		List<KeywordAction> actions = this.keywordActionProvider.getActions(message.getMessageBody());
//		log.info("Matched KActions: " + actions.size());

		KeywordAction[] actions = this.keywordActionDao.getActions(message.getMessageBody());
		//log.info("Matched KActions: " + actions.length);
		
		// process keyword actions
		for(KeywordAction action : actions) {
			this.keywordActionProcessor.process(action, message);
		}
	}
}
