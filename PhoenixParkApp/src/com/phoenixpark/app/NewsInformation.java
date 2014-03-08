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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;


public class NewsInformation extends Activity
{
	 private String url = "http://parkdomain.comoj.com/android_get_news_item.php";
	 TextView news_title, news_desc, news_date;
	 ImageView image;
	 Intent intent;
	 public String the_id;
	 public String set_title, set_desc, set_date, the_location, the_link;
	 private LocalDbManager db;
	 private ShareActionProvider myShareActionProvider;
	 
	 // JSON Node names
	 private static final String TAG_NEWS = "news_updates";
	 private static final String TAG_TITLE = "title";
	 private static final String TAG_DESC = "description";
	 private static final String TAG_LOCATION = "location";
	 private static final String TAG_LINK = "link";

	 // Creating service handler class instance
	 public ServiceHandler sh = new ServiceHandler();
	 public String jsonStr;
	 
	 // events JSONArray
	 JSONArray news;
	 
	 JSONObject jObject;
	 
	 // Hashmap for ListView
	 ArrayList<HashMap<String, String>> newsList;
	 
	public void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.newsinformation_layout);
	    
	     news_title = (TextView)findViewById(R.id.news_title);
		 news_desc = (TextView)findViewById(R.id.news_description);
		 image = (ImageView) findViewById(R.id.image);
		 
		 //get ID from the last activity
		 intent = getIntent();
		 the_id = intent.getExtras().getString("id");
		 Log.i("ID:", the_id);
		 
		 //Open favorites database to write
	     db = new LocalDbManager(this);
	     db.openToWrite();

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
                    
                    jObject = news.getJSONObject(0);
                         
                    set_title = jObject.getString(TAG_TITLE);
                    set_desc = jObject.getString(TAG_DESC);
                    the_location = jObject.getString(TAG_LOCATION);
                    the_link = jObject.getString(TAG_LINK);
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
        	
        	//set image to wherever location the news item is referencing
        	if(the_location.equals("Farmleigh"))
        		image.setImageResource(R.drawable.farmleigh_house_item);
        	else if(the_location.equals("Visitor Centre"))
        		image.setImageResource(R.drawable.visitor_centre_item);
        	else
        		image.setImageResource(R.drawable.events_icon);
            
            super.onPostExecute(result);
            
            // Dismiss the progress dialog
            progress.dismiss();
        }
    }
    
    //action bar
    @SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
    	// Inflate menu resource file.  
        getMenuInflater().inflate(R.menu.information_menu, menu);  
        
        //Getting the actionprovider associated with the menu item whose id is share
        myShareActionProvider = (ShareActionProvider) menu.findItem(R.id.share).getActionProvider();
 
        //Setting a share intent
        myShareActionProvider.setShareIntent(getDefaultShareIntent());

        return true;
	}
    
    //returns share intent
    private Intent getDefaultShareIntent()
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "SUBJECT");
        intent.putExtra(Intent.EXTRA_TEXT,"Phoenix Park news: " + the_link);
        return intent;
    }
    
    //action bar listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	int id = item.getItemId();
    	
    	if(id == R.id.fav_action)
    	{
    		String title, desc;
			try 
			{
				title = jObject.getString(TAG_TITLE);
				desc = jObject.getString(TAG_DESC);

			    db.insert(title, desc, "24-12-23", "Farmleigh", the_link);
				    
				Toast.makeText(getApplicationContext(), "Added to favorites", Toast.LENGTH_LONG).show();
			} 
			catch (JSONException e) {
				e.printStackTrace();
			}
    	}
    	else if(id == R.id.link_page)
    	{
    		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(the_link));
	    	  startActivity(browserIntent);
    	}
        return true;
    }
    
    //back button pressed by user
    @Override
    public void onBackPressed() 
    {
        finish();//go back to the previous Activity
        overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_left_to_right);   
    }
}