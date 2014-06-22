package com.failbone.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class LocationReceiver extends BroadcastReceiver implements LocationListener {
	
    @Override
    public void onReceive(Context context, Intent intent) {
        
    }

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		Log.v("location finally", Double.toString(location.getLatitude()));
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
}