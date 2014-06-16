package com.ecommerce.app;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecommerce.app.R;
import com.ecommerce.app.custom.PopupAdapter;
import com.ecommerce.app.models.Retailer;
import com.ecommerce.app.models.RetailerInfoResponse;
import com.ecommerce.app.models.RetailerStores;
import com.ecommerce.app.utils.Constants;
import com.ecommerce.app.utils.SQLiteHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

public class BranchLocationActivity extends FragmentActivity implements
		OnInfoWindowClickListener {

	// private String TAG = getClass().getSimpleName();
	private SQLiteHelper sqliteHelper = null;

	private Activity activity;
	private GoogleMap map;
	private ArrayList<RetailerStores> stores = new ArrayList<RetailerStores>();
	private TextView textViewHeader;
	private Context context;
	
	private LatLngBounds.Builder bld;
	private SharedPreferences pref;
	private Gson gson;
	private RetailerInfoResponse res;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_branchlocation);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		textViewHeader = (TextView) findViewById(R.id.textViewHeader);
		activity = this;
		context = this;
		getActionBar().hide();
		/*sqliteHelper = new SQLiteHelper(getApplicationContext());

		sqliteHelper.openDataBase();
		retailer = sqliteHelper.getRetailer();
		sqliteHelper.close();*/
		pref=context.getSharedPreferences(Constants.PREF_NAME, Constants.PRIVATE_MODE);
		gson = new Gson();
	    String json = pref.getString("RetailerInfoResponse", "");
	   
	    res = gson.fromJson(json, RetailerInfoResponse.class);
		try {
			setHeaderTheme(activity);
		} catch (Exception e) {
		}

		/*sqliteHelper.openDataBase();*/
		stores = (ArrayList<RetailerStores>) res.getRetailerData().getRetailerStores();
		/*sqliteHelper.close();
*/
		map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map_info)).getMap();

		map.addMarker(new MarkerOptions()
				.position(new LatLng(Constants.LAT, Constants.LNG))
				.title("Current Location")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

		textViewHeader.setText("Location");

		try {
			for (int i = 0; i < stores.size(); i++) {
				addMarker(Double.parseDouble(stores.get(i).getLatitude()),
						Double.parseDouble(stores.get(i).getLongitude()),
						stores.get(i).getStoreAddress(), stores.get(i)
								.getStoreContact(), map);

			}

			ArrayList<LatLng> list = new ArrayList<LatLng>();

			bld = new LatLngBounds.Builder();

			for (int i = 0; i < stores.size(); i++) {
				list.add(new LatLng(Double.parseDouble(stores.get(i)
						.getLatitude()), Double.parseDouble(stores.get(i)
						.getLongitude())));

			}

			for (LatLng item : list) {
				bld.include(item);
			}

			bld.include(new LatLng(Constants.LAT, Constants.LNG));

		} catch (Exception e) {
			e.printStackTrace();
		}

		map.setOnCameraChangeListener(new OnCameraChangeListener() {

			@Override
			public void onCameraChange(CameraPosition arg0) {
				map.animateCamera(CameraUpdateFactory.newLatLngBounds(
						bld.build(), 50));

				map.setOnCameraChangeListener(null);
			}
		});

		map.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
		map.setOnInfoWindowClickListener(this);

	}

	private void addMarker(double lat, double lng, String branchName,
			String contact, GoogleMap map) {
		map.addMarker(new MarkerOptions()
				.position(new LatLng(lat, lng))
				.title(res.getRetailerData().getRetailerName() + "\r\nAddress\r\n"
						+ branchName + "\r\n\r\n" + "Contact")
				.snippet(contact)
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

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
	public void setHeaderTheme(Activity activity) {
		TextView textViewHeader = (TextView) activity
				.findViewById(R.id.textViewHeader);
		View header = (View) activity.findViewById(R.id.header);
		
		textViewHeader.setTextColor(Color.parseColor("#"
				+res.getRetailerData().getRetailerTextColor()));

		GradientDrawable gd = new GradientDrawable(
				GradientDrawable.Orientation.TOP_BOTTOM, new int[] {
						Color.parseColor("#AA" + res.getRetailerData().getHeaderColor()),
						Color.parseColor("#" + res.getRetailerData().getHeaderColor()) });
		gd.setCornerRadius(0f);

		header.setBackgroundDrawable(gd);

	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		try {
			if (marker.getSnippet().length() > 0) {
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setData(Uri.parse("tel:" + marker.getSnippet()));
				startActivity(intent);
			}
		} catch (Exception e) {
		}
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

}
