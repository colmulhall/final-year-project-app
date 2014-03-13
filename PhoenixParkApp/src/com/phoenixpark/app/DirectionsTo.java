package com.phoenixpark.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class DirectionsTo extends FragmentActivity implements LocationListener 
{ 
    private GoogleMap map;
    private ArrayList<LatLng> markerPoints;
    private Location location; // location
    private LatLng currentLocation;
    private Location loc, loc2;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected String latitude,longitude;
    private Intent intent;
    private String url;
    private LocalDbManager db;
    public String the_location;
    double current_lat, current_lng;
    private Float meters_between;

    private String mode;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directionsto_layout);
        
        //Open locations database to write
	    db = new LocalDbManager(this);
	    db.openLocsToRead();
 
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        
        // Initializing
        markerPoints = new ArrayList<LatLng>();
 
        // Getting reference to SupportMapFragment of the activity_main
        SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
 
        // Getting Map for the SupportMapFragment
        map = fm.getMap();
        
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        location = locationManager.getLastKnownLocation(provider);
        current_lat = location.getLatitude();
		current_lng = location.getLongitude();
		currentLocation = new LatLng(current_lat, current_lng);
		
		// Zoom into the current location in Google Map
	    map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
	    map.animateCamera(CameraUpdateFactory.zoomTo(15));
        
        //get location of this event/news item
		intent = getIntent();
        the_location = intent.getExtras().getString("loc");
        
        //get lat/long from local database
        String lt = db.getLocLatitude(the_location);
        String lg = db.getLocLongitude(the_location);
        
        //parse these from string to double so they can be plotted on a map
        double lat = Double.parseDouble(lt);
        double lng = Double.parseDouble(lg);
        
        if(map != null)
        {
            // Enable MyLocation Button in the Map
            map.setMyLocationEnabled(true);
            
            LatLng origin = new LatLng(location.getLatitude(), location.getLongitude());
        	LatLng dest = new LatLng(lat, lng);
        	
        	//get current location coordinates
        	Location loc = new Location("Current");
        	loc.setLatitude(current_lat);
    		loc.setLongitude(current_lng);
        	
    		//get location of the event
    		Location loc2 = new Location("Destination");
        	loc2.setLatitude(lat);
        	loc2.setLongitude(lng);
        	
        	//calculate the distance between the two
        	meters_between = loc.distanceTo(loc2);
        	
        	//add marker to the destination
        	MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, lng)).title(the_location);
        	marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        	map.addMarker(marker);

        	//check if the person is located near the park.
        	if(meters_between < 10000)
        	{
        		mode = "mode=walking";    //close enough to walk
        		
	            // Getting URL to the Google Directions API
	            String url = getDirectionsUrl(origin, dest);
	
	            DownloadTask downloadTask = new DownloadTask();
	
	            // Start downloading json data from Google Directions API
	            downloadTask.execute(url);
        	}
        	else if(meters_between >= 10000 && meters_between <= 100000)
        	{
        		mode = "mode=driving";   //too far to walk
        		
	            // Getting URL to the Google Directions API
	            String url = getDirectionsUrl(origin, dest);
	
	            DownloadTask downloadTask = new DownloadTask();
	
	            // Start downloading json data from Google Directions API
	            downloadTask.execute(url);
        	}
        	else
        	{
        		//tell user they are too far from the park to calculate directions
        		AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("You are located too far from the park for directions.");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
                {
                    public void onClick(DialogInterface dialog, int id) 
                    {
                        dialog.cancel();
                    }
                });

                AlertDialog alert11 = builder.create();
                alert11.show();
        	}
        }
    }
    
    private String getDirectionsUrl(LatLng origin,LatLng dest)
    {
        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;
 
        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;
 
        // Sensor enabled
        String sensor = "sensor=false";
 
        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+mode;
 
        // Output format
        String output = "json";
 
        // Building the url to the web service
        url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
 
        return url;
    }
    
    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException
    {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
 
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
 
            // Connecting to url
            urlConnection.connect();
 
            // Reading data from url
            iStream = urlConnection.getInputStream();
 
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
 
            StringBuffer sb = new StringBuffer();
 
            String line = "";
            while((line = br.readLine()) != null)
            {
                sb.append(line);
            }
 
            data = sb.toString();
 
            br.close();
 
        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
 
    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String>
    {
        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) 
        {
            // For storing data from web service
            String data = "";
 
            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }
 
        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) 
        {
            super.onPostExecute(result);
 
            ParserTask parserTask = new ParserTask();
 
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }
 
    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >
    {
        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
 
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
 
            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
 
                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }
 
        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) 
        {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
 
            // Traversing through all the routes
            for(int i=0;i<result.size();i++)
            {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();
 
                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);
 
                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++)
                {
                    HashMap<String,String> point = path.get(j);
 
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
 
                    points.add(position);
                }
 
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(Color.CYAN);
            }
 
            // Drawing polyline in the Google Map for the i-th route
            map.addPolyline(lineOptions);
        }
    }

    // unimplemented methods
	@Override
	public void onLocationChanged(Location location) 
	{
		double lat = location.getLatitude();
		double lng = location.getLongitude();

		LatLng currentLocation = new LatLng(lat, lng);
		
		//DownloadTask downloadTask = new DownloadTask();
        //downloadTask.execute(url);

		// Showing the current location in Google Map
	    //map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));

	    // Zoom in the Google Map
	    //map.animateCamera(CameraUpdateFactory.zoomTo(15));
	}

	@Override
	public void onProviderDisabled(String provider) 
	{
		// TODO Auto-generated method stub	
	}

	@Override
	public void onProviderEnabled(String provider)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) 
	{
		// TODO Auto-generated method stub	
	}
	
	//**************************************************
	//action bar
    @SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
    	// Inflate menu resource file.  
        getMenuInflater().inflate(R.menu.directions_to_menu, menu); 
        return true;
	}
    
    //action bar listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	int id = item.getItemId();
    	
    	if(id == R.id.travel_action)
    	{
    		//convert from meters to kilometers and round to 2 decimal places
    		Double calculate_km = meters_between * 0.001;
    		float km_between = (float) calculate_km.floatValue();
    		BigDecimal bd = new BigDecimal(Float.toString(km_between));
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
    
    		// Give the user travel information
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder.setTitle("Travel Information");
            builder.setMessage("You are " + bd + " km from " + the_location);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int id) 
                {
                    dialog.cancel();
                }
            });

            AlertDialog alert11 = builder.create();
            alert11.show();
    	}
        return true;
    }
    
	// back button pressed by user
    @Override
    public void onBackPressed() 
    {
        finish();//go back to the previous Activity
        overridePendingTransition(R.anim.slideup_in, R.anim.slideup_out);   
    }
}