package com.laras.pi;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

public class Login extends Activity {
	TextView txt_pilih_login, txt_ortu, txt_anak;
	ImageButton btn_ortu, btn_anak;
	String server;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		
		btn_ortu = (ImageButton) findViewById(R.id.imageButton1);
		btn_anak = (ImageButton) findViewById(R.id.imageButton2);
		txt_pilih_login = (TextView) findViewById(R.id.textView1);
		txt_ortu = (TextView) findViewById(R.id.textView2);
		txt_anak = (TextView) findViewById(R.id.textView3);
		
		//Atur Custom Font
		Typeface font = Typeface.createFromAsset(getAssets(), "KittenBoldTrial.ttf");
		txt_pilih_login.setTypeface(font);
		txt_ortu.setTypeface(font);
		txt_anak.setTypeface(font);
		
		//set nilai server
		//server = "http://192.168.100.11:90/pilaras/";
		//server = "http://192.168.100.14:90/pilaras/";
		//server = "http://192.168.43.202/laras/";
		server = "http://anastasialaras.esy.es/";
		
		//aksi untuk btn_ortu
		btn_ortu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				suara_button("click.mp3");
				Intent i = new Intent(getApplicationContext(), Login_ortu.class);
				i.putExtra("server", server);
				startActivity(i);
				finish();
				
			}
		});
		
		//aksi untuk btn_anak
		btn_anak.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				suara_button("click.mp3");
				Intent i = new Intent(getApplicationContext(), Login_anak.class);
				i.putExtra("server", server);
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
}
