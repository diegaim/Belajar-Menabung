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
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Ortu_menu_2 extends Activity {
	EditText misi_gol_tot, misi_hadiah;
	Button misi_simpan, misi_lihat;
	ImageButton btn_home, btn_hadiah, btn_report, btn_keluar;
	String server, nama_ortu, user_ortu, nama_anak, user_anak, jenis_pet, status_pet, status_makanan, misi_desc, hadiah;
	int jml_tabungan, misi_gol;
	
	InputStream is=null;
	String result=null;
	String line=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ortu_menu_2);
		
		btn_home = (ImageButton) findViewById(R.id.imageButton1);
		btn_hadiah = (ImageButton) findViewById(R.id.imageButton2);
		btn_report = (ImageButton) findViewById(R.id.imageButton3);
		btn_keluar = (ImageButton) findViewById(R.id.imageButton4);
		misi_gol_tot = (EditText) findViewById(R.id.editText1);
		misi_hadiah = (EditText) findViewById(R.id.editText2);
		misi_simpan = (Button) findViewById(R.id.button1);
		misi_lihat = (Button) findViewById(R.id.button2);
		
		//Atur Custom Font
		Typeface font = Typeface.createFromAsset(getAssets(), "KittenBoldTrial.ttf");
		misi_simpan.setTypeface(font);
		misi_simpan.setTextColor(Color.parseColor("#FFFFFF"));
		misi_lihat.setTypeface(font);
		misi_lihat.setTextColor(Color.parseColor("#FFFFFF"));
		
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
		jml_tabungan = intent.getIntExtra("jml_tabungan", 0);
		misi_gol = intent.getIntExtra("misi_gol", 0);
		
		//mengatur muncul atau tidaknya button lihat misi
		if (jml_tabungan == 0 && misi_gol == 0){
			misi_lihat.setVisibility(View.GONE);
		}else{
			misi_lihat.setVisibility(View.VISIBLE);
		}
		
		//aksi untuk btn_home
		btn_home.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				suara_button("click.mp3");
				Intent i = new Intent(getApplicationContext(), Ortu_home.class);
				i.putExtra("nama_ortu", nama_ortu);
				i.putExtra("user_ortu", user_ortu);
				i.putExtra("nama_anak", nama_anak);
				i.putExtra("user_anak", user_anak);
				i.putExtra("jml_tabungan", jml_tabungan);
				i.putExtra("jenis_pet", jenis_pet);
				i.putExtra("status_pet", status_pet);
				i.putExtra("status_makanan", status_makanan);
				i.putExtra("misi_desc", misi_desc);
				i.putExtra("misi_gol", misi_gol);
				i.putExtra("hadiah", hadiah);
				i.putExtra("server", server);
				startActivity(i);
    			finish();
			}
		});
		
		//aksi untuk btn_report
		btn_report.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				suara_button("click.mp3");
				Intent i = new Intent(getApplicationContext(), Ortu_menu_3.class);
				i.putExtra("nama_ortu", nama_ortu);
				i.putExtra("user_ortu", user_ortu);
				i.putExtra("nama_anak", nama_anak);
				i.putExtra("user_anak", user_anak);
				i.putExtra("jml_tabungan", jml_tabungan);
				i.putExtra("jenis_pet", jenis_pet);
				i.putExtra("status_pet", status_pet);
				i.putExtra("status_makanan", status_makanan);
				i.putExtra("misi_desc", misi_desc);
				i.putExtra("misi_gol", misi_gol);
				i.putExtra("hadiah", hadiah);
				i.putExtra("server", server);
				startActivity(i);
    			finish();
			}
		});
		
		//aksi untuk misi_simpan
		misi_simpan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				suara_button("click.mp3");
				if (misi_gol_tot.getText().toString().equals("") || misi_gol_tot.getText().toString().equals("0")){
					Toast.makeText(getApplicationContext(), "Target tabungan tidak boleh kosong",Toast.LENGTH_SHORT).show();
				}else if (misi_hadiah.getText().toString().equals("")){
					Toast.makeText(getApplicationContext(), "Hadiah tidak boleh kosong",Toast.LENGTH_SHORT).show();
				}else{
					if (misi_gol == 0){
						tambah_misi();
						Toast.makeText(getApplicationContext(), "Berhasil menambahkan misi baru",Toast.LENGTH_SHORT).show();
					}else{
						if (jml_tabungan <= misi_gol){ //Untuk cek apakah ada misi yang sedang berjalan
							//kalau ada
							Toast.makeText(getApplicationContext(), "Tidak dapat menyimpan karna ada misi yang sedang dijalankan",Toast.LENGTH_SHORT).show();
						}else{
							//kalau tidak ada
							tambah_misi();
							misi_gol_tot.setText("");
							misi_hadiah.setText("");
							Toast.makeText(getApplicationContext(), "Berhasil menambahkan misi baru",Toast.LENGTH_SHORT).show();
						}
					}
				}
				
				//atur button lihat misi lagi
				if (jml_tabungan == 0 && misi_gol == 0){
					misi_lihat.setVisibility(View.GONE);
				}else{
					misi_lihat.setVisibility(View.VISIBLE);
				}
			}
		});
		
		//aksi untuk misi_lihat
		misi_lihat.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				suara_button("click.mp3");
				Intent i = new Intent(getApplicationContext(), Ortu_menu_2_2.class);
				i.putExtra("nama_ortu", nama_ortu);
				i.putExtra("user_ortu", user_ortu);
				i.putExtra("nama_anak", nama_anak);
				i.putExtra("user_anak", user_anak);
				i.putExtra("jml_tabungan", jml_tabungan);
				i.putExtra("jenis_pet", jenis_pet);
				i.putExtra("status_pet", status_pet);
				i.putExtra("status_makanan", status_makanan);
				i.putExtra("misi_desc", misi_desc);
				i.putExtra("misi_gol", misi_gol);
				i.putExtra("hadiah", hadiah);
				i.putExtra("server", server);
				startActivity(i);
    			finish();
			}
		});
		
		//aksi untuk btn_keluar
		btn_keluar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				suara_button("click.mp3");
				keluar_alert();
			}
		});	
	}
	
	@Override
	public void onBackPressed(){
		//nothing
		keluar_alert();
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
	
	public void tambah_misi(){
		//mengirimkan variabel ke url
	    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    nameValuePairs.add(new BasicNameValuePair("user_anak", user_anak));
	    nameValuePairs.add(new BasicNameValuePair("misi_gol_tot", misi_gol_tot.getText().toString()));
	    nameValuePairs.add(new BasicNameValuePair("misi_hadiah", misi_hadiah.getText().toString()));
	    
	    //fungsi untuk memberikan izin pemakaian jaringan
	    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	    
	    //mencoba mengakses url melalui httppost
	    try{    		
	    	HttpClient httpclient = new DefaultHttpClient();
	        HttpPost httppost = new HttpPost(server+"tambah_misi.php"); // <= definisi url di variabel httppost
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); // <= definisi kalo nameValuePairs itu di letakan bersama url di variabel httppost
	        HttpResponse response = httpclient.execute(httppost); // <= mengakses url dengan mengirimkan variabel id_kontak yg disimpan dgn variabel httppost
	        HttpEntity entity = response.getEntity();
	        is = entity.getContent(); // <= mengambil balasan dari file php
	        Log.e("pass 1", "connection success ");
	    }catch(Exception e){
	      	Log.e("Fail 1", e.toString());
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
        try{
        	JSONObject json_data = new JSONObject(result);
        	// menyimpan nilai dari tipe user
        	nama_ortu = (json_data.getString("nama_ortu"));
        	user_ortu = (json_data.getString("user_ortu"));
        	nama_anak = (json_data.getString("nama_anak"));
        	user_anak = (json_data.getString("user_anak"));
        	jml_tabungan = (json_data.getInt("jml_tabungan"));
        	jenis_pet = (json_data.getString("jenis_pet"));
        	status_pet = (json_data.getString("status_pet"));
        	status_makanan = (json_data.getString("status_makanan"));
        	misi_desc = (json_data.getString("misi_desc"));
        	hadiah = (json_data.getString("hadiah"));
        	misi_gol = (json_data.getInt("misi_gol"));
    	}
        catch(Exception e){
        	Log.e("Fail 3", e.toString());
    	}
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
}
