package com.failbone.android;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMConstants;

public class GCMIntentService extends GCMBaseIntentService {

	@Override
	protected void onError(Context context, String errorId) {

		if (GCMConstants.ERROR_ACCOUNT_MISSING.equalsIgnoreCase(errorId)) {
			Log.v("custom_message", "Error Account Missing");
		} else if (GCMConstants.ERROR_AUTHENTICATION_FAILED
				.equalsIgnoreCase(errorId)) {
			Log.v("custom_message", "Error Authentication Failed");
		} else if (GCMConstants.ERROR_INVALID_PARAMETERS
				.equalsIgnoreCase(errorId)) {
			Log.v("custom_message", "Error Invalid Parameters");
		} else if (GCMConstants.ERROR_INVALID_SENDER.equalsIgnoreCase(errorId)) {
			Log.v("custom_message", "Error Invalid Sender");
		} else if (GCMConstants.ERROR_PHONE_REGISTRATION_ERROR
				.equalsIgnoreCase(errorId)) {
			Log.v("custom_message", "Error Phone Registration Error");
		} else if (GCMConstants.ERROR_SERVICE_NOT_AVAILABLE
				.equalsIgnoreCase(errorId)) {
			Log.v("custom_message", "Error Service Not Available");
		}
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		
		MediaPlayer mp = MediaPlayer
				.create(context.getApplicationContext(), R.raw.bone);
		mp.start();
		
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_launcher, "You Got Boned", System.currentTimeMillis());
		
		Intent notificationIntent = new Intent(context.getApplicationContext(), MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, notificationIntent, 0);
		
		notification.setLatestEventInfo(context.getApplicationContext(), "Boned", "You Got Boned", pendingIntent);
		
		notificationManager.notify(9999, notification);
		
		Log.v("custom_message", "push notification received");
	}

	@Override
	protected void onRegistered(Context context, String regId) {

		Log.v("custom_message", "Successfull Registration : " + regId);
	}

	@Override
	protected void onUnregistered(Context context, String regId) {

		Log.v("custom_message", "Successfully Unregistred : " + regId);
	}

	@Override
	protected String[] getSenderIds(Context context) {
		return super.getSenderIds(context);
	}

	@Override
	protected void onDeletedMessages(Context context, int total) {
		super.onDeletedMessages(context, total);
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		return super.onRecoverableError(context, errorId);
	}
}