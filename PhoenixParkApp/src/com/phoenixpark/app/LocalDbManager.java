package com.phoenixpark.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteStatement;

public class LocalDbManager 
{
	//favorites database
	public static final String KEY_FAV_ID = "_id";
	public static final String KEY_FAV_EVENT_TITLE = "title";
	public static final String KEY_FAV_EVENT_DESCRIPTION = "description";
	public static final String KEY_FAV_EVENT_DATE = "date";
	public static final String KEY_FAV_EVENT_LOCATION = "location";
	public static final String KEY_FAV_EVENT_LINK = "link";
	
	public static final String FAV_DATABASE_NAME = "Event favorites database";
	public static final String FAV_DATABASE_TABLE = "favorites_list_local";
	public static final int FAV_DATABASE_VERSION = 1;
	
	
	//create database table for favorites
	private static final String CREATE_DATABASE_FAVORITES =
			"create table " + FAV_DATABASE_TABLE + " ("
			+ KEY_FAV_ID + " integer primary key autoincrement, "
			+ KEY_FAV_EVENT_TITLE + " text not null unique,"
			+ KEY_FAV_EVENT_DESCRIPTION + " integer ,"
			+ KEY_FAV_EVENT_DATE + " text ,"
			+ KEY_FAV_EVENT_LOCATION + " text ,"
			+ KEY_FAV_EVENT_LINK + " text);";
	
	//locations database
	public static final String KEY_LOC_ID = "_id";
	public static final String KEY_LOC_TITLE = "title";
	public static final String KEY_LOC_DESCRIPTION = "description";
	public static final String KEY_LOC_LATITUDE = "latitude";
	public static final String KEY_LOC_LONGITUDE = "longitude";
	
	public static final String LOC_DATABASE_NAME = "Park locations database";
	public static final String LOC_DATABASE_TABLE = "locations_local";
	public static final int LOC_DATABASE_VERSION = 1;
	
	//create database table for locations
	private static final String CREATE_DATABASE_LOCATIONS =
			"create table " + LOC_DATABASE_TABLE + " ("
			+ KEY_LOC_ID + " integer primary key autoincrement, "
			+ KEY_LOC_TITLE + " text not null,"
			+ KEY_LOC_DESCRIPTION + " integer ,"
			+ KEY_LOC_LATITUDE + " text ,"
			+ KEY_LOC_LONGITUDE + " text);";
	
	
	private Context context;
	private DBHelper DBHelper;
	private SQLiteDatabase db;

	public LocalDbManager(Context ctx)
	{
		context = ctx;
	}
	
	//beginning of helper class **************************************************************************
	public class DBHelper extends SQLiteOpenHelper 
	{
		public DBHelper(Context context, String name, CursorFactory factory, int version)
		{
			super(context, name, factory, version);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			 db.execSQL(CREATE_DATABASE_FAVORITES);
			 db.execSQL(CREATE_DATABASE_LOCATIONS);
		}
		
		//this will be called if I make a change to the database and give it a new version number
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
		{
			onCreate(db);
		}
	}
	//end of helper class ***********************************************************************************
	
	//**************** FAVORITES DATABASE OPERATIONS *********************
	public LocalDbManager openFavsToRead() throws SQLException 
	{
		DBHelper = new DBHelper(context, FAV_DATABASE_TABLE, null, FAV_DATABASE_VERSION);
		db = DBHelper.getReadableDatabase();
		return this;
	}
	
	public LocalDbManager openFavsToWrite() throws SQLException 
	{
		DBHelper = new DBHelper(context, FAV_DATABASE_TABLE, null, FAV_DATABASE_VERSION);
		db = DBHelper.getWritableDatabase();
		return this;
	}
	
	//close the database
	public void close()
	{
		DBHelper.close();
	}
	
	//insert a new item into the favorites database
	public long insertFav(String title, String desc, String date, String location, String link)
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put(KEY_FAV_EVENT_TITLE, title);
		contentValues.put(KEY_FAV_EVENT_DESCRIPTION, desc);
		contentValues.put(KEY_FAV_EVENT_DATE, date);
		contentValues.put(KEY_FAV_EVENT_LOCATION, location);
		contentValues.put(KEY_FAV_EVENT_LINK, link);
		
		return db.insert(FAV_DATABASE_TABLE, null, contentValues);
	}
	
	//delete everything from the database
	public int deleteAllFav()
	{
		return db.delete(FAV_DATABASE_TABLE, null, null);
	}
	
	//delete the database 
	public void deleteDatabaseFav()
	{
		context.deleteDatabase(FAV_DATABASE_NAME);
	}
	
	//delete a particular item, based on its passed in ID
	public void deleteFavorite(int num)
	{
		db.delete(FAV_DATABASE_TABLE, KEY_FAV_ID + " = " + num, null);
	}
	
	//queue the items in the database
	public Cursor queueAllFav()
	{
		String[] columns = new String[]{
				KEY_FAV_ID, 
				KEY_FAV_EVENT_TITLE,
				KEY_FAV_EVENT_DESCRIPTION,
				KEY_FAV_EVENT_DATE,
				KEY_FAV_EVENT_LOCATION,
				KEY_FAV_EVENT_LINK};
		Cursor cursor = db.query(FAV_DATABASE_TABLE, columns,
		  null, null, null, null, null);
	
		return cursor;
	}
	
	//order the list by the year in descending order
	public Cursor orderListFav()
	{
		String[] columns = new String[]{
				KEY_FAV_ID, 
				KEY_FAV_EVENT_TITLE,
				KEY_FAV_EVENT_DESCRIPTION,
				KEY_FAV_EVENT_DATE,
				KEY_FAV_EVENT_LOCATION,
				KEY_FAV_EVENT_LINK};


		Cursor cursor = db.query(FAV_DATABASE_TABLE, columns, null, null, null, null, KEY_FAV_EVENT_DATE + " DESC");
		return cursor;
	}
	
	public long countFavs() 
	{
	    String sql = "SELECT COUNT(*) FROM " + FAV_DATABASE_TABLE;
	    SQLiteStatement statement = db.compileStatement(sql);
	    long count = statement.simpleQueryForLong();
	    
	    return count;
	}
	
	//getters for the favorites database
	public String getFavTitle(int num)
    {
		Cursor cursor = db.query(FAV_DATABASE_TABLE, new String[] {"title"}, 
				"_id like " + num, null, null, null, null);
		
		cursor.moveToFirst();
		return cursor.getString(0);
    }
	
	public String getFavDesc(int num)
    {
		Cursor cursor = db.query(FAV_DATABASE_TABLE, new String[] {"description"}, 
				"_id like " + num, null, null, null, null);
		
		cursor.moveToFirst();
		return cursor.getString(0);
    }
	
	public String getFavLocation(int num)
    {
		Cursor cursor = db.query(FAV_DATABASE_TABLE, new String[] {"location"}, 
				"_id like " + num, null, null, null, null);
		
		cursor.moveToFirst();
		return cursor.getString(0);
    }
	
	public String getFavLink(int num)
    {
		Cursor cursor = db.query(FAV_DATABASE_TABLE, new String[] {"link"}, 
				"_id like " + num, null, null, null, null);
		
		cursor.moveToFirst();
		return cursor.getString(0);
    }
	//******************** END OF FAVORITES OPERATIONS *********************
	
	
	
	
	//******************** LOCATIONS OPERATIONS *********************
	//queue the items in the database
		public Cursor queueAllLoc()
		{
			String[] columns = new String[] {
					KEY_LOC_ID, 
					KEY_LOC_TITLE,
					KEY_LOC_DESCRIPTION,
					KEY_LOC_LATITUDE,
					KEY_LOC_LONGITUDE};
			Cursor cursor = db.query(FAV_DATABASE_TABLE, columns,
			  null, null, null, null, null);
		
			return cursor;
		}
	
	//getters for the database
	public String getLocTitle(int num)
    {
		Cursor cursor = db.query(LOC_DATABASE_TABLE, new String[] {"title"}, 
				"_id like " + num, null, null, null, null);
			
		cursor.moveToFirst();
		return cursor.getString(0);
	 }
		
	public String getLocDesc(int num)
    {
		Cursor cursor = db.query(LOC_DATABASE_TABLE, new String[] {"description"}, 
				"_id like " + num, null, null, null, null);
			
		cursor.moveToFirst();
		return cursor.getString(0);
	 }
		
	public String getLocLatitiude(int num)
	{
		Cursor cursor = db.query(LOC_DATABASE_TABLE, new String[] {"latitude"}, 
				"_id like " + num, null, null, null, null);
			
		cursor.moveToFirst();
		return cursor.getString(0);
	 }
		
	public String getLocLongitude(int num)
	{
		Cursor cursor = db.query(LOC_DATABASE_TABLE, new String[] {"longitude"}, 
				"_id like " + num, null, null, null, null);
			
		cursor.moveToFirst();
		return cursor.getString(0);
	 }
}