package com.phoenixpark.app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EventFeedback extends Activity
{
	private String urlUpload = "http://parkdomain.comoj.com/android_users_event_feedback.php";
	private String urlGetInfo = "http://parkdomain.comoj.com/android_get_event_item.php";
	private TextView enterComment, enterRating;
	private EditText editComment;
	private Spinner ratings, categories;
	private Button submit;
	private Intent intent;
	private String the_id;
	
	// Variables to hold the event information
	String title, desc, location, date, comment, star_rating, category;
	
	// Creating connection handler class instance
	public HandleConnections sh = new HandleConnections();
	public String jsonStr;
	
	// JSON Node names
	private static final String TAG_EVENTS = "event_list";
	private static final String TAG_TITLE = "title";
	private static final String TAG_DESC = "description";
	private static final String TAG_LOCATION = "location";
	
	JSONArray events;
	JSONObject jObject;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventfeedback_layout);
        
        // get ID from the last activity
		intent = getIntent();
		the_id = intent.getExtras().getString("id");
		 
        // textviews
        enterComment = (TextView)findViewById(R.id.enter_comment);
        enterRating = (TextView)findViewById(R.id.enter_rating);
        
        // edittext
        editComment = (EditText)findViewById(R.id.comment_enter);
        editComment.setOnTouchListener(new OnTouchListener() 
        {
            public boolean onTouch(View view, MotionEvent event) 
            {
                // TODO Auto-generated method stub
                if (view.getId() == R.id.enter_comment) 
                {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction()&MotionEvent.ACTION_MASK)
                    {
	                    case MotionEvent.ACTION_UP:
	                        view.getParent().requestDisallowInterceptTouchEvent(false);
	                        break;
                    }
                }
                return false;
            }
        });
        
        // spinners
        ratings = (Spinner)findViewById(R.id.rating_spin);
        categories = (Spinner)findViewById(R.id.category_spin);
        
        //populate the ratings spinner with items in the string array from strings.xml
        String[] ratings_array = getResources().getStringArray(R.array.ratings);
        ArrayAdapter<String> rating_adapter = 
		         new ArrayAdapter<String>
        			(this,android.R.layout.simple_dropdown_item_1line, ratings_array);
        ratings.setAdapter(rating_adapter);
        
        //populate the categories spinner with items in the string array from strings.xml
        String[] categories_array = getResources().getStringArray(R.array.categories);
        ArrayAdapter<String> category_adapter = 
		         new ArrayAdapter<String>
        			(this,android.R.layout.simple_dropdown_item_1line, categories_array);
        categories.setAdapter(category_adapter);
        
        // button
        submit = (Button)findViewById(R.id.submit_button);
        
        submit.setOnClickListener(new View.OnClickListener() 
        {
			public void onClick(View v) 
			{
				new UploadFeedback().execute();   //
			}
        });
        
     // run the get event method
        new UploadTask().execute();
    }
    
    
    // get information on the event in question *************************************************************
    private class UploadTask extends AsyncTask<String, Integer, String>
    {
	    @Override
	    protected String doInBackground(String... params)
	    {
	        // Add and send the ID to the PHP file
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("id", the_id));
			jsonStr = sh.makeServiceCall(urlGetInfo, HandleConnections.POST, nameValuePairs);

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
    	
        @Override
        protected void onPreExecute() 
        {
            super.onPreExecute();
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
                         
                    // get information on the event in question from the event_list database
                    title = jObject.getString(TAG_TITLE);
                    desc = jObject.getString(TAG_DESC);
                    location = jObject.getString(TAG_LOCATION);
                    
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
        }
    }
    
    //upload feedback to the database ***********************************************************************
    private class UploadFeedback extends AsyncTask<String, Integer, String>
    {
    	private ProgressDialog progress; 
    	
    	@Override
	     protected void onPreExecute() 
	     {
			 super.onPreExecute();
	         // Showing progress dialog
	         progress = ProgressDialog.show(EventFeedback.this, "Giving feedback", "Please Wait...");
	     }
    	
	    @SuppressWarnings("deprecation")
		@Override
	    protected String doInBackground(String... params)
	    {
	    	// get current system date
	    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	    	Date newdate = new Date();
	    	
	    	// get the rating from the user
	    	String rate = String.valueOf(ratings.getSelectedItem());
	    	int user_rating;
	    	
	    	// set rating based on the spinner
	    	if(rate.equals("5 Stars"))
	    		user_rating = 5;
	    	else if(rate.equals("4 Stars"))
	    		user_rating = 4;
	    	else if(rate.equals("3 Stars"))
	    		user_rating = 3;
	    	else if(rate.equals("2 Stars"))
	    		user_rating = 2;
	    	else if(rate.equals("1 Stars"))
	    		user_rating = 1;
	    	else
	    		user_rating = 0;
	    	
	    	//get the rest of the data about the event (already have title, desc and location from previous task)
	    	date = dateFormat.format(newdate);
	    	comment = editComment.getText().toString();
	    	star_rating = ""+user_rating;
	    	category = String.valueOf(categories.getSelectedItem());
	    	
	        // Send the users entered parameters to the PHP script through POST
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("title", title));
			nameValuePairs.add(new BasicNameValuePair("description", desc));
			nameValuePairs.add(new BasicNameValuePair("date", date));
			nameValuePairs.add(new BasicNameValuePair("location", location));
			nameValuePairs.add(new BasicNameValuePair("comment", comment));
			nameValuePairs.add(new BasicNameValuePair("rating", star_rating));
			nameValuePairs.add(new BasicNameValuePair("category", category));
			
			jsonStr = sh.makeServiceCall(urlUpload, HandleConnections.POST, nameValuePairs);
			return jsonStr;
	    }
	    
	    @Override
	    protected void onPostExecute(String result) 
	    {
	    	progress.dismiss();
	        // process the result
	        super.onPostExecute(result);
	        finish(); 

	        Toast.makeText(getApplicationContext(), "Feedback submitted", Toast.LENGTH_SHORT).show();
	    }
    }
    
    // back button pressed by user
    @Override
    public void onBackPressed() 
    {
        finish();//go back to the previous Activity
        overridePendingTransition(R.anim.slideup_in, R.anim.slideup_out);   
    }
}