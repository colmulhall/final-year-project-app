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

public class EventInformation extends Activity
{
	 private String url = "http://parkdomain.comoj.com/android_get_event_item.php";
	 TextView ev_title, ev_desc, ev_date;
	 ImageView image;
	 Intent intent;
	 public String the_id;
	 public String set_title, set_desc, set_date, the_location, the_link;
	 private LocalDbManager db;
	 private ShareActionProvider myShareActionProvider;
	 
	 // Tag to send location to maps and/or feedback actiivties
	 private static final String TAG_LOC = "loc";
	 private static final String TAG_ID = "id";
	 
	 // JSON Node names
	 private static final String TAG_EVENTS = "event_list";
	 private static final String TAG_TITLE = "title";
	 private static final String TAG_DESC = "description";
	 private static final String TAG_LOCATION = "location";
	 private static final String TAG_LINK = "link";

	 // Creating service handler class instance
	 public HandleConnections sh = new HandleConnections();
	 public String jsonStr;
	 
	 // Sharing intent
	 public Intent shareintent = new Intent(Intent.ACTION_SEND);
	 
	 // events JSONArray
	 JSONArray events;
	 
	 JSONObject jObject;
	 
	 // Hashmap for ListView
	 ArrayList<HashMap<String, String>> eventList;
	 
	public void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.eventinformation_layout);
	    
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
            progress = ProgressDialog.show(EventInformation.this, "Getting event", "Please Wait...");
        }
 
        @Override
        protected Void doInBackground(Void... arg0)
        {
            if (jsonStr != null) 
            {
                try 
                {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                     
                    // Getting JSON Array node
                    events = jsonObj.getJSONArray(TAG_EVENTS);
                    
                    jObject = events.getJSONObject(0);
                         
                    set_title = jObject.getString(TAG_TITLE);
                    set_desc = jObject.getString(TAG_DESC);
                    the_location = jObject.getString(TAG_LOCATION);
                    the_link = jObject.getString(TAG_LINK);
                    
                } catch (JSONException e) {
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
        	ev_title.setText(set_title);
        	ev_desc.setText(set_desc);
        	
        	//set image to wherever location the event is taking place	
        	if(the_location.equals("Farmleigh"))
        		image.setImageResource(R.drawable.farmleigh_house_item);
        	else if(the_location.equals("Phoenix Park"))
        		image.setImageResource(R.drawable.phoenixpark_item);
        	else if(the_location.equals("Visitor Centre"))
        		image.setImageResource(R.drawable.visitor_centre_item);
        	else if(the_location.equals("Dublin Zoo"))
        		image.setImageResource(R.drawable.zoo_item);
        	else if(the_location.equals("Sports Grounds"))
        		image.setImageResource(R.drawable.sportsgrounds_item);
        	else if(the_location.equals("Magazine Fort"))
        		image.setImageResource(R.drawable.magazine_static_1);
        	else if(the_location.equals("Papal Cross"))
        		image.setImageResource(R.drawable.papal_static_2);
        	else if(the_location.equals("Wellington Monument"))
        		image.setImageResource(R.drawable.wellington_static_1);
        	else if(the_location.equals("Peoples Garden"))
        		image.setImageResource(R.drawable.peoples_garden_static_1);
        	else if(the_location.equals("Ashtown Castle"))
        		image.setImageResource(R.drawable.ashtown_static_1);
        	else
        		image.setImageResource(R.drawable.deerfield_static_1);
            
            super.onPostExecute(result);
            
            // set the message for sharing now that the link has been retrieved from the database
            shareintent.putExtra(Intent.EXTRA_TEXT,"Phoenix Park event: " + the_link);
            
            // Dismiss the progress dialog
            progress.dismiss();
        }
    }
    
    // action bar
    @SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
    	// Inflate menu resource file.  
        getMenuInflater().inflate(R.menu.eventinformation_menu, menu);  
        
        //Getting the actionprovider associated with the menu item whose id is share
        myShareActionProvider = (ShareActionProvider) menu.findItem(R.id.share).getActionProvider();
 
        //Setting a share intent
        myShareActionProvider.setShareIntent(getDefaultShareIntent());

        return true;
	}
    
    // returns share intent
    private Intent getDefaultShareIntent()
    {
        
    	shareintent.setType("text/plain");
    	shareintent.putExtra(Intent.EXTRA_SUBJECT, "Event in the Phoenix Park");
    	
        return shareintent;
    }
    
    // action bar listener
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

			    db.insertFav(title, desc, "24-12-23", "Farmleigh", the_link);
				    
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
    	else if(id == R.id.event_feedback)
    	{
    		Intent in = new Intent(getApplicationContext(), EventFeedback.class);
    		in.putExtra(TAG_ID, the_id);
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
        overridePendingTransition(R.anim.slide_in_right_to_left, R.anim.slide_out_right_to_left);   
    }
    
    //life cycles
    @Override
    protected void onPause()
    {
	    super.onPause();
    }
    
    @Override
    protected void onDestroy()
    {
    	super.onDestroy();
    	db.close();
    }
    
	@Override
	protected void onResume()
	{
		super.onResume();
	}
}