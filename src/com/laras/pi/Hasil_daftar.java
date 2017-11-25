package com.laras.pi;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Hasil_daftar extends Activity {
	String server, hasil;
	TextView tampil_hasil;
	Button btn_hasil;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_hasil_daftar);
		
		tampil_hasil = (TextView) findViewById(R.id.textView1);
		btn_hasil = (Button) findViewById(R.id.button1);
		
		Intent intent = getIntent();
		server = intent.getStringExtra("server");
		hasil = intent.getStringExtra("hasil");
		
		if (hasil.equals("berhasil")) {
			tampil_hasil.setText("Akun berhasil dibuat");
			btn_hasil.setText("Login");
		} else {
			tampil_hasil.setText("Akun gagal dibuat");
			btn_hasil.setText("Kembali");
		}
		
		//aksi untuk kembali
		btn_hasil.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				suara_button("click.mp3");
				if (hasil.equals("berhasil")) {
					//login
					Intent i = new Intent(getApplicationContext(), Login.class);
					startActivity(i);
					finish();
				} else {
					//daftar
					Intent i = new Intent(getApplicationContext(), Daftar_akun.class);
					i.putExtra("server", server);
					startActivity(i);
					finish();
				}
						
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
}
