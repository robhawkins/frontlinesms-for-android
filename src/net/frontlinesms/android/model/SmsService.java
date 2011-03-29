package net.frontlinesms.android.model;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import net.frontlinesms.android.model.model.Contact;
import net.frontlinesms.android.util.sms.PropertySubstituter;
import net.frontlinesms.android.util.sms.WholeSmsMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: mathias.lin
 * Date: 3/19/11
 * Time: 1:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class SmsService {

    private final static String TAG = PIMService.class.getSimpleName();

    public static void sendMessage(final Context context, List<Contact> contacts, String message) {
        individualizeAndSendMessage(context, contacts, message, null, null);
    }

    /**
     * Sends out a SMS to a provided list of recipients (contacts).
     * @param context Context
     * @param contacts Recipient list
     * @param message Message to be sent
     */
    public static void individualizeAndSendMessage(final Context context, List<Contact> contacts,
                                   String message, String keyword, WholeSmsMessage sms) {

        final PropertySubstituter propSub = new PropertySubstituter(context);
        Log.d(TAG, "Send message to contacts: " + contacts.size());

        for (Contact contact:contacts) {

            Log.d(TAG, "Send message to contact id: " + contact.getId().toString());

            String formattedMessage = propSub.substitute(keyword, sms, contact, message);

            Cursor pCur = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    new String[]{contact.getId().toString()}, null);

            while (pCur.moveToNext()) {
                String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Log.d(TAG, "Phone Number: " + phone);
//                    phone = "+8618688200424";
//                    phone = "+8613802849305";
                PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent("SMS_SENT"), 0);
                PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0, new Intent("SMS_DELIVERED"), 0);
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phone, null, formattedMessage, sentPI, deliveredPI);
            }

        }

    }


    /*public class SmsSender {
        private Logger log = Logger.getLogger(this);
        // SMS Service centre number.  Set this to <code>null</code> to use the default.
        private String scAddress;
        private SmsManager smsManager = SmsManager.getDefault();

        public void sendSms(String destinationAddress, String text, PendingIntent sentIntent, PendingIntent deliveryIntent) {
            // TODO handle splitting for long SMS
            log.info("Sending 1 SMS to <" + destinationAddress + ">: " + text);
            this.smsManager.sendTextMessage(destinationAddress, this.scAddress, text, sentIntent, deliveryIntent);
        }
    }*/


}
