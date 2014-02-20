/* When the user clicks on an event they are 
 * brought to this screen to view more about it.
 */

package com.phoenixpark.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


public class NewsInformation extends Activity
{
	 private String url = "http://10.0.2.2/FYP-Web-Coding/android_get_news_item.php";
	 TextView news_title, news_desc, news_date;
	 Intent intent;
	 public String the_id;
	 public String set_title, set_desc, set_date;
	 
	 // JSON Node names
	 private static final String TAG_NEWS = "news_updates";
	 private static final String TAG_TITLE = "title";
	 private static final String TAG_DESC = "description";

	 // Creating service handler class instance
	 public ServiceHandler sh = new ServiceHandler();
	 public String jsonStr;
	 
	 // events JSONArray
	 JSONArray news;
	 
	 // Hashmap for ListView
	 ArrayList<HashMap<String, String>> newsList;
	 
	public void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.newsinformation_layout);
	    
	     news_title = (TextView)findViewById(R.id.news_title);
		 news_desc = (TextView)findViewById(R.id.news_description);
		 
		 //get ID from the last activity
		 intent = getIntent();
		 the_id = intent.getExtras().getString("id");
		 Log.i("ID:", the_id);

	    new UploadTask().execute();
	}
	
	private class UploadTask extends AsyncTask<String, Integer, String>
    {
	    @Override
	    protected String doInBackground(String... params)
	    {
	        // Add and send the ID to the PHP file
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("id", the_id));
			jsonStr = sh.makeServiceCall(url, ServiceHandler.POST, nameValuePairs);

			return jsonStr;
	    }
	    
	    @Override
	    protected void onPostExecute(String result) 
	    {
	        // process the result
	        super.onPostExecute(result);
	        
	        // Calling async task to get json
	        new GetEvents().execute();
	    }
    }
 
    //Async task class to get json by making HTTP call
    private class GetEvents extends AsyncTask<Void, Void, Void>
    {
    	private ProgressDialog progress;  //progress dialog when loading events
    	
        @Override
        protected void onPreExecute() 
        {
            super.onPreExecute();
            // Showing progress dialog
            progress = ProgressDialog.show(NewsInformation.this, "Getting the news item", "Please Wait...");
        }
 
        @Override
        protected Void doInBackground(Void... arg0)
        {
        	Log.d("Response: ", "> " + jsonStr);
            if (jsonStr != null) 
            {
                try 
                {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                     
                    // Getting JSON Array node
                    news = jsonObj.getJSONArray(TAG_NEWS);
                    
                    JSONObject c = news.getJSONObject(0);
                         
                    set_title = c.getString(TAG_TITLE);
                    set_desc = c.getString(TAG_DESC);
                    Log.i("NEWS", set_desc);
                    
                } 
                catch (JSONException e) 
                {
                    e.printStackTrace();
                }
            }
            else 
            {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
           
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) 
        {
        	news_title.setText(set_title);
        	news_desc.setText(set_desc);
            
            super.onPostExecute(result);
            
            // Dismiss the progress dialog
            progress.dismiss();
        }
    }
    
    //back button pressed by user
    @Override
    public void onBackPressed() 
    {
        finish();//go back to the previous Activity
        overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_left_to_right);   
    }
}