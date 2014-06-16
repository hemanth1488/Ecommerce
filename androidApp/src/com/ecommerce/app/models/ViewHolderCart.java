package com.ecommerce.app.models;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecommerce.app.R;

public class ViewHolderCart {
	private View row;
	private ImageView image = null;
	private TextView name = null;
	private TextView shortDesc = null;
	private TextView oldPrice = null;
	private TextView newPrice = null;
	private ImageView ivStroke = null;

	public ViewHolderCart(View row) {
		this.row = row;
	}

	

	public ImageView getImage() {
		if (null == image) {
			image = (ImageView) row.findViewById(R.id.product_img);
		}
		return image;
	}

	public TextView getName() {
		if (null == name) {
			name = (TextView) row.findViewById(R.id.product_name);
		}
		return name;
	}

	



	public TextView getNewPrice() {
		if (null == newPrice) {
			newPrice = (TextView) row.findViewById(R.id.product_price);
		}
		return newPrice;
	}

}
