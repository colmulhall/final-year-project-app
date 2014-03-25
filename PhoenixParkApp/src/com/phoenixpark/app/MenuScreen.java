package com.phoenixpark.app;

import java.util.ArrayList;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class MenuScreen extends Activity 
{
	GridView gridView;
	ArrayList<Item> gridArray = new ArrayList<Item>();
	CustomGridViewAdapter customGridAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menuscreen_layout);
		
		//set grid view item
		Bitmap eventsIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.events_icon);
		Bitmap newsIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.news_icon);
		Bitmap placesIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.static_info);
		Bitmap twitterIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.twitter_icon);
		Bitmap mapIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.map_icon);
		Bitmap userSubmittedIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.user_submitted_icon);
		
		gridArray.add(new Item(eventsIcon,"Events"));
		gridArray.add(new Item(newsIcon,"News"));
		gridArray.add(new Item(placesIcon,"Places"));
		gridArray.add(new Item(twitterIcon,"Tweets"));
		gridArray.add(new Item(mapIcon,"Map"));
		gridArray.add(new Item(userSubmittedIcon,"User Submitted"));
		
		gridView = (GridView) findViewById(R.id.grid_view);
		customGridAdapter = new CustomGridViewAdapter(this, R.layout.row_grid, gridArray);
		gridView.setAdapter(customGridAdapter);
        
        //clicking on a menu item
        gridView.setOnItemClickListener(new OnItemClickListener() 
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
            {
            	// Events icon selected
            	if(position == 0)
            	{
	                Intent i = new Intent(getApplicationContext(), EventList.class);
	                startActivity(i);
	                overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_left_to_right);  //sliding animation
            	}
            	// News icon selected
            	else if(position == 1)
            	{
	                Intent i = new Intent(getApplicationContext(), NewsList.class);
	                startActivity(i);
	                overridePendingTransition(R.anim.slide_in_right_to_left, R.anim.slide_out_right_to_left);  //sliding animation
            	}
            	// Static park information selected
            	else if(position == 2)
            	{
	                Intent i = new Intent(getApplicationContext(), StaticInfoMain.class);
	                startActivity(i);
	                overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_left_to_right);  //sliding animation
            	}
            	// Twitter feed selected
            	else if(position == 3)
            	{
	                Intent i = new Intent(getApplicationContext(), TwitterFeed.class);
	                startActivity(i);
	                overridePendingTransition(R.anim.slide_in_right_to_left, R.anim.slide_out_right_to_left);  //sliding animation
            	}
            	 // Map selected
            	else if(position == 4)
            	{
            		
	                Intent i = new Intent(getApplicationContext(), MapPark.class);
	                startActivity(i);
	                overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_left_to_right);  //sliding animation
            	}
            	 // User events selected
            	else if(position == 5)
            	{
	                Intent i = new Intent(getApplicationContext(), UserSubmittedList.class);
	                startActivity(i);
	                overridePendingTransition(R.anim.slide_in_right_to_left, R.anim.slide_out_right_to_left);  //sliding animation
            	}
            }
        });
	}
	
	//action bar
    @SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
    	// Inflate menu resource file.  
        getMenuInflater().inflate(R.menu.mainscreen_menu, menu);  

        return true;
	}
    
    //action bar listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	int id = item.getItemId();
    	
    	// show the users favorite events
    	if(id == R.id.fav_action)
    	{	
    		Intent i = new Intent(getApplicationContext(), FavoritesList.class);
            startActivity(i);
            overridePendingTransition(R.anim.slidedown_in, R.anim.slidedown_out);  //sliding animation
    	}
        return true;
    }
}