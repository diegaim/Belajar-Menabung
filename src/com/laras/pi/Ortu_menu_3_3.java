package com.laras.pi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ActionBar.LayoutParams;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Ortu_menu_3_3 extends Activity {
	ImageButton btn_kembali;
	String server, nama_ortu, user_ortu, nama_anak, user_anak, jenis_pet, status_pet, status_makanan, misi_desc, hadiah, dibuka_dari;
	int misi_gol, id_misi, jml_tabungan;
	TableLayout tabelBiodata;
	
	InputStream is=null;
	String result=null;
	String line=null;
	
	String hasil = null;
	StringBuilder sb = null;
	
	String id;
	String[] nama;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ortu_menu_3_3);
		
		tabelBiodata = (TableLayout) findViewById(R.id.tableBiodata);
		btn_kembali = (ImageButton) findViewById(R.id.imageButton1);
		
		//mengambil data yg dikirim dari halaman sebelumnya
		Intent intent = getIntent();
		server = intent.getStringExtra("server");
		nama_ortu = intent.getStringExtra("nama_ortu");
		user_ortu = intent.getStringExtra("user_ortu");
		nama_anak = intent.getStringExtra("nama_anak");
		user_anak = intent.getStringExtra("user_anak");
		jenis_pet = intent.getStringExtra("jenis_pet");
		status_pet = intent.getStringExtra("status_pet");
		status_makanan = intent.getStringExtra("status_makanan");
		misi_desc = intent.getStringExtra("misi_desc");
		hadiah = intent.getStringExtra("hadiah");
		dibuka_dari = intent.getStringExtra("dibuka_dari");
		jml_tabungan = intent.getIntExtra("jml_tabungan", 0);
		misi_gol = intent.getIntExtra("misi_gol", 0);
		id_misi = intent.getIntExtra("id_misi", 0);
		
		//Membuat layout table
				TableRow barisTabel = new TableRow(this);
		        barisTabel.setBackgroundColor(Color.parseColor("#44916c"));

		        TextView viewHeaderTgl = new TextView(this);
		        TextView viewHeaderNominal = new TextView(this);
		        TextView viewHeaderSaldo = new TextView(this);

		        viewHeaderTgl.setText(" Tanggal"); viewHeaderTgl.setTextColor(Color.parseColor("#FFFFFF"));
		        viewHeaderNominal.setText("Jumlah"); viewHeaderNominal.setTextColor(Color.parseColor("#FFFFFF"));
		        viewHeaderSaldo.setText("Total tabungan"); viewHeaderSaldo.setTextColor(Color.parseColor("#FFFFFF"));

		        viewHeaderTgl.setPadding(5, 10, 15, 20);
		        viewHeaderNominal.setPadding(5, 10, 15, 20);
		        viewHeaderSaldo.setPadding(5, 10, 15, 20);

		        barisTabel.addView(viewHeaderTgl);
		        barisTabel.addView(viewHeaderNominal);
		        barisTabel.addView(viewHeaderSaldo);

		        tabelBiodata.addView(barisTabel, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		        //################################################################################################
		        //################################################################################################
		        //################################################################################################
		        //memuat dari php
		        //################################################################################################
		        //################################################################################################
		        //################################################################################################
		        //################################################################################################
		        
		        //mengirimkan variabel ke url
		    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		    	nameValuePairs.add(new BasicNameValuePair("id_misi", String.valueOf(id_misi)));
		    	
			    //fungsi untuk memberikan izin pemakaian jaringan
			    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			    StrictMode.setThreadPolicy(policy);
			    
			    //mencoba mengakses url melalui httppost
			    try{
			        HttpClient httpclient = new DefaultHttpClient();
			        HttpPost httppost = new HttpPost(server+"hist_tabung.php"); // <= definisi url di variabel httppost
			        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); // <= definisi kalo nameValuePairs itu di letakan bersama url di variabel httppost
			        HttpResponse response = httpclient.execute(httppost); // <= mengakses url dengan mengirimkan variabel id_kontak yg disimpan dgn variabel httppost
			        HttpEntity entity = response.getEntity();
			        is = entity.getContent(); // <= mengambil balasan dari file php
			        Log.e("pass 1", "connection success ");
			        
			    }catch(Exception e){
			    	
			    }
			    
			    //membaca keluaran dari php
		        try{        	
		         	BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8); // <= membaca file balasan dengan standar iso
		            StringBuilder sb = new StringBuilder();
		            while ((line = reader.readLine()) != null){
		       		    sb.append(line + "\n");
		           	}
		            is.close();
		            result = sb.toString();
			        Log.e("pass 2", "connection success ");
			        
		        }
		        catch(Exception e){
		        	Log.e("Fail 2", e.toString());
		        	
		        }
		       
		        //memasukkan hasil keluaran dari php ke JSON
		        JSONArray jArray;
		        try{
		        	//JSONObject json_data = new JSONObject(result);
		        	// menyimpan nilai dari tipe user
		        	jArray = new JSONArray(result);
		            JSONObject jObject = null;
	            	for(int i = 0; i<jArray.length(); i++){
			            jObject = jArray.getJSONObject(i);
			            
			            barisTabel = new TableRow(this);
			    		
			            TextView viewTanggal = new TextView(this);
			            viewTanggal.setText(jObject.getString("tanggal"));
			            viewTanggal.setPadding(5, 10, 15, 20);
		                barisTabel.addView(viewTanggal);

		                TextView viewNominal = new TextView(this);
		                viewNominal.setText("Rp."+jObject.getString("nominal"));
		                viewNominal.setPadding(5, 10, 15, 20);
		                barisTabel.addView(viewNominal);
		                
		                TextView viewSaldo = new TextView(this);
		                viewSaldo.setText("Rp."+jObject.getString("saldo_akhir"));
		                viewSaldo.setPadding(5, 10, 15, 20);
		                barisTabel.addView(viewSaldo);
		                
		                tabelBiodata.addView(barisTabel, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));		            	
	            	}
		        }catch(Exception e){
		        	//Toast.makeText(getApplicationContext(), "Terjadi kesalahan : \n"+e.toString(), Toast.LENGTH_SHORT).show();
		        	Toast.makeText(getApplicationContext(), "Data Kosong", Toast.LENGTH_LONG).show();
		    	}
				
		        //################################################################################################
		        //################################################################################################
		        //################################################################################################
		        //################################################################################################
		        
		        //aksi untuk btn_kembali
		        btn_kembali.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						suara_button("click.mp3");
						if (dibuka_dari.equals("ortu_menu_3")){
							Intent j = new Intent(getApplicationContext(), Ortu_menu_3.class);
							j.putExtra("nama_ortu", nama_ortu);
							j.putExtra("user_ortu", user_ortu);
							j.putExtra("nama_anak", nama_anak);
							j.putExtra("user_anak", user_anak);
							j.putExtra("jml_tabungan", jml_tabungan);
							j.putExtra("jenis_pet", jenis_pet);
							j.putExtra("status_pet", status_pet);
							j.putExtra("status_makanan", status_makanan);
							j.putExtra("misi_desc", misi_desc);
							j.putExtra("misi_gol", misi_gol);
							j.putExtra("hadiah", hadiah);
							j.putExtra("server", server);
							startActivity(j);
						}else{
							Intent j = new Intent(getApplicationContext(), Ortu_menu_2_2.class);
							j.putExtra("nama_ortu", nama_ortu);
							j.putExtra("user_ortu", user_ortu);
							j.putExtra("nama_anak", nama_anak);
							j.putExtra("user_anak", user_anak);
							j.putExtra("jml_tabungan", jml_tabungan);
							j.putExtra("jenis_pet", jenis_pet);
							j.putExtra("status_pet", status_pet);
							j.putExtra("status_makanan", status_makanan);
							j.putExtra("misi_desc", misi_desc);
							j.putExtra("misi_gol", misi_gol);
							j.putExtra("hadiah", hadiah);
							j.putExtra("server", server);
							startActivity(j);
						}
		    			finish();
					}
				});	
	}
	
	@Override
	public void onBackPressed(){
		//nothing
		keluar_alert();
	}
	
	public void keluar_alert(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
			    this);
			 
			   // set title
			   alertDialogBuilder.setTitle("Konfirmasi");
			 
			   // set dialog message
			   alertDialogBuilder
			    .setMessage("Yakin ingin keluar?")
			    .setCancelable(false)
			    .setPositiveButton("Iya",new DialogInterface.OnClickListener() {
				     public void onClick(DialogInterface dialog,int id) {
				    	 //if this button is clicked, close
				    	 // current activity
				    	 finish();
				     }
			     })
			    .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
				     public void onClick(DialogInterface dialog,int id) {
				    	 // if this button is clicked, just close
				    	 // the dialog box and do nothing
				    	 dialog.cancel();
				     }
			    });
			 
			    // create alert dialog
			    AlertDialog alertDialog = alertDialogBuilder.create();
			 
			    // show it
			    alertDialog.show();
	}
	
	public void suara_button(String soundfile) {
		MediaPlayer m;
		try {
			m = new MediaPlayer();
			// AssetManager mngr = getAssets();
			if (m.isPlaying()) {
				m.stop();
				m.release();
				m = new MediaPlayer();
			}

			// AssetFileDescriptor afd = getAssets().openFd(fileName);
			AssetFileDescriptor descriptor = getAssets().openFd(soundfile);
			m.setDataSource(descriptor.getFileDescriptor(),
					descriptor.getStartOffset(), descriptor.getLength());
			descriptor.close();

			m.prepare();
			m.setVolume(1f, 1f);
			// m.setLooping(true);
			m.start();
		} catch (Exception e) {
		}
	}
}
