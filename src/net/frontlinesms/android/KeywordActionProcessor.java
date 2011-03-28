/**
 * 
 */
package net.frontlinesms.android;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import net.frontlinesms.android.model.PIMService;
import net.frontlinesms.android.model.SmsService;
import net.frontlinesms.android.model.model.Contact;
import net.frontlinesms.android.model.model.KeywordAction;
import net.frontlinesms.android.util.sms.PropertySubstituter;
import net.frontlinesms.android.util.sms.WholeSmsMessage;

import java.util.Set;
import java.util.Vector;

/**
 * @author Alex Anderson
 */
public class KeywordActionProcessor {
	//private final SmsSender smsSender = new SmsSender();
	private final PropertySubstituter propSub;
    //private final ContentResolver mResolver;
    private final Context mContext;

	
	public KeywordActionProcessor(Context context) {
		this.propSub = new PropertySubstituter(context);
        this.mContext = context;
      //  this.mResolver = contentResolver;
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
        Integer contactId = PIMService.getContactIdByPhoneNumber(mContext, message.getOriginatingAddress());
        Integer groupId = PIMService.getGroupIdByName(mContext, action.getGroup());
        if (groupId!=null && contactId!=null) {
            PIMService.addContactToGroup( mContext, (long) contactId, (long) groupId);
        }
	}
	
	private void processLeave(KeywordAction action, WholeSmsMessage message) {
		if(action.getType() != KeywordAction.Type.LEAVE) throw new IllegalStateException();
		Integer contactId = PIMService.getContactIdByPhoneNumber(mContext, message.getOriginatingAddress());
        Integer groupId = PIMService.getGroupIdByName(mContext, action.getGroup());
        if (groupId!=null && contactId!=null) {
            PIMService.removeContactFromGroup(mContext, (long) contactId, (long) groupId);
        }
	}


	private void processReply(KeywordAction action, WholeSmsMessage message) {
		if(action.getType() != KeywordAction.Type.REPLY) throw new IllegalStateException();
		String unformattedReplyText = action.getText();
		String replyText = this.propSub.substitute(action, message, message.getOriginatingAddress(), unformattedReplyText);
        Vector<Contact> contacts = new Vector<Contact>();
        Contact contact = PIMService.getContactByPhoneNumber(mContext, message.getOriginatingAddress());
        Log.d("reply", "reply to: " + message.getOriginatingAddress());
        Log.d("reply", "reply contact: " + contact);
        Log.d("reply", "reply replyText: " + replyText);
        if (contact!=null) {
            contacts.add(contact);
            SmsService.sendMessage(mContext, contacts, replyText);
        }
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


}
