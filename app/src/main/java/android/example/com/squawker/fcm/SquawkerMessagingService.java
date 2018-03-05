package android.example.com.squawker.fcm;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.example.com.squawker.provider.SquawkContract;
import android.example.com.squawker.provider.SquawkProvider;
import android.os.AsyncTask;
import android.os.IBinder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

//  (1) Make a new Service in the fcm package that extends from FirebaseMessagingService.


public class SquawkerMessagingService extends FirebaseMessagingService {
// (2) As part of the new Service - Override onMessageReceived. This method will
// be triggered whenever a squawk is received. You can get the data from the squawk
// message using getData(). When you send a test message, this data will include the
// following key/value pairs:
// test: true
// author: Ex. "TestAccount"
// authorKey: Ex. "key_test"
// message: Ex. "Hello world"
// date: Ex. 1484358455343

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Map<String, String> data = remoteMessage.getData();
        String author = data.get(SquawkContract.COLUMN_AUTHOR);
        String author_key = data.get(SquawkContract.COLUMN_AUTHOR_KEY);
        String message = data.get(SquawkContract.COLUMN_MESSAGE);
        String date = data.get(SquawkContract.COLUMN_DATE);

        String test = data.get("test");

        if(author.isEmpty() || author_key.isEmpty() || message.isEmpty() || date.isEmpty()
               || (test != null && !test.equalsIgnoreCase("true"))) return;

        //  (3) As part of the new Service - If there is message data, get the data using
        // the keys and do two things with it :
        // 1. Display a notification with the first 30 character of the message
        NotificationUtils.NotifyOnSquawk(getApplicationContext(),
                author + " posted message",
                message.substring(0, message.length() > 30 ? 30 : message.length()));
        // 2. Use the content provider to insert a new message into the local database
        // Hint: You shouldn't be doing content provider operations on the main thread.
        // If you don't know how to make notifications or interact with a content provider
        // look at the notes in the classroom for help.

        final ContentValues cv = new ContentValues();

        cv.put(SquawkContract.COLUMN_AUTHOR, author);
        cv.put(SquawkContract.COLUMN_DATE, date);
        cv.put(SquawkContract.COLUMN_MESSAGE, message);
        cv.put(SquawkContract.COLUMN_AUTHOR_KEY, author_key);

        Runnable runnable = new Runnable() {
            public void run() {
                getApplicationContext().getContentResolver().insert(SquawkProvider.SquawkMessages.CONTENT_URI, cv);
            }
        };
        new Thread(runnable).run();

    }
}
