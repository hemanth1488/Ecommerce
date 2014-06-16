package com.ecommerce.app.models;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.VideoView;

import com.ecommerce.app.R;

public class ViewHolderVoucher {
	private View row;
	public ImageButton imageViewClose = null;
	public ImageView imageViewVoucher = null;
	public VideoView videoView = null;

	public ViewHolderVoucher(View row) {
		this.row = row;
	}

	public ImageButton getClose() {
		if (null == imageViewClose) {
			imageViewClose = (ImageButton) row.findViewById(R.id.iv_close);
		}
		return imageViewClose;
	}

	public ImageView getImage() {
		if (null == imageViewVoucher) {
			imageViewVoucher = (ImageView) row.findViewById(R.id.iv_voucher);
		}
		return imageViewVoucher;
	}

	public VideoView getVideo() {
		if (null == videoView) {
			videoView = (VideoView) row.findViewById(R.id.videoView);
		}
		return videoView;
	}

}
