package com.phoenixpark.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class LocalDbManager 
{
	public static final String KEY_ID = "_id";
	public static final String KEY_EVENT_TITLE = "title";
	public static final String KEY_EVENT_DESCRIPTION = "description";
	public static final String KEY_EVENT_DATE = "date";
	public static final String KEY_EVENT_LOCATION = "location";
	public static final String KEY_EVENT_LINK = "link";
	
	public static final String DATABASE_NAME = "Event favorites database";
	public static final String DATABASE_TABLE = "event_list_local";
	public static final int DATABASE_VERSION = 1;
	
	//create database table
	private static final String SCRIPT_CREATE_DATABASE =
			"create table " + DATABASE_TABLE + " ("
			+ KEY_ID + " integer primary key autoincrement, "
			+ KEY_EVENT_TITLE + " text not null,"
			+ KEY_EVENT_DESCRIPTION + " integer ,"
			+ KEY_EVENT_DATE + " text ,"
			+ KEY_EVENT_LOCATION + " text ,"
			+ KEY_EVENT_LINK + " text);";
	
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
			 db.execSQL(SCRIPT_CREATE_DATABASE);
		}
		
		//this will be called if I make a change to the database and give it a new version number
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
		{
			onCreate(db);
		}
	}
	//end of helper class ***********************************************************************************
	
	public LocalDbManager openToRead() throws SQLException 
	{
		DBHelper = new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
		db = DBHelper.getReadableDatabase();
		return this;
	}
	
	public LocalDbManager openToWrite() throws SQLException 
	{
		DBHelper = new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
		db = DBHelper.getWritableDatabase();
		return this;
	}
	
	//close the database
	public void close()
	{
		DBHelper.close();
	}
	
	//insert a new item into the database
	public long insert(String title, String desc, String date, String location, String link)
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put(KEY_EVENT_TITLE, title);
		contentValues.put(KEY_EVENT_DESCRIPTION, desc);
		contentValues.put(KEY_EVENT_DATE, date);
		contentValues.put(KEY_EVENT_LOCATION, location);
		contentValues.put(KEY_EVENT_LINK, link);
		
		return db.insert(DATABASE_TABLE, null, contentValues);
	}
	
	//delete everything from the database
	public int deleteAll()
	{
		return db.delete(DATABASE_TABLE, null, null);
	}
	
	//delete the database 
	public void deleteDatabase()
	{
		context.deleteDatabase(DATABASE_NAME);
	}
	
	//delete a particular country, based on its passed in ID
	public void deleteCountry(int num)
	{
		db.delete(DATABASE_TABLE, KEY_ID + " = " + num, null);
	}
	
	//queue the items in the database
	public Cursor queueAll()
	{
		String[] columns = new String[]{
				KEY_ID, 
				KEY_EVENT_TITLE,
				KEY_EVENT_DESCRIPTION,
				KEY_EVENT_DATE,
				KEY_EVENT_LOCATION,
				KEY_EVENT_LINK};
		Cursor cursor = db.query(DATABASE_TABLE, columns,
		  null, null, null, null, null);
	
		return cursor;
	}
	
	//order the list by the year in descending order
	public Cursor orderList()
	{
		String[] columns = new String[]{
				KEY_ID, 
				KEY_EVENT_TITLE,
				KEY_EVENT_DESCRIPTION,
				KEY_EVENT_DATE,
				KEY_EVENT_LOCATION,
				KEY_EVENT_LINK};
		
		Cursor cursor = db.query(DATABASE_TABLE, columns, null, null, null, null, KEY_EVENT_DATE + " DESC");
		
		return cursor;
	}
	
	//getters for the database
	public String getItemTitle(int num)
    {
		Cursor cursor = db.query(DATABASE_TABLE, new String[] {"title"}, 
				"_id like " + num, null, null, null, null);
		
		cursor.moveToFirst();
		return cursor.getString(0);
    }
	
	public String getItemDesc(int num)
    {
		Cursor cursor = db.query(DATABASE_TABLE, new String[] {"description"}, 
				"_id like " + num, null, null, null, null);
		
		cursor.moveToFirst();
		return cursor.getString(0);
    }
}