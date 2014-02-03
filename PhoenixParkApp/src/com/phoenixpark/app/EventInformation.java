/* When the user clicks on an event they are 
 * brought to this screen to view more about it.
 */

package com.phoenixpark.app;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class EventInformation extends Activity
{
	 private String url = "http://10.0.2.2/FYP-Web-Coding/android_get_event.php";
	 TextView ev_title, ev_desc;
	 Intent intent;
	 public String the_id;
	 
	 @Override
	 protected void onCreate(Bundle savedInstanceState) 
	 {
	     super.onCreate(savedInstanceState);
	     setContentView(R.layout.eventinformation_layout);
	     
	     ev_title = (TextView)findViewById(R.id.event_title);
		 ev_desc = (TextView)findViewById(R.id.event_description);
		 
		 //get the event ID from the last activity
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
	         HttpClient httpclient = new DefaultHttpClient();
	         HttpPost httppost = new HttpPost(url);
	         
		      try 
		      {
		    	 httppost.setEntity(new StringEntity("your string"));    
		    	 HttpResponse resp = httpclient.execute(httppost);
		    	 resp.getEntity();
		    	 postData("erfer");
		      }
		      catch (ClientProtocolException e) {
		          e.printStackTrace();
		      } catch (IOException e) {
		          e.printStackTrace();
		      }
		      return null;
	     }
	     
	     public void postData(String send_this) throws ClientProtocolException, IOException 
	     {
				// Create a new HttpClient and Post Header
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(url);
	 
				try {
					// Add your data
					ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("action", send_this));
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					
					// Execute HTTP Post Request
					HttpResponse response = httpclient.execute(httppost);
					HttpEntity httpEntity = response.getEntity();
	 
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}
		 }
		  @Override
		  protected void onPostExecute(String result)
		  {
		      //ListDrawer();
		  }
	 }// end async task
	 
	 public void accessWebService() 
	 {
		  JsonReadTask task = new JsonReadTask();
		  // passes values for the urls string array
		  task.execute(new String[] { url });
	 }
}