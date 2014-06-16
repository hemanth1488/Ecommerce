package com.ecommerce.app.adapters;

import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;





import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecommerce.app.R;
import com.ecommerce.app.models.Product;
import com.ecommerce.app.models.Products;
import com.ecommerce.app.models.ViewHolderCart;
import com.ecommerce.app.models.ViewHolderEShop;
import com.ecommerce.app.utils.ImageCacheLoader;

public class ShoppingCartAdapter extends ArrayAdapter<Products> {

	private List<Products> objects;
	private Context context;
	private int layoutResourceId;
	private ImageCacheLoader imageCacheLoader;

	public ShoppingCartAdapter(Context context, int resource, List<Products> objects) {
		super(context, resource, objects);
		this.objects = objects;
		this.context = context;
		this.layoutResourceId=resource;
		imageCacheLoader = new ImageCacheLoader(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		RequestHolder holder = null;
		View row = convertView;
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		row = inflater.inflate(layoutResourceId, parent, false);
		holder = new RequestHolder();
		holder.products = objects.get(position);
		holder.product=objects.get(position).getProduct();
		holder.quan=objects.get(position).getQuantity();
		holder.imageUrl=objects.get(position).getProduct().getImage();
		holder.name = (TextView)row.findViewById(R.id.product_name);
		holder.quantity=(TextView)row.findViewById(R.id.product_quantity);
		holder.total=(TextView)row.findViewById(R.id.product_total);
		holder.price=(TextView)row.findViewById(R.id.product_price);
		holder.productImage = (ImageView)row.findViewById(R.id.product_img);
		//imageView = holder.productImage;

		imageCacheLoader.displayImage(holder.imageUrl,
				R.drawable.image_placeholder, holder.productImage);
		setupItem(holder);
		return row;
	}
		
private void setupItem(RequestHolder holder) {
		
		
						holder.name.setText(holder.product.getShortDescription());
						holder.price.setText(holder.product.getNewPrice());
						holder.quantity.setText(holder.quan);
						double val = Double.parseDouble(holder.product.getNewPrice())
								* Double.parseDouble(holder.products.getQuantity());

						holder.total.setText(String.format("$%.2f", val));
						
					
		
}

		
	
	public static class RequestHolder {
		TextView price;
		TextView name;
		TextView quantity;
		TextView total;
		Products products;
		Product product;
		ImageView productImage;
		String imageUrl;
		String quan;
	}
}
