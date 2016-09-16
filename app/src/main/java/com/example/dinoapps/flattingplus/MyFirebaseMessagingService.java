package com.example.dinoapps.flattingplus;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if(remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String content = remoteMessage.getNotification().getBody();
            Log.d(TAG, "Notification Message Body: " + content + " Title: " + title);

            //Handle pulling down new data
            if(title.equals("New note added"))
            {
                //Need to pull down all the notes after the last edited time in our local db
                Intent i= new Intent(this, getService.class);
                //TODO update these to get them from the database
                String groupname = MainActivity.dbHelper.getGroup();
                String date = MainActivity.dbHelper.getMostRecentDate();
                Log.v(TAG, "Groupname: " + groupname + " Date: " + date);
                i.putExtra("groupname", groupname);
                i.putExtra("date", date);
                this.startService(i);
            }
            else if(title.equals("New money item added"))
            {

                //Need to pull down all the money notes after the last edited time in our local db
                Intent i= new Intent(this, getServiceMoney.class);

                String groupname = MainActivity.dbHelper.getGroup();
                String date = MainActivity.dbHelper.getMostRecentDateMoney();
                Log.v(TAG, "Groupname: " + groupname + " Date: " + date);
                i.putExtra("groupname", groupname);
                i.putExtra("date", date);
                this.startService(i);
            }
            else if(title.equals("New shopping item added"))
            {
                //Need to pull down all the money notes after the last edited time in our local db
                Intent i= new Intent(this, getService.class);

                String groupname = MainActivity.dbHelper.getGroup();
                String date = MainActivity.dbHelper.getMostRecentDateShopping();
                Log.v(TAG, "Groupname: " + groupname + " Date: " + date);
                i.putExtra("groupname", groupname);
                i.putExtra("date", date);
                i.putExtra("Type", "Shopping");
                this.startService(i);
            }
        }
        else
        {
            Log.v(TAG, "Recieved push notification, but notif had no body");
        }
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_arrow_drop_down_white_24dp)
                .setContentTitle("FCM Message")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}