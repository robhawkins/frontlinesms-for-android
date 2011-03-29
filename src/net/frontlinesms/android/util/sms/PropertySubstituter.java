/**
 * 
 */
package net.frontlinesms.android.util.sms;

import android.content.ContentResolver;
import android.content.Context;
import net.frontlinesms.android.model.PIMService;
import net.frontlinesms.android.model.model.Contact;
import net.frontlinesms.android.model.model.KeywordAction;

/**
 * @author aga
 *
 */
public class PropertySubstituter {
	public static final String KEY_ORIGINAL_MESSAGE = "${message_content}";
	public static final String KEY_SENDER_PHONENUMBER = "${sender_number}";
	public static final String KEY_SENDER_NAME = "${sender_name}";
	public static final String KEY_DESTINATION_PHONENUMBER = "${recipient_number}";
	public static final String KEY_DESTINATION_NAME = "${recipient_name}";
    public static final String KEY_KEYWORD = "${keyword}";

    private final Context mContext;

	public PropertySubstituter(Context context) {
		this.mContext = context;
	}

	/** Substitute properties of the message into the reply text */
	public String substitute(String keyword, WholeSmsMessage message, Contact contact, String subText) {
		if(subText.contains(KEY_SENDER_NAME)) {
			String contactName = PIMService.getContactNameByPhoneNumber(mContext, message.getOriginatingAddress());
			if(contactName != null) {
				subText = subText.replace(KEY_SENDER_NAME, contactName);
			}
		}
		
		if(subText.contains(KEY_DESTINATION_NAME)) {
			// String contactName = PIMService.getContactNameByPhoneNumber(mContext, destinationAddress);
			if(contact.getDisplayName() != null) {
				subText = subText.replace(KEY_DESTINATION_NAME, contact.getDisplayName());
			}
		}
		
		return subText
                .replace(KEY_KEYWORD, (keyword==null?"":keyword))
				.replace(KEY_DESTINATION_PHONENUMBER, contact.getMobile())
				.replace(KEY_SENDER_PHONENUMBER, (message==null?"":message.getOriginatingAddress()))
				.replace(KEY_ORIGINAL_MESSAGE, message.getMessageBody()) // always substitute message body last to avoid injection
				;
	}

}
