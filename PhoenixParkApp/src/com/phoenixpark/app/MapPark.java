package com.phoenixpark.app;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapPark extends FragmentActivity implements LocationListener, OnInfoWindowClickListener
{
    GoogleMap map;
    ArrayList<LatLng> markerPoints;
    Location location; // location
    LatLng phoenixpark;
    Location loc;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected String latitude,longitude;
    private LocalDbManager db;
    public String the_location;
    
    // Lists and string arrays for markers
    List<Marker> notable_places_markers = new ArrayList<Marker>();
    List<Marker> gates_markers = new ArrayList<Marker>();
    String[] places = {};
    String[] gates = {};
    String marker_title;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mappark_layout);
        
        //Open locations database to write
	    db = new LocalDbManager(this);
	    db.openLocsToRead();
	    
	    // populate the string from the locations resource
	    places = this.getResources().getStringArray(R.array.locations);
	    gates = this.getResources().getStringArray(R.array.gates);
 
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

        // Get the parks locaiton
        location = locationManager.getLastKnownLocation(provider);
		phoenixpark = new LatLng(53.356065, -6.329665);
		
		// Zoom into the parks location when the activity starts
	    map.moveCamera(CameraUpdateFactory.newLatLng(phoenixpark));
	    map.animateCamera(CameraUpdateFactory.zoomTo(13));
	    
	    map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);  // open with satellite map
	    
	    // add markers
	    addAllMarkers();
	    
	    // set marker listener
	    //map.setOnMarkerClickListener(this);
	    map.setOnInfoWindowClickListener(this);
        
        if(map != null)
        {
            // Enable MyLocation Button in the Map
            map.setMyLocationEnabled(true);
        }
    }
 
    @Override
	public void onLocationChanged(Location location) 
	{
	}

	@Override
	public void onProviderDisabled(String provider) 
	{
		Toast.makeText(getApplicationContext(), "Provider disabled", Toast.LENGTH_SHORT).show();
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
	}

	@Override
	public void onProviderEnabled(String provider)
	{
	}
	
	// Add notable locations markers to the map
	private void addNotableMarkersToMap() 
	{
	    map.clear();
	    for (int i = 0; i < places.length; i++) 
	    {
	    	MarkerOptions marker_name = new MarkerOptions().position(new LatLng
            			(Double.parseDouble(db.getLocLatitude(places[i]).toString()), 
            			 Double.parseDouble(db.getLocLongitude(places[i]).toString())));
        	marker_name.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        	marker_name.title(places[i]);
            map.addMarker(marker_name);
	     }
	 }
	
	// Add gate locations to the map
	private void addGateMarkersToMap() 
	{
		map.clear();
		for (int i = 0; i < gates.length; i++) 
		{
			MarkerOptions marker_name2 = new MarkerOptions().position(new LatLng
	            		(Double.parseDouble(db.getLocLatitude(gates[i]).toString()), 
	            		Double.parseDouble(db.getLocLongitude(gates[i]).toString())));
	        marker_name2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
	        marker_name2.title(gates[i]);
	        map.addMarker(marker_name2);
		 }
	}
	
	// add all markers
	private void addAllMarkers()
	{
		map.clear();
		for (int i = 0; i < places.length; i++) 
		{
			MarkerOptions marker_name = new MarkerOptions().position(new LatLng
	            		(Double.parseDouble(db.getLocLatitude(places[i]).toString()), 
	            		Double.parseDouble(db.getLocLongitude(places[i]).toString())));
	        marker_name.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
	        marker_name.title(places[i]);
	        map.addMarker(marker_name);
	        
	        // once all sights markers have been added, begin adding gate markers
	        if(i == places.length-1)
	        {
	        	for (int j = 0; j < gates.length; j++) 
	    		{
	    			MarkerOptions marker_name2 = new MarkerOptions().position(new LatLng
	    	            		(Double.parseDouble(db.getLocLatitude(gates[j]).toString()), 
	    	            		Double.parseDouble(db.getLocLongitude(gates[j]).toString())));
	    	        marker_name2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
	    	        marker_name2.title(gates[j]);
	    	        map.addMarker(marker_name2);
	    		 }
	        }
		 }
	}
	
	public void onInfoWindowClick(Marker marker) 
    {
    	marker_title = marker.getTitle();
		
		// Ask the user if they want directions to the selected location
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Directions");
		builder.setMessage("Would you like directions to this location?");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() 
        {
            public void onClick(DialogInterface dialog, int id) 
            {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() 
        {
            public void onClick(DialogInterface dialog, int id) 
            {
            	Intent in = new Intent(getApplicationContext(), DirectionsTo.class);
            	in.putExtra("loc", marker_title);
                startActivity(in);
        	    overridePendingTransition(R.anim.slidedown_in, R.anim.slidedown_out);
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras)
	{
		// inform the user of status changes
		switch (status) 
		{
		    case LocationProvider.OUT_OF_SERVICE:
		        Toast.makeText(this, "Status Changed: Out of Service",
		                Toast.LENGTH_SHORT).show();
		        break;
		    case LocationProvider.TEMPORARILY_UNAVAILABLE:
		        Toast.makeText(this, "Status Changed: Temporarily Unavailable",
		                Toast.LENGTH_SHORT).show();
		        break;
		    case LocationProvider.AVAILABLE:
		        Toast.makeText(this, "Status Changed: Available",
		                Toast.LENGTH_SHORT).show();
		        break;
		}
	}
	
	//action bar
    @SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
    	// Inflate menu resource file.  
        getMenuInflater().inflate(R.menu.map_park_menu, menu); 
        return true;
	}
    
    //action bar listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	int id = item.getItemId();
    	
    	if(id == R.id.change_map_type)
    	{
    		// Allow the user to switch between map types
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder.setTitle("Select map type");
            builder.setNegativeButton("Normal", new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int id) 
                {
                	map.setMapType(GoogleMap.MAP_TYPE_NORMAL);  // normal map
                    dialog.cancel();
                }
            });
            builder.setNeutralButton("Satellite", new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int id) 
                {
                	map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);  // satellite map
                    dialog.cancel();
                }
            });
            builder.setPositiveButton("Hybrid", new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int id) 
                {
                	map.setMapType(GoogleMap.MAP_TYPE_HYBRID);  // hybrid map
                    dialog.cancel();
                }
            });

            AlertDialog alert11 = builder.create();
            alert11.show();
    	}
    	else if(id == R.id.filter_markers)
    	{
    		// Allow the user to filter map markers
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder.setTitle("Filter markers");
            builder.setPositiveButton("Sights", new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int id) 
                {
                	addNotableMarkersToMap();
                    dialog.cancel();
                }
            });
            builder.setNeutralButton("Both", new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int id) 
                {
                	addAllMarkers();
                    dialog.cancel();
                }
            });
            builder.setNegativeButton("Gates", new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int id) 
                {
                	addGateMarkersToMap();
                    dialog.cancel();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
    	}
        return true;
    }
    
    //back button pressed by user
    @Override
    public void onBackPressed() 
    {
        finish();//go back to the previous Activity
        overridePendingTransition(R.anim.slide_in_right_to_left, R.anim.slide_out_right_to_left);   
    }
}