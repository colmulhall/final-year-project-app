package com.phoenixpark.app;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SubmitEvent extends Activity
{
	private String url = "http://parkdomain.comoj.com/android_get_users_submission.php";
	private TextView enterTitle, enterDesc, enterDate, enterLocation, enterCategory, enterContact_link;
	private EditText editTitle, editDesc, editLocation, editContact_Link;
	private DatePicker editDate;
	private Spinner categories;
	private Button submit;
	
	//dates for date picker
	private int year;
	private int month;
	private int day;

	
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
        
        // text prompts / button
        enterTitle = (TextView)findViewById(R.id.enter_title);
        enterDesc = (TextView)findViewById(R.id.enter_desc);
        enterDate = (TextView)findViewById(R.id.enter_date);
        enterLocation = (TextView)findViewById(R.id.enter_location);
        enterCategory = (TextView)findViewById(R.id.enter_category);
        enterContact_link = (TextView)findViewById(R.id.enter_contactlink);
        
        // edit fields
        editTitle = (EditText)findViewById(R.id.title_enter);
        
        editDesc = (EditText)findViewById(R.id.desc_enter);
        editDesc.setOnTouchListener(new OnTouchListener() 
        {
            public boolean onTouch(View view, MotionEvent event) 
            {
                if (view.getId() == R.id.desc_enter) 
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
        
        editDate = (DatePicker)findViewById(R.id.datepicker);
        editLocation = (EditText)findViewById(R.id.location_enter);
        editContact_Link = (EditText)findViewById(R.id.contactlink_enter);
        
        //set current date by default by the button
        setCurrentDateOnView();
        editDate.setMinDate(System.currentTimeMillis() - 1000);   //only allow future dates
        
        // category spinner
        categories = (Spinner)findViewById(R.id.category_spin);
        
        //populate the categories spinner with items in the string array from strings.xml
        String[] categories_array = getResources().getStringArray(R.array.categories);
        ArrayAdapter<String> category_adapter = 
		         new ArrayAdapter<String>
        			(this,android.R.layout.simple_dropdown_item_1line, categories_array);
        categories.setAdapter(category_adapter);
        
        //set listeners for buttons
        addListenerOnButtons();
    }
    
    // display current date
 	public void setCurrentDateOnView() 
 	{
 		editDate = (DatePicker) findViewById(R.id.datepicker);

 		final Calendar c = Calendar.getInstance();
 		year = c.get(Calendar.YEAR);
 		month = c.get(Calendar.MONTH);
 		day = c.get(Calendar.DAY_OF_MONTH);

 		// set current date into datepicker
 		editDate.init(year, month, day, null);
 	}
 	
 	// upload user event to database
 	class UploadTask extends AsyncTask<String, Integer, String>
    {
    	private ProgressDialog progress; 
    	
    	@Override
	     protected void onPreExecute() 
	     {
			 super.onPreExecute();
	         // Showing progress dialog
	         progress = ProgressDialog.show(SubmitEvent.this, "Uploading Event", "Please Wait...");
	     }
    	
	    @SuppressWarnings("deprecation")
		@Override
	    protected String doInBackground(String... params)
	    {
	    	//get data from the text boxes
	    	String title = editTitle.getText().toString();
	    	String desc = editDesc.getText().toString();
	    	String date = editDate.getYear()+"-"+editDate.getMonth()+"-"+editDate.getDayOfMonth();
	    	String location = editLocation.getText().toString();
	    	String category = String.valueOf(categories.getSelectedItem());
	    	String contact_link = editContact_Link.getText().toString();
	    	
	        // Send the users entered parameters to the PHP script through POST
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("title", title));
			nameValuePairs.add(new BasicNameValuePair("description", desc));
			nameValuePairs.add(new BasicNameValuePair("date", date));
			nameValuePairs.add(new BasicNameValuePair("location", location));
			nameValuePairs.add(new BasicNameValuePair("category", category));
			nameValuePairs.add(new BasicNameValuePair("contact_link", contact_link));
			
			/*//check if data has been entered
			if(title.equals(""))
			{
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() 
		        {
					//"Are you sure?" dialog options
		            @Override
		            public void onClick(DialogInterface dialog, int which) 
		            {
		                switch (which)
		                {
			                case DialogInterface.BUTTON_POSITIVE:
			                    //Yes button clicked
			    				finish();
			                    break;
			
			                default:
			                	break;
		                }
		            }
		        };

		        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
		        builder.setTitle("Missing attributes");
		        builder.setMessage("Please fill in all fields")
		        .setPositiveButton("Yes", dialogClickListener)
		        .show();
			}*/
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
    
    // add listeners to button
    public void addListenerOnButtons() 
    {
    	//submit button
		submit = (Button)findViewById(R.id.submit_button);
        submit.setOnClickListener(new View.OnClickListener() 
        {
			public void onClick(View v) 
			{
				new UploadTask().execute();
			}
        });
	}
    
    // back button pressed by user
    @Override
    public void onBackPressed() 
    {
        finish();//go back to the previous Activity
        overridePendingTransition(R.anim.slideup_in, R.anim.slideup_out);   
    }
}