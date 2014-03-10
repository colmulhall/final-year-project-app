package com.phoenixpark.app;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SubmitEvent extends Activity
{
	private String url = "http://parkdomain.comoj.com/android_get_users_submission.php";
	private TextView enterTitle, enterDesc, enterDate, enterLocation, enterCategory, enterContact_link;
	private EditText editTitle, editDesc, editDate, editLocation, editCategory, editContact_Link;
	private Button submit;
	
	// Creating connection handler class instance
	public HandleConnections sh = new HandleConnections();
	public String jsonStr;
	
	JSONArray events;
	JSONObject jObject;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_event_layout);
        
        // textviews
        enterTitle = (TextView)findViewById(R.id.enter_title);
        enterDesc = (TextView)findViewById(R.id.enter_desc);
        enterDate = (TextView)findViewById(R.id.enter_date);
        enterLocation = (TextView)findViewById(R.id.enter_location);
        enterCategory = (TextView)findViewById(R.id.enter_category);
        enterContact_link = (TextView)findViewById(R.id.enter_contactlink);
        
        // edittexts
        editTitle = (EditText)findViewById(R.id.title_enter);
        editDesc = (EditText)findViewById(R.id.desc_enter);
        editDate = (EditText)findViewById(R.id.date_enter);
        editLocation = (EditText)findViewById(R.id.location_enter);
        editCategory = (EditText)findViewById(R.id.category_enter);
        editContact_Link = (EditText)findViewById(R.id.contactlink_enter);
        
        // button
        submit = (Button)findViewById(R.id.submit_button);
        
        submit.setOnClickListener(new View.OnClickListener() 
        {
			public void onClick(View v) 
			{
				new UploadTask().execute();
			}
        });
    }
    
    private class UploadTask extends AsyncTask<String, Integer, String>
    {
    	private ProgressDialog progress; 
    	
    	@Override
	     protected void onPreExecute() 
	     {
			 super.onPreExecute();
	         // Showing progress dialog
	         progress = ProgressDialog.show(SubmitEvent.this, "Uploading Event", "Please Wait...");
	     }
    	
	    @Override
	    protected String doInBackground(String... params)
	    {
	    	//get data from the text boxes
	    	String title = editTitle.getText().toString();
	    	String desc = editDesc.getText().toString();
	    	String date = editDate.getText().toString();
	    	String location = editLocation.getText().toString();
	    	String category = editCategory.getText().toString();
	    	String contact_link = editContact_Link.getText().toString();
	    	
	        // Send the users entered parameters to the PHP script through POST
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("title", title));
			nameValuePairs.add(new BasicNameValuePair("description", desc));
			nameValuePairs.add(new BasicNameValuePair("date", date));
			nameValuePairs.add(new BasicNameValuePair("location", location));
			nameValuePairs.add(new BasicNameValuePair("category", category));
			nameValuePairs.add(new BasicNameValuePair("contact_link", contact_link));

			jsonStr = sh.makeServiceCall(url, HandleConnections.POST, nameValuePairs);
			
			return jsonStr;
	    }
	    
	    @Override
	    protected void onPostExecute(String result) 
	    {
	    	progress.dismiss();
	        // process the result
	        super.onPostExecute(result);
	        finish(); 

	        Toast.makeText(getApplicationContext(), "Event submitted", Toast.LENGTH_SHORT).show();
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