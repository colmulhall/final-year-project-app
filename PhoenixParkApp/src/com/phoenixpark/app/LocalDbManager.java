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
	public static final String FAV_DATABASE_TABLE = "favorites_local";
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
	
	// create database table for locations
	private static final String CREATE_DATABASE_LOCATIONS =
			"create table " + LOC_DATABASE_TABLE + " ("
			+ KEY_LOC_ID + " integer primary key autoincrement, "
			+ KEY_LOC_TITLE + " text not null,"
			+ KEY_LOC_DESCRIPTION + " integer ,"
			+ KEY_LOC_LATITUDE + " text ,"
			+ KEY_LOC_LONGITUDE + " text);";
	
	// places to eat database
	public static final String KEY_FOOD_ID = "_id";
	public static final String KEY_FOOD_TITLE = "title";
	public static final String KEY_FOOD_LATITUDE = "latitude";
	public static final String KEY_FOOD_LONGITUDE = "longitude";
		
	public static final String FOOD_DATABASE_NAME = "Park food database";
	public static final String FOOD_DATABASE_TABLE = "food_local";
	public static final int FOOD_DATABASE_VERSION = 1;
		
	// create database table for restaurants
	private static final String CREATE_DATABASE_FOOD =
			"create table " + FOOD_DATABASE_TABLE + " ("
			+ KEY_FOOD_ID + " integer primary key autoincrement, "
			+ KEY_FOOD_TITLE + " text not null,"
			+ KEY_FOOD_LATITUDE + " text ,"
			+ KEY_FOOD_LONGITUDE + " text);";
	
	// car parks database
	public static final String KEY_PARKING_ID = "_id";
	public static final String KEY_PARKING_TITLE = "title";
	public static final String KEY_PARKING_LATITUDE = "latitude";
	public static final String KEY_PARKING_LONGITUDE = "longitude";
			
	public static final String PARKING_DATABASE_NAME = "Car parking database";
	public static final String PARKING_DATABASE_TABLE = "parking_local";
	public static final int PARKING_DATABASE_VERSION = 1;
			
	//create database table for car parks
	private static final String CREATE_DATABASE_PARKING =
			"create table " + PARKING_DATABASE_TABLE + " ("
			+ KEY_PARKING_ID + " integer primary key autoincrement, "
			+ KEY_PARKING_TITLE + " text not null,"
			+ KEY_PARKING_LATITUDE + " text ,"
			+ KEY_PARKING_LONGITUDE + " text);";
	
	
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
			 db.execSQL(CREATE_DATABASE_FOOD);
			 db.execSQL(CREATE_DATABASE_PARKING);
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
	public LocalDbManager openLocsToRead() throws SQLException 
	{
		DBHelper = new DBHelper(context, LOC_DATABASE_TABLE, null, LOC_DATABASE_VERSION);
		db = DBHelper.getReadableDatabase();
		return this;
	}
	
	public LocalDbManager openLocsToWrite() throws SQLException 
	{
		DBHelper = new DBHelper(context, LOC_DATABASE_TABLE, null, LOC_DATABASE_VERSION);
		db = DBHelper.getWritableDatabase();
		return this;
	}
	
	//insert all locations
	public void insertLocations()
	{
		String row1 = "INSERT INTO " + LOC_DATABASE_TABLE + " ("
	              + KEY_LOC_ID + ", " + KEY_LOC_TITLE + ", "
	              + KEY_LOC_DESCRIPTION + ", " + KEY_LOC_LATITUDE + ", "
	              + KEY_LOC_LONGITUDE + ") Values ('1','�ras an Uachtar�in', '-', '53.359772', '-6.317010');";
		String row2 = "INSERT INTO " + LOC_DATABASE_TABLE + " ("
	              + KEY_LOC_ID + ", " + KEY_LOC_TITLE + ", "
	              + KEY_LOC_DESCRIPTION + ", " + KEY_LOC_LATITUDE + ", "
	              + KEY_LOC_LONGITUDE + ") Values ('2','Dublin Zoo', '-', '53.355663', '-6.304805');";
		String row3 = "INSERT INTO " + LOC_DATABASE_TABLE + " ("
	              + KEY_LOC_ID + ", " + KEY_LOC_TITLE + ", "
	              + KEY_LOC_DESCRIPTION + ", " + KEY_LOC_LATITUDE + ", "
	              + KEY_LOC_LONGITUDE + ") Values ('3','Magazine Fort', '-', '53.348747', '-6.316204');";
		String row4 = "INSERT INTO " + LOC_DATABASE_TABLE + " ("
	              + KEY_LOC_ID + ", " + KEY_LOC_TITLE + ", "
	              + KEY_LOC_DESCRIPTION + ", " + KEY_LOC_LATITUDE + ", "
	              + KEY_LOC_LONGITUDE + ") Values ('4','Wellington Monument', '-', '53.349052', '-6.303136');";
		String row5 = "INSERT INTO " + LOC_DATABASE_TABLE + " ("
	              + KEY_LOC_ID + ", " + KEY_LOC_TITLE + ", "
	              + KEY_LOC_DESCRIPTION + ", " + KEY_LOC_LATITUDE + ", "
	              + KEY_LOC_LONGITUDE + ") Values ('5','Sports Grounds', '-', '53.350174', '-6.309466');";
		String row6 = "INSERT INTO " + LOC_DATABASE_TABLE + " ("
	              + KEY_LOC_ID + ", " + KEY_LOC_TITLE + ", "
	              + KEY_LOC_DESCRIPTION + ", " + KEY_LOC_LATITUDE + ", "
	              + KEY_LOC_LONGITUDE + ") Values ('6','American Ambassadors Residence', '-', '53.359051', '-6.333670');";
		String row7 = "INSERT INTO " + LOC_DATABASE_TABLE + " ("
	              + KEY_LOC_ID + ", " + KEY_LOC_TITLE + ", "
	              + KEY_LOC_DESCRIPTION + ", " + KEY_LOC_LATITUDE + ", "
	              + KEY_LOC_LONGITUDE + ") Values ('7','The Phoenix Monument', '-', '53.360119', '-6.325827');";
		String row8 = "INSERT INTO " + LOC_DATABASE_TABLE + " ("
	              + KEY_LOC_ID + ", " + KEY_LOC_TITLE + ", "
	              + KEY_LOC_DESCRIPTION + ", " + KEY_LOC_LATITUDE + ", "
	              + KEY_LOC_LONGITUDE + ") Values ('8','Papal Cross', '-', '53.356628', '-6.329111');";
		String row9 = "INSERT INTO " + LOC_DATABASE_TABLE + " ("
	              + KEY_LOC_ID + ", " + KEY_LOC_TITLE + ", "
	              + KEY_LOC_DESCRIPTION + ", " + KEY_LOC_LATITUDE + ", "
	              + KEY_LOC_LONGITUDE + ") Values ('9','Visitor Centre', '-', '53.366055', '-6.331740');";
		String row10 = "INSERT INTO " + LOC_DATABASE_TABLE + " ("
	              + KEY_LOC_ID + ", " + KEY_LOC_TITLE + ", "
	              + KEY_LOC_DESCRIPTION + ", " + KEY_LOC_LATITUDE + ", "
	              + KEY_LOC_LONGITUDE + ") Values ('10','St Marys Hospital', '-', '53.351345', '-6.336785');";
		String row11 = "INSERT INTO " + LOC_DATABASE_TABLE + " ("
	              + KEY_LOC_ID + ", " + KEY_LOC_TITLE + ", "
	              + KEY_LOC_DESCRIPTION + ", " + KEY_LOC_LATITUDE + ", "
	              + KEY_LOC_LONGITUDE + ") Values ('11','Farmleigh', '-', '53.365472', '-6.359487');";
		String row12 = "INSERT INTO " + LOC_DATABASE_TABLE + " ("
	              + KEY_LOC_ID + ", " + KEY_LOC_TITLE + ", "
	              + KEY_LOC_DESCRIPTION + ", " + KEY_LOC_LATITUDE + ", "
	              + KEY_LOC_LONGITUDE + ") Values ('12','Main Gate', '-', '53.348401', '-6.296831');";
		String row13 = "INSERT INTO " + LOC_DATABASE_TABLE + " ("
	              + KEY_LOC_ID + ", " + KEY_LOC_TITLE + ", "
	              + KEY_LOC_DESCRIPTION + ", " + KEY_LOC_LATITUDE + ", "
	              + KEY_LOC_LONGITUDE + ") Values ('13','NCR Gate', '-', '53.348401', '-6.296831');";
		String row14 = "INSERT INTO " + LOC_DATABASE_TABLE + " ("
	              + KEY_LOC_ID + ", " + KEY_LOC_TITLE + ", "
	              + KEY_LOC_DESCRIPTION + ", " + KEY_LOC_LATITUDE + ", "
	              + KEY_LOC_LONGITUDE + ") Values ('14','Cabra Gate', '-', '53.361618', '-6.311034');";
		String row15 = "INSERT INTO " + LOC_DATABASE_TABLE + " ("
	              + KEY_LOC_ID + ", " + KEY_LOC_TITLE + ", "
	              + KEY_LOC_DESCRIPTION + ", " + KEY_LOC_LATITUDE + ", "
	              + KEY_LOC_LONGITUDE + ") Values ('15','Ashtown Gate', '-', '53.370850', '-6.333568');";
		String row16 = "INSERT INTO " + LOC_DATABASE_TABLE + " ("
	              + KEY_LOC_ID + ", " + KEY_LOC_TITLE + ", "
	              + KEY_LOC_DESCRIPTION + ", " + KEY_LOC_LATITUDE + ", "
	              + KEY_LOC_LONGITUDE + ") Values ('16','Castleknock Gate', '-', '53.369694', '-6.348618');";
		String row17 = "INSERT INTO " + LOC_DATABASE_TABLE + " ("
	              + KEY_LOC_ID + ", " + KEY_LOC_TITLE + ", "
	              + KEY_LOC_DESCRIPTION + ", " + KEY_LOC_LATITUDE + ", "
	              + KEY_LOC_LONGITUDE + ") Values ('17','Whites Gate', '-', '53.366189', '-6.351333');";
		String row18 = "INSERT INTO " + LOC_DATABASE_TABLE + " ("
	              + KEY_LOC_ID + ", " + KEY_LOC_TITLE + ", "
	              + KEY_LOC_DESCRIPTION + ", " + KEY_LOC_LATITUDE + ", "
	              + KEY_LOC_LONGITUDE + ") Values ('18','Knockmaroon Gate', '-', '53.357826', '-6.356568');";
		String row19 = "INSERT INTO " + LOC_DATABASE_TABLE + " ("
	              + KEY_LOC_ID + ", " + KEY_LOC_TITLE + ", "
	              + KEY_LOC_DESCRIPTION + ", " + KEY_LOC_LATITUDE + ", "
	              + KEY_LOC_LONGITUDE + ") Values ('19','Chapelizod Gate', '-', '53.346756', '-6.336934');";
		String row20 = "INSERT INTO " + LOC_DATABASE_TABLE + " ("
	              + KEY_LOC_ID + ", " + KEY_LOC_TITLE + ", "
	              + KEY_LOC_DESCRIPTION + ", " + KEY_LOC_LATITUDE + ", "
	              + KEY_LOC_LONGITUDE + ") Values ('20','Islandbridge Gate', '-', '53.348042', '-6.311632');";
		
		db.execSQL(row1);
		db.execSQL(row2);
		db.execSQL(row3);
		db.execSQL(row4);
		db.execSQL(row5);
		db.execSQL(row6);
		db.execSQL(row7);
		db.execSQL(row8);
		db.execSQL(row9);
		db.execSQL(row10);
		db.execSQL(row11);
		db.execSQL(row12);
		db.execSQL(row13);
		db.execSQL(row14);
		db.execSQL(row15);
		db.execSQL(row16);
		db.execSQL(row17);
		db.execSQL(row18);
		db.execSQL(row19);
		db.execSQL(row20);
	}
	
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
		
	public String getLocLatitude(String location)
	{
		Cursor c = db.rawQuery("SELECT latitude FROM " +LOC_DATABASE_TABLE+ " where title="+"'"+location+"'" , null);

		String ret = null;
		if(c != null && c.moveToNext()) 
		{
	        ret = c.getString(0);     
	    }
		return ret;
	 }
		
	public String getLocLongitude(String location)
	{
		Cursor c = db.rawQuery("SELECT longitude FROM " +LOC_DATABASE_TABLE+ " where title="+"'"+location+"'" , null);

		String ret = null;
		if(c != null && c.moveToNext()) 
		{
	        ret = c.getString(0);     
	    }
		return ret;
	 }
	
	//******************** FOOD DATABASE OPERATIONS *********************
	public LocalDbManager openFoodToRead() throws SQLException 
	{
		DBHelper = new DBHelper(context, FOOD_DATABASE_TABLE, null, FOOD_DATABASE_VERSION);
		db = DBHelper.getReadableDatabase();
		return this;
	}
		
	public LocalDbManager openFoodToWrite() throws SQLException 
	{
		DBHelper = new DBHelper(context, FOOD_DATABASE_TABLE, null, FOOD_DATABASE_VERSION);
		db = DBHelper.getWritableDatabase();
		return this;
	}
		
	//insert all restaurants
	public void insertFood()
	{
		String row1 = "INSERT INTO " + FOOD_DATABASE_TABLE + " ("
	              + KEY_FOOD_ID + ", " + KEY_FOOD_TITLE + ", "
	              + KEY_FOOD_LATITUDE + ", "
	              + KEY_FOOD_LONGITUDE + ") Values ('1','The Phoenix Cafe', '53.365775', '-6.330487');";
		String row2 = "INSERT INTO " + FOOD_DATABASE_TABLE + " ("
	              + KEY_FOOD_ID + ", " + KEY_FOOD_TITLE + ", "
	              + KEY_FOOD_LATITUDE + ", "
	              + KEY_FOOD_LONGITUDE + ") Values ('2','Phoenix Park Tea Rooms', '53.352100', '-6.304636');";
		String row3 = "INSERT INTO " + FOOD_DATABASE_TABLE + " ("
	              + KEY_FOOD_ID + ", " + KEY_FOOD_TITLE + ", "
	              + KEY_FOOD_LATITUDE + ", "
	              + KEY_FOOD_LONGITUDE + ") Values ('3','The Boathouse Cafe', '53.365447', '-6.357915');";
			
			db.execSQL(row1);
			db.execSQL(row2);
			db.execSQL(row3);
	}
		
	//queue the items in the database
	public Cursor queueAllFood()
	{
		String[] columns = new String[] {
				KEY_FOOD_ID, 
				KEY_FOOD_TITLE,
				KEY_FOOD_LATITUDE,
				KEY_FOOD_LONGITUDE};
		Cursor cursor = db.query(FOOD_DATABASE_TABLE, columns,
		  null, null, null, null, null);
	
		return cursor;
	}
		
	//getters for the database
	public String getFoodTitle(int num)
    {
		Cursor cursor = db.query(FOOD_DATABASE_TABLE, new String[] {"title"}, 
				"_id like " + num, null, null, null, null);
			
		cursor.moveToFirst();
		return cursor.getString(0);
	}
			
	public String getFoodLatitude(String location)
	{
		Cursor c = db.rawQuery("SELECT latitude FROM " +FOOD_DATABASE_TABLE+ " where title="+"'"+location+"'" , null);

		String ret = null;
		if(c != null && c.moveToNext()) 
		{
			ret = c.getString(0);     
		}
		return ret;
	}
			
	public String getFoodLongitude(String location)
	{
		Cursor c = db.rawQuery("SELECT longitude FROM " +FOOD_DATABASE_TABLE+ " where title="+"'"+location+"'" , null);

		String ret = null;
		if(c != null && c.moveToNext()) 
		{
	        ret = c.getString(0);     
	    }
		return ret;
     }
	//********************************************************************************************
	
	
	//******************** CAR PARK DATABASE OPERATIONS *********************
	public LocalDbManager openParkingToRead() throws SQLException 
	{
		DBHelper = new DBHelper(context, PARKING_DATABASE_TABLE, null, PARKING_DATABASE_VERSION);
		db = DBHelper.getReadableDatabase();
		return this;
	}
			
	public LocalDbManager openParkingToWrite() throws SQLException 
	{
		DBHelper = new DBHelper(context, PARKING_DATABASE_TABLE, null, PARKING_DATABASE_VERSION);
		db = DBHelper.getWritableDatabase();
		return this;
	}
			
	//insert all car parks
	public void insertPark()
	{
		String row1 = "INSERT INTO " + PARKING_DATABASE_TABLE + " ("
	              + KEY_PARKING_ID + ", " + KEY_PARKING_TITLE + ", "
	              + KEY_PARKING_LATITUDE + ", "
	              + KEY_PARKING_LONGITUDE + ") Values ('1','Cricket Club Parking', '53.351389', '-6.307311');";
		String row2 = "INSERT INTO " + PARKING_DATABASE_TABLE + " ("
	              + KEY_PARKING_ID + ", " + KEY_PARKING_TITLE + ", "
	              + KEY_PARKING_LATITUDE + ", "
	              + KEY_PARKING_LONGITUDE + ") Values ('2','Dublin Zoo Parking', '53.354565', '-6.307697');";
		String row3 = "INSERT INTO " + PARKING_DATABASE_TABLE + " ("
		          + KEY_PARKING_ID + ", " + KEY_PARKING_TITLE + ", "
	              + KEY_PARKING_LATITUDE + ", "
                  + KEY_PARKING_LONGITUDE + ") Values ('3','Papal Cross Car Park', '53.357059', '-6.326762');";
		String row4 = "INSERT INTO " + PARKING_DATABASE_TABLE + " ("
		          + KEY_PARKING_ID + ", " + KEY_PARKING_TITLE + ", "
	              + KEY_PARKING_LATITUDE + ", "
                  + KEY_PARKING_LONGITUDE + ") Values ('4','Visitor Centre Car Park', '53.364441', '-6.331743');";
		String row5 = "INSERT INTO " + PARKING_DATABASE_TABLE + " ("
		          + KEY_PARKING_ID + ", " + KEY_PARKING_TITLE + ", "
	              + KEY_PARKING_LATITUDE + ", "
	              + KEY_PARKING_LONGITUDE + ") Values ('5','Martins Row Car Park', '53.351216', '-6.346315');";
				
		db.execSQL(row1);
		db.execSQL(row2);
		db.execSQL(row3);
		db.execSQL(row4);
		db.execSQL(row5);
	}
			
	//queue the items in the database
	public Cursor queueAllParking()
	{
		String[] columns = new String[] {
				KEY_PARKING_ID, 
				KEY_PARKING_TITLE,
				KEY_PARKING_LATITUDE,
				KEY_PARKING_LONGITUDE};
		Cursor cursor = db.query(PARKING_DATABASE_TABLE, columns,
		  null, null, null, null, null);
		
		return cursor;
	}
			
	//getters for the database
	public String getParkingTitle(int num)
	{
		Cursor cursor = db.query(PARKING_DATABASE_TABLE, new String[] {"title"}, 
				"_id like " + num, null, null, null, null);
				
		cursor.moveToFirst();
		return cursor.getString(0);
	}
				
	public String getParkingLatitude(String location)
	{
		Cursor c = db.rawQuery("SELECT latitude FROM " +PARKING_DATABASE_TABLE+ " where title="+"'"+location+"'" , null);

		String ret = null;
		if(c != null && c.moveToNext()) 
		{
			ret = c.getString(0);     
		}
		return ret;
	}
				
	public String getParkingLongitude(String location)
	{
		Cursor c = db.rawQuery("SELECT longitude FROM " +PARKING_DATABASE_TABLE+ " where title="+"'"+location+"'" , null);

		String ret = null;
		if(c != null && c.moveToNext()) 
		{
	        ret = c.getString(0);     
	    }
		return ret;
	}
}