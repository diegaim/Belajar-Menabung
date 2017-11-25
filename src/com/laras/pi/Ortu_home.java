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
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Ortu_home extends Activity {
	ImageButton btn_home, btn_hadiah, btn_report, btn_keluar;
	Button btn_ingatkan;
	ImageButton btn_tambah_tabungan;
	EditText input_tabungan;
	TextView tampil_tabungan;
	
	InputStream is=null;
	String result=null;
	String line=null;
	String server, nama_ortu, user_ortu, nama_anak, user_anak, jenis_pet, status_pet, status_makanan, misi_desc, hadiah;
	int jml_tabungan, misi_gol;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ortu_home);
		
		btn_home = (ImageButton) findViewById(R.id.imageButton1);
		btn_hadiah = (ImageButton) findViewById(R.id.imageButton2);
		btn_report = (ImageButton) findViewById(R.id.imageButton3);
		btn_keluar = (ImageButton) findViewById(R.id.imageButton4);
		input_tabungan = (EditText) findViewById(R.id.editText1);
		tampil_tabungan = (TextView) findViewById(R.id.textView3);
		btn_tambah_tabungan = (ImageButton) findViewById(R.id.img_btn_add);
		btn_ingatkan = (Button) findViewById(R.id.button1);
		
		//Atur Custom Font
		Typeface font = Typeface.createFromAsset(getAssets(), "KittenBoldTrial.ttf");
		btn_ingatkan.setTypeface(font);
		btn_ingatkan.setTextColor(Color.parseColor("#FFFFFF"));
		
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
		
		//untuk nampilin progress jml tabungan saat ini
		tampil_tabungan.setText("Rp."+jml_tabungan);

		//aksi untuk btn_tambah_tabungan
		btn_tambah_tabungan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				suara_button("click.mp3");
				//tambah tabungan
				if (misi_gol == 0){
					Toast.makeText(getApplicationContext(), "Misi belum diatur, gagal menambahkan saldo",Toast.LENGTH_LONG).show();
				}else{
					tambah_tabungan();					
				}
			}
		});	
		
		//aksi untuk btn_ingatkan
		btn_ingatkan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				suara_button("click.mp3");
				if (misi_gol == 0){
					Toast.makeText(getApplicationContext(), "Misi belum diatur, operasi dibatalkan",Toast.LENGTH_LONG).show();
				}else{
					ingatkan_anak();
				}
			}
		});
		
		//aksi untuk btn_hadiah
		btn_hadiah.setOnClickListener(new View.OnClickListener() {
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
		
		//aksi untuk btn_keluar
		btn_keluar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				suara_button("click.mp3");
				keluar_alert();
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
		//nothing
		keluar_alert();
	}
	
	public void tambah_tabungan(){
		//cek formnya diisi atau engga
		if (input_tabungan.getText().toString().equals("")){
			Toast.makeText(getApplicationContext(), "Saldo yang disetor kosong",Toast.LENGTH_SHORT).show();
		}else{
			//mengirimkan variabel ke url
	    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    	nameValuePairs.add(new BasicNameValuePair("user_ortu", user_ortu));
	    	nameValuePairs.add(new BasicNameValuePair("user_anak", user_anak));
	    	nameValuePairs.add(new BasicNameValuePair("jml_tabung", input_tabungan.getText().toString()));
	    	nameValuePairs.add(new BasicNameValuePair("jml_tambah_tabungan", String.valueOf(Integer.parseInt(input_tabungan.getText().toString()) + jml_tabungan)));
	    	
	    	//fungsi untuk memberikan izins pemakaian jaringan
	    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    	StrictMode.setThreadPolicy(policy);
	    	
	    	//mencoba mengakses url melalui httppost
	    	try
	       	{    		
	       		HttpClient httpclient = new DefaultHttpClient();
	            HttpPost httppost = new HttpPost(server+"tambah_tabungan.php"); // <= definisi url di variabel httppost
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); // <= definisi kalo nameValuePairs itu di letakan bersama url di variabel httppost
	            HttpResponse response = httpclient.execute(httppost); // <= mengakses url dengan mengirimkan variabel id_kontak yg disimpan dgn variabel httppost
	            HttpEntity entity = response.getEntity();
	            is = entity.getContent(); // <= mengambil balasan dari file php
	            Log.e("pass 1", "connection success ");
	            
	            //saat berhasil
	            Toast.makeText(getApplicationContext(), "Tabungan anak telah ditambahkan",Toast.LENGTH_SHORT).show();
	            //untuk nampilin progress jml tabungan saat ini
	            int temp2 = Integer.parseInt(input_tabungan.getText().toString()) + jml_tabungan;
	            jml_tabungan = temp2;
	    		tampil_tabungan.setText("Rp."+ temp2);
	    		input_tabungan.setText("");
	       	}
	        catch(Exception e){
	        	Log.e("Fail 1", e.toString());
	        	Toast.makeText(getApplicationContext(), "Gagal menghubungkan ke server",Toast.LENGTH_SHORT).show();
	        }
		}
    }
	
	public void ingatkan_anak(){
		//mengirimkan variabel ke url
	    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    nameValuePairs.add(new BasicNameValuePair("user_anak", user_anak));
	    
	    //fungsi untuk memberikan izin pemakaian jaringan
	    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	    
	    //mencoba mengakses url melalui httppost
	    try{    		
	    	HttpClient httpclient = new DefaultHttpClient();
	        HttpPost httppost = new HttpPost(server+"ingatkan_anak.php"); // <= definisi url di variabel httppost
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); // <= definisi kalo nameValuePairs itu di letakan bersama url di variabel httppost
	        HttpResponse response = httpclient.execute(httppost); // <= mengakses url dengan mengirimkan variabel id_kontak yg disimpan dgn variabel httppost
	        HttpEntity entity = response.getEntity();
	        is = entity.getContent(); // <= mengambil balasan dari file php
	        Log.e("pass 1", "connection success ");
	            
	        //saat berhasil
	        Toast.makeText(getApplicationContext(), "Berhasil mengingatkan anak",Toast.LENGTH_SHORT).show();
	    }catch(Exception e){
	      	Log.e("Fail 1", e.toString());
	       	Toast.makeText(getApplicationContext(), "Gagal menghubungkan ke server",Toast.LENGTH_SHORT).show();
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