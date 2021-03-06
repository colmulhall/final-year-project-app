/* Icons for the main menu items are set here
 * as well as the layout.
 */

package com.phoenixpark.app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
 
public class MenuItems extends BaseAdapter 
{
    private Context mContext;
 
    // Keep all Images in array
    public Integer[] mThumbIds = 
    {
            R.drawable.events_icon,
            R.drawable.news_icon,
            R.drawable.static_info,
            R.drawable.twitter_icon,
            R.drawable.map_icon,
            R.drawable.user_submitted_icon
    };
 
    // Constructor
    public MenuItems(Context c)
    {
        mContext = c;
    }
 
    @Override
    public int getCount() 
    {
        return mThumbIds.length;
    }
 
    @Override
    public Object getItem(int position) 
    {
        return mThumbIds[position];
    }
    
    @Override
    public long getItemId(int position) 
    {
        return 0;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) 
    {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(mThumbIds[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
        return imageView;
    }
}