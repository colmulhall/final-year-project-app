package com.phoenixpark.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ShareActionProvider;
import android.widget.AdapterView.OnItemClickListener;

public class MenuScreen extends Activity
{
	private ShareActionProvider myShareActionProvider;
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuscreen_layout);
        
        GridView gridView = (GridView) findViewById(R.id.grid_view);
        
        gridView.setAdapter(new MenuItems(this));
        
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
            	else if(position == 2)
            	{
	                Intent i = new Intent(getApplicationContext(), StaticInfoMain.class);
	                startActivity(i);
	                overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_left_to_right);  //sliding animation
            	}
            	else if(position == 3)
            	{
	                Intent i = new Intent(getApplicationContext(), TwitterFeed.class);
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
        getMenuInflater().inflate(R.menu.information_menu, menu);  
        
        //Getting the actionprovider associated with the menu item whose id is share
        myShareActionProvider = (ShareActionProvider) menu.findItem(R.id.share).getActionProvider();
 
        //Setting a share intent
        myShareActionProvider.setShareIntent(getDefaultShareIntent());

        return true;
	}
    
    //returns share intent
    private Intent getDefaultShareIntent()
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"Event: ");
        return intent;
    }
    
    //action bar listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	switch (item.getItemId()) 
    	{	
    	  // show the users favorite events
	      case R.id.fav_action:
	    	  Intent i = new Intent(getApplicationContext(), FavoritesList.class);
              startActivity(i);
              overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_left_to_right);  //sliding animation
    	}
        return true;
    }
}