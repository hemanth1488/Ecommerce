package com.ecommerce.app;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.ecommerce.app.R;
import com.ecommerce.app.custom.BaseActivity;
import com.ecommerce.app.models.ProductResponse;
import com.ecommerce.app.models.Retailer;
import com.ecommerce.app.models.RetailerInfoResponse;
import com.ecommerce.app.models.RetailerStores;
import com.ecommerce.app.utils.Constants;
import com.ecommerce.app.utils.HTTPHandler;
import com.ecommerce.app.utils.Utils;
import com.google.gson.Gson;

public class MainActivity extends BaseActivity {
	private Activity activity;
	private TextView textViewHeader;
	
	private Button buttonLocateUs;
	private VideoView videoView;
	private ImageView imageView, imageViewLogo;
	private View viewLineHome, viewLineEShop, viewLineLoyalty,
			viewLineFeedback, viewLineVouchers;
	private View rootView;
	private ImageView rootViewImage;
	private RelativeLayout rel;
	private SharedPreferences pref;
	private Gson gson;
	private ImageView overflowImg;
	 private RetailerInfoResponse restemp,resserver; 

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pref=context.getSharedPreferences(Constants.PREF_NAME, Constants.PRIVATE_MODE);
		gson = new Gson();
		activity = this;
		if(pref.contains("RetailerInfoResponse")){
		    String json = pref.getString("RetailerInfoResponse", "");
		   
		    restemp= gson.fromJson(json, RetailerInfoResponse.class);
		    loadHomeScreen();
			}
		new AsyncWorker().execute();
		
		

		/*sqliteHelper.openDataBase();
		retailer = sqliteHelper.getRetailer();
		sqliteHelper.close();*/
		
		
		
		

		

	}
	private void loadHomeScreen(){
		
		setContentView(R.layout.activity_main);
		
		textViewHeader = (TextView) findViewById(R.id.textViewHeader);
		textViewHeader.setText(restemp.getRetailerData().getRetailerName());
		textViewHeader.setTextColor(Color.parseColor("#"
				+ restemp.getRetailerData().getRetailerTextColor()));
		setHeaderTheme(activity, restemp.getRetailerData().getRetailerTextColor(),
				restemp.getRetailerData().getHeaderColor());
		buttonLocateUs = (Button) findViewById(R.id.buttonLocateUs);
		videoView = (VideoView) findViewById(R.id.videoView);
		imageView = (ImageView) findViewById(R.id.imageView);
		imageViewLogo = (ImageView) findViewById(R.id.imageViewLogo);
		rootView = (View) findViewById(R.id.rootView);
		rootViewImage = (ImageView) findViewById(R.id.rootViewImage);
		rel = (RelativeLayout) findViewById(R.id.rel);
		setViewLines();
		GradientDrawable g = new GradientDrawable(
				GradientDrawable.Orientation.TOP_BOTTOM, new int[] {
						Color.WHITE, Color.WHITE });
		g.setCornerRadius(20);
		rel.setBackgroundDrawable(g);
		
		try {
			if (restemp.getRetailerData().getBackdropType().equalsIgnoreCase("Color")) {
				try {
					GradientDrawable gdd = new GradientDrawable(
							GradientDrawable.Orientation.TOP_BOTTOM, new int[] {
									Color.parseColor("#"
											+ restemp.getRetailerData().getBackdropColor1()),
									Color.parseColor("#"
											+ restemp.getRetailerData().getBackdropColor2()) });
					rootView.setBackgroundDrawable(gdd);
					rootViewImage.setVisibility(View.GONE);
					rootView.setVisibility(View.VISIBLE);
				} catch (Exception e) {
				}
			} else {
				rootView.setVisibility(View.GONE);
				rootViewImage.setVisibility(View.VISIBLE);
				imageCacheloader.displayImage(restemp.getRetailerData().getBackdropFile(),
						R.drawable.image_placeholder, rootViewImage);

			}
			
			
			
			buttonLocateUs.setTextColor(Color.parseColor("#"
					+ restemp.getRetailerData().getRetailerTextColor()));

			buttonLocateUs.setBackgroundDrawable(getGradientDrawable(restemp.getRetailerData()
					.getHeaderColor()));

		} catch (Exception e) {
		}

		try {
			if (restemp.getRetailerData().getRetailerFileType().equalsIgnoreCase("Image")) {
				imageCacheloader.displayImage(restemp.getRetailerData().getRetailerFile(),
						R.drawable.image_placeholder, imageView);
				imageView.setVisibility(View.VISIBLE);
				((View) findViewById(R.id.frameImageView))
						.setVisibility(View.VISIBLE);
				videoView.setVisibility(View.GONE);
				((View) findViewById(R.id.frameVideoView))
						.setVisibility(View.GONE);
			} else {

				videoView.setVideoPath(restemp.getRetailerData().getRetailerFile());
				MediaController mediaController = new MediaController(this);
				mediaController.setAnchorView(videoView);
				videoView.setMediaController(mediaController);
				videoView.requestFocus();
				videoView.seekTo(1);
				videoView.start();
				videoView.setVisibility(View.VISIBLE);
				imageView.setVisibility(View.GONE);
				((View) findViewById(R.id.frameVideoView))
						.setVisibility(View.VISIBLE);
				((View) findViewById(R.id.frameImageView))
						.setVisibility(View.GONE);
			}
		} catch (Exception e) {
		}

		imageCacheloader.displayImage(restemp.getRetailerData().getCompanyLogo(),
				R.drawable.image_placeholder, imageViewLogo);

		
	}

	@SuppressWarnings("deprecation")
	public static Drawable drawableFromUrl(String url) throws IOException {
		Bitmap x;

		HttpURLConnection connection = (HttpURLConnection) new URL(url)
				.openConnection();
		connection.connect();
		InputStream input = connection.getInputStream();

		x = BitmapFactory.decodeStream(input);
		return new BitmapDrawable(x);
	}

	private void setViewLines() {
		viewLineHome = (View) findViewById(R.id.viewLineHome);
		viewLineEShop = (View) findViewById(R.id.viewLineEShop);
		viewLineLoyalty = (View) findViewById(R.id.viewLineLoyalty);
		viewLineFeedback = (View) findViewById(R.id.viewLineFeedback);
		viewLineVouchers = (View) findViewById(R.id.viewLineVouchers);
		viewLineHome.setVisibility(View.VISIBLE);
		viewLineEShop.setVisibility(View.INVISIBLE);
		viewLineLoyalty.setVisibility(View.INVISIBLE);
		viewLineFeedback.setVisibility(View.INVISIBLE);
		viewLineVouchers.setVisibility(View.INVISIBLE);
	}

	public void locateUsPressed(View v) {
		Intent intent = new Intent(getApplicationContext(),
				BranchLocationActivity.class);
		startActivity(intent);
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
				
				try {
					obj.put(Constants.PARAM_RETAILER_ID, Constants.RETAILER_ID);
					
					JSONObject jsonObject = HTTPHandler.defaultHandler()
							.doPost(Constants.URL_GET_RETAILER_INFO, obj);

					
					resserver = gson.fromJson(
							jsonObject.toString(), RetailerInfoResponse.class);
					//List<RetailerStores> stores = new ArrayList<RetailerStores>();

					if (resserver.getErrorCode().equals("1")) {

						String json=gson.toJson(resserver);
						if(!gson.toJson(resserver).equalsIgnoreCase(gson.toJson(restemp))){
							pref.edit().putString("RetailerInfoResponse", json).commit();
							restemp=resserver;
						}
						

					} else {
						status = false;
					}
					resserver = null;

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

			loadHomeScreen();
			dismissLoadingDialog();
		}

		@Override
		protected void onPreExecute() {
			showLoadingDialog();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}
}
