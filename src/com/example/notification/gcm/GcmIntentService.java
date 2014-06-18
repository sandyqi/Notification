/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.notification.gcm;



import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.example.notification.Message1;
import com.example.notification.R;
import com.example.notification.databaseAD.DatabaseAdapter;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	String msg = "";
	String status, check, notifier,title ;
	String body="";
	private DatabaseAdapter dbAdapter;

	public GcmIntentService() {
		super("GcmIntentService");
	
	}

	public static final String TAG = "GCM Demo";

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		String messageType = gcm.getMessageType(intent);
		if (!extras.isEmpty()) { // has effect of unparcelling Bundle
			/*
			 * Filter messages based on message type. Since it is likely that
			 * GCM will be extended in the future with new message types, just
			 * ignore any message types you're not interested in, or that you
			 * don't recognize.
			 */
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				sendNotification("Deleted messages on server: "
						+ extras.toString());
				// If it's a regular GCM message, do some work.
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {
				// This loop represents the service doing some work.
				// for (int i = 0; i < 5; i++) {
				// Log.i(TAG, "Working... " + (i + 1)
				// + "/5 @ " + SystemClock.elapsedRealtime());
				// try {
				// Thread.sleep(5000);
				// } catch (InterruptedException e) {
				// }
				// }
				// Log.i(TAG, "Completed work @ " +
				// SystemClock.elapsedRealtime());

				// Post notification of received message.
				msg = extras.toString();
				status = extras.getString("status");
				check = extras.getString("check");
				notifier = "From: "+extras.getString("notifier");
				dbAdapter = new DatabaseAdapter(this);
				dbAdapter.open();		
				title = notifier;
				body = "The status is "+status+"   "+"   Detail: "+check ;
				dbAdapter.addNotif(title, body);  // add notification into database
				sendNotification("Received: " + "Something happened. Let's solve it quickly!");
				Log.i(TAG, "Received: " + extras.toString());
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		WakefulBroadcastReceiver.completeWakefulIntent(intent);
	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	private void sendNotification(String msg) {
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Uri soundUri = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		
		// get a resource's Uri
		String soundPath = "android.resource://"+getPackageName()+"/"+R.raw.sound;
		Uri soundURI=Uri.parse(soundPath);
		
		
		try {
			
			Intent ourIntent = new Intent(GcmIntentService.this, Message1.class);
			ourIntent.putExtra("GCM", body);
			ourIntent.putExtra("FROM", notifier);
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
					ourIntent, PendingIntent.FLAG_ONE_SHOT);
			
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					this)
					.setSmallIcon(R.drawable.a3)
					.setContentTitle("GCM Notification")
					.setAutoCancel(true)
					.setVibrate(new long[] { 1000, 1000, 1000, 1000 })
					.setSound(soundURI)
					.setWhen(System.currentTimeMillis())
					.setStyle(
							new NotificationCompat.BigTextStyle().bigText(msg))
					.setContentText(msg);
			mBuilder.setContentIntent(contentIntent);
			// mBuilder
			mNotificationManager.notify(0, mBuilder.build());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
