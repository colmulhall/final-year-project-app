/* When the user clicks on an event they are 
 * brought to this screen to view more about it.
 */

package com.phoenixpark.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class EventInformation extends Activity
{
	 private String url = "http://10.0.2.2/FYP-Web-Coding/android_get_event.php";
	 TextView ev_title, ev_desc;
	 Intent intent;
	 public String the_title, the_desc;
	 public HttpClient httpclient;
	 public HttpPost httppost;
	 StringBuilder sb1;
	 
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.eventinformation_layout);
	    
	     ev_title = (TextView)findViewById(R.id.event_title);
		 ev_desc = (TextView)findViewById(R.id.event_description);
		 
		 //get information from the last activity
		 intent = getIntent();
		 the_title = intent.getExtras().getString("title");
		 ev_title.setText(the_title);
		 the_desc = intent.getExtras().getString("desc");
		 ev_desc.setText(the_desc);
	    
	    String text = "Hello!";
	    new UploadTask().execute(text);
	}
	
	    private class UploadTask extends AsyncTask<String, Integer, String> 
	    {
		    private ProgressDialog progressDialog;
		    @Override
		    protected void onPreExecute() 
		    {
		        progressDialog = new ProgressDialog(EventInformation.this);
		        progressDialog.setMessage("Uploading...");
		        progressDialog.setCancelable(false);
		        progressDialog.setIndeterminate(true);
		        progressDialog.show();
		        super.onPreExecute();
		    }
		
		    @Override
		    protected String doInBackground(String... params) 
		    {
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost(url);
		
		        try {
		            // Add your data
		            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		            nameValuePairs.add(new BasicNameValuePair("id", "23"));
		
		            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		
		            // Execute HTTP Post Request
		            HttpResponse response = httpclient.execute(httppost);
		            HttpEntity resEntity = response.getEntity();

		            if (resEntity != null)
		            {
		                String responseStr = EntityUtils.toString(resEntity).trim();
		                Log.v("TAG", "Response: " +  responseStr);
		                return responseStr;
		            }
		            
		        } catch (ClientProtocolException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
	
	        return null;
	    }
	
	    @Override
	    protected void onPostExecute(String result) 
	    {
	        if (progressDialog != null) {
	            progressDialog.dismiss();
	        }
	        // process the result
	        super.onPostExecute(result);
	    }
	}
}




/*public class EventInformation extends Activity
{
	 private String url = "http://10.0.2.2/FYP-Web-Coding/android_get_event.php";
	 TextView ev_title, ev_desc;
	 Intent intent;
	 public String the_id;
	 public HttpClient httpclient;
	 public HttpPost httppost;
	 StringBuilder sb1;
	 
	 @Override
	 protected void onCreate(Bundle savedInstanceState) 
	 {
	     super.onCreate(savedInstanceState);
	     setContentView(R.layout.eventinformation_layout);
	     
	     ev_title = (TextView)findViewById(R.id.event_title);
		 ev_desc = (TextView)findViewById(R.id.event_description);
		 
		 //get information from the last activity
		 intent = getIntent();
		 the_id = intent.getExtras().getString("id");
		 ev_title.setText(the_id);
		 
	     accessWebService();
	 }

	 // Async Task to access the web
	 private class JsonReadTask extends AsyncTask<String, Void, String> 
	 {
	     @Override
	     protected String doInBackground(String... params) 
	     {
	         httpclient = new DefaultHttpClient();
	         httppost = new HttpPost(url);
	         try 
	    	 {
	        	 // Add your data
	             List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	             nameValuePairs.add(new BasicNameValuePair("id", params[0]));
	             httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

	             // Execute HTTP Post Request
	             HttpResponse response = httpclient.execute(httppost);
	             if (response != null) {
	                 InputStream in = response.getEntity().getContent();
	                 String responseContent = inputStreamToString(in);

	                 return responseContent;
	             }
	    	 }
	    	 catch (ClientProtocolException e) 
	    	 {
	    	  	Toast.makeText(getApplicationContext(), e.toString() , Toast.LENGTH_LONG).show();
	    	 } 
	    	 catch (IOException e) 
	    	 {
	    	    Toast.makeText(getApplicationContext(), e.toString() , Toast.LENGTH_LONG).show();
	    	 }
		     return null;
	     }
	     
		 @Override
		 protected void onPostExecute(String result)
		 {
			 ev_title.setText(sb1.toString());
		 }
		 
		 private String inputStreamToString(InputStream is) throws IOException 
		 {
			 String line = "";
		     StringBuilder total = new StringBuilder();

		     // Wrap a BufferedReader around the InputStream
		     BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		     // Read response until the end
		     while ((line = rd.readLine()) != null) 
		     {
		    	 total.append(line); 
		     }

		     // Return full string
		     return total.toString();
		  }
	 }
	 
	 public void accessWebService() 
	 {
		  JsonReadTask task = new JsonReadTask();
		  // passes values for the urls string array
		  task.execute(new String[] { url });
	 }
}*/