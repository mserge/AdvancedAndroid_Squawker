package android.example.com.squawker.fcm;

/**
 * Created by mserge on 05.03.2018.
 */


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.example.com.squawker.MainActivity;
import android.example.com.squawker.R;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;


/**
 * Utility class for creating hydration notifications
 */
public class NotificationUtils {

    /*
     * This notification ID can be used to access our notification after we've displayed it. This
     * can be handy when we need to cancel the notification, or perhaps update it. This number is
     * arbitrary and can be set to whatever you like. 1138 is in no way significant.
     */
    private static final int SQUAWKER_NOTIFICATION_ID = 1138;
    /**
     * This pending intent id is used to uniquely reference the pending intent
     */
    private static final int SQUAWKER_PENDING_INTENT_ID = 3417;
    /**
     * This notification channel id is used to link notifications to this channel
     */
    private static final String SQUAWKER_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel";


    public static void NotifyOnSquawk(Context context, String title, String text) {
        // COMPLETED (8) Get the NotificationManager using context.getSystemService
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        // COMPLETED (9) Create a notification channel for Android O devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    SQUAWKER_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }
        // COMPLETED (10) In the remindUser method use NotificationCompat.Builder to create a notification
        // that:
        // - has a color of R.colorPrimary - use ContextCompat.getColor to get a compatible color
        // - has ic_drink_notification as the small icon
        // - uses icon returned by the largeIcon helper method as the large icon
        // - sets the title to the charging_reminder_notification_title String resource
        // - sets the text to the charging_reminder_notification_body String resource
        // - sets the style to NotificationCompat.BigTextStyle().bigText(text)
        // - sets the notification defaults to vibrate
        // - uses the content intent returned by the contentIntent helper method for the contentIntent
        // - automatically cancels the notification when the notification is clicked
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, SQUAWKER_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_duck)
                // .setLargeIcon(largeIcon(context))
                .setContentTitle(title)
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true);

        // COMPLETED (11) If the build version is greater than JELLY_BEAN and lower than OREO,
        // set the notification's priority to PRIORITY_HIGH.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        // COMPLETED (12) Trigger the notification by calling notify on the NotificationManager.
        // Pass in a unique ID of your choosing for the notification and notificationBuilder.build()
        notificationManager.notify(SQUAWKER_NOTIFICATION_ID, notificationBuilder.build());
    }

    // COMPLETED (1) Create a helper method called contentIntent with a single parameter for a Context. It
    // should return a PendingIntent. This method will create the pending intent which will trigger when
    // the notification is pressed. This pending intent should open up the MainActivity.
    private static PendingIntent contentIntent(Context context) {
        // COMPLETED (2) Create an intent that opens up the MainActivity
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        // COMPLETED (3) Create a PendingIntent using getActivity that:
        // - Take the context passed in as a parameter
        // - Takes an unique integer ID for the pending intent (you can create a constant for
        //   this integer above
        // - Takes the intent to open the MainActivity you just created; this is what is triggered
        //   when the notification is triggered
        // - Has the flag FLAG_UPDATE_CURRENT, so that if the intent is created again, keep the
        // intent but update the data
        return PendingIntent.getActivity(
                context,
                SQUAWKER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }


}

