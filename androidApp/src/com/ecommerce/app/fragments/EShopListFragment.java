package com.ecommerce.app.fragments;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ecommerce.app.R;
import com.ecommerce.app.EShopDetailActivity;
import com.ecommerce.app.adapters.EShopListAdapter;
import com.ecommerce.app.models.Product;
import com.ecommerce.app.models.ProductCategory;
import com.ecommerce.app.models.ProductResponse;
import com.ecommerce.app.utils.Constants;
import com.ecommerce.app.utils.SQLiteHelper;
import com.google.gson.Gson;

public class EShopListFragment extends Fragment {

	private View view;

	private ListView listview;
	private EShopListAdapter adapter;
	private ArrayList<Product> productList = new ArrayList<Product>();
	private SharedPreferences pref;
	private Context context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		context = getActivity();
	
		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);

		} else {
			view = inflater.inflate(R.layout.fragment_eshop_list, container,
					false);
			listview = (ListView) view.findViewById(R.id.lv_eshop);

		}

		try {
			Bundle bundle = getArguments();
			if (bundle.containsKey("ID")) {
				int id = bundle.getInt("ID");
				loadListView(id);
				Log.i("ID", id + "");
			}
		} catch (Exception e) {
		}

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Product product = (Product) parent.getItemAtPosition(position);

				Intent intent = new Intent(context, EShopDetailActivity.class);
				intent.putExtra("product", product);
				startActivity(intent);
			}
		});

		return view;
	}

	private void loadListView(int id) {
		pref=context.getSharedPreferences(Constants.PREF_NAME, Constants.PRIVATE_MODE);
		Gson gson = new Gson();
	    String json = pref.getString("productResponse", "");
	    ProductResponse data = gson.fromJson(json, ProductResponse.class);
	    
	    List<ProductCategory> categoryList = new ArrayList<ProductCategory>();
		categoryList = data.getData();

		
			
			productList = new ArrayList<Product>();

			productList = (ArrayList<Product>) categoryList.get(id).getProducts();


		adapter = new EShopListAdapter(context, R.layout.row_eshop, productList);

		listview.setAdapter(adapter);

		Log.i("", productList.size() + "");

	}
}
