package net.frontlinesms.android;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import net.frontlinesms.android.activity.JobList;
import net.frontlinesms.android.model.IJobDao;
import net.frontlinesms.android.model.JobDao;
import net.frontlinesms.android.model.JobService;

import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

public class FrontlineSMSService extends Service {

    private NotificationManager mNM;
    private TimerTask mTT;
    private Timer mT;

    private final int CHECK_FOR_JOBS_INTERVAL = 6000;
    private static boolean bTaskRunning = false;

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = R.string.local_service_started;

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        FrontlineSMSService getService() {
            return FrontlineSMSService.this;
        }
    }

    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        // Display a notification about us starting.  We put an icon in the status bar.
        showNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.

        mT = new Timer();
        mTT = new FrontlineSMSTimerTask();
        mT.scheduleAtFixedRate(mTT, 0, CHECK_FOR_JOBS_INTERVAL);


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);

        // Tell the user we stopped.
        Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.local_service_started);

        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification(R.drawable.service_icon, text,
                System.currentTimeMillis());

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, JobList.class), 0);

        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, getText(R.string.local_service_label),
                       text, contentIntent);

        // Send the notification.
        mNM.notify(NOTIFICATION, notification);
    }



    class FrontlineSMSTimerTask extends TimerTask {
        @Override
        public void run() {
            Log.d("FrontlineSMSTimerTask", "TT run!");
            if (!bTaskRunning) {
                bTaskRunning = true;
                JobService.executeDueJobs(FrontlineSMSService.this);
                bTaskRunning = false;
            }

        }
    }

}
