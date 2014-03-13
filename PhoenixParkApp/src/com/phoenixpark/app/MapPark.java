package com.phoenixpark.app;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapPark extends FragmentActivity implements LocationListener 
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
 
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mappark_layout);
        
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
		phoenixpark = new LatLng(53.356065, -6.329665);
		
		// Zoom into the current location in Google Map
	    map.moveCamera(CameraUpdateFactory.newLatLng(phoenixpark));
	    map.animateCamera(CameraUpdateFactory.zoomTo(13));
	    
	    map.setMapType(GoogleMap.MAP_TYPE_HYBRID);  //hybrid map
        
        if(map != null)
        {
            // Enable MyLocation Button in the Map
            map.setMyLocationEnabled(true);
            
            Double d = Double.parseDouble(db.getLocLatitude("Farmleigh").toString());
        	
        	/*//add markers to the park locations
        	MarkerOptions farmleigh_marker = new MarkerOptions().position(new LatLng
        			(Double.parseDouble(db.getLocLatitude("Farmleigh").toString()), 
        			 Double.parseDouble(db.getLocLongitude("Farmleigh").toString())));
        	farmleigh_marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        	map.addMarker(farmleigh_marker);
        	
        	MarkerOptions visitorcentre_marker = new MarkerOptions().position(new LatLng
        			(Double.parseDouble(db.getLocLatitude("Visitor Centre").toString()), 
        			 Double.parseDouble(db.getLocLongitude("Visitor Centre").toString())));
        	visitorcentre_marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        	map.addMarker(visitorcentre_marker);*/
        	
        	String[] places = {"Farmleigh", "Visitor Centre", "Papal Cross"};
        	
        	for(int i = 0; i < places.length; i++)
        	{
        		if(i == 0)
        		{
	        		MarkerOptions marker_name = new MarkerOptions().position(new LatLng
	            			(Double.parseDouble(db.getLocLatitude(places[i+1]).toString()), 
	            			 Double.parseDouble(db.getLocLongitude(places[i+1]).toString())));
	        		marker_name.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
	            	map.addMarker(marker_name);
        		}
        	}
        }
    }
 
	@Override
	public void onLocationChanged(Location arg0) 
	{
		// TODO Auto-generated method stub	
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
}