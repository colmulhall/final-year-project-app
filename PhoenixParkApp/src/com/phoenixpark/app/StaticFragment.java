package com.phoenixpark.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class StaticFragment extends Fragment{
	
	int mCurrentPage;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/** Getting the arguments to the Bundle object */
		Bundle data = getArguments();
		
		/** Getting integer data of the key current_page from the bundle */
		mCurrentPage = data.getInt("current_page", 0);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View v = inflater.inflate(R.layout.fragment_layout, container,false);	
		
		TextView tv = (TextView ) v.findViewById(R.id.tv);
		ImageView img = (ImageView) v.findViewById(R.id.im);
		
		if(mCurrentPage == 1)
		{
			//img.setImageResource(R.drawable.ic_action_search);
			tv.setText("Aras");
		}
		else if(mCurrentPage == 2)
		{
			img.setImageResource(R.drawable.ic_launcher);
			tv.setText("Monu");
		}
		return v;		
	}
}