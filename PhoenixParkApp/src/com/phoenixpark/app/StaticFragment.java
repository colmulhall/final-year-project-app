package com.phoenixpark.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class StaticFragment extends Fragment
{	
	int mCurrentPage;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
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
		
		ImageView img1 = (ImageView) v.findViewById(R.id.im1);
		TextView tv1 = (TextView ) v.findViewById(R.id.pt1);
		ImageView img2 = (ImageView) v.findViewById(R.id.im2);
		TextView tv2 = (TextView ) v.findViewById(R.id.pt2);
		ImageView img3 = (ImageView) v.findViewById(R.id.im3);
		TextView tv3 = (TextView ) v.findViewById(R.id.pt3);
		
		if(mCurrentPage == 1)  // Aras an uachtaran page
		{
			img1.setImageResource(R.drawable.aras_front);
			tv1.setText(R.string.aras_an_uachtarain_desc_p1);
			img2.setImageResource(R.drawable.aras_yard);
			tv2.setText(R.string.aras_an_uachtarain_desc_p2);
			img3.setImageResource(R.drawable.aras_reception_room);
			tv3.setText(R.string.aras_an_uachtarain_desc_p3);
		}
		else if(mCurrentPage == 2)   // 
		{
			img1.setImageResource(R.drawable.ic_launcher);
			tv1.setText("hello");
		}
		return v;		
	}
}