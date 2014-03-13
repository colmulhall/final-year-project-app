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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Wunderground extends Activity
{
	 private String url = "http://parkdomain.comoj.com/wunderground.php";
	 TextView weather_tv;
	 
	 // JSON Node names
	 private static final String TAG_OBSERVE = "response";
	 private static final String TAG_TITLE = "city";

	 // Creating service handler class instance
	 public HandleConnections sh = new HandleConnections();
	 public String jsonStr;
	 
	 // events JSONArray
	 JSONArray events;
	 
	 JSONObject jObject;
	 
	 String set;
	 
	public void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.wunderground_layout);
	    
	     weather_tv = (TextView)findViewById(R.id.weather_title);
		 
		 new GetWeather().execute();
	}
 
    //Async task class to get json by making HTTP call
    private class GetWeather extends AsyncTask<Void, Void, Void>
    {
    	private ProgressDialog progress;  //progress dialog when loading events
    	
        @Override
        protected void onPreExecute() 
        {
            super.onPreExecute();
            // Showing progress dialog
            progress = ProgressDialog.show(Wunderground.this, "Getting weather", "Please Wait...");
        }
 
        @Override
        protected Void doInBackground(Void... arg0)
        {
        	HandleConnections sh = new HandleConnections();
        	 
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, HandleConnections.GET);
            
            if (jsonStr != null) 
            {
            	Log.d("Weather Response: ", "> " + jsonStr);
                try 
                {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                     
                    // Getting JSON Array node
                    events = jsonObj.getJSONArray(TAG_OBSERVE);
                    
                    jObject = events.getJSONObject(0);
                         
                    set = jObject.getString(TAG_TITLE);
                    Log.i("WHERE?", jObject.getString(TAG_TITLE));
                    
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
            super.onPostExecute(result);
            
            weather_tv.setText(set);
            
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