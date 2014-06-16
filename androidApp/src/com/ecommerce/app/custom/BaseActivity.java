package com.ecommerce.app.custom;

import java.io.IOException;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecommerce.app.R;
import com.ecommerce.app.EShopFragmentActivity;
import com.ecommerce.app.FeedbackActivity;
import com.ecommerce.app.LoyaltyActivity;
import com.ecommerce.app.MainActivity;
import com.ecommerce.app.ProfileActivity;
import com.ecommerce.app.VoucherActivity;
import com.ecommerce.app.custom.MyLocation.LocationResult;
import com.ecommerce.app.utils.Constants;
import com.ecommerce.app.utils.HTTPHandler;
import com.ecommerce.app.utils.ImageCacheLoader;
import com.ecommerce.app.utils.SQLiteHelper;
import com.ecommerce.app.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class BaseActivity extends Activity {

	public String TAG = getClass().getSimpleName();
	public SQLiteHelper sqliteHelper = null;
	public ImageCacheLoader imageCacheloader;
	// private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private static String SENDER_ID = "610819397652";
	private static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private GoogleCloudMessaging gcm;
	private String regid;
	protected Context context;
	private MyLocation myLocation;
	private double lat, lng;
	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		getActionBar().hide();
		sqliteHelper = new SQLiteHelper(getApplicationContext());
		imageCacheloader = new ImageCacheLoader(this);
		context = this;

		LocationResult locationResult = new LocationResult() {
			@Override
			public void gotLocation(Location location) {

				if (location != null) {

					lat = location.getLatitude();
					lng = location.getLongitude();

					Log.v(TAG,
							location.getLatitude() + ","
									+ location.getLongitude());
					Constants.LAT = lat;
					Constants.LNG = lng;
				} else {

					Criteria criteria = new Criteria();
					LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
					String curLoc = locMan.getBestProvider(criteria, true);
					location = locMan.getLastKnownLocation(curLoc);
					if (location != null) {

						lat = location.getLatitude();
						lng = location.getLongitude();
						Log.v(TAG,
								location.getLatitude() + ","
										+ location.getLongitude());
						Constants.LAT = lat;
						Constants.LNG = lng;
					}

				}
			}
		};

		myLocation = new MyLocation();
		myLocation.getLocation(getApplicationContext(), locationResult);

		if (!checkPlayServices()) {
			Log.i(TAG, "User has no valid google play services package");
		} else {
			gcm = GoogleCloudMessaging.getInstance(context);
			regid = getRegistrationId(context);
			Constants.REG_ID = regid;
			Log.i("REG ID", regid);
			if (regid.isEmpty())
				registerInBackground();

		}

	}

	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}

		return registrationId;
	}

	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {

				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regid = gcm.register(SENDER_ID);
					msg = "Device registered, registration ID=" + regid;

					sendRegistrationIdToBackend();

					// Persist the regID - no need to register again.
					storeRegistrationId(context, regid);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				Log.i(TAG, msg);

			}
		}.execute();
	}

	private void sendRegistrationIdToBackend() {
		// Your implementation here.
		if (Utils.hasNetworkConnection(context)) {
			new AsyncAddDevice().execute();
		}
	}

	public boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				// GooglePlayServicesUtil.getErrorDialog(resultCode, this,
				// PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	private final class AsyncAddDevice extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(Void... params) {
			JSONObject obj = new JSONObject();
			try {
				obj.put(Constants.PARAM_RETAILER_ID, Constants.RETAILER_ID);
				obj.put(Constants.PARAM_EMAIL, "");
				obj.put(Constants.PARAM_LAT, lat);
				obj.put(Constants.PARAM_LONG, lng);
				obj.put(Constants.PARAM_DEVICE_TOKEN, regid);
				obj.put(Constants.PARAM_DEVICE, 2);

				HTTPHandler.defaultHandler().doPost(
						Constants.URL_SEND_DEVICE_TOKEN, obj);
				Log.i(TAG, obj.toString());
				Constants.REG_ID = regid;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "";
		}

		@Override
		protected void onPostExecute(String status) {

		}
	}

	private SharedPreferences getGCMPreferences(Context context) {

		return getSharedPreferences(BaseActivity.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	public void overflowPressed(View v) {
		RelativeLayout header = (RelativeLayout) findViewById(R.id.header);
		final PopupMenu popupMenu = new PopupMenu(this, header);
		popupMenu.getMenu().add(Menu.NONE, 1, Menu.NONE, "PROFILE");
		popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent(context, ProfileActivity.class);
				startActivity(intent);
				return true;
			}
		});

		popupMenu.show();
	}

	@SuppressWarnings("deprecation")
	public void setHeaderTheme(Activity activity, String textColor,
			String headerColor) {

		TextView textViewHeader = (TextView) activity
				.findViewById(R.id.textViewHeader);
		View header = (View) activity.findViewById(R.id.header);

		textViewHeader.setTextColor(Color.parseColor("#" + textColor));

		GradientDrawable gd = new GradientDrawable(
				GradientDrawable.Orientation.TOP_BOTTOM, new int[] {
						Color.parseColor("#80" + headerColor),
						Color.parseColor("#" + headerColor) });
		gd.setCornerRadius(0f);

		header.setBackgroundDrawable(gd);

	}

	public GradientDrawable getGradientDrawableEditText(String headerColor) {
		GradientDrawable gdDefault = new GradientDrawable();
		gdDefault.setColor(Color.parseColor("#" + headerColor));
		gdDefault.setAlpha(30);
		gdDefault.setCornerRadius(24);
		gdDefault.setStroke(4, Color.parseColor("#000000"));
		return gdDefault;
	}

	public GradientDrawable getGradientDrawable(String headerColor) {
		GradientDrawable gd;

		gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
				new int[] { Color.parseColor("#80" + headerColor),
						Color.parseColor("#" + headerColor),
						Color.parseColor("#" + headerColor) });
		gd.setCornerRadius(20);
		return gd;
	}

	public GradientDrawable getGradientDrawableNoRad(String headerColor) {
		GradientDrawable gd = new GradientDrawable(
				GradientDrawable.Orientation.TOP_BOTTOM, new int[] {
						Color.parseColor("#80" + headerColor),
						Color.parseColor("#" + headerColor) });

		gd.setCornerRadius(0);
		return gd;
	}

	public void homePressed(View v) {
		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	
	}

	public void eShopPressed(View v) {
		Intent intent = new Intent(this, EShopFragmentActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		
	}

	public void loyaltyPressed(View v) {
		Intent intent = new Intent(this, LoyaltyActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		
	}

	public void feedbackPressed(View v) {
		Intent intent = new Intent(this, FeedbackActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		
	}

	public void vouchersPressed(View v) {
		Intent intent = new Intent(this, VoucherActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		
	}

	public void showLoadingDialog() {
		try {
			if (progressDialog == null || !progressDialog.isShowing()) {
				progressDialog = new ProgressDialog(context);
				progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				progressDialog.setMessage("Loading...");
				// progressDialog.setCancelable(false);
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.show();
			}
		} catch (Exception ex) {
			Log.e(TAG,
					"Could not display progress dialog because the activity that called it is no longer active");
		}
	}

	public void dismissLoadingDialog() {
		try {
			progressDialog.dismiss();
		} catch (Exception ex) {
			Log.e(TAG,
					"Could not dismiss progress dialog because the activity that called it is no longer active");
		}
	}
}
