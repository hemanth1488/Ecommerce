package com.ecommerce.app;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.app.R;
import com.ecommerce.app.custom.BaseActivity;
import com.ecommerce.app.models.Product;
import com.ecommerce.app.models.Products;
import com.ecommerce.app.models.Retailer;
import com.ecommerce.app.models.RetailerInfoResponse;
import com.ecommerce.app.models.Voucher;
import com.ecommerce.app.utils.Constants;
import com.ecommerce.app.utils.ObjectSerializer;
import com.ecommerce.app.utils.Utils;
import com.google.gson.Gson;

public class EShopDetailActivity extends BaseActivity {

	private TextView textViewHeader;
	private Product product;
	private Activity activity;
	private TextView textViewName, textViewShortDesc, textViewDetail,
			textViewHowItWorks, textViewNewPrice;
	private ImageView imageView;
	private Button buttonBuy;
	private RelativeLayout relProductDet, relProductHowItWorks;
	private EditText editTextQty;
	private View list_left1, list_bot1;
	private View list_left2, list_bot2;
	private View lineTop, lineBot;
	private SharedPreferences pref;
	private Gson gson;
	private Retailer retailer;
	private Boolean commercial;
	private ImageButton cart;
	private ArrayList<Products> shoppingCart;
	

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eshop_detail);
		list_bot1 = (View) findViewById(R.id.list_bot1);
		list_bot2 = (View) findViewById(R.id.list_bot2);
		list_left1 = (View) findViewById(R.id.list_left1);
		list_left2 = (View) findViewById(R.id.list_left2);
		
		textViewHeader = (TextView) findViewById(R.id.textViewHeader);
		imageView = (ImageView) findViewById(R.id.iv_eshop_detail_image);
		textViewDetail = (TextView) findViewById(R.id.tv_product_detail_txt);
		textViewName = (TextView) findViewById(R.id.tv_eshop_detail_name);
		textViewShortDesc = (TextView) findViewById(R.id.tv_eshop_detail_short_desc);
		textViewHowItWorks = (TextView) findViewById(R.id.tv_how_it_works_txt);
		editTextQty = (EditText) findViewById(R.id.edt_qty);
		lineBot = (View) findViewById(R.id.lineBot);
		lineTop = (View) findViewById(R.id.lineTop);
		// textViewOldPrice = (TextView)
		// findViewById(R.id.tv_shop_detail_old_price);
		textViewNewPrice = (TextView) findViewById(R.id.tv_shop_detail_new_price);
		relProductDet = (RelativeLayout) findViewById(R.id.relProductDet);
		relProductHowItWorks = (RelativeLayout) findViewById(R.id.relProductHowItWorks);
		buttonBuy = (Button) findViewById(R.id.btn_buy);
		cart=(ImageButton) findViewById(R.id.imageViewCart);
		
		Intent intent = getIntent();
		product = (Product) intent.getSerializableExtra("product");
		activity = this;
		pref=context.getSharedPreferences(Constants.PREF_NAME, Constants.PRIVATE_MODE);
		gson = new Gson();
	    String json = pref.getString("RetailerInfoResponse", "");
	    commercial=pref.getBoolean("commercial", false);
	    RetailerInfoResponse res = gson.fromJson(json, RetailerInfoResponse.class);
		
		retailer = res.getRetailerData();
		

		editTextQty.setBackgroundDrawable(getGradientDrawableEditText(retailer
				.getHeaderColor()));
		textViewHeader.setText(product.getShortDescription());

		textViewName.setText(product.getName());
		textViewShortDesc.setText(product.getShortDescription());
		textViewDetail.setText(product.getDescription());
		textViewHowItWorks.setText(product.getHowItWorks());
		// textViewOldPrice.setText("$" + product.getOldPrice());
		textViewNewPrice.setText("$" + product.getNewPrice());
		/*textViewNewPrice.setTextColor(Color.parseColor("#"
				+ retailer.getRetailerTextColor()));*/
		// textViewOldPrice.setPaintFlags(textViewOldPrice.getPaintFlags()
		// | Paint.STRIKE_THRU_TEXT_FLAG);
		cart.setVisibility(View.VISIBLE);
		cart.setBackgroundDrawable(getGradientDrawableEditText(retailer
				.getHeaderColor()));
		lineTop.setBackgroundColor(Color.parseColor("#"
				+ retailer.getHeaderColor()));
		lineBot.setBackgroundColor(Color.parseColor("#"
				+ retailer.getHeaderColor()));

		imageCacheloader.displayImage(product.getImage(),
				R.drawable.image_placeholder, imageView);
		buttonBuy.setTextColor(Color.parseColor("#"
				+ retailer.getRetailerTextColor()));
		buttonBuy.setBackgroundDrawable(getGradientDrawable(retailer
				.getHeaderColor()));
		editTextQty.setText("1");

		if (product.getDescription().length() == 0) {
			relProductDet.setVisibility(View.GONE);
			textViewDetail.setVisibility(View.GONE);
		}

		if (product.getHowItWorks().length() == 0) {
			relProductHowItWorks.setVisibility(View.GONE);
			textViewHowItWorks.setVisibility(View.GONE);
		}

		setHeaderTheme(activity, retailer.getRetailerTextColor(),
				retailer.getHeaderColor());
		list_bot1.setBackgroundColor(Color.parseColor("#"
				+ retailer.getHeaderColor()));
		list_bot2.setBackgroundColor(Color.parseColor("#"
				+ retailer.getHeaderColor()));
		list_left1.setBackgroundColor(Color.parseColor("#"
				+ retailer.getHeaderColor()));
		list_left2.setBackgroundColor(Color.parseColor("#"
				+ retailer.getHeaderColor()));

		try {
			if (intent.getStringExtra("FROM").equalsIgnoreCase("PAYPAL")) {
				Log.i(TAG, intent.getStringExtra("message"));

				if (intent.getStringExtra("status").equalsIgnoreCase("success")) {
					DisplayMetrics metrics = getResources().getDisplayMetrics();
					int width = metrics.widthPixels;

					final Dialog dialog = new Dialog(context);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.dialog_order_success);

					dialog.getWindow().setLayout((6 * width) / 7,
							LayoutParams.WRAP_CONTENT);

					TextView textViewProductName = (TextView) dialog
							.findViewById(R.id.textViewProductName);
					TextView textViewMessage = (TextView) dialog
							.findViewById(R.id.textViewMessage);
					TextView textViewTitle = (TextView) dialog
							.findViewById(R.id.textViewTitle);

					TextView textViewProductQuantity = (TextView) dialog
							.findViewById(R.id.textViewProductQuantity);
					ImageView imageView = (ImageView) dialog
							.findViewById(R.id.imageView);

					Button buttonClose = (Button) dialog
							.findViewById(R.id.buttonClose);

					textViewMessage.setText(intent.getStringExtra("message"));
					textViewProductName.setText(product.getShortDescription());

					textViewTitle.setTextColor(Color.parseColor("#"
							+ retailer.getRetailerTextColor()));
					textViewTitle
							.setBackgroundDrawable(getGradientDrawableNoRad(retailer
									.getHeaderColor()));

					textViewProductQuantity.setText(editTextQty.getText()
							.toString());

					buttonClose.setTextColor(Color.parseColor("#"
							+ retailer.getRetailerTextColor()));
					buttonClose
							.setBackgroundDrawable(getGradientDrawable(retailer
									.getHeaderColor()));

					buttonClose.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();

						}
					});

					imageCacheloader.displayImage(product.getImage(),
							R.drawable.image_placeholder, imageView);

					dialog.show();
				}
			}
		} catch (Exception e) {

		}

		editTextQty.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				try {

					double val = Double.parseDouble(product.getNewPrice())
							* Double.parseDouble(editTextQty.getText()
									.toString());

					textViewNewPrice.setText(String.format("$%.2f", val));

				} catch (Exception e) {
					textViewNewPrice.setText(String.format("$%.2f", 0.00));
					e.printStackTrace();
				}

			}
		});
		

	}

	public void buyPressed(View v) {
		//ObjectSerializer obSerializer=new ObjectSerializer();
		if (editTextQty.getText().toString().length() != 0
				&& !editTextQty.getText().toString().equalsIgnoreCase("0")) {
			try {
				shoppingCart = (ArrayList<Products>) ObjectSerializer.deserialize(pref.getString("Cart", ObjectSerializer.serialize(new ArrayList<Products>())));
			
			 if (shoppingCart==null) {
		            shoppingCart = new ArrayList<Products>();
		            }
			 
			 		product.setQty(editTextQty.getText().toString());
			 		
			 		shoppingCart.add(new Products(product.getId(),editTextQty.getText().toString(),product));
		            pref.edit().putString("Cart", ObjectSerializer.serialize(shoppingCart)).commit();
			 
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        
			
			
			if (Utils.hasNetworkConnection(context)) {
				
				Intent intent = new Intent(this, ShoppingCartActivity.class);
				intent.putExtra("FROM", "ESHOP");
				intent.putExtra("product", product);
				startActivity(intent);
			} else {
				Toast.makeText(context,
						"You need internet connection to buy a product.",
						Toast.LENGTH_LONG).show();
			}
		}else {
			Toast.makeText(context, "Please enter a valid quantity.",
					Toast.LENGTH_SHORT).show();
		}
	}

}
