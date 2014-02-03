/* This is where the events are retrieved from the database and 
 * put into a list for the user to view.
 */

package com.phoenixpark.app;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class EventList extends ListActivity 
{
    private ProgressDialog pDialog;
 
    // URL to get contacts JSON
    private static String url = "http://10.0.2.2/FYP-Web-Coding/android_get_titles.php";
 
    // JSON Node names
    private static final String TAG_EVENTS = "event_list";
    private static final String TAG_ID = "id";
    private static final String TAG_TITLE = "title";
 
    // contacts JSONArray
    JSONArray events = null;
 
    // Hashmap for ListView
    ArrayList<HashMap<String, String>> eventList;
 
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventlist_layout);
 
        eventList = new ArrayList<HashMap<String, String>>();
 
        ListView lv = getListView();
        
        /*// Listview on item click listener
        lv.setOnItemClickListener(new OnItemClickListener() 
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
            {
                // getting values from selected ListItem
                String ev_title = ((TextView) view.findViewById(R.id.the_title)).getText().toString();
                String ev_id = ((TextView) view.findViewById(R.id.the_id)).getText().toString();
 
                // Starting single contact activity
                Intent in = new Intent(getApplicationContext(), EventInformation.class);
                in.putExtra(TAG_TITLE, ev_title);
                in.putExtra(TAG_ID, ev_id);
                startActivity(in);
            }
        });*/
 
        // Calling async task to get json
        new GetEvents().execute();
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
            progress = ProgressDialog.show(EventList.this, "Getting events", "Please Wait...");
        }
 
        @Override
        protected Void doInBackground(Void... arg0) 
        {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
 
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
 
            Log.d("Response: ", "> " + jsonStr);
 
            if (jsonStr != null) 
            {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                     
                    // Getting JSON Array node
                    events = jsonObj.getJSONArray(TAG_EVENTS);
 
                    // looping through All Contacts
                    for (int i = 0; i < events.length(); i++) 
                    {
                        JSONObject c = events.getJSONObject(i);
                         
                        String the_title = c.getString(TAG_TITLE);
                        String the_id = c.getString(TAG_ID);
 
                        // tmp hashmap for single contact
                        HashMap<String, String> contact = new HashMap<String, String>();
 
                        // adding each child node to HashMap key => value
                        contact.put(TAG_TITLE, the_title);
                        contact.put(TAG_ID, the_id);
 
                        // adding contact to contact list
                        eventList.add(contact);
                    }
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

            ListAdapter adapter = new SimpleAdapter( EventList.this, eventList,
                    R.layout.list_item, new String[] { TAG_TITLE, TAG_ID}, new int[] { R.id.the_title,
                            R.id.the_id});
 
            setListAdapter(adapter);
        }
    }
}


/*import java.io.BufferedReader;
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
import android.util.Log;
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
		         jsonResult = inputStreamToString( response.getEntity().getContent()).toString();
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
	 
	 //build hash set for list view
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
				    title = jsonChildNode.optString("title");    //get the event title from the result set
				    Log.i("HERE TIS", title);
				    id = jsonChildNode.optString("id");          //do the same for ID
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
	 
	 private HashMap<String, String> createEvent(String name, String event) 
	 {
		 HashMap<String, String> eventTitle = new HashMap<String, String>();
		 eventTitle.put(name, event);
		 return eventTitle;
	 }
	 
	//handle click on a list item (view an event)
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
	{
	    Intent i = new Intent(EventList.this, EventInformation.class);
	    
	    //Get the item that has been clicked
	    String selectedFromList = (listView.getItemAtPosition(position).toString());
	    selectedFromList = selectedFromList.substring(7, selectedFromList.length()-1);  //remove brackets and event tag
	    i.putExtra("event_title", selectedFromList);   //send the string to the next activity
	    
	    startActivity(i);
	    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);  //animation
	}
}*/