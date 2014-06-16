package com.ecommerce.app.models;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecommerce.app.R;

public class ViewHolderEShop {

	private View row;
	private ImageView image = null;
	private TextView name = null;
	private TextView shortDesc = null;
	private TextView oldPrice = null;
	private TextView newPrice = null;
	private ImageView ivStroke = null;

	public ViewHolderEShop(View row) {
		this.row = row;
	}

	public ImageView getStroke() {
		if (null == ivStroke) {
			ivStroke = (ImageView) row.findViewById(R.id.ivStrike);
		}
		return ivStroke;
	}

	public ImageView getImage() {
		if (null == image) {
			image = (ImageView) row.findViewById(R.id.iv_eshop);
		}
		return image;
	}

	public TextView getName() {
		if (null == name) {
			name = (TextView) row.findViewById(R.id.tv_eshop_name);
		}
		return name;
	}

	public TextView getShortDesc() {
		if (null == shortDesc) {
			shortDesc = (TextView) row.findViewById(R.id.tv_eshop_short_desc);
		}
		return shortDesc;
	}

	public TextView getOldPrice() {
		if (null == oldPrice) {
			oldPrice = (TextView) row.findViewById(R.id.tv_eshop_old_price);
		}
		return oldPrice;
	}

	public TextView getNewPrice() {
		if (null == newPrice) {
			newPrice = (TextView) row.findViewById(R.id.tv_eshop_new_price);
		}
		return newPrice;
	}

}
