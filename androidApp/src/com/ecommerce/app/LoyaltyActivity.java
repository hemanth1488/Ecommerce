package com.ecommerce.app;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.app.R;
import com.ecommerce.app.custom.BaseActivity;
import com.ecommerce.app.models.Loyalty;
import com.ecommerce.app.models.Retailer;
import com.ecommerce.app.models.RetailerInfoResponse;
import com.ecommerce.app.utils.Constants;
import com.ecommerce.app.utils.HTTPHandler;
import com.ecommerce.app.utils.Utils;
import com.google.gson.Gson;

public class LoyaltyActivity extends BaseActivity {

	private Activity activity;
	private Context context;
	private TextView textViewHeader;
	private View viewLineHome, viewLineEShop, viewLineLoyalty,
			viewLineFeedback, viewLineVouchers;
	private String password = "";
	private Button buttonMerchantAdmin, buttonReset;
	private Dialog dialog;

	private ImageButton imageButtonStamp1, imageButtonStamp2,
			imageButtonStamp3, imageButtonStamp4, imageButtonStamp5;
	private int i = 0;
	private boolean isPasswordEntered = false;

	private boolean stamp1, stamp2, stamp3, stamp4, stamp5;
	private TextView textViewTermsCond;
	private Loyalty loyalty;
	private Loyalty loyaltyres ;

	private ImageView imageViewLoyalty;
	private Button buttonFb;
	private Retailer retailer;
	private SharedPreferences pref;
	private Gson gson;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loyalty);
		activity = this;
		context = this;

		pref=context.getSharedPreferences(Constants.PREF_NAME, Constants.PRIVATE_MODE);
		gson = new Gson();
	    String json = pref.getString("RetailerInfoResponse", "");
	    RetailerInfoResponse res = gson.fromJson(json, RetailerInfoResponse.class);
		retailer = res.getRetailerData();
		

		imageButtonStamp1 = (ImageButton) findViewById(R.id.imageButton1);
		imageButtonStamp2 = (ImageButton) findViewById(R.id.imageButton2);
		imageButtonStamp3 = (ImageButton) findViewById(R.id.imageButton3);
		imageButtonStamp4 = (ImageButton) findViewById(R.id.imageButton4);
		imageButtonStamp5 = (ImageButton) findViewById(R.id.imageButton5);

		textViewTermsCond = (TextView) findViewById(R.id.tv_termscond);
		buttonMerchantAdmin = (Button) findViewById(R.id.btn_merchant_admin);
		buttonReset = (Button) findViewById(R.id.btn_reset);
		textViewHeader = (TextView) findViewById(R.id.textViewHeader);
		imageViewLoyalty = (ImageView) findViewById(R.id.iv_loyalty);
		buttonFb = (Button) findViewById(R.id.btn_fb);

		try {
			textViewHeader.setText("LOYALTY");
			textViewHeader.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));
			setHeaderTheme(activity, retailer.getRetailerTextColor(),
					retailer.getHeaderColor());
			buttonFb.setBackgroundColor(Color.parseColor("#"
					+ retailer.getHeaderColor()));
			buttonMerchantAdmin.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));
			buttonMerchantAdmin
					.setBackgroundDrawable(getGradientDrawable(retailer
							.getHeaderColor()));

			buttonReset.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));

			buttonReset.setBackgroundDrawable(getGradientDrawable(retailer
					.getHeaderColor()));
		} catch (Exception e) {
		}

		setViewLines();

		loadStamps();

		new AsyncGetLoyalty().execute();

		imageButtonStamp1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isPasswordEntered) {
					i++;
					Handler handler = new Handler();
					Runnable r = new Runnable() {

						@Override
						public void run() {
							if (i == 1) {
								imageButtonStamp1
										.setImageResource(R.drawable.coupon_selected);
								stamp1 = true;
								Log.i("Single", "1");
							}
							i = 0;
						}
					};
					if (i == 1) {
						handler.postDelayed(r, 250);
					} else if (i == 2 && !stamp2) {
						imageButtonStamp1
								.setImageResource(R.drawable.coupon_default);
						stamp1 = false;
						Log.i("Double", "2");
						i = 0;
					}
				}
			}

		});

		imageButtonStamp2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isPasswordEntered) {
					i++;
					Handler handler = new Handler();
					Runnable r = new Runnable() {

						@Override
						public void run() {
							if (i == 1) {
								imageButtonStamp2
										.setImageResource(R.drawable.coupon_selected);
								stamp2 = true;
								Log.i("Single", "1");
							}
							i = 0;
						}
					};
					if (i == 1 && stamp1) {
						handler.postDelayed(r, 250);
					} else if (i == 2 && !stamp3) {
						imageButtonStamp2
								.setImageResource(R.drawable.coupon_default);
						stamp2 = false;
						Log.i("Double", "2");
						i = 0;
					}
				}
			}

		});

		imageButtonStamp3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isPasswordEntered) {
					i++;
					Handler handler = new Handler();
					Runnable r = new Runnable() {

						@Override
						public void run() {
							if (i == 1) {
								imageButtonStamp3
										.setImageResource(R.drawable.coupon_selected);
								stamp3 = true;
								Log.i("Single", "1");
							}
							i = 0;
						}
					};
					if (i == 1 && stamp1 && stamp2) {
						handler.postDelayed(r, 250);
					} else if (i == 2 && !stamp4) {
						imageButtonStamp3
								.setImageResource(R.drawable.coupon_default);
						stamp3 = false;
						Log.i("Double", "2");
						i = 0;
					}
				}
			}

		});

		imageButtonStamp4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isPasswordEntered) {
					i++;
					Handler handler = new Handler();
					Runnable r = new Runnable() {

						@Override
						public void run() {
							if (i == 1) {
								imageButtonStamp4
										.setImageResource(R.drawable.coupon_selected);
								stamp4 = true;
								Log.i("Single", "1");
							}
							i = 0;
						}
					};
					if (i == 1 && stamp3 && stamp2 && stamp1) {
						handler.postDelayed(r, 250);
					} else if (i == 2 && !stamp5) {
						imageButtonStamp4
								.setImageResource(R.drawable.coupon_default);
						stamp4 = false;
						Log.i("Double", "2");
						i = 0;
					}
				}
			}

		});

		imageButtonStamp5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isPasswordEntered) {
					i++;
					Handler handler = new Handler();
					Runnable r = new Runnable() {

						@Override
						public void run() {
							if (i == 1) {
								imageButtonStamp5
										.setImageResource(R.drawable.coupon_selected);
								stamp5 = true;
								Log.i("Single", "1");
							}
							i = 0;
						}
					};
					if (i == 1 && stamp4 && stamp3 && stamp2 && stamp1) {
						handler.postDelayed(r, 250);
					} else if (i == 2) {
						imageButtonStamp5
								.setImageResource(R.drawable.coupon_default);
						stamp5 = false;
						Log.i("Double", "2");
						i = 0;
					}
				}
			}

		});

	}

	@Override
	public void onResume() {
		super.onResume();
		// textViewHeader.setText("Loyalty");
		// textViewHeader.setTextColor(Color.parseColor("#"
		// + retailer.getRetailerTextColor()));
		// setHeaderTheme(activity, retailer.getRetailerTextColor(),
		// retailer.getHeaderColor());
		//
		// buttonMerchantAdmin.setTextColor(Color.parseColor("#"
		// + retailer.getRetailerTextColor()));
		// buttonMerchantAdmin.setBackgroundDrawable(getGradientDrawable(retailer
		// .getHeaderColor()));
	}

	public void resetPressed(View v) {
		if (isPasswordEntered) {
			Utils.setStatusLoyaltyStamp1(context, 0);
			Utils.setStatusLoyaltyStamp2(context, 0);
			Utils.setStatusLoyaltyStamp3(context, 0);
			Utils.setStatusLoyaltyStamp4(context, 0);
			Utils.setStatusLoyaltyStamp5(context, 0);

			imageButtonStamp1.setImageResource(R.drawable.coupon_default);
			imageButtonStamp2.setImageResource(R.drawable.coupon_default);
			imageButtonStamp3.setImageResource(R.drawable.coupon_default);
			imageButtonStamp4.setImageResource(R.drawable.coupon_default);
			imageButtonStamp5.setImageResource(R.drawable.coupon_default);

			stamp1 = false;
			stamp2 = false;
			stamp3 = false;
			stamp4 = false;
			stamp5 = false;
		}
	}

	private void loadStamps() {
		if (Utils.getStatusLoyaltyStamp1(context) == 0) {
			imageButtonStamp1.setImageResource(R.drawable.coupon_default);
			stamp1 = false;
		} else {
			imageButtonStamp1.setImageResource(R.drawable.coupon_selected);
			stamp1 = true;
		}

		if (Utils.getStatusLoyaltyStamp2(context) == 0) {
			imageButtonStamp2.setImageResource(R.drawable.coupon_default);
			stamp2 = false;
		} else {
			imageButtonStamp2.setImageResource(R.drawable.coupon_selected);
			stamp2 = true;
		}

		if (Utils.getStatusLoyaltyStamp3(context) == 0) {
			imageButtonStamp3.setImageResource(R.drawable.coupon_default);
			stamp3 = false;
		} else {
			imageButtonStamp3.setImageResource(R.drawable.coupon_selected);
			stamp3 = true;
		}

		if (Utils.getStatusLoyaltyStamp4(context) == 0) {
			imageButtonStamp4.setImageResource(R.drawable.coupon_default);
			stamp4 = false;
		} else {
			imageButtonStamp4.setImageResource(R.drawable.coupon_selected);
			stamp4 = true;
		}

		if (Utils.getStatusLoyaltyStamp5(context) == 0) {
			imageButtonStamp5.setImageResource(R.drawable.coupon_default);
			stamp5 = false;
		} else {
			imageButtonStamp5.setImageResource(R.drawable.coupon_selected);
			stamp5 = true;
		}
	}

	private void setViewLines() {
		viewLineHome = (View) findViewById(R.id.viewLineHome);
		viewLineEShop = (View) findViewById(R.id.viewLineEShop);
		viewLineLoyalty = (View) findViewById(R.id.viewLineLoyalty);
		viewLineFeedback = (View) findViewById(R.id.viewLineFeedback);
		viewLineVouchers = (View) findViewById(R.id.viewLineVouchers);
		viewLineHome.setVisibility(View.INVISIBLE);
		viewLineEShop.setVisibility(View.INVISIBLE);
		viewLineLoyalty.setVisibility(View.VISIBLE);
		viewLineFeedback.setVisibility(View.INVISIBLE);
		viewLineVouchers.setVisibility(View.INVISIBLE);
	}

	@SuppressWarnings("deprecation")
	public void merchantAdminPressed(View v) {
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		try {
			dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.dialog_merchant_password);

			dialog.getWindow().setLayout((6 * width) / 7,
					LayoutParams.WRAP_CONTENT);

			Button buttonEnter = (Button) dialog.findViewById(R.id.btn_enter);
			Button buttonSaveAndClose = (Button) dialog
					.findViewById(R.id.btn_saveclose);

			final EditText editTextPassword = (EditText) dialog
					.findViewById(R.id.edt_password);

			TextView textViewTitle = (TextView) dialog
					.findViewById(R.id.textViewTitle);
			editTextPassword.setBackgroundColor(Color.parseColor("#"
					+ retailer.getHeaderColor()));
			editTextPassword.getBackground().setAlpha(30);
			editTextPassword.setText(password);
			buttonEnter.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));
			buttonEnter.setBackgroundDrawable(getGradientDrawable(retailer
					.getHeaderColor()));

			buttonSaveAndClose.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));
			buttonSaveAndClose
					.setBackgroundDrawable(getGradientDrawable(retailer
							.getHeaderColor()));

			textViewTitle
					.setBackgroundDrawable(getGradientDrawableNoRad(retailer
							.getHeaderColor()));
			textViewTitle.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));

			buttonEnter.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					password = editTextPassword.getText().toString();
					if (Utils.hasNetworkConnection(context)) {
						new AsyncGetPassword().execute();
					} else {

						Log.i(TAG, Utils.getPasswordImage(context) + "");

						if (password.equalsIgnoreCase(Utils
								.getPasswordImage(context))) {
							isPasswordEntered = !isPasswordEntered;
							dialog.dismiss();
						} else {
							Toast.makeText(context,
									"Incorrect password. Please try again.",
									Toast.LENGTH_SHORT).show();
						}
					}
				}
			});

			buttonSaveAndClose.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					password = editTextPassword.getText().toString();
					if (Utils.hasNetworkConnection(context)) {
						if (password.length() == 0) {
							dialog.dismiss();
						} else {
							new AsyncGetPasswordSaveNClose().execute();
						}
					} else {

						Log.i(TAG, Utils.getPasswordImage(context) + "");

						if (password.length() == 0) {
							dialog.dismiss();
						} else {
							if (password.equalsIgnoreCase(Utils
									.getPasswordImage(context))) {
								isPasswordEntered = !isPasswordEntered;
								password = "";
								dialog.dismiss();
							} else {
								Toast.makeText(
										context,
										"Incorrect password. Please try again.",
										Toast.LENGTH_SHORT).show();

							}
						}
					}
				}
			});
		} catch (Exception e) {
		}

		dialog.show();
	}

	public void likePressed(View v) {
		try {
			
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(loyalty.getFbUrl()));
			// i.setData(Uri.parse("http://www.facebook.com"));
			startActivity(i);
			buttonFb.setText("Liked");
		} catch (Exception e) {
		}
	}

	private final class AsyncGetPassword extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			showLoadingDialog();
		}

		@Override
		protected String doInBackground(Void... params) {

			JSONObject param = new JSONObject();
			try {
				param.put(Constants.PARAM_RETAILER_ID, Constants.RETAILER_ID);
				param.put(Constants.PARAM_PASSWORD, password);
				JSONObject jsonObject = HTTPHandler.defaultHandler().doPost(
						Constants.URL_VERIFY_LOYALTY_PASS, param);

				return jsonObject.getString("errorCode");

			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}

		}

		@Override
		protected void onPostExecute(String errCode) {

			dismissLoadingDialog();

			if (errCode.equalsIgnoreCase("1")) {
				dialog.dismiss();

				Utils.setPassword(context, password);

				if (isPasswordEntered) {
					if (stamp1) {
						Utils.setStatusLoyaltyStamp1(context, 1);
					} else {
						Utils.setStatusLoyaltyStamp1(context, 0);
					}

					if (stamp2) {
						Utils.setStatusLoyaltyStamp2(context, 1);
					} else {
						Utils.setStatusLoyaltyStamp2(context, 0);
					}

					if (stamp3) {
						Utils.setStatusLoyaltyStamp3(context, 1);
					} else {
						Utils.setStatusLoyaltyStamp3(context, 0);
					}

					if (stamp4) {
						Utils.setStatusLoyaltyStamp4(context, 1);
					} else {
						Utils.setStatusLoyaltyStamp4(context, 0);
					}

					if (stamp5) {
						Utils.setStatusLoyaltyStamp5(context, 1);
					} else {
						Utils.setStatusLoyaltyStamp5(context, 0);
					}
				}

				isPasswordEntered = !isPasswordEntered;

				Log.i("Password Result", "Success");

			} else {

				Toast.makeText(context,
						"Incorrect password. Please try again.",
						Toast.LENGTH_SHORT).show();
			}

		}
	}

	private final class AsyncGetPasswordSaveNClose extends
			AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			showLoadingDialog();
		}

		@Override
		protected String doInBackground(Void... params) {

			JSONObject param = new JSONObject();
			try {
				param.put(Constants.PARAM_RETAILER_ID, Constants.RETAILER_ID);
				param.put(Constants.PARAM_PASSWORD, password);
				JSONObject jsonObject = HTTPHandler.defaultHandler().doPost(
						Constants.URL_VERIFY_LOYALTY_PASS, param);

				return jsonObject.getString("errorCode");

			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}

		}

		@Override
		protected void onPostExecute(String errCode) {

			dismissLoadingDialog();

			if (errCode.equalsIgnoreCase("1")) {
				dialog.dismiss();

				Utils.setPassword(context, password);

				if (isPasswordEntered) {
					if (stamp1) {
						Utils.setStatusLoyaltyStamp1(context, 1);
					} else {
						Utils.setStatusLoyaltyStamp1(context, 0);
					}

					if (stamp2) {
						Utils.setStatusLoyaltyStamp2(context, 1);
					} else {
						Utils.setStatusLoyaltyStamp2(context, 0);
					}

					if (stamp3) {
						Utils.setStatusLoyaltyStamp3(context, 1);
					} else {
						Utils.setStatusLoyaltyStamp3(context, 0);
					}

					if (stamp4) {
						Utils.setStatusLoyaltyStamp4(context, 1);
					} else {
						Utils.setStatusLoyaltyStamp4(context, 0);
					}

					if (stamp5) {
						Utils.setStatusLoyaltyStamp5(context, 1);
					} else {
						Utils.setStatusLoyaltyStamp5(context, 0);
					}
				}

				isPasswordEntered = !isPasswordEntered;

				Log.i("Password Result", "Success");
				password = "";

			} else {

				Toast.makeText(context,
						"Incorrect password. Please try again.",
						Toast.LENGTH_SHORT).show();
			}

		}
	}

	private final class AsyncGetLoyalty extends AsyncTask<Void, Void, Boolean> {

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
						Constants.URL_GET_LOYALTY, param);

				if (jsonObject.getString("errorCode").equals("1")) {
					Loyalty loyalty = new Loyalty();

					loyalty.setLoyaltyImage(jsonObject
							.getString("loyaltyImage"));
					loyalty.setTermsCond(jsonObject.getString("termsCond"));
					loyalty.setFbIconDisplay(jsonObject
							.getString("fbIconDisplay"));

					if (jsonObject.getString("fbIconDisplay").equals("1")) {
						loyalty.setFbUrl(jsonObject.getString("fbUrl"));
					} else {
						loyalty.setFbUrl("");
					}
					loyaltyres = gson.fromJson(
							jsonObject.toString(), Loyalty.class);
					String json=gson.toJson(loyaltyres);
					pref=getSharedPreferences(Constants.PREF_NAME, Constants.PRIVATE_MODE);
				    pref.edit().putString("GetLoyalty", json).commit();

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
			pref=context.getSharedPreferences(Constants.PREF_NAME, Constants.PRIVATE_MODE);
			gson = new Gson();
		    String json = pref.getString("GetLoyalty", "");
		   
		    	loyalty = gson.fromJson(json, Loyalty.class);

			// try {
			// if (loyalty.getFbIconDisplay().equalsIgnoreCase("0")) {
			// ((RelativeLayout)
			// findViewById(R.id.botLayout)).setVisibility(View.GONE);
			// buttonFb.setVisibility(View.GONE);
			// } else {
			// ((RelativeLayout)
			// findViewById(R.id.botLayout)).setVisibility(View.VISIBLE);
			// buttonFb.setVisibility(View.VISIBLE);
			// }
			// } catch (Exception e) {
			// buttonFb.setVisibility(View.GONE);
			// }
		    if(loyalty.getTermsCond()!=null)
			textViewTermsCond.setText(loyalty.getTermsCond());
			imageCacheloader.displayImage(loyalty.getLoyaltyImage(),
					R.drawable.image_placeholder, imageViewLoyalty);

			dismissLoadingDialog();
		}
	}
}
