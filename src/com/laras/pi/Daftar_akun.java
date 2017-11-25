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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Daftar_akun extends Activity {
	EditText nm_ortu, user_ortu, pass_ortu, nm_anak, user_anak, pass_anak;
	Button daftar;
	ImageButton kembali;
	String server;
	InputStream is=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_daftar_akun);
		
		nm_ortu = (EditText) findViewById(R.id.editText1);
		user_ortu = (EditText) findViewById(R.id.editText2);
		pass_ortu = (EditText) findViewById(R.id.editText3);
		nm_anak = (EditText) findViewById(R.id.editText4);
		user_anak = (EditText) findViewById(R.id.editText5);
		pass_anak = (EditText) findViewById(R.id.editText6);
		daftar = (Button) findViewById(R.id.button1);
		kembali = (ImageButton) findViewById(R.id.imageButton1);
		
		Intent intent = getIntent();
		server = intent.getStringExtra("server");
		
		//aksi untuk kembali
		kembali.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				suara_button("click.mp3");
				Intent i = new Intent(getApplicationContext(), Login_ortu.class);
				i.putExtra("server", server);
				startActivity(i);
				finish();
						
			}
		});
		
		//aksi untuk daftar
		daftar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				suara_button("click.mp3");
				if (nm_ortu.getText().toString().equals("") || user_ortu.getText().toString().equals("") || pass_ortu.getText().toString().equals("") || nm_anak.getText().toString().equals("") || user_anak.getText().toString().equals("") || pass_anak.getText().toString().equals("")){
					//Toast ingetin untuk isi semua form
					Toast.makeText(getApplicationContext(), "Isi semua form dengan benar",Toast.LENGTH_SHORT).show();
				}else{
					daftar();
				}
				
			}
		});
	}
	
	public void daftar(){
    	//mengirimkan variabel ke url
    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    	nameValuePairs.add(new BasicNameValuePair("nama_ortu", nm_ortu.getText().toString()));
    	nameValuePairs.add(new BasicNameValuePair("user_ortu", user_ortu.getText().toString()));
    	nameValuePairs.add(new BasicNameValuePair("pass_ortu", pass_ortu.getText().toString()));
    	nameValuePairs.add(new BasicNameValuePair("nama_anak", nm_anak.getText().toString()));
    	nameValuePairs.add(new BasicNameValuePair("user_anak", user_anak.getText().toString()));
    	nameValuePairs.add(new BasicNameValuePair("pass_anak", pass_anak.getText().toString()));
    	
    	//fungsi untuk memberikan izin pemakaian jaringan
    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    	StrictMode.setThreadPolicy(policy);
    	
    	//mencoba mengakses url melalui httppost
    	try
    	{    		
    		HttpClient httpclient = new DefaultHttpClient();
	        HttpPost httppost = new HttpPost(server+"daftar.php"); // <= definisi url di variabel httppost
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); // <= definisi kalo nameValuePairs itu di letakan bersama url di variabel httppost
	        HttpResponse response = httpclient.execute(httppost); // <= mengakses url dengan mengirimkan variabel id_kontak yg disimpan dgn variabel httppost
	        HttpEntity entity = response.getEntity();
	        is = entity.getContent(); // <= mengambil balasan dari file php
	        Log.e("pass 1", "connection success ");
	        
	        Intent i = new Intent(getApplicationContext(), Hasil_daftar.class);
			i.putExtra("hasil", "berhasil");
			i.putExtra("server", server);
			startActivity(i);
			finish();
	     }
        catch(Exception e){
        	Log.e("Fail 1", e.toString());
	    	Toast.makeText(getApplicationContext(), "Gagal menghubungkan ke server",Toast.LENGTH_SHORT).show();
	     
	    	Intent i = new Intent(getApplicationContext(), Hasil_daftar.class);
			i.putExtra("hasil", "gagal");
			i.putExtra("server", server);
			startActivity(i);		 	
        }
        
    }
	
	@Override
	public void onBackPressed(){
		Intent i = new Intent(getApplicationContext(), Login_ortu.class);
		i.putExtra("server", server);
		startActivity(i);
		finish();
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
