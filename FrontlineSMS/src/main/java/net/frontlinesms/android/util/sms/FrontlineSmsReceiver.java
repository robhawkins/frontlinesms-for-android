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

import android.os.AsyncTask;
import android.util.Log;

/**
 * @author Alex Anderson
 *
 */
public class FrontlineSmsReceiver extends SmsReceiver implements IReceivedSmsHandler {

    private final static String TAG = FrontlineSmsReceiver.class.getSimpleName();
    private MessageProcessor messageProcessor;

	@Override
	protected void init() {
		this.messageProcessor = new MessageProcessor(this.getContext());
	}
	
	@Override
	protected IReceivedSmsHandler getReceivedSmsHandler() {
		return this;
	}

	@Override
	public void handleReceivedSms(final WholeSmsMessage message) {
        Log.d(TAG, "handleReceivedSms " + message);
        new AsyncTask() {

            @Override
            protected Object doInBackground(Object... objects) {
                FrontlineSmsReceiver.this.messageProcessor.process(message);
                return null;
            }
        }.execute();

	}

}

