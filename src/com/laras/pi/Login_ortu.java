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
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.ImageButton;

@SuppressLint("NewApi")
public class Login_ortu extends Activity {
	Button login;
	ImageButton kembali;
	EditText user, pass;
	TextView klik_daftar;
	InputStream is=null;
	String result=null;
	String line=null;
	String server, user_send, pass_send, nama_ortu, user_ortu, nama_anak, user_anak, jenis_pet, status_pet, status_makanan, misi_desc, hadiah;
	int pantau, jml_tabungan, misi_gol;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login_ortu);
		
		login = (Button) findViewById(R.id.button2);
		kembali = (ImageButton) findViewById(R.id.imageButton1);
		klik_daftar = (TextView) findViewById(R.id.textView5);
		user = (EditText) findViewById(R.id.editText1);
		pass = (EditText) findViewById(R.id.editText2);
		
		//Atur Custom Font
		Typeface font = Typeface.createFromAsset(getAssets(), "KittenBoldTrial.ttf");
		login.setTypeface(font);
		login.setTextColor(Color.parseColor("#FFFFFF"));
		
		//mengambil nilai dari halaman sebelumnya
		Intent intent = getIntent();
		server = intent.getStringExtra("server");
		
		//aksi untuk klik_daftar
		klik_daftar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				suara_button("click.mp3");
				Intent i = new Intent(getApplicationContext(), Daftar_akun.class);
				i.putExtra("server", server);
				startActivity(i);
				finish();
			}
		});	
		
		//aksi untuk login
		login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				suara_button("click.mp3");
				user_send = user.getText().toString();
				pass_send = pass.getText().toString();
				pantau = 0;
				
				if (user_send.equals("") || pass_send.equals("")){
					Toast.makeText(getApplicationContext(), "Masukkan User dan Password terlebih dahulu",Toast.LENGTH_SHORT).show();
				}else{
					login();
					if (pantau == 0){
						if (nama_ortu == null){
							//jika tidak ada data yg ditemukan
							Toast.makeText(getApplicationContext(), "User / Kata sandi salah ",Toast.LENGTH_SHORT).show(); // <= jika tidak
						}else{
							//jika ada data yg ditemukan
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
					}
				}
			}
		});	
		
		//aksi untuk kembali
		kembali.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				suara_button("click.mp3");
				Intent i = new Intent(getApplicationContext(), Login.class); // <= jika iya
				startActivity(i);
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
	
	@Override
	public void onBackPressed(){
		Intent i = new Intent(getApplicationContext(), Login.class); // <= jika iya
		startActivity(i);
		finish();
	}
	
	public void login(){
    	//mengirimkan variabel ke url
    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    	nameValuePairs.add(new BasicNameValuePair("user", user_send));
    	nameValuePairs.add(new BasicNameValuePair("pass", pass_send));
    	
    	//fungsi untuk memberikan izin pemakaian jaringan
    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    	StrictMode.setThreadPolicy(policy);
    	
    	//mencoba mengakses url melalui httppost
    	try
    	{    		
    		HttpClient httpclient = new DefaultHttpClient();
	        HttpPost httppost = new HttpPost(server+"login_ortu.php"); // <= definisi url di variabel httppost
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); // <= definisi kalo nameValuePairs itu di letakan bersama url di variabel httppost
	        HttpResponse response = httpclient.execute(httppost); // <= mengakses url dengan mengirimkan variabel id_kontak yg disimpan dgn variabel httppost
	        HttpEntity entity = response.getEntity();
	        is = entity.getContent(); // <= mengambil balasan dari file php
	        Log.e("pass 1", "connection success ");
	        pantau = 0;
    	}
        catch(Exception e){
        	Log.e("Fail 1", e.toString());
	    	Toast.makeText(getApplicationContext(), "Gagal menghubungkan ke server",Toast.LENGTH_SHORT).show();
	    	pantau = 1;
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
}
