/**
 * 
 */
package net.frontlinesms.android.util.sms;

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
	public void handleReceivedSms(WholeSmsMessage message) {
        Log.d(TAG, "handleReceivedSms " + message);
		this.messageProcessor.process(message);
	}

}

