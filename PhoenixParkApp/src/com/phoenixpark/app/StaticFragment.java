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
			img1.setImageResource(R.drawable.aras_static_1);
			tv1.setText(R.string.aras_an_uachtarain_desc_p1);
			img2.setImageResource(R.drawable.aras_static_2);
			tv2.setText(R.string.aras_an_uachtarain_desc_p2);
			img3.setImageResource(R.drawable.aras_static_3);
			tv3.setText(R.string.aras_an_uachtarain_desc_p3);
		}
		else if(mCurrentPage == 2)   // Papal cross page
		{
			img1.setImageResource(R.drawable.papal_static_1);
			tv1.setText(R.string.papal_cross_desc_p1);
			img2.setImageResource(R.drawable.papal_static_2);
			tv2.setText(R.string.papal_cross_desc_p2);
		}
		else if(mCurrentPage == 3)   // farmleigh page
		{
			img1.setImageResource(R.drawable.farmleigh_static_1);
			tv1.setText(R.string.farmleigh_desc_p1);
			img2.setImageResource(R.drawable.farmleigh_static_2);
			tv2.setText(R.string.farmleigh_desc_p2);
			img3.setImageResource(R.drawable.farmleigh_static_3);
			tv3.setText(R.string.farmleigh_desc_p3);
		}
		else if(mCurrentPage == 4)   // wellington monument page
		{
			img1.setImageResource(R.drawable.wellington_static_1);
			tv1.setText(R.string.wellington_desc_p1);
			img2.setImageResource(R.drawable.wellington_static_2);
			tv2.setText(R.string.wellington_desc_p2);
		}
		else if(mCurrentPage == 5)   // dublin zoo page
		{
			img1.setImageResource(R.drawable.dublin_zoo_static_1);
			tv1.setText(R.string.dublin_zoo_desc_p1);
			img2.setImageResource(R.drawable.dublin_zoo_static_2);
			tv2.setText(R.string.dublin_zoo_desc_p2);
			img3.setImageResource(R.drawable.dublin_zoo_static_3);
			tv3.setText(R.string.dublin_zoo_desc_p3);
		}
		else if(mCurrentPage == 6)   // deerfield page
		{
			img1.setImageResource(R.drawable.deerfield_static_1);
			tv1.setText(R.string.deerfield_desc_p1);
			img2.setImageResource(R.drawable.deerfield_static_2);
			tv2.setText(R.string.deerfield_desc_p2);
			img3.setImageResource(R.drawable.deerfield_static_3);
			tv3.setText(R.string.deerfield_desc_p3);
		}
		else if(mCurrentPage == 7)   // ashtown castle page
		{
			img1.setImageResource(R.drawable.ashtown_static_1);
			tv1.setText(R.string.ashtown_desc_p1);
			img2.setImageResource(R.drawable.ashtown_static_2);
			tv2.setText(R.string.ashtown_desc_p2);
			img3.setImageResource(R.drawable.ashtown_static_3);
			tv3.setText(R.string.ashtown_desc_p3);
		}
		else if(mCurrentPage == 8)   // peoples garden page
		{
			img1.setImageResource(R.drawable.peoples_garden_static_1);
			tv1.setText(R.string.peoples_garden_desc_1);
			img2.setImageResource(R.drawable.peoples_garden_static_2);
			tv2.setText(R.string.peoples_garden_desc_2);
		}
		else if(mCurrentPage == 9)   // magazine fort page
		{
			img1.setImageResource(R.drawable.magazine_static_1);
			tv1.setText(R.string.magazine_desc_1);
			img2.setImageResource(R.drawable.magazine_static_2);
			tv2.setText(R.string.magazine_desc_2);
			img3.setImageResource(R.drawable.magazine_static_3);
			tv3.setText(R.string.magazine_desc_3);
		}
		return v;	
	}
}