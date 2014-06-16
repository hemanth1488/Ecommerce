package com.ecommerce.app.utils;

import android.graphics.Color;

public class Constants {

	public static String RETAILER_ID = "MerchantA1";

	// --- Global Var --- //

	public static String REG_ID = "";
	public static double LAT = 0.0;
	public static double LNG = 0.0;

	// --- Theme --- //
	public static int BACKDROP1 = Color.parseColor("#FFFFFF");
	public static int BACKDROP2 = Color.parseColor("#FFFFFF");
//	public static int HEADER_COLOR = Color.parseColor("#951015");
//	public static int END_COLOR = Color.parseColor("#B8B8B8");
//	public static int END_COLOR = Color.parseColor("#AAA41212");
	// public static int END_COLOR_LIGHTER = Color.parseColor("#7E7E7E");
//	public static int TEXT_COLOR = Color.parseColor("#000000");

	// --- Shared Preferences --- //
	public static String SPREF = "APPWIZ_SHARED_PREF";
	public static String SPLASH_IMG = "SPLASH_IMAGE";
	public static String PROFILE = "PROFILE";

	// --- Parameters --- //

	public static String PARAM_RETAILER_ID = "retailerId";
	public static String PARAM_EMAIL = "email";
	public static String PARAM_LAT = "lat";
	public static String PARAM_LONG = "long";
	public static String PARAM_DEVICE_TOKEN = "device_token";
	public static String PARAM_DEVICE = "device";
	public static String PARAM_PRODUCTID_FOR_TOKEN = "productId";
	public static String PARAM_QUANTITY = "quantity";
	public static String PARAM_FRIEND_EMAIL = "frndEmail";
	public static String PARAM_FRIEND_NAME = "frndName";
	public static String PARAM_FRIEND_MOBILE = "frndMobile";
	public static String PARAM_FEEDBACK_SUB = "feedbackSub";
	public static String PARAM_FEEDBACK_MSG = "feedbackMsg";
	public static String PARAM_DOWNLOAD_URL = "downloadUrl";
	public static String PARAM_PASSWORD = "merchantPwd";
	public static String PREF_NAME= "preference13";
	public static int PRIVATE_MODE=0;

	// --- URL --- //

	public static String HOST = "http://appwizlive.com/";

	public static String URL_GET_PRODUCTS = HOST + "/getProducts.php";
	public static String URL_MAKE_PAYMENT = HOST + "/place_order.php";
	public static String URL_GET_COUNTRIES = HOST + "/getCountries.php";
	public static String URL_GET_SPLASH = HOST + "/getSplash.php";
	public static String URL_GET_RETAILER_INFO = HOST + "/getRetailerInfo.php";
	public static String URL_SAVE_CONSUMER_PROFILE = HOST
			+ "/consumer_profile.php";
	public static String URL_SEND_DEVICE_TOKEN = HOST
			+ "/send_device_token.php";
	public static String URL_GET_LOYALTY = HOST + "/getLoyaltyInfo.php";
	public static String URL_GET_FEEDBACK_GIFT = HOST + "/getFeedbackGift.php";
	public static String URL_SEND_FEEDBACK = HOST + "/feedback_mail.php";
	public static String URL_REFER_FRIEND = HOST + "/refer_mail.php";
	public static String URL_VERIFY_LOYALTY_PASS = HOST + "/loyalty_pwd.php";
	public static String URL_GET_PAYPAL_TOKEN = HOST + "/paypal_token.php";
	public static String URL_PAYPAL_SANDBOX = "https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&useraction=commit&token=";
	public static String URL_PAY_SUCCESS = HOST + "/pay_success.php";
	public static String URL_GET_PUBLISHER_ID = HOST + "/getPublisherId.php";

}
