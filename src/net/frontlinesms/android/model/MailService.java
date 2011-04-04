/**
 * This software is written by Meta Healthcare Systems Ltd. and subject
 * to a contract between Meta Healthcare Systems and its customer.
 * <p/>
 * This software stays property of Meta Healthcare Systems unless differing
 * arrangements between Meta Healthcare Systems and its customer apply.
 * <p/>
 * Meta Healthcare Systems Ltd.
 * 20/F Central Tower
 * 28 Queen's Road Central
 * Hong Kong
 * <p/>
 * Tel: +852 8199 9605
 * http://www.metahealthcare.com
 * mailto:info@metahealthcare.com
 * <p/>
 * (c)2010 Meta Healthcare Systems Ltd. All rights reserved.
 */
package net.frontlinesms.android.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import net.frontlinesms.android.FrontlineSMS;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.util.Calendar;
import java.util.Properties;

public class MailService {

    private Context mContext;
    private SharedPreferences mPrefs;

	private String toList;
	private String subject;
	private String from;
	private String txtBody;
	private boolean authenticationRequired = false;

   /* public MailService(Context context) {
        this.mContext = context;
    }*/

	public MailService(Context context, String from, String toList, String subject, String txtBody) {
        this.mContext = context;
		this.txtBody = txtBody;
		this.subject = subject;
		this.from = from;
		this.toList = toList;
		this.authenticationRequired = true;
	}

	/*public void sendAuthenticated() throws AddressException, MessagingException {
		authenticationRequired = true;
		send();
	}*/

	/**
	 * Send an e-mail
	 * 
	 * @throws MessagingException
	 * @throws AddressException
	 */
	public void send() throws AddressException, MessagingException {
        mPrefs = mContext.getSharedPreferences(FrontlineSMS.SHARED_PREFS_ID, Activity.MODE_PRIVATE);

		Properties props = new Properties();
		// set the host smtp address
		props.put("mail.smtp.host", mPrefs.getString(FrontlineSMS.PREF_SETTINGS_EMAIL_SERVER, ""));
		props.put("mail.user", from);

        if (mPrefs.getBoolean(FrontlineSMS.PREF_SETTINGS_EMAIL_SSL, false)) {
            props.put("mail.smtp.starttls.enable", "true");  // needed for gmail
            props.put("mail.smtp.auth", "true"); // needed for gmail
            props.put("mail.smtp.port", mPrefs.getString(FrontlineSMS.PREF_SETTINGS_EMAIL_PORT, "587"));
        } else {
            props.put("mail.smtp.port", mPrefs.getString(FrontlineSMS.PREF_SETTINGS_EMAIL_PORT, "25"));
        }

		/*Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("mobile@mobileguide.asia", "86D288");
            }
        };*/

		Session session;
		if (authenticationRequired) {
			Authenticator auth = new SMTPAuthenticator();
			props.put("mail.smtp.auth", "true");
			session = Session.getDefaultInstance(props, auth);
		} else {
			session = Session.getDefaultInstance(props, null);			
		}

		// get the default session
		session.setDebug(true);

		// create message
		Message msg = new javax.mail.internet.MimeMessage(session);

		// set from and to address
        try {
		    msg.setFrom(new InternetAddress(from, from));
            msg.setReplyTo(new InternetAddress[]{new InternetAddress(from,from)});
        } catch (Exception e) {
            msg.setFrom(new InternetAddress(from));
            msg.setReplyTo(new InternetAddress[]{new InternetAddress(from)});
        }

		// set send date
		msg.setSentDate(Calendar.getInstance().getTime());

		// parse the recipients TO address
		java.util.StringTokenizer st = new java.util.StringTokenizer(toList, ",");
		int numberOfRecipients = st.countTokens();
		InternetAddress[] addressTo = new InternetAddress[numberOfRecipients];

		int i = 0;
		while (st.hasMoreTokens()) {
			addressTo[i++] = new javax.mail.internet.InternetAddress(st
					.nextToken());
		}
		msg.setRecipients(javax.mail.Message.RecipientType.TO, addressTo);

		// set header
		msg.addHeader("X-Mailer", "FrontlineSMS");
		msg.addHeader("Precedence", "bulk");
		// setting the subject and content type
		msg.setSubject(subject);

		Multipart mp = new MimeMultipart("related");

        // set body message
		MimeBodyPart bodyMsg = new MimeBodyPart();
		bodyMsg.setText(txtBody, "iso-8859-1");
        //bodyMsg.setContent(htmlBody, "text/html");
		mp.addBodyPart(bodyMsg);

		msg.setContent(mp);

		// send it
		try {
			javax.mail.Transport.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
			// logger.error(e.getStackTrace());
		}

	}

	/**
	 * SimpleAuthenticator is used to do simple authentication when the SMTP
	 * server requires it.
	 */
	private class SMTPAuthenticator extends javax.mail.Authenticator {

	    @Override
		protected PasswordAuthentication getPasswordAuthentication() {
            String username = mPrefs.getString(FrontlineSMS.PREF_SETTINGS_EMAIL_USERNAME, "");
			String password = mPrefs.getString(FrontlineSMS.PREF_SETTINGS_EMAIL_PASSWORD, "");
			return new PasswordAuthentication(username, password);
		}
	}


}
