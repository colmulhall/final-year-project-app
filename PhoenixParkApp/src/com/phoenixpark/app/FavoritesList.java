package com.phoenixpark.app;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class FavoritesList extends Activity implements AdapterView.OnItemClickListener
{
	private LocalDbManager db;
	private Cursor cursor;
	private ListView listContent;
	public final static String ID_EXTRA = "com.phoenixpark.app._ID";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites_list_layout);
        
        listContent = (ListView)findViewById(R.id.list);
        
        //Open database to read
        db = new LocalDbManager(this);
        db.openFavsToRead();
        
        //check if any favorites have been added. If not end the activity with a message
        if(db.countFavs() > 0)
        {
	        cursor = db.orderListFav();
	        
	        String[] from = new String[]{
	        		LocalDbManager.KEY_FAV_EVENT_TITLE, 
	        		LocalDbManager.KEY_FAV_ID,
	        		LocalDbManager.KEY_FAV_EVENT_DESCRIPTION,
	        		LocalDbManager.KEY_FAV_EVENT_LOCATION,
	        		LocalDbManager.KEY_FAV_EVENT_DATE,
	        		LocalDbManager.KEY_FAV_EVENT_LINK};
	        int[] to = new int[]{R.id.the_title};
	
	        @SuppressWarnings("deprecation")
			SimpleCursorAdapter cursorAdapter =  new SimpleCursorAdapter(this, R.layout.row, cursor, from, to);
	        
	        listContent.setAdapter(cursorAdapter);
	        listContent.setOnItemClickListener(this);
        }
        else
        {
        	Toast.makeText(getApplicationContext(), "No favorites saved", Toast.LENGTH_SHORT).show();
        	finish();
        }
    }
    
    //action bar
    @Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		//Inflate the menu. This adds items to the action bar if it is present.
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainscreen_menu, menu);
        return true;
	}
    
    //handle click on a list item
    @Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
    {
    	Intent i = new Intent(this, FavoritesInformation.class);
    	i.putExtra(ID_EXTRA, String.valueOf(id));  //pass the id of the selected item with the intent
    	startActivity(i);
	}
    
    @Override
	protected void onResume()
	{
		super.onResume();
		this.onCreate(null); //refresh the activity once it resumes 
	}
}