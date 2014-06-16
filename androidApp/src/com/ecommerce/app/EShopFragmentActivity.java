package com.ecommerce.app;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecommerce.app.R;
import com.ecommerce.app.fragments.EShopFragment;
import com.ecommerce.app.models.Product;
import com.ecommerce.app.models.ProductCategory;
import com.ecommerce.app.models.ProductResponse;
import com.ecommerce.app.models.Retailer;
import com.ecommerce.app.models.RetailerInfoResponse;
import com.ecommerce.app.utils.Constants;
import com.ecommerce.app.utils.HTTPHandler;
import com.ecommerce.app.utils.SQLiteHelper;
import com.google.gson.Gson;

public class EShopFragmentActivity extends FragmentActivity {

	public String TAG = getClass().getSimpleName();
	private Fragment content;
	private Context context;
	private ProgressDialog progressDialog;
	private Activity activity;
	private View viewLineHome, viewLineEShop, viewLineLoyalty,
			viewLineFeedback, viewLineVouchers;
	private TextView textViewHeader;
	private ImageButton cart;
	private Retailer retailer;
	private SharedPreferences pref;
	private Gson gson;
	private Boolean commercial;
	private EditText input ;
	private  Dialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		getActionBar().hide();
		context = this;
		activity = this;

		pref=context.getSharedPreferences(Constants.PREF_NAME, Constants.PRIVATE_MODE);
		gson = new Gson();
	    String json = pref.getString("RetailerInfoResponse", "");
	   
	    RetailerInfoResponse res = gson.fromJson(json, RetailerInfoResponse.class);
	   	retailer=res.getRetailerData();
	   	commercial=pref.getBoolean("commercial", false);
		if (savedInstanceState != null)
			content = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		
		new AsyncGetEShopDetails().execute();
		
	}

	public void homePressed(View v) {
		Intent intent = new Intent(context, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

	public void eShopPressed(View v) {

	}

	public void loyaltyPressed(View v) {
		Intent intent = new Intent(this, LoyaltyActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

	public void feedbackPressed(View v) {
		Intent intent = new Intent(this, FeedbackActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

	public void vouchersPressed(View v) {
		Intent intent = new Intent(context, VoucherActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}
	public GradientDrawable getGradientDrawableEditText(String headerColor) {
		GradientDrawable gdDefault = new GradientDrawable();
		gdDefault.setColor(Color.parseColor("#" + headerColor));
		gdDefault.setAlpha(30);
		gdDefault.setCornerRadius(24);
		gdDefault.setStroke(4, Color.parseColor("#000000"));
		return gdDefault;
	}
	@SuppressWarnings("deprecation")
	public void setHeaderTheme(Activity activity) {
		TextView textViewHeader = (TextView) activity
				.findViewById(R.id.textViewHeader);
		View header = (View) activity.findViewById(R.id.header);
		cart=(ImageButton) activity.findViewById(R.id.imageViewCart);
		cart.setVisibility(View.VISIBLE);
		cart.setBackgroundDrawable(getGradientDrawableEditText(retailer
				.getHeaderColor()));
		textViewHeader.setTextColor(Color.parseColor("#"
				+ retailer.getRetailerTextColor()));

		GradientDrawable gd = new GradientDrawable(
				GradientDrawable.Orientation.TOP_BOTTOM, new int[] {
						Color.parseColor("#AA" + retailer.getHeaderColor()),
						Color.parseColor("#" + retailer.getHeaderColor()) });
		gd.setCornerRadius(0f);

		header.setBackgroundDrawable(gd);

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

	private void setViewLines() {
		viewLineHome.setVisibility(View.INVISIBLE);
		viewLineEShop.setVisibility(View.VISIBLE);
		viewLineLoyalty.setVisibility(View.INVISIBLE);
		viewLineFeedback.setVisibility(View.INVISIBLE);
		viewLineVouchers.setVisibility(View.INVISIBLE);
		if(!(commercial)){
			 dialog = new Dialog(context);
			
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.dialog_authenticate);
			//dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			//LinearLayout layout=(LinearLayout) findViewById(R.id.title_layout);
			View header = (View) dialog.findViewById(R.id.header_auth);
			header.setBackgroundColor(Color.parseColor("#"
				+ retailer.getHeaderColor()));
			TextView textViewHeaderDialog = (TextView)dialog.findViewById(R.id.textViewHeaderAuth);
			textViewHeaderDialog.setTextColor(Color.parseColor("#"
				+ retailer.getRetailerTextColor()));
			Button newUser=(Button) dialog.findViewById(R.id.New_user);
			Button enter=(Button) dialog.findViewById(R.id.enter);
			newUser.setTextColor(Color.parseColor("#"
				+ retailer.getRetailerTextColor()));
			enter.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));
			newUser.setBackgroundColor(Color.parseColor("#"
				+ retailer.getHeaderColor()));
			enter.setBackgroundColor(Color.parseColor("#"
					+ retailer.getHeaderColor()));
			
			//dialog.show();
			//enter.performClick();
	}
		
	}
	public void enter(View v){
		dialog.dismiss();
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

	private final class AsyncGetEShopDetails extends
			AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			showLoadingDialog();
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			JSONObject param = new JSONObject();
			try {
				param.put(Constants.PARAM_RETAILER_ID, Constants.RETAILER_ID);
				JSONObject jsonObject = HTTPHandler.defaultHandler().doPost(
						Constants.URL_GET_PRODUCTS, param);

				Gson gson = new Gson();
				ProductResponse data = gson.fromJson(jsonObject.toString(),
						ProductResponse.class);

				if (data.getErrorCode().equals("1")) {
					List<ProductCategory> categoryList = new ArrayList<ProductCategory>();
					categoryList = data.getData();

					for (int i = 0; i < categoryList.size(); i++) {
						
						List<Product> productList = new ArrayList<Product>();

						productList = categoryList.get(i).getProducts();
						categoryList.get(i).setId(i);
						for (int ii = 0; ii < productList.size(); ii++) {
							Product product = productList.get(ii);
							product.setCategoryId(Integer.parseInt(i + ""));
							productList.set(ii, product);
							
						}
						categoryList.get(i).setProducts(productList);

					}
					
					// setting data in shared Preference 
					data.setData(categoryList);
					String json=gson.toJson(data);
					pref=getSharedPreferences(Constants.PREF_NAME, Constants.PRIVATE_MODE);
					pref.edit().putString("productResponse", json).commit();
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

		}

		@Override
		protected void onPostExecute(Boolean status) {
			dismissLoadingDialog();

			if (content == null) {
				content = new EShopFragment();
			}

			setContentView(R.layout.fragment_frame);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container_framelayout, content).commit();

			try {
				setHeaderTheme(activity);
			} catch (Exception e) {
			}

			textViewHeader = (TextView) findViewById(R.id.textViewHeader);
			viewLineHome = (View) findViewById(R.id.viewLineHome);
			viewLineEShop = (View) findViewById(R.id.viewLineEShop);
			viewLineLoyalty = (View) findViewById(R.id.viewLineLoyalty);
			viewLineFeedback = (View) findViewById(R.id.viewLineFeedback);
			viewLineVouchers = (View) findViewById(R.id.viewLineVouchers);
			textViewHeader.setText("E-SHOP");
			setViewLines();
			
			
		}}}

