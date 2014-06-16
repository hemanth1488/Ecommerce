package com.ecommerce.app;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecommerce.app.R;
import com.ecommerce.app.custom.BaseActivity;
import com.ecommerce.app.models.Retailer;
import com.ecommerce.app.models.RetailerInfoResponse;
import com.ecommerce.app.models.RetailerStores;
import com.ecommerce.app.utils.Constants;
import com.ecommerce.app.utils.HTTPHandler;
import com.ecommerce.app.utils.Utils;
import com.google.gson.Gson;

public class SplashscreenActivity extends BaseActivity {

	private ImageView imageViewSplashScreen;
	private TextView textViewOperator;
	private SharedPreferences spref;

	
	private RetailerInfoResponse res;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splashscreen);

		textViewOperator = (TextView) findViewById(R.id.textViewOperator);

		imageViewSplashScreen = (ImageView) findViewById(R.id.imageViewSplashScreen);
		
		spref = getSharedPreferences(Constants.PREF_NAME, Constants.PRIVATE_MODE);
		if(spref!=null){
		if (spref.contains(Constants.SPLASH_IMG)) {
			imageCacheloader.displayImage(
					spref.getString(Constants.SPLASH_IMG, ""),
					R.drawable.image_placeholder, imageViewSplashScreen);
		}
		}
		new AsyncWorker().execute();
	}

	private class AsyncWorker extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {

			try {
				/*sqliteHelper.createDataBase();

				sqliteHelper.openDataBase();
				retailer = sqliteHelper.getRetailer();
				sqliteHelper.close();
*/
			} catch (Exception e) {
				Log.v("DBInfo", "Failed to create DB");
				e.printStackTrace();
			}

			if (Utils.hasNetworkConnection(getApplicationContext())) {
				JSONObject obj = new JSONObject();
				Boolean status = false;
				Gson gson = new Gson();
				String splash="";
				try {
					obj.put(Constants.PARAM_RETAILER_ID, Constants.RETAILER_ID);
					if (!spref.contains(Constants.SPLASH_IMG))
					{
					JSONObject splashUrl = HTTPHandler.defaultHandler()
							.doPost(Constants.URL_GET_SPLASH, obj);
					spref.edit().putString(Constants.SPLASH_IMG, splashUrl.getString("splashImage")).commit();
					imageCacheloader.displayImage(splashUrl.getString("splashImage"),
							R.drawable.image_placeholder, imageViewSplashScreen);
					}
					JSONObject jsonObject = HTTPHandler.defaultHandler()
							.doPost(Constants.URL_GET_RETAILER_INFO, obj);

					
					 res = gson.fromJson(
							jsonObject.toString(), RetailerInfoResponse.class);
					List<RetailerStores> stores = new ArrayList<RetailerStores>();

					if (res.getErrorCode().equals("1")) {

						String json=gson.toJson(res);
					
						spref.edit().putString("RetailerInfoResponse", json).commit();
						if(res.getRetailerData().getEnablePassword().equalsIgnoreCase("1")){
							spref.edit().putBoolean("Commercial", true);
						}
						else{
							spref.edit().putBoolean("Commercial", false);
						}

					} else {
						status = false;
					}
					res = null;

					return status;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}

			} else {

				return false;
			}

		}

		@Override
		protected void onPostExecute(Boolean result) {

			if (result) {

				

				try {
					if (res.getRetailerData().getBackdropColor1().length() > 0) {
						Constants.BACKDROP1 = Color.parseColor("#"
								+ res.getRetailerData().getBackdropColor1());
					}
				} catch (Exception e) {
				}

				try {
					if (res.getRetailerData().getBackdropColor2().length() > 0) {
						Constants.BACKDROP2 = Color.parseColor("#"
								+ res.getRetailerData().getBackdropColor2());
					}
				} catch (Exception e) {
				}

				textViewOperator.setText(res.getRetailerData().getPoweredBy());
				

				

				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						Intent intent = new Intent(getApplicationContext(),
								MainActivity.class);
						startActivity(intent);
						finish();
					}
				}, 2000);

			} else {

				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						Intent intent = new Intent(getApplicationContext(),
								MainActivity.class);
						startActivity(intent);
						finish();
					}
				}, 5000);
			}
		}

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

}
