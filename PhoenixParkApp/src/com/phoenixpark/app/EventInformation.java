/* When the user clicks on an event they are 
 * brought to this screen to view more about it.
 */

package com.phoenixpark.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class EventInformation extends Activity
{
	 private String jsonResult;
	 private String url = "http://10.0.2.2/FYP-Web-Coding/android_get_titles.php";
	 TextView ev_title, ev_desc;
	 Intent intent;
	 public String the_title;
	 
	 @Override
	 protected void onCreate(Bundle savedInstanceState) 
	 {
	     super.onCreate(savedInstanceState);
	     setContentView(R.layout.eventinformation_layout);
	     
	     ev_title = (TextView)findViewById(R.id.event_title);
		 ev_desc = (TextView)findViewById(R.id.event_description);
		 
		 //get information from the last activity
		 intent = getIntent();
		 the_title = intent.getExtras().getString("event_title");
		 
	     accessWebService();
	 }
	 
	 // Async Task to access the web
	 private class JsonReadTask extends AsyncTask<String, Void, String> 
	 {
	     @Override
	     protected String doInBackground(String... params) 
	     {
	         HttpClient httpclient = new DefaultHttpClient();
	         HttpPost httppost = new HttpPost(params[0]);
		     try {
		         HttpResponse response = httpclient.execute(httppost);
		         jsonResult = inputStreamToString(
		         response.getEntity().getContent()).toString();
		      }
		      catch (ClientProtocolException e) {
		          e.printStackTrace();
		      } catch (IOException e) {
		          e.printStackTrace();
		      }
		      return null;
	     }
		 
		  private StringBuilder inputStreamToString(InputStream is) 
		  {
			  String rLine = "";
		      StringBuilder answer = new StringBuilder();
		      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		 
		      try {
		         while ((rLine = rd.readLine()) != null) 
		         {
		           answer.append(rLine);
		         }
		      }
		      catch (IOException e) {
		       // e.printStackTrace();
		      Toast.makeText(getApplicationContext(),
		      "Error..." + e.toString(), Toast.LENGTH_LONG).show();
		      }
		      return answer;
		  }
		 
		  @Override
		  protected void onPostExecute(String result)
		  {
		      ListDrawer();
		  }
	 }// end async task
	 
	 public void accessWebService() 
	 {
		  JsonReadTask task = new JsonReadTask();
		  // passes values for the urls string array
		  task.execute(new String[] { url });
	 }
	 
	 // build hash set for list view
	 public void ListDrawer() 
	 {
		  List<Map<String, String>> eventList = new ArrayList<Map<String, String>>();
		  try 
		  {
			   JSONObject jsonResponse = new JSONObject(jsonResult);
			   JSONArray jsonMainNode = jsonResponse.optJSONArray("event_list");
			 
			   //get all of the records returned from the query
			   for (int i = 0; i < jsonMainNode.length(); i++) 
			   {
				    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				    String desc = jsonChildNode.optString("title");  //get the event title from the result set
				    eventList.add(createEvent("Event", desc));  //add a new event to the list with its title
			   }
		  }
		  catch (JSONException e) {
		     Toast.makeText(getApplicationContext(), "Error" + e.toString(),
		     Toast.LENGTH_SHORT).show();
		  }
		  ev_title.setText(the_title);
	 }
	 
	 private HashMap<String, String> createEvent(String name, String number) 
	 {
		 HashMap<String, String> eventTitleDesc = new HashMap<String, String>();
		 eventTitleDesc.put(name, number);
		 return eventTitleDesc;
	 }
}