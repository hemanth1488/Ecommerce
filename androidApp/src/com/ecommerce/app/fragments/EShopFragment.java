package com.ecommerce.app.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabWidget;
import android.widget.TextView;

import com.ecommerce.app.R;
import com.ecommerce.app.models.ProductCategory;
import com.ecommerce.app.models.ProductResponse;
import com.ecommerce.app.utils.Constants;
import com.ecommerce.app.utils.SQLiteHelper;
import com.google.gson.Gson;

public class EShopFragment extends Fragment {

	public String TAG = getClass().getSimpleName();
	private View root;
	private FragmentTabHost tabHost;
	private ArrayList<ProductCategory> categories = new ArrayList<ProductCategory>();
	private SharedPreferences pref;
	private Gson gson;
	private String jsontemp;
	private Context context;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_eshop, null);
		context = getActivity();

		tabHost = (FragmentTabHost) root.findViewById(android.R.id.tabhost);

		pref=context.getSharedPreferences(Constants.PREF_NAME, Constants.PRIVATE_MODE);
		gson = new Gson();
	    String json = pref.getString("productResponse", "");
	    if(!(json.equalsIgnoreCase(jsontemp))){
	    	jsontemp=json;
	    	ProductResponse data = gson.fromJson(json, ProductResponse.class);
	    
	    	categories = new ArrayList<ProductCategory>();
	    	categories = (ArrayList<ProductCategory>) data.getData();
	    
	    	setupTabHost();
	    }
		return root;
	}

	private void setupTabHost() {
		try {
			tabHost.setup(getActivity(), getChildFragmentManager(),
					R.id.tabContent);
			tabHost.computeScroll();

			populateTabs();
		} catch (Exception e) {
		}
	}

	private void populateTabs() {
		try {
			for (int i = 0; i < categories.size(); i++) {
				Bundle bundle = new Bundle();
				bundle.putInt("ID", categories.get(i).getId());
				tabHost.addTab(
						tabHost.newTabSpec(categories.get(i).getCategory())
								.setIndicator(categories.get(i).getCategory()),
						EShopListFragment.class, bundle);

				TabWidget tw = (TabWidget) tabHost
						.findViewById(android.R.id.tabs);
				View tabView = tw.getChildTabViewAt(i);
				TextView tv = (TextView) tabView
						.findViewById(android.R.id.title);
				int dp = (int) (getResources().getDimension(R.dimen.textsize) / getResources()
						.getDisplayMetrics().density);

				tv.setTextSize(dp - 6);
			}
		} catch (Exception e) {
		}

	}
}
