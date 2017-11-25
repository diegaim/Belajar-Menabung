package com.laras.pi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Hadiah extends Activity {
	String server, user, nama, jml_tabungan, jenis_pet, status_pet, misi_desc, misi_gol, status_makanan, hadiah;
	ImageView hadiah_view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_hadiah);
		
		hadiah_view = (ImageView) findViewById(R.id.imageView1);
		
		//mengambil data yg dikirim dari halaman sebelumnya
		Intent intent = getIntent();
		nama = String.valueOf(intent.getStringExtra("nama"));
		jml_tabungan = String.valueOf(intent.getIntExtra("jml_tabungan", 0));
		jenis_pet = String.valueOf(intent.getStringExtra("jenis_pet"));
		status_pet = String.valueOf(intent.getStringExtra("status_pet"));
		misi_desc = String.valueOf(intent.getStringExtra("misi_desc"));
		misi_gol = String.valueOf(intent.getIntExtra("misi_gol", 0));
		user = String.valueOf(intent.getStringExtra("user"));
		status_makanan = String.valueOf(intent.getStringExtra("status_makanan"));
		hadiah = String.valueOf(intent.getStringExtra("hadiah"));
		server = intent.getStringExtra("server");
		
		RelativeLayout rlayout = (RelativeLayout) findViewById(R.id.mainlayout);
		rlayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Tekan tombol kembali/home untuk keluar",Toast.LENGTH_SHORT).show();
		    }
		 });
		
		hadiah_view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				suara_button("click.mp3");
				alert();
		    }
		 });
		
		
	}
	
	@Override
	public void onBackPressed(){
		finish();
	}
	
	public void alert(){
		new AlertDialog.Builder(this)
	    .setTitle("Hadiah")
	    .setMessage("Kamu mendapatkan "+hadiah+"!")
	    .setNeutralButton("Tutup", new DialogInterface.OnClickListener() {
	     
	     @Override
	     public void onClick(DialogInterface dlg, int sumthin) {
	    	 // TODO Auto-generated method stub
	    	 suara_button("click.mp3");
	     }
	    })
	    .show();
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
