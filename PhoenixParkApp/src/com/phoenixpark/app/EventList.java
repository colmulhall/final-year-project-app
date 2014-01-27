/* This is where the events are retrieved from the database and 
 * put into a list for the user to view.
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

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView;
 
public class EventList extends Activity implements AdapterView.OnItemClickListener
{
	 private String jsonResult;
	 private String url = "http://10.0.2.2/FYP-Web-Coding/android_get_titles.php";
	 private ListView listView;
	 public String title, id;
	 public List<Map<String, String>> eventList;
	 
	 
	 @Override
	 protected void onCreate(Bundle savedInstanceState) 
	 {
	     super.onCreate(savedInstanceState);
	     setContentView(R.layout.eventlist_layout);
	     listView = (ListView) findViewById(R.id.listView1);
	     accessWebService();
	 }
	 
	 @Override
	 public boolean onCreateOptionsMenu(Menu menu) 
	 {
		 // Inflate the menu; this adds items to the action bar if it is present.
	     getMenuInflater().inflate(R.menu.main, menu);
	     return true;
	 }
	 
	 //Async Task to access the web
	 private class JsonReadTask extends AsyncTask<String, Void, String> 
	 {
		 private ProgressDialog progress;  //progress dialog in case of slow connection
		 
		 @Override
	     protected void onPreExecute() 
		 {
			 progress = ProgressDialog.show(EventList.this, "Getting events", "Please Wait...");
	     }
		 
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
		      progress.dismiss();
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
		  eventList = new ArrayList<Map<String, String>>();
		  try 
		  {
			   JSONObject jsonResponse = new JSONObject(jsonResult);
			   JSONArray jsonMainNode = jsonResponse.optJSONArray("event_list");
			 
			   //get all of the records returned from the query
			   for (int i = 0; i < jsonMainNode.length(); i++) 
			   {
				    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				    title = jsonChildNode.optString("title");  //get the event title from the result set
				    id = jsonChildNode.optString("id");        //do the same for ID
				    eventList.add(createEvent("Event", title));  //add a new event to the list with its title
			   }
		  }
		  catch (JSONException e) {
		     Toast.makeText(getApplicationContext(), "Error" + e.toString(),
		     Toast.LENGTH_SHORT).show();
		  }
		 
		  //adapter to put event into the list
		  SimpleAdapter simpleAdapter = new SimpleAdapter(this, eventList, android.R.layout.simple_list_item_1,
				  new String[] { "Event" }, new int[] { android.R.id.text1 });
		  
		  //set adapter and listener
		  listView.setAdapter(simpleAdapter);
		  listView.setOnItemClickListener(this);
	 }
	 
	 private HashMap<String, String> createEvent(String name, String number) 
	 {
		 HashMap<String, String> eventTitleDesc = new HashMap<String, String>();
		 eventTitleDesc.put(name, number);
		 return eventTitleDesc;
	 }
	 
	//handle click on a list item (view an event)
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
	{
	    Intent i = new Intent(EventList.this, EventInformation.class);
	    
	    i.putExtra("event_id", title);  //pass the event title to the next activity
	    startActivity(i);
	    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);  //animation
	}
}