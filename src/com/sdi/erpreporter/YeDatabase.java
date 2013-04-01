package com.sdi.erpreporter;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public class YeDatabase {

	
	static final String dbName="SdiReporter";
	static final int 	dbVersion = 1;
	
	static final String tbDatabase="Databases";
	static final String clDbDataBaseID="ID";
	static final String clDbDataBaseName="Name";
	static final String clDbDataBaseInstance="Instance";
	static final String clDbDataBaseIp="Ip";
	static final String clDbDataBasePort="Port";
	static final String clDbDataBaseUser="User";
	static final String clDbDataBasePass="Pass";
	static final String clDbDataBaseInUse="InUse";
	static final String clDbDataDescription="Description";
	
	
	
	static final String tbQuery="Query";
	static final String clQuID="ID";
	static final String clQuDataBaseID="DataBaseID";
	static final String clQuQuery="Name";
	static final String clQuDataDescription="Description";
	static final String clQuDataType="Type";
	
	
	
	private static final String scrTbDataBasesCreate =
			"CREATE TABLE "+tbDatabase+" (" +
				  	clDbDataBaseID			+ " INTEGER PRIMARY KEY , "+
				  	clDbDataBaseName 		+ " TEXT," +
					clDbDataBaseInstance 	+ " TEXT," +
					clDbDataBaseIp 			+ " TEXT," +
					clDbDataBasePort 		+ " TEXT," +
					clDbDataBaseUser 		+ " TEXT," +
					clDbDataBasePass 		+ " TEXT," +
					clDbDataBaseInUse 		+ " INTEGER," +
					clDbDataDescription 	+ " TEXT)"
			;
	
	private static final String scrTbQueryCreate =
			"CREATE TABLE "+tbQuery+" (" +
				  	clQuID					+ " INTEGER PRIMARY KEY , "+
				  	clQuDataBaseID  		+ " INTEGER," +
				  	clQuQuery			 	+ " TEXT," +
				  	clQuDataType 			+ " INTEGER," +
				  	clQuDataDescription 	+ " TEXT)" 
						  ;
	private static final String scrFkQueryDatabase =
				"CREATE TRIGGER fk_QueryDatabase " +
			    " BEFORE INSERT "+
			    " ON "+tbQuery+
			
			    " FOR EACH ROW BEGIN"+
			    " SELECT CASE WHEN ((SELECT "+clDbDataBaseID+" FROM "+tbQuery+"" + 
				" WHERE " + clDbDataBaseID + "=new."+clQuDataBaseID+" ) IS NULL)"+
			    " THEN RAISE (ABORT,'Foreign Key Violation') END;"+
			    " END;";
	
	
	private DbHelper helper;
	private Context context;
	private SQLiteDatabase database;
	
			
	private static class DbHelper extends SQLiteOpenHelper {
		public DbHelper(Context context){
			super(context, dbName, null, dbVersion);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			  db.execSQL(scrTbDataBasesCreate);
			  db.execSQL(scrTbQueryCreate);
			  db.execSQL(scrFkQueryDatabase);
		}		

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			  db.execSQL("DROP TABLE IF EXISTS " + tbDatabase);
			  db.execSQL("DROP TABLE IF EXISTS " + tbQuery);
			  db.execSQL("DROP TRIGGER IF EXISTS fk_QueryDatabase");
			  onCreate(db);
		}
	}

	
	public YeDatabase(Context context){
		this.context=context;
	}
	
	public YeDatabase open(){
		helper = new YeDatabase.DbHelper(context);
		database = helper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		helper.close();
	}	

	
	
	
	public boolean InsertTbDataBases(ObDataBase yDb)
	{
		try 
		{
			ContentValues cv = new ContentValues();
	
			cv.put(clDbDataBaseName,yDb.Name);
			cv.put(clDbDataBaseInstance,yDb.Instance);
			cv.put(clDbDataBaseIp,yDb.Ip);
			cv.put(clDbDataBasePort,yDb.Port);
			cv.put(clDbDataBaseUser,yDb.User);
			cv.put(clDbDataBasePass,yDb.Pass);
			cv.put(clDbDataBaseInUse,yDb.InUse);
			cv.put(clDbDataDescription,yDb.Description);

			database.insert(tbDatabase, null, cv);
			return true; 
		} 
		catch (Exception e) {
			return false;
		}
	}
	
	
	public boolean UpdateTbDataBases(ObDataBase yDb)
	{
		try 
		{
			ContentValues cv = new ContentValues();
			cv.put(clDbDataBaseName,yDb.Name);
			cv.put(clDbDataBaseInstance,yDb.Instance);
			cv.put(clDbDataBaseIp,yDb.Ip);
			cv.put(clDbDataBasePort,yDb.Port);
			cv.put(clDbDataBaseUser,yDb.User);
			cv.put(clDbDataBasePass,yDb.Pass);
			cv.put(clDbDataBaseInUse,yDb.InUse);
			cv.put(clDbDataDescription,yDb.Description);

			database.update(tbDatabase, cv, clDbDataBaseID + "=?", new String[] { yDb.ID + "" } );
			return true; 
		} 
		catch (Exception e) {
			return false;
		}
	}
	
	
	
	
	public boolean  DeleteTbDataBases(ObDataBase yDb)
 	{
		try 
		{
			database.delete(tbDatabase,clDbDataBaseID+"=?", new String [] {String.valueOf(yDb.ID)});
			database.close();
			return true; 
		} 
		catch (Exception e) {
			ShowMessage(e.getMessage(), 0);
			return false;
		}
		   
  	}
	
	public Cursor QueryTbDataBases(){
		Cursor c = database.query(tbDatabase, 
			new String[] {
							clDbDataBaseID,
							clDbDataBaseName,
							clDbDataBaseInstance,
							clDbDataBaseIp,
							clDbDataBasePort,
							clDbDataBaseUser,
							clDbDataBasePass,
							clDbDataBaseInUse,
							clDbDataDescription
							}, null, null, null, null, null);
 							
	return c;
	}


	public Cursor QueryTbDataBasesWhereCmd(String select, String whereColumn, String whereClause){
		Cursor c = database.query(true, tbDatabase, new String[] { select } , whereColumn+"=?",new String[] { whereClause}, null, null, null, null, null);
	return c;
	}	
	
	public void ShowMessage(String message, int tip){
		if(tip == 0)
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}
	
}



