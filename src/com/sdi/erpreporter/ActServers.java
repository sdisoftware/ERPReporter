package com.sdi.erpreporter;

import java.sql.ResultSetMetaData;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class ActServers extends Activity implements OnClickListener {
	
	
	Button btIslem;
	EditText etIpAdresi, etDatabase, etKullaniciAdi, etSifre, etInstance, etPort, etDesc, etID;
	TableLayout tlResults;
	ResultSetMetaData metaDataResults;
	ArrayList<String> arrayResults= new ArrayList<String>();
	public int aktifDatabase = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_act_servers);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setTitle("Veritabaný Ayarlarý");
		
		Kontroller();
		SetState();
		TabloDoldur();
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // This is called when the Home (Up) button is pressed
	            // in the Action Bar.
	            Intent parentActivityIntent = new Intent(this, ActWelcome.class);
	            parentActivityIntent.addFlags(
	                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
	                    Intent.FLAG_ACTIVITY_NEW_TASK);
	            startActivity(parentActivityIntent);
	            finish();
	            return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	
	
	
	private void Kontroller(){
		
		btIslem=(Button)findViewById(R.id.btIslemx);
		etIpAdresi = (EditText)findViewById(R.id.etIpAdresi);
		etInstance = (EditText)findViewById(R.id.etInstance);
		etDesc = (EditText)findViewById(R.id.etDescription);
		etPort = (EditText)findViewById(R.id.etPort);
		etKullaniciAdi = (EditText)findViewById(R.id.etKullaniciAdi);
		etDatabase = (EditText)findViewById(R.id.etDatabase);
		etSifre = (EditText)findViewById(R.id.etSifre);
		etID = (EditText)findViewById(R.id.etID);
		tlResults = (TableLayout)findViewById(R.id.tblConnectionsResult);
		
		btIslem.setOnClickListener(this);	
	}
	

	private void SetState(){
		try {
			YeDatabase db = new YeDatabase(this);
			db.open();
			
			Cursor c = db.QueryTbDataBasesWhereCmd("ID","InUse", "1" );
			int count = c.getCount();
			if(count == 0){
				btIslem.setText("Kaydet");
			} else {
				btIslem.setText("Güncelle");
			}
			db.close();
			
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		} 
	
	}

	
	@Override
	public void onClick(View v) {
		
		ObDataBase yDb = new ObDataBase(this);

		yDb.ID = Integer.parseInt(etID.getText().toString());
		yDb.Ip =etIpAdresi.getText().toString();
		yDb.Port =etPort.getText().toString();
		yDb.Instance = etInstance.getText().toString();
		yDb.Name =etDatabase.getText().toString();
		yDb.User =etKullaniciAdi.getText().toString();
		yDb.Pass =etSifre.getText().toString();
		yDb.Description =etDesc.getText().toString();
		String btnText = btIslem.getText().toString();
		
		
		YeDatabase db = new YeDatabase(this);
		db.open();
		
		boolean insertResult = false; 
		boolean updateResult = false;
		
		if(btnText.equals("Kaydet")){
			insertResult = db.InsertTbDataBases(yDb);
		}else if(btnText.equals("Güncelle")){
			updateResult = db.UpdateTbDataBases(yDb);
		}
		
		if(insertResult){
			Toast.makeText(this, "Veri Eklendi", Toast.LENGTH_SHORT).show();
		}else if (updateResult){
			Toast.makeText(this, "Veri Güncellendi", Toast.LENGTH_SHORT).show();
		}
		
		db.close();
		
	}

	
	private void TabloDoldur(){
	try {
		
		Integer count=0;
		YeDatabase db = new YeDatabase(this);
		final ObDataBase yDb = new ObDataBase(this);
		db.open();
		final Cursor cursor = db.QueryTbDataBases() ;
		tlResults.removeAllViews();
		while (cursor.moveToNext()) {
		
		
			yDb.ID = cursor.getInt(0);
			yDb.Name = cursor.getString(1);
			yDb.Instance = cursor.getString(2);
			yDb.Ip = cursor.getString(3);
			yDb.Port = cursor.getString(4);
			yDb.User = cursor.getString(5);
			yDb.Pass = cursor.getString(6);
			yDb.InUse = cursor.getInt(7);
			yDb.Description= cursor.getString(8);
			
			
			LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			TableRow tr = (TableRow)inflater.inflate(R.layout.satir, null);		
			
			tr.setId(100+count);
			tr.setLayoutParams(new LayoutParams(
			LayoutParams.MATCH_PARENT,
			LayoutParams.WRAP_CONTENT));
			
			TextView labelDesc= new TextView(this,null,R.style.BodyText);
			labelDesc.setId(200+count); 
			labelDesc.setText(yDb.Description);
			tr.addView(labelDesc);
			
			TextView labelIp = new TextView(this,null,R.style.BodyText);
			labelIp.setId(200+count); 
			labelIp.setText(yDb.Ip);
			tr.addView(labelIp);
			
			TextView labelName = new TextView(this);
			labelName.setId(200+count); 
			labelName.setText(yDb.Name);
			tr.addView(labelName);
			
			RadioButton labelInUse = new RadioButton(this);
			labelInUse.setId(200+count);
			labelInUse.setTag(200+"");
			labelInUse.setGravity(Gravity.CENTER_VERTICAL);
			labelInUse.setGravity(Gravity.CENTER_HORIZONTAL);
			labelInUse.setEnabled(false);
			
			//rdgActiveData.addView(labelInUse);
			if(yDb.InUse == 1)
				labelInUse.setChecked(true);
			else
				labelInUse.setChecked(false);
				
			tr.addView(labelInUse);
			
			tlResults.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
			count++;
			
			tr.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					DetayDoldur((TableRow)view);
				}
			});
			tr.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					SetAktifVeriTabani((TableRow)v);
					return true;
				}
			});
			
	    }
	} catch (Exception e) {
		Toast.makeText(this, "detay göster" + e.getMessage(), Toast.LENGTH_LONG).show();
	}
	}
	
	public void ShowMessage(String message, int tip){
		if(tip == 0)
			Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
	private void SetAktifVeriTabani(TableRow ndb){
	try 
	{
		//View vi = ndb.getChildAt(0);
		TextView x = (TextView)ndb.getChildAt(0);
		
		YeDatabase yDbUpdate = new YeDatabase(this);
		yDbUpdate.open();
		Cursor crx = yDbUpdate.QueryTbDataBases();
		ObDataBase db = new ObDataBase(this);
		while (crx.moveToNext()) 
		{
			
			if(x.getText().equals(crx.getString(8)))
			{
				db.ID = crx.getInt(0);
				db.Name = crx.getString(1);
				db.Instance = crx.getString(2);
				db.Ip = crx.getString(3);
				db.Port = crx.getString(4);
				db.User = crx.getString(5);
				db.Pass = crx.getString(6);
				db.InUse = 1;
				db.Description= crx.getString(8);
			}
			else
			{
				db.ID = crx.getInt(0);
				db.Name = crx.getString(1);
				db.Instance = crx.getString(2);
				db.Ip = crx.getString(3);
				db.Port = crx.getString(4);
				db.User = crx.getString(5);
				db.Pass = crx.getString(6);
				db.InUse = 0;
				db.Description= crx.getString(8);
			}
				yDbUpdate.UpdateTbDataBases(db);
		}

		yDbUpdate.close();
		TabloDoldur();
		ShowMessage(db.Description + " Aktive edildi..", 0);
	} catch (Exception e) 
	{
		Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
	}
				

	}

	
	
	private void DetayDoldur(TableRow tr) {

		ObDataBase db = new ObDataBase(this);
		TextView x = (TextView)tr.getChildAt(0);
		YeDatabase yDbUpdate = new YeDatabase(this);
		yDbUpdate.open();
		Cursor crx = yDbUpdate.QueryTbDataBases();
		while (crx.moveToNext()) 
		{
			if(x.getText().equals(crx.getString(8)))
			{
				db.ID = crx.getInt(0);
				db.Name = crx.getString(1);
				db.Instance = crx.getString(2);
				db.Ip = crx.getString(3);
				db.Port = crx.getString(4);
				db.User = crx.getString(5);
				db.Pass = crx.getString(6);
				db.InUse = 1;
				db.Description= crx.getString(8);
			}
		}
		etID.setText(db.ID +"");
		etDatabase.setText(db.Name.toString());
		etDesc.setText(db.Description.toString());
		etInstance.setText(db.Instance.toString());
		etIpAdresi.setText(db.Ip.toString());
		etKullaniciAdi.setText(db.User.toString());
		etPort.setText(db.Port.toString());
		etSifre.setText(db.Pass.toString());
		
	}
}
