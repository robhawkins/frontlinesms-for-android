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
package net.frontlinesms.android.util.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * {@link android.content.BroadcastReceiver} for SMS messages.
 * @author Alex Anderson
 */
public abstract class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = SmsReceiver.class.getSimpleName();
    //private Logger log = Logger.getLogger(this);
	
	protected abstract IReceivedSmsHandler getReceivedSmsHandler();
	
	private Context context;
	
	protected Context getContext() {
		return this.context;
	}
	
	/** Initialise any necessary properties.  This must be called before
	 * getting the receivedSmsHandler. */
	// TODO does this make sense here, or should we pass on the context etc.
	// in this.getReceivedSmsHandler().handleReceivedSms(message) ?
	protected abstract void init();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		IntentType intentType = IntentType.get(intent);
		if(intentType != null) {
			this.context = context;
			this.init();
			
			switch(intentType) {
			case SMS_RECEIVED:
				WholeSmsMessage message = getMessagesFromIntent(intent);
				if(message != null) {
					Log.i(TAG, "Handling received message...");
					this.getReceivedSmsHandler().handleReceivedSms(message);
				} else {
					Log.w(TAG, "No SmsMessage objects extracted from " + intentType);
				}
				break;
			default: throw new IllegalStateException("Unhandled intent: " + intentType.getAction());
			}
		}
	}

	private WholeSmsMessage getMessagesFromIntent(Intent intent) {
		Bundle bundle = intent.getExtras();
		if(bundle == null) return null;
		else {
			Object[] pdus = (Object[]) bundle.get("pdus");
			SmsMessage[] messages = new SmsMessage[pdus.length];
			for (int i = 0; i < messages.length; i++) {
				byte[] pdu = (byte[]) pdus[i];
				messages[i] = SmsMessage.createFromPdu(pdu);
			}
			return new WholeSmsMessage(messages);
		}
	}

}

enum IntentType {
	SMS_RECEIVED("android.provider.Telephony.SMS_RECEIVED");
	
	private final String action;
	
	IntentType(String action) { this.action = action; }
	
	public String getAction() {
		return action;
	}
	
	static IntentType get(Intent intent) {
		String action = intent.getAction();
		for(IntentType t : values()) {
			if(t.getAction().equals(action)) {
				return t;
			}
		}
		return null;
	}
}