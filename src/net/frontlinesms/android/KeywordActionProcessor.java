/**
 * 
 */
package net.frontlinesms.android;

import android.app.PendingIntent;
import android.content.ContentResolver;
import net.frontlinesms.android.model.model.KeywordAction;
import net.frontlinesms.android.util.sms.PropertySubstituter;
import net.frontlinesms.android.util.sms.WholeSmsMessage;

import java.util.Set;

/**
 * @author Alex Anderson
 */
public class KeywordActionProcessor {
	//private final SmsSender smsSender = new SmsSender();
	private final PropertySubstituter propSub;
	
	public KeywordActionProcessor(ContentResolver contentResolver) {
		this.propSub = new PropertySubstituter(contentResolver);
	}

	public void process(KeywordAction action, WholeSmsMessage message) {
		switch(action.getType()) {
		case REPLY:
			processReply(action, message);
			break;
		case FORWARD:
			processForward(action, message);
			break;
		case JOIN:
			processJoin(action, message);
			break;
		case LEAVE:
			processLeave(action, message);
			break;
		default:
			throw new IllegalStateException("Unknown action type: " + action.getClass());
		}
	}
	
	private void processJoin(KeywordAction action, WholeSmsMessage message) {
		if(action.getType() != KeywordAction.Type.JOIN) throw new IllegalStateException();
		// this.listDao.addToList(action.getList(), message.getOriginatingAddress());
        // TODO add to google group
	}
	
	private void processLeave(KeywordAction action, WholeSmsMessage message) {
		if(action.getType() != KeywordAction.Type.LEAVE) throw new IllegalStateException();
		// this.listDao.removeFromList(action.getList(), message.getOriginatingAddress());
        // TODO remove from google group
	}

	private void processForward(KeywordAction action, WholeSmsMessage message) {
		if(action.getType() != KeywordAction.Type.FORWARD) throw new IllegalStateException();
		String unformattedText = action.getText();
		
		// get the group to send to
		Set<String> recipients = null; // TODO was: this.listDao.getNumbers(action.getList());
		// remove the original sender from the forward group
		recipients.remove(message.getOriginatingAddress());
		
		for(String recipient : recipients) {
			String forwardText = this.propSub.substitute(action, message, recipient, unformattedText);
			// TODO send SMS
//            this.smsSender.sendSms(recipient, forwardText, getSentIntent(), getDeliveryIntent());
		}
	}

	private void processReply(KeywordAction action, WholeSmsMessage message) {
		if(action.getType() != KeywordAction.Type.REPLY) throw new IllegalStateException();
		String unformattedReplyText = action.getText();
		String replyText = this.propSub.substitute(action, message, message.getOriginatingAddress(), unformattedReplyText);
		// TODO send SMS
//        this.smsSender.sendSms(message.getOriginatingAddress(),
//				replyText, getSentIntent(), getDeliveryIntent());
	}

	private PendingIntent getDeliveryIntent() {
		return null;
	}

	private PendingIntent getSentIntent() {
		return null;
	}
}
