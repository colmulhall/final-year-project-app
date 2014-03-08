package com.phoenixpark.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

public class FavoritesInformation extends Activity
{
	private LocalDbManager db;
	private String passedValue, title, desc, location, link;
	private TextView the_title, the_desc;
	public int id;
	public final static String ID_EXTRA = "com.phoenixpark.app._ID";
	private ShareActionProvider myShareActionProvider;
			
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favorites_information_layout);
		
		//intent passed data
		passedValue = getIntent().getStringExtra(FavoritesList.ID_EXTRA);
		id = Integer.parseInt(passedValue);
		
		the_title = (TextView)findViewById(R.id.item_title);
		the_desc = (TextView)findViewById(R.id.item_description);
		
		//open database to read and begin selecting its information
		db = new LocalDbManager(this);
		db.openToRead();
		
		//get information from the local database for the item
		title = db.getItemTitle(id);
		desc = db.getItemDesc(id);
		//location = db.getItemLocation(id);
		link = db.getItemLink(id);
		
		//set the text of each TextView to the information in the database
		the_title.setText(title);
		the_desc.setText(desc);
    }
	
	//action bar
    @SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
    	// Inflate menu resource file.  
        getMenuInflater().inflate(R.menu.favorites_information_menu, menu);
        
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
        intent.putExtra(Intent.EXTRA_SUBJECT, "SUBJECT");
        intent.putExtra(Intent.EXTRA_TEXT,"Phoenix Park event: " + link);
        return intent;
    }
    
    //action bar listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	int menu_item_id = item.getItemId();
    	
    	if(menu_item_id == R.id.link_page)
    	{
    		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
	    	startActivity(browserIntent);
    	}
    	else if(menu_item_id == R.id.delete_fav)
    	{
    		Toast.makeText(getApplicationContext(), "Deleted from favorites", Toast.LENGTH_LONG).show();
			db.deleteFavorite(id);
			finish();
    	}
        return true;
    }
}