package com.ecommerce.app.utils;

import java.io.IOException;
import java.util.ArrayList;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ecommerce.app.R;
import com.ecommerce.app.VoucherDisplayActivity;
import com.ecommerce.app.models.Voucher;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCMIntentService extends IntentService {

	public static int notification_id = 1;

	private static String TAG = "GCMIntentService";
	private Bundle extras;
	private SQLiteHelper sqliteHelper;

	private NotificationManager mNotificationManager;
//	private Retailer retailer;
	private String msg, type;
	private ArrayList<Voucher> voucherlist;
	private SharedPreferences pref;

	public GCMIntentService() {
		super("GCMIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		Log.i(TAG, gcm.getMessageType(intent));
		pref=getSharedPreferences(Constants.PREF_NAME, Constants.PRIVATE_MODE);
		extras = intent.getExtras();

		msg = extras.getString("msg");
		type = extras.getString("pnType");

		Voucher voucher = new Voucher();
		voucher.setMsg(msg);
		voucher.setType(type);
		try {
			
		voucherlist = (ArrayList<Voucher>) ObjectSerializer.deserialize(pref.getString("Voucher", ObjectSerializer.serialize(new ArrayList<Voucher>())));
		voucherlist.add(voucher);
		pref.edit().putString("Voucher", ObjectSerializer.serialize(voucherlist)).commit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*sqliteHelper = new SQLiteHelper(this);

		sqliteHelper.openDataBase();
		sqliteHelper.insertOrReplaceVoucher(voucher);
		sqliteHelper.close();*/
		

//		try {
//			sqliteHelper.openDataBase();
//			retailer = sqliteHelper.getRetailer();
//			sqliteHelper.close();
//			Constants.TEXT_COLOR = Color.parseColor("#"
//					+ retailer.getRetailerTextColor());
//			Constants.HEADER_COLOR = Color.parseColor("#"
//					+ retailer.getHeaderColor());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		sendNotification();

		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	// --- Send Notification --- //
	private void sendNotification() {
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent intent = new Intent(this, VoucherDisplayActivity.class);
		intent.putExtra("type", type);
		intent.putExtra("msg", msg);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("AppWiz")
				.setStyle(
						new NotificationCompat.BigTextStyle()
								.bigText("New Voucher"))
				.setContentText("New Voucher");

		mBuilder.setContentIntent(contentIntent);
		Notification notif = mBuilder.build();
		notif.flags |= Notification.FLAG_AUTO_CANCEL;

		mNotificationManager.notify(notification_id, notif);
	}

}
