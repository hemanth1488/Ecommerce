package com.ecommerce.app.adapters;


import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.ecommerce.app.R;
import com.ecommerce.app.models.ViewHolderVoucher;
import com.ecommerce.app.models.Voucher;
import com.ecommerce.app.utils.ImageCacheLoader;

public class VouchersListAdapter extends ArrayAdapter<Voucher> {

	private Context context;
	private ArrayList<Voucher> voucherList;
	private ImageCacheLoader imageLoader;

	public VouchersListAdapter(Context context, int resource,
			ArrayList<Voucher> voucherList) {
		super(context, resource, voucherList);

		this.context = context;
		this.voucherList = voucherList;
		this.imageLoader = new ImageCacheLoader(context);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolderVoucher holder = null;
		ImageView imageViewVoucher = null;
		VideoView videoView = null;

		if (null == convertView) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.row_voucher, null);
			holder = new ViewHolderVoucher(convertView);
			convertView.setTag(holder);
		}

		holder = (ViewHolderVoucher) convertView.getTag();

		Voucher voucher = voucherList.get(position);

		imageViewVoucher = holder.getImage();
		videoView = holder.getVideo();

		if (voucher.getType().equalsIgnoreCase("Image")) {
			imageViewVoucher.setVisibility(View.VISIBLE);
			videoView.setVisibility(View.GONE);
			imageLoader.displayImage(voucher.getMsg(),
					R.drawable.image_placeholder, imageViewVoucher);
		} else {
			imageViewVoucher.setVisibility(View.GONE);
			videoView.setVisibility(View.VISIBLE);
			videoView.setVideoPath(voucher.getMsg());
			MediaController mediaController = new MediaController(context);
			mediaController.setAnchorView(videoView);
			videoView.setMediaController(mediaController);
			videoView.requestFocus();
			videoView.seekTo(1);
		}

		return convertView;
	}

}
