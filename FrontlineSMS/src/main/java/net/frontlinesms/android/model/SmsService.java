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
import net.frontlinesms.android.FrontlineSMS;
import net.frontlinesms.android.util.sms.PropertySubstituter;
import net.frontlinesms.android.util.sms.WholeSmsMessage;

import java.util.List;

public class SmsService {

    private static final boolean TEST_MODE_DONT_ACTUALLY_SEND_SMS = false;
    private final static String TAG = SmsService.class.getSimpleName();

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

        final SharedPreferences prefs = context.getSharedPreferences(FrontlineSMS.SHARED_PREFS_ID, Activity.MODE_PRIVATE);
        int deliveryDelay = prefs.getInt(FrontlineSMS.PREF_SETTINGS_DELAY, 0);

        final PropertySubstituter propSub = new PropertySubstituter(context);
        Log.d(TAG, "individualizeAndSendMessage for : " + contacts.size() + " contacts");

        final IJobDao jobDao = new JobDao(context.getContentResolver());

        //---when the SMS has been sent---
        try {
            context.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context arg0, Intent intent) {


                    Log.d("SmsService", "Result data: " + getResultData());
                    Log.d("SmsService", "Result data string: " + (getResultExtras(false)==null?0:getResultExtras(false).size()));

                    Log.d("SmsService", "Intent data: " + intent.getData());
                    Log.d("SmsService", "Intent data string: " + intent.getDataString());
                    Log.d("SmsService", "Intent data string: " + (intent.getExtras()==null?0:intent.getExtras().size()));

    //                WholeSmsMessage message = SmsReceiver.getMessagesFromIntent(intent);
    //                if (message!=null) {
    //                    Log.d("SmsService", "Sent body: " + message.getMessageBody());
    //                    Log.d("SmsService", "Sent number: " + message.getOriginatingAddress());
    //                }
    //
    //                Log.d("SmsService", "Intent data: " + intent.getData());
    //                Log.d("SmsService", "Intent data string: " + intent.getDataString());
    //                Log.d("SmsService", "Intent data string: " + (intent.getExtras()==null?0:intent.getExtras().size()));

                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                           Toast.makeText(context, "SMS sent", Toast.LENGTH_SHORT).show();
                           Log.d("SmsService", "Intent data number: " + intent.getStringExtra("number"));
                           Log.d("SmsService", "Intent data number: " + intent.getBundleExtra("number"));

    //                        if (message!=null) {
    //                            ContentValues values = new ContentValues();
    //                            values.put("address", message.getOriginatingAddress());
    //                            values.put("body", message.getMessageBody());
    //                            context.getContentResolver().insert(Uri.parse("content://sms/sent"), values);
    //                        }

                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            //Toast.makeText(context, "Generic failure", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            //Toast.makeText(context, "No service", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            //Toast.makeText(context, "Null PDU", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            //Toast.makeText(context, "Radio off", Toast.LENGTH_SHORT).show();
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
                            //Toast.makeText(context, "SMS delivered", Toast.LENGTH_SHORT).show();
                            break;
                        case Activity.RESULT_CANCELED:
                            //Toast.makeText(context, "SMS not delivered", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }, new IntentFilter("SMS_DELIVERED"));
        } catch (ReceiverCallNotAllowedException e) {
            Log.e(TAG, "IntentReceiver components are not allowed to register to receive intents.", e);
        }

        for (Contact contact:contacts) {

//            Log.d(TAG, "individualizeAndSendMessage to contact id: " + contact.getId().toString());
//            Log.d(TAG, "individualizeAndSendMessage message: " + message);
//            Log.d(TAG, "individualizeAndSendMessage contact: " + contact);

            String formattedMessage = propSub.substitute(keyword, sms, contact, message);

            Cursor pCur = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?" +
                        " AND " + ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
                    new String[]{contact.getId().toString()}, null);

            while (pCur!=null && pCur.moveToNext()) {
                String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Log.d(TAG, "Phone Number: " + phone);
//                    phone = "+8618688200424";
//                    phone = "+8613802849305";


                // TODO This delivery section needs to move into the service (tbd)...
                // === temporary section, we don't send sms right away but put it in the delivery queue ===
                Intent sendIntent = new Intent("SMS_SENT");
                sendIntent.putExtra("message", formattedMessage);
                sendIntent.putExtra("number", phone);
                PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, sendIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                Intent deliveredIntent = new Intent("SMS_DELIVERED");
                deliveredIntent.putExtra("message", formattedMessage);
                deliveredIntent.putExtra("number", phone);
                PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0, deliveredIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                if (!TEST_MODE_DONT_ACTUALLY_SEND_SMS) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phone, null, formattedMessage, sentPI, deliveredPI);

                    // store the sent sms in the sent folder (that shouldn't be necessary?!)
                    ContentValues values = new ContentValues();
                    values.put("address", phone);
                    values.put("body", formattedMessage);
                    context.getContentResolver().insert(Uri.parse("content://sms/sent"), values);
                    // === end of tmp section ===
                }


                // store the delivery as a job
                //Job job = new Job(Job.Type.SMS_JOB, phone, null, formattedMessage);
                // KeywordAction.Type type, String recipient, String subject, String text
                //jobDao.saveOrUpdateJob(job);

                if (deliveryDelay>0) try {Thread.sleep(deliveryDelay);} catch (Exception e) {}
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
