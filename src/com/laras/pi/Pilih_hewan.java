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
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Pilih_hewan extends Activity {
	ImageButton biru, pink;
	InputStream is=null;
	String result=null;
	String line=null;
	String server, user, nama, jenis_pet, status_pet, misi_desc, hasil, status_makanan, hadiah;
	int jml_tabungan, misi_gol;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_pilih_hewan);
		
		biru = (ImageButton) findViewById(R.id.imageButton1);
		pink = (ImageButton) findViewById(R.id.imageButton2);
		
		Intent intent = getIntent();
		nama = String.valueOf(intent.getStringExtra("nama"));
		jml_tabungan = intent.getIntExtra("jml_tabungan", 0);
		jenis_pet = String.valueOf(intent.getStringExtra("jenis_pet"));
		status_pet = String.valueOf(intent.getStringExtra("status_pet"));
		misi_desc = String.valueOf(intent.getStringExtra("misi_desc"));
		misi_gol = intent.getIntExtra("misi_gol", 0);
		user = String.valueOf(intent.getStringExtra("user"));
		status_makanan = String.valueOf(intent.getStringExtra("status_makanan"));
		server = intent.getStringExtra("server");
		hadiah = String.valueOf(intent.getStringExtra("hadiah"));
		
		//aksi untuk biru
		biru.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				suara_button("click.mp3");
				jenis_pet = "biru";
				pilih_pet();
				cek();
			}
		});
		
		//aksi untuk pink
		pink.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				suara_button("click.mp3");
				jenis_pet = "pink";
				pilih_pet();
				cek();
			}
		});
	}
	
	@Override
	public void onBackPressed(){
		//nothing
	}
	
	public void pilih_pet(){
    	//mengirimkan variabel ke url
    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    	nameValuePairs.add(new BasicNameValuePair("user", user));
    	nameValuePairs.add(new BasicNameValuePair("jenis_pet", jenis_pet));
    	
    	//fungsi untuk memberikan izin pemakaian jaringan
    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    	StrictMode.setThreadPolicy(policy);
    	
    	//mencoba mengakses url melalui httppost
    	try
    	{    		
    		HttpClient httpclient = new DefaultHttpClient();
	        HttpPost httppost = new HttpPost(server+"pilih_hewan.php"); // <= definisi url di variabel httppost
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
        	nama = (json_data.getString("nama"));
        	jml_tabungan = (json_data.getInt("jml_tabungan"));
        	jenis_pet = (json_data.getString("jenis_pet"));
        	status_pet = (json_data.getString("status_pet"));
        	misi_desc = (json_data.getString("misi_desc"));
        	misi_gol = (json_data.getInt("misi_gol"));
        	hasil = (json_data.getString("hasil"));
        	status_makanan = (json_data.getString("status_makanan"));
        	hadiah = (json_data.getString("hadiah"));
    	}
        catch(Exception e){
        	Log.e("Fail 3", e.toString());
    	}
    }
	
	public void cek(){
		if (hasil.equals("sukses")){
			Intent i = new Intent(getApplicationContext(), Hewan_makan.class);
    		i.putExtra("nama", nama);
    		i.putExtra("jml_tabungan", jml_tabungan);
    		i.putExtra("jenis_pet", jenis_pet);
    		i.putExtra("status_pet", status_pet);
    		i.putExtra("misi_desc", misi_desc);
    		i.putExtra("misi_gol", misi_gol);
    		i.putExtra("user", user);
    		i.putExtra("status_makanan", status_makanan);
    		i.putExtra("hadiah", hadiah);
    		i.putExtra("server", server);
			startActivity(i);
			finish();
		}else{
			Toast.makeText(getApplicationContext(), "Kesalahan pada database",Toast.LENGTH_SHORT).show();
		}
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
