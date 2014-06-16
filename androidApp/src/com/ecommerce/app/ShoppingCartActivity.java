package com.ecommerce.app;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ecommerce.app.R;
import com.ecommerce.app.adapters.ShoppingCartAdapter;
import com.ecommerce.app.adapters.VouchersListAdapter;
import com.ecommerce.app.custom.BaseActivity;
import com.ecommerce.app.models.Products;
import com.ecommerce.app.models.Retailer;
import com.ecommerce.app.models.RetailerInfoResponse;
import com.ecommerce.app.models.Voucher;
import com.ecommerce.app.utils.Constants;
import com.ecommerce.app.utils.HTTPHandler;
import com.ecommerce.app.utils.ObjectSerializer;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;

public class ShoppingCartActivity extends BaseActivity {

	private Activity activity;
	private View viewLineHome, viewLineEShop, viewLineLoyalty,
			viewLineFeedback, viewLineVouchers;
	private TextView textViewHeader;
	private ListView listViewVoucher;
	private ShoppingCartAdapter adapter;
	private ArrayList<Products> cartList = new ArrayList<Products>();
	private Context context;
	private AdView adView;
	private LinearLayout linAdMob;
	private Retailer retailer;
	private SharedPreferences pref;
	private Gson gson;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shoppingcart);
		activity = this;
		context = this;

		listViewVoucher = (ListView) findViewById(R.id.lv_cart);

		pref=context.getSharedPreferences(Constants.PREF_NAME, Constants.PRIVATE_MODE);
		gson = new Gson();
	    String json = pref.getString("RetailerInfoResponse", "");
	    RetailerInfoResponse res = gson.fromJson(json, RetailerInfoResponse.class);
		retailer = res.getRetailerData();
		Button credit=(Button) findViewById(R.id.btn_credit);
		credit.setBackgroundColor(Color.parseColor("#"
				+ retailer.getHeaderColor()));
		
		try {
			cartList = (ArrayList<Products>) ObjectSerializer.deserialize(pref.getString("Cart", ObjectSerializer.serialize(new ArrayList<Products>())));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		adapter = new ShoppingCartAdapter(context, R.layout.row_cart,
				cartList);

		Log.i(TAG, cartList.size() + "");

		setViewLines();

		textViewHeader.setText("cartList");

		listViewVoucher.setAdapter(adapter);

		try {
			setHeaderTheme(activity, retailer.getRetailerTextColor(),
					retailer.getHeaderColor());
		} catch (Exception e) {
		}

		new AsyncGetPublisherId().execute();

	}

	

	private void setViewLines() {
		viewLineHome = (View) findViewById(R.id.viewLineHome);
		viewLineEShop = (View) findViewById(R.id.viewLineEShop);
		viewLineLoyalty = (View) findViewById(R.id.viewLineLoyalty);
		viewLineFeedback = (View) findViewById(R.id.viewLineFeedback);
		viewLineVouchers = (View) findViewById(R.id.viewLineVouchers);
		textViewHeader = (TextView) findViewById(R.id.textViewHeader);
		viewLineHome.setVisibility(View.INVISIBLE);
		viewLineEShop.setVisibility(View.VISIBLE);
		viewLineLoyalty.setVisibility(View.INVISIBLE);
		viewLineFeedback.setVisibility(View.INVISIBLE);
		viewLineVouchers.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (adView != null) {
			adView.resume();
		}
	}

	@Override
	public void onPause() {
		if (adView != null) {
			adView.pause();
		}
		super.onPause();
	}

	@Override
	public void onDestroy() {

		if (adView != null) {
			adView.destroy();
		}
		super.onDestroy();
	}

	private final class AsyncGetPublisherId extends
			AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(Void... params) {

			JSONObject param = new JSONObject();
			try {
				param.put(Constants.PARAM_RETAILER_ID, Constants.RETAILER_ID);
				JSONObject jsonObject = HTTPHandler.defaultHandler().doPost(
						Constants.URL_GET_PUBLISHER_ID, param);

				return jsonObject.getString("publisherId");

			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}

		}

		
	}

}
