package com.failbone.android;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

public class MyStartServiceReceiver extends BroadcastReceiver {
	
	private Context mainContext;
	
	private String deviceId;
	private String latitude;
	private String longitude;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v("custom_message", "Start Receiver");
		mainContext = context;
		context.startService(new Intent(context.getApplicationContext(),
				LocationService.class));

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context.getApplicationContext());

		deviceId = sharedPreferences.getString("deviceId", "");
		latitude = sharedPreferences.getString("latitude", "");
		longitude = sharedPreferences.getString("longitude", "");

		if (deviceId.length() > 1 && latitude.length() > 1 && longitude.length() > 1) {
			new PostData().execute();
		}
	}
	
	private class PostData extends AsyncTask<Object, Integer, Exception> {

		private String response = "";
		private boolean successful = false;

		protected void onProgressUpdate(Integer... progress) {

		}

		protected void onPostExecute(Exception e) {
			
			Toast.makeText(mainContext.getApplicationContext(), response,
					Toast.LENGTH_LONG).show();

		}

		@Override
		protected Exception doInBackground(Object... params) {
			try {

				if (!postData()) {
					return new Exception("Failure");
				}

				successful = true;
				return new Exception("Success");
			} catch (Exception e) {
				return e;
			}
		}

		public boolean postData() {

			try {
				// Create a new HttpClient and Post Header
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost("http://failbone.com/update");

				// Add your data
				List<NameValuePair> postDataPairs = new ArrayList<NameValuePair>();

				postDataPairs.add(new BasicNameValuePair("deviceId", deviceId));
				postDataPairs.add(new BasicNameValuePair("os", "android"));
				postDataPairs.add(new BasicNameValuePair("lat", latitude));
				postDataPairs.add(new BasicNameValuePair("long", longitude));
				
				httpPost.setEntity(new UrlEncodedFormEntity(postDataPairs));

				// Execute HTTP Post Request
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				response = httpclient.execute(httpPost, responseHandler);

				Log.d("custom_message", "Response: " + response);
				return true;
			} catch (Exception ex) {
				Log.v("custom_message", ex.toString());
				return false;
			}
		}
	}
}