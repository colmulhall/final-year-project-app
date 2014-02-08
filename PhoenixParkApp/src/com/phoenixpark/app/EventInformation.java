/* When the user clicks on an event they are 
 * brought to this screen to view more about it.
 */

package com.phoenixpark.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class EventInformation extends Activity
{
	 private String url = "http://10.0.2.2/FYP-Web-Coding/android_get_event.php";
	 TextView ev_title, ev_desc, ev_date;
	 Intent intent;
	 public String the_id;
	 
	 // JSON Node names
	 private static final String TAG_EVENTS = "event_list";
	 private static final String TAG_ID = "id";
	 private static final String TAG_TITLE = "title";
	 private static final String TAG_DATE = "date";

	 // Creating service handler class instance
	 public ServiceHandler sh = new ServiceHandler();
	 public String jsonStr;
	 
	 // events JSONArray
	 JSONArray events = null;
	 
	 // Hashmap for ListView
	 ArrayList<HashMap<String, String>> eventList;
	 
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.eventinformation_layout);
	    
	     ev_title = (TextView)findViewById(R.id.event_title);
		 ev_desc = (TextView)findViewById(R.id.event_description);
		 ev_date = (TextView)findViewById(R.id.event_date);
		 
		 //get information from the last activity
		 intent = getIntent();
		 the_id = intent.getExtras().getString("id");

	    new UploadTask().execute();
	}
	
	private class UploadTask extends AsyncTask<String, Integer, String> 
    {
	    @Override
	    protected String doInBackground(String... params) 
	    {
	        // Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("id", the_id));

			return jsonStr = sh.makeServiceCall(url, ServiceHandler.POST, nameValuePairs);
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
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    String the_title = null;
                     
                    // Getting JSON Array node
                    events = jsonObj.getJSONArray(TAG_EVENTS);
 
                    // looping through All Events
                    for (int i = 0; i < events.length(); i++) 
                    {
                        JSONObject c = events.getJSONObject(i);
                         
                        the_title = c.getString(TAG_TITLE);
                        Log.i("EVENT", the_title);
                        
                        String the_id = c.getString(TAG_ID);
                        String the_date = c.getString(TAG_DATE);
                    }
                    ev_title.setText(the_title);
                    
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
 
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) 
        {
            super.onPostExecute(result);
            
            // Dismiss the progress dialog
            progress.dismiss();
        }
    }
}