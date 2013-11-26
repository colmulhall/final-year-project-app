package com.phoenixpark.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuScreen extends Activity
{
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuscreen_layout);
        
        Button events = (Button)findViewById(R.id.button);
        
        events.setOnClickListener(new View.OnClickListener() 
        {
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(MenuScreen.this, EventList.class);
				startActivity(intent);
			}
        });
        
	}
}