package com.sdi.erpreporter;

//import android.R.color;
import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import android.app.AlertDialog.Builder;
//import android.app.AlertDialog;
import android.content.Context;
//import android.content.DialogInterface;
//import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
//import android.text.style.BulletSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

//commit test 19:33
public class ActQuery extends Activity implements OnClickListener {

	 
	Button btSorgula,btTablo;
	EditText etSorgu;
	
	TableLayout tl;
	
	ResultSetMetaData metaData;
	TableRow renkTableRow;
	
	int kolonsayisi = 0, tvId = 0, otoID = 1;
	String url,driver,userName, password;
	
	ArrayList<String> arrayTablolar= new ArrayList<String>();
	ArrayList<String> arrayResults= new ArrayList<String>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.activity_act_query);
	    
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
      	StrictMode.setThreadPolicy(policy);
	    
	    Kontroller();
	    Ayarlar();
	}

	private void Ayarlar(){
	try{
			YeDatabase db = new YeDatabase(this);
			
			db.open();
			Cursor c = db.QueryTbDataBasesWhereCmd("*", "INUSE", "1");
			ObDataBase activeDb = new ObDataBase(this);
			
			
			while (c.moveToNext()) 
			{
				activeDb.ID = c.getInt(0);
				activeDb.Name = c.getString(1);
				activeDb.Instance = c.getString(2);
				activeDb.Ip = c.getString(3);
				activeDb.Port = c.getString(4);
				activeDb.User = c.getString(5);
				activeDb.Pass = c.getString(6);
				activeDb.InUse = c.getInt(7);
				activeDb.Description = c.getString(8);
			}
			
			String ipAdresi = null, veriTabaniAdi = null, kullaniciAdi = null, sifre = null;
			
			String portString = "";
			String instanceString = "";
			String instance = activeDb.Instance;	
			String port = activeDb.Port;
			veriTabaniAdi = activeDb.Name;
			kullaniciAdi = activeDb.User;
			sifre = activeDb.Pass;
			ipAdresi = activeDb.Ip;
			
			userName = activeDb.User;
			password = activeDb.Pass;
			
			
			if (instance != "")
				instanceString = "instance=" + instance + ";"; 
			else
				instanceString = "";

			if (!port.isEmpty())
				portString = ":" + port + ""; 
			else
				portString = "";
	
			
			url="jdbc:jtds:sqlserver://" + ipAdresi + portString+ "/" + veriTabaniAdi + ";" + instanceString ;
			driver = "net.sourceforge.jtds.jdbc.Driver";		
			db.close();
			Toast.makeText(this, veriTabaniAdi + " Veritabanýna Baðlantý baþarýlý", Toast.LENGTH_SHORT).show();
		}
		catch(Exception e)
		{
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	
	private void Kontroller(){
		btSorgula = (Button)findViewById(R.id.btnExecute);
		btTablo = (Button)findViewById(R.id.btnTablolar);
		etSorgu = (EditText)findViewById(R.id.etQuery);
		tl = (TableLayout)findViewById(R.id.tblResult);
		
		btSorgula.setOnClickListener(this);
		btTablo.setOnClickListener(this);
		
	}
	
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnExecute:
			String sorgu = etSorgu.getText().toString();
			Sorgula(sorgu);
			break;
		case R.id.btnTablolar:
			break;
		}
		
	}
	
	
	private boolean NetworkKontrol(){
		ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		if(manager.getActiveNetworkInfo() != null
				&& manager.getActiveNetworkInfo().isAvailable() 
				&& manager.getActiveNetworkInfo().isConnected()){
			return true;
		}else{
				return false;
			}
		}
	
	private ResultSet TabloSorgula(String sorgu){
		
		ResultSet results = null;
		try {
			Class.forName(driver).newInstance();
			Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.1.16:1433/KIMTEKS_V2;instance=YE;",userName,password);
			Statement statement = conn.createStatement();
			results = statement.executeQuery(sorgu);
		} catch (Exception e) {
			Toast.makeText(this, "Hata : " + e.toString(), Toast.LENGTH_SHORT).show();
		}
		return results;
	}
	
	private void Sorgula (String sorgu){

		try {

			if(!NetworkKontrol()){
				Toast.makeText(this, "Network Baðlantýsýný Kontrol Ediniz!!", Toast.LENGTH_SHORT).show();
				return;
			}
			
			ResultSet results = TabloSorgula(sorgu);

			metaData = results.getMetaData();
			kolonsayisi = metaData.getColumnCount();

			otoID = 1;

			arrayResults.clear();
			tl.removeAllViews();
			
			BasliklariEkle();
			SatirlariEkle(results);
			
		} catch (Exception e) {
			Toast.makeText(this, "Hata : " + e.toString(), Toast.LENGTH_SHORT).show();
		}
	}
	
	private void BasliklariEkle(){
		try {
			TableRow rowHeader = new TableRow(this);
			TextView ID = new TextView(this);
			ID.setText("Key ");
			rowHeader.addView(ID);
			
			for (int i = 1; i < kolonsayisi + 1; i++) {
				TextView tv = new TextView(this);
				String cName;
				
				cName = metaData.getColumnName(i);
				
				arrayResults.add(cName);
				if (cName.length() > 7) {
					cName = cName.substring(0,7) + ".";
				}
				
				tv.setText(cName);
				tv.setId(tvId);
				rowHeader.addView(tv);
				tvId++;
			}
			tl.addView(rowHeader);
		} catch (Exception e) {
			Toast.makeText(this, "Hata : " + e.toString(), Toast.LENGTH_SHORT).show();
		}
	}
	
	private void SatirlariEkle(ResultSet results){
		try {
			int satirSayisi = 0;
			int rowColor = Color.BLACK;
			
			while (results.next()){
				TableRow row = new TableRow(this);
				row.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						SatirGoster(((TableRow) view).getId());
						RenkAyarla(view);
						view.setBackgroundColor(Color.rgb(245, 92, 44));					
					}
				});
				TextView tvID = new TextView(this);
				tvID.setText(String.valueOf(otoID));
				row.setId(otoID);
				row.addView(tvID);
				
				if(rowColor == Color.GRAY){
					rowColor = Color.DKGRAY;
					row.setBackgroundColor(rowColor);
				}
				else{
					rowColor = Color.GRAY;
					row.setBackgroundColor(rowColor);
				}
				otoID++;
				
				for (int i = 1; i < kolonsayisi + 1; i++) {
					TextView tv = new TextView(this);
					String s = "";
					if(results.getString(i) != null)
						s = results.getString(i).toString()+"";
					else
						s = "null";
					arrayResults.add(s);
					
					if(s.length() > 7){
						s = s.substring(0,7) + ".";
					}
					tv.setText(s + "	");
					tv.setId(tvId);
					row.addView(tv);
					tvId++;
				}
				satirSayisi++;
				tl.addView(row);
			}
			Toast.makeText(this, satirSayisi + "tane satýr listelendi", Toast.LENGTH_SHORT).show();
			
		} catch (Exception e) {
			Toast.makeText(this, "Hata : " + e.toString(), Toast.LENGTH_SHORT).show();
			return;
		}
	}	
	
	private void SatirGoster(int otoId) {
		String satir = "";
		for (int i = 1; i < kolonsayisi + 1; i++) {
			try {
				satir += metaData.getColumnName(i).toString() + "\r\n";
				satir += arrayResults.get(otoId * kolonsayisi - 1 + i).toString()+ "\r\n\r\n";
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		Builder builder = new Builder(this);
		builder.setMessage(satir).show();
	}
	
	private void RenkAyarla(View v) {
		if(renkTableRow != null){
			TableRow tr = renkTableRow;
			if(tr.getId() % 2 == 0)
				tr.setBackgroundColor(Color.GRAY);
			else
				tr.setBackgroundColor(Color.DKGRAY);
		}
	}		
		
	
	
	
}
