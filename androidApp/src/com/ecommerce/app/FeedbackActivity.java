package com.ecommerce.app;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.app.R;
import com.ecommerce.app.custom.BaseActivity;
import com.ecommerce.app.models.Profile;
import com.ecommerce.app.models.Retailer;
import com.ecommerce.app.models.RetailerInfoResponse;
import com.ecommerce.app.utils.Constants;
import com.ecommerce.app.utils.HTTPHandler;
import com.ecommerce.app.utils.Utils;
import com.google.gson.Gson;

public class FeedbackActivity extends BaseActivity {

	private Context context;
	private Activity activity;
	private TextView textViewHeader;
	private View viewLineHome, viewLineEShop, viewLineLoyalty,
			viewLineFeedback, viewLineVouchers;
	private EditText editTextFeedback, editTextFriendName, editTextFriendEmail,
			editTextFriendMobile;
	private Button buttonSubmit, buttonRefer;
	private ImageView imageViewLoyaltyLogo;
	private Profile profile;
	private RadioGroup radioGroupFeedback;
	private RadioButton radioButtonFeedback;
	private Retailer retailer;
	private SharedPreferences pref;
	private Gson gson;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		activity = this;
		context = this;
		pref=context.getSharedPreferences(Constants.PREF_NAME, Constants.PRIVATE_MODE);
		gson = new Gson();
	    String json = pref.getString("RetailerInfoResponse", "");
	    RetailerInfoResponse res = gson.fromJson(json, RetailerInfoResponse.class);
		retailer = res.getRetailerData();
		
		

		try {
			setHeaderTheme(activity, retailer.getRetailerTextColor(),
					retailer.getHeaderColor());
		} catch (Exception e) {
		}
		textViewHeader = (TextView) findViewById(R.id.textViewHeader);
		buttonRefer = (Button) findViewById(R.id.btn_refer);
		buttonSubmit = (Button) findViewById(R.id.btn_submit);
		editTextFeedback = (EditText) findViewById(R.id.edt_feedback);
		editTextFriendEmail = (EditText) findViewById(R.id.edt_friend_email);
		editTextFriendMobile = (EditText) findViewById(R.id.edt_friend_mobile);
		editTextFriendName = (EditText) findViewById(R.id.edt_friend_name);
		imageViewLoyaltyLogo = (ImageView) findViewById(R.id.iv_loyalty_logo);
		radioGroupFeedback = (RadioGroup) findViewById(R.id.rg_feedback);

		// editTextFeedback.setBackgroundColor(Constants.HEADER_COLOR
		// - Constants.END_COLOR_LIGHTER);
		try {
			editTextFeedback
					.setBackgroundDrawable(getGradientDrawableEditText(retailer
							.getHeaderColor()));

			editTextFriendEmail
					.setBackgroundDrawable(getGradientDrawableEditText(retailer
							.getHeaderColor()));

			editTextFriendMobile
					.setBackgroundDrawable(getGradientDrawableEditText(retailer
							.getHeaderColor()));

			editTextFriendName
					.setBackgroundDrawable(getGradientDrawableEditText(retailer
							.getHeaderColor()));

			textViewHeader.setText("FEEDBACK");
			buttonRefer.setBackgroundDrawable(getGradientDrawable(retailer
					.getHeaderColor()));
			buttonSubmit.setBackgroundDrawable(getGradientDrawable(retailer
					.getHeaderColor()));
			buttonRefer.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));
			buttonSubmit.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));
		} catch (Exception e) {
		}

		editTextFeedback.setScroller(new Scroller(context));

		editTextFeedback.setVerticalScrollBarEnabled(true);

		setViewLines();

		imageCacheloader.displayImage(Utils.getFeedbackImage(context),
				R.drawable.ic_launcher, imageViewLoyaltyLogo);

		if (Utils.hasNetworkConnection(context)) {
			new AsyncGetGiftImage().execute();
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
		viewLineLoyalty.setVisibility(View.INVISIBLE);
		viewLineFeedback.setVisibility(View.VISIBLE);
		viewLineVouchers.setVisibility(View.INVISIBLE);
	}

	public void submitPressed(View v) {
		if (Utils.hasNetworkConnection(context)) {
			if (Utils.isProfileAvailable(context)) {
				if (editTextFeedback.getText().toString().length() > 0) {
					new AsyncFeedback().execute();
				} else {
					Toast.makeText(context, "Enter your feedback.",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				showProfileAlert();
			}

		} else {
			Toast.makeText(context,
					"You need internet connection to send a feedback.",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void referPressed(View v) {
		if (Utils.hasNetworkConnection(context)) {
			if (Utils.isProfileAvailable(context)) {
				if (checkFields()) {
					new AsyncRefer().execute();
				}
			} else {
				showProfileAlert();
			}
		} else {
			Toast.makeText(context,
					"You need internet connection to refer a friend.",
					Toast.LENGTH_SHORT).show();
		}

	}

	@SuppressWarnings("deprecation")
	private void showProfileAlert() {
		try {
			DisplayMetrics metrics = getResources().getDisplayMetrics();
			int width = metrics.widthPixels;

			final Dialog dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.dialog_profile);

			dialog.getWindow().setLayout((6 * width) / 7,
					LayoutParams.WRAP_CONTENT);

			TextView textViewTitle = (TextView) dialog
					.findViewById(R.id.textViewTitle);
			Button buttonProfile = (Button) dialog
					.findViewById(R.id.btn_profile);

			textViewTitle.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));
			textViewTitle
					.setBackgroundDrawable(getGradientDrawableNoRad(retailer
							.getHeaderColor()));

			buttonProfile.setBackgroundDrawable(getGradientDrawable(retailer
					.getHeaderColor()));

			buttonProfile.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, ProfileActivity.class);
					intent.putExtra("FROM", "FEEDBACK");
					startActivity(intent);
					dialog.dismiss();

				}
			});

			buttonProfile.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));

			dialog.show();
		} catch (Exception e) {
		}

	}

	private boolean checkFields() {
		boolean status = true;

		if (editTextFriendName.getText().toString().trim().length() == 0) {
			Toast.makeText(context, "Enter your friend's name.",
					Toast.LENGTH_SHORT).show();
			status = false;
		} else if (editTextFriendEmail.getText().toString().trim().length() == 0) {
			Toast.makeText(context, "Enter your friend's email.",
					Toast.LENGTH_SHORT).show();
			status = false;
		} else if (editTextFriendMobile.getText().toString().trim().length() == 0) {
			Toast.makeText(context, "Enter your friend's mobile.",
					Toast.LENGTH_SHORT).show();
			status = false;
		} else {
			status = true;
		}

		return status;
	}

	public static void hideSoftKeyboard(Activity activity) {

		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
				.getWindowToken(), 0);
	}

	private final class AsyncGetGiftImage extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(Void... params) {

			JSONObject param = new JSONObject();
			try {
				param.put(Constants.PARAM_RETAILER_ID, Constants.RETAILER_ID);
				JSONObject jsonObject = HTTPHandler.defaultHandler().doPost(
						Constants.URL_GET_FEEDBACK_GIFT, param);

				return jsonObject.getString("feedbackImage");

			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}

		}

		@Override
		protected void onPostExecute(String image) {

			Utils.setFeedbackImage(context, image);
			imageCacheloader.displayImage(image, R.drawable.ic_launcher,
					imageViewLoyaltyLogo);

		}
	}

	private final class AsyncRefer extends AsyncTask<Void, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
			showLoadingDialog();
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			
			
			String json = pref.getString("Profile", "");
			   // commercial=pref.getBoolean("commercial", false);
			profile = gson.fromJson(json, Profile.class);
				
				
			JSONObject param = new JSONObject();
			try {
				param.put(Constants.PARAM_RETAILER_ID, Constants.RETAILER_ID);
				param.put(Constants.PARAM_EMAIL, profile.getEmail());
				param.put(Constants.PARAM_FRIEND_NAME, editTextFriendName
						.getText().toString());
				param.put(Constants.PARAM_FRIEND_MOBILE, editTextFriendMobile
						.getText().toString());
				param.put(Constants.PARAM_FRIEND_EMAIL, editTextFriendEmail
						.getText().toString());
				param.put(Constants.PARAM_DOWNLOAD_URL, "");

				JSONObject jsonObject = HTTPHandler.defaultHandler().doPost(
						Constants.URL_REFER_FRIEND, param);

				return jsonObject;

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		}

		@Override
		protected void onPostExecute(JSONObject json) {
			dismissLoadingDialog();
			if (json != null && json.has("errorMessage")) {
				try {
					Toast.makeText(context, json.getString("errorMessage"),
							Toast.LENGTH_SHORT).show();
					editTextFriendEmail.setText("");
					editTextFriendMobile.setText("");
					editTextFriendName.setText("");

					hideSoftKeyboard(activity);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			finish();
		}
		return false;
	}

	private final class AsyncFeedback extends AsyncTask<Void, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
			showLoadingDialog();
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			sqliteHelper.openDataBase();
			profile = sqliteHelper.getProfile();
			sqliteHelper.close();
			int selectedId = radioGroupFeedback.getCheckedRadioButtonId();

			radioButtonFeedback = (RadioButton) findViewById(selectedId);
			JSONObject param = new JSONObject();
			try {
				param.put(Constants.PARAM_RETAILER_ID, Constants.RETAILER_ID);
				param.put(Constants.PARAM_EMAIL, profile.getEmail());
				param.put(Constants.PARAM_FEEDBACK_SUB, radioButtonFeedback
						.getText().toString());
				param.put(Constants.PARAM_FEEDBACK_MSG, editTextFeedback
						.getText().toString());

				JSONObject jsonObject = HTTPHandler.defaultHandler().doPost(
						Constants.URL_SEND_FEEDBACK, param);

				return jsonObject;

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		}
		
		@Override
		protected void onPostExecute(JSONObject json) {
			dismissLoadingDialog();
			if (json != null && json.has("errorMessage")) {
				try {
					Toast.makeText(context, json.getString("errorMessage"),
							Toast.LENGTH_SHORT).show();
					editTextFeedback.setText("");

					hideSoftKeyboard(activity);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
