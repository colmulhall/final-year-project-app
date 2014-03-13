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

public class UserSubmittedInformation extends Activity
{
	 private String url = "http://parkdomain.comoj.com/android_get_user_submitted_item.php";
	 TextView ev_title, ev_desc, ev_date;
	 ImageView image;
	 Intent intent;
	 public String the_id;
	 public String the_title, the_desc, the_date, the_location, the_category, the_link;
	 private LocalDbManager db;
	 private ShareActionProvider myShareActionProvider;
	 
	 // Tag to send location to maps and/or feedback actiivties
	 private static final String TAG_LOC = "loc";
	 private static final String TAG_ID = "id";
	 
	 // JSON Node names
	 private static final String TAG_EVENTS = "user_events";
	 private static final String TAG_TITLE = "title";
	 private static final String TAG_DESC = "description";
	 private static final String TAG_LOCATION = "location";
	 private static final String TAG_CATEGORY = "category";
	 private static final String TAG_DATE = "date";

	 // Creating service handler class instance
	 public HandleConnections sh = new HandleConnections();
	 public String jsonStr;
	 
	 // events JSONArray
	 JSONArray events;
	 
	 JSONObject jObject;
	 
	 // Hashmap for ListView
	 ArrayList<HashMap<String, String>> eventList;
	 
	public void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.submitted_event_item);
	    
	     ev_title = (TextView)findViewById(R.id.event_title);
		 ev_desc = (TextView)findViewById(R.id.event_description);
		 image = (ImageView) findViewById(R.id.image);
		 
		 //get ID from the last activity
		 intent = getIntent();
		 the_id = intent.getExtras().getString("id");
		 
		 //Open favorites database to write
	     db = new LocalDbManager(this);
	     db.openFavsToWrite();

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
			jsonStr = sh.makeServiceCall(url, HandleConnections.POST, nameValuePairs);

			return jsonStr;
	    }
	    
	    @Override
	    protected void onPostExecute(String result) 
	    {
	        // process the result
	        super.onPostExecute(result);
	        
	        // Calling async task to get json
	        new GetEvent().execute();
	    }
    }
 
    //Async task class to get json by making HTTP call
    private class GetEvent extends AsyncTask<Void, Void, Void>
    {
    	private ProgressDialog progress;  //progress dialog when loading events
    	
        @Override
        protected void onPreExecute() 
        {
            super.onPreExecute();
            // Showing progress dialog
            progress = ProgressDialog.show(UserSubmittedInformation.this, "Getting event", "Please Wait...");
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
                    events = jsonObj.getJSONArray(TAG_EVENTS);
                    
                    jObject = events.getJSONObject(0);
                         
                    the_title = jObject.getString(TAG_TITLE);
                    the_desc = jObject.getString(TAG_DESC);
                    the_location = jObject.getString(TAG_LOCATION);
                    the_date = jObject.getString(TAG_DATE);
                    
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else 
            {
                Log.e("Connection Handler:", "Couldn't get any data from the url");
            }
           
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) 
        {
        	ev_title.setText(the_title);
        	ev_desc.setText(the_desc);
        	
        	//set image to wherever location the event is taking place	
        	if(the_location.equals("Farmleigh"))
        		image.setImageResource(R.drawable.farmleigh_house_item);
        	else if(the_location.equals("Phoenix Park"))
        		image.setImageResource(R.drawable.phoenixpark_item);
        	else if(the_location.equals("Visitor Centre"))
        		image.setImageResource(R.drawable.visitor_centre_item);
        	else
        		image.setImageResource(R.drawable.zoo_item);
            
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
        getMenuInflater().inflate(R.menu.newsinformation_menu, menu);  
        
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
        intent.putExtra(Intent.EXTRA_TEXT,"Phoenix Park event: " + the_title);
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

			    db.insertFav(title, desc, the_date, the_location, the_link);
				    
				Toast.makeText(getApplicationContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
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
    	else if(id == R.id.map_directions)
    	{
    		Intent in = new Intent(getApplicationContext(), DirectionsTo.class);
    		in.putExtra(TAG_LOC, the_location);
            startActivity(in);
	    	overridePendingTransition(R.anim.slidedown_in, R.anim.slidedown_out);
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