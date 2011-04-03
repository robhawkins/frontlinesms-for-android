/**
 * FrontlineSMS <http://www.frontlinesms.com>
 * Copyright 2010, Meta Healthcare Systems Ltd.
 *
 * This file is part of FrontlineSMS for Android.
 *
 * FrontlineSMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * FrontlineSMS is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FrontlineSMS. If not, see <http://www.gnu.org/licenses/>.
 */
package net.frontlinesms.android;

import android.content.Context;
import android.util.Log;
import net.frontlinesms.android.model.PIMService;
import net.frontlinesms.android.model.SmsService;
import net.frontlinesms.android.model.model.Contact;
import net.frontlinesms.android.model.model.KeywordAction;
import net.frontlinesms.android.util.sms.PropertySubstituter;
import net.frontlinesms.android.util.sms.WholeSmsMessage;

import java.util.ArrayList;

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
        Integer rawContactId = PIMService.getRawContactIdByPhoneNumber(mContext, message.getOriginatingAddress());
        Integer groupId = PIMService.getGroupIdByName(mContext, action.getGroup());
        Log.d("Action", "Action.contactId " + contactId);
        Log.d("Action", "Action.rawContactId " + rawContactId);
        Log.d("Action", "Action.groupId " + groupId);
        if (groupId!=null && contactId!=null) {
            PIMService.addContactToGroup( mContext, (long) contactId, (long) rawContactId, (long) groupId);
        }
	}
	
	private void processLeave(KeywordAction action, WholeSmsMessage message) {
		if(action.getType() != KeywordAction.Type.LEAVE) throw new IllegalStateException();
		Integer contactId = PIMService.getContactIdByPhoneNumber(mContext, message.getOriginatingAddress());
        Integer rawContactId = PIMService.getRawContactIdByPhoneNumber(mContext, message.getOriginatingAddress());
        Integer groupId = PIMService.getGroupIdByName(mContext, action.getGroup());
        Log.d("Action", "Action.contactId " + contactId);
        Log.d("Action", "Action.rawContactId " + rawContactId);
        Log.d("Action", "Action.groupId " + groupId);
        if (groupId!=null && contactId!=null) {
            PIMService.removeContactFromGroup(mContext, (long) contactId, (long) rawContactId, (long) groupId);
        }
	}


	private void processReply(KeywordAction action, WholeSmsMessage message) {
		if(action.getType() != KeywordAction.Type.REPLY) throw new IllegalStateException();
        Contact contact = PIMService.getContactByPhoneNumber(mContext, message.getOriginatingAddress());
		String unformattedReplyText = action.getText();
        String replyText = this.propSub.substitute(action.getKeyword(), message, contact, unformattedReplyText);
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        Log.d("reply", "reply to: " + message.getOriginatingAddress());
        Log.d("reply", "reply contact: " + contact);
        Log.d("reply", "reply replyText: " + replyText);
        if (contact!=null) {
            contacts.add(contact);
            Log.d("reply", "reply replyText send to: " + contact.getDisplayName());
            SmsService.individualizeAndSendMessage(mContext, contacts, replyText, action.getKeyword(), message);
        }
	}


	private void processForward(KeywordAction action, WholeSmsMessage message) {
        Log.d("forward", "forward msg::: " + message);
		if(action.getType() != KeywordAction.Type.FORWARD) throw new IllegalStateException();
		// get the group to send to
		Log.d("forward", "forward msg..getContactsByGroup.. start ");
        ArrayList<Contact> recipients = PIMService.getContactsByGroup(mContext, action.getGroup());
        Log.d("forward", "forward msg..getContactsByGroup.. done ");
        Log.d("forward", "forward msg to recipients before: " + recipients.size());
		// remove the original sender from the forward group
        for (Contact r:recipients) {
            Log.d("forward", "forward to contact loop: " + r.getDisplayName());
            if (r.getMobile().equals(message.getOriginatingAddress())) {
                recipients.remove(r);
            }
        }
        Log.d("forward", "forward msg to recipients after: " + recipients.size());
        SmsService.individualizeAndSendMessage(mContext, recipients, action.getText(), action.getKeyword(), message);
	}


}
