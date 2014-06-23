package com.failbone.android;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;

public class MainActivity extends Activity {

	public double latitude = 0;
	public double longitude = 0;

	private static final String SENDER_ID = "67251574531";
	private static final long REPEAT_TIME = 1000 * 30;

	public String registrationId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TextView countTextView = (TextView) findViewById(R.id.countTextView);
		countTextView.setVisibility(View.GONE);

		// Custom font
		Typeface font = Typeface
				.createFromAsset(getAssets(), "Bariol_Bold.otf");
		Typeface chunkFont = Typeface.createFromAsset(getAssets(), "Chunk.otf");
		Button boneButton = (Button) findViewById(R.id.boneButton);

		boneButton.setTypeface(font);
		countTextView.setTypeface(chunkFont);

		// Register Push Notifications
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		registrationId = GCMRegistrar.getRegistrationId(this);
		if (registrationId.equals("")) {
			GCMRegistrar.register(this, SENDER_ID);
			registrationId = GCMRegistrar.getRegistrationId(this);
		} else {
			Log.v("custom_message", "Already registered : " + registrationId);
		}

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext()
						.getApplicationContext());
		
		if (!sharedPreferences.getBoolean("registered", false))
		{
			new Register().execute();
		}

		if (sharedPreferences.getString("deviceId", "").length() <= 1) {
			SharedPreferences.Editor editor = sharedPreferences.edit();

			editor.putString("deviceId", registrationId);

			editor.commit();
		}

		AlarmManager service = (AlarmManager) getApplicationContext()
				.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(getApplicationContext(),
				MyStartServiceReceiver.class);
		PendingIntent pending = PendingIntent.getBroadcast(
				getApplicationContext(), 0, i,
				PendingIntent.FLAG_CANCEL_CURRENT);

		Calendar cal = Calendar.getInstance();
		// start 30 seconds after boot completed
		cal.add(Calendar.SECOND, 20);
		// fetch every 30 seconds
		// InexactRepeating allows Android to optimize the energy consumption
		service.setInexactRepeating(AlarmManager.RTC_WAKEUP,
				cal.getTimeInMillis(), REPEAT_TIME, pending);
	}

	public void onBoneClick(View view) {
		new MakeFail().execute();
		
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext()
						.getApplicationContext());
		
		String response = sharedPreferences.getString("response", "boned");
		
		Animation slideUpIn = AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.slide_up_in);
		TextView countTextView = (TextView) findViewById(R.id.countTextView);
		countTextView.setText(response);
		countTextView.startAnimation(slideUpIn);

		MediaPlayer mp = MediaPlayer.create(getApplicationContext(),
				R.raw.bone);
		mp.start();
		
	}

	private class MakeFail extends AsyncTask<Object, Integer, Exception> {

		private String response = "";
		private boolean successful = false;

		protected void onProgressUpdate(Integer... progress) {

		}

		protected void onPostExecute(Exception e) {

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
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://failbone.com/bone");

			try {
				// Add your data
				List<NameValuePair> postDataPairs = new ArrayList<NameValuePair>();

				postDataPairs.add(new BasicNameValuePair("deviceId",
						registrationId));
				postDataPairs.add(new BasicNameValuePair("latitude", Double
						.toString(latitude)));
				postDataPairs.add(new BasicNameValuePair("longitude", Double
						.toString(longitude)));

				httpPost.setEntity(new UrlEncodedFormEntity(postDataPairs));

				// Execute HTTP Post Request
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				response = httpclient.execute(httpPost, responseHandler);

				Log.d("custom_message", "Response: " + response);

				return false;
			} catch (Exception e) {
				Log.d("custom_error", "Exception: " + e.toString());
				return false;
			}
		}
	}

	private class Register extends AsyncTask<Object, Integer, Exception> {

		private String response = "";
		private boolean successful = false;

		protected void onProgressUpdate(Integer... progress) {

		}

		protected void onPostExecute(Exception e) {

			Animation slideUpIn = AnimationUtils.loadAnimation(
					getApplicationContext(), R.anim.slide_up_in);
			TextView countTextView = (TextView) findViewById(R.id.countTextView);
			countTextView.setText(response);
			countTextView.startAnimation(slideUpIn);

			if (response.contains("ok")) {
				SharedPreferences sharedPreferences = PreferenceManager
						.getDefaultSharedPreferences(getApplicationContext()
								.getApplicationContext());

				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putBoolean("registered", true);
				editor.commit();
			}
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
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://failbone.com/register");

			try {
				// Add your data
				List<NameValuePair> postDataPairs = new ArrayList<NameValuePair>();

				postDataPairs.add(new BasicNameValuePair("deviceId",
						registrationId));
				postDataPairs.add(new BasicNameValuePair("latitude", Double
						.toString(latitude)));
				postDataPairs.add(new BasicNameValuePair("longitude", Double
						.toString(longitude)));

				httpPost.setEntity(new UrlEncodedFormEntity(postDataPairs));

				// Execute HTTP Post Request
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				response = httpclient.execute(httpPost, responseHandler);

				Log.d("custom_message", "Response: " + response);

				return false;
			} catch (Exception e) {
				Log.d("custom_error", "Exception: " + e.toString());
				return false;
			}
		}
	}
}