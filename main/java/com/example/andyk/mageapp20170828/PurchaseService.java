package com.example.andyk.mageapp20170828;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.ParcelUuid;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.util.Log;

import com.example.andyk.mageapp20170828.helper.SharedPref;
import com.example.andyk.mageapp20170828.rest.Soap;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class PurchaseService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this

    protected static final String TAG = PurchaseService.class.getName();
    protected static final int ACTIVITY_REQUEST_CODE = 0;
    protected static final int NOTIFICATION_ID = 0;
    protected static final String MAGE_ADMIN_URL = "http://mage.testing.acacloud.com/admin";
    protected static final long ALARM_INTERVAL_ONE_MINUTE = (1000 * 60);
    protected static final int SERVICE_REQUEST_CODE = 1;

    public PurchaseService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "PurchaseService.onHandleIntent() called..");

        // check background service availability..
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
        boolean networkOk = (netInfo.isConnected() && netInfo.isAvailable());
        if (!networkOk) {
            Log.d(TAG, "No background network available!");
            return;
        }

        // get order info
        String lastOrderNumber = SharedPref.getLastOrderNumber(this);
        String orderNumber = new Soap().fetchLastOrderNumber();

        String msg = String.format("Last order number: #%s, new order number: #%s",
                lastOrderNumber, orderNumber);
        Log.d(TAG, msg);

        if (orderNumber == null) {
            Log.d(TAG, "Could not fetch the new order.");
            return;
        }

        if (orderNumber.equals(lastOrderNumber)) {
            Log.d(TAG, "No new order found!");
        } else {
            sendNotification();
            Log.d(TAG, "New order found.");
        }

        SharedPref.putLastOrderNumber(this, orderNumber);
        Log.d(TAG, "PurchaseService.onHandleIntent() finished..");
    }

    protected static Intent newIntent(Context c) {
        return new Intent(c, PurchaseService.class);
    }

    public static void setPurchaseService(Context context, boolean isOn) {
        Log.d(TAG, "is alarm on: " + isOn);
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long triggerMillis = SystemClock.elapsedRealtime();
        long intervalMillis = ALARM_INTERVAL_ONE_MINUTE;
        Intent service = newIntent(context);
        PendingIntent operation = PendingIntent.getService(context, SERVICE_REQUEST_CODE, service,
                PendingIntent.FLAG_UPDATE_CURRENT);
        if (isOn) {
            alarmMgr.cancel(operation);
            operation.cancel();
        } else {
            alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, triggerMillis,
                    intervalMillis, operation);
        }
    }

    public static boolean isServiceAlarmOn(Context context) {
        Intent service = newIntent(context);
        PendingIntent operation = PendingIntent.getService(context, SERVICE_REQUEST_CODE, service,
                PendingIntent.FLAG_NO_CREATE);
        return (operation != null);
    }

    protected void sendNotification() {
        Resources res = getResources();
        String title = res.getString(R.string.purchase_notif_title);
        String text = res.getString(R.string.purchase_notif_text);

        Uri uri = Uri.parse(MAGE_ADMIN_URL);
        Intent browser = new Intent(Intent.ACTION_VIEW, uri);
        PendingIntent operation = PendingIntent.getActivity(this, ACTIVITY_REQUEST_CODE, browser,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notif = new NotificationCompat.Builder(this)
                .setTicker(title)
                .setSmallIcon(android.R.drawable.ic_notification_overlay)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(operation)
                .setAutoCancel(true)
                .build();

        NotificationManagerCompat notifMgr = NotificationManagerCompat.from(this);
        notifMgr.notify(NOTIFICATION_ID, notif);
    }
}
