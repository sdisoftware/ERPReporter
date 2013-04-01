package com.sdi.erpreporter;

//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
//import java.sql.SQLException;
//import java.sql.Statement;
import java.util.ArrayList;

import android.os.Bundle;
//import android.R;
import android.app.Activity;
import android.app.AlertDialog.Builder;
//import android.app.AlertDialog.Builder;
//import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
//import android.database.*;
//import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class ActSettings extends Activity implements OnClickListener {
	
	
	Button btIslem;
	EditText etIpAdresi, etDatabase, etKullaniciAdi, etSifre;
	TableLayout tlResults;
	ResultSetMetaData metaDataResults;
	ArrayList<String> arrayResults= new ArrayList<String>();
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_act_settings);
		setTitle("ERP Reporter v1.00");
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Kontroller();
		SetButtonText();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		BaslikEkle();
		TabloDoldur();
	}
	
	
	private void Kontroller(){
		
		btIslem=(Button)findViewById(R.id.btIslem);
		etIpAdresi = (EditText)findViewById(R.id.etIpAdresi);
		etKullaniciAdi = (EditText)findViewById(R.id.etKullaniciAdi);
		etDatabase = (EditText)findViewById(R.id.etDatabase);
		etSifre = (EditText)findViewById(R.id.etSifre);
		tlResults = (TableLayout)findViewById(R.id.tblConnectionsResult);

		btIslem.setOnClickListener(this);
	}

	private void SetButtonText(){
		DB db = new DB(this);
		db.open();
		Cursor c = db.Query();
		int count = c.getCount();
		//Toast.makeText(this,"sayý : " + count , Toast.LENGTH_SHORT).show();
		if(count == 0){
			btIslem.setText("Kaydet");
		} else if (count == 4) {
			btIslem.setText("Güncelle");
			SetResults(c);
		}
		db.close();
	
	}
	
	
	private void SetResults(Cursor c){
//		while(c.moveToNext()){
		c.moveToFirst();
		if(c.isFirst() == true){
			
			String ipAdresi = c.getString(c.getColumnIndex("IpAdresi"));
			String veritabaniAdi = c.getString(c.getColumnIndex("VeriTabaniAdi"));
			String kullaniciAdi = c.getString(c.getColumnIndex("KullaniciAdi"));
			String sifre = c.getString(c.getColumnIndex("Sifre"));
			
			etIpAdresi.setText(ipAdresi);
			etDatabase.setText(veritabaniAdi);
			etKullaniciAdi.setText(kullaniciAdi);
			etSifre.setText(sifre);
			//return;
		}
		
	}
	
	@Override
	public void onClick(View v) {
		String ipAdresi =etIpAdresi.getText().toString();
		String veritabaniAdi =etDatabase.getText().toString();
		String kullaniciAdi =etKullaniciAdi.getText().toString();
		String sifre =etSifre.getText().toString();
		
		String btnText = btIslem.getText().toString();
		
		DB db = new DB(this);
		db.open();
		
		boolean insertResult = false, updateResult = false;
		
		if(btnText.equals("Kaydet")){
			insertResult = db.Insert(ipAdresi, veritabaniAdi, kullaniciAdi, sifre);
		}else if(btnText.equals("Güncelle")){
			updateResult = db.Update(ipAdresi, veritabaniAdi, kullaniciAdi, sifre);
		}
		
		if(insertResult){
			Toast.makeText(this, "Veri Eklendi", Toast.LENGTH_SHORT).show();
		}else if (updateResult){
			Toast.makeText(this, "Veri Güncellendi", Toast.LENGTH_SHORT).show();
		}
		
		db.close();
		
	}

	
	private void BaslikEkle(){
		TableRow tr_head = new TableRow(this);
		tr_head.setId(10);
		tr_head.setBackgroundColor(Color.GRAY);
		tr_head.setLayoutParams(new LayoutParams(
		LayoutParams.WRAP_CONTENT,
		LayoutParams.WRAP_CONTENT));
		
		
		TextView labelId = new TextView(this);
		labelId.setId(20);
		labelId.setText("ID");
		labelId.setTextColor(Color.WHITE);
		labelId.setPadding(5, 5, 5, 5);
        tr_head.addView(labelId);// add the column to the table row here

        TextView label_weight_kg = new TextView(this);
        label_weight_kg.setId(21);// define id that must be unique
        label_weight_kg.setText("IP"); // set the text for the header 
        label_weight_kg.setTextColor(Color.WHITE); // set the color
        label_weight_kg.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_head.addView(label_weight_kg); // add the column to the table row here
        
        
        TextView labelData = new TextView(this);
        labelData.setId(21);// define id that must be unique
        labelData.setText("DATA"); // set the text for the header 
        labelData.setTextColor(Color.WHITE); // set the color
        labelData.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_head.addView(labelData); // add the column to the table row here
        
        TextView labelUser = new TextView(this);
        labelUser.setId(21);// define id that must be unique
        labelUser.setText("USER"); // set the text for the header 
        labelUser.setTextColor(Color.WHITE); // set the color
        labelUser.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_head.addView(labelUser); // add the column to the table row here
        
        TextView labelPass = new TextView(this);
        labelPass.setId(21);// define id that must be unique
        labelPass.setText("PASS"); // set the text for the header 
        labelPass.setTextColor(Color.WHITE); // set the color
        labelPass.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_head.addView(labelPass); // add the column to the table row here
        
        try {
            tlResults.addView(tr_head, new TableLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
			
		} catch (Exception e) {
			Toast.makeText(this,e.getMessage() , Toast.LENGTH_LONG).show();
		}

	}
	
	private void TabloDoldur(){
		Integer count=0;
		DB db = new DB(this);
		db.open();
		final Cursor cursor = db.Query();
		
		while (cursor.moveToNext()) {
		
			String id = cursor.getString(0);
			String ip = cursor.getString(1);
			String data = cursor.getString(2);
			String user = cursor.getString(3);
			
			
			TableRow tr = new TableRow(this);
			if(count%2!=0) 
				tr.setBackgroundColor(Color.GRAY);
			else
				tr.setBackgroundColor(Color.BLACK);
			tr.setId(100+count);
			tr.setLayoutParams(new LayoutParams(
			LayoutParams.MATCH_PARENT,
			LayoutParams.WRAP_CONTENT));
	
			
			TextView labelId = new TextView(this);
			labelId.setId(200+count); 
			labelId.setText(id);
			labelId.setPadding(2, 0, 5, 0);
			labelId.setTextColor(Color.WHITE);
			tr.addView(labelId);
			
			TextView labelIp = new TextView(this);
			labelIp.setId(200+count); 
			labelIp.setText(ip);
			labelIp.setPadding(2, 0, 5, 0);
			labelIp.setTextColor(Color.WHITE);
			tr.addView(labelIp);
			
			TextView labelData = new TextView(this);
			labelData.setId(200+count); 
			labelData.setText(data);
			labelData.setPadding(2, 0, 5, 0);
			labelData.setTextColor(Color.WHITE);
			tr.addView(labelData);
			
			TextView labelUser = new TextView(this);
			labelUser.setId(200+count); 
			labelUser.setText(user);
			labelUser.setPadding(2, 0, 5, 0);
			labelUser.setTextColor(Color.WHITE);
			tr.addView(labelUser);
			
			TextView labelPass = new TextView(this);
			labelPass.setId(200+count); 
			labelPass.setText("*****");
			labelPass.setPadding(2, 0, 5, 0);
			labelPass.setTextColor(Color.WHITE);
			tr.addView(labelPass);
			// finally add this to the table row
			tlResults.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
			count++;
			tr.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					SatirGoster((TableRow)view);
					PosizyonAyarla(cursor.getPosition());
				}
			});
	    }
		
	}
	protected void PosizyonAyarla(int i) {
		
	}


	private void SatirGoster(TableRow tr) {
		String satir = "";
		int say = tr.getChildCount();
		for (int i = 0; i < say; i++) {
			try {
				TextView cell = (TextView)tr.getChildAt(i);
				String s = cell.getText().toString();
				satir += s + "\r\r";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Builder builder = new Builder(this);
		builder.setMessage(satir).show();
	}

	
}
