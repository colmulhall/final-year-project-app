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


public class EventInformation extends Activity
{
	 private String url = "http://10.0.2.2/FYP-Web-Coding/android_get_event_item.php";
	 TextView ev_title, ev_desc, ev_date;
	 Intent intent;
	 public String the_id;
	 public String set_title, set_desc, set_date;
	 
	 // JSON Node names
	 private static final String TAG_EVENTS = "event_list";
	 private static final String TAG_TITLE = "title";
	 private static final String TAG_DESC = "description";

	 // Creating service handler class instance
	 public ServiceHandler sh = new ServiceHandler();
	 public String jsonStr;
	 
	 // events JSONArray
	 JSONArray events;
	 
	 // Hashmap for ListView
	 ArrayList<HashMap<String, String>> eventList;
	 
	public void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.eventinformation_layout);
	    
	     ev_title = (TextView)findViewById(R.id.event_title);
		 ev_desc = (TextView)findViewById(R.id.event_description);
		 
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
            progress = ProgressDialog.show(EventInformation.this, "Getting event", "Please Wait...");
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
                    
                    JSONObject c = events.getJSONObject(0);
                         
                    set_title = c.getString(TAG_TITLE);
                    set_desc = c.getString(TAG_DESC);
                    
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
        overridePendingTransition(R.anim.slide_in_right_to_left, R.anim.slide_out_right_to_left);   
    }
}