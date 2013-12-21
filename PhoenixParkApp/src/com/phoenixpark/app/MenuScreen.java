package com.phoenixpark.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class MenuScreen extends Activity
{
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuscreen_layout);
        
        GridView gridView = (GridView) findViewById(R.id.grid_view);
        
        gridView.setAdapter(new MenuItems(this));
        
        //clicking on a menu item
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                    int position, long id) {
 
                // Sending image id to FullScreenActivity
                Intent i = new Intent(getApplicationContext(), EventList.class);
                startActivity(i);
            }
        });
	}
}