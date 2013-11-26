package com.phoenixpark.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

public class EventList extends ListActivity
{
	ArrayList<HashMap<String, String>> eventList;
	private ProgressDialog progressMessage;
	JSONParser jParser = new JSONParser();
	
	private static String url = "http://10.0.2.2/FYP-Web-Coding/android_connet.php";
	JSONArray events = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventlist_layout);
		eventList = new ArrayList<HashMap<String, String>>();
		new LoadAllEvents().execute();
	}
	 
	class LoadAllEvents extends AsyncTask<String, String, String> 
	{
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			progressMessage = new ProgressDialog(EventList.this);
			progressMessage.setMessage("Loading ...");
			progressMessage.setIndeterminate(false);
			progressMessage.setCancelable(false);
			progressMessage.show();
		}
	 
		@Override
		protected String doInBackground(String... args) 
		{
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			JSONObject json = jParser.getJSONFromUrl(url);
			 
			Log.d("Events: ", json.toString());
			
			try 
			{
				int success = json.getInt("success");
			 
				if (success == 1) 
				{
					events = json.getJSONArray("events");
					for (int i = 0; i < events.length(); i++) 
					{
						JSONObject c = events.getJSONObject(i);
						String id = c.getString("id");
						String name = c.getString("title");
						String type = c.getString("description");
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("event_id", id);
						map.put("event_title", name);
						map.put("event_description", type);
						eventList.add(map);
					}
				}
			}
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
	 
			return null;
		}
		
		@Override
		protected void onPostExecute(String file_url) 
		{
			progressMessage.dismiss();
			runOnUiThread(new Runnable() 
			{
				@Override
				public void run() 
				{
					ListAdapter adapter = new SimpleAdapter(EventList.this, eventList, R.layout.eventlist_layout, 
					new String[] { "event_id","event_title","event_description"}, null);
				
					setListAdapter(adapter);
				}
			});
		}
	}
}