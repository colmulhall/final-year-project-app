package com.phoenixpark.app;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

public class StaticInfoMain extends FragmentActivity 
{
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.static_info_main_layout);
        
        /** Getting a reference to the ViewPager defined the layout file */        
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        
        /** Getting fragment manager */
        FragmentManager fm = getSupportFragmentManager();
        
        /** Instantiating FragmentPagerAdapter */
        StaticFragmentPagerAdapter pagerAdapter = new StaticFragmentPagerAdapter(fm);
        
        /** Setting the pagerAdapter to the pager object */
        pager.setAdapter(pagerAdapter);
    }
    
    //back button pressed by user
    @Override
    public void onBackPressed() 
    {
        finish();//go back to the previous Activity
        overridePendingTransition(R.anim.slide_in_right_to_left, R.anim.slide_out_right_to_left);   
    }
}