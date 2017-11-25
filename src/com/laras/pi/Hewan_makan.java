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
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.content.res.AssetFileDescriptor;

@SuppressLint("NewApi")
public class Hewan_makan extends Activity {
	ImageButton makanan, keluar, refresh;
	TextView misi, jml_tab_anak;
	ImageView tampil_gambar;
	ProgressBar pbar;
	InputStream is=null;
	String result=null;
	String line=null;
	String server, user, nama, jenis_pet, status_pet, misi_desc, status_makanan, hadiah;
	int jml_tabungan, misi_gol;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_hewan_makan);
		
		makanan = (ImageButton) findViewById(R.id.imageButton5);
		keluar = (ImageButton) findViewById(R.id.hewan_makan_keluar);
		refresh = (ImageButton) findViewById(R.id.hewan_makan_refresh);
		misi = (TextView) findViewById(R.id.textView2);
		jml_tab_anak = (TextView) findViewById(R.id.textView4);
		tampil_gambar = (ImageView) findViewById(R.id.imageView1);
		pbar = (ProgressBar) findViewById(R.id.progressBar1);
		
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
		
		//mengatur misi
		misi.setText("Menabung hingga Rp."+String.valueOf(misi_gol));
		
		//mengatur progress bar pertama
		cek_tabungan();
		
		//mengatur tombol makanan
		if (status_makanan.equals("kosong")){
			makanan.setVisibility(View.GONE);
		}else{
			makanan.setVisibility(View.VISIBLE);
		}
		
		//mengatur animasi awal
		cek_lapar();
		
		//mengatur progress bar kedua, kenapa 2x? soalnya kalo satu kali progress barnya engga jalan. 
		//gatau juga kenapa bisa begitu
		cek_tabungan();
		
		
		//aksi untuk makanan
		makanan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				suara_button("click.mp3");
				beri_makan();
			}
		});
		
		//aksi untuk keluar
		keluar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				suara_button("click.mp3");
				keluar_alert();
			}
		});
		
		//aksi untuk refresh
		refresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				suara_button("click.mp3");
				refresh();
				
				//atur tombol makanan lagi
				if (status_makanan.equals("kosong")){
					makanan.setVisibility(View.GONE);
				}else{
					makanan.setVisibility(View.VISIBLE);
				}

				//cek misi
				cek_tugas();
				
				//atur ulang progress bar
				cek_tabungan();
				
				//atur animasi lagi
				cek_lapar();
			}
		});
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
	
	@Override
	public void onBackPressed(){
		//nothing
		keluar_alert();
	}
	
	public void animasi_awal(){
		//belum atur animasi kalo laper
		if (jenis_pet.equals("biru")){
			tampil_gambar.setBackgroundResource(R.drawable.animasi_sehat_biru);
		}else{
			tampil_gambar.setBackgroundResource(R.drawable.animasi_sehat_pink);
		}
		final AnimationDrawable frameAnimation = (AnimationDrawable) tampil_gambar.getBackground();
		frameAnimation.start();
	}
	
	public void cek_lapar(){
		if (status_pet.equals("sehat")){
			animasi_awal();
		}else{
			if (jenis_pet.equals("biru")){
				tampil_gambar.setBackgroundResource(R.drawable.animasi_sedih_biru);
			}else{
				tampil_gambar.setBackgroundResource(R.drawable.animasi_sedih_pink);
			}
			final AnimationDrawable frameAnimation = (AnimationDrawable) tampil_gambar.getBackground();
			frameAnimation.start();
		}
	}
	
	public void refresh(){
    	//mengirimkan variabel ke url
    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    	nameValuePairs.add(new BasicNameValuePair("user", user));
    	
    	//fungsi untuk memberikan izin pemakaian jaringan
    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    	StrictMode.setThreadPolicy(policy);
    	
    	//mencoba mengakses url melalui httppost
    	try
    	{    		
    		HttpClient httpclient = new DefaultHttpClient();
	        HttpPost httppost = new HttpPost(server+"refresh.php"); // <= definisi url di variabel httppost
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
        	status_makanan = (json_data.getString("status_makanan"));
        	hadiah = (json_data.getString("hadiah"));
    	}
        catch(Exception e){
        	Log.e("Fail 3", e.toString());
    	}
    }
	
	public void beri_makan(){
    	//mengirimkan variabel ke url
    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    	nameValuePairs.add(new BasicNameValuePair("user", user));
    	
    	//fungsi untuk memberikan izin pemakaian jaringan
    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    	StrictMode.setThreadPolicy(policy);
    	
    	//mencoba mengakses url melalui httppost
    	try
    	{    		
    		HttpClient httpclient = new DefaultHttpClient();
	        HttpPost httppost = new HttpPost(server+"beri_makan.php"); // <= definisi url di variabel httppost
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); // <= definisi kalo nameValuePairs itu di letakan bersama url di variabel httppost
	        HttpResponse response = httpclient.execute(httppost); // <= mengakses url dengan mengirimkan variabel id_kontak yg disimpan dgn variabel httppost
	        HttpEntity entity = response.getEntity();
	        is = entity.getContent(); // <= mengambil balasan dari file php
	        Log.e("pass 1", "connection success ");
	        
	        
	        //bila berhasil tersambung ke php
	        status_makanan = "kosong";
			makanan.setVisibility(View.GONE);
			//atur pergerakan animasi makan
			if (jenis_pet.equals("biru")){
				tampil_gambar.setBackgroundResource(R.drawable.animasi_makan_biru);
			}else{
				tampil_gambar.setBackgroundResource(R.drawable.animasi_makan_pink);
			}
			final AnimationDrawable frameAnimation = (AnimationDrawable) tampil_gambar.getBackground();
			frameAnimation.start();
			//atur animasi ke kenyang
			new CountDownTimer(2500, 1000) {
			    public void onTick(long millisUntilFinished) {
			    }

			    public void onFinish() {
			    	animasi_awal();
			    }
			}.start();
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
        	//gausah nyimpen karna aksi pegubahan atribut status_makanan aja
    	}
        catch(Exception e){
        	Log.e("Fail 3", e.toString());
    	}
    }
	
	public void cek_tugas(){
		if (jml_tabungan !=0 && misi_gol !=0){
			if (jml_tabungan >= misi_gol){
				//ke halaman reward
				Intent i = new Intent(getApplicationContext(), Hadiah.class);
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
			}
		}
	}
	
	public void cek_tabungan(){
		pbar.setScaleY(5f); // <= mengatur besar sudut y progress bar
		pbar.setMax(misi_gol);// <= mengatur nilai saat proses 100%
		pbar.setProgress(jml_tabungan); // <= mengatur proses saat ini
		jml_tab_anak.setText("Rp."+jml_tabungan+" / Rp."+misi_gol);
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
