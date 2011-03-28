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
	
	public void process(WholeSmsMessage message) {
		// match keyword actions
//		List<KeywordAction> actions = this.keywordActionProvider.getActions(message.getMessageBody());
//		log.info("Matched KActions: " + actions.size());

		KeywordAction[] actions = this.mKeywordActionDao.getActions(message.getMessageBody());
		Log.d("MsgActions", "Matched KActions: " + actions.length);
		
		// process keyword actions
		for(KeywordAction action : actions) {
            Log.d("MsgActions", "Matched Action: " + action.getKeyword());
			this.mKeywordActionProcessor.process(action, message);
		}
	}
}

/*
03-28 20:43:31.985: INFO/SmsReceiver(2744): Handling received message...
03-28 20:43:31.985: DEBUG/FrontlineSmsReceiver(2744): handleReceivedSms net.frontlinesms.android.util.sms.WholeSmsMessage@4054acd0
03-28 20:43:31.985: DEBUG/MsgActions(2744): msg from: 18688200424
03-28 20:43:31.995: DEBUG/C1.result:(2744): times_contacted: 0
03-28 20:43:31.995: DEBUG/C1.result:(2744): custom_ringtone: null
03-28 20:43:31.995: DEBUG/C1.result:(2744): has_phone_number: 1
03-28 20:43:31.995: DEBUG/C1.result:(2744): label: null
03-28 20:43:31.995: DEBUG/C1.result:(2744): number: +8618688200424
03-28 20:43:31.995: DEBUG/C1.result:(2744): type: 2
03-28 20:43:32.005: DEBUG/C1.result:(2744): lookup: 3601i501a66ab8b9a41f8
03-28 20:43:32.005: DEBUG/C1.result:(2744): last_time_contacted: 0
03-28 20:43:32.005: DEBUG/C1.result:(2744): display_name: Mathias Lin
03-28 20:43:32.005: DEBUG/C1.result:(2744): in_visible_group: 0
03-28 20:43:32.005: DEBUG/C1.result:(2744): _id: 1407
03-28 20:43:32.005: DEBUG/C1.result:(2744): starred: 0
03-28 20:43:32.005: DEBUG/C1.result:(2744): photo_id: 17022
03-28 20:43:32.005: DEBUG/C1.result:(2744): send_to_voicemail: 0
=============================================================
03-28 20:43:32.015: DEBUG/C2.result:(2744): data_version: 0
03-28 20:43:32.015: DEBUG/C2.result:(2744): phonetic_name: null
03-28 20:43:32.015: DEBUG/C2.result:(2744): phonetic_name_style: 3
03-28 20:43:32.015: DEBUG/C2.result:(2744): contact_id: 1407
03-28 20:43:32.015: DEBUG/C2.result:(2744): lookup: 3601i501a66ab8b9a41f8
03-28 20:43:32.015: DEBUG/C2.result:(2744): data12: null
03-28 20:43:32.015: DEBUG/C2.result:(2744): data11: null
03-28 20:43:32.015: DEBUG/C2.result:(2744): data10: null
03-28 20:43:32.015: DEBUG/C2.result:(2744): mimetype: vnd.android.cursor.item/phone_v2
03-28 20:43:32.015: DEBUG/C2.result:(2744): data15: null
03-28 20:43:32.015: DEBUG/C2.result:(2744): data14: null
03-28 20:43:32.015: DEBUG/C2.result:(2744): data13: null
03-28 20:43:32.015: DEBUG/C2.result:(2744): display_name_source: 40
03-28 20:43:32.015: DEBUG/C2.result:(2744): data_sync1: null
03-28 20:43:32.015: DEBUG/C2.result:(2744): data_sync3: null
03-28 20:43:32.015: DEBUG/C2.result:(2744): data_sync2: null
03-28 20:43:32.015: DEBUG/C2.result:(2744): contact_chat_capability: null
03-28 20:43:32.015: DEBUG/C2.result:(2744): data_sync4: 1
03-28 20:43:32.025: DEBUG/C2.result:(2744): account_type: com.google
03-28 20:43:32.025: DEBUG/C2.result:(2744): custom_ringtone: null
03-28 20:43:32.025: DEBUG/C2.result:(2744): status: null
03-28 20:43:32.025: DEBUG/C2.result:(2744): data1: +8618688200424
03-28 20:43:32.025: DEBUG/C2.result:(2744): data4: 4240028868168+
03-28 20:43:32.025: DEBUG/C2.result:(2744): data5: null
03-28 20:43:32.025: DEBUG/C2.result:(2744): data2: 2
03-28 20:43:32.025: DEBUG/C2.result:(2744): data3: null
03-28 20:43:32.025: DEBUG/C2.result:(2744): data8: null
03-28 20:43:32.025: DEBUG/C2.result:(2744): data9: null
03-28 20:43:32.025: DEBUG/C2.result:(2744): group_sourceid: null
03-28 20:43:32.025: DEBUG/C2.result:(2744): data6: null
03-28 20:43:32.025: DEBUG/C2.result:(2744): account_name: mathias.conradt@gmail.com
03-28 20:43:32.025: DEBUG/C2.result:(2744): data7: null
03-28 20:43:32.025: DEBUG/C2.result:(2744): display_name: Mathias Lin
03-28 20:43:32.025: DEBUG/C2.result:(2744): in_visible_group: 0
03-28 20:43:32.025: DEBUG/C2.result:(2744): display_name_alt: Lin, Mathias
03-28 20:43:32.025: DEBUG/C2.result:(2744): contact_status_res_package: null
03-28 20:43:32.025: DEBUG/C2.result:(2744): is_primary: 0
03-28 20:43:32.025: DEBUG/C2.result:(2744): contact_status_ts: null
03-28 20:43:32.025: DEBUG/C2.result:(2744): raw_contact_id: 1394
03-28 20:43:32.025: DEBUG/C2.result:(2744): times_contacted: 0
03-28 20:43:32.025: DEBUG/C2.result:(2744): contact_status: null
03-28 20:43:32.025: DEBUG/C2.result:(2744): status_res_package: null
03-28 20:43:32.025: DEBUG/C2.result:(2744): status_icon: null
03-28 20:43:32.025: DEBUG/C2.result:(2744): contact_status_icon: null
03-28 20:43:32.025: DEBUG/C2.result:(2744): mode: null
03-28 20:43:32.025: DEBUG/C2.result:(2744): version: 7
03-28 20:43:32.025: DEBUG/C2.result:(2744): last_time_contacted: 0
03-28 20:43:32.025: DEBUG/C2.result:(2744): res_package: null
03-28 20:43:32.025: DEBUG/C2.result:(2744): _id: 17032
03-28 20:43:32.025: DEBUG/C2.result:(2744): name_verified: 0
03-28 20:43:32.025: DEBUG/C2.result:(2744): status_ts: null
03-28 20:43:32.035: DEBUG/C2.result:(2744): dirty: 0
03-28 20:43:32.035: DEBUG/C2.result:(2744): is_super_primary: 0
03-28 20:43:32.035: DEBUG/C2.result:(2744): photo_id: 17022
03-28 20:43:32.035: DEBUG/C2.result:(2744): send_to_voicemail: 0
03-28 20:43:32.035: DEBUG/C2.result:(2744): name_verified_id: 1394
03-28 20:43:32.035: DEBUG/C2.result:(2744): contact_status_label: null
03-28 20:43:32.035: DEBUG/C2.result:(2744): status_label: null
03-28 20:43:32.035: DEBUG/C2.result:(2744): sort_key_alt: Lin, Mathias
03-28 20:43:32.035: DEBUG/C2.result:(2744): starred: 0
03-28 20:43:32.035: DEBUG/C2.result:(2744): sort_key: Mathias Lin
03-28 20:43:32.035: DEBUG/C2.result:(2744): contact_presence: null
03-28 20:43:32.035: DEBUG/C2.result:(2744): sourceid: 501a66ab8b9a41f8
03-28 20:43:32.035: DEBUG/MsgActions(2744): Matched contactId: 3
03-28 20:43:32.055: DEBUG/MsgActions(2744): Matched KActions: 1
03-28 20:43:32.055: DEBUG/MsgActions(2744): Matched Action: join
*/
