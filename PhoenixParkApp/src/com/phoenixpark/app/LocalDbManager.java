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
	public static final String KEY_COUNTRY = "country";
	public static final String KEY_YEAR = "year";
	public static final String KEY_MONTH = "month";
	public static final String KEY_TRANSPORT = "transport";
	public static final String KEY_DESC = "description";
	
	public static final String DATABASE_NAME = "Countries database";
	public static final String DATABASE_TABLE = "Country";
	public static final int DATABASE_VERSION = 5;
	
	//create database table
	private static final String SCRIPT_CREATE_DATABASE =
			"create table " + DATABASE_TABLE + " ("
			+ KEY_ID + " integer primary key autoincrement, "
			+ KEY_COUNTRY + " text not null,"
			+ KEY_YEAR + " integer ,"
			+ KEY_MONTH + " text ,"
			+ KEY_TRANSPORT + " text ,"
			+ KEY_DESC + " text);";
	
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
	public long insert(String content, int year, String month, String transport, String description)
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put(KEY_COUNTRY, content);
		contentValues.put(KEY_YEAR, year);
		contentValues.put(KEY_MONTH, month);
		contentValues.put(KEY_TRANSPORT, transport);
		contentValues.put(KEY_DESC, description);
		
		return db.insert(DATABASE_TABLE, null, contentValues);
	}
	
	//edit an item in the database
	public boolean update(int id, String content, int year, String month, String transport, String description)
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put(KEY_COUNTRY, content);
		contentValues.put(KEY_YEAR, year);
		contentValues.put(KEY_MONTH, month);
		contentValues.put(KEY_TRANSPORT, transport);
		contentValues.put(KEY_DESC, description);
		
		return db.update(DATABASE_TABLE, contentValues, KEY_ID + " = " + id, null) > 0;
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
	
	//queue the items in the database
	public Cursor queueAll()
	{
		String[] columns = new String[]{
				KEY_ID, 
				KEY_COUNTRY,
				KEY_YEAR,
				KEY_MONTH,
				KEY_TRANSPORT,
				KEY_DESC};
		Cursor cursor = db.query(DATABASE_TABLE, columns,
		  null, null, null, null, null);
	
		return cursor;
	}
	
	//order the list by the year in descending order
	public Cursor orderList()
	{
		String[] columns = new String[]{
				KEY_ID, 
				KEY_COUNTRY,
				KEY_YEAR,
				KEY_MONTH,
				KEY_TRANSPORT,
				KEY_DESC};
		
		Cursor cursor = db.query(DATABASE_TABLE, columns, null, null, null, null, KEY_YEAR + " DESC");
		
		return cursor;
	}
}