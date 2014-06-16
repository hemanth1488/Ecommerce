package com.ecommerce.app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.app.R;
import com.ecommerce.app.custom.BaseActivity;
import com.ecommerce.app.models.Country;
import com.ecommerce.app.models.PaypalTokenRequest;
import com.ecommerce.app.models.Product;
import com.ecommerce.app.models.Profile;
import com.ecommerce.app.models.Retailer;
import com.ecommerce.app.models.RetailerInfoResponse;
import com.ecommerce.app.utils.Constants;
import com.ecommerce.app.utils.HTTPHandler;
import com.ecommerce.app.utils.ObjectSerializer;
import com.ecommerce.app.utils.ServiceHandler;
import com.ecommerce.app.utils.Utils;
import com.google.gson.Gson;

public class ProfileActivity extends BaseActivity {
	private Activity activity;
	private TextView textViewHeader;
	private ImageView imageViewOverflow;

	private ArrayList<Country> countryList = new ArrayList<Country>();
	private ArrayList<String> countryNameList = new ArrayList<String>();

	private Spinner spinnerGender;
	private Button buttonCountries;
	private EditText editTextFirstName, editTextLastName, editTextMobileNumber,
			editTextEmail, editTextAddress, editTextCity, editTextState,
			editTextZip;
	private TextView textViewDateOfBirth;
	private Button buttonSave;
	private SimpleDateFormat sdf;

	private Context context;
	private Product product;
	private PaypalTokenRequest payPalTokenRequest;
	private Intent intent;
	private Retailer retailer;
	private ArrayAdapter<String> adapter;
	private ArrayList<String> countrySearchList = new ArrayList<String>();
	private View lineTop, lineBot;
	private SharedPreferences pref;
	private Gson gson;

	@SuppressWarnings("deprecation")
	@SuppressLint("SimpleDateFormat")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		activity = this;
		context = this;

		pref=context.getSharedPreferences(Constants.PREF_NAME, Constants.PRIVATE_MODE);
		gson = new Gson();
	    String json = pref.getString("RetailerInfoResponse", "");
	   // commercial=pref.getBoolean("commercial", false);
	    RetailerInfoResponse res = gson.fromJson(json, RetailerInfoResponse.class);
		
		retailer = res.getRetailerData();

		textViewHeader = (TextView) findViewById(R.id.textViewHeader);
		imageViewOverflow = (ImageView) findViewById(R.id.imageViewOverflow);
		buttonCountries = (Button) findViewById(R.id.sp_country);
		spinnerGender = (Spinner) findViewById(R.id.sp_gender);
		buttonSave = (Button) findViewById(R.id.btn_save);
		editTextFirstName = (EditText) findViewById(R.id.pass_auth);
		editTextLastName = (EditText) findViewById(R.id.edt_lname);
		editTextMobileNumber = (EditText) findViewById(R.id.edt_mobile_number);
		editTextEmail = (EditText) findViewById(R.id.edt_email);
		editTextAddress = (EditText) findViewById(R.id.edt_address);
		editTextCity = (EditText) findViewById(R.id.edt_city);
		editTextState = (EditText) findViewById(R.id.edt_state);
		editTextZip = (EditText) findViewById(R.id.edt_zip);
		textViewDateOfBirth = (TextView) findViewById(R.id.tv_date);
		lineBot = (View) findViewById(R.id.lineBot);
		lineTop = (View) findViewById(R.id.lineTop);
		sdf = new SimpleDateFormat("dd MMM yyyy");

		lineTop.setBackgroundColor(Color.parseColor("#"
				+ retailer.getHeaderColor()));
		lineBot.setBackgroundColor(Color.parseColor("#"
				+ retailer.getHeaderColor()));

		try {
			editTextFirstName
					.setBackgroundDrawable(getGradientDrawableEditText(retailer
							.getHeaderColor()));

			editTextLastName
					.setBackgroundDrawable(getGradientDrawableEditText(retailer
							.getHeaderColor()));

			editTextMobileNumber
					.setBackgroundDrawable(getGradientDrawableEditText(retailer
							.getHeaderColor()));

			editTextEmail
					.setBackgroundDrawable(getGradientDrawableEditText(retailer
							.getHeaderColor()));

			editTextAddress
					.setBackgroundDrawable(getGradientDrawableEditText(retailer
							.getHeaderColor()));

			editTextCity
					.setBackgroundDrawable(getGradientDrawableEditText(retailer
							.getHeaderColor()));

			editTextState
					.setBackgroundDrawable(getGradientDrawableEditText(retailer
							.getHeaderColor()));

			editTextZip
					.setBackgroundDrawable(getGradientDrawableEditText(retailer
							.getHeaderColor()));

			textViewDateOfBirth
					.setBackgroundDrawable(getGradientDrawableEditText(retailer
							.getHeaderColor()));

			buttonCountries
					.setBackgroundDrawable(getGradientDrawableEditText(retailer
							.getHeaderColor()));

			spinnerGender
					.setBackgroundDrawable(getGradientDrawableEditText(retailer
							.getHeaderColor()));
		} catch (Exception e) {
		}

		textViewHeader.setText("PROFILE");
		imageViewOverflow.setVisibility(View.INVISIBLE);
		try {
			setHeaderTheme(activity, retailer.getRetailerTextColor(),
					retailer.getHeaderColor());
			buttonSave.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));
			buttonSave.setBackgroundDrawable(getGradientDrawable(retailer
					.getHeaderColor()));
		} catch (Exception e) {
		}

		textViewDateOfBirth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Calendar c = Calendar.getInstance();
				if (textViewDateOfBirth.getText().toString().length() > 0) {
					try {
						c.setTime(sdf.parse(textViewDateOfBirth.getText()
								.toString()));
					} catch (ParseException e) {

						e.printStackTrace();
					}
				}

				DatePickerDialog dialog = new DatePickerDialog(context,
						dateListener, c.get(Calendar.YEAR), c
								.get(Calendar.MONTH), c
								.get(Calendar.DAY_OF_MONTH));
				dialog.show();
			}
		});

		intent = getIntent();
		try {
			if (intent.getStringExtra("FROM").equalsIgnoreCase("ESHOP")) {
				product = (Product) intent.getSerializableExtra("product");
				buttonSave.setText("Buy");
			}
		} catch (Exception e) {
		}

		if (Utils.isProfileAvailable(context)) {
			setUpFields();
		}

		new AsyncGetCountries().execute();

	}

	public void countryPressed(View v) {
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.dialog_countries);
		dialog.setTitle("Countries");

		final EditText editTextSearch = (EditText) dialog
				.findViewById(R.id.editTextSearch);

		final ListView listViewCountries = (ListView) dialog
				.findViewById(R.id.listViewCountries);

		adapter = new ArrayAdapter<String>(getApplicationContext(),
				R.layout.list_row_text, android.R.id.text1, countryNameList);

		listViewCountries.setAdapter(adapter);

		listViewCountries.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long arg) {

				if (countrySearchList.size() > 0) {
					buttonCountries.setText(countrySearchList.get(position));
				} else {
					buttonCountries.setText(countryNameList.get(position));
				}

				dialog.dismiss();
			}

		});

		editTextSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				countrySearchList.clear();
				for (int i = 0; i < countryNameList.size(); i++) {
					if (countryNameList.get(i).toLowerCase(Locale.ENGLISH)
							.contains(s)) {
						countrySearchList.add(countryNameList.get(i));

					}
				}

				runOnUiThread(new Runnable() {

					@Override
					public void run() {

						ArrayAdapter<String> adapterSearch = new ArrayAdapter<String>(
								getApplicationContext(),
								R.layout.list_row_text, android.R.id.text1,
								countrySearchList);
						adapter.notifyDataSetChanged();
						listViewCountries.setAdapter(adapterSearch);

					}
				});

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		dialog.show();
	}

	DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int yr, int monthOfYear,
				int dayOfMonth) {

			Calendar calendar = Calendar.getInstance();
			calendar.set(yr, monthOfYear, dayOfMonth);

			String dateString = sdf.format(calendar.getTime());

			textViewDateOfBirth.setText(dateString);
		}
	};

	private void setUpFields() {
		
		String json = pref.getString("Profile", "");
		   // commercial=pref.getBoolean("commercial", false);
		    Profile profile = gson.fromJson(json, Profile.class);
		editTextFirstName.setText(profile.getFirstName());
		editTextLastName.setText(profile.getLastName());
		editTextAddress.setText(profile.getAddress());
		editTextCity.setText(profile.getCity());
		editTextEmail.setText(profile.getEmail());
		editTextMobileNumber.setText(profile.getMobileNo() + "");
		editTextState.setText(profile.getState());
		buttonCountries.setText(profile.getCountry());
		editTextZip.setText(profile.getZip() + "");

		textViewDateOfBirth.setText(profile.getDob());

		spinnerGender
				.setSelection(profile.getGender().equalsIgnoreCase("MALE") ? 0
						: 1);
	}

	public void savePressed(View v) {

		if (Utils.hasNetworkConnection(context)) {
			if (checkFields()) {
				new AsyncWorker().execute();
			}
		} else {

			try {
				if (intent.getStringExtra("FROM").equalsIgnoreCase("ESHOP")) {
					Toast.makeText(context,
							"You need internet connection to buy a product.",
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				Toast.makeText(context,
						"You need internet connection to save your profile.",
						Toast.LENGTH_LONG).show();
			}

		}
	}

	private void populateCountries() {

		for (int i = 1; i < countryList.size(); i++) {
			countryNameList.add(countryList.get(i).getCountryName());
		}

	}

	private boolean checkFields() {
		boolean status = true;

		if (editTextFirstName.getText().toString().trim().length() == 0) {
			Toast.makeText(context, "Enter your firstname", Toast.LENGTH_SHORT)
					.show();
			status = false;
		} else if (editTextLastName.getText().toString().trim().length() == 0) {
			Toast.makeText(context, "Enter your last name", Toast.LENGTH_SHORT)
					.show();
			status = false;
		} else if (textViewDateOfBirth.getText().toString().trim().length() == 0) {
			Toast.makeText(context, "Enter your date of birth",
					Toast.LENGTH_SHORT).show();
			status = false;
		} else if (editTextMobileNumber.getText().toString().trim().length() == 0) {
			Toast.makeText(context, "Enter your mobile number",
					Toast.LENGTH_SHORT).show();
			status = false;
		} else if (editTextEmail.getText().toString().trim().length() == 0) {
			Toast.makeText(context, "Enter your email", Toast.LENGTH_SHORT)
					.show();
			status = false;
		} else if (editTextAddress.getText().toString().trim().length() == 0) {
			Toast.makeText(context, "Enter your address", Toast.LENGTH_SHORT)
					.show();
			status = false;
		} else if (editTextCity.getText().toString().trim().length() == 0) {
			Toast.makeText(context, "Enter your city", Toast.LENGTH_SHORT)
					.show();
			status = false;
		} else if (buttonCountries.getText().length() == 0) {

			Toast.makeText(context, "Select your country", Toast.LENGTH_SHORT)
					.show();
			status = false;
		} else if (editTextZip.getText().toString().trim().length() == 0) {

			Toast.makeText(context, "Enter your zip", Toast.LENGTH_SHORT)
					.show();
			status = false;
		} else {
			status = true;
		}

		return status;
	}

	private class AsyncGetCountries extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			showLoadingDialog();
		}

		@SuppressWarnings("unchecked")
		@Override
		protected Boolean doInBackground(Void... arg0) {

			if (Utils.hasNetworkConnection(context)) {

				Boolean status = false;
				try {
					ServiceHandler jsonParser = new ServiceHandler();
					String json = jsonParser.makeServiceCall(
							Constants.URL_GET_COUNTRIES, ServiceHandler.GET);
					if (json != null) {

						JSONArray jsonArray = new JSONArray(json);
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject country = (JSONObject) jsonArray.get(i);
							Country cat = new Country();
							cat.setCountryCode(country.getString("countryCode"));
							cat.setCountryName(country.getString("countryName"));
							countryList.add(cat);
						}
						
						pref.edit().putString("Countries", ObjectSerializer.serialize(countryList));
						/*sqliteHelper.openDataBase();
						for (int i = 0; i < countryList.size(); i++) {
							sqliteHelper.insertOrReplaceCountry(countryList
									.get(i));
						}
						sqliteHelper.close();*/

						status = true;
					} else {
						status = false;
					}
				} catch (Exception e) {
					return false;
				}
				return status;
			} else {

				try {
					
					countryList = (ArrayList<Country>) ObjectSerializer.deserialize(pref.getString("Countries", ObjectSerializer.serialize(new ArrayList<Country>())));
					
				} catch (Exception e) {
				}

				return false;
			}

		}

		@Override
		protected void onPostExecute(Boolean result) {

			populateCountries();

			dismissLoadingDialog();

		}
	}

	private class AsyncWorker extends AsyncTask<Void, Void, Boolean> {

		@SuppressWarnings("deprecation")
		@Override
		protected Boolean doInBackground(Void... params) {

			if (Utils.hasNetworkConnection(getApplicationContext())) {
				JSONObject obj = new JSONObject();
				Boolean status = false;
				try {

					Date dateOfBirth = new Date();
					if (textViewDateOfBirth.getText().length() > 0) {
						dateOfBirth = sdf.parse(textViewDateOfBirth.getText()
								.toString());
					}

					Calendar cal = Calendar.getInstance();
					String countryCode="";
					for(Country c:countryList){
						if(c.getCountryName().equalsIgnoreCase(buttonCountries.getText().toString()))
							countryCode= c.getCountryCode();
					}
					
					
								
					

					obj.put("retailerId", Constants.RETAILER_ID);
					obj.put("fname", editTextFirstName.getText().toString());
					obj.put("lname", editTextLastName.getText().toString());
					obj.put("gender",
							(spinnerGender.getSelectedItemPosition() == 0) ? "M"
									: "F");
					obj.put("age", new Date().getYear() - dateOfBirth.getYear());
					obj.put("dob", textViewDateOfBirth.getText().toString());
					obj.put("mobile_num", editTextMobileNumber.getText()
							.toString());
					obj.put("email", editTextEmail.getText().toString());
					obj.put("address", editTextAddress.getText().toString());
					obj.put("city", editTextCity.getText().toString());
					obj.put("state", editTextState.getText().toString());

					obj.put("country", countryCode);
					obj.put("zip",
							Integer.parseInt(editTextZip.getText().toString()));
					obj.put("lat", Constants.LAT);
					obj.put("long", Constants.LNG);
					obj.put("device_token", Constants.REG_ID);
					obj.put("device", 2);
					obj.put("latestTime", cal.getTimeInMillis());

					JSONObject jsonObject = HTTPHandler.defaultHandler()
							.doPost(Constants.URL_SAVE_CONSUMER_PROFILE, obj);

					if (jsonObject.getString("errorCode").equals("1")) {
						status = true;

					} else {
						status = false;
					}

					return status;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}

			} else {

				return false;
			}

		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(Boolean result) {

			if (result) {
				Date dateOfBirth = new Date();
				if (textViewDateOfBirth.getText().length() > 0) {

					try {
						dateOfBirth = sdf.parse(textViewDateOfBirth.getText()
								.toString());
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}

				Profile profile = new Profile();
				profile.setAddress(editTextAddress.getText().toString());
				profile.setAge(new Date().getYear() - dateOfBirth.getYear());
				profile.setCity(editTextCity.getText().toString());
				profile.setCountry(buttonCountries.getText().toString());
				profile.setDeviceToken(Constants.REG_ID);
				profile.setDob(textViewDateOfBirth.getText().toString().trim());
				profile.setEmail(editTextEmail.getText().toString());
				profile.setFirstName(editTextFirstName.getText().toString());
				profile.setLastName(editTextLastName.getText().toString());
				profile.setGender(spinnerGender.getSelectedItem().toString());
				profile.setLat(Constants.LAT);
				profile.setLng(Constants.LNG);
				profile.setMobileNo(Long.parseLong(editTextMobileNumber
						.getText().toString()));
				profile.setState(editTextState.getText().toString());
				profile.setTime(new Date().getTime());
				profile.setZip(Long.parseLong(editTextZip.getText().toString()));
				
				String json = gson.toJson(profile);
				pref=getSharedPreferences(Constants.PREF_NAME, Constants.PRIVATE_MODE);
				pref.edit().putString("Profile", json).commit();
				/*sqliteHelper.openDataBase();
				sqliteHelper.insertOrReplaceProfile(profile);
				sqliteHelper.close();*/

				Utils.setProfile(context, true);

				if (buttonSave.getText().toString().equals("Buy")) {
					new AsyncGetPayPalToken().execute();
				} else {

					Toast.makeText(context, "Profile successfully saved",
							Toast.LENGTH_LONG).show();

					try {
						if (intent.getStringExtra("FROM").equalsIgnoreCase(
								"FEEDBACK")) {
							finish();
						}
					} catch (Exception e) {

					}
				}
			}
			dismissLoadingDialog();

		}

		@Override
		protected void onPreExecute() {
			showLoadingDialog();
		}

	}

	private class AsyncGetPayPalToken extends AsyncTask<Void, Void, JSONObject> {

		@Override
		protected void onPreExecute() {

			showLoadingDialog();
			payPalTokenRequest = new PaypalTokenRequest();
			payPalTokenRequest.setRetailerId(Constants.RETAILER_ID);
			payPalTokenRequest.setProductId(Integer.parseInt(product.getId()));
			payPalTokenRequest.setQuantity(Integer.parseInt(product.getQty()));
			payPalTokenRequest.seteMail(editTextEmail.getText().toString());

		}

		@Override
		protected JSONObject doInBackground(Void... params) {

			JSONObject json = new JSONObject();
			try {
				json.put(Constants.PARAM_RETAILER_ID,
						payPalTokenRequest.getRetailerId());
				json.put(Constants.PARAM_PRODUCTID_FOR_TOKEN,
						payPalTokenRequest.getProductId());
				json.put(Constants.PARAM_QUANTITY,
						payPalTokenRequest.getQuantity());
				json.put(Constants.PARAM_EMAIL, payPalTokenRequest.geteMail());

				JSONObject result = HTTPHandler.defaultHandler().doPost(
						Constants.URL_GET_PAYPAL_TOKEN, json);

				return result;

			} catch (JSONException e) {
				e.printStackTrace();
				return null;

			}

		}

		@Override
		protected void onPostExecute(JSONObject result) {

			if (result != null && result.has("errorCode")) {

				try {
					if (result.getString("errorCode").equals("1")) {

						Bundle bundle = new Bundle();

						bundle.putSerializable("product", product);
						bundle.putString("token", result.getString("token"));
						bundle.putString("sucessUrl",
								result.getString("successUrl"));
						bundle.putString("cancelUrl",
								result.getString("cancelUrl"));

						Intent i = new Intent(context, PaypalActivity.class);
						i.putExtras(bundle);
						startActivity(i);

					}
				} catch (JSONException e) {

					e.printStackTrace();

					try {
						Toast.makeText(context,
								result.getString("errorMessage"),
								Toast.LENGTH_SHORT).show();

					} catch (JSONException e1) {

						e1.printStackTrace();
					}
				}
			}

			dismissLoadingDialog();

		}

	}
}
