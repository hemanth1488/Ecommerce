package com.ecommerce.app;



import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ecommerce.app.R;
import com.ecommerce.app.custom.BaseActivity;
import com.ecommerce.app.models.Product;
import com.ecommerce.app.utils.Constants;
import com.ecommerce.app.utils.ServiceHandler;
import com.ecommerce.app.utils.Utils;

public class PaypalActivity extends BaseActivity {
	private Bundle bundleArgs = null;
	private Product product;
	private Context context;
	private String message;
	private String successUrl;
	private String cancelUrl;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paypal);
		context = this;
		bundleArgs = getIntent().getExtras();

		String token = bundleArgs.getString("token");
		product = (Product) bundleArgs.getSerializable("product");

		successUrl = bundleArgs.getString("sucessUrl");
		cancelUrl = bundleArgs.getString("cancelUrl");

		WebView w = (WebView) findViewById(R.id.webView);

		String url = Constants.URL_PAYPAL_SANDBOX + token;
		w.getSettings().setJavaScriptEnabled(true);
		w.loadUrl(url);

		w.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				boolean shouldOverride = false;
				Log.i("URL", url);
				if (url.equalsIgnoreCase(successUrl)) {
					// --- Payment Success --- //
					new AsyncGetMessageSuccess().execute();
					shouldOverride = true;

				} else if (url.equalsIgnoreCase(cancelUrl)) {
					// --- Payment Failed --- //

					Intent intent = new Intent(context,
							EShopDetailActivity.class);
					intent.putExtra("product", product);
					intent.putExtra("FROM", "PAYPAL");
					intent.putExtra("status", "fail");
					startActivity(intent);
					finish();
					shouldOverride = true;
				}

				return shouldOverride;
			}

			public void onLoadResource(WebView view, String url) {
				if (url.startsWith(cancelUrl))
					showLoadingDialog();
			}

			public void onPageFinished(WebView view, String url) {

				dismissLoadingDialog();
			}
		});
	}

	private class AsyncGetMessageSuccess extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			showLoadingDialog();
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {

			if (Utils.hasNetworkConnection(context)) {

				Boolean status = false;
				try {
					ServiceHandler jsonParser = new ServiceHandler();
					String json = jsonParser.makeServiceCall(successUrl,
							ServiceHandler.GET);
					if (json != null) {

						JSONObject obj = new JSONObject(json);

						message = obj.getString("errorMessage");

						status = true;
					} else {
						status = false;
					}
				} catch (Exception e) {
					return false;
				}
				return status;
			} else {

				return false;
			}

		}

		@Override
		protected void onPostExecute(Boolean result) {

			dismissLoadingDialog();

			Intent intent = new Intent(context, EShopDetailActivity.class);
			intent.putExtra("product", product);
			intent.putExtra("FROM", "PAYPAL");
			intent.putExtra("status", "success");
			intent.putExtra("message", message);
			startActivity(intent);
			finish();

		}
	}

}
