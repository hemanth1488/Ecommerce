package com.ecommerce.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.ecommerce.app.R;
import com.ecommerce.app.custom.BaseActivity;

public class VoucherDisplayActivity extends BaseActivity {

	private ImageView imageView;
	private VideoView videoView;
	private Intent intent;
	private String msg, type;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voucher_display);

		imageView = (ImageView) findViewById(R.id.iv_voucher);
		videoView = (VideoView) findViewById(R.id.videoView);

		intent = getIntent();

		msg = intent.getStringExtra("msg");
		type = intent.getStringExtra("type");

		if (type.equalsIgnoreCase("Image")) {
			imageCacheloader.displayImage(msg, R.drawable.image_placeholder,
					imageView);
			imageView.setVisibility(View.VISIBLE);
			videoView.setVisibility(View.INVISIBLE);
		} else {

			videoView.setVideoPath(msg);
			MediaController mediaController = new MediaController(this);
			mediaController.setAnchorView(videoView);
			videoView.setMediaController(mediaController);
			videoView.requestFocus();
			videoView.seekTo(1);
			videoView.start();
			videoView.setVisibility(View.VISIBLE);
			imageView.setVisibility(View.INVISIBLE);
		}

	}

	public void dismissPressed(View v) {
		Intent intent = new Intent(getApplicationContext(),
				VoucherActivity.class);
		startActivity(intent);
		finish();
	}
}
