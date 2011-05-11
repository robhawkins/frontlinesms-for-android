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

import android.content.Context;
import net.frontlinesms.android.model.Contact;
import net.frontlinesms.android.model.PIMService;

/**
 * @author Alexander Anderson
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

        // drop the keyword in a forwarded message or when using substitutes
        String msg = message==null?null:message.getMessageBody();
        if (msg!=null && keyword!=null && !"".equals(keyword)) {
            msg = message.getMessageBody().replaceFirst(keyword,"").trim();
        }

		return subText
                .replace(KEY_KEYWORD, (keyword==null?"":keyword))
				.replace(KEY_DESTINATION_PHONENUMBER, (contact.getMobile()==null?"":contact.getMobile()))
				.replace(KEY_SENDER_PHONENUMBER,
                        (message==null||message.getOriginatingAddress()==null?"":message.getOriginatingAddress()))
				.replace(KEY_ORIGINAL_MESSAGE, (msg==null?"":msg))
                        // always substitute message body last to avoid injection
				;
	}

}
