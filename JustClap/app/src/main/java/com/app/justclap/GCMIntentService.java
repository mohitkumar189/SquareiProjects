package com.app.justclap;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import static com.app.justclap.CommonUtilities.SENDER_ID;
import static com.app.justclap.CommonUtilities.displayMessage;

import com.app.justclap.activities.Splash;
import com.app.justclap.utils.AppUtils;
import com.google.android.gcm.GCMBaseIntentService;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;


public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = "GCMIntentService";
    String pushType = "";
    String imageLink = "";
    Bitmap image = null;

    public GCMIntentService() {
        super(SENDER_ID);
    }

    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {

        try {
            if (!(registrationId.equals(""))) {
                SharedPreferences sub_share = getSharedPreferences("regid", 0);
                SharedPreferences.Editor editor11 = sub_share.edit();
                editor11.putString("rid", registrationId);
                editor11.commit();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        //Toast.makeText(getBaseContext(), "reg   "+registrationId, Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Device registered: regId = " + registrationId);
        displayMessage(context, "Your device registred with GCM");
        Log.d("NAME", "");
        ServerUtilities.register(context, "", "", registrationId);
    }

    /**
     * Method called on device un registred
     */
    @Override
    protected void onUnregistered(Context context, String registrationId) {

        //Toast.makeText(getBaseContext(), "unre   "+registrationId, Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Device unregistered");
        displayMessage(context, getString(R.string.gcm_unregistered));
        ServerUtilities.unregister(context, registrationId);
    }

    /**
     * Method called on Receiving a new message
     */
    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message");
        Bundle b = intent.getExtras();
        String message = b.getString("message");
        if (b.containsKey("img")) {
            imageLink = b.getString("img");
            Log.e("imageLink", "*" + b.getString("img"));
        }
        Log.e("imageLink", "*" + b.getString("img"));
        Log.e("pushType", "*" + b.getString("type"));
        pushType = b.getString("type");
        displayMessage(context, message);
        // notifies user


        if (pushType.equalsIgnoreCase("3")) {
            Log.e("isChatVisible", "*" + AppUtils.getIsChatVisible(context));
            Log.e("user_id", "*" + b.getString("user_id"));
            if (AppUtils.getIsChatVisible(context) == false) {
                generateNotification(context, message);
            } else if (!b.getString("user_id").equalsIgnoreCase(AppUtils.getChatUserId(context))) {
                generateNotification(context, message);
            }

        } else {
            generateNotification(context, message);
        }
    }

    /**
     * Method called on receiving a deleted message
     */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }

    /**
     * Method called on Error
     */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        displayMessage(context, getString(R.string.gcm_recoverable_error,
                errorId));
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private void generateNotification(Context context, String message) {
        //message="jai sri ram";
        int currentAPIVersion = android.os.Build.VERSION.SDK_INT;
        int icon = 0;
        if (currentAPIVersion == Build.VERSION_CODES.KITKAT) {
            icon = R.drawable.push_notefication;

        } else if (currentAPIVersion >= Build.VERSION_CODES.LOLLIPOP) {
            icon = R.drawable.push_notificationsnew;
        }


        Random r = new Random();
        int when = r.nextInt(1000);
        //long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        //  Notification notification = new Notification(icon, message, when);

        String title = context.getString(R.string.app_name);

        if (!imageLink.equalsIgnoreCase("")) {
            URL url = null;
            try {
                url = new URL(imageLink);
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Intent notificationIntent = new Intent(context, Splash.class);
        //notificationIntent.putExtra("id", message);

        // notificationIntent.putExtra("marker", marker);

        //  saveLogoutData(marker);

        // Log.e("marker notificationIntent", marker);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);

        Notification notification;
        if (!imageLink.equalsIgnoreCase("")) {

            notification = new Notification.Builder(context)
                    .setContentTitle(title)
                    .setStyle(new Notification.BigPictureStyle().bigPicture(image).setSummaryText(message))
                    .setContentIntent(intent)
                    .setSmallIcon(icon)
                    .setContentText(message)
                    .setAutoCancel(true).build();
        } else {
            notification = new Notification.Builder(context)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(icon)
                    .setContentIntent(intent)
                    .setAutoCancel(true).build();
        }

        if (currentAPIVersion >= Build.VERSION_CODES.LOLLIPOP) {
            notification.color = getResources().getColor(R.color.txt_black);
        }

        //   notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;

        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");

        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(when, notification);

    }
    
    
  /*  private void saveLogoutData(String data) 
    {
     SharedPreferences sp = getSharedPreferences("logout", 0);
     Editor e = sp.edit();
     e.putString("logout", data);
     e.commit();
    }
*/
}
