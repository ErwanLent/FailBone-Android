package com.failbone.android;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class LocationService extends Service {
	WakeLock wakeLock;

	private LocationManager locationManager;

	private Context context;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		context = getApplicationContext();

		PowerManager pm = (PowerManager) getSystemService(this.POWER_SERVICE);

		wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DoNotSleep");

		// Toast.makeText(getApplicationContext(), "Service Created",
		// Toast.LENGTH_SHORT).show();

		Log.e("Google", "Service Created");

	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);

		Log.e("Google", "Service Started");

		locationManager = (LocationManager) getApplicationContext()
				.getSystemService(Context.LOCATION_SERVICE);

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				5000, 5, listener);

	}

	private LocationListener listener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			if (location == null)
				return;

			Log.e("custom_message", Double.toString(location.getLatitude()));

			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext()
							.getApplicationContext());

			SharedPreferences.Editor editor = sharedPreferences.edit();

			editor.putString("latitude", Double.toString(location.getLatitude()));
			editor.putString("longitude", Double.toString(location.getLongitude()));

			editor.commit();

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		wakeLock.release();

	}

}
