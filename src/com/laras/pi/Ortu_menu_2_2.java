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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Ortu_menu_2_2 extends Activity {
	ImageButton kembali;
	Button btn_batalkan_misi, btn_selesai_misi, btn_catatan_tabungan;
	TextView misi_tampil;
	String server, nama_ortu, user_ortu, nama_anak, user_anak, jenis_pet, status_pet, status_makanan, misi_desc, hadiah;
	int jml_tabungan, misi_gol;
	
	InputStream is=null;
	String result=null;
	String line=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ortu_menu_2_2);
		
		kembali = (ImageButton) findViewById(R.id.imageButton1);
		btn_selesai_misi = (Button) findViewById(R.id.button1);
		btn_batalkan_misi = (Button) findViewById(R.id.button2);
		btn_catatan_tabungan = (Button) findViewById(R.id.button3);
		misi_tampil = (TextView) findViewById(R.id.textView1);
		
		//Atur Custom Font
		Typeface font = Typeface.createFromAsset(getAssets(), "KittenBoldTrial.ttf");
		btn_selesai_misi.setTypeface(font);
		btn_selesai_misi.setTextColor(Color.parseColor("#FFFFFF"));
		btn_batalkan_misi.setTypeface(font);
		btn_batalkan_misi.setTextColor(Color.parseColor("#FFFFFF"));
		btn_catatan_tabungan.setTypeface(font);
		btn_catatan_tabungan.setTextColor(Color.parseColor("#FFFFFF"));
		
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
		
		misi_tampil.setText("Anda memberi tugas kepada anak anda untuk menabung hingga \nRp."+misi_gol+" \ndengan hadiah \n"+hadiah);
		
		//aksi untuk kembali
		kembali.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				suara_button("click.mp3");
				Intent i = new Intent(getApplicationContext(), Ortu_menu_2.class);
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
		
		//aksi untuk btn_selesai_misi
		btn_selesai_misi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				suara_button("click.mp3");
				berhasil_misi();
				Intent i = new Intent(getApplicationContext(), Ortu_menu_2.class);
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
				Toast.makeText(getApplicationContext(), "Misi telah ditetapkan berhasil",Toast.LENGTH_SHORT).show();
    			finish();
			}
		});
		
		//aksi untuk btn_batalkan_misi
		btn_batalkan_misi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				suara_button("click.mp3");
				batalkan_misi();
				Intent i = new Intent(getApplicationContext(), Ortu_menu_2.class);
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
				Toast.makeText(getApplicationContext(), "Misi telah dibatalkan",Toast.LENGTH_SHORT).show();
    			finish();
			}
		});
		
		//aksi untuk btn_catatan_tabungan
		btn_catatan_tabungan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				suara_button("click.mp3");
				Intent j = new Intent(getApplicationContext(), Ortu_menu_3_3.class);
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
				j.putExtra("id_misi", 0);
				j.putExtra("dibuka_dari", "ortu_menu_2");
				startActivity(j);
    			finish();
			}
		});
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
	
	public void batalkan_misi(){
    	//mengirimkan variabel ke url
    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    	nameValuePairs.add(new BasicNameValuePair("user_anak", user_anak));
    	nameValuePairs.add(new BasicNameValuePair("misi_gol", String.valueOf(misi_gol)));
    	nameValuePairs.add(new BasicNameValuePair("hadiah", hadiah));
    	nameValuePairs.add(new BasicNameValuePair("status", "Gagal"));
    	
    	//fungsi untuk memberikan izin pemakaian jaringan
    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    	StrictMode.setThreadPolicy(policy);
    	
    	//mencoba mengakses url melalui httppost
    	try
    	{    		
    		HttpClient httpclient = new DefaultHttpClient();
	        HttpPost httppost = new HttpPost(server+"misi_berakhir.php"); // <= definisi url di variabel httppost
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); // <= definisi kalo nameValuePairs itu di letakan bersama url di variabel httppost
	        HttpResponse response = httpclient.execute(httppost); // <= mengakses url dengan mengirimkan variabel id_kontak yg disimpan dgn variabel httppost
	        HttpEntity entity = response.getEntity();
	        is = entity.getContent(); // <= mengambil balasan dari file php
	        Log.e("pass 1", "connection success ");
    	}
        catch(Exception e){
        	Log.e("Fail 1", e.toString());
	    	Toast.makeText(getApplicationContext(), "Gagal menghubungkan ke server",Toast.LENGTH_SHORT).show();
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
        	Toast.makeText(getApplicationContext(), "gagal hapus misi "+e.toString(),Toast.LENGTH_SHORT).show();
    	}
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
	
	public void berhasil_misi(){
    	//mengirimkan variabel ke url
    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    	nameValuePairs.add(new BasicNameValuePair("user_anak", user_anak));
    	nameValuePairs.add(new BasicNameValuePair("misi_gol", String.valueOf(misi_gol)));
    	nameValuePairs.add(new BasicNameValuePair("hadiah", hadiah));
    	nameValuePairs.add(new BasicNameValuePair("status", "Berhasil"));
    	
    	//fungsi untuk memberikan izin pemakaian jaringan
    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    	StrictMode.setThreadPolicy(policy);
    	
    	//mencoba mengakses url melalui httppost
    	try
    	{    		
    		HttpClient httpclient = new DefaultHttpClient();
	        HttpPost httppost = new HttpPost(server+"misi_berakhir.php"); // <= definisi url di variabel httppost
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); // <= definisi kalo nameValuePairs itu di letakan bersama url di variabel httppost
	        HttpResponse response = httpclient.execute(httppost); // <= mengakses url dengan mengirimkan variabel id_kontak yg disimpan dgn variabel httppost
	        HttpEntity entity = response.getEntity();
	        is = entity.getContent(); // <= mengambil balasan dari file php
	        Log.e("pass 1", "connection success ");
    	}
        catch(Exception e){
        	Log.e("Fail 1", e.toString());
	    	Toast.makeText(getApplicationContext(), "Gagal menghubungkan ke server",Toast.LENGTH_SHORT).show();
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
        	Toast.makeText(getApplicationContext(), "gagal hapus misi "+e.toString(),Toast.LENGTH_SHORT).show();
    	}
    }
}
