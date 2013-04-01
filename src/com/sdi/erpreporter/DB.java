package com.sdi.erpreporter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DB {
	private static final String DB_NAME = "Conn";
	private static final int DB_VERSION = 1;
	private static final String DB_CREATE = "CREATE TABLE Info ("
			+" ID INTEGER PRIMARY KEY AUTOINCREMENT, "
			+" IpAdresi TEXT, VeriTabaniAdi TEXT, "
			+" KullaniciAdi TEXT, Sifre TEXT)";
	
	private static final String DB_DROP = " DROP TABLE IF EXIST Info ";
	
	private DbHelper helper;
	private Context context;
	private SQLiteDatabase database;
					
	
	private static class DbHelper extends SQLiteOpenHelper {
		public DbHelper(Context context){
			super(context, DB_NAME, null, DB_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db){
			db.execSQL(DB_CREATE);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion){
			db.execSQL(DB_DROP);
			onCreate(db);
		}
	}
	
	public DB(Context context){
		this.context=context;
	}
	public DB open(){
		helper = new DB.DbHelper(context);
		database = helper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		helper.close();
	}
	
	public boolean Insert(String ipAdresi,String veritabaniAdi,String kullaniciAdi,String sifre){
		try {
			ContentValues cv = new ContentValues();
			cv.put("IpAdresi", ipAdresi);
			cv.put("VeritabaniAdi", veritabaniAdi);
			cv.put("KullaniciAdi", kullaniciAdi);
			cv.put("Sifre", sifre);
			
			
			database.insert("Info", null, cv);
			return true; 
			
		} catch (Exception e) {
			return false;
		}
	}
	
	
	public boolean Update(String ipAdresi,String veritabaniAdi,String kullaniciAdi,String sifre){
		try {
			ContentValues cv = new ContentValues();
			cv.put("IpAdresi", ipAdresi);
			cv.put("VeritabaniAdi", veritabaniAdi);
			cv.put("KullaniciAdi", kullaniciAdi);
			cv.put("Sifre", sifre);
			
			database.update("Info", cv, "ID=?", new String[] { "1" } );
			//database.update(table, values, whereClause, whereArgs)
			return true; 
			
		} catch (Exception e) {
			return false;
		}
	}
/*
	public boolean Delete(String ipAdresi,String veritabaniAdi,String kullaniciAdi,String sifre, int kullanilan){
		try {
			ContentValues cv = new ContentValues();
			cv.put("IpAdresi", ipAdresi);
			cv.put("VeritabaniAdi", veritabaniAdi);
			cv.put("KullaniciAdi", kullaniciAdi);
			cv.put("Sifre", sifre);
			
			database.delete("Info", "ID=?", new String[] { "1" } );
			return true; 
			
		} catch (Exception e) {
			return false;
		}
	}
*/	
	public Cursor Query(){
		Cursor c = database.query("Info", new String[] {"ID", "IpAdresi","VeritabaniAdi","KullaniciAdi","Sifre"}, null, null, null, null, null);
		return c;
	}
	
	public void Delete(){
		database.delete("Info", null, null);
	}
	
	
	
}







