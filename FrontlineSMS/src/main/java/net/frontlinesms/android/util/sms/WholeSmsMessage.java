/**
 * 
 */
package net.frontlinesms.android.util.sms;

import android.telephony.SmsMessage;
import android.util.Log;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author aga
 */
public class WholeSmsMessage {

	/** ordered list of parts comprising this message. */
	private final List<SmsMessage> parts;
	
	WholeSmsMessage(SmsMessage[] parts) {
		this.parts = Collections.unmodifiableList(Arrays.asList(parts));
        for (SmsMessage part:parts) {
            Log.d("SMS", "SMS Part.getDisplayMessageBody: " + part.getDisplayMessageBody());
            Log.d("SMS", "SMS Part.getDisplayOriginatingAddress: " + part.getDisplayOriginatingAddress());
            Log.d("SMS", "SMS Part.getMessageBody: " + part.getMessageBody());
            Log.d("SMS", "SMS Part.getOriginatingAddress: " + part.getOriginatingAddress());
            Log.d("SMS", "SMS Part.getPseudoSubject: " + part.getPseudoSubject());
            Log.d("SMS", "SMS Part.getUserData: " + part.getUserData());
        }
	}

	public String getMessageBody() {
		StringBuilder bob = new StringBuilder();
		for(SmsMessage m : parts) {
			bob.append(m.getMessageBody());
		}
		return bob.toString();
	}

	public String getOriginatingAddress() {
        int cnt =0;
        for (SmsMessage part:parts) {
            Log.d("SMS", cnt + "SMS Part.getDisplayMessageBody: " + part.getDisplayMessageBody());
            Log.d("SMS", cnt +  "SMS Part.getDisplayOriginatingAddress: " + part.getDisplayOriginatingAddress());
            Log.d("SMS",  cnt + "SMS Part.getMessageBody: " + part.getMessageBody());
            Log.d("SMS", cnt +  "SMS Part.getOriginatingAddress: " + part.getOriginatingAddress());
            Log.d("SMS", cnt +  "SMS Part.getPseudoSubject: " + part.getPseudoSubject());
            Log.d("SMS",  cnt + "SMS Part.getUserData: " + part.getUserData());
            cnt++;
        }
		return parts.get(0).getOriginatingAddress();
	}
}
