package com.ecommerce.app.adapters;


import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ecommerce.app.R;
import com.ecommerce.app.models.Product;
import com.ecommerce.app.models.ViewHolderEShop;
import com.ecommerce.app.utils.ImageCacheLoader;

public class EShopListAdapter extends ArrayAdapter<Product> {

	private List<Product> objects;
	private Context context;
	private ImageCacheLoader imageCacheLoader;

	public EShopListAdapter(Context context, int resource, List<Product> objects) {
		super(context, resource, objects);
		this.objects = objects;
		this.context = context;
		imageCacheLoader = new ImageCacheLoader(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolderEShop holder = null;
		ImageView imageView = null;
		ImageView ivStroke = null;

		if (null == convertView) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.row_eshop, null);
			holder = new ViewHolderEShop(convertView);
			convertView.setTag(holder);
		}
		holder = (ViewHolderEShop) convertView.getTag();

		Product object = objects.get(position);

		holder.getName().setText(object.getName());
		holder.getShortDesc().setText(object.getShortDescription());
		holder.getOldPrice().setText("$" + object.getOldPrice());
		holder.getNewPrice().setText("$" + object.getNewPrice());

		ivStroke = holder.getStroke();

		// ImageView ivStrike = (ImageView)findViewById(R.id.ivStrike);

		float textSize = holder.getOldPrice().getTextSize() / 2;
		int textLength = holder.getOldPrice().getText().length();
		int totalLengthApprox = (int) (textLength * textSize) - textLength;
		int height = 8; // YOUR_REQUIRED_HEIGHT

		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
				totalLengthApprox, height);
		param.addRule(RelativeLayout.CENTER_VERTICAL
				| RelativeLayout.ALIGN_PARENT_LEFT);
		param.setMargins(4, 0, 0, 0);
		ivStroke.setLayoutParams(param);

		imageView = holder.getImage();

		imageCacheLoader.displayImage(object.getImage(),
				R.drawable.image_placeholder, imageView);

		return convertView;
	}

}
