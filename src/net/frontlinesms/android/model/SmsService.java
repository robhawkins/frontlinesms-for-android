package net.frontlinesms.android.model;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;
import net.frontlinesms.android.util.sms.PropertySubstituter;
import net.frontlinesms.android.util.sms.SmsReceiver;
import net.frontlinesms.android.util.sms.WholeSmsMessage;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mathias.lin
 * Date: 3/19/11
 * Time: 1:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class SmsService {

    private final static String TAG = PIMService.class.getSimpleName();

    /*public static void sendMessage(final Context context, List<Contact> contacts, String message) {
        individualizeAndSendMessage(context, contacts, message, null, null);
    }*/

    /**
     * Sends out a SMS to a provided list of recipients (contacts).
     * @param context Context
     * @param contacts Recipient list
     * @param message Message to be sent
     */
    public static void individualizeAndSendMessage(final Context context, List<Contact> contacts,
                                   String message, String keyword, WholeSmsMessage sms) {

        final PropertySubstituter propSub = new PropertySubstituter(context);
        Log.d(TAG, "individualizeAndSendMessage for : " + contacts.size() + " contacts");



        //---when the SMS has been sent---
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent intent) {

                WholeSmsMessage message = SmsReceiver.getMessagesFromIntent(intent);
                Log.d("SmsService", "Sent body: " + message.getMessageBody());
                Log.d("SmsService", "Sent number: " + message.getOriginatingAddress());

                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS sent", Toast.LENGTH_SHORT).show();

                        ContentValues values = new ContentValues();
                        values.put("address", message.getOriginatingAddress());
                        values.put("body", message.getMessageBody());
                        context.getContentResolver().insert(Uri.parse("content://sms/sent"), values);

                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "Generic failure", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "No service", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "Null PDU", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "Radio off", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter("SMS_SENT"));

        //---when the SMS has been delivered---
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS delivered", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(context, "SMS not delivered", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter("SMS_DELIVERED"));


        for (Contact contact:contacts) {

            Log.d(TAG, "individualizeAndSendMessage to contact id: " + contact.getId().toString());

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

                // store the sent sms in the sent folder (that shouldn't be necessary?!)
//                ContentValues values = new ContentValues();
//                values.put("address", phone);
//                values.put("body", formattedMessage);
//                context.getContentResolver().insert(Uri.parse("content://sms/sent"), values);
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
