package com.phoenixpark.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class StaticFragmentPagerAdapter extends FragmentPagerAdapter
{	
	final int PAGE_COUNT = 9;

	/** Constructor of the class */
	public StaticFragmentPagerAdapter(FragmentManager fm) 
	{
		super(fm);
	}

	/** This method will be invoked when a page is requested to create */
	@Override
	public Fragment getItem(int arg0) 
	{
		
		StaticFragment myFragment = new StaticFragment();
		Bundle data = new Bundle();
		data.putInt("current_page", arg0+1);
		myFragment.setArguments(data);
		return myFragment;
	}

	/** Returns the number of pages */
	@Override
	public int getCount() 
	{		
		return PAGE_COUNT;
	}
	
	@Override
	public CharSequence getPageTitle(int position) 
	{		
		if(position == 0)
			return "Aras an Uachtaran";
		else if(position == 1)
			return "Papal Cross";
		else if(position == 2)
			return "Farmleigh House";
		else if(position == 3)
			return "Wellington Monument";
		else if(position == 4)
			return "Dublin Zoo";
		else if(position == 5)
			return "Deerfield Residence";
		else if(position == 6)
			return "Ashtown Castle & Visitor Centre";
		else if(position == 7)
			return "People's Gardens";
		else
			return "Magazine Fort";
	}
}