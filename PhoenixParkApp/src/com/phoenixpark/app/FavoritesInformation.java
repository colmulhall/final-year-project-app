package com.phoenixpark.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FavoritesInformation extends Activity
{
	private LocalDbManager db;
	private String passedValue;
	private TextView the_title, the_desc;
	public int id;
	public final static String ID_EXTRA = "com.phoenixpark.app._ID";
	
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
		
		//set the text of each TextView to the information in the database
		the_title.setText(db.getItemTitle(id));
		the_desc.setText(db.getItemDesc(id));
        
        /*//handle deleting the item
        delete.setOnClickListener(new View.OnClickListener() 
        {
			public void onClick(View v) 
			{
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() 
		        {
					//"Are you sure?" dialog options
		            @Override
		            public void onClick(DialogInterface dialog, int which) 
		            {
		                switch (which)
		                {
			                case DialogInterface.BUTTON_POSITIVE:
			                    //Yes button clicked
			                	Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
			    				db.deleteCountry(id);
			    				finish();
			                    break;
			
			                case DialogInterface.BUTTON_NEGATIVE:
			                    //No button clicked
			                    break;
		                }
		            }
		        };

		        AlertDialog.Builder builder = new AlertDialog.Builder(CountryInfo.this);
		        builder.setTitle("Delete");
		        builder.setMessage("Are you sure?")
		        .setPositiveButton("Yes", dialogClickListener)
		        .setNegativeButton("No", dialogClickListener)
		        .show();
			}
        });
	}
	
	//action bar
    @Override
	 public boolean onCreateOptionsMenu(Menu menu) 
	 {
    	//Inflate the menu. This adds items to the action bar if it is present.
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.country_menu, menu);
        return true;
	 }
    
    //action bar listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	switch (item.getItemId()) 
        {
    	  case R.id.edit_action:
    		    Intent i = new Intent(CountryInfo.this, EditCountry.class);
    		    i.putExtra("Country ID", id);
    		    startActivity(i);
    		    break;
				
    	  case R.id.about_action:
	        	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() 
		        {
		            @Override
		            public void onClick(DialogInterface dialog, int which) 
		            {
		                switch(which)
		                {
			                case DialogInterface.BUTTON_POSITIVE:
			                    //OK button clicked. exits dialog
			                    break;
			                    
			                case DialogInterface.BUTTON_NEUTRAL:
			                	//Feedback button clicked. option to send email to developer
			                	final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
			                	emailIntent.setType("plain/text");
			                	emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"colmmul92@yahoo.co.uk"});
			                	emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Travel Log Feedback");
			                	
			                	//get information about the device in which feedback is coming from
			                	String Device = Build.MANUFACTURER;
			                	String Model = Build.MODEL;
			                	@SuppressWarnings("deprecation")
								String APIlevel = android.os.Build.VERSION.SDK;
			                	emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, 
			                			"Device: " + Device + 
			                			"\n" + "Model: " + Model + 
			                			"\n" + "API Level: " + APIlevel + "\n\n");
			                	startActivity(Intent.createChooser(emailIntent, "Send mail using..."));
		                }
		            }
		        };
		        
		        AlertDialog.Builder builder = new AlertDialog.Builder(CountryInfo.this);
		        builder.setTitle("Travel Log");
		        builder.setMessage("Travel Log provides an easy way of logging countries that you have been to."
		        		+ "\n\n" + "Author: Colm Mulhall" + "\n"
		        				 +  
		        		"Version: 1.2")
		        .setPositiveButton("OK", dialogClickListener)
		        .setNeutralButton("Feedback", dialogClickListener)
		        .show();
	        	break;
	
	      default:
	    	  	break;
        }
        return true;*/
    }
}