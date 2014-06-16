package com.ecommerce.app;



import com.google.gson.Gson;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class AuthenticateActivity extends Activity{
	
	String url;
	
	Dialog dialog;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);  
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//url=getIntent().getStringExtra("img");
		//new ServerRead().execute("");
		 dialog = new Dialog(this);
		dialog.getWindow();
	    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
		dialog.setContentView(R.layout.push_image);
		

		// set the custom dialog components - text, image and button
		
		 
		
		 dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

	            @Override
	            public void onCancel(DialogInterface dialog) {
	               finish();
	            }
	        });

	        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

	            @Override
	            public void onDismiss(DialogInterface dialog) {
	                // TODO Auto-generated method stub
	            	finish();
	            }
	        });
		
		// set the custom dialog components - text, image and button
		//splash.setImageBitmap(splashbitmap);
		
	        setView();
		

		
	
}
	
	public void setView(){
		
		dialog.show();
	}
	private class ServerRead extends AsyncTask<String, Void, String> {
        
		@Override
        protected String doInBackground(String... params) {
			
		
          
            return null;
        }
       
        @Override
        protected void onPostExecute(String result) {
        	runOnUiThread(new Runnable() {
       	     @Override
       	     public void run() {
       	    	 	
       	    }
       	});
        	
    	      
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        	
        }

  }


}