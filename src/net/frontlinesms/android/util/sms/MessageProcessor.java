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

import android.app.Activity;
import android.content.Context;

import net.frontlinesms.android.FrontlineSMS;
import net.frontlinesms.android.KeywordActionProcessor;
import net.frontlinesms.android.model.KeywordAction;
import net.frontlinesms.android.model.KeywordActionDao;

public class MessageProcessor {
	//private final Logger log = Logger.getLogger(this);
	private final KeywordActionProcessor mKeywordActionProcessor;
	private final KeywordActionDao mKeywordActionDao;
    //private final PollDao mPollDao;
    private final Context mContext;
//    private final ContentResolver mResolver;
	
	public MessageProcessor(Context context) {
        this.mContext = context;
		this.mKeywordActionDao = new KeywordActionDao(context.getContentResolver());
		this.mKeywordActionProcessor = new KeywordActionProcessor(context);
//        this.mResolver = resolver;
	}

    // process keyword actions
	public void process(WholeSmsMessage message) {
        boolean allowAnywhere = mContext.getSharedPreferences(FrontlineSMS.SHARED_PREFS_ID, Activity.MODE_PRIVATE)
                .getBoolean(FrontlineSMS.PREF_SETTINGS_ALLOW_KEYWORD_ANYWHERE, false);
		KeywordAction[] actions = this.mKeywordActionDao.getActions(message.getMessageBody(), allowAnywhere);
		for(KeywordAction action : actions) {
			this.mKeywordActionProcessor.process(action, message);
		}

        //if actions is empty,
        //if there's an active poll,
        //if poll is restricted to original recipients && if responder was an original recipient  (dont do this for now),
        //if message matches a poll keyword
        //process the message for that poll
	}
}
